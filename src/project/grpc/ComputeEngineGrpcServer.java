package project.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import project.ComputationService;
import project.ComputationServiceImpl;
import project.DataStorageService;
import project.MultiThreadedComputeEngineServiceImpl;
import project.model.PrimeFactorizationResponse;
import project.model.StorageDestination;
import project.model.StorageSource;

public class ComputeEngineGrpcServer {

    public static final int DEFAULT_PORT = 50052;

    private final int port;
    private final Server server;

    public ComputeEngineGrpcServer(int port, DataStorageService dataStorageService, ComputationService computationService) {
        this.port = port;
        this.server = ServerBuilder.forPort(port)
                .addService(new ComputeEngineGrpcImpl(dataStorageService, computationService))
                .build();
    }

    public void start() throws IOException {
        server.start();
        System.out.println("ComputeEngineGrpcServer started on port " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                ComputeEngineGrpcServer.this.stop();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }));
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public int getPort() {
        return server.getPort();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        String dataStoreHost = args.length > 1 ? args[1] : "localhost";
        int dataStorePort = args.length > 2 ? Integer.parseInt(args[2]) : DataStoreGrpcServer.DEFAULT_PORT;

        GrpcDataStorageServiceClient dataStoreClient = new GrpcDataStorageServiceClient(dataStoreHost, dataStorePort);
        ComputeEngineGrpcServer server = new ComputeEngineGrpcServer(
                port, dataStoreClient, new ComputationServiceImpl());
        server.start();
        server.blockUntilShutdown();
    }

    static class ComputeEngineGrpcImpl extends ComputeEngineGrpcServiceGrpc.ComputeEngineGrpcServiceImplBase {

        private final DataStorageService dataStorageService;
        private final ComputationService computationService;

        ComputeEngineGrpcImpl(DataStorageService dataStorageService, ComputationService computationService) {
            this.dataStorageService = dataStorageService;
            this.computationService = computationService;
        }

        @Override
        public void compute(ComputeRequest request, StreamObserver<ComputeResponse> responseObserver) {
            try {
                String outputPath = request.getOutputFilePath();
                if (outputPath == null || outputPath.trim().isEmpty()) {
                    sendError(responseObserver, "Output file path is required.");
                    return;
                }

                String delimiter = (request.getDelimiter() == null || request.getDelimiter().isEmpty())
                        ? ","
                        : request.getDelimiter();
                StorageDestination destination = new StorageDestination(outputPath.trim(), delimiter);

                List<Integer> numbers;
                if (request.hasInputFilePath()) {
                    String inputPath = request.getInputFilePath();
                    if (inputPath == null || inputPath.trim().isEmpty()) {
                        sendError(responseObserver, "Input file path cannot be empty.");
                        return;
                    }
                    numbers = dataStorageService.readInput(new StorageSource(inputPath.trim()));
                } else if (request.hasInlineNumbers()) {
                    numbers = new ArrayList<>(request.getInlineNumbers().getNumbersList());
                } else {
                    sendError(responseObserver, "Either an input file path or inline numbers must be provided.");
                    return;
                }

                if (numbers.isEmpty()) {
                    sendError(responseObserver, "No valid integers found to process.");
                    return;
                }

                InlineCoordinator coordinator = new InlineCoordinator(dataStorageService, computationService);
                List<PrimeFactorizationResponse> responses = coordinator.computeBatch(numbers, destination);

                List<String> formatted = formatResponses(responses);
                ComputeResponse response = ComputeResponse.newBuilder()
                        .setSuccess(true)
                        .setStatusMessage("SUCCESS: Processed " + responses.size() + " numbers and wrote output to " + outputPath.trim())
                        .addAllFormattedResults(formatted)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            } catch (Exception e) {
                String msg = e.getMessage() != null ? e.getMessage() : "Unexpected computation error";
                sendError(responseObserver, msg);
            }
        }

        private void sendError(StreamObserver<ComputeResponse> responseObserver, String message) {
            ComputeResponse response = ComputeResponse.newBuilder()
                    .setSuccess(false)
                    .setStatusMessage("FAILED: " + message)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        private List<String> formatResponses(List<PrimeFactorizationResponse> responses) {
            List<String> formatted = new ArrayList<>();
            for (PrimeFactorizationResponse resp : responses) {
                List<Integer> factors = resp.getFactors();
                if (factors.isEmpty()) {
                    formatted.add("none");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < factors.size(); i++) {
                        sb.append(factors.get(i));
                        if (i < factors.size() - 1) {
                            sb.append("*");
                        }
                    }
                    formatted.add(sb.toString());
                }
            }
            return formatted;
        }
    }

    static class InlineCoordinator extends MultiThreadedComputeEngineServiceImpl {

        InlineCoordinator(DataStorageService dataStorageService, ComputationService computationService) {
            super(dataStorageService, computationService);
        }

        List<PrimeFactorizationResponse> computeBatch(List<Integer> inputs, StorageDestination destination) throws Exception {
            List<PrimeFactorizationResponse> responses = processNumbers(inputs);
            dataStorageService.writeOutput(destination, responses);
            return responses;
        }
    }
}
package project.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import project.DataStorageService;
import project.DataStorageServiceImpl;
import project.model.PrimeFactorizationResponse;
import project.model.StorageDestination;
import project.model.StorageSource;

public class DataStoreGrpcServer {

    public static final int DEFAULT_PORT = 50051;

    private final int port;
    private final Server server;

    public DataStoreGrpcServer(int port, DataStorageService delegate) {
        this.port = port;
        this.server = ServerBuilder.forPort(port)
                .addService(new DataStorageGrpcImpl(delegate))
                .build();
    }

    public void start() throws IOException {
        server.start();
        System.out.println("DataStoreGrpcServer started on port " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                DataStoreGrpcServer.this.stop();
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
        DataStoreGrpcServer server = new DataStoreGrpcServer(port, new DataStorageServiceImpl());
        server.start();
        server.blockUntilShutdown();
    }

    static class DataStorageGrpcImpl extends DataStorageGrpcServiceGrpc.DataStorageGrpcServiceImplBase {

        private final DataStorageService delegate;

        DataStorageGrpcImpl(DataStorageService delegate) {
            this.delegate = delegate;
        }

        @Override
        public void readInput(ReadInputRequest request, StreamObserver<ReadInputResponse> responseObserver) {
            try {
                List<Integer> numbers = delegate.readInput(new StorageSource(request.getSourceLocation()));
                ReadInputResponse response = ReadInputResponse.newBuilder()
                        .addAllNumbers(numbers)
                        .setSuccess(true)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            } catch (Exception e) {
                ReadInputResponse response = ReadInputResponse.newBuilder()
                        .setSuccess(false)
                        .setErrorMessage(e.getMessage() != null ? e.getMessage() : "Read failed")
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        }

        @Override
        public void writeOutput(WriteOutputRequest request, StreamObserver<WriteOutputResponse> responseObserver) {
            try {
                List<PrimeFactorizationResponse> results = new ArrayList<>();
                for (FactorList factorList : request.getResultsList()) {
                    results.add(new PrimeFactorizationResponse(factorList.getFactorsList()));
                }
                StorageDestination destination = new StorageDestination(
                        request.getDestinationLocation(), request.getDelimiter());
                delegate.writeOutput(destination, results);
                WriteOutputResponse response = WriteOutputResponse.newBuilder()
                        .setSuccess(true)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            } catch (Exception e) {
                WriteOutputResponse response = WriteOutputResponse.newBuilder()
                        .setSuccess(false)
                        .setErrorMessage(e.getMessage() != null ? e.getMessage() : "Write failed")
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        }
    }
}
package project.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ComputeEngineClient implements AutoCloseable {

    private final ManagedChannel channel;
    private final ComputeEngineGrpcServiceGrpc.ComputeEngineGrpcServiceBlockingStub blockingStub;

    public ComputeEngineClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext().build());
    }

    public ComputeEngineClient(ManagedChannel channel) {
        this.channel = channel;
        this.blockingStub = ComputeEngineGrpcServiceGrpc.newBlockingStub(channel);
    }

    public ComputeResponse submitFileRequest(String inputFilePath, String outputFilePath, String delimiter) {
        ComputeRequest.Builder builder = ComputeRequest.newBuilder()
                .setInputFilePath(inputFilePath)
                .setOutputFilePath(outputFilePath);
        if (delimiter != null && !delimiter.isEmpty()) {
            builder.setDelimiter(delimiter);
        }
        return blockingStub.compute(builder.build());
    }

    public ComputeResponse submitInlineRequest(List<Integer> numbers, String outputFilePath, String delimiter) {
        NumberList numberList = NumberList.newBuilder()
                .addAllNumbers(numbers)
                .build();
        ComputeRequest.Builder builder = ComputeRequest.newBuilder()
                .setInlineNumbers(numberList)
                .setOutputFilePath(outputFilePath);
        if (delimiter != null && !delimiter.isEmpty()) {
            builder.setDelimiter(delimiter);
        }
        return blockingStub.compute(builder.build());
    }

    public static List<Integer> parseNumbers(String rawInput) {
        List<Integer> numbers = new ArrayList<>();
        if (rawInput == null || rawInput.trim().isEmpty()) {
            return numbers;
        }
        String[] tokens = rawInput.trim().split("[,\\s]+");
        for (String token : tokens) {
            if (!token.isEmpty()) {
                numbers.add(Integer.parseInt(token));
            }
        }
        return numbers;
    }

    @Override
    public void close() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = ComputeEngineGrpcServer.DEFAULT_PORT;
        String inputFile = null;
        String inlineRaw = null;
        String outputFile = null;
        String delimiter = ",";

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--host":
                    if (i + 1 < args.length) {
                        host = args[++i];
                    }
                    break;
                case "--port":
                    if (i + 1 < args.length) {
                        port = Integer.parseInt(args[++i]);
                    }
                    break;
                case "--file":
                    if (i + 1 < args.length) {
                        inputFile = args[++i];
                    }
                    break;
                case "--numbers":
                    if (i + 1 < args.length) {
                        inlineRaw = args[++i];
                    }
                    break;
                case "--output":
                    if (i + 1 < args.length) {
                        outputFile = args[++i];
                    }
                    break;
                case "--delimiter":
                    if (i + 1 < args.length) {
                        delimiter = args[++i];
                    }
                    break;
                default:
                    break;
            }
        }

        if (outputFile == null || (inputFile == null && inlineRaw == null)) {
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("=== Prime Factorization Compute Client ===");
                System.out.print("Choose input mode (1 = Input File, 2 = Typed Numbers): ");
                String mode = scanner.nextLine().trim();

                if ("1".equals(mode)) {
                    System.out.print("Enter input file path: ");
                    inputFile = scanner.nextLine().trim();
                } else {
                    System.out.print("Enter numbers (comma or space separated): ");
                    inlineRaw = scanner.nextLine().trim();
                }

                System.out.print("Enter output file path: ");
                outputFile = scanner.nextLine().trim();

                System.out.print("Enter output delimiter (press Enter for default ','): ");
                String customDelimiter = scanner.nextLine();
                if (customDelimiter != null && !customDelimiter.isEmpty()) {
                    delimiter = customDelimiter;
                }
            }
        }

        try (ComputeEngineClient client = new ComputeEngineClient(host, port)) {
            ComputeResponse response;
            if (inputFile != null) {
                response = client.submitFileRequest(inputFile, outputFile, delimiter);
            } else {
                List<Integer> numbers = parseNumbers(inlineRaw);
                response = client.submitInlineRequest(numbers, outputFile, delimiter);
            }

            System.out.println("Task Status: " + (response.getSuccess() ? "SUCCEEDED" : "FAILED"));
            System.out.println("Details: " + response.getStatusMessage());
            if (!response.getFormattedResultsList().isEmpty()) {
                System.out.println("Computed Results: " + String.join(delimiter, response.getFormattedResultsList()));
            }
        }
    }
}
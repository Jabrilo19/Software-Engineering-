package project.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import project.DataStorageService;
import project.model.PrimeFactorizationResponse;
import project.model.StorageDestination;
import project.model.StorageSource;

public class GrpcDataStorageServiceClient implements DataStorageService, AutoCloseable {

    private final ManagedChannel channel;
    private final DataStorageGrpcServiceGrpc.DataStorageGrpcServiceBlockingStub blockingStub;

    public GrpcDataStorageServiceClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext().build());
    }

    public GrpcDataStorageServiceClient(ManagedChannel channel) {
        this.channel = channel;
        this.blockingStub = DataStorageGrpcServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public List<Integer> readInput(StorageSource source) {
        if (source == null || source.getLocation() == null) {
            return Collections.emptyList();
        }
        try {
            ReadInputRequest request = ReadInputRequest.newBuilder()
                    .setSourceLocation(source.getLocation())
                    .build();
            ReadInputResponse response = blockingStub.readInput(request);
            return new ArrayList<>(response.getNumbersList());
        } catch (Exception expected) {
            return Collections.emptyList();
        }
    }

    @Override
    public void writeOutput(StorageDestination destination, List<PrimeFactorizationResponse> results) {
        if (destination == null || destination.getLocation() == null || results == null) {
            return;
        }
        try {
            WriteOutputRequest.Builder requestBuilder = WriteOutputRequest.newBuilder()
                    .setDestinationLocation(destination.getLocation())
                    .setDelimiter(destination.getDelimiter() != null ? destination.getDelimiter() : ",");

            for (PrimeFactorizationResponse response : results) {
                FactorList.Builder factorListBuilder = FactorList.newBuilder();
                if (response != null && response.getFactors() != null) {
                    factorListBuilder.addAllFactors(response.getFactors());
                }
                requestBuilder.addResults(factorListBuilder.build());
            }

            blockingStub.writeOutput(requestBuilder.build());
        } catch (Exception expected) {
        }
    }

    @Override
    public void close() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
}
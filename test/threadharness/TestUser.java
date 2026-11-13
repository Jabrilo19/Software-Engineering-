package threadharness;

import java.io.File;
import project.ComputeEngineService;
import project.model.NetworkDataRequest;
import project.model.StorageDestination;
import project.model.StorageSource;

public class TestUser {

    private final ComputeEngineService coordinator;

    public TestUser(ComputeEngineService coordinator) {
        this.coordinator = coordinator;
    }

    public void run(String outputPath) {
        char delimiter = ';';
        String inputPath = "test" + File.separatorChar + "testInputFile.test";

        StorageSource source = new StorageSource(inputPath);
        StorageDestination destination = new StorageDestination(outputPath);
        NetworkDataRequest request = new NetworkDataRequest(inputPath, String.valueOf(delimiter));

        coordinator.compute(source, destination, request);
    }
}
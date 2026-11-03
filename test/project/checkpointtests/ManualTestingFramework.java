package project.checkpointtests;

import project.ComputationService;
import project.ComputationServiceImpl;
import project.ComputeEngineService;
import project.ComputeEngineServiceImpl;
import project.DataStorageService;
import project.DataStorageServiceImpl;
import project.model.NetworkDataRequest;
import project.model.StorageDestination;
import project.model.StorageSource;

public class ManualTestingFramework {
    
    public static final String INPUT = "manualTestInput.txt";
    public static final String OUTPUT = "manualTestOutput.txt";

    public static void main(String[] args) {
        DataStorageService dataStorageService = new DataStorageServiceImpl();
        ComputationService computationService = new ComputationServiceImpl();
        ComputeEngineService computeEngineService = new ComputeEngineServiceImpl(dataStorageService, computationService);

        StorageSource source = new StorageSource(INPUT);
        StorageDestination destination = new StorageDestination(OUTPUT);
        NetworkDataRequest request = new NetworkDataRequest(INPUT, ",");

        computeEngineService.compute(source, destination, request);
    }
}

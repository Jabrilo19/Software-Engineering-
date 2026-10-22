package project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import project.model.PrimeFactorizationResponse;
import project.model.StorageDestination;
import project.model.StorageSource;

public class InMemoryDataStorageService implements DataStorageService {

    private final List<Integer> inputData;
    private final List<PrimeFactorizationResponse> outputData;

    public InMemoryDataStorageService(List<Integer> inputData) {
        this.inputData = new ArrayList<>(inputData);
        this.outputData = new ArrayList<>();
    }

    public InMemoryDataStorageService() {
        this(Collections.emptyList());
    }

    @Override
    public List<Integer> readInput(StorageSource source) {
        return Collections.unmodifiableList(inputData);
    }

    @Override
    public void writeOutput(StorageDestination destination, List<PrimeFactorizationResponse> results) {
        if (results != null) {
            outputData.addAll(results);
        }
    }

    public List<PrimeFactorizationResponse> getOutputData() {
        return Collections.unmodifiableList(outputData);
    }
}
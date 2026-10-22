package project;

import java.util.Collections;
import java.util.List;
import project.model.PrimeFactorizationResponse;
import project.model.StorageDestination;
import project.model.StorageSource;

public class DataStorageServiceImpl implements DataStorageService {

    @Override
    public List<Integer> readInput(StorageSource source) {
        return Collections.emptyList();
    }

    @Override
    public void writeOutput(StorageDestination destination, List<PrimeFactorizationResponse> results) {
        // empty stub implementation
    }
}
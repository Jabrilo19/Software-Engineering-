package project;

import java.util.List;
import project.annotations.ProcessAPI;
import project.model.PrimeFactorizationResponse;
import project.model.StorageDestination;
import project.model.StorageSource;

@ProcessAPI
public interface DataStorageService {
    // Reads integer values from the configured source descriptor
    List<Integer> readInput(StorageSource source);

    // Writes computed factorization results to the destination descriptor
    void writeOutput(StorageDestination destination, List<PrimeFactorizationResponse> results);
}
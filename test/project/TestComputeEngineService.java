package project;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import project.model.PrimeFactorizationResponse;
import project.model.StorageDestination;
import project.model.StorageSource;

public class TestComputeEngineService {

    @Test
    public void smokeTestComputeEngineService() {
        DataStorageService mockStorage = mock(DataStorageService.class);
        ComputationService mockComputation = mock(ComputationService.class);

        ComputeEngineService engine = new ComputeEngineServiceImpl(mockStorage, mockComputation);
        StorageSource source = new StorageSource("input.txt");
        StorageDestination destination = new StorageDestination("output.txt");

        PrimeFactorizationResponse result = engine.compute(source, destination);
        Assertions.assertNotNull(result);
    }
}
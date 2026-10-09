package project;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestComputeEngineService {

    @Test
    public void smokeTestComputeEngineService() {
        DataStorageService mockStorage = mock(DataStorageService.class);
        ComputationService mockComputation = mock(ComputationService.class);

        ComputeEngineService engine = new ComputeEngineServiceImpl(mockStorage, mockComputation);
        String result = engine.compute("input.txt", "output.txt");
        Assertions.assertNotNull(result);
    }
}

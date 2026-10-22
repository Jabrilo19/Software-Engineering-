package project;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import project.model.PrimeFactorizationResponse;
import project.model.StorageDestination;
import project.model.StorageSource;

public class ComputeEngineIntegrationTest {

    @Test
    public void testComputeEngineIntegration() {
        List<Integer> inputs = Arrays.asList(1, 10, 25);
        InMemoryDataStorageService dataStorage = new InMemoryDataStorageService(inputs);
        ComputationService computation = new ComputationServiceImpl();
        ComputeEngineService engine = new ComputeEngineServiceImpl(dataStorage, computation);

        StorageSource source = new StorageSource("in-memory-in");
        StorageDestination destination = new StorageDestination("in-memory-out");

        PrimeFactorizationResponse result = engine.compute(source, destination);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(3, dataStorage.getOutputData().size());
    }
}
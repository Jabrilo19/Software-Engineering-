package project;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ComputeEngineIntegrationTest {

    @Test
    public void testComputeEngineIntegration() {
        List<Integer> inputs = Arrays.asList(1, 10, 25);
        DataStorageService dataStorage = new InMemoryDataStorageService(inputs);
        ComputationService computation = new ComputationServiceImpl();
        ComputeEngineService engine = new ComputeEngineServiceImpl(dataStorage, computation);

        String result = engine.compute("in-memory-in", "in-memory-out");

        Assertions.assertNotNull(result);
    }
}

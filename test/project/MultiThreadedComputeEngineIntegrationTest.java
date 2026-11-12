package project;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import project.model.PrimeFactorizationResponse;
import project.model.StorageDestination;
import project.model.StorageSource;

public class MultiThreadedComputeEngineIntegrationTest {

    @Test
    public void testMultiThreadedIntegrationPreservesOrder() {
        List<Integer> inputs = Arrays.asList(1, 15, 10, 5, 2, 3, 8);
        InMemoryDataStorageService dataStorage = new InMemoryDataStorageService(inputs);
        ComputationService computation = new ComputationServiceImpl();
        ComputeEngineService engine = new MultiThreadedComputeEngineServiceImpl(dataStorage, computation);

        StorageSource source = new StorageSource("in-memory-in");
        StorageDestination destination = new StorageDestination("in-memory-out");

        PrimeFactorizationResponse lastResult = engine.compute(source, destination);

        Assertions.assertNotNull(lastResult);
        List<PrimeFactorizationResponse> outputs = dataStorage.getOutputData();
        Assertions.assertEquals(7, outputs.size());
        Assertions.assertEquals(Collections.emptyList(), outputs.get(0).getFactors());
        Assertions.assertEquals(Arrays.asList(3, 5), outputs.get(1).getFactors());
        Assertions.assertEquals(Arrays.asList(2, 5), outputs.get(2).getFactors());
        Assertions.assertEquals(Collections.singletonList(5), outputs.get(3).getFactors());
        Assertions.assertEquals(Collections.singletonList(2), outputs.get(4).getFactors());
        Assertions.assertEquals(Collections.singletonList(3), outputs.get(5).getFactors());
        Assertions.assertEquals(Arrays.asList(2, 2, 2), outputs.get(6).getFactors());
    }

    @Test
    public void testMultiThreadedMatchesSingleThreadedOutput() {
        List<Integer> inputs = Arrays.asList(84, 100, 17, 60, 2, 99);
        InMemoryDataStorageService singleStorage = new InMemoryDataStorageService(inputs);
        InMemoryDataStorageService multiStorage = new InMemoryDataStorageService(inputs);
        ComputationService computation = new ComputationServiceImpl();

        ComputeEngineService singleEngine = new ComputeEngineServiceImpl(singleStorage, computation);
        ComputeEngineService multiEngine = new MultiThreadedComputeEngineServiceImpl(multiStorage, computation);

        singleEngine.compute(new StorageSource("in"), new StorageDestination("out"));
        multiEngine.compute(new StorageSource("in"), new StorageDestination("out"));

        Assertions.assertEquals(singleStorage.getOutputData().size(), multiStorage.getOutputData().size());
        for (int i = 0; i < singleStorage.getOutputData().size(); i++) {
            Assertions.assertEquals(
                    singleStorage.getOutputData().get(i).getFactors(),
                    multiStorage.getOutputData().get(i).getFactors());
        }
    }

    @Test
    public void testNullAndEmptyInputsHandledCleanly() {
        InMemoryDataStorageService emptyStorage = new InMemoryDataStorageService();
        ComputationService computation = new ComputationServiceImpl();
        ComputeEngineService engine = new MultiThreadedComputeEngineServiceImpl(emptyStorage, computation);

        PrimeFactorizationResponse emptyResponse = engine.compute(
                new StorageSource("empty-in"), new StorageDestination("empty-out"));
        Assertions.assertNotNull(emptyResponse);
        Assertions.assertTrue(emptyResponse.getFactors().isEmpty());

        PrimeFactorizationResponse nullResponse = engine.compute(null, null);
        Assertions.assertNotNull(nullResponse);
        Assertions.assertTrue(nullResponse.getFactors().isEmpty());
    }
}
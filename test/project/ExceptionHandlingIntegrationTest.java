package project;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import project.model.PrimeFactorizationRequest;
import project.model.PrimeFactorizationResponse;
import project.model.StorageDestination;
import project.model.StorageSource;

public class ExceptionHandlingIntegrationTest {

    private static class BrokenDataStorageService implements DataStorageService {
        @Override
        public List<Integer> readInput(StorageSource source) {
            throw new RuntimeException("Simulated disk read failure");
        }

        @Override
        public void writeOutput(StorageDestination destination, List<PrimeFactorizationResponse> results) {
            throw new RuntimeException("Simulated disk write failure");
        }
    }

    private static class BrokenComputationService implements ComputationService {
        @Override
        public PrimeFactorizationResponse compute(PrimeFactorizationRequest request) {
            throw new ArithmeticException("Simulated calculation error");
        }
    }

    @Test
    public void testComputeEngineCatchesStorageException() {
        ComputeEngineService engine = new ComputeEngineServiceImpl(
            new BrokenDataStorageService(),
            new ComputationServiceImpl()
        );

        StorageSource source = () -> "dummy-source";
        StorageDestination dest = () -> "dummy-dest";

        PrimeFactorizationResponse response = Assertions.assertDoesNotThrow(() -> engine.compute(source, dest));
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.getFactors().isEmpty());
    }

    @Test
    public void testComputeEngineCatchesComputationException() {
        List<Integer> testData = new ArrayList<>();
        testData.add(10);

        ComputeEngineService engine = new ComputeEngineServiceImpl(
            new DataStorageService() {
                @Override
                public List<Integer> readInput(StorageSource source) {
                    return testData;
                }

                @Override
                public void writeOutput(StorageDestination destination, List<PrimeFactorizationResponse> results) {
                }
            },
            new BrokenComputationService()
        );

        StorageSource source = () -> "dummy-source";
        StorageDestination dest = () -> "dummy-dest";

        PrimeFactorizationResponse response = Assertions.assertDoesNotThrow(() -> engine.compute(source, dest));
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.getFactors().isEmpty());
    }
}
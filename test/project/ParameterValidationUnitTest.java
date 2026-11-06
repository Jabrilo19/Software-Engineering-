package project;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import project.model.PrimeFactorizationRequest;
import project.model.PrimeFactorizationResponse;

public class ParameterValidationUnitTest {

    @Test
    public void testComputeEngineNullParameterValidation() {
        DataStorageService storage = new InMemoryDataStorageService();
        ComputationService computation = new ComputationServiceImpl();
        ComputeEngineService engine = new ComputeEngineServiceImpl(storage, computation);

        PrimeFactorizationResponse response = engine.compute(null, null);
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.getFactors().isEmpty());
    }

    @Test
    public void testComputationServiceNullRequestValidation() {
        ComputationService computation = new ComputationServiceImpl();
        PrimeFactorizationResponse response = computation.compute(null);
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.getFactors().isEmpty());
    }

    @Test
    public void testDataStorageNullParameterValidation() {
        DataStorageService storage = new DataStorageServiceImpl();
        List<Integer> inputs = storage.readInput(null);
        Assertions.assertNotNull(inputs);
        Assertions.assertTrue(inputs.isEmpty());
        Assertions.assertDoesNotThrow(() -> storage.writeOutput(null, null));
    }
}
package project;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import project.model.PrimeFactorizationRequest;
import project.model.PrimeFactorizationResponse;

public class TestComputationService {

    @Test
    public void smokeTestComputationService() {
        ComputationService service = new ComputationServiceImpl();
        PrimeFactorizationRequest request = new PrimeFactorizationRequest(12);
        PrimeFactorizationResponse result = service.compute(request);
        Assertions.assertNotNull(result);
    }
}
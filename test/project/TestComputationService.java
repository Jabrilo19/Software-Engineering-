package project;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestComputationService {

    @Test
    public void smokeTestComputationService() {
        ComputationService service = new ComputationServiceImpl();
        List<Integer> result = service.compute(12);
        Assertions.assertNotNull(result);
    }
}

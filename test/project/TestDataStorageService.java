package project;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestDataStorageService {

    @Test
    public void smokeTestDataStorageService() {
        DataStorageService service = new DataStorageServiceImpl();
        List<Integer> result = service.readInput("input.txt");
        Assertions.assertNotNull(result);
    }
}

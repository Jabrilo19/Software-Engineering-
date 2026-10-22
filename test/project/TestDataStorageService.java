package project;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import project.model.StorageSource;

public class TestDataStorageService {

    @Test
    public void smokeTestDataStorageService() {
        DataStorageService service = new DataStorageServiceImpl();
        StorageSource source = new StorageSource("input.txt");
        List<Integer> result = service.readInput(source);
        Assertions.assertNotNull(result);
    }
}
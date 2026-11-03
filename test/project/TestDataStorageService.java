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

    @Test
    public void testReadInputNonExistentFileReturnsEmptyList() {
        DataStorageService service = new DataStorageServiceImpl();
        StorageSource missingSource = new StorageSource("non_existent_file_edge_case.txt");
        List<Integer> result = service.readInput(missingSource);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty(), "Expected empty list when reading a file that does not exist");
    }
}

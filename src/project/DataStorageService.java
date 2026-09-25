package project;

import java.util.List;
import project.annotations.ProcessAPI;

@ProcessAPI
public interface DataStorageService {
    // reads integer values from the source
    List<Integer> readInput(String source);

    // writes results to the destination
    void writeOutput(String destination, List<String> results);
}

package project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemoryDataStorageService implements DataStorageService {

    private final List<Integer> inputData;
    private final List<String> outputData;

    public InMemoryDataStorageService(List<Integer> inputData) {
        this.inputData = new ArrayList<>(inputData);
        this.outputData = new ArrayList<>();
    }

    public InMemoryDataStorageService() {
        this(Collections.emptyList());
    }

    @Override
    public List<Integer> readInput(String source) {
        return Collections.unmodifiableList(inputData);
    }

    @Override
    public void writeOutput(String destination, List<String> results) {
        outputData.addAll(results);
    }

    public List<String> getOutputData() {
        return Collections.unmodifiableList(outputData);
    }
}

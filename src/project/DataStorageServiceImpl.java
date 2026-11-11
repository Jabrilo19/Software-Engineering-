package project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import project.model.PrimeFactorizationResponse;
import project.model.StorageDestination;
import project.model.StorageSource;

public class DataStorageServiceImpl implements DataStorageService {

    @Override
    public List<Integer> readInput(StorageSource source) {
        if (source == null || source.getLocation() == null) {
            return Collections.emptyList();
        }

        try {
            Path path = Paths.get(source.getLocation());
            if (!Files.exists(path)) {
                return Collections.emptyList();
            }

            List<Integer> numbers = new ArrayList<>();
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                String[] tokens = trimmed.split("[,\\s]+");
                for (String token : tokens) {
                    if (!token.isEmpty()) {
                        numbers.add(Integer.parseInt(token));
                    }
                }
            }
            return numbers;
        } catch (Exception expected) {
            // Catches IOException, NumberFormatException, InvalidPathException, SecurityException
            return Collections.emptyList();
        }
    }

    @Override
    public void writeOutput(StorageDestination destination, List<PrimeFactorizationResponse> results) {
        if (destination == null || destination.getLocation() == null || results == null) {
            return;
        }

        try {
            Path path = Paths.get(destination.getLocation());
            List<String> formattedResults = new ArrayList<>();

            for (PrimeFactorizationResponse response : results) {
                if (response != null && response.getFactors() != null) {
                    List<Integer> factors = response.getFactors();
                    if (factors.isEmpty()) {
                        formattedResults.add("none");
                    } else {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < factors.size(); i++) {
                            sb.append(factors.get(i));
                            if (i < factors.size() - 1) {
                                sb.append("*");
                            }
                        }
                        formattedResults.add(sb.toString());
                    }
                }
            }

            String outputLine = String.join(",", formattedResults);
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            Files.writeString(path, outputLine);
        } catch (Exception expected) {
            // Safely swallows unexpected file or path runtime exceptions (e.g. InvalidPathException, SecurityException)
            // at the storage boundary without propagating unhandled exceptions.
        }
    }
}
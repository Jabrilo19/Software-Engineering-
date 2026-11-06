package project;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import project.model.NetworkDataRequest;
import project.model.PrimeFactorizationRequest;
import project.model.PrimeFactorizationResponse;
import project.model.StorageDestination;
import project.model.StorageSource;

public class ComputeEngineServiceImpl implements ComputeEngineService {

    private final DataStorageService dataStorageService;
    private final ComputationService computationService;

    public ComputeEngineServiceImpl(DataStorageService dataStorageService, ComputationService computationService) {
        this.dataStorageService = Objects.requireNonNull(dataStorageService, "dataStorageService cannot be null");
        this.computationService = Objects.requireNonNull(computationService, "computationService cannot be null");
    }

    @Override
    public PrimeFactorizationResponse compute(StorageSource inputSource, StorageDestination outputSource) {
        try {
            if (inputSource == null || outputSource == null) {
                return new PrimeFactorizationResponse(new ArrayList<>());
            }

            List<Integer> inputs = dataStorageService.readInput(inputSource);
            if (inputs == null) {
                inputs = new ArrayList<>();
            }

            List<PrimeFactorizationResponse> responses = new ArrayList<>();
            for (Integer num : inputs) {
                if (num != null) {
                    responses.add(computationService.compute(new PrimeFactorizationRequest(num)));
                }
            }
            dataStorageService.writeOutput(outputSource, responses);
            return responses.isEmpty() ? new PrimeFactorizationResponse(new ArrayList<>()) : responses.get(responses.size() - 1);
        } catch (Exception expected) {
            return new PrimeFactorizationResponse(new ArrayList<>());
        }
    }

    @Override
    public PrimeFactorizationResponse compute(StorageSource inputSource, StorageDestination outputSource, NetworkDataRequest networkConfig) {
        // networkConfig is optional in standard file mode; all values including null are acceptable
        return compute(inputSource, outputSource);
    }
}
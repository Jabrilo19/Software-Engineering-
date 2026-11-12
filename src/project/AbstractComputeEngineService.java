package project;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import project.model.NetworkDataRequest;
import project.model.PrimeFactorizationResponse;
import project.model.StorageDestination;
import project.model.StorageSource;

public abstract class AbstractComputeEngineService {

    protected final DataStorageService dataStorageService;
    protected final ComputationService computationService;

    protected AbstractComputeEngineService(DataStorageService dataStorageService, ComputationService computationService) {
        this.dataStorageService = Objects.requireNonNull(dataStorageService, "dataStorageService cannot be null");
        this.computationService = Objects.requireNonNull(computationService, "computationService cannot be null");
    }

    public PrimeFactorizationResponse compute(StorageSource inputSource, StorageDestination outputSource) {
        try {
            if (inputSource == null || outputSource == null) {
                return new PrimeFactorizationResponse(new ArrayList<>());
            }

            List<Integer> inputs = dataStorageService.readInput(inputSource);
            if (inputs == null) {
                inputs = new ArrayList<>();
            }

            List<PrimeFactorizationResponse> responses = processNumbers(inputs);
            dataStorageService.writeOutput(outputSource, responses);
            return responses.isEmpty()
                    ? new PrimeFactorizationResponse(new ArrayList<>())
                    : responses.get(responses.size() - 1);
        } catch (Exception expected) {
            return new PrimeFactorizationResponse(new ArrayList<>());
        }
    }

    public PrimeFactorizationResponse compute(
            StorageSource inputSource, StorageDestination outputSource, NetworkDataRequest networkConfig) {
        return compute(inputSource, outputSource);
    }

    protected abstract List<PrimeFactorizationResponse> processNumbers(List<Integer> inputs) throws Exception;
}
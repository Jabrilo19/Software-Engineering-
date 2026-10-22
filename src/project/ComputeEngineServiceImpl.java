package project;

import java.util.ArrayList;
import java.util.List;
import project.model.NetworkDataRequest;
import project.model.PrimeFactorizationRequest;
import project.model.PrimeFactorizationResponse;
import project.model.StorageDestination;
import project.model.StorageSource;

public class ComputeEngineServiceImpl implements ComputeEngineService {

    private final DataStorageService dataStorageService;
    private final ComputationService computationService;

    public ComputeEngineServiceImpl(DataStorageService dataStorageService, ComputationService computationService) {
        this.dataStorageService = dataStorageService;
        this.computationService = computationService;
    }

    @Override
    public PrimeFactorizationResponse compute(StorageSource inputSource, StorageDestination outputSource) {
        List<Integer> inputs = dataStorageService.readInput(inputSource);
        List<PrimeFactorizationResponse> responses = new ArrayList<>();
        for (Integer num : inputs) {
            responses.add(computationService.compute(new PrimeFactorizationRequest(num)));
        }
        dataStorageService.writeOutput(outputSource, responses);
        return responses.isEmpty() ? new PrimeFactorizationResponse(new ArrayList<>()) : responses.get(responses.size() - 1);
    }

    @Override
    public PrimeFactorizationResponse compute(StorageSource inputSource, StorageDestination outputSource, NetworkDataRequest networkConfig) {
        return compute(inputSource, outputSource);
    }
}
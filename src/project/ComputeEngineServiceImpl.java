package project;

import java.util.ArrayList;
import java.util.List;
import project.model.PrimeFactorizationRequest;
import project.model.PrimeFactorizationResponse;

public class ComputeEngineServiceImpl extends AbstractComputeEngineService implements ComputeEngineService {

    public ComputeEngineServiceImpl(DataStorageService dataStorageService, ComputationService computationService) {
        super(dataStorageService, computationService);
    }

    @Override
    protected List<PrimeFactorizationResponse> processNumbers(List<Integer> inputs) {
        List<PrimeFactorizationResponse> responses = new ArrayList<>();
        for (Integer num : inputs) {
            if (num != null) {
                responses.add(computationService.compute(new PrimeFactorizationRequest(num)));
            }
        }
        return responses;
    }
}
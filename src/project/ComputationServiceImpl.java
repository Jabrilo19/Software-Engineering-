package project;

import java.util.ArrayList;
import java.util.List;
import project.model.PrimeFactorizationRequest;
import project.model.PrimeFactorizationResponse;

public class ComputationServiceImpl implements ComputationService {

    @Override
    public PrimeFactorizationResponse compute(PrimeFactorizationRequest request) {
        if (request == null) {
            return new PrimeFactorizationResponse(new ArrayList<>());
        }
        int n = request.getValue();
        List<Integer> factors = new ArrayList<>();
        for (int i = 2; i * i <= n; i++) {
            while (n % i == 0) {
                factors.add(i);
                n /= i;
            }
        }
        if (n > 1) {
            factors.add(n);
        }
        return new PrimeFactorizationResponse(factors);
    }
}
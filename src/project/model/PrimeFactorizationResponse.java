package project.model;

import java.util.Collections;
import java.util.List;

public class PrimeFactorizationResponse {
    private final List<Integer> factors;

    public PrimeFactorizationResponse(List<Integer> factors) {
        this.factors = factors != null ? List.copyOf(factors) : Collections.emptyList();
    }

    public List<Integer> getFactors() {
        return factors;
    }
}
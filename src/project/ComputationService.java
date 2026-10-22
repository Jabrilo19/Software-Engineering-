package project;

import project.annotations.ConceptualAPI;
import project.model.PrimeFactorizationRequest;
import project.model.PrimeFactorizationResponse;

@ConceptualAPI
public interface ComputationService {
    // Computes prime factors for a wrapped integer request
    PrimeFactorizationResponse compute(PrimeFactorizationRequest request);
}
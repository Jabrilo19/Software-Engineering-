package project;

import project.annotations.ConceptualAPIPrototype;
import project.model.PrimeFactorizationRequest;

public class ComputationServicePrototype {

    // test call to verify conceptual computation api using request wrapper object
    @ConceptualAPIPrototype
    public void prototypeClient(ComputationService service) {
        PrimeFactorizationRequest request = new PrimeFactorizationRequest(12);
        service.compute(request);
    }
}
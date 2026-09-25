package project;

import project.annotations.ConceptualAPIPrototype;

public class ComputationServicePrototype {

    // test call to verify conceptual computation api
    @ConceptualAPIPrototype
    public void prototypeClient(ComputationService service) {
        service.compute(12);
    }
}

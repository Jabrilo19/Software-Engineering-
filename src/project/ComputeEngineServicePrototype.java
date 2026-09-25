package project;

import project.annotations.NetworkAPIPrototype;

public class ComputeEngineServicePrototype {

    // test call to verify network api methods
    @NetworkAPIPrototype
    public void prototypeClient(ComputeEngineService service) {
        service.compute("input.txt", "output.txt");
        service.compute("input.txt", "output.txt", ";", ",");
    }
}

package project;

import project.annotations.NetworkAPIPrototype;
import project.model.NetworkDataRequest;
import project.model.StorageDestination;
import project.model.StorageSource;

public class ComputeEngineServicePrototype {

    // test call to verify network api methods using domain objects
    @NetworkAPIPrototype
    public void prototypeClient(ComputeEngineService service) {
        StorageSource inputSource = new StorageSource("input.txt");
        StorageDestination outputSource = new StorageDestination("output.txt");
        NetworkDataRequest customRequest = new NetworkDataRequest("12;15", ";");

        service.compute(inputSource, outputSource);
        service.compute(inputSource, outputSource, customRequest);
    }
}
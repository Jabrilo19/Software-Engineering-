package project;

import java.util.ArrayList;
import project.annotations.ProcessAPIPrototype;

public class DataStorageServicePrototype {

    // test call to verify data storage process api
    @ProcessAPIPrototype
    public void prototypeClient(DataStorageService service) {
        service.readInput("input.txt");
        service.writeOutput("output.txt", new ArrayList<>());
    }
}

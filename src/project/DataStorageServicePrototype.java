package project;

import java.util.ArrayList;
import project.annotations.ProcessAPIPrototype;
import project.model.StorageDestination;
import project.model.StorageSource;

public class DataStorageServicePrototype {

    // test call to verify data storage process api using descriptor objects
    @ProcessAPIPrototype
    public void prototypeClient(DataStorageService service) {
        StorageSource source = new StorageSource("input.txt");
        StorageDestination destination = new StorageDestination("output.txt");
        service.readInput(source);
        service.writeOutput(destination, new ArrayList<>());
    }
}
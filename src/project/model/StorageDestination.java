package project.model;

public class StorageDestination {
    private final String location;
    private final String delimiter;

    public StorageDestination(String location) {
        this(location, ",");
    }

    public StorageDestination(String location, String delimiter) {
        this.location = location;
        this.delimiter = (delimiter == null || delimiter.isEmpty()) ? "," : delimiter;
    }

    public String getLocation() {
        return location;
    }

    public String getDelimiter() {
        return delimiter;
    }
}
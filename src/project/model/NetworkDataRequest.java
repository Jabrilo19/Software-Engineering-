package project.model;

public class NetworkDataRequest {
    private final String rawData;
    private final String delimiter;

    public NetworkDataRequest(String rawData, String delimiter) {
        this.rawData = rawData;
        this.delimiter = delimiter;
    }

    public NetworkDataRequest(String rawData) {
        this(rawData, ",");
    }

    public String getRawData() {
        return rawData;
    }

    public String getDelimiter() {
        return delimiter;
    }
}
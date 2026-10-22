package project.model;

public class PrimeFactorizationRequest {
    private final int value;

    public PrimeFactorizationRequest(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
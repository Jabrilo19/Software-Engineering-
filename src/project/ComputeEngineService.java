package project;

import project.annotations.NetworkAPI;
import project.model.NetworkDataRequest;
import project.model.PrimeFactorizationResponse;
import project.model.StorageDestination;
import project.model.StorageSource;

@NetworkAPI
public interface ComputeEngineService {
    // Runs computation using source and destination descriptors with default delimiters
    PrimeFactorizationResponse compute(StorageSource inputSource, StorageDestination outputSource);

    // Runs computation using request configuration wrapper for custom delimiter specs
    PrimeFactorizationResponse compute(StorageSource inputSource, StorageDestination outputSource, NetworkDataRequest networkConfig);
}
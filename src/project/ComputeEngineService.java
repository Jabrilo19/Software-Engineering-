package project;

import project.annotations.NetworkAPI;

@NetworkAPI
public interface ComputeEngineService {
    // runs computation with default delimiters
    String compute(String inputSource, String outputSource);

    // runs computation with custom delimiters
    String compute(String inputSource, String outputSource, String pairDelimiter, String itemDelimiter);
}

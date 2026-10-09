package project;

public class ComputeEngineServiceImpl implements ComputeEngineService {

    private final DataStorageService dataStorageService;
    private final ComputationService computationService;

    public ComputeEngineServiceImpl(DataStorageService dataStorageService, ComputationService computationService) {
        this.dataStorageService = dataStorageService;
        this.computationService = computationService;
    }

    @Override
    public String compute(String inputSource, String outputSource) {
        return "";
    }

    @Override
    public String compute(String inputSource, String outputSource, String pairDelimiter, String itemDelimiter) {
        return "";
    }
}

package project;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import project.model.PrimeFactorizationRequest;
import project.model.PrimeFactorizationResponse;

public class MultiThreadedComputeEngineServiceImpl extends AbstractComputeEngineService implements ComputeEngineService {

    public static final int MAX_THREADS = 4;

    private final int threadCount;

    public MultiThreadedComputeEngineServiceImpl(
            DataStorageService dataStorageService, ComputationService computationService) {
        this(dataStorageService, computationService, MAX_THREADS);
    }

    public MultiThreadedComputeEngineServiceImpl(
            DataStorageService dataStorageService, ComputationService computationService, int threadCount) {
        super(dataStorageService, computationService);
        this.threadCount = threadCount > 0 ? threadCount : MAX_THREADS;
    }

    @Override
    protected List<PrimeFactorizationResponse> processNumbers(List<Integer> inputs) throws Exception {
        if (inputs.isEmpty()) {
            return new ArrayList<>();
        }

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        try {
            List<Future<PrimeFactorizationResponse>> futures = new ArrayList<>();
            for (Integer num : inputs) {
                if (num != null) {
                    futures.add(executor.submit(() -> computationService.compute(new PrimeFactorizationRequest(num))));
                }
            }

            List<PrimeFactorizationResponse> responses = new ArrayList<>(futures.size());
            for (Future<PrimeFactorizationResponse> future : futures) {
                responses.add(future.get());
            }
            return responses;
        } finally {
            executor.shutdown();
        }
    }
}
package project;

import java.util.List;
import project.annotations.ConceptualAPI;

@ConceptualAPI
public interface ComputationService {
    // computes results for a single integer input
    List<Integer> compute(int input);
}

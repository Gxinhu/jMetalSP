package org.uma.jmetalsp.util.detectstrategy;


import org.uma.jmetal.solution.Solution;
import org.uma.jmetalsp.DynamicProblem;

import java.util.List;

/**
 * @author HUXin
 * @description Detect strategy
 * @date 2020/12/7
 **/
public class DetectStrategy<S extends Solution<?>> {
    private List<S> detector;
    private final DetectChangeStrategy<S> detectChangeStrategy;
    private final FilterDetectorStrategy<S> filterDetectorStrategy;

    public DetectStrategy(DetectChangeStrategy<S> detectChangeStrategy, FilterDetectorStrategy<S> filterDetectorStrata) {
        this.detectChangeStrategy = detectChangeStrategy;
        this.filterDetectorStrategy = filterDetectorStrata;
    }

    public void saveFilterDetector(List<S> population) {
        detector = filterDetectorStrategy.filterDetector(population);
    }

    public boolean isChange(DynamicProblem<S, ?> problem) {
        return detectChangeStrategy.detect(this.detector, problem);
    }
}

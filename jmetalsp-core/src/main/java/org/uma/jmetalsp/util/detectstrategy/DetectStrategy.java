package org.uma.jmetalsp.util.detectstrategy;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetalsp.DynamicProblem;
import org.uma.jmetalsp.util.detectstrategy.impl.FilterDetector;

import java.util.List;

/**
 * @author HUXin
 * @description
 * @date 2020/12/7
 **/
public class DetectStrategy<S extends Solution<?>> {
    private List<S> detector;
    private final DetectChangeStrategy<S> detectChangeStrategy;
    private final FilterDetector<S> fliterDetector;

    public DetectStrategy(DetectChangeStrategy<S> detectChangeStrategy) {
        this.detectChangeStrategy = detectChangeStrategy;
    }

    public void saveTempPopulaiton(List<S> tempPopulation) {
        this.detector = tempPopulation;
    }

    public boolean isChange( DynamicProblem<S, ?> problem) {
        return detectChangeStrategy.detect(this.detector, problem);
    }
}

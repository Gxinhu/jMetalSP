package org.uma.jmetalsp.util.detectstrategy.impl;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetalsp.DynamicProblem;
import org.uma.jmetalsp.util.detectstrategy.DetectChangeStrategy;

import java.util.Arrays;
import java.util.List;

/**
 * @author hu
 */
public class DefaultDetectStrategy<S extends Solution<?>> implements DetectChangeStrategy<S> {
    @Override
    public boolean detect(List<S> tempPopulation, DynamicProblem<S, ?> problem) {
        boolean isChange = false;
        for (S solution : tempPopulation) {
            double[] tempObjectives = solution.getObjectives();
            problem.evaluate(solution);
            if (!Arrays.equals(tempObjectives, solution.getObjectives())) {
                isChange = true;
                break;
            }

        }
        return isChange;
    }

}

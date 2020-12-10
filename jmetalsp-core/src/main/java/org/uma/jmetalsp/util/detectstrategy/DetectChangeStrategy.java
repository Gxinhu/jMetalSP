package org.uma.jmetalsp.util.detectstrategy;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetalsp.DynamicProblem;

import java.util.List;

/**
 * @author HUXin
 * @description Detect the dynamic problem is change or not
 * @date 2020/12/7
 **/
public interface DetectChangeStrategy<S extends Solution<?>> {
    /**
     * Detect the dynamic problem status
     *
     * @param detector detector population
     * @param problem  problem
     * @return The problem status
     */
    boolean detect(List<S> detector, DynamicProblem<S, ?> problem);
}

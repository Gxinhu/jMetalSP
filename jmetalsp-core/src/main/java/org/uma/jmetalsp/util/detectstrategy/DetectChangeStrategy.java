package org.uma.jmetalsp.util.detectstrategy;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetalsp.DynamicProblem;

import java.util.List;

/**
 * @author HUXin
 * @description
 * @date 2020/12/7
 **/
public interface DetectChangeStrategy<S extends Solution<?>> {
    boolean detect(List<S> tempPopulation, DynamicProblem<S, ?> problem);
}

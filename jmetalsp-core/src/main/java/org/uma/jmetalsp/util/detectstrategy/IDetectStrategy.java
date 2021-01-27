package org.uma.jmetalsp.util.detectstrategy;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetalsp.DynamicProblem;

import java.util.List;

/**
 * @author HUXin
 * @description
 * @date 2020/12/7
 **/
public interface IDetectStrategy<S extends Solution<?>> {
  boolean detect(List<S> detector, DynamicProblem<S, ?> problem);
}

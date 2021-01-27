package org.uma.jmetalsp.util.detectstrategy.impl;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetalsp.DynamicProblem;
import org.uma.jmetalsp.util.detectstrategy.IDetectStrategy;

import java.util.Arrays;
import java.util.List;

/**
 * @author HUXin
 * @description
 * @date 2021/1/26
 **/
public class DefaultDetectStrategy<S extends Solution<?>> implements IDetectStrategy<S> {

  @Override
  public boolean detect(List<S> detector, DynamicProblem<S, ?> problem) {
    return detector
        .stream()
        .anyMatch(solution -> {
          double[] tempObjective = solution.getObjectives().clone();
          problem.evaluate(solution);
          return !Arrays.equals(tempObjective, solution.getObjectives());
        });

  }
}

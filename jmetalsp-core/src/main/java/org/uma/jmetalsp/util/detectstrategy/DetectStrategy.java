package org.uma.jmetalsp.util.detectstrategy;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetalsp.DynamicProblem;

import java.util.List;

/**
 * @author HUXin
 * @description
 * @date 2020/12/7
 **/
public class DetectStrategy<S extends Solution<?>> {
  private final IDetectStrategy<S> detectStrategy;
  private final IFilterStrategy<S> filterStrategy;

  public DetectStrategy(IDetectStrategy<S> detectChangeStrategy, IFilterStrategy<S> filterDetector) {
    this.detectStrategy = detectChangeStrategy;
    this.filterStrategy = filterDetector;
  }


  public boolean isChange(DynamicProblem<S, ?> problem, List<S> population) {
    List<S> detector = filterStrategy.filter(population);
    return detectStrategy.detect(detector, problem);
  }
}

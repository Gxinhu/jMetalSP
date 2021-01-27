package org.uma.jmetalsp.util.detectstrategy.impl;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetalsp.util.detectstrategy.IFilterStrategy;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author HUXin
 * @description
 * @date 2021/1/26
 **/
public class FilterNSolutions<S extends Solution<?>> implements IFilterStrategy<S> {
  private final int filterPercent;

  public FilterNSolutions(int filterPercent) {
    this.filterPercent = filterPercent;
  }

  @Override
  public List<S> filter(List<S> population) {
    Random random = new Random();
    int limitSize = population.size() * filterPercent / 100;
    return random.ints(limitSize, 0, population.size())
        .mapToObj(population::get)
        .collect(Collectors.toList());
  }
}

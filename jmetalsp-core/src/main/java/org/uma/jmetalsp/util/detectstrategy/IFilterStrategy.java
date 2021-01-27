package org.uma.jmetalsp.util.detectstrategy;

import org.uma.jmetal.solution.Solution;

import java.util.List;

/**
 * @author HUXin
 * @description
 * @date 2020/12/7
 **/
public interface IFilterStrategy<S extends Solution<?>> {
  List<S> filter(List<S> population);
}

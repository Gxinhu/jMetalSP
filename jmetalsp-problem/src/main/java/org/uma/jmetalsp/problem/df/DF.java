package org.uma.jmetalsp.problem.df;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetalsp.DynamicProblem;
import org.uma.jmetalsp.DynamicUpdate;
import org.uma.jmetalsp.observeddata.ObservedValue;
import org.uma.jmetalsp.observer.Observable;
import org.uma.jmetalsp.observer.impl.DefaultObservable;
import org.uma.jmetalsp.problem.fda.FDA;

import java.io.Serializable;

/**
 * Cristóbal Barba <cbarba@lcc.uma.es>
 */
public abstract class DF
    extends AbstractDoubleProblem
    implements DynamicProblem<DoubleSolution, ObservedValue<Integer>>, DynamicUpdate, Serializable {
  protected double time;
  protected boolean theProblemHasBeenModified;
  protected Observable<ObservedValue<Integer>> observable;
  private int startChangeIteration = 50;
  private int numberChange = 10;//10,30
  private int severityChange = 10;
  private int count;

  public DF(Observable<ObservedValue<Integer>> observable) {
    this.observable = observable;
    this.time = 1.0;
    observable.register(this);
    this.count = 0;
  }

  public DF() {
    this(new DefaultObservable<>());
  }

  @Override
  public void update(Observable<ObservedValue<Integer>> observable, ObservedValue<Integer> counter) {
    updateTime(counter.getValue());
    theProblemHasBeenModified = true;
  }

  public double helperSum(DoubleSolution solution, int start, int end, double y) {
    double result = 0.0d;
    for (int i = start; i < end; i++) {
      result += Math.pow(solution.getVariableValue(i) - y, 2.0d);
    }
    return result;
  }

  @Override
  public void update() {
    updateTime(count);
    count++;
    theProblemHasBeenModified = true;
  }

  private void updateTime(int value) {
    int temp = Math.max(value + numberChange - (startChangeIteration), 0);
    time = (1.0d / (double) severityChange) * Math.floor((double) temp / numberChange);
  }

  public DF setSeverityVChange(int severityVChange) {
    this.severityChange = severityVChange;
    return this;
  }

  public DF setStartChangeIteration(int startChangeIteration) {
    this.startChangeIteration = startChangeIteration;
    return this;
  }

  public DF setNumberChange(int numberChange) {
    this.numberChange = numberChange;
    return this;
  }

}

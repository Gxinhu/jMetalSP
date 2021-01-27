package org.uma.jmetalsp.problem.df;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetalsp.DynamicProblem;
import org.uma.jmetalsp.DynamicUpdate;
import org.uma.jmetalsp.observeddata.ObservedValue;
import org.uma.jmetalsp.observer.Observable;
import org.uma.jmetalsp.observer.impl.DefaultObservable;

import java.io.Serializable;

/**
 * Cristóbal Barba <cbarba@lcc.uma.es>
 */
public abstract class AbstractDF
        extends AbstractDoubleProblem
        implements DynamicProblem<DoubleSolution, ObservedValue<Integer>>, DynamicUpdate, Serializable {
    protected double time;
    protected boolean theProblemHasBeenModified;
    protected Observable<ObservedValue<Integer>> observable;
    private int startChangeIteration = 50;
    private int numberChange = 10;
    private int severityChange = 10;
    private int count;

    public AbstractDF(Observable<ObservedValue<Integer>> observable) {
        this.observable = observable;
        this.time = 1.0;
        observable.register(this);
        this.count = 0;
    }

    public AbstractDF() {
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
        double tauTmp = Math.max((double) value + numberChange - (startChangeIteration + 1), 0.0);
        time = (1.0d / (double) severityChange) * Math.floor(tauTmp / (double) numberChange);
    }

    @Override
    public void setNumberChange(int numberChange) {
        this.numberChange = numberChange;
    }

    @Override
    public void setStartChangeIteration(int startChangeIteration) {
        this.startChangeIteration = startChangeIteration;
    }

    @Override
    public void setSeverityChange(int severityChange) {
        this.severityChange = severityChange;
    }
}

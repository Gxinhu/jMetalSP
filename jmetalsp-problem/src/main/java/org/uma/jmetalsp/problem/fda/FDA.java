package org.uma.jmetalsp.problem.fda;

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
public abstract class FDA extends AbstractDoubleProblem
        implements DynamicProblem<DoubleSolution, ObservedValue<Integer>>, DynamicUpdate, Serializable {
    protected double time;
    protected boolean theProblemHasBeenModified;
    protected Observable<ObservedValue<Integer>> observable;
    private int count;
    private int numberChange = 5;
    private int severityChange = 10;
    private int startChangeIteration = 50;

    public FDA(Observable<ObservedValue<Integer>> observable) {
        this.observable = observable;
        this.time = 1.0;
        observable.register(this);
        this.count = 0;
    }

    public FDA() {
        this(new DefaultObservable<>());
    }

    @Override
    public void update(Observable<ObservedValue<Integer>> observable, ObservedValue<Integer> counter) {
        updateTime(counter.getValue());
    }

    @Override
    public void update() {
        updateTime(count);
        count++;
        theProblemHasBeenModified = true;
    }

    @Override
    public void setSeverityVChange(int severityVChange) {
        this.severityChange = severityVChange;
    }

    @Override
    public void setStartChangeIteration(int startChangeIteration) {

        this.startChangeIteration = startChangeIteration;
    }

    @Override
    public void setNumberChange(int numberChange) {

        this.numberChange = numberChange;
    }

    private void updateTime(int value) {
        int temp = Math.max(value + numberChange - (startChangeIteration), 0);
        time = (1.0d / (double) severityChange) * Math.floor((double) temp / numberChange);
    }
}

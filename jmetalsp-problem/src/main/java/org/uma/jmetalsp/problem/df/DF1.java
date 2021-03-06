package org.uma.jmetalsp.problem.df;

import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetalsp.observeddata.ObservedValue;
import org.uma.jmetalsp.observer.Observable;
import org.uma.jmetalsp.observer.impl.DefaultObservable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DF1 extends AbstractDF implements Serializable {
    public DF1(Observable<ObservedValue<Integer>> observable) {
        this(10, 2, observable);
    }

    public DF1() {
        this(new DefaultObservable<>());
    }

    public DF1(Integer numberOfVariables, Integer numberOfObjectives, Observable<ObservedValue<Integer>> observer) throws JMetalException {
        super(observer);
        setNumberOfVariables(numberOfVariables);
        setNumberOfObjectives(numberOfObjectives);
        setName("DF1");

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());

        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(0.0);
            upperLimit.add(1.0);
        }

        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);
        time = 0.0d;
        theProblemHasBeenModified = false;
    }

    @Override
    public boolean hasTheProblemBeenModified() {
        return theProblemHasBeenModified;
    }

    @Override
    public void reset() {
        theProblemHasBeenModified = false;
    }

    @Override
    public void evaluate(DoubleSolution solution) {
        double[] f = new double[getNumberOfObjectives()];
        double G = Math.abs(Math.sin(0.5 * Math.PI * time));
        double H = 0.75 * Math.sin(0.5 * Math.PI * time) + 1.25;
        double g = 1 + helperSum(solution, 1, solution.getNumberOfVariables(), G);
        f[0] = solution.getVariableValue(0);
        f[1] = g * (1 - Math.pow(solution.getVariableValue(0) / g, H));

        solution.setObjective(0, f[0]);
        solution.setObjective(1, f[1]);
    }

    /**
     * Returns the value of the FDA2 function G.
     *
     * @param solution Solution
     */
    private double evalG(DoubleSolution solution, int limitInf, int limitSup) {

        double g = 0.0;
        for (int i = limitInf; i < limitSup; i++) {
            g += Math.pow(solution.getVariableValue(i), 2.0);
        }
        for (int i = limitSup; i < solution.getNumberOfVariables(); i++) {
            g += Math.pow((solution.getVariableValue(i) + 1.0), 2.0);
        }
        g = g + 1.0;
        return g;
    }

    /**
     * Returns the value of the FDA function H.
     *
     * @param f First argument of the function H.
     * @param g Second argument of the function H.
     */
    private double evalH(double f, double g) {
        double HT = 0.2 + 4.8 * Math.pow(time, 2.0);
        double h = 1.0 - Math.pow((f / g), HT);
        return h;
    }
}

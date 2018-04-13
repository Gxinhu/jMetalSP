package org.uma.jmetalsp.algorithm.wasfga;

import java.util.List;
import org.uma.jmetal.algorithm.multiobjective.rnsgaii.RNSGAII;
import org.uma.jmetal.algorithm.multiobjective.wasfga.WASFGA;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetalsp.DynamicProblem;
import org.uma.jmetalsp.InteractiveAlgorithm;
import org.uma.jmetalsp.util.restartstrategy.RestartStrategy;

public class InteractiveWASFGA <S extends Solution<?>> extends WASFGA<S> implements
    InteractiveAlgorithm<S,List<S>> {
  List<S> offspringPopulation;
  List<S> matingPopulation;

  public InteractiveWASFGA(Problem<S> problem, int populationSize,
      int maxIterations,
      CrossoverOperator<S> crossoverOperator,
      MutationOperator<S> mutationOperator,
      SelectionOperator<List<S>, S> selectionOperator,
      SolutionListEvaluator<S> evaluator, double epsilon,
      List<Double> referencePoint, String weightVectorsFileName) {
    super(problem, populationSize, maxIterations, crossoverOperator, mutationOperator,
        selectionOperator, evaluator, epsilon, referencePoint, weightVectorsFileName);
  }

  public InteractiveWASFGA(Problem<S> problem, int populationSize, int maxIterations,
      CrossoverOperator<S> crossoverOperator,
      MutationOperator<S> mutationOperator,
      SelectionOperator<List<S>, S> selectionOperator,
      SolutionListEvaluator<S> evaluator, double epsilon,
      List<Double> referencePoint) {
    super(problem, populationSize, maxIterations, crossoverOperator, mutationOperator,
        selectionOperator, evaluator, epsilon, referencePoint);
  }

  @Override
  public void restart(RestartStrategy restartStrategy) {
   restartStrategy.restart(getPopulation(), (DynamicProblem<S, ?>)getProblem());
    this.evaluatePopulation(this.getPopulation());
    this.initProgress();
    this.specificMOEAComputations();
  }

  @Override
  public void compute() {
    /**
     *
     * List<S> offspringPopulation;
     * 		List<S> matingPopulation;
     *
     * 		this.setPopulation(createInitialPopulation());
     * 		this.evaluatePopulation(this.getPopulation());
     * 		initProgress();
     * 		//specific GA needed computations
     * 		this.specificMOEAComputations();
     * 		while (!isStoppingConditionReached()) {
     * 			matingPopulation = selection(this.getPopulation());
     * 			offspringPopulation = reproduction(matingPopulation);
     * 			offspringPopulation = evaluatePopulation(offspringPopulation);
     * 			this.setPopulation(replacement(this.getPopulation(), offspringPopulation));
     * 			updateProgress();
     * 			// specific GA needed computations
     * 			this.specificMOEAComputations();
     *        }
     *
     */

    matingPopulation = selection(this.getPopulation());
    offspringPopulation = reproduction(matingPopulation);
    offspringPopulation = evaluatePopulation(offspringPopulation);
    this.setPopulation(replacement(this.getPopulation(), offspringPopulation));
    //updateProgress();
    // specific GA needed computations
    this.specificMOEAComputations();
  }

  @Override
  public List<S> initializePopulation() {
    this.setPopulation(createInitialPopulation());
    this.evaluatePopulation(this.getPopulation());
    this.specificMOEAComputations();
    return this.getPopulation();

  }

  @Override
  public void evaluate(List<S> population) {
    this.setPopulation( this.evaluatePopulation(population));
  }


}

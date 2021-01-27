//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package org.uma.jmetalsp.algorithm.nsgaii;

import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.point.PointSolution;
import org.uma.jmetalsp.DynamicAlgorithm;
import org.uma.jmetalsp.DynamicProblem;
import org.uma.jmetalsp.observeddata.AlgorithmObservedData;
import org.uma.jmetalsp.observeddata.ObservedValue;
import org.uma.jmetalsp.observer.Observable;
import org.uma.jmetalsp.qualityindicator.CoverageFront;
import org.uma.jmetalsp.util.detectstrategy.DetectStrategy;
import org.uma.jmetalsp.util.restartstrategy.RestartStrategy;
import org.uma.jmetalsp.util.restartstrategy.impl.CreateNRandomSolutions;
import org.uma.jmetalsp.util.restartstrategy.impl.RemoveFirstNSolutions;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class implementing a dynamic version of NSGA-II. Most of the code of the
 * original NSGA-II is reused, and measures are used to allow external
 * components to access the results of the computation.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 * @todo Explain the behaviour of the dynamic algorithm
 */
public class DynamicNSGAII<S extends Solution<?>> extends NSGAII<S>
    implements DynamicAlgorithm<List<S>, AlgorithmObservedData, S> {

  private final int completedIterations;
  private boolean stopAtTheEndOfTheCurrentIteration = false;
  private RestartStrategy<S> restartStrategyForProblemChange;
  private Comparator<S> dominanceComparator;
  private final Observable<AlgorithmObservedData> observable;
  private List<S> lastReceivedFront;
  private boolean autoUpdate;
  private CoverageFront<PointSolution> coverageFront;
  private DetectStrategy<S> detectstrategy;

  public DynamicNSGAII(DynamicProblem<S, ?> problem, int maxEvaluations, int populationSize, int matingPoolSize,
                       int offspringPopulationSize, Comparator<S> dominanceComparator, CrossoverOperator<S> crossoverOperator,
                       MutationOperator<S> mutationOperator, SelectionOperator<List<S>, S> selectionOperator,
                       SolutionListEvaluator<S> evaluator, Observable<AlgorithmObservedData> observable, boolean autoUpdate,
                       CoverageFront<PointSolution> coverageFront) {

    super(problem, maxEvaluations, populationSize, matingPoolSize, offspringPopulationSize, crossoverOperator,
        mutationOperator, selectionOperator, dominanceComparator, evaluator);

    this.completedIterations = 0;
    this.autoUpdate = autoUpdate;
    this.observable = observable;
    this.coverageFront = coverageFront;
    this.restartStrategyForProblemChange = new RestartStrategy<>(new RemoveFirstNSolutions<S>(populationSize),
        new CreateNRandomSolutions<S>());
  }

  public DynamicNSGAII(DynamicProblem<S, ?> problem, int maxEvaluations, int populationSize,
                       CrossoverOperator<S> crossoverOperator, MutationOperator<S> mutationOperator,
                       SelectionOperator<List<S>, S> selectionOperator, SolutionListEvaluator<S> evaluator,
                       Comparator<S> dominanceComparator, Observable<AlgorithmObservedData> observable, boolean autoUpdate,
                       CoverageFront<PointSolution> coverageFront) {
    super(problem, maxEvaluations, populationSize, populationSize, populationSize, crossoverOperator, mutationOperator,
        selectionOperator, dominanceComparator, evaluator);

    this.completedIterations = 0;
    this.observable = observable;
    this.autoUpdate = autoUpdate;
    this.coverageFront = coverageFront;
    this.restartStrategyForProblemChange = new RestartStrategy<>(new RemoveFirstNSolutions<S>(populationSize),
        new CreateNRandomSolutions<S>());
  }


  @Override
  public DynamicProblem<S, ?> getDynamicProblem() {
    return (DynamicProblem<S, ?>) super.getProblem();
  }

  @Override
  protected boolean isStoppingConditionReached() {
    if (evaluations <= maxEvaluations) {
      int iteration = evaluations / getPopulation().size();
      ((DynamicProblem) getDynamicProblem()).update(null, new ObservedValue<>(iteration));
      if (detectstrategy.isChange(getDynamicProblem(), population)) {
        Map<String, Object> algorithmData = new HashMap<>();
        algorithmData.put("numberOfIterations", evaluations);
        algorithmData.put("algorithmName", getName());
        algorithmData.put("problemName", problem.getName());
        algorithmData.put("numberOfObjectives", problem.getNumberOfObjectives());
        observable.setChanged();
        observable.notifyObservers(new AlgorithmObservedData((List<Solution<?>>) getPopulation(), algorithmData));
        observable.clearChanged();
        restart();
        evaluator.evaluate(getPopulation(), getDynamicProblem());
        evaluations += getPopulation().size();
      }
    } else {
      stopAtTheEndOfTheCurrentIteration = true;
    }
    return stopAtTheEndOfTheCurrentIteration;
  }

  @Override
  public String getName() {
    return "DynamicNSGAII";
  }

  @Override
  public String getDescription() {
    return "Dynamic version of algorithm NSGA-II";
  }

  @Override
  public Observable<AlgorithmObservedData> getObservable() {
    return this.observable;
  }

  @Override
  public void restart() {
    this.restartStrategyForProblemChange.restart(getPopulation(), (DynamicProblem<S, ?>) getProblem());
  }

  @Override
  public void setRestartStrategy(RestartStrategy<S> restartStrategy) {
    this.restartStrategyForProblemChange = restartStrategy;
  }

  @Override
  public void setDetectStrategy(DetectStrategy<S> detectStrategy) {
    this.detectstrategy = detectStrategy;
  }
}

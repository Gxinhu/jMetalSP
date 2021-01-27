package org.uma.jmetalsp.examples.continuousproblemapplication;

import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetalsp.DataConsumer;
import org.uma.jmetalsp.DynamicAlgorithm;
import org.uma.jmetalsp.DynamicProblem;
import org.uma.jmetalsp.JMetalSPApplication;
import org.uma.jmetalsp.consumer.ChartConsumer;
import org.uma.jmetalsp.consumer.LocalDirectoryOutputConsumer;
import org.uma.jmetalsp.impl.DefaultRuntime;
import org.uma.jmetalsp.observeddata.AlgorithmObservedData;
import org.uma.jmetalsp.observeddata.ObservedValue;
import org.uma.jmetalsp.problem.fda.FDA2;
import org.uma.jmetalsp.util.detectstrategy.DetectStrategy;
import org.uma.jmetalsp.util.detectstrategy.impl.DefaultDetectStrategy;
import org.uma.jmetalsp.util.detectstrategy.impl.FilterNSolutions;
import org.uma.jmetalsp.util.restartstrategy.RestartStrategy;
import org.uma.jmetalsp.util.restartstrategy.impl.CreateNRandomSolutions;
import org.uma.jmetalsp.util.restartstrategy.impl.RemoveNRandomSolutions;

import java.io.IOException;
import java.util.List;

/**
 * Example of jMetalSP application. Features: - Algorithm: to choose among
 * NSGA-II, SMPSO, MOCell, and WASF-GA - Problem: Any of the FDA familiy -
 * Default streaming runtime (Spark is not used)
 * <p>
 * Steps to compile and run the example: 1. Compile the project: mvn package 2.
 * Run the program: java -cp
 * jmetalsp-examples/target/jmetalsp-examples-2.1-SNAPSHOTar-with-dependencies.jar
 * \
 * org.uma.jmetalsp.examples.continuousproblemapplication.DynamicContinuousApplication
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class DynamicContinuousApplication {

  public static void main(String[] args) throws IOException, InterruptedException {
    DynamicProblem<DoubleSolution, ObservedValue<Integer>> problem = new FDA2();
    DynamicAlgorithm<List<DoubleSolution>, AlgorithmObservedData, DoubleSolution> algorithm = AlgorithmFactory.getAlgorithm("NSGAII",
        problem);
    algorithm.setRestartStrategy(new RestartStrategy<>(
        new RemoveNRandomSolutions<>(15), new CreateNRandomSolutions<>()));
    algorithm.setDetectStrategy(new DetectStrategy<>(new DefaultDetectStrategy<>(),
        new FilterNSolutions<>(5)));

    DataConsumer<AlgorithmObservedData> localDirectoryOutputConsumer = new LocalDirectoryOutputConsumer<DoubleSolution>(
        "./outputdirectory");
    DataConsumer<AlgorithmObservedData> chartConsumer = new ChartConsumer<DoubleSolution>(algorithm.getName(),
        "./outputdirectory/");
    JMetalSPApplication<DoubleSolution, DynamicProblem<DoubleSolution, ObservedValue<Integer>>, DynamicAlgorithm<List<DoubleSolution>, AlgorithmObservedData, DoubleSolution>> application;
    application = new JMetalSPApplication<>();
    application.setStreamingRuntime(new DefaultRuntime()).setProblem(problem).setAlgorithm(algorithm)
        .addAlgorithmDataConsumer(chartConsumer)
        .addAlgorithmDataConsumer(localDirectoryOutputConsumer).run();
  }
}

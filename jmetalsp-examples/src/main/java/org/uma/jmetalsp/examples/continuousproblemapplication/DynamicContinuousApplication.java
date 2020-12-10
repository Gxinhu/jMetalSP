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
import org.uma.jmetalsp.util.detectstrategy.impl.RandomSelectDetectorStrategy;
import org.uma.jmetalsp.util.restartstrategy.RestartStrategy;
import org.uma.jmetalsp.util.restartstrategy.impl.CreateNRandomSolutions;
import org.uma.jmetalsp.util.restartstrategy.impl.RemoveNRandomSolutions;

import java.util.List;

/**
 * Example of jMetalSP application. Features: - Algorithm: to choose among
 *
 * @author HUXin
 */
public class DynamicContinuousApplication {

    public static void main(String[] args) throws InterruptedException {
        // STEP 1. Create the problem
        DynamicProblem<DoubleSolution, ObservedValue<Integer>> problem = new FDA2();
        // STEP 2. Create the algorithm
        DynamicAlgorithm<List<DoubleSolution>, AlgorithmObservedData> algorithm = AlgorithmFactory.getAlgorithm("NSGAII",
                problem);

        // STEP 3. Create the restart and detect strategy and set into the algorithm
        algorithm.setRestartStrategy(new RestartStrategy<>(
                new RemoveNRandomSolutions<>(15), new CreateNRandomSolutions<DoubleSolution>()));
        algorithm.setDetectStrategy(new DetectStrategy<>(new DefaultDetectStrategy<>(), new RandomSelectDetectorStrategy<DoubleSolution>(0.05)));

        // STEP 4. Create the data consumers and register into the algorithm
        DataConsumer<AlgorithmObservedData> localDirectoryOutputConsumer = new LocalDirectoryOutputConsumer<DoubleSolution>(
                "Subdirectory");

        DataConsumer<AlgorithmObservedData> chartConsumer = new ChartConsumer<DoubleSolution>(algorithm.getName(),
                "./outputting/");
        // STEP 5. Create the application and run
        JMetalSPApplication<DoubleSolution, DynamicProblem<DoubleSolution, ObservedValue<Integer>>, DynamicAlgorithm<List<DoubleSolution>, AlgorithmObservedData>> application;

        application = new JMetalSPApplication<>();

        application.setStreamingRuntime(new DefaultRuntime()).setProblem(problem).setAlgorithm(algorithm)
                .addAlgorithmDataConsumer(localDirectoryOutputConsumer).run();
    }
}

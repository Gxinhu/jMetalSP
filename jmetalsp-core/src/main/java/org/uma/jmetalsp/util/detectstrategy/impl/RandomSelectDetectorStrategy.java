package org.uma.jmetalsp.util.detectstrategy.impl;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetalsp.util.detectstrategy.FilterDetectorStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author hu
 */
public class RandomSelectDetectorStrategy<S extends Solution<?>> implements FilterDetectorStrategy<S> {
    private final double selectPercent;

    public RandomSelectDetectorStrategy(double selectPercent) {
        this.selectPercent = selectPercent;
    }

    @Override
    public List<S> filterDetector(List<S> population) {
        int detectorSize = (int) Math.ceil(population.size() * selectPercent);
        List<S> detector = new ArrayList<>(detectorSize);
        IntStream.range(0, detectorSize).forEach(s -> {

            int chosen = JMetalRandom.getInstance().nextInt(0, population.size() - 1);
            detector.add(population.get(chosen));
        });

        return detector;
    }
}

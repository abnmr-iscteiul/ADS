package com.example.demo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.lab.experiment.Experiment;
import org.uma.jmetal.lab.experiment.ExperimentBuilder;
import org.uma.jmetal.lab.experiment.component.impl.ComputeQualityIndicators;
import org.uma.jmetal.lab.experiment.component.impl.ExecuteAlgorithms;
import org.uma.jmetal.lab.experiment.component.impl.GenerateBoxplotsWithR;
import org.uma.jmetal.lab.experiment.component.impl.GenerateFriedmanTestTables;
import org.uma.jmetal.lab.experiment.component.impl.GenerateLatexTablesWithStatistics;
import org.uma.jmetal.lab.experiment.component.impl.GenerateWilcoxonTestTablesWithR;
import org.uma.jmetal.lab.experiment.util.ExperimentAlgorithm;
import org.uma.jmetal.lab.experiment.util.ExperimentProblem;
import org.uma.jmetal.operator.crossover.impl.IntegerSBXCrossover;
import org.uma.jmetal.operator.crossover.impl.SBXCrossover;
import org.uma.jmetal.operator.mutation.impl.IntegerPolynomialMutation;
import org.uma.jmetal.operator.mutation.impl.PolynomialMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.qualityindicator.impl.Epsilon;
import org.uma.jmetal.qualityindicator.impl.GenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistancePlus;
import org.uma.jmetal.qualityindicator.impl.NormalizedHypervolume;
import org.uma.jmetal.qualityindicator.impl.Spread;
import org.uma.jmetal.qualityindicator.impl.hypervolume.impl.PISAHypervolume;
import org.uma.jmetal.solution.integersolution.IntegerSolution;
import org.uma.jmetal.util.errorchecking.JMetalException;

import ch.qos.logback.core.util.FileUtil;

/**
 * Example of experimental study based on solving the ZDT problems with four versions of NSGA-II,
 * each of them applying a different crossover probability (from 0.7 to 1.0).
 *
 * <p>This org.uma.jmetal.experiment assumes that the reference Pareto front are known and that,
 * given a problem named P, there is a corresponding file called P.pf containing its corresponding
 * Pareto front. If this is not the case, please refer to class {@link DTLZStudy} to see an example
 * of how to explicitly indicate the name of those files.
 *
 * <p>Six quality indicators are used for performance assessment.
 *
 * <p>The steps to carry out the org.uma.jmetal.experiment are: 1. Configure the
 * org.uma.jmetal.experiment 2. Execute the algorithms 3. Compute the quality indicators 4. Generate
 * Latex tables reporting means and medians 5. Generate Latex tables with the result of applying the
 * Wilcoxon Rank Sum Test 6. Generate Latex tables with the ranking obtained by applying the
 * Friedman test 7. Generate R scripts to obtain boxplots
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class NSGAIIStudy {
  private static final int INDEPENDENT_RUNS = 5;

  public static void main(String[] args) throws IOException {
    String experimentBaseDirectory = "base";
    FileUtils.deleteDirectory(new File("base/NSGAIIStudy/data"));
    
    List<ExperimentProblem<IntegerSolution>> problemList = new ArrayList<>();
   
    problemList.add(new ExperimentProblem<>(new NMMin()));

    List<ExperimentAlgorithm<IntegerSolution, List<IntegerSolution>>> algorithmList =
        configureAlgorithmList(problemList);

    Experiment<IntegerSolution, List<IntegerSolution>> experiment =
        new ExperimentBuilder<IntegerSolution, List<IntegerSolution>>("NSGAIIStudy")
            .setAlgorithmList(algorithmList)
            .setProblemList(problemList)
            .setExperimentBaseDirectory(experimentBaseDirectory)
            .setOutputParetoFrontFileName("FUN")
            .setOutputParetoSetFileName("VAR")
            .setReferenceFrontDirectory("resources/referenceFrontsCSV")
            .setIndicatorList(
                Arrays.asList(
                        new Epsilon(),
                        new Spread(),
                        new GenerationalDistance(),
                        new PISAHypervolume(),
                        new NormalizedHypervolume(),
                        new InvertedGenerationalDistance(),
                        new InvertedGenerationalDistancePlus()))
            .setIndependentRuns(INDEPENDENT_RUNS)
            .setNumberOfCores(8)
            .build();

    new ExecuteAlgorithms<>(experiment).run();
    new ComputeQualityIndicators<>(experiment).run();
    new GenerateLatexTablesWithStatistics(experiment).run();
    new GenerateWilcoxonTestTablesWithR<>(experiment).run();
    new GenerateFriedmanTestTables<>(experiment).run();
    new GenerateBoxplotsWithR<>(experiment).setRows(2).setColumns(3).run();
  }

  /**
   * The algorithm list is composed of pairs {@link Algorithm} + {@link Problem} which form part of
   * a {@link ExperimentAlgorithm}, which is a decorator for class {@link Algorithm}. The {@link
   * ExperimentAlgorithm} has an optional tag component, that can be set as it is shown in this
   * example, where four variants of a same algorithm are defined.
   */
  static List<ExperimentAlgorithm<IntegerSolution, List<IntegerSolution>>> configureAlgorithmList(
      List<ExperimentProblem<IntegerSolution>> problemList) {
    List<ExperimentAlgorithm<IntegerSolution, List<IntegerSolution>>> algorithms = new ArrayList<>();

    for (int run = 0; run < INDEPENDENT_RUNS; run++) {
      for (int i = 0; i < problemList.size(); i++) {
        Algorithm<List<IntegerSolution>> algorithm =
            new NSGAIIBuilder<>(
                    problemList.get(i).getProblem(),
                    new IntegerSBXCrossover(1.0, 5),
                    new IntegerPolynomialMutation(
                        1.0 / problemList.get(i).getProblem().getNumberOfVariables(), 10.0),
                    100)
                .setMaxEvaluations(25000)
                .build();
        algorithms.add(new ExperimentAlgorithm<>(algorithm, "NSGAIIa", problemList.get(i), run));
      }
    }
    return algorithms;
  }
}
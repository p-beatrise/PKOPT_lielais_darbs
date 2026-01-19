package org.acme.employeescheduling.rest;

import ai.timefold.solver.benchmark.api.PlannerBenchmark;
import ai.timefold.solver.benchmark.api.PlannerBenchmarkFactory;
import org.acme.employeescheduling.domain.EmployeeSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Benchmark runner for comparing Late Acceptance and Tabu Search algorithms.
 * 
 * This class generates benchmark datasets of different sizes and runs both algorithms
 * to compare their performance in terms of solution quality and solve time.
 * 
 * Run with: mvn exec:java -Dexec.mainClass="org.acme.employeescheduling.rest.BenchmarkRunner"
 */
public class BenchmarkRunner {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkRunner.class);
    
    private final DemoDataGenerator demoDataGenerator;
    
    public BenchmarkRunner() {
        this.demoDataGenerator = new DemoDataGenerator();
    }
    
    /**
     * Main method for standalone execution.
     */
    public static void main(String[] args) {
        LOGGER.info("=".repeat(80));
        LOGGER.info("Employee Scheduling Benchmark");
        LOGGER.info("Comparing Late Acceptance vs Tabu Search");
        LOGGER.info("=".repeat(80));
        
        BenchmarkRunner runner = new BenchmarkRunner();
        runner.runBenchmarks();
        
        LOGGER.info("=".repeat(80));
        LOGGER.info("Benchmark execution completed!");
        LOGGER.info("View results at: target/benchmarks/index.html");
        LOGGER.info("=".repeat(80));
    }
    
    /**
     * Run benchmarks using the benchmarkConfig.xml configuration.
     * Generates small, medium, and large datasets and compares algorithms.
     */
    public void runBenchmarks() {
        LOGGER.info("Starting benchmark execution...");
        
        try {
            // Load benchmark configuration
            PlannerBenchmarkFactory benchmarkFactory = 
                PlannerBenchmarkFactory.createFromXmlResource("benchmarkConfig.xml");
            
            // Generate problem datasets
            List<EmployeeSchedule> problems = generateBenchmarkProblems();
            
            // Create benchmark with problems
            PlannerBenchmark benchmark = benchmarkFactory.buildPlannerBenchmark(
                problems.toArray(new EmployeeSchedule[0])
            );
            
            // Run the benchmark
            benchmark.benchmark();
            
            LOGGER.info("Benchmark completed! Results available in target/benchmarks directory");
            LOGGER.info("Open target/benchmarks/index.html in a browser to view detailed results");
            
        } catch (Exception e) {
            LOGGER.error("Error running benchmarks", e);
            throw new RuntimeException("Benchmark execution failed", e);
        }
    }
    
    /**
     * Generate benchmark problems of different sizes.
     * @return List of EmployeeSchedule problems (small, medium, large)
     */
    private List<EmployeeSchedule> generateBenchmarkProblems() {
        List<EmployeeSchedule> problems = new ArrayList<>();
        
        // Small dataset: ~5 workers, ~3 shifts per day, 3 days
        LOGGER.info("Generating SMALL dataset...");
        problems.add(demoDataGenerator.generateConfigurableDataSet(5, 3, 3));
        
        // Medium dataset: ~20 workers, ~15 shifts per day, 5 days
        LOGGER.info("Generating MEDIUM dataset...");
        problems.add(demoDataGenerator.generateConfigurableDataSet(20, 15, 5));
        
        // Large dataset: ~50 workers, ~30 shifts per day, 7 days
        LOGGER.info("Generating LARGE dataset...");
        problems.add(demoDataGenerator.generateConfigurableDataSet(50, 30, 7));
        
        return problems;
    }
    
    /**
     * Get benchmark results directory.
     */
    public File getBenchmarkResultsDir() {
        return new File("target/benchmarks");
    }
}

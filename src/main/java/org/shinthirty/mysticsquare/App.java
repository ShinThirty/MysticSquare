package org.shinthirty.mysticsquare;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

/**
 * Entry point.
 *
 * @author shinthirty
 */
public class App implements Runnable {

    @Parameter(names = { "-i", "--inputFile" }, description = "Path of input file", required = true)
    private String inputFile;

    @Parameter(names = { "-o", "--outputFile" }, description = "Path of output file", required = true)
    private String outputFile;

    /**
     * Entry point main method.
     *
     * @param args    Command line arguments
     */
    public static void main(final String[] args) {
        long startTime = System.currentTimeMillis();

        App app = new App();
        JCommander jc = JCommander.newBuilder().addObject(app).build();
        try {
            jc.parse(args);
        } catch (ParameterException ex) {
            jc.usage();
            return;
        }

        app.run();

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.format("%dms elapsed%n", elapsedTime);
    }

    @Override
    public void run() {
        PuzzleSolver solver = new PuzzleSolver(inputFile, outputFile);
        solver.solve();
    }
}

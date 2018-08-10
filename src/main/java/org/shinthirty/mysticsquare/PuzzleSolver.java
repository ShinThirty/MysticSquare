package org.shinthirty.mysticsquare;

/**
 * Puzzle solver for Mystic Square puzzle.
 *
 * @author shinthirty
 */
public class PuzzleSolver {

  /**
   * Puzzle to be solved.
   */
  private Puzzle puzzle;

  /**
   * Output file path.
   */
  private String outputFile;

  /**
   * Constructor.
   *
   * @param inputFile     Input file
   * @param outputFile    Output file
   */
  PuzzleSolver(final String inputFile, final String outputFile) {
    this.puzzle = Puzzle.fromFile(inputFile);
    this.outputFile = outputFile;
  }

  /**
   * Solve the puzzle.
   */
  void solve() {

  }

  /**
   * Write the solution to output file.
   *
   * @param solution    Solved puzzle
   */
  private void generateSolution(final Puzzle solution) {

  }

}

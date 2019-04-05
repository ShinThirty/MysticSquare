package org.shinthirty.mysticsquare;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import org.shinthirty.mysticsquare.models.Block;
import org.shinthirty.mysticsquare.models.Direction;
import org.shinthirty.mysticsquare.models.Puzzle;

/**
 * Puzzle solver for Mystic Square puzzle.
 *
 * @author shinthirty
 */
class PuzzleSolver {

  /**
   * Output file path.
   */
  private String outputFile;

  /**
   * The puzzle to be solved.
   */
  private Puzzle puzzle;

  /**
   * Constructor.
   *
   * @param inputFile     Input file
   * @param outputFile    Output file
   * @throws IOException  When input file is invalid
   */
  PuzzleSolver(final String inputFile, final String outputFile) throws IOException {
    puzzle = Puzzle.fromFile(inputFile);
    this.outputFile = outputFile;
  }

  /**
   * Solve the puzzle.
   */
  void solve() {

  }

  /**
   * Get next possible puzzle states from the current puzzle state.
   *
   * @param current    Current puzzle state
   * @return           Next possible puzzle state
   */
  private Collection<Puzzle> nextPuzzles(Puzzle current) {
    List<Puzzle> nextPuzzles = new ArrayList<>();
    Puzzle next;

    for (Block block : current.getBlocks()) {
      for (Direction direction : Direction.values()) {
        next = current.move(block, direction);
        if (next != null && !visited.contains(next)) {
          visited.add(next);
          nextPuzzles.add(next);
        }
      }
    }

    return Collections.unmodifiableCollection(nextPuzzles);
  }

  /**
   * Write the solution to output file.
   *
   * @param solution    Solved puzzle
   */
  private void generateSolution(final Puzzle solution) {
    Deque<Puzzle> steps = new ArrayDeque<>();
    Puzzle current = solution;
    while (current != null) {
      steps.push(current);
      current = current.getPrev();
    }

    try (PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(outputFile), StandardCharsets.UTF_8)))) {
      pw.print("Solution\n");
      int step = 1;
      while (!steps.isEmpty()) {
        current = steps.pop();
        pw.format("%d.\n%s\n", step, current.toString());
        step++;
      }
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    }
  }
}

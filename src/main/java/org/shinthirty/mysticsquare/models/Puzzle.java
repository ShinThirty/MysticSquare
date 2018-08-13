package org.shinthirty.mysticsquare.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.shinthirty.mysticsquare.configs.Configuration;

/**
 * Puzzle board and state.
 *
 * @author shinthirty
 */
@Data
@Builder
public class Puzzle {

  /**
   * List of blocks in the puzzle.
   */
  @NonNull private List<Block> blocks;

  /**
   * Previous puzzle state in the solution steps.
   */
  private Puzzle prev;

  /**
   * A string to represent the puzzle state.
   */
  private String state;

  /**
   * Bitboard value to represent the occupied grids.
   */
  private int occupied;

  /**
   * A string to represent the solved puzzle state.
   */
  private static String solvedState;

  /**
   * Top row.
   */
  private static Bitboard top;

  /**
   * Right column.
   */
  private static Bitboard right;

  /**
   * Bottom row.
   */
  private static Bitboard bottom;

  /**
   * Left column.
   */
  private static Bitboard left;

  /**
   * Create a puzzle from an input file.
   *
   * @param inputFile    Input file path
   * @return             New {@link Puzzle}
   * @throws IOException When input file is invalid
   */
  public static Puzzle fromFile(final String inputFile) throws IOException {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile),
        StandardCharsets.UTF_8))) {
      String[] size = br.readLine().split(" ");
      Configuration.INSTANCE.width = Integer.valueOf(size[0]);
      Configuration.INSTANCE.height = Integer.valueOf(size[1]);

      String line;
      int y = 0;
      List<Block> blocks = new ArrayList<>();
      while ((line = br.readLine()) != null) {
        String[] indexes = line.split(" ");
        for (int x = 0; x < Configuration.INSTANCE.width; x++) {
          String index = indexes[x];
          if (!".".equals(index)) {
            blocks.add(Block.fromIndexAndPosition(Integer.valueOf(index), x, y));
          }
        }

        y++;
      }

      createSolvedState();

      top = Bitboard.draw(0, 0, Configuration.INSTANCE.width-1, 0);
      right = Bitboard.draw(Configuration.INSTANCE.width-1, 0, Configuration.INSTANCE.width-1, Configuration.INSTANCE.height-1);
      bottom = Bitboard.draw(0, Configuration.INSTANCE.height-1, Configuration.INSTANCE.width-1, Configuration.INSTANCE.height-1);
      left = Bitboard.draw(0, 0, 0, Configuration.INSTANCE.height-1);

      return Puzzle.builder().blocks(blocks).occupied(0).build();
    }
  }

  /**
   * Create solved puzzle state.
   */
  private static void createSolvedState() {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    try (PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)))) {
      for (int index = 1; index < Configuration.INSTANCE.width * Configuration.INSTANCE.height;
          index++) {
        pw.format("%02d", index);
      }

      pw.format("%02d", 0);
    }

    solvedState = new String(os.toByteArray(), StandardCharsets.UTF_8);
  }

  /**
   * Retrieve state of the puzzle.
   *
   * @return    Puzzle state as a {@link String}
   */
  public String getState() {
    if (state == null) {
      int[] blockIndexes = new int[Configuration.INSTANCE.width * Configuration.INSTANCE.height];
      for (int i = 0; i < blockIndexes.length; i++) {
        blockIndexes[i] = 0;
      }

      for (Block block : blocks) {
        int x = block.getX();
        int y = block.getY();
        int index = Bitboard.toBitboardIndex(x, y);
        blockIndexes[index] = block.getIndex();
      }

      ByteArrayOutputStream os = new ByteArrayOutputStream();
      try (PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)))) {
        for (int index : blockIndexes) {
          pw.format("%02d", index);
        }
      }

      state = new String(os.toByteArray(), StandardCharsets.UTF_8);
    }

    return state;
  }

  /**
   * Check if current puzzle state is in solved state.
   *
   * @return    True if the puzzle is solved, False otherwise.
   */
  public boolean isSolved() {
    return solvedState.equals(getState());
  }

  /**
   * Create a new puzzle by moving a block towards a direction.
   *
   * @param block        Block to be moved
   * @param direction    Direction
   * @return             New {@link Puzzle} with a block moved, null if such movement is illegal
   */
  public Puzzle move(final Block block, final Direction direction) {
    if (block.overlaps(top) && direction == Direction.UP) {
      return null;
    }

    if (block.overlaps(right) && direction == Direction.RIGHT) {
      return null;
    }

    if (block.overlaps(bottom) && direction == Direction.DOWN) {
      return null;
    }

    if (block.overlaps(left) && direction == Direction.LEFT) {
      return null;
    }

    int occupiedWithoutCurrent = getOccupied() & ~block.getValue();
    int movedBlockValue = block.attemptMove(direction);
    if ((occupiedWithoutCurrent & movedBlockValue) != 0) {
      return null;
    } else {
      List<Block> newBlocks = new ArrayList<>();
      for (Block oldBlock : blocks) {
        Block newBlock = new Block(oldBlock);
        newBlocks.add(newBlock);
        if (newBlock.equals(block)) {
          newBlock.move(direction);
        }
      }

      return Puzzle.builder()
          .blocks(newBlocks)
          .occupied(0)
          .prev(this)
          .build();
    }

  }

  /**
   * Get the bitboard value of occupied grids in the current puzzle state.
   *
   * @return    Bitboard value
   */
  private int getOccupied() {
    if (occupied == 0) {
      for (Block block : blocks) {
        occupied |= block.getValue();
      }
    }

    return occupied;
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) {
      return true;
    }

    if (other == null) {
      return false;
    }

    if (getClass() != other.getClass()) {
      return false;
    }

    Puzzle otherPuzzle = (Puzzle) other;

    return getState().equals(otherPuzzle.getState());
  }

  @Override
  public int hashCode() {
    return getState().hashCode();
  }

  @Override
  public String toString() {
    int[] blockIndexes = new int[Configuration.INSTANCE.width * Configuration.INSTANCE.height];
    for (int i = 0; i < blockIndexes.length; i++) {
      blockIndexes[i] = 0;
    }

    for (Block block : blocks) {
      int x = block.getX();
      int y = block.getY();
      int index = Bitboard.toBitboardIndex(x, y);
      blockIndexes[index] = block.getIndex();
    }

    ByteArrayOutputStream os = new ByteArrayOutputStream();
    try (PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)))) {
      for (int y = 0; y < Configuration.INSTANCE.height; y++) {
        for (int x = 0; x < Configuration.INSTANCE.width; x++) {
          int index = Bitboard.toBitboardIndex(x, y);
          int blockIndex = blockIndexes[index];
          if (blockIndex == 0) {
            pw.print(" .");
          } else {
            pw.format(" %d", blockIndex);
          }
        }

        pw.print("\n");
      }
    }

    return new String(os.toByteArray(), StandardCharsets.UTF_8);
  }
}

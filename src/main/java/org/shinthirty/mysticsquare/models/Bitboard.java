package org.shinthirty.mysticsquare.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.shinthirty.mysticsquare.configs.Configuration;

/**
 * Bitboard definition.
 *
 * @author shinthirty
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
class Bitboard {

  /**
   * Bitboard value.
   */
  private int value;

  /**
   * Check if this bitboard overlaps another one.
   *
   * @param other    The other bitboard
   * @return         True if bitboards overlap, False otherwise
   */
  public boolean overlaps(final Bitboard other) {
    return (value & other.getValue()) != 0;
  }

  /**
   * Convert coordinates to bitboard value.
   *
   * @param x    X
   * @param y    Y
   * @return     Bitboard value
   */
  static int toValue(final int x, final int y) {
    return 1 << toBitboardIndex(x, y);
  }

  /**
   * Convert coordinates to bitboard index.
   *
   * @param x    X
   * @param y    Y
   * @return     Bitboard index
   */
  static int toBitboardIndex(final int x, final int y) {
    return x + y * Configuration.INSTANCE.width;
  }

  /**
   * Get a bitboard representing a rectangular area.
   *
   * @param x1    X coordinate of the uppermost line
   * @param y1    Y coordinate of the leftmost line
   * @param x2    X coordinate of the furthest down line
   * @param y2    Y coordinate of the rightmost line
   * @return      Bitboard
   */
  static Bitboard draw(final int x1, final int y1, final int x2, final int y2) {
    int value = 0;
    for (int y = y1; y <= y2; y++) {
      for (int x = x1; x <= x2; x++) {
        value |= toValue(x, y);
      }
    }

    return new Bitboard(value);
  }

}

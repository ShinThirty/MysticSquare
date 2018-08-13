package org.shinthirty.mysticsquare.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Block definition.
 *
 * @author shinthirty
 */
@Getter
@Setter
@AllArgsConstructor
public class Block extends Bitboard {

  /**
   * Index.
   */
  private int index;

  /**
   * X.
   */
  private int x;

  /**
   * Y.
   */
  private int y;

  /**
   * Copy constructor.
   *
   * @param other    Other block
   */
  Block(final Block other) {
    this.index = other.getIndex();
    this.x = other.getX();
    this.y = other.getY();
    this.setValue(other.getValue());
  }

  /**
   * Attempt move towards direction.
   *
   * @param direction    Direction
   * @return             New bitboard value
   */
  int attemptMove(final Direction direction) {
    int x = direction.getX();
    int y = direction.getY();

    int shift = Bitboard.toBitboardIndex(x, y);
    if (shift >= 0) {
      return getValue() << shift;
    } else {
      return getValue() >>> -shift;
    }
  }

  /**
   * Move towards direction.
   *
   * @param direction    Direction
   */
  void move(final Direction direction) {
    int x = direction.getX();
    int y = direction.getY();
    int value = attemptMove(direction);

    this.x += x;
    this.y += y;
    setValue(value);
  }

  /**
   * Create a block by index, coordinates x and y.
   *
   * @param index    Index
   * @param x        X
   * @param y        Y
   * @return         {@link Block}
   */
  static Block fromIndexAndPosition(final int index, final int x, final int y) {
    Block block = new Block(index, x, y);
    block.setValue(Bitboard.toValue(x, y));
    return block;
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

    Block otherBlock = (Block) other;

    return index == otherBlock.getIndex() && x == otherBlock.getX() && y == otherBlock.getY();
  }

  @Override
  public int hashCode() {
    return index;
  }
}

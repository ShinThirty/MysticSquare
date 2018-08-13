package org.shinthirty.mysticsquare.models;

import lombok.Getter;

/**
 * Direction definition.
 *
 * @author shinthirty
 */
@Getter
public enum Direction {
  UP(0, -1),
  RIGHT(1, 0),
  DOWN(0, 1),
  LEFT(-1, 0);

  /**
   * X.
   */
  private int x;

  /**
   * Y.
   */
  private int y;

  /**
   * Constructor.
   *
   * @param x    X
   * @param y    Y
   */
  Direction(final int x, final int y) {
    this.x = x;
    this.y = y;
  }
}

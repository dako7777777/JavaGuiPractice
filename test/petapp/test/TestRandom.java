package petapp.test;

import java.util.Random;

/**
 * A custom Random implementation that allows controlling random values for testing.
 */
public class TestRandom extends Random {
  private float nextFloatValue = 0.5f;
  private int[] nextIntValues = new int[]{0};
  private int nextIntIndex = 0;

  /**
   * Default constructor.
   */
  public TestRandom() {
    super(0); // Use a fixed seed for determinism
  }

  /**
   * Sets the value to be returned by nextFloat().
   *
   * @param value The value to return
   */
  public void setNextFloat(float value) {
    this.nextFloatValue = value;
  }

  /**
   * Sets the values to be returned by nextInt() in sequence.
   *
   * @param values Array of values to return
   */
  public void setNextIntValues(int[] values) {
    this.nextIntValues = values;
    this.nextIntIndex = 0;
  }

  @Override
  public float nextFloat() {
    return nextFloatValue;
  }

  @Override
  public int nextInt(int bound) {
    // Get the current value and advance to the next
    int value = nextIntValues[nextIntIndex];
    nextIntIndex = (nextIntIndex + 1) % nextIntValues.length;

    // Ensure the value is within the bound
    return Math.abs(value) % bound;
  }
}
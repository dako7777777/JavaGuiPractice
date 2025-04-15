package petapp.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import petapp.test.TestRandom;

/**
 * Tests for the AnxietyMoodStrategy.
 */
public class AnxietyMoodStrategyTest {
  private Pet pet;
  private TestRandom testRandom;
  private AnxietyMoodStrategy strategy;

  /**
   * Construct AnxietyMood.
   */
  @Before
  public void setUp() {
    // Create a testable Random object
    testRandom = new TestRandom();

    // Use the test random in both the pet and strategy
    pet = new Pet(testRandom);
    strategy = new AnxietyMoodStrategy(testRandom);

    // Set pet to anxiety mood with our test strategy
    pet.setMoodStrategy(strategy);

    // Initialize test values
    pet.setHunger(50);
    pet.setSocial(50);
    pet.setHygiene(50);
    pet.setSleep(50);
  }

  @Test
  public void testHugAction() {
    // Hug should not use random values, so it's predictable
    int initialSocial = pet.getSocial();
    strategy.applyAction(pet, Action.HUG);

    // Social should decrease by 30
    assertEquals(Math.max(0, initialSocial - 30), pet.getSocial());

    // Other stats should remain unchanged
    assertEquals(50, pet.getHunger());
    assertEquals(50, pet.getHygiene());
    assertEquals(50, pet.getSleep());
  }

  @Test
  public void testFeedActionWithPositiveRandom() {
    // Control random to give a positive random ratio of 0.5
    // nextFloat() returns value between 0-1, then we calculate (value*2)-1 to get -1 to 1
    // So for 0.5 random ratio, nextFloat should return 0.75
    testRandom.setNextFloat(0.75f);

    strategy.applyAction(pet, Action.FEED);

    // For hunger: 50 - (15 * 0.75 * 0.5) = 50 - 5.625 = 44
    assertEquals(44, pet.getHunger());

    // For social: 50 + (5 * 0.75 * 0.5) = 50 + 1.875 = 52
    assertEquals(52, pet.getSocial());

    // For hygiene: 50 + (3 * 0.75 * 0.5) = 50 + 1.125 = 51
    assertEquals(51, pet.getHygiene());

    // For sleep: 50 + (5 * 0.75 * 0.5) = 50 + 1.875 = 52
    assertEquals(52, pet.getSleep());
  }

  @Test
  public void testFeedActionWithNegativeRandom() {
    // Control random to give a negative random ratio of -0.5
    // For -0.5 random ratio, nextFloat should return 0.25
    testRandom.setNextFloat(0.25f);

    strategy.applyAction(pet, Action.FEED);

    // For hunger: 50 - (15 * 0.75 * -0.5) = 50 + 5.625 = 56
    assertEquals(56, pet.getHunger());

    // For social: 50 + (5 * 0.75 * -0.5) = 50 - 1.875 = 48
    assertEquals(48, pet.getSocial());

    // For hygiene: 50 + (3 * 0.75 * -0.5) = 50 - 1.125 = 49
    assertEquals(49, pet.getHygiene());

    // For sleep: 50 + (5 * 0.75 * -0.5) = 50 - 1.875 = 48
    assertEquals(48, pet.getSleep());
  }

  @Test
  public void testUpdateStatusWithControlledRandom() {
    // Control the random values
    // Set social change to +3 (10-7) and sleep change to -5 (5-10)
    testRandom.setNextIntValues(new int[]{10, 5});

    strategy.updateStatus(pet);

    // Verify expected changes
    assertEquals(57, pet.getHunger());  // +7 (fixed)
    assertEquals(53, pet.getHygiene()); // +3 (fixed)
    assertEquals(53, pet.getSocial());  // +3 (from 10-7)
    assertEquals(45, pet.getSleep());   // -5 (from 5-10)
  }

  @Test
  public void testMoodModifierWithThreeStatsBelow50() {
    // Set 3 values below 50
    pet.setHunger(40);
    pet.setSocial(40);
    pet.setSleep(40);

    // Should change to HAPPY
    assertEquals(MoodEnum.HAPPY, strategy.moodModifier(pet));
  }

  @Test
  public void testMoodModifierWithTwoStatsBelow50() {
    // Set only 2 values below 50
    pet.setHunger(40);
    pet.setSocial(40);
    pet.setSleep(60);
    pet.setHygiene(60);

    // Should remain anxious
    assertEquals(MoodEnum.ANXIETY, strategy.moodModifier(pet));
  }

  @Test
  public void testHugClearsRandomEffects() {
    // First apply a hug
    strategy.applyAction(pet, Action.HUG);

    // Feed should now use a ratio of 1.0 instead of random
    // For hunger: 50 - (15 * 0.75 * 1.0) = 50 - 11.25 = 39
    strategy.applyAction(pet, Action.FEED);
    assertEquals(39, pet.getHunger());

    // Step to reset the hug effect
    testRandom.setNextIntValues(new int[]{7, 10}); // No social change, no sleep change
    strategy.updateStatus(pet);

    // Now random should be back in effect
    testRandom.setNextFloat(0.25f); // -0.5 ratio

    // Apply feed again - should use random now
    // For hunger: 46 - (15 * 0.75 * -0.5) = 46 + 5.625 = 52
    strategy.applyAction(pet, Action.FEED);
    assertEquals(52, pet.getHunger());
  }
}
package petapp.model;

import java.util.Random;

/**
 * Implementation of the MoodStrategy for an anxious pet.
 */
public class AnxietyMoodStrategy implements MoodStrategy {
  private final Random random;
  private boolean hugApplied = false;

  /**
   * Default constructor that uses a standard Random instance.
   */
  public AnxietyMoodStrategy() {
    this(new Random());
  }

  /**
   * Test constructor that allows injecting a Random for testing.
   *
   * @param random The Random instance to use
   */
  public AnxietyMoodStrategy(Random random) {
    this.random = random;
  }

  @Override
  public void applyAction(Pet pet, Action action) {
    if (action == Action.HUG) {
      // HUG greatly decreases social need but doesn't affect other statuses
      pet.setSocial(Math.max(0, pet.getSocial() - 30));
      hugApplied = true;
      return;
    }

    int hunger = pet.getHunger();
    int social = pet.getSocial();
    int hygiene = pet.getHygiene();
    int sleep = pet.getSleep();

    // Generate a random ratio between -1.0 and 1.0 if hug hasn't been applied
    float randomRatio = hugApplied ? 1.0f : (random.nextFloat() * 2.0f - 1.0f);

    // Apply action with the random ratio based on happy values * 0.75
    switch (action) {
      case FEED:
        // Using HappyMoodStrategy values as base
        hunger = Math.max(0, hunger - Math.round(15 * 0.75f * randomRatio));
        social = Math.min(100, social + Math.round(5 * 0.75f * randomRatio));
        hygiene = Math.min(100, hygiene + Math.round(3 * 0.75f * randomRatio));
        sleep = Math.min(100, sleep + Math.round(5 * 0.75f * randomRatio));
        break;
      case PLAY:
        hunger = Math.min(100, hunger + Math.round(5 * 0.75f * randomRatio));
        social = Math.max(0, social - Math.round(10 * 0.75f * randomRatio));
        hygiene = Math.min(100, hygiene + Math.round(3 * 0.75f * randomRatio));
        sleep = Math.min(100, sleep + Math.round(10 * 0.75f * randomRatio));
        break;
      case CLEAN:
        hygiene = Math.max(0, hygiene - Math.round(15 * 0.75f * randomRatio));
        social = Math.min(100, social + Math.round(3 * 0.75f * randomRatio));
        sleep = Math.min(100, sleep + Math.round(10 * 0.75f * randomRatio));
        break;
      case SLEEP:
        sleep = Math.max(0, sleep - Math.round(30 * 0.75f * randomRatio));
        social = Math.max(0, social - Math.round(10 * 0.75f * randomRatio));
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + action);
    }

    pet.setHunger(hunger);
    pet.setSocial(social);
    pet.setHygiene(hygiene);
    pet.setSleep(sleep);
  }

  @Override
  public void updateStatus(Pet pet) {
    // Hunger increases by 7
    pet.setHunger(Math.min(100, pet.getHunger() + 7));

    // Social randomly changes within (-7, 7)
    int socialChange = random.nextInt(15) - 7; // range from -7 to 7
    pet.setSocial(Math.max(0, Math.min(100, pet.getSocial() + socialChange)));

    // Hygiene increases by 3
    pet.setHygiene(Math.min(100, pet.getHygiene() + 3));

    // Sleep randomly changes within (-10, 10)
    int sleepChange = random.nextInt(21) - 10; // range from -10 to 10
    pet.setSleep(Math.max(0, Math.min(100, pet.getSleep() + sleepChange)));

    // Reset hug effect after each update
    hugApplied = false;
  }

  @Override
  public MoodEnum moodModifier(Pet pet) {
    // Count how many health stats are under 50
    int lowStatusCount = 0;
    if (pet.getHunger() < 50) {
      lowStatusCount++;
    }
    if (pet.getSocial() < 50) {
      lowStatusCount++;
    }
    if (pet.getHygiene() < 50) {
      lowStatusCount++;
    }
    if (pet.getSleep() < 50) {
      lowStatusCount++;
    }

    // If any 3 of the health statuses are under 50, anxiety disappears
    if (lowStatusCount >= 3) {
      return MoodEnum.HAPPY;
    }

    // Otherwise, stay anxious
    return MoodEnum.ANXIETY;
  }

  /**
   * For testing: get the hug applied status.
   */
  boolean isHugApplied() {
    return hugApplied;
  }
}
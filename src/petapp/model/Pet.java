package petapp.model;

import java.util.Random;

/**
 * Main Pet class implementing the PetInterface.
 * This class represents a virtual pet with various needs and moods.
 */

public class Pet implements PetInterface {
  // Health status fields, representing different need.
  // All between 0-100 and the lower, the better.
  private int hunger;
  private int hygiene;
  private int social;
  private int sleep;

  // Mood and death states
  private MoodEnum mood;
  private boolean dead;

  // Strategy pattern for mood-based behavior
  private MoodStrategy moodStrategy;

  // Random number generator for anxiety check
  private final Random random = new Random();

  /**
   * Constructor that initializes the pet with default values.
   */
  public Pet() {
    // Initialize with default values from document
    this.hunger = 20;
    this.hygiene = 60;
    this.social = 60;
    this.sleep = 15;
    this.mood = MoodEnum.HAPPY;
    this.dead = false;

    // Default to happy mood strategy
    this.moodStrategy = new HappyMoodStrategy();
  }

  /**
   * Advances the pet's internal state by one unit of time.
   */
  @Override
  public void step() {
    if (dead) {
      return;
    }

    // Update status based on current mood strategy
    moodStrategy.updateStatus(this);

    // Check and update mood
    MoodEnum newMood = moodStrategy.moodModifier(this);
    setMood(newMood);

    // Random chance to become anxious if not already
    checkAnxiety();

    // Check if pet should die
    checkDeath();
  }

  /**
   * The pet receives an interaction which affects its health status.
   */
  @Override
  public void interactWith(Action action) {
    if (dead) {
      return;
    }

    // Apply the action based on current mood strategy
    moodStrategy.applyAction(this, action);

    // Check and update mood after interaction
    MoodEnum newMood = moodStrategy.moodModifier(this);
    setMood(newMood);

    // Random chance to become anxious if not already
    checkAnxiety();

    // Check if pet should die after interaction
    checkDeath();
  }

  /**
   * Returns an instance of HealthStatus that encapsulates all relevant health data.
   */
  @Override
  public HealthStatus getHealth() {
    return new HealthStatus(mood, dead, hunger, hygiene, social, sleep);
  }

  /**
   * Allows the pet's mood to be set manually and updates the strategy accordingly.
   */
  @Override
  public void setMood(MoodEnum mood) {
    this.mood = mood;

    // Update strategy based on mood
    switch (mood) {
      case HAPPY:
        this.moodStrategy = new HappyMoodStrategy();
        break;
      case SAD:
        this.moodStrategy = new SadMoodStrategy();
        break;
      case ANXIETY:
        this.moodStrategy = new AnxietyMoodStrategy();
        break;
      default:
        throw new IllegalStateException("Unexpected mood: " + mood);
    }
  }

  /**
   * Returns the current mood of the pet.
   */
  @Override
  public MoodEnum getMood() {
    return mood;
  }

  /**
   * Checks if the pet should die based on its current status.
   */
  private void checkDeath() {
    // Pet dies if both hunger and sleep are critically high
    if (hunger > 95 && sleep > 95) {
      dead = true;
    }
  }

  /**
   * Checks if the pet should become anxious based on its current status.
   */
  private void checkAnxiety() {
    // Only check if not already anxious
    if (mood != MoodEnum.ANXIETY) {
      boolean badCondition = hunger > 60 && sleep > 60;
      int maxProbability = badCondition ? 50 : 20;

      // Random chance based on condition
      if (random.nextInt(100) < maxProbability) {
        setMood(MoodEnum.ANXIETY);
      }
    }
  }

  // Getter and setter methods for health status fields

  public int getHunger() {
    return hunger;
  }

  public void setHunger(int hunger) {
    this.hunger = Math.max(0, Math.min(100, hunger));
  }

  public int getHygiene() {
    return hygiene;
  }

  public void setHygiene(int hygiene) {
    this.hygiene = Math.max(0, Math.min(100, hygiene));
  }

  public int getSocial() {
    return social;
  }

  public void setSocial(int social) {
    this.social = Math.max(0, Math.min(100, social));
  }

  public int getSleep() {
    return sleep;
  }

  public void setSleep(int sleep) {
    this.sleep = Math.max(0, Math.min(100, sleep));
  }

  public boolean isDead() {
    return dead;
  }
}
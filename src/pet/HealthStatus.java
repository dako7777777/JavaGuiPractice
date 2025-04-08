package pet;

/**
 * Class to encapsulate the pet's health-related data.
 */

public record HealthStatus(MoodEnum mood, boolean dead,
                           int hunger, int hygiene, int social, int sleep) {

  @Override
  public String toString() {
    return "HealthStatus{"
        + "mood = " + mood
        + ", alive = " + !dead
        + ", hunger = " + hunger
        + ", hygiene = " + hygiene
        + ", social = " + social
        + ", sleep = " + sleep
        + '}';
  }
}
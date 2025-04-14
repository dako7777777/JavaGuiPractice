package petapp.model;

/**
 * Class to encapsulate the pet's health-related data.
 */

public record HealthStatus(MoodEnum mood, boolean dead,
                           int hunger, int hygiene, int social, int sleep) {

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("HealthStatus{")
        .append("mood = ").append(mood)
        .append(", alive = ").append(!dead)
        .append(", hunger = ").append(hunger)
        .append(", hygiene = ").append(hygiene)
        .append(", social = ").append(social)
        .append(", sleep = ").append(sleep)
        .append('}');
    return sb.toString();
  }
}
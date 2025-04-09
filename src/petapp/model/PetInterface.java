package petapp.model;

/**
 * Official pet interface.
 */

public interface PetInterface {
  void step();

  void interactWith(Action action);

  HealthStatus getHealth();

  void setMood(MoodEnum mood);

  MoodEnum getMood();
}

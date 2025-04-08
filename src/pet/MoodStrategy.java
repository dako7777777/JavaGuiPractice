package pet;

/**
 * Interface for the Strategy pattern that defines behaviors based on the pet's mood.
 */

public interface MoodStrategy {
  void applyAction(Pet pet, Action action);

  void updateStatus(Pet pet);

  MoodEnum moodModifier(Pet pet);
}
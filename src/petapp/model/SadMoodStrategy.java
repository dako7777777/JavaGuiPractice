package petapp.model;

/**
 * Implementation of the MoodStrategy for a sad pet.
 */

public class SadMoodStrategy implements MoodStrategy {
  @Override
  public void applyAction(Pet pet, Action action) {
    int hunger = pet.getHunger();
    int social = pet.getSocial();
    int hygiene = pet.getHygiene();
    int sleep = pet.getSleep();

    switch (action) {
      case FEED:
        hunger = Math.max(0, hunger - 20);
        social = Math.min(100, social + 1);
        hygiene = Math.min(100, hygiene + 1);
        sleep = Math.min(100, sleep + 1);
        break;
      case PLAY:
        hunger = Math.min(100, hunger + 1);
        social = Math.max(0, social - 15);
        hygiene = Math.min(100, hygiene + 1);
        sleep = Math.min(100, sleep + 1);
        break;
      case CLEAN:
        hygiene = Math.max(0, hygiene - 20);
        social = Math.min(100, social + 1);
        sleep = Math.min(100, sleep + 1);
        break;
      case SLEEP:
        sleep = Math.max(0, sleep - 35);
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
    // For a sad pet, status values change by 5 each hour.
    pet.setHunger(Math.min(100, pet.getHunger() + 5));
    pet.setSocial(Math.min(100, pet.getSocial() + 5));
    pet.setHygiene(Math.min(100, pet.getHygiene() + 3));
    pet.setSleep(Math.min(100, pet.getSleep() + 5));
  }

  @Override
  public MoodEnum moodModifier(Pet pet) {
    // Same mood modifying logic for both strategies.
    int problemCount = 0;
    if (pet.getSocial() > 60) {
      problemCount++;
    }
    if (pet.getHygiene() > 60) {
      problemCount++;
    }
    if (pet.getHunger() > 60) {
      problemCount++;
    }
    if (pet.getSleep() > 60) {
      problemCount++;
    }

    // If 2 or more statuses are problematic, change to SAD
    return problemCount >= 2 ? MoodEnum.SAD : MoodEnum.HAPPY;
  }
}
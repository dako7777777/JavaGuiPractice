package pet;

/**
 * Implementation of the MoodStrategy for a happy pet.
 */

public class HappyMoodStrategy implements MoodStrategy {
  @Override
  public void applyAction(Pet pet, Action action) {
    int hunger = pet.getHunger();
    int social = pet.getSocial();
    int hygiene = pet.getHygiene();
    int sleep = pet.getSleep();

    switch (action) {
      case FEED:
        hunger = Math.max(0, hunger - 15);
        social = Math.min(100, social + 5);
        hygiene = Math.min(100, hygiene + 3);
        sleep = Math.min(100, sleep + 5);
        break;
      case PLAY:
        hunger = Math.min(100, hunger + 5);
        social = Math.max(0, social - 10);
        hygiene = Math.min(100, hygiene + 3);
        sleep = Math.min(100, sleep + 10);
        break;
      case CLEAN:
        hygiene = Math.max(0, hygiene - 15);
        social = Math.min(100, social + 3);
        sleep = Math.min(100, sleep + 10);
        break;
      case SLEEP:
        sleep = Math.max(0, sleep - 30);
        social = Math.max(0, social - 10);
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
    // For a happy pet, status values change by 1 each hour
    pet.setHunger(Math.min(100, pet.getHunger() + 2));
    pet.setSocial(Math.min(100, pet.getSocial() + 2));
    pet.setHygiene(Math.min(100, pet.getHygiene() + 1));
    pet.setSleep(Math.min(100, pet.getSleep() + 1));
  }

  @Override
  public MoodEnum moodModifier(Pet pet) {
    // Same mood modifying logic for both strategies
    // High value is bad
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


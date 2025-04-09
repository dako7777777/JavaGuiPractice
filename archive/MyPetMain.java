package resources;

import petapp.model.Action;
import petapp.model.MoodEnum;
import petapp.model.Pet;
import petapp.model.PetInterface;

import java.util.Scanner;

/**
 * The resources.MyPetMain class is the entry point for the Virtual Pet Care application.
 * It allows users to interact with a virtual pet by issuing commands to
 * play, feed, clean, put to sleep, or advance the pet's state.
 */
public class MyPetMain {

  /**
   * The main method is the entry point of the Virtual Pet Care application.
   * It initializes the pet and scanner, prints the menu, and processes user commands in a loop.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args) {
    PetInterface pet = new Pet();
    Scanner scanner = new Scanner(System.in);

    printMenu();
    displayFullStatus(pet);

    while (scanner.hasNext()) {
      String command = scanner.next();
      int times = 1; // Default to 1 if no number is provided

      // Check if there's another token (the times' parameter)
      if (scanner.hasNextInt()) {
        times = scanner.nextInt();
      }

      switch (command) {
        case "p":
          for (int i = 0; i < times; i++) {
            pet.interactWith(Action.PLAY);
          }
          System.out.println("Played with pet " + times + " time(s)");
          break;
        case "f":
          for (int i = 0; i < times; i++) {
            pet.interactWith(Action.FEED);
          }
          System.out.println("Fed pet " + times + " time(s)");
          break;
        case "c":
          for (int i = 0; i < times; i++) {
            pet.interactWith(Action.CLEAN);
          }
          System.out.println("Cleaned pet " + times + " time(s)");
          break;
        case "s":
          for (int i = 0; i < times; i++) {
            pet.interactWith(Action.SLEEP);
          }
          System.out.println("Put pet to sleep " + times + " time(s)");
          break;
        case "h":
          for (int i = 0; i < times; i++) {
            pet.interactWith(Action.HUG);
          }
          System.out.println("Hugged pet " + times + " time(s)");
          break;
        case "a":
          for (int i = 0; i < times; i++) {
            pet.step();
          }
          System.out.println("Advanced pet state " + times + " step(s)");
          break;
        case "x":
          runSimulation(pet, times);
          System.out.println("Ran simulation for " + times + " step(s)");
          break;
        case "m":
          // Set mood command
          if (scanner.hasNext()) {
            String moodStr = scanner.next().toUpperCase();
            try {
              MoodEnum mood = MoodEnum.valueOf(moodStr);
              pet.setMood(mood);
              System.out.println("Set pet mood to " + mood);
            } catch (IllegalArgumentException e) {
              System.out.println("Invalid mood. Valid moods are: HAPPY, SAD, ANXIETY");
            }
          }
          break;
        case "t":
          // Test anxiety behavior
          testAnxietyBehavior(pet, times);
          break;
        case "help":
          printMenu();
          break;
        default:
          System.out.println("Unknown command. Type 'help' for menu.");
          continue;
      }

      displayFullStatus(pet);
    }
  }

  /**
   * Prints the menu of commands for the Virtual Pet Care application.
   */
  private static void printMenu() {
    System.out.println("\n========= Virtual Pet Care =========");
    System.out.println("Commands:");
    System.out.println("p [n] - Play n times (default 1)");
    System.out.println("f [n] - Feed n items of food (default 1)");
    System.out.println("c [n] - Clean n times (default 1)");
    System.out.println("s [n] - Put pet to sleep n times (default 1)");
    System.out.println("h [n] - Hug pet n times (default 1)");
    System.out.println("a [n] - Advance pet state n steps (default 1)");
    System.out.println("x [n] - Execute simulation for n steps (default 1)");
    System.out.println("m [MOOD] - Set pet mood (HAPPY, SAD, ANXIETY)");
    System.out.println("t [n] - Run anxiety test for n cycles (default 1)");
    System.out.println("help - Display this menu");
    System.out.println("===================================\n");
  }

  /**
   * Displays detailed status information about the pet.
   *
   * @param pet the pet to display status for
   */
  private static void displayFullStatus(PetInterface pet) {
    System.out.println("\n--- Pet Status ---");
    System.out.println("Current Mood: " + pet.getMood());

    // Get individual status values
    var health = pet.getHealth();
    System.out.println("Hunger: " + health.hunger() + " (Lower is better)");
    System.out.println("Hygiene: " + health.hygiene() + " (Lower is better)");
    System.out.println("Social: " + health.social() + " (Lower is better)");
    System.out.println("Sleep: " + health.sleep() + " (Lower is better)");
    System.out.println("Alive: " + !health.dead());

    // Display anxiety-specific notes when applicable
    if (pet.getMood() == MoodEnum.ANXIETY) {
      System.out.println("\n*ANXIOUS PET NOTES*");
      System.out.println("- Actions may have unpredictable effects");
      System.out.println("- Try hugging the pet ('h' command)");
      System.out.println("- Anxiety will go away when 3+ stats are below 50");
    }

    System.out.println("-----------------\n");
  }

  /**
   * Runs a simulation that interacts with the pet for a significant number of steps.
   *
   * @param pet   the pet to interact with
   * @param steps the number of steps to simulate
   */
  private static void runSimulation(PetInterface pet, int steps) {
    for (int i = 0; i < steps; i++) {
      pet.step();
      if (i % 5 == 0) {
        pet.interactWith(Action.FEED);
      }
      if (i % 10 == 0) {
        pet.interactWith(Action.PLAY);
      }
      if (i % 15 == 0) {
        pet.interactWith(Action.CLEAN);
      }
      if (i % 20 == 0) {
        pet.interactWith(Action.SLEEP);
      }

      // Add hug when the pet is anxious
      if (pet.getMood() == MoodEnum.ANXIETY && i % 8 == 0) {
        pet.interactWith(Action.HUG);
      }
    }
  }

  /**
   * Runs a test specifically designed to demonstrate anxiety behavior.
   *
   * @param pet the pet to test
   * @param cycles number of test cycles to run
   */
  private static void testAnxietyBehavior(PetInterface pet, int cycles) {
    System.out.println("\n----- ANXIETY TEST STARTED -----");

    // First force the pet into anxiety mode
    pet.setMood(MoodEnum.ANXIETY);
    System.out.println("Pet forced into ANXIETY mode");
    displayFullStatus(pet);

    // For each cycle, test actions with and without hugging
    for (int cycle = 0; cycle < cycles; cycle++) {
      System.out.println("\n--- Test Cycle " + (cycle + 1) + " ---");

      // Test FEED without hugging
      System.out.println("Testing FEED without hugging first:");
      var beforeFeed = pet.getHealth();
      pet.interactWith(Action.FEED);
      var afterFeed = pet.getHealth();
      System.out.println("Hunger before: " + beforeFeed.hunger() + ", after: " + afterFeed.hunger());

      // Reset to anxiety for next test
      pet.setMood(MoodEnum.ANXIETY);

      // Test HUG followed by FEED
      System.out.println("\nTesting HUG followed by FEED:");
      pet.interactWith(Action.HUG);
      var beforeFeedAfterHug = pet.getHealth();
      pet.interactWith(Action.FEED);
      var afterFeedAfterHug = pet.getHealth();
      System.out.println("Hunger before: " + beforeFeedAfterHug.hunger() + ", after: " + afterFeedAfterHug.hunger());

      // Reset to anxiety for next cycle
      pet.setMood(MoodEnum.ANXIETY);
    }

    System.out.println("\n----- ANXIETY TEST COMPLETED -----");
  }
}
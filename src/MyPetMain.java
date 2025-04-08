import pet.Action;
import pet.Pet;
import pet.PetInterface;

import java.util.Scanner;

/**
 * The MyPetMain class is the entry point for the Virtual Pet Care application.
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

    while (scanner.hasNext()) {
      String command = scanner.next();
      int times = scanner.nextInt();

      switch (command) {
        case "p":
          for (int i = 0; i < times; i++) {
            pet.interactWith(Action.PLAY);
          }
          break;
        case "f":
          for (int i = 0; i < times; i++) {
            pet.interactWith(Action.FEED);
          }
          break;
        case "c":
          for (int i = 0; i < times; i++) {
            pet.interactWith(Action.CLEAN);
          }
          break;
        case "s":
          for (int i = 0; i < times; i++) {
            pet.interactWith(Action.SLEEP);
          }
          break;
        case "a":
          for (int i = 0; i < times; i++) {
            pet.step();
          }
          break;
        case "x":
          runSimulation(pet, times);
          break;
        default:
          System.out.println("Unknown command.");
          continue;
      }

      System.out.println("Updated Pet Status: " + pet.getHealth());
      System.out.println("Current Mood: " + pet.getMood());
    }
  }

  /**
   * Prints the menu of commands for the Virtual Pet Care application.
   */
  private static void printMenu() {
    System.out.println("Welcome to Virtual Pet Care! Type a command:");
    System.out.println("Commands:");
    System.out.println("p n  - Play n times");
    System.out.println("f n  - Feed n items of food");
    System.out.println("c n  - Clean n times");
    System.out.println("s n  - Put pet to sleep n times");
    System.out.println("a n  - Advance pet state n steps");
    System.out.println("x n  - Execute simulation for n steps");
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
    }
  }
}
package petapp;

import petapp.controller.PetController;
import petapp.model.Pet;
import petapp.model.PetInterface;
import petapp.view.PetView;

/**
 * The main entry point for the Virtual Pet application.
 * Creates and connects the MVC components.
 */
public class Main {

  /**
   * Application entry point.
   *
   * @param args command line arguments (not used)
   */
  public static void main(String[] args) {
    // Create MVC components
    PetInterface pet = new Pet();
    PetView view = new PetView();
    PetController controller = new PetController(pet, view);
  }
}
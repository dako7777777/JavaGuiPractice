package petapp.controller;

import javax.swing.JMenuItem;
import javax.swing.Timer;
import petapp.model.Action;
import petapp.model.HealthStatus;
import petapp.model.MoodEnum;
import petapp.model.PetInterface;
import petapp.view.PetView;

/**
 * The PetController class manages communication between the pet model and view.
 * It handles user inputs from the view, updates the model accordingly,
 * and refreshes the view to reflect the current state of the model.
 */
public class PetController {
  private final PetInterface pet;
  private final PetView view;

  // Timer for automatic step updates
  private Timer timer;
  private static final int DEFAULT_TIMER_DELAY = 2000; // 2 seconds

  /**
   * Constructs a new controller for the pet application.
   *
   * @param pet the model
   * @param view the view
   */
  public PetController(PetInterface pet, PetView view) {
    this.pet = pet;
    this.view = view;

    // Attach action listeners to buttons
    view.getFeedButton().addActionListener(e -> handleInteraction(Action.FEED));
    view.getPlayButton().addActionListener(e -> handleInteraction(Action.PLAY));
    view.getCleanButton().addActionListener(e -> handleInteraction(Action.CLEAN));
    view.getSleepButton().addActionListener(e -> handleInteraction(Action.SLEEP));
    view.getHugButton().addActionListener(e -> handleInteraction(Action.HUG));
    view.getStepButton().addActionListener(e -> stepGame());

    // Set up mood menu item listener
    view.setMoodMenuItemListener(e -> {
      JMenuItem source = (JMenuItem) e.getSource();
      try {
        MoodEnum mood = MoodEnum.valueOf(source.getText());
        setMood(mood);
      } catch (IllegalArgumentException ex) {
        System.err.println("Invalid mood: " + source.getText());
      }
    });

    // Set up timer for automatic step updates
    timer = new Timer(DEFAULT_TIMER_DELAY, e -> stepGame());
    timer.start();

    // Initial view update
    updateView();
  }

  /**
   * Handles a pet interaction action from the view.
   *
   * @param action the action to perform
   */
  private void handleInteraction(Action action) {
    pet.interactWith(action);
    updateView();
  }

  /**
   * Advances the pet's state by one step.
   */
  private void stepGame() {
    pet.step();
    updateView();
  }

  /**
   * Updates the view to reflect the current model state.
   */
  private void updateView() {
    HealthStatus health = pet.getHealth();

    // Update health stats
    view.updateHealth(
        health.hunger(),
        health.hygiene(),
        health.social(),
        health.sleep(),
        !health.dead()
    );

    // Update mood and corresponding image
    view.updateMood(pet.getMood().name());
    view.updateImage(pet.getMood().name());

    // Show/hide HUG button based on mood
    view.setHugButtonVisible(pet.getMood() == MoodEnum.ANXIETY);
  }

  /**
   * Sets the pet's mood.
   *
   * @param mood the mood to set
   */
  public void setMood(MoodEnum mood) {
    pet.setMood(mood);
    updateView();
  }
}
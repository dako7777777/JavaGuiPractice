package petapp.controller;

import javax.swing.JMenuItem;
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

  // Used to track previous status values for feedback
  private int prevHunger;
  private int prevHygiene;
  private int prevSocial;
  private int prevSleep;
  private MoodEnum prevMood;

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
    view.getExitButton().addActionListener(e -> exitGame());

    // Set up mood menu item listener (for developer testing)
    view.setMoodMenuItemListener(e -> {
      JMenuItem source = (JMenuItem) e.getSource();
      String moodText = source.getText();

      if (moodText.equals("DEAD")) {
        // Special case for DEAD
        killPet();
      } else {
        try {
          MoodEnum mood = MoodEnum.valueOf(moodText);
          setMood(mood);
        } catch (IllegalArgumentException ex) {
          System.err.println("Invalid mood: " + moodText);
        }
      }
    });

    // Initialize previous status values
    HealthStatus initialHealth = pet.getHealth();
    prevHunger = initialHealth.hunger();
    prevHygiene = initialHealth.hygiene();
    prevSocial = initialHealth.social();
    prevSleep = initialHealth.sleep();
    prevMood = initialHealth.mood();

    // Initial view update
    updateView();

    // Add initial hints based on pet state
    view.addMessage("❓ HINTS ❓");
    addHintBasedOnStatus();
  }

  /**
   * Handles a pet interaction action from the view.
   *
   * @param action the action to perform
   */
  private void handleInteraction(Action action) {
    if (pet.getHealth().dead()) {
      view.addMessage("☠️ Your pet is dead. No actions can be taken.");
      return;
    }

    // Store pre-action values
    HealthStatus beforeStatus = pet.getHealth();
    prevHunger = beforeStatus.hunger();
    prevHygiene = beforeStatus.hygiene();
    prevSocial = beforeStatus.social();
    prevSleep = beforeStatus.sleep();
    prevMood = beforeStatus.mood();

    // Perform the action
    pet.interactWith(action);

    // Update the view
    updateView();

    // Add a separator line
    view.addMessage("\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

    // Provide feedback on the action
    provideFeedback(action);

    // Check for mood change first (more important)
    checkMoodChange();

    // Add hints based on current status
    if (!pet.getHealth().dead()) {
      view.addMessage("\n❓ HINTS ❓");
      addHintBasedOnStatus();
    }
  }

  /**
   * Advances the pet's state by one step.
   */
  private void stepGame() {
    if (pet.getHealth().dead()) {
      view.addMessage("☠️ Your pet is dead. No more steps possible.");
      return;
    }

    // Store pre-step values
    HealthStatus beforeStatus = pet.getHealth();
    prevHunger = beforeStatus.hunger();
    prevHygiene = beforeStatus.hygiene();
    prevSocial = beforeStatus.social();
    prevSleep = beforeStatus.sleep();
    prevMood = beforeStatus.mood();

    // Perform the step
    pet.step();

    // Update the view
    updateView();

    // Add a separator line
    view.addMessage("\n▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

    // Provide feedback on the step
    view.addMessage("⏱️ Time passes... Your pet's needs have changed.");
    provideFeedbackOnChanges();

    // Check for mood change
    checkMoodChange();

    // Add hints based on current status if not dead
    if (!pet.getHealth().dead()) {
      view.addMessage("\n❓ HINTS ❓");
      addHintBasedOnStatus();
    }
  }

  /**
   * Checks and reports mood changes
   */
  private void checkMoodChange() {
    HealthStatus currentHealth = pet.getHealth();

    // Check for mood change
    if (currentHealth.mood() != prevMood && !currentHealth.dead()) {
      view.addMessage("\n🔄 MOOD CHANGED 🔄");
      view.addMessage("Your pet's mood changed from " + prevMood + " to " + currentHealth.mood() + "!");
    }

    // Check for death
    if (currentHealth.dead()) {
      view.addMessage("\n☠️ Your pet has died! Game over.");
    }
  }

  /**
   * Kills the pet (triggered by the exit button).
   */
  private void exitGame() {
    // First disable the exit button to prevent multiple clicks
    view.getExitButton().setEnabled(false);
    view.getExitButton().setText("Exited");

    // Kill the pet
    killPet();

    // Add message about abandonment
    view.addMessage("\n👋 You abandoned your pet. It couldn't survive alone!");
  }

  /**
   * Kills the pet and updates the UI accordingly.
   */
  private void killPet() {
    // Force the pet to die (implementation might vary based on Pet class)
    // Set extreme values to trigger death condition
    pet.interactWith(Action.FEED);
    pet.interactWith(Action.FEED);
    pet.interactWith(Action.FEED);
    pet.interactWith(Action.FEED);
    pet.interactWith(Action.FEED);

    // Update the view
    updateView();

    // Make sure the dead UI elements are shown
    if (!pet.getHealth().dead()) {
      view.setPetDead();
    }
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
    if (health.dead()) {
      view.setMood("DEAD");
    } else {
      view.updateMood(pet.getMood().name());
      view.updateImage(pet.getMood().name());
    }

    // Show/hide HUG button based on mood (only if not dead)
    if (!health.dead()) {
      view.setHugButtonVisible(pet.getMood() == MoodEnum.ANXIETY);
    }
  }

  /**
   * Provides feedback on the action that was performed.
   */
  private void provideFeedback(Action action) {
    // Create action feedback message with emoji
    String actionMessage;
    switch (action) {
      case FEED:
        actionMessage = "🍔 You fed your pet.";
        break;
      case PLAY:
        actionMessage = "🎮 You played with your pet.";
        break;
      case CLEAN:
        actionMessage = "🧼 You cleaned your pet.";
        break;
      case SLEEP:
        actionMessage = "😴 You put your pet to sleep.";
        break;
      case HUG:
        actionMessage = "🤗 You hugged your pet. It feels less anxious now!";
        break;
      default:
        actionMessage = "👋 You interacted with your pet.";
        break;
    }

    view.addMessage(actionMessage);
    provideFeedbackOnChanges();
  }

  /**
   * Provides feedback on changes in stats.
   */
  private void provideFeedbackOnChanges() {
    HealthStatus currentHealth = pet.getHealth();

    // Create status change message
    StringBuilder changes = new StringBuilder("📊 Status Changes: \n");

    // Show changes in health stats
    int hungerChange = currentHealth.hunger() - prevHunger;
    int hygieneChange = currentHealth.hygiene() - prevHygiene;
    int socialChange = currentHealth.social() - prevSocial;
    int sleepChange = currentHealth.sleep() - prevSleep;

    changes.append("   Hunger: ").append(formatChange(hungerChange)).append("\n");
    changes.append("   Hygiene: ").append(formatChange(hygieneChange)).append("\n");
    changes.append("   Social: ").append(formatChange(socialChange)).append("\n");
    changes.append("   Sleep: ").append(formatChange(sleepChange));

    view.addMessage(changes.toString());
  }

  /**
   * Formats a change value with + or - sign.
   */
  private String formatChange(int change) {
    // Remember that HIGHER values are WORSE in this game
    if (change > 0) {
      return "+" + change + " (worse)";
    } else if (change < 0) {
      return "" + change + " (better)";
    } else {
      return "no change";
    }
  }

  /**
   * Adds hints based on the current status.
   */
  private void addHintBasedOnStatus() {
    if (pet.getHealth().dead()) {
      return; // No hints for dead pet
    }

    HealthStatus health = pet.getHealth();
    boolean hintsAdded = false;

    // Check for critical values
    if (health.hunger() > 80) {
      view.addMessage("• Your pet is very hungry! Try feeding it.");
      hintsAdded = true;
    }

    if (health.hygiene() > 80) {
      view.addMessage("• Your pet is very dirty! Try cleaning it.");
      hintsAdded = true;
    }

    if (health.social() > 80) {
      view.addMessage("• Your pet is very lonely! Try playing with it.");
      hintsAdded = true;
    }

    if (health.sleep() > 80) {
      view.addMessage("• Your pet is very tired! Let it sleep.");
      hintsAdded = true;
    }

    // Mood-specific hints
    if (pet.getMood() == MoodEnum.ANXIETY) {
      view.addMessage("• Your pet is anxious! Try hugging it to calm it down.");
      view.addMessage("• When anxious, normal actions may have unpredictable effects.");
      hintsAdded = true;
    }

    // If no specific hints were added, add a general one
    if (!hintsAdded) {
      view.addMessage("• Your pet is doing fine right now.");
      view.addMessage("• Remember to care for its needs regularly!");
    }
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
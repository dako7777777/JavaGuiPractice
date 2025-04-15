package petapp.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import petapp.model.MoodEnum;


/**
 * The PetView class represents the graphical user interface for the Virtual Pet application.
 * It extends JFrame and contains components to display the pet's health status, mood, and image,
 * as well as buttons for interacting with the pet.
 */
public class PetView extends JFrame {
  private final JLabel healthLabel;
  private final JLabel moodLabel;
  private final JLabel imageLabel;
  private final JButton feedButton;
  private final JButton playButton;
  private final JButton cleanButton;
  private final JButton sleepButton;
  private final JButton hugButton;
  private final JButton stepButton;
  private final JButton exitButton;
  private final JPopupMenu moodMenu;
  private final JTextArea messageArea;
  private final JPanel buttonPanel;
  private final JLabel deadMessageLabel;

  // Progress bars for each health stat
  private final JProgressBar hungerBar;
  private final JProgressBar hygieneBar;
  private final JProgressBar socialBar;
  private final JProgressBar sleepBar;

  /**
   * Constructs a new PetView with all UI components.
   * Sets up the window layout, buttons, labels, and mood menu.
   * Initializes the pet's image to "HAPPY" mood.
   */
  public PetView() {
    setTitle("Virtual Pet");
    setSize(700, 800);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout(10, 10));

    // Top panel for exit button and health summary
    final JPanel topPanel = new JPanel(new BorderLayout());

    // Exit button on the top right
    exitButton = new JButton("Exit");
    exitButton.setForeground(Color.RED);
    exitButton.setFocusPainted(false);
    JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    exitPanel.add(exitButton);
    topPanel.add(exitPanel, BorderLayout.NORTH);

    // North panel for health summary and mood
    JPanel northPanel = new JPanel(new BorderLayout());
    northPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

    // Health Display
    healthLabel = new JLabel("Pet Status: Alive", SwingConstants.CENTER);
    healthLabel.setFont(new Font("Arial", Font.BOLD, 16));
    northPanel.add(healthLabel, BorderLayout.NORTH);

    // Progress bars for each stat
    JPanel statsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
    statsPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

    statsPanel.add(new JLabel("Hunger:"));
    hungerBar = createProgressBar();
    statsPanel.add(hungerBar);

    statsPanel.add(new JLabel("Hygiene:"));
    hygieneBar = createProgressBar();
    statsPanel.add(hygieneBar);

    statsPanel.add(new JLabel("Social:"));
    socialBar = createProgressBar();
    statsPanel.add(socialBar);

    statsPanel.add(new JLabel("Sleep:"));
    sleepBar = createProgressBar();
    statsPanel.add(sleepBar);

    northPanel.add(statsPanel, BorderLayout.CENTER);
    topPanel.add(northPanel, BorderLayout.CENTER);
    add(topPanel, BorderLayout.NORTH);

    // Center panel with image and message area
    JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
    centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

    // Image Display
    imageLabel = new JLabel();
    imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    imageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    centerPanel.add(imageLabel, BorderLayout.CENTER);

    // Dead message (only visible when pet is dead)
    deadMessageLabel =
        new JLabel("What kills you makes you more dead. R.I.P my virtual friend!",
            SwingConstants.CENTER);
    deadMessageLabel.setFont(new Font("Arial", Font.BOLD, 14));
    deadMessageLabel.setForeground(Color.RED);
    deadMessageLabel.setVisible(false);
    centerPanel.add(deadMessageLabel, BorderLayout.SOUTH);

    add(centerPanel, BorderLayout.CENTER);

    // Bottom panel that contains buttons, mood label, and message area
    JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
    bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

    // Message area for feedback
    messageArea = new JTextArea(10, 40);
    messageArea.setEditable(false);
    messageArea.setLineWrap(true);
    messageArea.setWrapStyleWord(true);
    messageArea.setFont(new Font("Arial", Font.PLAIN, 12));
    messageArea.setBorder(BorderFactory.createTitledBorder("Messages"));
    JScrollPane scrollPane = new JScrollPane(messageArea);
    bottomPanel.add(scrollPane, BorderLayout.CENTER);

    // Mood Display
    moodLabel = new JLabel("Mood: HAPPY", SwingConstants.CENTER);
    moodLabel.setFont(new Font("Arial", Font.BOLD, 14));
    moodLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
    bottomPanel.add(moodLabel, BorderLayout.NORTH);

    // Interaction Buttons
    buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(1, 6, 10, 5));

    feedButton = createStyledButton("Feed");
    playButton = createStyledButton("Play");
    cleanButton = createStyledButton("Clean");
    sleepButton = createStyledButton("Sleep");
    hugButton = createStyledButton("Hug");
    stepButton = createStyledButton("Step");

    stepButton.setBackground(new Color(173, 216, 230)); // Light blue for step button

    buttonPanel.add(feedButton);
    buttonPanel.add(playButton);
    buttonPanel.add(cleanButton);
    buttonPanel.add(sleepButton);
    buttonPanel.add(hugButton);
    buttonPanel.add(stepButton);

    // Add instructions about right-clicking mood
    JLabel instructionLabel =
        new JLabel("Right-click on mood to change (Dev only)",
            SwingConstants.CENTER);
    instructionLabel.setFont(new Font("Arial", Font.ITALIC, 10));

    JPanel bottomControlPanel = new JPanel(new BorderLayout(5, 5));
    bottomControlPanel.add(instructionLabel, BorderLayout.NORTH);
    bottomControlPanel.add(buttonPanel, BorderLayout.CENTER);

    bottomPanel.add(bottomControlPanel, BorderLayout.SOUTH);
    add(bottomPanel, BorderLayout.SOUTH);

    // Hide HUG button by default - will only be visible during anxiety
    hugButton.setVisible(false);

    // Mood Popup Menu (for developers)
    moodMenu = new JPopupMenu();
    for (MoodEnum mood : MoodEnum.values()) {
      JMenuItem item = new JMenuItem(mood.name());
      moodMenu.add(item);
    }
    // Add DEAD as an option for testing
    JMenuItem deadItem = new JMenuItem("DEAD");
    moodMenu.add(deadItem);

    // Right-click listener to open the mood menu
    moodLabel.setComponentPopupMenu(moodMenu);

    updateImage("HAPPY");

    // Add initial welcome message
    addMessage("Welcome to Virtual Pet! "
        + "Press 'Step' to advance time or use actions to interact with your pet.");

    setLocationRelativeTo(null); // Center on screen
    setVisible(true);
  }

  /**
   * Creates a styled button with consistent appearance.
   */
  private JButton createStyledButton(String text) {
    JButton button = new JButton(text);
    button.setFocusPainted(false);
    button.setBackground(new Color(230, 230, 250)); // Light purple
    return button;
  }

  /**
   * Creates a standardized progress bar for health stats.
   * Lower values are better, so the colors are inverted.
   */
  private JProgressBar createProgressBar() {
    JProgressBar bar = new JProgressBar(0, 100);
    bar.setStringPainted(true);
    return bar;
  }

  /**
   * Returns the feed button for attaching listeners.
   * @return the feed button component that controls feeding functionality
   */
  public JButton getFeedButton() {
    return feedButton;
  }

  /**
   * Returns the play button for attaching listeners.
   * @return the playButton button.
   */
  public JButton getPlayButton() {
    return playButton;
  }

  /**
   * Returns the clean button for attaching listeners.
   * @return the playButton cleanButton.
   */
  public JButton getCleanButton() {
    return cleanButton;
  }

  /**
   * Returns the sleep button for attaching listeners.
   * @return the  sleepButton cleanButton.
   */
  public JButton getSleepButton() {
    return sleepButton;
  }

  /**
   * Returns the hug button for attaching listeners.
   * @return the  hugButton cleanButton.
   */
  public JButton getHugButton() {
    return hugButton;
  }

  /**
   * Returns the step button for attaching listeners.
   * @return the  stepButton cleanButton.
   */
  public JButton getStepButton() {
    return stepButton;
  }

  /**
   * Returns the exit button for attaching listeners.
   * @return the  exitButton cleanButton.
   */
  public JButton getExitButton() {
    return exitButton;
  }

  /**
   * Adds a message to the message area.
   * @param message send to user.
   */
  public void addMessage(String message) {
    // Check if this is a new message group (starts with a special character)
    boolean isNewGroup = message.startsWith("\n")
        || message.contains("HINTS")
        || message.contains("MOOD CHANGED")
        || message.startsWith("▬");

    // If it's a new message group but doesn't start with newline, add one
    if (isNewGroup && !message.startsWith("\n")) {
      messageArea.append("\n");
    }

    // Add the message
    messageArea.append(message + "\n");

    // Scroll to the bottom
    messageArea.setCaretPosition(messageArea.getDocument().getLength());
  }

  /**
   * Attaches a listener to all mood menu items.
   * @param listener the ActionListener to be attached to each mood menu item.
   */
  public void setMoodMenuItemListener(java.awt.event.ActionListener listener) {
    for (int i = 0; i < moodMenu.getComponentCount(); i++) {
      if (moodMenu.getComponent(i) instanceof JMenuItem) {
        ((JMenuItem) moodMenu.getComponent(i)).addActionListener(listener);
      }
    }
  }

  /**
   * Sets the pet's mood and updates both the mood label and the displayed image.
   * @param mood the String representing the pet's mood state (e.g., "HAPPY", "SAD", "DEAD").
   */
  public void setMood(String mood) {
    updateMood(mood);
    updateImage(mood);

    // Check if pet is dead
    if ("DEAD".equals(mood)) {
      setPetDead();
    }
  }

  /**
   * Updates the pet's image based on the specified mood.
   * Loads the appropriate image file from resources.
   * @param mood the String representing the pet's mood state (e.g., "HAPPY", "SAD", "DEAD").
   */
  public void updateImage(String mood) {
    String lowerMood = mood.toLowerCase();
    // Convert DEAD special case
    if ("dead".equals(lowerMood)) {
      lowerMood = "dead";
    }

    String imagePath = "/images/" + lowerMood + ".png";

    try {
      // This is the proper way to load resources from JAR
      InputStream imageStream = getClass().getResourceAsStream(imagePath);

      if (imageStream != null) {
        Image image = ImageIO.read(imageStream);
        Image scaledImage = image.getScaledInstance(256, 256, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaledImage));
      } else {
        // Try alternative path
        imagePath = "resources/images/" + lowerMood + ".png";
        imageStream = getClass().getResourceAsStream(imagePath);

        if (imageStream != null) {
          Image image = ImageIO.read(imageStream);
          Image scaledImage = image.getScaledInstance(256, 256, Image.SCALE_SMOOTH);
          imageLabel.setIcon(new ImageIcon(scaledImage));
        } else {
          System.err.println("Image not found in resources: " + imagePath);
          imageLabel.setText("Pet Image (" + mood + ")");
        }
      }
    } catch (IOException e) {
      System.err.println("Error loading image: " + e.getMessage());
      imageLabel.setText("Pet Image (" + mood + ")");
    }
  }

  /**
   * Updates the mood label with the specified text.
   * @param moodText the String representing the pet's mood state (e.g., "HAPPY", "SAD", "DEAD").
   */
  public void updateMood(String moodText) {
    moodLabel.setText("Mood: " + moodText);

    // Change color based on mood
    switch (moodText) {
      case "HAPPY" -> moodLabel.setForeground(new Color(0, 150, 0)); // Green
      case "SAD" -> moodLabel.setForeground(new Color(150, 0, 0)); // Red
      case "ANXIETY" -> moodLabel.setForeground(new Color(200, 150, 0)); // Orange
      case "DEAD" -> moodLabel.setForeground(Color.BLACK);
      default -> moodLabel.setForeground(Color.BLACK);
    }
  }

  /**
   * Updates the health label and progress bars with the specified health status.
   * @param hunger the hunger level of the pet (0-100, where lower is better)
   * @param hygiene the hygiene level of the pet (0-100, where lower is better)
   * @param social the social level of the pet (0-100, where lower is better)
   * @param sleep the sleep level of the pet (0-100, where lower is better)
   * @param alive indicates whether the pet is alive (true) or dead (false)
   */
  public void updateHealth(int hunger, int hygiene, int social, int sleep, boolean alive) {
    String healthText = String.format("Pet Status: %s", alive ? "Alive" : "Dead");
    healthLabel.setText(healthText);

    // Update health label color
    if (!alive) {
      healthLabel.setForeground(Color.RED);
      setPetDead();
    } else {
      healthLabel.setForeground(Color.BLACK);
    }

    // Update progress bars
    updateProgressBar(hungerBar, hunger);
    updateProgressBar(hygieneBar, hygiene);
    updateProgressBar(socialBar, social);
    updateProgressBar(sleepBar, sleep);
  }

  /**
   * Updates a progress bar with a value and sets appropriate colors.
   * Since lower values are better in this game, colors are reversed.
   * @param bar the JProgressBar component to update
   * @param value the value to set for the progress bar (0-100)
   */
  private void updateProgressBar(JProgressBar bar, int value) {
    bar.setValue(value);

    // Set color based on value (lower is better)
    if (value < 30) {
      bar.setForeground(new Color(0, 153, 0)); // Green
    } else if (value < 60) {
      bar.setForeground(new Color(255, 204, 0)); // Yellow
    } else {
      bar.setForeground(new Color(204, 0, 0)); // Red
    }

    bar.setString(value + "%");
  }

  /**
   * Sets the visibility of the hug button.
   * @param visible true to make the hug button visible, false to hide it
   */
  public void setHugButtonVisible(boolean visible) {
    hugButton.setVisible(visible);
  }

  /**
   * Updates the UI for dead pet state.
   */
  public void setPetDead() {
    // Hide all action buttons
    for (java.awt.Component component : buttonPanel.getComponents()) {
      component.setEnabled(false);
    }

    // Also disable the exit button since the pet is already dead
    exitButton.setEnabled(false);
    exitButton.setText("Exited");

    // Show dead message
    deadMessageLabel.setVisible(true);

    // Add death message to message area
    addMessage("\n☠️ Your pet has died. Game over.");

    // Update mood and image
    updateMood("DEAD");
    updateImage("DEAD");
  }
}
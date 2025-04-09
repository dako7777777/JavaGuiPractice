package petapp.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import petapp.model.MoodEnum;
import java.io.InputStream;

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
  private final JPopupMenu moodMenu;

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
    setSize(600, 500);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout(10, 10));

    // North panel for health summary and mood
    JPanel northPanel = new JPanel(new BorderLayout());
    northPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

    // Health Display
    healthLabel = new JLabel("Pet Status: Alive", SwingConstants.CENTER);
    healthLabel.setFont(new Font("Arial", Font.BOLD, 16));
    northPanel.add(healthLabel, BorderLayout.NORTH);

    // Progress bars for each stat
    JPanel statsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
    statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

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
    add(northPanel, BorderLayout.NORTH);

    // Image Display
    imageLabel = new JLabel();
    imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    imageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    add(imageLabel, BorderLayout.CENTER);

    // Bottom panel that contains buttons and mood label
    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

    // Mood Display
    moodLabel = new JLabel("Mood: HAPPY", SwingConstants.CENTER);
    moodLabel.setFont(new Font("Arial", Font.BOLD, 14));
    moodLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    bottomPanel.add(moodLabel, BorderLayout.NORTH);

    // Interaction Buttons
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(1, 6, 10, 10));

    feedButton = createStyledButton("Feed");
    playButton = createStyledButton("Play");
    cleanButton = createStyledButton("Clean");
    sleepButton = createStyledButton("Sleep");
    hugButton = createStyledButton("Hug");
    stepButton = createStyledButton("Step");

    buttonPanel.add(feedButton);
    buttonPanel.add(playButton);
    buttonPanel.add(cleanButton);
    buttonPanel.add(sleepButton);
    buttonPanel.add(hugButton);
    buttonPanel.add(stepButton);

    bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
    add(bottomPanel, BorderLayout.SOUTH);

    // Hide HUG button by default - will only be visible during anxiety
    hugButton.setVisible(false);

    // Mood Popup Menu
    moodMenu = new JPopupMenu();
    for (MoodEnum mood : MoodEnum.values()) {
      JMenuItem item = new JMenuItem(mood.name());
      moodMenu.add(item);
    }

    // Right-click listener to open the mood menu
    moodLabel.setComponentPopupMenu(moodMenu);

    // Add instructions about right-clicking
    JLabel instructionLabel = new JLabel("Right-click on mood to change", SwingConstants.CENTER);
    instructionLabel.setFont(new Font("Arial", Font.ITALIC, 10));
    bottomPanel.add(instructionLabel, BorderLayout.CENTER);

    updateImage("HAPPY");

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
   */
  public JButton getFeedButton() {
    return feedButton;
  }

  /**
   * Returns the play button for attaching listeners.
   */
  public JButton getPlayButton() {
    return playButton;
  }

  /**
   * Returns the clean button for attaching listeners.
   */
  public JButton getCleanButton() {
    return cleanButton;
  }

  /**
   * Returns the sleep button for attaching listeners.
   */
  public JButton getSleepButton() {
    return sleepButton;
  }

  /**
   * Returns the hug button for attaching listeners.
   */
  public JButton getHugButton() {
    return hugButton;
  }

  /**
   * Returns the step button for attaching listeners.
   */
  public JButton getStepButton() {
    return stepButton;
  }

  /**
   * Attaches a listener to all mood menu items.
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
   */
  public void setMood(String mood) {
    updateMood(mood);
    updateImage(mood);
  }

  /**
   * Updates the pet's image based on the specified mood.
   * Loads the appropriate image file from resources.
   */
  public void updateImage(String mood) {
    String imagePath = "/images/" + mood.toLowerCase() + ".png";

    try {
      // This is the proper way to load resources from JAR
      InputStream imageStream = getClass().getResourceAsStream(imagePath);

      if (imageStream != null) {
        Image image = ImageIO.read(imageStream);
        Image scaledImage = image.getScaledInstance(256, 256, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaledImage));
      } else {
        System.err.println("Image not found in resources: " + imagePath);
        imageLabel.setText("Pet Image (" + mood + ")");
      }
    } catch (Exception e) {
      System.err.println("Error loading image: " + e.getMessage());
      imageLabel.setText("Pet Image (" + mood + ")");
    }
  }

  /**
   * Updates the mood label with the specified text.
   */
  public void updateMood(String moodText) {
    moodLabel.setText("Mood: " + moodText);

    // Change color based on mood
    if (moodText.equals("HAPPY")) {
      moodLabel.setForeground(new Color(0, 150, 0)); // Green
    } else if (moodText.equals("SAD")) {
      moodLabel.setForeground(new Color(150, 0, 0)); // Red
    } else if (moodText.equals("ANXIETY")) {
      moodLabel.setForeground(new Color(200, 150, 0)); // Orange
    } else {
      moodLabel.setForeground(Color.BLACK);
    }
  }

  /**
   * Updates the health label and progress bars with the specified health status.
   */
  public void updateHealth(int hunger, int hygiene, int social, int sleep, boolean alive) {
    String healthText = String.format("Pet Status: %s", alive ? "Alive" : "Dead");
    healthLabel.setText(healthText);

    // Update health label color
    if (!alive) {
      healthLabel.setForeground(Color.RED);
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
   */
  public void setHugButtonVisible(boolean visible) {
    hugButton.setVisible(visible);
  }
}
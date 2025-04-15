package petapp.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import petapp.test.TestRandom;

/**
 * Tests for the PetTest.
 */
public class PetTest {
  private Pet pet;
  private TestRandom testRandom;

  @Before
  public void setUp() {
    testRandom = new TestRandom();
    pet = new Pet(testRandom);
  }

  @Test
  public void testInitialValues() {
    // Verify initial values
    HealthStatus health = pet.getHealth();
    assertEquals(20, health.hunger());
    assertEquals(60, health.hygiene());
    assertEquals(60, health.social());
    assertEquals(15, health.sleep());
    assertEquals(MoodEnum.HAPPY, health.mood());
    assertFalse(health.dead());
  }

  @Test
  public void testCheckAnxietyBadConditionTriggered() {
    // Setup bad condition (hunger and sleep > 60)
    pet.setHunger(70);
    pet.setSleep(70);

    // Control random to trigger anxiety (probability is max 50 in bad condition)
    testRandom.setNextIntValues(new int[]{25}); // Less than 50

    // Step to trigger anxiety check
    pet.step();

    // Verify pet became anxious
    assertEquals(MoodEnum.ANXIETY, pet.getMood());
  }

  @Test
  public void testCheckAnxietyBadConditionNotTriggered() {
    // Setup bad condition (hunger and sleep > 60)
    pet.setHunger(70);
    pet.setSleep(70);

    // Control random to NOT trigger anxiety
    testRandom.setNextIntValues(new int[]{75}); // More than 50

    // Step to trigger anxiety check
    pet.step();

    // Verify pet did not become anxious
    assertNotEquals(MoodEnum.ANXIETY, pet.getMood());
  }

  @Test
  public void testCheckAnxietyGoodConditionTriggered() {
    // Setup good condition (hunger or sleep <= 60)
    pet.setHunger(40);
    pet.setSleep(40);

    // Control random to trigger anxiety (probability is max 20 in good condition)
    testRandom.setNextIntValues(new int[]{10}); // Less than 20

    // Step to trigger anxiety check
    pet.step();

    // Verify pet became anxious
    assertEquals(MoodEnum.ANXIETY, pet.getMood());
  }

  @Test
  public void testCheckAnxietyGoodConditionNotTriggered() {
    // Setup good condition
    pet.setHunger(40);
    pet.setSleep(40);

    // Control random to NOT trigger anxiety
    testRandom.setNextIntValues(new int[]{30}); // More than 20

    // Step to trigger anxiety check
    pet.step();

    // Verify pet did not become anxious
    assertNotEquals(MoodEnum.ANXIETY, pet.getMood());
  }

  @Test
  public void testPetDeath() {
    // Set values that will trigger death
    pet.setHunger(96);
    pet.setSleep(96);

    // Ensure anxiety check doesn't interfere
    testRandom.setNextIntValues(new int[]{99});

    // Step to check death
    pet.step();

    // Verify pet is dead
    assertTrue(pet.getHealth().dead());
  }

  @Test
  public void testDeadPetDoesNotChangeWithStep() {
    // First kill the pet
    pet.setHunger(96);
    pet.setSleep(96);
    pet.step();

    // Save current values
    int hunger = pet.getHunger();
    int hygiene = pet.getHygiene();
    final int social = pet.getSocial();
    final int sleep = pet.getSleep();

    // Try to step
    pet.step();

    // Verify no change
    assertEquals(hunger, pet.getHunger());
    assertEquals(hygiene, pet.getHygiene());
    assertEquals(social, pet.getSocial());
    assertEquals(sleep, pet.getSleep());
  }

  @Test
  public void testDeadPetDoesNotChangeWithInteract() {
    // First kill the pet
    pet.setHunger(96);
    pet.setSleep(96);
    pet.step();

    // Save current values
    int hunger = pet.getHunger();
    int hygiene = pet.getHygiene();
    final int social = pet.getSocial();
    final int sleep = pet.getSleep();

    // Try to interact
    pet.interactWith(Action.FEED);

    // Verify no change
    assertEquals(hunger, pet.getHunger());
    assertEquals(hygiene, pet.getHygiene());
    assertEquals(social, pet.getSocial());
    assertEquals(sleep, pet.getSleep());
  }

  @Test
  public void testAnxietyMoodStrategyCreatedWithInjectedRandom() {
    // Set mood to anxiety which should create AnxietyMoodStrategy
    pet.setMood(MoodEnum.ANXIETY);

    // Control randomness for testing
    testRandom.setNextFloat(0.75f); // 0.5 ratio
    testRandom.setNextIntValues(new int[]{10, 5}); // Social +3, Sleep -5

    // Initialize consistent values
    pet.setHunger(50);
    pet.setSocial(50);
    pet.setHygiene(50);
    pet.setSleep(50);

    // Step to test if anxiety strategy uses the controlled random
    pet.step();

    // Verify anxiety strategy gets the controlled random and behaves as expected
    assertEquals(57, pet.getHunger());  // +7 (fixed)
    assertEquals(53, pet.getHygiene()); // +3 (fixed)
    assertEquals(53, pet.getSocial());  // +3 (from 10-7)
    assertEquals(45, pet.getSleep());   // -5 (from 5-10)
  }
}
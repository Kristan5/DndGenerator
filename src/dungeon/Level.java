package dungeon;
import dnd.die.D20;


public class Level {
  /**
   * Method:  getDescription: gets a description of a passage section based on a die roll.
   * @return result of die roll/ string of passage section description
   */
  public String getDescription() {
    String result;
    int roll;

    roll = rollDie20();

    if (roll > 0 && roll < 3) {
      result = "passage goes straight for 10 ft";
    } else if (roll > 2 && roll < 6) {
      result = "passage ends in Door to a Chamber";
    } else if (roll > 5 && roll < 8) {
      result = "archway (door) to right (main passage continues straight for 10 ft)";
    } else if (roll > 7 && roll < 10) {
      result = "archway (door) to left (main passage continues straight for 10 ft)";
    } else if (roll > 9 && roll < 12) {
      result = "passage turns to left and continues for 10 ft";
    } else if (roll > 11 && roll < 14) {
      result = "passage turns to right and continues for 10 ft";
    } else if (roll > 13 && roll < 16) {
      result = "passage ends in archway (door) to chamber";
    } else if (roll > 15 && roll < 18) {
      result = "stairs, (passage continues straight for 10 ft)";
    } else if (roll > 17 && roll < 20) {
      result = "Dead End";
    } else if (roll == 20) {
      result = "Wandering Monster (passage continues straight for 10 ft)";
    } else {
      result = "Not Possible";
    }

    return result;
  }

   /**
   * Method: rollDie20 rolls a 20 sided die and returns the outcome.
   * @return int of the result of a die roll
   */
  private int rollDie20() {
    int dieRoll20;
    D20 die20 = new D20();
    return die20.roll();
  }
}

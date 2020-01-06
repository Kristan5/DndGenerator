package dungeon;

import dnd.models.Monster;
import dnd.models.Treasure;
import java.io.Serializable;

/* Represents a 10 ft section of passageway */

public class PassageSection implements java.io.Serializable {

  /**
   * Represents a door in this passage section.
   */
  private Door aDoor;

  /**
   * Represents a monster in this passage section.
   */
  private Monster aMonster;

  /**
   * Represents a treasure in this passage section.
   */
  private Treasure aTreasure;

  /**
   * Represents a chamber in this passage section.
   */
  private Chamber aChamber;

  /**
   * Represents the description of this passage section.
   */
  private String passageDescription;

  /**
   * Method:  PassageSection constructor.
   *
   */
  public PassageSection() {
    //sets up the 10 foot section with default settings
    passageDescription = "passage goes straight for 10 ft";

  }

  /**
   * Method:  PassageSection costructor.
   * @param description of passage section based off of table provided in the assignment description
   */
  public PassageSection(String description) {
    //sets up a specific passage based on the values sent in from
    //modified table 1

    passageDescription = description;

    if (description.equals("passage ends in Door to a Chamber")) {
      aDoor = new Door();
      aChamber = new Chamber();
    } else if (description.equals("archway (door) to right (main passage continues straight for 10 ft)")) {
      aDoor = new Door();
      aDoor.setArchway(true);
    } else if (description.equals("archway (door) to left (main passage continues straight for 10 ft)")) {
      aDoor = new Door();
      aDoor.setArchway(true);
    } else if (description.equals("passage ends in archway (door) to chamber")) {
      aDoor = new Door();
      aDoor.setArchway(true);
      aChamber = new Chamber();
    } else if (description.equals("Wandering Monster (passage continues straight for 10 ft)")) {
      aMonster = new Monster();
    }
  }

  /**
   * Method:  getDoor: gets door in this section.
   * @return aDoor: the door in this passage section
   */
  public Door getDoor() {
    //returns the door that is in the passage section, if there is one
    if (aDoor != null) {
      return aDoor;
    } else {
      return null;
    }
  }

  /**
   * Method:  setDoor: sets a door in this section.
   * @param theDoor the door to be set
   */
  public void setDoor(Door theDoor) {
    aDoor = theDoor;
  }

  /**
   * Method:  getMonster: gets Monster in this section.
   * @return aMonster the monster in this section if there is one
   */
  public Monster getMonster() {
    //returns the monster that is in the passage section, if there is one
    if (aMonster != null) {
      return aMonster;
    } else {
      return null;
    }
  }

  /**
   * Method:  getTreasure: gets Treasure in this section.
   * @return aTreasure the treasure in this section if there is one
   */
  public Treasure getTreasure() {
    //returns the monster that is in the passage section, if there is one
    if (aTreasure != null) {
      return aTreasure;
    } else {
      return null;
    }
  }

  /**
   * Method:  getDescription: gets description of this passage section.
   * @return passageDescription description of passage section
   */
  public String getDescription() {
    return passageDescription;
  }

  /**
   * Method:  setMonster: sets monster in this section.
   * @param theMonster the monster that is to be put in this section
   */
  public void setMonster(Monster theMonster) {
    aMonster = theMonster;
  }

  /**
   * Method:  setTreasure : sets monster in this section.
   * @param theTreasure the monster that is to be put in this section
   */
  public void setTreasure(Treasure theTreasure) {
    aTreasure = theTreasure;
  }

  /**
   * Method setDescription sets the description oof this passage section.
   * @param description string of description to be set into section
   */
  public void setDescription(String description) {
    passageDescription = passageDescription + " and " + description;
  }

  /**
   * Method getChamber: gets the chamber in this passage section.
   * @return aChamber a chamber in this passage sectiono if there is one
   */
  public Chamber getChamber() {
    if (aChamber != null) {
      return aChamber;
    } else {
      return null;
    }
  }

}

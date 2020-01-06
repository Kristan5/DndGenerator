package dungeon;

import dnd.models.Monster;
import dnd.models.Treasure;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;
/*
A passage begins at a door and ends at a door.  It may have many other doors along
the way
You will need to keep track of which door is the "beginning" of the passage
so that you know how to
*/

public class Passage extends Space implements java.io.Serializable {
  //these instance variables are suggestions only
  //you can change them if you wish.


  /**
   *
   *
   */
  private ArrayList<PassageSection> thePassage = new ArrayList<PassageSection>();

  /**
   *
   *
   */
  private HashMap<Door, PassageSection> doorMap;
  /**
   *
   *
   */
  private Door aDoor = new Door();
  /**
   *
   *
   */
  private ArrayList<Door> passageDoors = new ArrayList<Door>();
  /**
   * Object's unique ID.
   */
  private int id;

  /*
  Method:  getDoors: gets all the doors in this passage
  @param  none
  @return  arraylist of all the doors in this passage
  */

  /**
   *
   * @return passageDoors an array list of all the doorsin the passage
   */
  public ArrayList<Door> getDoors() {
    //gets all of the doors in the entire passage
    return passageDoors;
  }

  /*
  Method:  getDoor: gets a door in this passage at a specified index
  @param  int i: index of door to be found
  @return  Door object if a door is found at the index
  */

  /**
   * @param i index of door to be found
   * @return Door object if a door is found at the index
   */
  public Door getDoor(int i) {
    //returns the door in section 'i'. If there is no door, returns null
    if (i < passageDoors.size() && i >= 0) {
      return passageDoors.get(i);
    } else {
      return null;
    }
  }

  /**
   * @param i passage section to get monster from
   * @return monster if a monster has been found at the pasage section provided
   */
  public Monster getMonster(int i) {
    //returns Monster door in section 'i'. If there is no Monster, returns null
    if (thePassage.get(i).getMonster() != null) {
      return thePassage.get(i).getMonster();
    } else {
      return null;
    }
  }

  /**
   * @param i passage section to remove monster from
   */
  public void removeMonster(int i) {
    if (thePassage.get(i).getMonster() != null) {
      thePassage.get(i).setMonster(null);
    }
  }

  /**
   * @param theMonster the monster to be added
   * @param i index of section to add monster to
   */
  public void addMonster(Monster theMonster, int i) {
    // adds a monster to section 'i' of the passage
    thePassage.get(i).setMonster(theMonster);
    thePassage.get(i).setDescription(theMonster.getDescription());
  }

  /**
   * Method:  removeMonster: remove monster from chamber.
   * @param i index of passage section
   * @param aMonster  amosnter to remove
   */
  public void removeMonster(Monster aMonster, int i) {
    if (thePassage.get(i).getMonster() != null) {
      thePassage.get(i).setMonster(null);
    }
  }

  /**
   * @param aTreasure the monster to be added
   * @param i index of section to add treasure to
   */
  public void addTreasure(Treasure aTreasure, int i) {
    thePassage.get(i).setTreasure(aTreasure);
    thePassage.get(i).setDescription(aTreasure.getDescription());
  }

  /**
   * @return aTreasure the treasure to be got
   * @param i index of section to add treasure to
   */
  public Treasure getTreasure(int i) {
    return thePassage.get(i).getTreasure();
  }

  /**
   * Method:  removeTreasure: remove treasure from chamber.
   * @param aTreasure atreasure to be removed
   * @param i index passage section
   */
  public void removeTreasure(Treasure aTreasure, int i) {
    if (thePassage.get(i).getTreasure() != null) {
      thePassage.get(i).setTreasure(null);
    }
  }
  /*
  Method:  addPassageSection: adds a passage section to the passage
  @param  PassageSection toAdd: the passage section that is being added
  @return  none
  */


  /**
   *
   * @param toAdd passage section that is being added
   */
  public void addPassageSection(PassageSection toAdd) {
    //adds the passage section to the passageway
    thePassage.add(toAdd);
  }

  /**
   *
   * @return arrayList of the passage sections in this passage.
   */
  public ArrayList<PassageSection> getPassageSections() {
    return thePassage;
  }

  /*
  Method:  setDoor: sets a door connection to this passage
  @param  Door newDoor: Door object to be set
  @return  none
  */


  /**
   *
   * @param newDoor new door object to be set
   */
  @Override
  public void setDoor(Door newDoor) {
    //should add a door connection to the current Passage Section
    aDoor = newDoor;
    passageDoors.add(aDoor);
  }

  /**
   * Method: setDoor: to add a door connection to this room.
   * @param newDoor door object newDoor that is to be added to this room
   * @param doorID id of door
   */
  public void setDoor(Door newDoor, int doorID) {
    passageDoors.add(newDoor);
    for (int i = 0; i < getDoors().size(); i++) {
      if (getDoors().get(i).getID() == doorID) {
        passageDoors.set(i, newDoor);
      }
    }
  }

  /*
  Method:  getDescription: gets the description of each passage section in this passage
  @param  none
  @return  String of passage description
  */


  /**
   *
   * @return concatenated string of each passage section in passage
   */
  @Override
  public String getDescription() {
    String concatenated = "";
    concatenated += "The Passage is made up of " + thePassage.size() + " sections:\n";
    for (int i = 0; i < thePassage.size(); i++) {
      concatenated += "\t" + (i + 1) + ": " + thePassage.get(i).getDescription() + "\n";
    }
    return concatenated;
  }

  /**
   * Method: unique id for object to be set to.
   * @param idNum id num to be set to
   */
  public void setID(int idNum) {
    id = idNum;
  }

  /**
   * Method: get unique id of this object.
   * @return int of objects id
   */
  public int getID() {
    return id;
  }
}

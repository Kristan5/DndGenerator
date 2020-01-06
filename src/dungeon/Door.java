package dungeon;

import dnd.models.Exit;
import dnd.models.Trap;
import java.util.ArrayList;
import dnd.die.D20;
import dnd.die.D10;
import dnd.die.D6;
import java.io.*;

public class Door implements java.io.Serializable {

  /**
   * Boolean that is set to true if the door is an archway.
   */
  private boolean archway;

  /**
   * Boolean that is set to true if the door is trapped.
   */
  private boolean trapped;

  /**
   * Boolean that is set to true if the door is open.
   */
  private boolean open;

  /**
   * String of the direction that the door is in the space.
   */
  private String doorDirection;

  /**
   * String of the doors Location in the space.
   */
  private String doorLocation;

  /**
   * Trap to be set if the door is trapped.
   */
  private Trap aTrap;

  /**
   * ArrayList of the spaces connected by this door.
   */
  private ArrayList<Space> spaceList;

  /**
   * Object's unique ID.
   */
  private int id;

  /**
   * Method: Door constructor.
   *
   */
  public Door() {
    aTrap = null;
    trapped = false;
    spaceList = new ArrayList<Space>();
    //needs to set defaults
    //sets up the door based on the Exit from the tables
    Exit anExit = new Exit();
    setDoortoExit(anExit);
    doorState();
  }

  /**
   * Method:  Door constructor.
   * @param theExit exit object
   */
  public Door(Exit theExit) {
    aTrap = null;
    trapped = false;
    spaceList = new ArrayList<Space>();
    //sets up the door based on the Exit from the tables
    setDoortoExit(theExit);
    doorState();
  }

  /**
   * Method: sets the doors direction and location to that of the exits.
   * @param theExit exit object to be set to this door
   */
  private void setDoortoExit(Exit theExit) {
    doorDirection = theExit.getDirection();
    doorLocation = theExit.getLocation();
  }

  /**
   * Method: setTrapped: sets the door to a trapped door.
   * @param flag boolean flag if the door should be trapped or not
   * @param roll array of die rolls
   */
  public void setTrapped(boolean flag, int... roll) {
    // true == trapped.  Trap must be rolled if no integer is given
    trapped = flag;
    if (flag) {
      aTrap = new Trap();
      if (roll.length > 0) {
        aTrap.chooseTrap(roll[0]);
      } else {
        aTrap = null;
      }
    }
  }

  /**
   * Method: setOpen to set the current door object to open.
   * @param flag boolean flag of if hte door should be open or closed
   */
  public void setOpen(boolean flag) {
    //true == open
    if (!archway) {
      open = flag;
    } else {
      open = true;
    }
  }

  /**
   * Method: setArchway to set the current door objext to an archway.
   * @param flag boolean flag of if the door should be open or closed
   */
  public void setArchway(boolean flag) {
    //true == is archway
    archway = flag;
    if (archway) {
      setTrapped(false);
      setOpen(true);
    }
  }

  /**
   * Method: isTrapped: checks current door is a trapped door.
   * @return boolean of if the door is trapped (true) or not (false)
   */
  public boolean isTrapped() {
    return trapped;
  }

  /**
   * Method:   isOpen: checks the current if its open.
   * @return boolean of if the door is open (true) or closed (false)
   */
  public boolean isOpen() {
    return open;
  }

  /**
   * Method:  isArchway: checks if the current door is an archway.
   * @return archway boolean of if the door is an archway (true) or not (false)
   */
  public boolean isArchway() {
    return archway;
  }

  /**
   * Method:  getstate.
   * @return state of door
   */
  public String getState() {
    if (trapped) {
      return "trapped";
    } else if (archway) {
      return "archway";
    } else if (open) {
      return "open";
    } else {
      return "just a";
    }
  }

  /**
   * Method: getTrapDescription: gets a description of the trap object in this class.
   * @return stringn of the traps description
   */
  public String getTrapDescription() {
    String result = "";
    if (trapped) {
      result = aTrap.getDescription();
    } else {
      result = "Door is not Trapped";
    }
    return result;
  }

  /**
   * Method:   getSpaces to return spaceArray of spaces connected by door.
   * @return spaceList: array of two spaces that are connected by door
   */
  public ArrayList<Space> getSpaces() {
    //returns the two spaces that are connected by the door
    return spaceList;
  }

  /**
   * Method:  setOneSpace to set a single space object to door instad of two (setSpaces).
   * @param aSpace single space to be set
   */
  public void setOneSpace(Space aSpace) {
    spaceList.add(aSpace);
    if (aSpace instanceof Chamber) {
      Chamber temp = (Chamber) aSpace;
      temp.setDoor(this, id);
    }
    if (aSpace instanceof Passage) {
      Passage temp = (Passage) aSpace;
      temp.setDoor(this, id);
    }
  }

  /**
   * Method:  setSpaces to set two space objects to connect to door.
   * @param spaceOne first space to be set
   * @param spaceTwo second space to be set
   *
   */
  public void setSpaces(Space spaceOne, Space spaceTwo) {
    setOneSpace(spaceOne);
    setOneSpace(spaceTwo);
  }

  /**
   * Method: getDescription; gets description of doors trap.
   * @return string of trap description
   */
  public String getDescription() {
    return getTrapDescription();
  }

  /**
   * Method: getDoorDirection: gets the direction of door in space.
   * @return doorDirection string
   */
  public String getDoorDirection() {
    if (doorDirection != null) {
      return doorDirection;
    } else {
      return null;
    }
  }

  /**
   * Method: getDoorLocation: gets the location of the door in space.
   * @return doorLocation of doors location description
   */
  public String getDoorLocation() {
    if (doorLocation != null) {
      return doorLocation;
    } else {
      return null;
    }
  }

  /**
   * Method:  doorState: sets state of door based off of chances outlined in assignment description.
   */
  private void doorState() {
    /*Generating Type of Door from table outlined in assignment desciption*/
    if (rollDie10() == 1) {
      /*Archway*/
      setArchway(true);
    }
    if (rollDie20() == 1) {
      /*Trapped door*/
      setTrapped(true);
    }
    if (rollDie6() == 1) {
      /*Locked*/
      setOpen(false);
    } else {
      setOpen(true);
    }
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

  /**
   * Method: rollDie10 rolls a 10 sided die and returns the outcome.
   * @return int of the result of a die roll
   */
  private int rollDie10() {
    int dieRoll10;
    D10 die10 = new D10();
    return die10.roll();
  }

  /**
   * Method: rollDie6 rolls a 6 sided die and returns the outcome.
   * @return int of the result of a die roll
   */
  private int rollDie6() {
    int dieRoll6;
    D6 die6 = new D6();
    return die6.roll();
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

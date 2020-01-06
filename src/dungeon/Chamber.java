package dungeon;

import dnd.models.ChamberContents;
import dnd.models.ChamberShape;
import dnd.models.Monster;
import dnd.models.Treasure;
import dnd.models.Stairs;
import dnd.models.Trap;
import dnd.die.D20;
import java.util.ArrayList;
import dnd.exceptions.UnusualShapeException;
import java.io.*;

public class Chamber extends Space implements java.io.Serializable {


  /**
   * Chamber contents object of the contents of this chamber.
   */
  private ChamberContents myContents;
  /**
   * ChamberShape of this chamber.
   */
  private ChamberShape myShape;
  /**
   * ArrayList of doors in this chamber.
   */
  private ArrayList<Door> chamberDoors;
  /**
   * ArrayList of all the monsters in this chamber.
   */
  private ArrayList<Monster> chamberMonsters;
  /**
   * ArrayList of all the Treasures in this chamber.
   */
  private ArrayList<Treasure> chamberTreasures;
  /**
   * ArrayList of all the traps in this chamber.
   */
  private ArrayList<Trap> chamberTraps;
  /**
   * Object's unique ID.
   */
  private int id;
  /**
   * Object's concatenated Description.
   */
  private String concatenated;
  /**
   * Object's concatenated Description.
   */
  private Stairs chamberStairs;

  /**
   * Method:  Chamber constructor.
   */
  public Chamber() {
    myContents = new ChamberContents();
    //myShape = new ChamberShape();
    setShape(myShape);
    myShape.setNumExits(rollDie());
    chamberDoors = new ArrayList<Door>();
    chamberMonsters = new ArrayList<Monster>();
    chamberTreasures = new ArrayList<Treasure>();
    chamberTraps = new ArrayList<Trap>();
    setDescription();
    /*Determine how many doors in chamber and create doors to match */
    makeChamberDoors(myShape);
  }

  /**
   * Method:  Chamber Constructor.
   * @param theShape chamber shape that is to be set to this chamber
   * @param theContents chamber contents that is to be set to this chamber
   */
  public Chamber(ChamberShape theShape, ChamberContents theContents) {
    myShape = theShape;
    myContents = theContents;
    chamberDoors = new ArrayList<Door>();
    chamberMonsters = new ArrayList<Monster>();
    chamberTreasures = new ArrayList<Treasure>();
    chamberTraps = new ArrayList<Trap>();
    /*Generating ChamberShape and contents*/
    setDescription();
    //Determine how many doors in chamber and create doors to match
    //makeChamberDoors(myShape);
  }

  /**
   * Method: makeChamberDoors: Makes a new door for each door in chamber shape.
   * @param theShape for which the doors are to be created base off of
   */
  public void makeChamberDoors(ChamberShape theShape) {
    int numExits;
    int i;
    numExits = theShape.getNumExits();

    //Output exit location and description for each exit
    for (i = 0; i < numExits; i++) {
      Door newDoor = new Door();
      /*Takes the id number of this chamber (up to 5)
      and adds the door number onto it
      for example chamber 5 door 2 id would be 52*/
      newDoor.setID((id * 10) + (i + 1));
      if (i == 1) {
        newDoor.setTrapped(true, rollDie());
      } else if (i == 2) {
        newDoor.setOpen(true);
      } else {
        newDoor.setArchway(true);
      }
      setDoor(newDoor);
    }
  }

  /**
   * Method: setShape: sets chambers shape based off parameter.
   * @param theShape the shape that is to be set
   */
  public void setShape(ChamberShape theShape) {
    myShape = theShape.selectChamberShape(rollDie());
    //theShape.setShape(theShape.getDescription());
  }

  /**
   * Method: getShape: gets chambers shape.
   * @return myShape the shape of this chamber.
   */
  public ChamberShape getChamberShape() {
    return myShape;
  }

  /**
   * Method: getContents: gets chambers shape.
   * @return myContents the contents of this chamber.
   */
  public ChamberContents getContents() {
    return myContents;
  }

  /**
   * Method: setExits sets the exits in this chamber.
   * @param theShape the shape that is to be set
   */
  public void setExits(ChamberShape theShape) {
    myShape = theShape.selectChamberShape(rollDie());
    //theShape.setShape(theShape.getDescription());
  }

  /**
   * Method:  getDoors: returns array of doors in chamber.
   * @return chamberDoors array of all doors in the chamber
   */
  public ArrayList<Door> getDoors() {
    return chamberDoors;
  }

  /**
   * Method: addMonster: adds monster to chamber.
   * @param theMonster a monster object that is to be added to the chamber
   */
  public void addMonster(Monster theMonster) {
    chamberMonsters.add(theMonster);
  }

  /**
   * Method:  removeMonster: remove monster from chamber.
   * @return boolean result as to if monster was removed
   * @param aMonster monster was removed
   */
  public boolean removeMonster(Monster aMonster) {
    boolean result = false;
    if (chamberMonsters.size() > 0) {
      for (int i = 0; i < chamberMonsters.size(); i++) {
        if (chamberMonsters.get(i).getDescription().contains(aMonster.getDescription())) {
          chamberMonsters.remove(i);
          result = true;
        }
      }
    }
    return result;
  }

  /**
   * Method:  getMonster: gets all the monsters that are in this chamber.
   * @return arraylist of all the monster in the chamber
   */
  public ArrayList<Monster> getMonsters() {
    return chamberMonsters;
  }
  /**
   * Method:  setMonsters: gets all the monsters that are in this chamber.
   * @param monsters array to be set to this chamber.
   */
  public void setMonsters(ArrayList<Monster> monsters) {
    chamberMonsters = monsters;
  }
  /**
   * Method:  getStairs: gets all the stairs that are in this chamber.
   * @return Stairs in the chamber
   */
  public Stairs getStairs() {
    return chamberStairs;
  }

  /**
   * Method:  addTreasure: adds treasure to chamber.
   * @param theTreasure treasure object that is to be added to this chamber
   */
  public void addTreasure(Treasure theTreasure) {
    chamberTreasures.add(theTreasure);
  }

  /**
   * Method:  removeTreasure: remove treasure from chamber.
   * @return boolean result as to if treasure was removed
   * @param aTreasure result as to if treasure was removed
   */
  public boolean removeTreasure(Treasure aTreasure) {
    boolean result = false;
    if (chamberTreasures.size() > 0) {
      for (int i = 0; i < chamberTreasures.size(); i++) {
        if (chamberTreasures.get(i).getDescription().contains(aTreasure.getDescription())) {
          chamberTreasures.remove(i);
          result = true;
        }
      }
    }
    return result;
  }

  /**
   * Method:  getTreasureList: gets the list of all treasures in this chamber.
   * @return arraylist of all the treasures in this chamber
   */
  public ArrayList<Treasure> getTreasureList() {
    return chamberTreasures;
  }

  /**
   * Method:  setTreasureList: sets the list of all treasures in this chamber.
   * @param treasures arraylist of all the treasures in this chamber
   */
  public void setTreasureList(ArrayList<Treasure> treasures) {
    chamberTreasures = treasures;
  }

  /**
   * Method: addTrap: adds trap to this chamber.
   * @param theTrap the trap that is to be added to the chamber
   */
  public void addTrap(Trap theTrap) {
    theTrap.chooseTrap(rollDie());
    chamberTraps.add(theTrap);
  }

  /**
   * Method: getTrapsf gets the trap arraylist in this chamber.
   * @return chamberTraps array of all traps in this chamber
   */
  public ArrayList<Trap> getTraps() {
    return chamberTraps;
  }

  /**
   * Method: set description of this chamber.
   * @return description string
   */
  public String setDescription() {
    concatenated = "";

    if (myContents.getDescription().equals("monster only")) {
      concatenated = generateMonster(concatenated);
    } else if (myContents.getDescription().equals("treasure")) {
      concatenated = generateTreasure(concatenated);
    } else if (myContents.getDescription().equals("monster and treasure")) {
      concatenated = generateMonster(concatenated);
      concatenated = concatenated + "\n";
      concatenated = generateTreasure(concatenated);
    } else if (myContents.getDescription().equals("empty")) {
      concatenated += "Nothing\n";
    } else if (myContents.getDescription().equals("stairs")) {
      concatenated = generateStairs(concatenated);
    } else if (myContents.getDescription().equals("trap")) {
      concatenated = generateTrap(concatenated);
    }

    return concatenated;
  }

  /**
   * Method: getDescription: gets description of chamber.
   * @return string of chambers description
   */
  @Override
  public String getDescription() {
    concatenated = "";
    /*Generate stairs, monster, treasure, or trap based on room contents generated*/
    try {
      concatenated += "The Chamber is a " + myShape.getShape() + " that is " + myShape.getLength() + "x" + myShape.getWidth() + "\n";
    } catch (UnusualShapeException e) {
      concatenated += "The Chamber is " + myShape.getShape() + "\n";
    }

    concatenated += "It is " + myShape.getArea() + " square feet.\n";
    concatenated += "In the Chamber there is: \n";
    if (myContents.getDescription().equals("monster only")) {
      concatenated = getMonstersAndTreasures(concatenated);
    } else if (myContents.getDescription().equals("treasure")) {
      concatenated = getMonstersAndTreasures(concatenated);
    } else if (myContents.getDescription().equals("empty")) {
      if (getMonstersAndTreasures(concatenated).equals(concatenated)) {
        concatenated += "\tNothing\n";
      } else {
        concatenated = getMonstersAndTreasures(concatenated);
      }
    } else if (myContents.getDescription().equals("monster and treasure")) {
      concatenated = concatenated + "\n";
      concatenated = getMonstersAndTreasures(concatenated);
    } else if (myContents.getDescription().equals("stairs")) {
      concatenated += "\tStairs: " + chamberStairs.getDescription() + "\n";
      concatenated = getMonstersAndTreasures(concatenated);
    } else if (myContents.getDescription().equals("trap")) {
      concatenated = getChamberTraps(concatenated);
      concatenated = getMonstersAndTreasures(concatenated);
    }
    return concatenated;
  }

  private String getChamberTraps(String concatenated) {
    for (int i = 0; i < chamberTraps.size(); i++) {
      concatenated += "\tTrap: " + chamberTraps.get(i).getDescription() + "\n";
    }
    return concatenated;
  }

  private String getMonstersAndTreasures(String concatenated) {
    for (int i = 0; i < chamberMonsters.size(); i++) {
      concatenated += "\tMonster: " + chamberMonsters.get(i).getDescription() + "\n";
    }
    for (int j = 0; j < chamberTreasures.size(); j++) {
      concatenated += "\tTreasure: " + chamberTreasures.get(j).getDescription();
      concatenated += " that is protected by " + chamberTreasures.get(j).getProtection() + "\n";
      concatenated += "\t\tIt is in " + chamberTreasures.get(j).getContainer() + "\n";
    }
    return concatenated;
  }

  /**
   * Method: setDoor: to add a door connection to this room.
   * @param newDoor door object newDoor that is to be added to this room
   */
  @Override
  public void setDoor(Door newDoor) {
    chamberDoors.add(newDoor);
  }

  /**
   * Method: setDoor: to add a door connection to this room.
   * @param newDoor door object newDoor that is to be added to this room
   * @param doorID doors id
   */
  public void setDoor(Door newDoor, int doorID) {
    for (int i = 0; i < getDoors().size(); i++) {
      if (getDoors().get(i).getID() == doorID) {
        chamberDoors.set(i, newDoor);
      }
    }
  }

  /**
   * Method: generateTreasure: generates some treasure.
   * @param concatenated string that is to be concatenated with
   * @return concatenated string of treasure
   */
  private String generateTreasure(String concatenated) {
    Treasure generatedTreasure = new Treasure();
    generatedTreasure.chooseTreasure(rollDie());
    generatedTreasure.setContainer(rollDie());
    addTreasure(generatedTreasure);
    concatenated += "\tTreasure: " + generatedTreasure.getDescription();
    concatenated += " that is protected by " + generatedTreasure.getProtection() + "\n";
    concatenated += "\t\tIt is in " + generatedTreasure.getContainer() + "\n";
    return concatenated;
  }

  /**
   * Method: generateTrap: generates a trap.
   * @param concatenated string that is to be concatenated with
   * @return concatenated string of trap
   */
  private String generateTrap(String concatenated) {
    Trap generatedTrap = new Trap();
    addTrap(generatedTrap);
    concatenated = concatenated + "\tTrap: " + generatedTrap.getDescription() + "\n";
    return concatenated;
  }

  /**
   * Method: generateTrap: generates a monster.
   * @param concatenated string that is to be concatenated with
   * @return concatenated string of monster
   */
  private String generateMonster(String concatenated) {
    Monster generatedMonster = new Monster();
    generatedMonster.setType(rollDie());
    addMonster(generatedMonster);
    for (int i = 0; i < chamberMonsters.size(); i++) {
      concatenated = concatenated + "\tMonster: " + chamberMonsters.get(i).getDescription() + "\n";
    }
    return concatenated;
  }

  /**
   * Method: generateTrap: generates a stairs.
   * @param concatenated string that is to be concatenated with
   * @return concatenated string of stairs
   */
  private String generateStairs(String concatenated) {
    Stairs generatedStairs = new Stairs();
    generatedStairs.setType(rollDie());
    chamberStairs = generatedStairs;
    concatenated = concatenated + "\tStairs: " + generatedStairs.getDescription();
    return concatenated;
  }

  /**
   * Method: rollDie rolls a 20 sided die.
   * @return int of die roll result
   */
  private int rollDie() {
    D20 aDie = new D20();
    return aDie.roll();
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

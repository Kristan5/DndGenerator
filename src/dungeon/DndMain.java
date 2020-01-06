package dungeon;

import java.util.ArrayList;
import java.util.HashMap;
import dnd.models.ChamberContents;
import dnd.models.ChamberShape;
import dnd.die.D20;
import java.io.*;

public final class DndMain implements java.io.Serializable {

  private ArrayList<Chamber> theChambers;
  private ArrayList<Space> theSpaces;
  private HashMap<Door, ArrayList<Chamber>> level;
  private ArrayList<Integer> unusedDoorsChamber;

  /**
   *
   */
  public DndMain() {
    initializeArrays();
    create();
  }
  private void initializeArrays() {
    theChambers = null;
    level = null;
    unusedDoorsChamber = null;
    theSpaces = null;
    theChambers = new ArrayList<Chamber>();
    level = new HashMap<Door, ArrayList<Chamber>>();
    unusedDoorsChamber = new ArrayList<Integer>();
    theSpaces = new ArrayList<Space>();
  }

  /**
   *
   * @return theChambers: ArrayList of all the 5 chambers in this level
   */
  public ArrayList<Chamber> getChambers() {
    return theChambers;
  }

  /**
   *
   * @return theChambers: ArrayList of all the 5 chambers in this level
   */
  public ArrayList<Space> getSpaces() {
    return theSpaces;
  }

  private ArrayList<Integer> getUnusedDoors() {
    return unusedDoorsChamber;
  }

  /**
   *
   * @return level: a HashMap of which door in this level is mapped to which chamber(s)
   */
  public HashMap<Door, ArrayList<Chamber>> getLevel() {
    return level;
  }

  private String generateChambers(String description) {
    int chamberID = 1;
    for (int i = 0; i < 5; i++) {
      Chamber aChamber = createChamber(chamberID);
      chamberID++;
      int exits = aChamber.getDoors().size();
      //description += "\nChamber #" + (i + 1) + " has " + exits + " exits.\n";
      description += exits + "\n";
      unusedDoorsChamber.add(exits);
      theChambers.add(aChamber);
      theSpaces.add(aChamber);
    }
    return description;
  }

  private Chamber createChamber(int chamberID) {
    ChamberShape aShape = null;
    aShape = aShape.selectChamberShape(rollDie());
    aShape.setNumExits(rollDie());
    ChamberContents aContents = new ChamberContents();
    aContents.chooseContents(rollDie());
    Chamber aChamber = new Chamber(aShape, aContents);
    aChamber.setID(chamberID);
    aChamber.makeChamberDoors(aShape);
    return aChamber;
  }
  /**
   *
   * @return int of dieRoll result
   */
  public int rollDie() {
    D20 aDie = new D20();
    return aDie.roll();
  }

  private Passage createPassage(int passageID) {
    PassageSection passageSectionOne = new PassageSection();
    PassageSection passageSectionTwo = new PassageSection();
    Passage aPassage = new Passage();
    aPassage.setID(passageID);
    aPassage.addPassageSection(passageSectionOne);
    aPassage.addPassageSection(passageSectionTwo);
    return aPassage;
  }

  private String setDoor(int j, int k, int nextDoor, String description) {
    unusedDoorsChamber.set(nextDoor, unusedDoorsChamber.get(nextDoor) - 1);
    unusedDoorsChamber.set(j, unusedDoorsChamber.get(j) - 1);
    /*
    description += "\nChamber #" + (j + 1) + " connects to Chamber #" + (nextDoor + 1);
    description += "\n\tDoor #" + (k + 1) + " in Chamber #" + (j + 1) + " and ";
    description += "Door #" + (nextDoor) + " in Chamber #" + (nextDoor + 1) + "\n";
    */
    return description;
  }

  private String generateLevel(String description) {
    /*Loop through each chamber*/
    int passageCount = 1;

    for (int j = 0; j < theChambers.size(); j++) {

      int numDoors = theChambers.get(j).getDoors().size();
      ArrayList<Chamber> chamberDoor = new ArrayList<Chamber>();
      ArrayList<Chamber> targetDoor = new ArrayList<Chamber>();
      int doorID;
      int passageID = 6;

      if (j != theChambers.size() - 1) {
        int nextDoor = j + 1;

        /*Checks if the next chamber exists and if it has any available doors*/
        if (unusedDoorsChamber.get(nextDoor) != null && unusedDoorsChamber.get(j) > 0) {
          int someDoors = unusedDoorsChamber.get(j);
          /*Finding a chamber for each door*/
          for (int k = 0; k < someDoors; k++) {

            if (nextDoor < theChambers.size() && unusedDoorsChamber.get(nextDoor) > 0) {
              targetDoor.add(theChambers.get(nextDoor));
              chamberDoor.add(theChambers.get(j));

              level.put(theChambers.get(nextDoor).getDoors().get(unusedDoorsChamber.get(nextDoor) - 1), targetDoor);
              level.put(theChambers.get(j).getDoors().get(k), chamberDoor);
              Passage aPassage = createPassage(passageID);
              passageID++;
              /*Sets the number k door in this chamber to a aPassage and another chamber*/
              theChambers.get(j).getDoors().get(k).setSpaces(theChambers.get(nextDoor), aPassage);
              theChambers.get(nextDoor).getDoors().get(unusedDoorsChamber.get(nextDoor) - 1).setSpaces(theChambers.get(j), aPassage);
              //new need to check if this works FIX THIS
              //aPassage.setDoor(theChambers.get(k).getDoors().get(k));
              theSpaces.add(aPassage);
              //passageCount ++;
              /*Reducing the amount of doors that are free for both doors that were connected*/
              description = setDoor(j, k, nextDoor, description);
              nextDoor++;
            }
          }
        }
      }
    }
    return description;
  }

  private String levelValid(String description) {
    int count = 0;
    for (int m = 0; m < theChambers.size(); m++) {
      if (unusedDoorsChamber.get(m) > 0) {
        count++;
      }
    }
    if (count != 0) {
      initializeArrays();
      description = "restart";
    }
    return description;
  }

  /**
  *@return array list of spaces
  */
  public ArrayList<Space> create() {
    boolean created = false;
    String description = "";
    while (!created) {
      description = generateChambers(description);
      description = generateLevel(description);
      description = levelValid(description);
      if (!description.equals("restart")) {
        created = true;
      } else {
        description = "";
      }
    }
    /*
    System.out.println("Right:\n"+description);
    test();
    */
    return theSpaces;
  }
}

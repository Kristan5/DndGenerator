package gui;

import java.util.ArrayList;
import java.util.HashMap;

import dungeon.Chamber;
import dungeon.Passage;
import dungeon.Space;
import dungeon.Door;
import dungeon.DndMain;
import dnd.models.Monster;
import dnd.models.Treasure;
import dnd.models.Trap;
import java.io.*;

public class Controller {
    private DndMain theMain;
    private DndGui myGui;
    private ArrayList<Space> theSpaces;
    private HashMap<Door, ArrayList<Chamber>> level;

  /**
   *
   * @param theGui gui that the controller controls
   */
    public Controller(DndGui theGui) {
        theMain = new DndMain();
        //theSpaces = theMain.create(theMain);
        theSpaces = theMain.getSpaces();

        myGui = theGui;
        level = theMain.getLevel();
    }
  /**
   *
   * @return arraylist of all the spaces in the level (space arrayList)
   */
    public ArrayList<Space> getSpacesList() {
        return theMain.getSpaces();
    }

  /**
   *
   * @return array list of all the spaces in level
   */
    public ArrayList<String> getNameSpaces() {
        ArrayList<String> spaceType = new ArrayList<String>();
        int passageCount = 0;
        int chamberCount = 0;
        for (int i = 0; i < theSpaces.size(); i++) {
            if (theSpaces.get(i) instanceof Passage) {
                passageCount++;
                spaceType.add("Passage #" + passageCount);
            } else if (theSpaces.get(i) instanceof Chamber) {
                chamberCount++;
                spaceType.add("Chamber #" + chamberCount);
            }
        }
        return spaceType;
    }

  /**
   * @return array list of all the doors in  the space
   * @param theSpace the space to get doors from
   */
    public ArrayList<String> getSpaceDoors(String theSpace) {
        Space aSpace = getSelectedSpace(theSpace);
        ArrayList<String> spaceDoorsString = new ArrayList<String>();
        ArrayList<Door> spaceDoors;

        if (aSpace instanceof Chamber) {
            Chamber aChamber = (Chamber) aSpace;
            spaceDoors = aChamber.getDoors();
        } else if (aSpace instanceof Passage) {
            Passage aPassage = (Passage) aSpace;
            spaceDoors = aPassage.getDoors();
        } else {
            spaceDoors = null;
            return null;
        }
        for (int k = 0; k < spaceDoors.size(); k++) {
            spaceDoorsString.add("Door #" + (k + 1));
        }
        return spaceDoorsString;
    }

  /**
   * @return Space of string given
   * @param aSpace space to get
   */
    public Space getSelectedSpace(String aSpace) {
        int spaceNum = Character.getNumericValue(aSpace.charAt(aSpace.length() - 1));
        int offset = 5;
        if (aSpace.charAt(0) == 'C') {
            return theSpaces.get(spaceNum - 1);
        } else if (aSpace.charAt(0) == 'P') {
            return theSpaces.get(offset + (spaceNum - 1));
        } else {
            return null;
        }
    }

  /**
   * @return String of space connected by door
   * @param aDoor door to get connection from
   * @param aSpace space to get connection of
   */
    public String getConnectedSpace(String aSpace, String aDoor) {
        Space theConnectedSpace = null;
        String connected = null;
        Space theSpace = getSelectedSpace(aSpace);
        Chamber temp1chamber = null;
        Passage temp1passage = null;
        if (theSpace instanceof Chamber) {
            temp1chamber = (Chamber) theSpace;
        } else if (theSpace instanceof Passage) {
            temp1passage = (Passage) theSpace;
        }
        Door theDoor = getSelectedDoor(aSpace, aDoor);
        ArrayList<Space> connectedSpaces = theDoor.getSpaces();
        //Gets spaces of doors and checks to get the other connected space
        for (int i = 0; i < connectedSpaces.size(); i++) {
            if (connectedSpaces.get(i) instanceof Chamber) {
                Chamber temp2 = (Chamber) connectedSpaces.get(i);
                //Checks to see if the spaces are the same by checking the ID
                if (temp1chamber != null && temp2.getID() != temp1chamber.getID()) {
                    theConnectedSpace = temp2;
                } else if (temp1passage != null && temp2.getID() != temp1passage.getID()) {
                    theConnectedSpace = temp2;
                }
            } else if (connectedSpaces.get(i) instanceof Passage) {
                Passage temp2 = (Passage) connectedSpaces.get(i);
                if (temp1chamber != null && temp2.getID() != temp1chamber.getID()) {
                    theConnectedSpace = temp2;
                } else if (temp1passage != null && temp2.getID() != temp1passage.getID()) {
                    theConnectedSpace = temp2;
                }
            } else {
                Chamber temp2 = null;
            }
        }
        if (theConnectedSpace instanceof Chamber) {
            Chamber temp3 = (Chamber) theConnectedSpace;
            connected = "Chamber #" + temp3.getID();
        } else if (theConnectedSpace instanceof Passage) {
            Passage temp3 = (Passage) theConnectedSpace;
            connected = "Passage #" + (temp3.getID() - 5);
        }
        return connected;
    }

  /**
   * @return description of space
   * @param theSpace to get description of
   */
    public String getSelectedSpaceDescription(String theSpace) {
        Space aSpace = getSelectedSpace(theSpace);
        return aSpace.getDescription();
    }

  /**
   * @return the door
   * @param theSpace space to get door from
   * @param aDoor a door to get in space
   */
    public Door getSelectedDoor(String theSpace, String aDoor) {
        /*in aSpace get aDoor*/
        Space aSpace = getSelectedSpace(theSpace);
        int doorNum = Character.getNumericValue(aDoor.charAt(aDoor.length() - 1));
        if (aSpace instanceof Chamber) {
            Chamber temp = (Chamber) aSpace;
            return temp.getDoors().get(doorNum - 1);
        } else if (aSpace instanceof Passage) {
            Passage temp = (Passage) aSpace;
            return temp.getDoors().get(doorNum - 1);
        } else {
            return null;
        }
    }

  /**
   * @return array list of all the monsters in the space
   * @param theSpace space to get monsters from
   */
    public ArrayList<String> getMonsterList(String theSpace) {
        Space aSpace = getSelectedSpace(theSpace);
        ArrayList<String> monsterList = new ArrayList<String>();
        if (aSpace instanceof Chamber) {
            Chamber temp = (Chamber) aSpace;
            ArrayList<Monster> monsters = temp.getMonsters();
            for (int i = 0; i < monsters.size(); i++) {
                monsterList.add(monsters.get(i).getDescription());
            }
        } else if (aSpace instanceof Passage) {
            Passage temp = (Passage) aSpace;
            if (temp.getPassageSections().get(0).getMonster() != null) {
                monsterList.add(temp.getPassageSections().get(0).getMonster().getDescription());
            }
            if (temp.getPassageSections().get(1).getMonster() != null) {
                monsterList.add(temp.getPassageSections().get(1).getMonster().getDescription());
            }
        }
        return monsterList;
    }

  /**
   * @return array list of all the treasure in the space
   * @param theSpace space to get treasure from
   */
    public ArrayList<String> getTreasureList(String theSpace) {
        Space aSpace = getSelectedSpace(theSpace);
        ArrayList<String> treasureList = new ArrayList<String>();
        if (aSpace instanceof Chamber) {
            Chamber temp = (Chamber) aSpace;
            ArrayList<Treasure> treasures = temp.getTreasureList();
            for (int i = 0; i < treasures.size(); i++) {
                treasureList.add(treasures.get(i).getDescription());
            }
        } else if (aSpace instanceof Passage) {
            Passage temp = (Passage) aSpace;
            if (temp.getPassageSections().get(0).getTreasure() != null) {
                treasureList.add(temp.getPassageSections().get(0).getTreasure().getDescription());
            }
            if (temp.getPassageSections().get(1).getTreasure() != null) {
                treasureList.add(temp.getPassageSections().get(1).getTreasure().getDescription());
            }
        }
        return treasureList;
    }

  /**
   * @return arraylist of all the traps in the space
   * @param theSpace the space to get traps from
   */
    public ArrayList<String> getTrapList(String theSpace) {
        Space aSpace = getSelectedSpace(theSpace);
        ArrayList<String> trapList = new ArrayList<String>();
        if (aSpace instanceof Chamber) {
            Chamber temp = (Chamber) aSpace;
            ArrayList<Trap> traps = temp.getTraps();
            for (int i = 0; i < traps.size(); i++) {
                trapList.add(traps.get(i).getDescription());
            }
        }
        return trapList;
    }

  /**
   *
   * @param theSelectedSpace space to add mosnter to
   */
    public void addMonsterButton(String theSelectedSpace) {
        if (getSelectedSpace(theSelectedSpace) instanceof Chamber) {
            Chamber temp = (Chamber) getSelectedSpace(theSelectedSpace);
            Monster aMonster = new Monster();
            aMonster.setType(theMain.rollDie());
            temp.addMonster(aMonster);
        } else if (getSelectedSpace(theSelectedSpace) instanceof Passage) {
            Passage temp = (Passage) getSelectedSpace(theSelectedSpace);
            Monster aMonster = new Monster();
            aMonster.setType(theMain.rollDie());
            temp.addMonster(aMonster, 0);
        }
    }

  /**
   *
   * @param theSelectedSpace space to remove monster from
   * @param monsterDescription of monster to remove
   */
    public void removeMonsterButton(String theSelectedSpace, String monsterDescription) {
        if (getSelectedSpace(theSelectedSpace) instanceof Chamber) {
            Chamber temp = (Chamber) getSelectedSpace(theSelectedSpace);
            ArrayList<Monster> newMonsters = temp.getMonsters();
            for (int i = 0; i < temp.getMonsters().size(); i++) {
                if (temp.getMonsters().get(i).getDescription().equals(monsterDescription)) {
                    newMonsters.remove(i);
                }
            }
            temp.setMonsters(newMonsters);
        } else if (getSelectedSpace(theSelectedSpace) instanceof Passage) {
            Passage temp = (Passage) getSelectedSpace(theSelectedSpace);
            if (temp.getMonster(0).getDescription().equals(monsterDescription)) {
                temp.removeMonster(temp.getMonster(0), 0);
            }
        }
    }

  /**
   *
   * @param theSelectedSpace space to add treasure to
   */
    public void addTreasureButton(String theSelectedSpace) {
        if (getSelectedSpace(theSelectedSpace) instanceof Chamber) {
            Chamber temp = (Chamber) getSelectedSpace(theSelectedSpace);

            Treasure aTreasure = new Treasure();
            aTreasure.chooseTreasure(theMain.rollDie());
            aTreasure.setContainer(theMain.rollDie());
            temp.addTreasure(aTreasure);
        } else if (getSelectedSpace(theSelectedSpace) instanceof Passage) {
            Passage temp = (Passage) getSelectedSpace(theSelectedSpace);

            Treasure aTreasure = new Treasure();
            aTreasure.chooseTreasure(theMain.rollDie());
            aTreasure.setContainer(theMain.rollDie());
            temp.addTreasure(aTreasure, 0);
        }
    }

  /**
   *
   * @param theSelectedSpace space to remove treasure from
   * @param treasureDescription description of treasure to remove
   */
    public void removeTreasureButton(String theSelectedSpace, String treasureDescription) {
        if (getSelectedSpace(theSelectedSpace) instanceof Chamber) {
            Chamber temp = (Chamber) getSelectedSpace(theSelectedSpace);
            ArrayList<Treasure> newTreasures = temp.getTreasureList();
            for (int i = 0; i < temp.getTreasureList().size(); i++) {
                if (temp.getTreasureList().get(i).getDescription().contains(treasureDescription)) {
                    newTreasures.remove(i);
                }
            }
            temp.setTreasureList(newTreasures);
        } else if (getSelectedSpace(theSelectedSpace) instanceof Passage) {
            Passage temp = (Passage) getSelectedSpace(theSelectedSpace);
            if (temp.getTreasure(0).getDescription().contains(treasureDescription)) {
                temp.removeTreasure(temp.getTreasure(0), 0);
            }
        }
    }

  /**
   * @return String of description
   * @param theDoor to get description of
   * @param theSelectedSpace space to get door from
   */
    public String getSelectedDoorDescription(String theDoor, String theSelectedSpace) {
        String result = "";
        Door aDoor = getSelectedDoor(theSelectedSpace, theDoor);
        if (aDoor.getSpaces().get(1) instanceof Chamber) {
            Chamber temp = (Chamber) aDoor.getSpaces().get(1);
            //result += "Door connects " + theSelectedSpace +" to Chamber #" + temp.getID() + " through ";
            result += "Door connects this Space to Chamber #" + temp.getID() + " to ";
        } else if (aDoor.getSpaces().get(1) instanceof Passage) {
            Passage temp = (Passage) aDoor.getSpaces().get(1);
            result += "Door connects Passage #" + (temp.getID() - 5) + " to ";
        }
        if (aDoor.getSpaces().get(0) instanceof Chamber) {
            Chamber temp = (Chamber) aDoor.getSpaces().get(0);
            result += "Chamber #" + temp.getID() + "\n";
        } else if (aDoor.getSpaces().get(0) instanceof Passage) {
            Passage temp = (Passage) aDoor.getSpaces().get(0);
            result += "Passage #" + (temp.getID() - 5) + "\n";
        }
        String doorsDescription = "";
        if (aDoor.getDescription() != null) {
            doorsDescription = aDoor.getDescription();
        }
        result += "Door is " + aDoor.getState() + ":\n\t" + doorsDescription + "\n";
        return result;
    }

  /**
   * @return string of shape
   * @param aSpace space to get shape of
   */
    public String getSelectedShape(String aSpace) {
        String result = null;
        Space theSpace;
        ArrayList<String> possibleShapes = possibleShapes();
        if (aSpace != null) {
            theSpace = getSelectedSpace(aSpace);
        } else  {
            theSpace = null;
            result = "Square";
        }
        if (theSpace instanceof Chamber) {
            Chamber temp = (Chamber) theSpace;
            result = temp.getChamberShape().getShape();
        } else if (theSpace instanceof Passage) {
            Passage temp = (Passage) theSpace;
            result = "Rectangle";
        }
        if (!possibleShapes.contains(result)) {
            result = "Square";
        }
        return result;
    }

  /**
   *
   * @return arrayList of all the possible shapes a chamber can be
   */
    private ArrayList<String> possibleShapes() {
        ArrayList<String> possibleShapes = new ArrayList<String>();
        possibleShapes.add("Cave");
        possibleShapes.add("Square");
        possibleShapes.add("Circular");
        possibleShapes.add("Hexagonal");
        possibleShapes.add("Odd-Shaped");
        possibleShapes.add("Oval");
        possibleShapes.add("Rectangle");
        possibleShapes.add("Trapezoidal");
        possibleShapes.add("Triangular");
        return possibleShapes;
    }

  /**
   * @return int number of treasures in aspace;
   * @param aSpace space to get number of treasures from
   */
    public int getNumTreasures(String aSpace) {
        Space theSpace = getSelectedSpace(aSpace);
        int numTreasures = 0;
        if (theSpace instanceof Chamber) {
            Chamber temp = (Chamber) theSpace;
            numTreasures = temp.getTreasureList().size();
        } else if (theSpace instanceof Passage) {
            Passage temp = (Passage) theSpace;
            for (int i = 0; i < temp.getPassageSections().size(); i++) {
                if (temp.getPassageSections().get(i).getTreasure() != null) {
                    numTreasures++;
                }
            }
        }
        return numTreasures;
    }

  /**
   * @return true if has stairs false if does not
   * @param aSpace space to check if has stairs
   */
    public boolean hasStairs(String aSpace) {
        Space theSpace = getSelectedSpace(aSpace);
        boolean stairs = false;
        if (theSpace instanceof Chamber) {
            Chamber temp = (Chamber) theSpace;
            if (temp.getStairs() != null) {
                stairs = true;
            }
        }
        return stairs;
    }

  /**
   * @return String of description
   * @param aSpace space to get stairs description from
   */
    public String getStairsDescription(String aSpace) {
        Space theSpace = getSelectedSpace(aSpace);
        String stairsDescription = "";
        if (theSpace instanceof Chamber) {
            Chamber temp = (Chamber) theSpace;
            if (temp.getStairs() != null) {
                stairsDescription = temp.getStairs().getDescription();
            } else {
                stairsDescription = "The stairs go down a level";
            }
        }
        return stairsDescription;
    }

  /**
   *
   * @return hashmap of level connection
   */
    public HashMap<Door, ArrayList<Chamber>> getLevelConnections() {
        HashMap<Door, ArrayList<Chamber>> theLevel = theMain.getLevel();
        /*This should help show all of the doors connected to whichever
        Chamber or passage on the right side of the window*/
        return theLevel;
    }

  /**
   *
   * @param filename name of file
   */
    public void serialize(File filename) {
        try {
            FileOutputStream fileout = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileout);
            out.writeObject(theMain);
            out.close();
            fileout.close();
            /*System.out.println("Serialized Data Saved in " + filename);*/
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
  /**
   *
   * @param filename name of file
   */
    public void deserialize(File filename) {
        DndMain load = null;
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            load = (DndMain) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("DndMain Level not Found");
            c.printStackTrace();
            return;
        }

        theMain = load;
        theSpaces = theMain.getSpaces();
        level = theMain.getLevel();
    }
}

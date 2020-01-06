package dungeon;

import java.io.*;

public abstract class Space implements java.io.Serializable {


  /**
   *
   * @return string of spaces description
   */
  public abstract String getDescription();

  /**
   *
   * @param theDoor the door to be set
   */
  public abstract void setDoor(Door theDoor);
}

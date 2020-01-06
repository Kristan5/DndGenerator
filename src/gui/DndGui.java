package gui;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Popup;
import javafx.stage.Stage;

import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import java.util.ArrayList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileInputStream;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;
import javafx.scene.layout.StackPane;



public class DndGui<toReturn> extends Application {
    /* Even if it is a GUI it is useful to have instance variables
    so that you can break the processing up into smaller methods that have
    one responsibility.
     */
    private Controller theController;
    private BorderPane root;  //the root element of this GUI
    private Popup descriptionPane;
    private Stage primaryStage;  //The stage that is passed in on initialization
    private ListView<String> spaceList;
    private ChoiceBox<String> doorList;
    private String spaceDescription = "";
    private String theSelectedSpace = "Chamber #1";
    private String theSelectedDoor;
    private String theEditedMonster;
    private String theEditedTreasure;
    private boolean confirmResult;
    private ObservableList<String> selected;

    /*a call to start replaces a call to the constructor for a JavaFX GUI*/
    @Override
    public void start(Stage assignedStage) {
        /*Initializing instance variables */
        theController = new Controller(this);
        primaryStage = assignedStage;
        root = setUpRoot();
        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add("res/guiStyleSheet.css");
        primaryStage.setTitle("Dungeon Master's ToolKit");

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private BorderPane setUpRoot() {
        BorderPane temp = new BorderPane();
        Node left = setLeftButtonPanel();
        Node center = setMiddlePanel(spaceDescription);
        Node right = setRightButtonPanel();
        Node top = setTopButtonPanel();
        Node bottom = setBottomButtonPanel();
        temp.setTop(top);
        temp.setLeft(left);
        temp.setRight(right);
        temp.setCenter(center);
        temp.setBottom(bottom);
        return temp;
    }

    private Node setMiddlePanel(String spaceDescription) {
        VBox middle = new VBox();
        Label middleDescription = new Label("Selected Space is: " + theSelectedSpace);
        //This is the optional Chamber/ Passage View part
        VBox room = makeSpaceView();
        Label aDescription = new Label(spaceDescription);
        Label numDoors = new Label();
        //Number of Doors
        if (theSelectedSpace != null) {
            ArrayList<String> spaceDoors = theController.getSpaceDoors(theSelectedSpace);
            numDoors = new Label("There are " + spaceDoors.size() + " doors\n");
        } else {
            numDoors = new Label();
        }
        aDescription.setStyle("fx-padding: 100");
        middle.setStyle("-fx-padding: 20;" + "-fx-border-style: solid inside;" + "-fx-border-radius: 10;" + "-fx-border-color: black;");
        spaceList = new ListView<String>();
        ArrayList<String> nameList = theController.getNameSpaces();
        for (int i = 0; i < nameList.size(); i++) {
            spaceList.getItems().add(theController.getNameSpaces().get(i));
        }
        middle.getChildren().addAll(middleDescription, room, numDoors, aDescription);
        return middle;
    }


    private Node setTopButtonPanel() {
        Menu menu1 = new Menu("File");
        MenuItem save = new MenuItem("Save File");
        MenuItem load = new MenuItem("Load File");
        save.setOnAction((ActionEvent event) -> {
            saveFile();
        });
        load.setOnAction((ActionEvent event) -> {
            loadFile();
        });
        menu1.getItems().add(save);
        menu1.getItems().add(new SeparatorMenuItem());
        menu1.getItems().add(load);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menu1);
        VBox temp = new VBox(menuBar);
        temp.setStyle("-fx-border-radius: 50px");

        return temp;
    }

    private VBox makeSpaceView() {
        Label instructions = new Label("Click on Monster, Treasure, Trap, Stairs, or Doors for details");
        StackPane spaceView = makeStackPane();
        VBox room = new VBox();
        room.getChildren().addAll(instructions, spaceView);
        return room;
    }

    private StackPane makeStackPane() {
        //For chamber/passage view
        StackPane temp = new StackPane();
        if (theSelectedSpace != null) {
            temp = setBackgroundShape(temp);
            HBox spaceContents = new HBox();
            spaceContents.setStyle("-fx-border-color: #dedcaf;" + "-fx-border-insets: 120;");
            HBox stairsHbox = makeStairsImage();
            HBox monsterHbox = makeMonsterImage();
            HBox treasureHbox = makeTreasureImage();
            VBox trapVBox = makeTrapImage();
            VBox doorsVbox = makeDoorsImage();
            spaceContents.getChildren().addAll(stairsHbox, monsterHbox, treasureHbox, doorsVbox, trapVBox);
            temp.getChildren().add(spaceContents);
        } else {
            temp.getChildren().add(new Label("Please Select a Space from the List"));
        }
        return temp;
    }

    private StackPane setBackgroundShape(StackPane temp) {
        //Finding the shape of the selected chamber and setting image of it
        String theShape = theController.getSelectedShape(theSelectedSpace);
        String filename = "res/" + theShape + ".png";
        try {
            Image image;
            if (theShape == "Rectangle") {
                FileInputStream input = new FileInputStream(filename);
                image = new Image(input, 350, 100, false, false);
            } else {
                FileInputStream input = new FileInputStream(filename);
                image = new Image(input, 350, 350, false, false);
            }
            ImageView iv1 = new ImageView(image);
            temp.getChildren().add(iv1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return temp;
    }

    private VBox makeTrapImage() {
        VBox trapVbox = new VBox();
        for (int i = 0; i < theController.getTrapList(theSelectedSpace).size(); i++) {
            Image trap = new Image("res/Trap.png", 60, 60, false, false);
            ImageView trapView = new ImageView(trap);
            trapVbox.getChildren().add(trapView);
            String aTrap = theController.getTrapList(theSelectedSpace).get(i);
            trapView.setOnMouseClicked((MouseEvent event) -> {
                makeDescriptionPopup(aTrap).show(primaryStage);
            });
        }
        return trapVbox;
    }

    private VBox makeDoorsImage() {
        VBox doorsVbox = new VBox();
        for (int i = 0; i < theController.getSpaceDoors(theSelectedSpace).size(); i++) {
            Image door = new Image("res/Door.png", 40, 40, false, false);
            ImageView doorView = new ImageView(door);
            doorsVbox.getChildren().add(doorView);
            String aDoor = theController.getSpaceDoors(theSelectedSpace).get(i);
            doorView.setOnMouseClicked((MouseEvent event) -> {
                theSelectedDoor = aDoor;
                createDoorPopUp(selectedDoorDescription(aDoor)).show(primaryStage);
            });
        }
        return doorsVbox;
    }

    private HBox makeStairsImage() {
        HBox stairsHbox = new HBox();
        if (theController.hasStairs(theSelectedSpace)) {
            Image stairs = new Image("res/Stairs.png", 40, 40, false, false);
            ImageView stairsView = new ImageView(stairs);
            stairsHbox.getChildren().add(stairsView);
            String aStairs = theController.getStairsDescription(theSelectedSpace);
            stairsView.setOnMouseClicked((MouseEvent event) -> {
                makeDescriptionPopup(aStairs).show(primaryStage);
            });
        }
        return stairsHbox;
    }

    private HBox makeMonsterImage() {
        HBox monsterHbox = new HBox();
        for (int i = 0; i < theController.getMonsterList(theSelectedSpace).size(); i++) {
            Image monster = new Image("res/Monster.png", 40, 40, false, false);
            ImageView monsterView = new ImageView(monster);
            monsterHbox.getChildren().add(monsterView);
            String aMonster = theController.getMonsterList(theSelectedSpace).get(i);
            monsterView.setOnMouseClicked((MouseEvent event) -> {
                makeDescriptionPopup(aMonster).show(primaryStage);
            });
        }
        return monsterHbox;
    }

    /*Generic Popup with description of label in parameter*/
    private Popup makeDescriptionPopup(String description) {
        Popup popup = new Popup();
        popup.setX(800);
        popup.setY(500);
        popup.setAutoHide(true);
        Label descriptionLabel = new Label(description);
        Button closePopUp = new Button("Close");
        closePopUp.setOnAction((ActionEvent event) -> {
            popup.hide();
        });
        closePopUp.setLayoutX(5);
        closePopUp.setLayoutY(110);
        popup.getContent().addAll(descriptionLabel, closePopUp);
        return popup;
    }

    private HBox makeTreasureImage() {
        HBox treasureHbox = new HBox();
        for (int i = 0; i < theController.getTreasureList(theSelectedSpace).size(); i++) {
            Image treasure = new Image("res/Treasure.png", 40, 40, false, false);
            ImageView treasureView = new ImageView(treasure);
            treasureHbox.getChildren().add(treasureView);
            String aTreasure = theController.getTreasureList(theSelectedSpace).get(i);
            //If mouse clicks the image the description of treasure will show
            treasureView.setOnMouseClicked((MouseEvent event) -> {
                makeDescriptionPopup(aTreasure).show(primaryStage);
            });
        }
        return treasureHbox;
    }

    private void loadFile() {
        FileChooser loadFile = new FileChooser();
        File selectedFile = loadFile.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            theController.deserialize(selectedFile);
            update();
        }
    }

    private void saveFile() {
        FileChooser saveFile = new FileChooser();
        File selectedFile = saveFile.showSaveDialog(primaryStage);
        if (selectedFile != null) {
            theController.serialize(selectedFile);
            update();
        }
    }

    private Node setBottomButtonPanel() {
        VBox temp = new VBox();
        temp.setStyle("-fx-padding: 20;");
        Button editButton = createButton("EDIT", "-fx-background-color: #ff0000; -fx-background-radius: 10, 10, 10, 10;");
        editButton.setOnAction((ActionEvent event) -> {
            createEditWindow();
        });
        temp.getChildren().add(editButton);
        return temp;
    }

    private Node setLeftButtonPanel() {
        VBox temp = new VBox();
        temp.setStyle("-fx-padding: 20;");

        Label theLabel = new Label("Spaces in this Level:");
        temp.getChildren().add(theLabel);

        spaceList = new ListView<String>();
        ArrayList<String> theSpaces = theController.getNameSpaces();
        for (int i = 0; i < theController.getNameSpaces().size(); i++) {
            spaceList.getItems().add(theSpaces.get(i));
        }
        spaceList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        selected = spaceList.getSelectionModel().getSelectedItems();
        spaceList.setOnMouseClicked((MouseEvent event) -> {
            if (!selected.isEmpty()) {
                    selectedSpace(selected);
                    update();
            }
        });
        temp.getChildren().add(spaceList);
        return temp;

    }

    private Node setRightButtonPanel() {
        VBox temp = new VBox();
        temp.setStyle("-fx-padding: 20;" + "-fx-border-radius: 50px");
        doorList = new ChoiceBox<String>();
        Label theLabel = new Label("Doors in this Space:");
        temp.getChildren().add(theLabel);
        //Populating with all the doors
        if (theSelectedSpace != null) {
            ArrayList<String> spaceDoors = theController.getSpaceDoors(theSelectedSpace);
            for (int i = 0; i < spaceDoors.size(); i++) {
                doorList.getItems().add(spaceDoors.get(i));
            }
        }
        doorList.setValue("List of Doors");
        doorList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                theSelectedDoor = newValue;
                createDoorPopUp(selectedDoorDescription(theSelectedDoor)).show(primaryStage);
            }
        });

        temp.getChildren().add(doorList);

        return temp;
    }

    //gets the description of a door string
    private String selectedDoorDescription(String theDoor) {
        String doorDescription = theController.getSelectedDoorDescription(theDoor, theSelectedSpace);
        return doorDescription;
    }

    /*popup that allows you to see where a door is connected and to change view by pressing
    go to connected space button*/
    private Popup createDoorPopUp(String doorDescription) {
        Popup popup = new Popup();
        popup.setX(600);
        popup.setY(300);
        popup.setAutoHide(true);
        Label doorLabel = new Label(doorDescription);
        Button closePopUp = new Button("Close");
        closePopUp.setOnAction((ActionEvent event) -> {
            popup.hide();
        });
        Button connectedSpace = new Button("Go to Connected Space");
        connectedSpace.setOnAction((ActionEvent event) -> {
            theSelectedSpace = theController.getConnectedSpace(theSelectedSpace, theSelectedDoor);
            spaceDescription = theController.getSelectedSpaceDescription(theSelectedSpace);
            popup.hide();
            update();
        });

        closePopUp.setLayoutX(5);
        closePopUp.setLayoutY(110);
        connectedSpace.setLayoutX(155);
        connectedSpace.setLayoutY(110);

        doorLabel.setStyle("-fx-padding: 20;");
        popup.getContent().addAll(doorLabel, closePopUp, connectedSpace);
        return popup;
    }

    private void selectedSpace(ObservableList<String> selected) {
        spaceDescription = theController.getSelectedSpaceDescription(selected.get(0));
        theSelectedSpace = selected.get(0);
    }

    private void update() {
        root = setUpRoot();
        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add("res/guiStyleSheet.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createEditWindow() {
        final Stage editWindow = new Stage();
        if (theSelectedSpace != null) {
            editWindow.setTitle("Editing " + theSelectedSpace);
            editWindow.initOwner(primaryStage);
            BorderPane temp = makeEditWindowBorderPane();
            Scene editScene = new Scene(temp, 575, 400);
            editWindow.setScene(editScene);
        } else {
            editWindow.setTitle("NO SPACE SELECTED");
        }
        editWindow.show();
        instructionsPopup().show(editWindow);
    }

    private Popup instructionsPopup() {
        Popup popup = new Popup();
        popup.setX(800);
        popup.setY(350);
        popup.setAutoHide(true);
        Label descriptionLabel = new Label("Instructions to use Editor:\nIf you would like to remove a specific monster or treausre,\nclick on the one you would like to remove in the list.\n If you would like to add a monster or treasure,\n click \"Add Monster\" or \"Add Treasure\"\n If you would like to see the Monster/Treasure you\n just added/removed in the editor\n please close and then reopen editor window\n(Click anywhere on this window to remove prompt)");
        descriptionLabel.setStyle("-fx-font-size: 11pt");
        popup.getContent().addAll(descriptionLabel);
        return popup;
    }

    private BorderPane makeEditWindowBorderPane() {
        BorderPane temp = new BorderPane();
        ListView editMonsterList = makeEditMonsterList();
        ListView editTreasureList = makeEditTreasureList();
        MenuBar menuBar = makeEditMenuBar(theEditedMonster, theEditedTreasure);

        ObservableList<String> selectedTreasure = editTreasureList.getSelectionModel().getSelectedItems();
        ObservableList<String> selectedMonster = editMonsterList.getSelectionModel().getSelectedItems();

        editMonsterList.setOnMouseClicked((MouseEvent event) -> {
            if (!selectedMonster.isEmpty()) {
                theEditedMonster = selectedMonster.get(0);
                makeAddRemovePopup(theEditedMonster, "monster").show(primaryStage);
            }
        });
        editTreasureList.setOnMouseClicked((MouseEvent event) -> {
            if (!selectedTreasure.isEmpty()) {
                theEditedTreasure = selectedTreasure.get(0);
                makeAddRemovePopup(theEditedTreasure, "treasure").show(primaryStage);
            }
        });
        VBox editVbox = new VBox(menuBar);
        Node editNode = (Node) editVbox;
        temp.setTop(editNode);
        temp.setLeft(editMonsterList);
        temp.setRight(editTreasureList);
        return temp;
    }

    private Popup makeAddRemovePopup(String description, String monsterOrTreasure) {
        Popup popup = new Popup();
        BorderPane popupPane = new BorderPane();
        popup.setX(800);
        popup.setY(600);
        popup.setAutoHide(true);
        Label descriptionLabel = new Label("Do you wish to remove " + description + "?");
        Button closePopUp = new Button("KEEP");
        closePopUp.setOnAction((ActionEvent event) -> {
            popup.hide();
        });
        Button remove = new Button("REMOVE");
        remove.setOnAction((ActionEvent event) -> {
            if (theEditedMonster != null && monsterOrTreasure.equals("monster")) {
                boolean confirmChange = confirmChanges();
                if (confirmChange) {
                    popup.hide();
                    theController.removeMonsterButton(theSelectedSpace, theEditedMonster);
                    spaceDescription = theController.getSelectedSpaceDescription(theSelectedSpace);
                    update();
                }
            } else if (theEditedTreasure != null && monsterOrTreasure.equals("treasure")) {
                boolean confirmChange = confirmChanges();
                if (confirmChange) {
                    popup.hide();
                    theController.removeTreasureButton(theSelectedSpace, theEditedTreasure);
                    spaceDescription = theController.getSelectedSpaceDescription(theSelectedSpace);
                    update();
                }
            }
        });
        popupPane.setTop(descriptionLabel);
        popupPane.setLeft(remove);
        popupPane.setRight(closePopUp);
        popup.getContent().addAll(popupPane);
        return popup;
    }

    private boolean confirmChanges() {
        Popup popup = new Popup();
        BorderPane aPane = new BorderPane();
        popup.setX(800);
        popup.setY(600);
        popup.setAutoHide(true);
        Label prompt = new Label("Would you like to confirm these changes?");
        Button confirm = new Button("Confirm");
        Button dismiss = new Button("Dismiss");
        aPane.setTop(prompt);
        aPane.setLeft(confirm);
        aPane.setRight(dismiss);
        confirm.setOnAction((ActionEvent event) -> {
            confirmResult = true;
            popup.hide();
        });
        dismiss.setOnAction((ActionEvent event) -> {
            confirmResult = false;
            popup.hide();
        });
        popup.getContent().add(aPane);
        popup.show(primaryStage);
        return confirmResult;
    }

    private MenuBar makeEditMenuBar(String theEditedMonster, String theEditedTreasure) {
        MenuBar menuBar = new MenuBar();
        Menu editMonster = makeEditMonsterMenu(theEditedMonster);
        Menu editTreasure = makeEditTreasureMenu(theEditedTreasure);
        menuBar.getMenus().addAll(editMonster, editTreasure);
        return menuBar;
    }

    private ListView<String> makeEditMonsterList() {
        ListView<String> editMonsterList = new ListView<String>();
        ArrayList<String> monsterArray = theController.getMonsterList(theSelectedSpace);
        for (int i = 0; i < monsterArray.size(); i++) {
            editMonsterList.getItems().add(monsterArray.get(i));
        }
        editMonsterList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        return editMonsterList;
    }

    private Menu makeEditMonsterMenu(String theEditedMonster) {
        Menu editMonster = new Menu("Add/ Remove Monster in " + theSelectedSpace);
        MenuItem addMonster = new MenuItem("Add Monster");
        addMonster.setOnAction((ActionEvent event) -> {
            boolean confirmed = confirmChanges();
            if (confirmed == true) {
                theController.addMonsterButton(theSelectedSpace);
                spaceDescription = theController.getSelectedSpaceDescription(theSelectedSpace);
                update();
            }
        });
        editMonster.getItems().add(addMonster);
        return editMonster;
    }

    private Menu makeEditTreasureMenu(String theEditedTreasure) {
        Menu editTreasure = new Menu("Add/ Remove Treasure in " + theSelectedSpace);
        MenuItem addTreasure = new MenuItem("Add Treasure");
        addTreasure.setOnAction((ActionEvent event) -> {
            boolean confirmed = confirmChanges();
            if (confirmed == true) {
                theController.addTreasureButton(theSelectedSpace);
                spaceDescription = theController.getSelectedSpaceDescription(theSelectedSpace);
                update();
            }
        });
        editTreasure.getItems().add(addTreasure);
        return editTreasure;
    }

    private ListView<String> makeEditTreasureList() {
        ListView<String> editTreasureList = new ListView<String>();
        ArrayList<String> treasureArray = theController.getTreasureList(theSelectedSpace);
        for (int j = 0; j < treasureArray.size(); j++) {
            editTreasureList.getItems().add(treasureArray.get(j));
        }
        editTreasureList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        return editTreasureList;
    }

    /*generic button creation method ensure that all buttons will have a
    similar style and means that the style only need to be in one place
     */
    private Button createButton(String text, String format) {
        Button btn = new Button();
        btn.setText(text);
        btn.setStyle("");
        return btn;
    }

  /**
   *
   * @param args for main
   */
    public static void main(String[] args) {
        launch(args);
    }
}

import boards.*;
import commands.Movement;
import commands.*;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

public class Controller implements Initializable {

    private static final List<Movement> commands = Arrays.asList(new MoveUp(), new MoveDown(), new MoveLeft(), new MoveRight());
    private static final int DEFAULT_BUTTON_SPEED = 500;
    private static final int MENU_SIZE = 30;
    private static final String HELP = "This is puzzle game, available in 2 modes: numbers or picture.\n Please select correct size before uploading image.";
    private final Stack<Movement> path = new Stack<>();
    private int size = 3;
    private int userMoves = 0;
    private int actualMoves = 0;
    private Board currentBoard;
    private GameMode currentMode = GameMode.NUMBER;

    @FXML
    private VBox wrapPane;
    @FXML
    private GridPane pane;
    @FXML
    private RadioMenuItem size3x3;
    @FXML
    private RadioMenuItem size4x4;
    @FXML
    private RadioMenuItem size5x5;
    @FXML
    private RadioMenuItem modeNumber;
    @FXML
    private RadioMenuItem modeImage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        size3x3.setSelected(true);
        modeNumber.setSelected(true);
        Board board = new NumberBoard(size);
        setupBoard(board);

        ToggleGroup sizeGroup = new ToggleGroup();
        sizeGroup.getToggles().addAll(size3x3, size4x4, size5x5);
        ToggleGroup modeGroup = new ToggleGroup();
        modeGroup.getToggles().addAll(modeNumber, modeImage);
    }

    public void setupBoard(Board board) {
        this.currentBoard = board;
        for (int i = 0; i < currentBoard.getSize(); i++) {
            for (int j = 0; j < currentBoard.getSize(); j++) {
                Button button = currentBoard.getButtons()[i][j];
                if (button != null) {
                    button.setStyle("-fx-border-color: black;");
                    button.setOnMouseClicked(this::handle);
                    pane.add(button, j, i);
                }
            }
        }
        shuffle();
        adjustSize();
    }

    private void adjustSize() {
        int height = currentBoard.getTileHeight() * size;
        int width = currentBoard.getTileWidth() * size;
        pane.setPrefHeight(height);
        pane.setPrefWidth(width);
        pane.setMinHeight(height);
        pane.setMinWidth(width);
        if (this.wrapPane.getScene() != null) {
            this.wrapPane.getScene().getRoot().resize(width, height);
            wrapPane.getScene().getWindow().sizeToScene();
        }
    }

    private void shuffle() {
        SequentialTransition sq = new SequentialTransition();
        int counter = newRandom(size * 8, size * 10);
        Movement previousStep = null;
        for (int i = 0; i < counter; i++) {
            int direction = newRandom(0, 4);
            Movement currentStep = commands.get(direction);
            if (previousStep != null && currentStep.opposite(previousStep)) {
                continue;
            }
            if (currentStep.isAllowed(currentBoard)) {
                currentStep.moveEmpty(currentBoard, 1);
                sq.getChildren().add(currentStep.getTransition());
                actualMoves++;
                path.push(currentStep.newOpposite());
                previousStep = currentStep;
            }
        }
        sq.play();
    }

    private void handle(MouseEvent e) {
        if (e.getSource() instanceof Button) {
            Button b = (Button) e.getSource();
            if (b.isDisabled()) {
                return;
            }
            b.setDisable(true);
            int i = ((int) e.getSceneY() - MENU_SIZE) / currentBoard.getTileHeight();
            int j = (int) e.getSceneX() / currentBoard.getTileWidth();
            for (Movement m : commands) {
                if (m.isAllowed(currentBoard, i, j)) {
                    m.move(currentBoard, i, j, DEFAULT_BUTTON_SPEED);
                    m.getTransition().play();
                    path.push(m.newOpposite());
                    userMoves++;
                    break;
                }
            }
            b.setDisable(false);
            if (currentBoard.isWinState()) {
                showWinDialog();
            }
        }
    }

    private void showWinDialog() {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.NONE);
        dialog.initOwner(this.wrapPane.getScene().getWindow());
        VBox dialogVbox = new VBox(20);
        Text result = new Text(String.format("Congratulations!\nYou WIN with %d clicks.\nActual moves to shuffle board - %d.", userMoves, actualMoves));
        dialogVbox.getChildren().add(result);
        Scene dialogScene = new Scene(dialogVbox, 200, 50);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    @FXML
    private void setSize(ActionEvent e) {
        if (e.getSource() instanceof RadioMenuItem) {
            RadioMenuItem source = (RadioMenuItem) e.getSource();
            String id = source.getId();
            Toggle selected = source.getToggleGroup().getSelectedToggle();
            source.getToggleGroup().getToggles().forEach(t -> t.setSelected(false));
            selected.setSelected(true);
            switch (id) {
                case "size3x3" -> this.size = 3;
                case "size4x4" -> this.size = 4;
                case "size5x5" -> this.size = 5;
            }
            generateBoard();
        }
    }

    @FXML
    private void setMode(ActionEvent e) {
        if (e.getSource() instanceof RadioMenuItem) {
            RadioMenuItem source = (RadioMenuItem) e.getSource();
            String id = source.getId();
            boolean isNumber = id.equals("modeNumber");
            modeNumber.setSelected(isNumber);
            modeImage.setSelected(!isNumber);
            this.currentMode = isNumber ? GameMode.NUMBER : GameMode.IMAGE;
            generateBoard();
        }
    }

    public void generateBoard() {
        removeNodes();
        Board board = currentMode == GameMode.NUMBER ? new NumberBoard(size) : new ImageBoard(size, null);
        setupBoard(board);
    }

    private void removeNodes() {
        if (currentBoard != null) {
            userMoves = 0;
            actualMoves = 0;
            for (int i = 0; i < currentBoard.getSize(); i++) {
                for (int j = 0; j < currentBoard.getSize(); j++) {
                    pane.getChildren().remove(currentBoard.getButtons()[i][j]);
                }
            }
        }
        path.clear();
    }

    public void exit() {
        Stage stage = (Stage) wrapPane.getScene().getWindow();
        stage.close();
    }

    public void help() {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.NONE);
        dialog.initOwner(this.wrapPane.getScene().getWindow());
        VBox dialogVbox = new VBox(100);
        Text result = new Text(HELP);
        dialogVbox.getChildren().add(result);
        Scene dialogScene = new Scene(dialogVbox, 350, 50);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public void solve() {
        SequentialTransition sq = new SequentialTransition();
        while (!path.isEmpty()) {
            Movement lastMove = path.pop();
            if (lastMove.isAllowed(currentBoard)) {
                lastMove.moveEmpty(currentBoard, DEFAULT_BUTTON_SPEED);
                sq.getChildren().add(lastMove.getTransition());
            }
        }
        sq.play();
    }

    public void uploadImage() {
        removeNodes();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filterJpg = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().addAll(filterJpg);
        File file = fileChooser.showOpenDialog(null);
        currentMode = GameMode.IMAGE;
        modeImage.setSelected(true);
        modeNumber.setSelected(false);
        Board board = new ImageBoard(size, file);
        setupBoard(board);
    }

    private int newRandom(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

}

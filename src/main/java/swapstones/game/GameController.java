package swapstones.game;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import puzzle.TwoPhaseMoveState;
import swapstones.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.tinylog.Logger;

public class GameController {

    @FXML
    private GridPane board;

    @FXML
    private TextField numberOfMovesField;

    @FXML
    private Label stopwatchLabel;

    @FXML
    private Button GiveUp;

    private final Stopwatch stopwatch = new Stopwatch();

    private PuzzleState model = new PuzzleState();

    private final IntegerProperty numberOfMoves = new SimpleIntegerProperty(0);

    private int fromChosen = -1;

    private int toChosen = -1;

    private void bindNumberOfMoves() {
        numberOfMovesField.textProperty().bind(numberOfMoves.asString());
    }

    @FXML
    private void initialize() {
        for (var j = 0; j < board.getColumnCount(); j++) {
            var square = createSquare(j);
            board.add(square, j, 0);
        }
        registerKeyEventHandler();
        bindNumberOfMoves();
        stopwatchLabel.textProperty().bind(stopwatch.hhmmssProperty());
        stopwatch.start();
    }

    private StackPane createSquare(int col) {
        var square = new StackPane();
        square.getStyleClass().add("square");
        var piece = new Circle(12);
        var color = getColor(model.getStone(col));
        piece.setFill(color);
        square.getChildren().add(piece);
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        var source = (Node) event.getSource();
        var col = GridPane.getColumnIndex(source);
        Logger.debug("Click on square ({})", col);

        if (fromChosen == -1) {
            fromChosen = col;
            Logger.debug("Selected square to move from: {}", fromChosen);
        } else {
            toChosen = col;
            Logger.debug("Selected square to move to: {}", toChosen);

            makeMoveIfLegal(fromChosen, toChosen);

            fromChosen = -1;
            toChosen = -1;
        }
    }

    private void registerKeyEventHandler() {
        Platform.runLater(() -> board.getScene().setOnKeyPressed(this::handleKeyPress));
    }
    private void restartGame() {
        createState();
        numberOfMoves.set(0);
        stopwatch.reset();
        clearAndPopulateGrid();
    }

    private void clearAndPopulateGrid() {
        board.getChildren().clear();
        for (var row = 0; row < board.getRowCount(); row++) {
            for (var col = 0; col < board.getColumnCount(); col++) {
                var square = createSquare(col);
                board.add(square, col, row);
            }
        }
    }

    private void createState() {
        model = new PuzzleState();
        initialize();
    }

    @FXML
    private void handleKeyPress(KeyEvent keyEvent) {
        var restartKeyCombination = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN);
        var quitKeyCombination = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);
        if (restartKeyCombination.match(keyEvent)) {
            Logger.debug("Restarting game");
            restartGame();
        } else if (quitKeyCombination.match(keyEvent)) {
            Logger.debug("Exiting");
            Platform.exit();
        }
    }

    private void makeMoveIfLegal(int fromIndex, int toIndex) {
        TwoPhaseMoveState.TwoPhaseMove<Integer> move = new TwoPhaseMoveState.TwoPhaseMove<>(fromIndex, toIndex);
        if (model.isLegalMove(move)) {
            model.makeMove(move);
            updateUI();
            numberOfMoves.set(numberOfMoves.get() + 1);

            if (model.isSolved()) {
                showSolvedAlertAndExit();
            }
        } else {
            Logger.warn("Attempted illegal move: {}", move);
        }
    }

    private void updateUI() {
        for (var j = 0; j < board.getColumnCount(); j++) {
            updateSquare(0, j);
        }
    }

    private void updateSquare(int row, int col) {
        var piece = (Circle) ((StackPane) getNodeFromGrid(row, col)).getChildren().get(0);
        var color = getColor(model.getStone(col));
        piece.setFill(color);
    }

    private Color getColor(Stone stone) {
        return switch (stone) {
            case EMPTY -> Color.TRANSPARENT;
            case HEAD -> Color.BLACK;
            case TAIL -> Color.RED;
        };
    }

    public Node getNodeFromGrid(int row, int col) {
        for (var square : board.getChildren()) {
            if (GridPane.getRowIndex(square) == row && GridPane.getColumnIndex(square) == col) {
                return square;
            }
        }
        throw new AssertionError();
    }

    @FXML
    public void handleGiveUp(ActionEvent event) {
        Platform.exit();
    }

    private void showSolvedAlertAndExit() {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        stopwatch.stop();
        alert.setHeaderText("Puzzle is solved");
        alert.setContentText("Congratulations! You solved the puzzle");
        alert.showAndWait();
        Platform.exit();
    }
}

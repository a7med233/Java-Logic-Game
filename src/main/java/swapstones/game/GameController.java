package swapstones.game;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.tinylog.Logger;
import puzzle.TwoPhaseMoveState;
import swapstones.model.PuzzleState;
import swapstones.model.Stone;

public class GameController {
    @FXML
    private GridPane board;

    @FXML
    private TextField numberOfMovesField;

    private final PuzzleState model = new PuzzleState();

    private final IntegerProperty numberOfMoves = new SimpleIntegerProperty(0);

    @FXML
    private void initialize() {

        for (var j = 0; j < board.getColumnCount(); j++) {
            var square = createSquare(j);
            board.add(square,j, 0);
        }
        bindNumberOfMoves();
    }

    private void bindNumberOfMoves() {
        numberOfMovesField.textProperty().bind(numberOfMoves.asString());
    }


    private StackPane createSquare(int col) {
        var square = new StackPane();
        square.getStyleClass().add("square");
        var piece = new Circle(12);
        var color = getColor(model.getStone(col));
        piece.setFill(color);
        square.getChildren().add(piece);
        return square;
    }


    private Color getColor(Stone square) {
        return switch (square) {
            case EMPTY -> Color.TRANSPARENT;
            case HEAD -> Color.BLACK;
            case TAIL -> Color.RED;
        };
    }
}

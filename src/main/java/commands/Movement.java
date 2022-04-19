package commands;

import boards.Board;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;

public abstract class Movement {
    protected TranslateTransition transition;

    protected boolean areIndexesCorrect(int i, int j, int size) {
        return i < size && i >= 0 && j < size && j >= 0;
    }

    public abstract boolean isAllowed(Board board, int i, int j);

    public abstract boolean isAllowed(Board board);

    public abstract boolean opposite(Movement move);

    public abstract Movement newOpposite();

    public abstract void moveEmpty(Board board, int speed);

    public void move(Board board, int i, int j, int speed) {
        Button b = board.getButtons()[i][j];
        transition = new TranslateTransition(Duration.millis(speed), b);
        board.getButtons()[board.getEmptyCellI()][board.getEmptyCellJ()] = board.getButtons()[i][j];
        board.getButtons()[i][j] = null;
    }

    public TranslateTransition getTransition() {
        return transition;
    }

}

package boards;

import javafx.scene.control.Button;

public abstract class Board {

    protected int size;
    protected Button[][] board;
    protected Integer[][] expectedBoard;
    protected int tileHeight;
    protected int tileWidth;
    private int emptyCellI;
    private int emptyCellJ;

    public Board(int size) {
        this.size = size;
        this.board = new Button[size][size];
        this.expectedBoard = new Integer[size][size];
        emptyCellI = size - 1;
        emptyCellJ = size - 1;
    }

    public int getSize() {
        return size;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public Button[][] getButtons() {
        return board;
    }

    public int getEmptyCellJ() {
        return emptyCellJ;
    }

    public int getEmptyCellI() {
        return emptyCellI;
    }

    public void moveEmptyDown() {
        emptyCellI++;
    }

    public void moveEmptyLeft() {
        emptyCellJ--;
    }

    public void moveEmptyUp() {
        emptyCellI--;
    }

    public void moveEmptyRight() {
        emptyCellJ++;
    }
}

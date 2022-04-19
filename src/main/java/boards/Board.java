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

    public boolean isWinState() {
        int lastIndex = size - 1;
        if (getEmptyCellI() != lastIndex || getEmptyCellJ() != lastIndex) {
            return false;
        }
        int value = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Button button = board[i][j];
                if (button != null) {
                    int buttonValue = Integer.parseInt(button.getId());
                    if (buttonValue != value) {
                        return false;
                    }
                }
                value++;
            }
        }
        return true;
    }

}

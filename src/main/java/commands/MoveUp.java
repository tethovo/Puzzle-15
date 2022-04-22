package commands;

import boards.Board;

public class MoveUp extends Movement {

    @Override
    public boolean isAllowed(Board board, int i, int j) {
        return areIndexesCorrect(i, j, board.getSize()) && board.emptyJ() == j && i - 1 == board.emptyI();
    }

    @Override
    public boolean isAllowed(Board board) {
        return isAllowed(board, board.emptyI() + 1, board.emptyJ());
    }

    @Override
    public boolean opposite(Movement move) {
        return move instanceof MoveDown;
    }

    @Override
    public Movement newOpposite() {
        return new MoveDown();
    }

    @Override
    public void move(Board board, int i, int j, int speed) {
        super.move(board, i, j, speed);
        transition.setByY(-board.getTileHeight());
        board.moveEmptyDown();
    }

    @Override
    public void moveEmpty(Board board, int speed) {
        move(board, board.emptyI() + 1, board.emptyJ(), speed);
    }
}

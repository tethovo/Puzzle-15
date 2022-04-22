package commands;

import boards.Board;

public class MoveLeft extends Movement {

    @Override
    public boolean isAllowed(Board board, int i, int j) {
        return areIndexesCorrect(i, j, board.getSize()) && board.emptyI() == i && j - 1 == board.emptyJ();
    }

    @Override
    public boolean isAllowed(Board board) {
        return isAllowed(board, board.emptyI(), board.emptyJ() + 1);
    }

    @Override
    public boolean opposite(Movement move) {
        return move instanceof MoveRight;
    }

    @Override
    public Movement newOpposite() {
        return new MoveRight();
    }

    @Override
    public void move(Board board, int i, int j, int speed) {
        super.move(board, i, j, speed);
        transition.setByX(-board.getTileWidth());
        board.moveEmptyRight();
    }

    @Override
    public void moveEmpty(Board board, int speed) {
        move(board, board.emptyI(), board.emptyJ() + 1, speed);
    }
}

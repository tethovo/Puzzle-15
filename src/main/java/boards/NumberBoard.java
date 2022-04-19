package boards;

import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class NumberBoard extends Board {

    private static final int TILE_SIZE = 150;

    public NumberBoard(int size) {
        super(size);
        tileWidth = TILE_SIZE;
        tileHeight = TILE_SIZE;
        int index = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (j == size - 1 && i == size - 1) {
                    break;
                } else {
                    Button rectangle = new Button();
                    Font font = Font.font("SanSerif", FontWeight.BOLD, 36);
                    rectangle.setFont(font);
                    rectangle.setMaxHeight(tileHeight);
                    rectangle.setMaxWidth(tileWidth);
                    rectangle.setPrefWidth(tileWidth);
                    rectangle.setPrefHeight(tileHeight);
                    String id = String.format("%d", index);
                    rectangle.setId(id);
                    rectangle.setText(String.valueOf(index));
                    this.board[i][j] = rectangle;
                    expectedBoard[i][j] = index++;
                }
            }
        }
    }
}

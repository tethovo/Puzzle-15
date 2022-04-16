package boards;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageBoard extends Board {

    private final String FOLDER = "/pictures/";
    private final String DEFAULT_IMAGE = "flowers.jpeg";
    private final int BORDER_WIDTH = 2;

    public ImageBoard(int size, File file) {
        super(size);
        final BufferedImage source;
        try {
            source = ImageIO.read(file == null ? new File(getClass().getResource(FOLDER + DEFAULT_IMAGE).getPath()) : file);
            int index = 1;
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            this.tileWidth = Math.min(source.getWidth(), gd.getDisplayMode().getWidth()) / size;
            this.tileHeight = Math.min(source.getHeight(), gd.getDisplayMode().getHeight()) / size;

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (j == size - 1 && i == size - 1) {
                        break;
                    } else {
                        BufferedImage subimage = source.getSubimage(j * tileWidth, i * tileHeight, tileWidth, tileHeight);
                        String name = getClass().getResource(FOLDER).getPath()  + "part" + i + j;
                        ImageIO.write(subimage, "jpeg", new File(name));
                        FileInputStream input = new FileInputStream(name);

                        Image image = new Image(input);
                        ImageView imageView = new ImageView(image);
                        imageView.setPreserveRatio(true);
                        imageView.setFitHeight(tileHeight);
                        imageView.setFitWidth(tileWidth);
                        Button button = new Button();
                        button.setGraphic(imageView);
                        button.setPadding(Insets.EMPTY);
                        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                        button.setPrefSize(tileWidth, tileHeight);
                        String id = String.format("%d", index);
                        button.setId(id);
                        this.board[i][j] = button;
                        expectedBoard[i][j] = index++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getTileHeight() {
        return tileHeight + BORDER_WIDTH;
    }

    @Override
    public int getTileWidth() {
        return tileWidth + BORDER_WIDTH;
    }
}

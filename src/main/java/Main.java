import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application{

    private FXMLLoader fxmlLoader;

    @Override
    public void init() {
        fxmlLoader = new FXMLLoader();
    }

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        fxmlLoader.setLocation(getClass().getResource("/sample.fxml"));

        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Puzzle Game");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
    }
}

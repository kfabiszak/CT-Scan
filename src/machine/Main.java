package machine;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Parent root;
    private static Controller controller = null;

    public static void load() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("main.fxml"));
        root = loader.load();
        controller = loader.getController();
    }

    public static Controller getController() {
        return controller;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        load();
        primaryStage.setTitle("CT Scan Simulator");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception{
        super.stop();
        controller.destroy();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

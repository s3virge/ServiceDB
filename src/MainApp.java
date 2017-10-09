/**
 * Created by s3virge on 04.10.17.
 *  взято отсюда http://docs.oracle.com/javafx/2/get_started/fxml_tutorial.htm
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static void main(String[] args) {

        //we need to connect to database

        //launch GUI
        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {
        Parent rootLayout = FXMLLoader.load(getClass().getResource("LoginWnd/LoginWnd.fxml"));

        Scene scene = new Scene(rootLayout, 400, 250);
        window.setTitle("A simple database of the service center");
        window.setScene(scene);
        window.show();
    }
}

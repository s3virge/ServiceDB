/**
 * Created by s3virge on 04.10.17.
 *  взято отсюда http://docs.oracle.com/javafx/2/get_started/fxml_tutorial.htm
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

public class MainApp extends Application {

    private static final Logger logger = Logger.getLogger(MainApp.class);
    private String sceneFile = null;
    private Parent layout = null;
    private Stage window;

    public static void main(String[] args) {
        //launch GUI
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        logger.debug("start(Stage window) is executed!");

        /* после подключениея Maven FXMLLoader нашел файл сцены только после создания паки Resouces и перемещения папки
        * LoginWindow туда, в pom.xml указана папка ресурсов*/
        showLoginWindow();
    }

    private void showLoginWindow() {
        sceneFile = "LoginWindow/LoginWnd.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader();

        try {
            fxmlLoader.setLocation(getClass().getResource(sceneFile));
            layout = fxmlLoader.load();
        }
        catch ( Exception ex ) {
            System.out.println( "Exception on FXMLLoader.load()" );
            System.out.println( "error - " + ex.getMessage() );   //-- Doesn't show in stack dump
            System.out.println( "    ----------------------------------------\n" );
        }

        // Give the controller access to the main app.
        LoginWndController controller = fxmlLoader.getController();
        controller.setMainApp(this);

        //показываем окно ввода логина и пароля
        Scene scene = new Scene(layout, 400, 250);
        window.setTitle("A simple database of the service center");
        window.setScene(scene);
        window.show();
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return window;
    }
}

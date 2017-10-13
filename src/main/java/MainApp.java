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

    public static void main(String[] args) {

        //we need to connect to database

        //launch GUI
        launch(args);
    }

    @Override
    public void start(Stage window) {

        String sceneFile = "LoginWindow/LoginWnd.fxml";
        Parent rootLayout = null;

        if (logger.isDebugEnabled()) {
            logger.debug("start(Stage window) is executed!");
        }

        /* после подключениея Maven FXMLLoader нашел файл сцены только после создания паки Resouces и перемещения папки
        * LoginWindow туда, в pom.xml указана папка ресурсов*/
        try {
            rootLayout = FXMLLoader.load(getClass().getResource(sceneFile));
            logger.debug("  fxmlResource = " + sceneFile );
        }
        catch ( Exception ex ) {
            System.out.println( "Exception on FXMLLoader.load()" );
            System.out.println( "error - " + ex.getMessage() );   //-- Doesn't show in stack dump
            System.out.println( "    ----------------------------------------\n" );
        }

        Scene scene = new Scene(rootLayout, 400, 250);
        window.setTitle("A simple database of the service center");
        window.setScene(scene);
        window.show();
    }
}

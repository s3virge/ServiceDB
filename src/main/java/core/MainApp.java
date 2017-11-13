package core; /**
 * Created by s3virge on 04.10.17.
 *  взято отсюда http://docs.oracle.com/javafx/2/get_started/fxml_tutorial.htm
 */

import core.controllers.LoginWndController;
import core.utils.DataBase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

public class MainApp extends Application {

    private static final Logger logger = Logger.getLogger(MainApp.class);

    private Stage loginWindow;

    public static void main(String[] args) {
        //launch GUI
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.debug("start(Stage loginWindow) is executed!");

        loginWindow = primaryStage;

        /* после подключениея Maven FXMLLoader нашел файл сцены только после создания паки Resouces и перемещения папки
        * LoginWindow туда, в pom.xml указана папка ресурсов*/

        //показываем окно ввода логина и пароля.
       showLoginWindow();
    }

    private void showLoginWindow() {
        logger.debug("execute showLoginWindow()");

        //если приложение запускается впервые, то базы данных нет
        DataBase db = new DataBase();

        if(!db.isExist()){
            db.createDB();
        }

        Parent layout = null;
        String sceneFile = "/LoginWindow/LoginWnd.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader();

        try {
            fxmlLoader.setLocation(getClass().getResource(sceneFile));
            layout = fxmlLoader.load();
        }
        catch ( Exception ex ) {
            logger.error(ex);
        }

        // Give the controller access to the main app.
        LoginWndController loginWndController = fxmlLoader.getController();
        //теперь контроллер может вызвать getPrimaryStage()
        loginWndController.setMainApp(this);

        //показываем окно ввода логина и пароля
        Scene scene = new Scene(layout, 360, 220);
        loginWindow.setTitle("A simple database of the service center");
        loginWindow.setResizable(false);
        loginWindow.setScene(scene);
        loginWindow.show();
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return loginWindow;
    }
}

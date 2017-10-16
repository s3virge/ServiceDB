import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

public class LoginWndController {
    private static final Logger logger = Logger.getLogger(LoginWndController.class);
    private Stage mainWindow;

    @FXML private Text errorLabel;
    @FXML private TextField loginField;
    @FXML private TextField passwordField;

    // Reference to the main application
    private MainApp mainApp;

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        if(isInputValid()){
            //соединиться с базой данных
            //найти логин пользователя
            //если такого логина нет
            //показать сообщение с ошибкой - ТАкой комбинации логина и пароля не существует

            //главное окно для разных групп пользователь буде отображаться по разному.
            //нужно получить из контроллера группу пользователя который залогинился.
            UserGroup userGroupLoggedIn = UserGroup.Employee;

            switch (userGroupLoggedIn) {
                case Administrator:
                case Manager:
                case Employee:
                    showMainWnd();
            }

            // Show the error message.
            showErrorMsg("Такой комбинации логина и пароля\nне существует.");
            loginField.requestFocus();
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    private boolean isInputValid() {

        if (loginField.getText() == null || loginField.getText().length() == 0) {
            errorLabel.setText("Login can not be empty!");
            loginField.requestFocus();
            return false;
        }

        if (passwordField.getText() == null || passwordField.getText().length() == 0) {
            errorLabel.setText("Password can not be empty!");
            passwordField.requestFocus();
            return false;
        }

        errorLabel.setText("");
        return true;
    }

    private void showErrorMsg(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("Что-то пошло не так.");
        alert.setHeaderText(null);
//        alert.setHeaderText("Please correct invalid fields");
        alert.setContentText(message);

        alert.showAndWait();
    }

    private void showMainWnd() {
        mainWindow = mainApp.getPrimaryStage();
        logger.debug("execute showMainWnd()");

        Parent layout = null;
        String sceneFile = "MainWindow/MainWnd.fxml";
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

        //показываем окно ввода логина и пароля
        Scene scene = new Scene(layout);
        mainWindow.setTitle("A simple database of the service center");
        mainWindow.setScene(scene);
        //mainWindow.setFullScreen(true);
        mainWindow.show();
    }
}
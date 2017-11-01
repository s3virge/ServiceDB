package core;

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
            String login = loginField.getText();
            String paswd = passwordField.getText();

            DataBase dataBase = new DataBase();
            User user = dataBase.getUser(login);

            //если юзера нет
            if (user.isEmpty()){
                showLoginError();
                return;
            }

            if (user.getPassword().equals(paswd)) {
                //главное окно для разных групп пользователь буде отображаться по разному.
                //нужно получить из контроллера группу пользователя который залогинился.
                UserGroup userGroupLoggedIn = UserGroup.Employee;

                switch (userGroupLoggedIn) {
                    case Administrator:
                    case Manager:
                    case Employee:
                        showMainWnd();
                }
            }
            else{
                //показать сообщение с ошибкой
                showLoginError();
                return;
            }
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

    private void showMainWnd() {
        mainWindow = mainApp.getPrimaryStage();
        logger.debug("execute showMainWnd()");

        Parent layout = null;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/MainWindow/MainWnd.fxml"));

        try {
            layout = fxmlLoader.load();
        }
        catch ( Exception ex ) {
            System.out.println( "Exception on FXMLLoader.load()" );
            System.out.println( "error - " + ex.getMessage() );   //-- Doesn't show in stack dump
            System.out.println( "    ----------------------------------------\n" );
        }

        Scene scene = new Scene(layout);
        mainWindow.setTitle("A simple database of the service center");
        mainWindow.setScene(scene);
        mainWindow.centerOnScreen();
        //mainWindow.setFullScreen(true);
        mainWindow.show();
    }

    private void showLoginError(){
        MsgBox.show("Такой комбинации логина и пароля не существует.", MsgBox.Type.MB_ERROR);
        loginField.requestFocus();
        passwordField.setText("");
    }
}
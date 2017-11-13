package core.controllers;

import core.utils.DataBase;
import core.MainApp;
import core.models.User;
import core.models.UserGroup;
import core.utils.MD5Hash;
import core.utils.MsgBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

public class LoginWndController {
    private static final Logger logger = Logger.getLogger(LoginWndController.class);

    @FXML private Text errorLabel;
    @FXML private TextField loginField;
    @FXML private TextField passwordField;

    // Reference to the main application
    private MainApp mainApp;

    /**
     * Инициализирует класс-контроллер. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() {
        setDefaultTextFieldValue();
    }

    private void setDefaultTextFieldValue() {
        loginField.setText("admin");
        passwordField.setText("admin");
    }

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {

        if(isInputValid()) {

            String login = loginField.getText();
            String paswd = null;

            try {
                paswd = MD5Hash.get(passwordField.getText());
            }
            catch (Exception e){
                MsgBox.show(e.getMessage(), MsgBox.Type.MB_ERROR);
            }

            DataBase dataBase = new DataBase();
            User user = dataBase.getUser(login);

            //если юзера нет
            if (user.isEmpty()) {
                showLoginError();
                return;
            }

            if (user.getPassword().equals(paswd)) {
                //главное окно для разных групп пользователь буде отображаться по разному.
                //нужно знать группу пользователя который залогинился.

                switch (user.getGroup()) {
                    case "administrator":
                        showMainWnd();
                }
            }
            else {
                //показать сообщение с ошибкой
                showLoginError();
                return;
            }
        }
    }

    /**
     * Получить доступ к главному классу приложения
    */
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
        logger.debug("execute showMainWnd()");

        Stage mainWindow = mainApp.getPrimaryStage();

        Parent layout = null;
        //Поскольку имя начинается с символа '/' – оно считается абсолютным. Без / - считается относительным
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/MainWindow/MainWindow.fxml"));

        try {
            layout = fxmlLoader.load();
        }
        catch ( Exception ex ) {
            System.out.println( "Exception on FXMLLoader.load()" );
            System.out.println( "error - " + ex.getMessage() );   //-- Doesn't show in stack dump
        }

        // Give the controller access to the main app.
        MainWndController mainWndController = fxmlLoader.getController();
        mainWndController.setMainApp(mainApp);

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
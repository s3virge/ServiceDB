import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import jdk.internal.util.xml.impl.Input;

public class LoginWndController {
    @FXML private Text errorMessage;
    @FXML private TextField loginField;
    @FXML private TextField passwordField;

    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        if(isInputValid()){
            //соединиться с базой данных
            //найти логин пользователя
            //если такого логина нет
            //показать сообщение с ошибкой - Такого пользователя не существует

        // Show the error message.
        Alert alert = new Alert(Alert.AlertType.ERROR);
        //alert.initOwner();
        alert.setTitle("Invalid Fields");
        alert.setHeaderText("Please correct invalid fields");
        alert.setContentText("Тут буде сообщение об ошибке.");

        alert.showAndWait();

        }
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {

        if (loginField.getText() == null || loginField.getText().length() == 0) {
            errorMessage.setText("Login can not be empty!");
            loginField.requestFocus();
            return false;
        }

        if (passwordField.getText() == null || passwordField.getText().length() == 0) {
            errorMessage.setText("Password can not be empty!");
            passwordField.requestFocus();
            return false;
        }

        return true;
    }
}
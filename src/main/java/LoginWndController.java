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
        isInputValid();
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {

        if (loginField.getText() == null || loginField.getText().length() == 0) {
            errorMessage.setText("No valid login!");
            loginField.requestFocus();

            return false;
        }

        if (passwordField.getText() == null || passwordField.getText().length() == 0) {
            errorMessage.setText("No valid password!");
            passwordField.requestFocus();
            return false;
        }

//        // Show the error message.
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.initOwner();
//        alert.setTitle("Invalid Fields");
//        alert.setHeaderText("Please correct invalid fields");
//        alert.setContentText(errorMessage);
//
//        alert.showAndWait();

        return false;
    }
}
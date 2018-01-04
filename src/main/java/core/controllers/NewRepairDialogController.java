package core.controllers;

import core.utils.AutoCompleteTextField;
import core.utils.MsgBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Arrays;

/**
 * Окно для изменения информации об адресате.
 *
 * @author Marco Jakob
 */
public class NewRepairDialogController {

    public Button btnFirst;
    public Button btnSecond;
    public Button btnThird;

    public AutoCompleteTextField tfDeviceType;
    public AutoCompleteTextField tfBrand;

    /**
     * Инициализирует класс-контроллер. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() {
        //получить из базы данных список производителей
        tfBrand.getEntries().addAll(Arrays.asList("Asus", "Acer", "Dell", "HP", "EMashines", "Fujitsu-Siemens"));
        tfBrand.setPromptText("Введите название брэнда");
    }

    /**
     * Проверяет пользовательский ввод в текстовых полях.
     *
     * @return true, если пользовательский ввод корректен
     */
    /*private boolean isInputValid() {
        String errorMessage = "";

        if (firstNameField.getText() == null || firstNameField.getText().length() == 0) {
            errorMessage += "No valid first name!\n";
        }
        if (lastNameField.getText() == null || lastNameField.getText().length() == 0) {
            errorMessage += "No valid last name!\n";
        }
        if (streetField.getText() == null || streetField.getText().length() == 0) {
            errorMessage += "No valid street!\n";
        }

        if (postalCodeField.getText() == null || postalCodeField.getText().length() == 0) {
            errorMessage += "No valid postal code!\n";
        }
        else {
            // пытаемся преобразовать почтовый код в int.
            try {
                Integer.parseInt(postalCodeField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid postal code (must be an integer)!\n";
            }
        }

        if (cityField.getText() == null || cityField.getText().length() == 0) {
            errorMessage += "No valid city!\n";
        }

        if (birthdayField.getText() == null || birthdayField.getText().length() == 0) {
            errorMessage += "No valid birthday!\n";
        }
        else {

        }

        if (errorMessage.length() == 0) {
            return true;
        }
        else {
            // Показываем сообщение об ошибке.
            return false;
        }
    }*/

    public void onBtnActions(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if(!(source instanceof Button)){
            return;
        }

        Button clickedButton = (Button) source;

        switch(clickedButton.getId()){
            case "btnFirst":
                MsgBox.show("Нажали на первую педаль", MsgBox.Type.MB_INFO);
                break;

            case "btnSecond":
                MsgBox.show("Нажали на вторую педаль", MsgBox.Type.MB_INFO);
                break;

            case "btnThird":
                MsgBox.show("Нажали на третюю педаль", MsgBox.Type.MB_INFO);
                break;
        }
    }

    @FXML
    /*закрываем диалог когда нажали кнопку ок или cancel*/
    private void closeDlg(ActionEvent actionEvent){
        //получить источник события
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
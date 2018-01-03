package core.controllers;

import core.utils.AutoCompleteComboBox;
import core.utils.AutoCompleteComboBoxListener;
import core.utils.AutoCompleteTextField;
import core.utils.MsgBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
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
    public TextField tfDeviceType;
    public AutoCompleteTextField cbBrand;

    /**
     * Инициализирует класс-контроллер. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() {
        /*if (dialogStage == null){
            MsgBox.show("dialogStage stage is empty", MsgBox.Type.MB_ERROR);
            System.exit(0);
        }*/
        cbBrand.getEntries().addAll(Arrays.asList("Asus", "Acer", "Dell", "HP", "EMashines", "Fujitsu-Siemens"));
        cbBrand.setPromptText("введите название брэнда");
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

    /**
     *
     * @param
     */
    /*public void setMainStage(Stage majorStage){
        dialogStage = majorStage;
    }*/

    public void onBtnActions(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if(!(source instanceof Button)){
            return;
        }

        Button clickedButton = (Button) source;

        switch(clickedButton.getId()){
            case "btnFirst":
                MsgBox.show("Нажали на первую педальё", MsgBox.Type.MB_INFO);
                break;

            case "btnSecond":
                MsgBox.show("Нажали на вторую педаль", MsgBox.Type.MB_INFO);
                break;

            case "btnThird":
                MsgBox.show("Нажали на третюю педаль", MsgBox.Type.MB_INFO);
                break;
        }
    }

    /**
     * Вызывается, когда пользователь кликнул по кнопке OK.
     */
    @FXML
    private void handleOk(ActionEvent actionEvent) {
        //получить источник события
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     * Вызывается, когда пользователь кликнул по кнопке Cancel.
     */
    @FXML
    private void handleCancel(ActionEvent actionEvent) {
        //получить источник события
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void handleTextFieldDeviceTypeKeyPress(KeyEvent keyEvent) {
        /*if (keyEvent.getCode() == KeyCode.ENTER) {
            cbBrand.setEditable(true);
        }*/
    }
}
package core.controllers;

//import core.utils.AutoCompleteTextField;
import core.utils.AutoCompleteTextField;
import core.utils.MsgBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Окно для изменения информации об адресате.
 *
 * @author Marco Jakob
 */
public class NewRepairDialogController {

    @FXML private Button btnAddDeviceType;
    @FXML private Button btnAddBrand;
    @FXML private Button btnAddModel;
    @FXML private Button btnAddcCmpleteness;
    @FXML private Button btnAddAppearance;
    @FXML private Button btnAddMalfunction;
    @FXML private Button btnFirst;
    @FXML private Button btnSecond;
    @FXML private Button btnThird;

   @FXML private AutoCompleteTextField tfDeviceType;
    @FXML private AutoCompleteTextField tfBrand;

    /**
     * Инициализирует класс-контроллер. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() {
        //получить из базы данных список производителей
        //tfBrand.getEntries().addAll(Arrays.asList("Asus", "Acer", "Dell", "HP", "EMashines", "Fujitsu-Siemens"));
        //tfBrand.setPromptText("Введите название брэнда");
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

    @FXML
    private void onBtnActions(ActionEvent actionEvent) {
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

    @FXML
    private void onBtnAdd(ActionEvent actionEvent) {
        Button clickedBtn = (Button) actionEvent.getSource();

        switch(clickedBtn.getId()){
            case "btnAddDeviceType":
                AddDeviceType();
            break;
        }
    }

    private void AddDeviceType() {
        //получить введенный текст из textfield
        String strDeviceType = tfDeviceType.getText();
        //если ничего не введено
        if (strDeviceType.isEmpty()) {
            MsgBox.show("Для начала нужно что-то написать в поле ввода", MsgBox.Type.MB_ERROR);
            tfDeviceType.requestFocus();
            return;
        }

        //цифры, символы, пробел нельзя, только буквы
        if (!isOnlyLetters(strDeviceType)) {
            MsgBox.show("Можно вводить только буквы.", MsgBox.Type.MB_ERROR);
            tfDeviceType.requestFocus();
            return;
        }

        //Сделать первую букву заглавной, а остальные прописными
        char firstChar = Character.toUpperCase(strDeviceType.charAt(0));
        String firstUpperLetter = String.valueOf(firstChar);
        //String characterToString = Character.toString('c');
        firstUpperLetter += strDeviceType.substring(1);

        System.out.println(firstUpperLetter);

        //если такая запись в базе уже есть
        //Сказать что запись есть и дубликатов быть не может
        //сделать запись в таблицу базы данных
    }

    private boolean isOnlyLetters(String strToVerification) {
        return strToVerification.matches("[a-zA-Zа-яА-Я]+");
    }
}
package core.controllers;

//import core.utils.AutoSuggestTextField;
import core.utils.AutoSuggestTextField;
import core.utils.DataBase;
import core.utils.MsgBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Окно для изменения информации об адресате.
 *
 * @author Marco Jakob
 */
public class NewRepairDialogController {

    /*@FXML private Button btnAddDeviceType;
    @FXML private Button btnAddBrand;
    @FXML private Button btnAddModel;
    @FXML private Button btnAddCompleteness;
    @FXML private Button btnAddAppearance;
    @FXML private Button btnAddDefect;
    @FXML private Button btnFirst;
    @FXML private Button btnSecond;
    @FXML private Button btnThird;*/

    @FXML private AutoSuggestTextField tfDeviceType;
    @FXML private AutoSuggestTextField tfBrand;
    @FXML private AutoSuggestTextField tfModel;
    @FXML private TextField tfSerialNumber;
    @FXML private AutoSuggestTextField tfCompleteness;
    @FXML private AutoSuggestTextField tfAppearance;
    @FXML private AutoSuggestTextField tfDefect;
    @FXML private TextField tfNote;
    @FXML private TextField tfOwner;
    @FXML private TextField tfPhone;
    @FXML private TextField tfEmail;

    /**
     * Инициализирует класс-контроллер. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() {
        //получить из базы данных список производителей
        getEntriesFromDataBase(tfDeviceType);
        tfBrand.getEntries().addAll(Arrays.asList("Asus", "Acer", "Dell", "HP", "EMashines", "Fujitsu-Siemens"));
        //tfBrand.setPromptText("Введите название брэнда");
    }

    private void getEntriesFromDataBase(AutoSuggestTextField autoTextFiels) {

    }

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
                recordToDataBase(tfDeviceType, "devicetype");
            break;

            case "btnAddBrand":
                recordToDataBase(tfBrand, "brand");
                break;

            case "btnAddModel":
                recordToDataBase(tfModel, "devicemodel");
                break;

            case "btnAddCompleteness":
                recordToDataBase(tfCompleteness, "completeness");
                break;

            case "btnAddAppearance":
                recordToDataBase(tfAppearance, "appearance");
                break;

            case "btnAddDefect":
                recordToDataBase(tfModel, "defect");
                break;
        }
    }

    private void recordToDataBase(AutoSuggestTextField textField, String strDBTable ) {
        //получить введенный текст из textfield
        String strTfText = textField.getText();
        //если ничего не введено
        if (strTfText.isEmpty()) {
            MsgBox.show("Для начала нужно что-то написать в поле ввода", MsgBox.Type.MB_ERROR);
            textField.requestFocus();
            return;
        }

        //цифры, символы, пробел нельзя, только буквы
        if (!isOnlyLetters(strTfText)) {
            MsgBox.show("Можно вводить только буквы.", MsgBox.Type.MB_ERROR);
            textField.requestFocus();
            return;
        }

        //Сделать первую букву заглавной, а остальные прописными
        String strDbValue = makeFirstLetterBig(strTfText);

        //если такая запись в базе уже есть
        //Сказать что запись есть и дубликатов быть не может

        //сделать запись в таблицу базы данных
        makeDataBaseRecord(strDBTable, strDbValue);

        //если в базу все записалось
        textField.setText(strDbValue);
        //то вставить в поле ввода новую строку с большой первой буквой
        //передать фокус ввода следующему полю ввода (программно нажать Tab кнопку)
    }

    private boolean isOnlyLetters(String strToVerification) {
        return strToVerification.matches("[a-zA-Zа-яА-Я]+");
        //+ значит один и более символов
    }

    //make the first letter big а остальные маленькие
    private String makeFirstLetterBig(String strToMod){
        char firstChar = Character.toUpperCase(strToMod.charAt(0));
        String strReuslt = String.valueOf(firstChar);
        //String characterToString = Character.toString('c');
        return strReuslt += strToMod.substring(1).toLowerCase();
    }

    private void makeDataBaseRecord(String strTable, String strValue) {
        DataBase dataBase = new DataBase();

        //String sql = "INSERT INTO devicetype (value) VALUE ('Планшет')";
        String sql = "INSERT INTO " + strTable + " (value) VALUE ('" + strValue + "')";
        //String sql = "INSERT INTO devicetype (value) VALUE (?)";

        try {
            PreparedStatement statement = dataBase.connect().prepareStatement(sql);
            //statement.setString(1, strValue);

            statement.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            dataBase.disconnect();
        }
    }
}
package core.controllers;

//import core.utils.AutoCompleteTextField;
import core.utils.AutoCompleteTextField;
import core.utils.DataBase;
import core.utils.MsgBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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
        String strDeviceTypeText = tfDeviceType.getText();
        //если ничего не введено
        if (strDeviceTypeText.isEmpty()) {
            MsgBox.show("Для начала нужно что-то написать в поле ввода", MsgBox.Type.MB_ERROR);
            tfDeviceType.requestFocus();
            return;
        }

        //цифры, символы, пробел нельзя, только буквы
        if (!isOnlyLetters(strDeviceTypeText)) {
            MsgBox.show("Можно вводить только буквы.", MsgBox.Type.MB_ERROR);
            tfDeviceType.requestFocus();
            return;
        }

        //Сделать первую букву заглавной, а остальные прописными
        String strDeviceType = makeFirstLetterBig(strDeviceTypeText);

        //если такая запись в базе уже есть
        //Сказать что запись есть и дубликатов быть не может

        //сделать запись в таблицу базы данных
        makeDataBaseRecord(strDeviceType);

        //если в базу все записалось
        tfDeviceType.setText(strDeviceType);
        //то вставить в поле ввода новую строку с большой первой буквой
        //передать фокус ввода следующему полю ввода (программно нажать Tab кнопку)
    }

    private boolean isOnlyLetters(String strToVerification) {
        return strToVerification.matches("[a-zA-Zа-яА-Я]+");
        //+ значит один и более символов
    }

    //make the first letter big
    private String makeFirstLetterBig(String strToMod){
        char firstChar = Character.toUpperCase(strToMod.charAt(0));
        String strReuslt = String.valueOf(firstChar);
        //String characterToString = Character.toString('c');
        return strReuslt += strToMod.substring(1);
    }

    private void makeDataBaseRecord(String strValue) {
        DataBase mysqlConnect = new DataBase();

        //String sql = "INSERT INTO devicetype (value) VALUE ('Планшет')";
        String sql = "INSERT INTO devicetype (value) VALUE ('" + strValue + "')";
        //String sql = "INSERT INTO devicetype (value) VALUE (?)";

        try {
            PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
            //statement.setString(1, strValue);

            statement.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            mysqlConnect.disconnect();
        }
    }
}
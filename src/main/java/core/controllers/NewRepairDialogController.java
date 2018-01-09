package core.controllers;

import core.utils.AutoSuggestTextField;
import core.utils.DataBase;
import core.utils.MsgBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;


public class NewRepairDialogController {

    @FXML private Label lDeviceID;
    @FXML private AutoSuggestTextField tfDeviceType;
    @FXML private AutoSuggestTextField tfBrand;
    @FXML private AutoSuggestTextField tfModel;
    @FXML private AutoSuggestTextField tfSerialNumber;
    @FXML private AutoSuggestTextField tfCompleteness;
    @FXML private AutoSuggestTextField tfAppearance;
    @FXML private AutoSuggestTextField tfDefect;
    @FXML private AutoSuggestTextField tfNote;
    @FXML private AutoSuggestTextField tfSurname;
    @FXML private AutoSuggestTextField tfName;
    @FXML private AutoSuggestTextField tfPatronymic;
    @FXML private AutoSuggestTextField tfPhone;

    @FXML
    private void initialize() {
        //tfBrand.setPromptText("Введите название брэнда");

        //получить из базы данных список производителей
        getEntriesFromDataBase(tfDeviceType,    "devicetype");
        getEntriesFromDataBase(tfBrand,         "brand");
        getEntriesFromDataBase(tfModel,         "devicemodel");
        getEntriesFromDataBase(tfCompleteness,  "completeness");
        getEntriesFromDataBase(tfAppearance,    "appearance");
        getEntriesFromDataBase(tfDefect,        "defect");

        getEntriesFromDataBase(tfSurname,       "surname");
        getEntriesFromDataBase(tfName,          "name");
        getEntriesFromDataBase(tfPatronymic,    "patronymic");
        //getEntriesFromDataBase(tfPhone,         "phone");
    }

    @FXML
    private void closeDlg(ActionEvent actionEvent){
        //получить источник события
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private boolean isOnlyLetters(String strToVerification) {
        return strToVerification.matches("[a-zA-Zа-яА-Я]+");
        //+ значит один и более символов
    }

    private boolean isOnlyDigits(String strToVerification) {
        return strToVerification.matches("[0-9]+");
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

    private void getEntriesFromDataBase(AutoSuggestTextField autoTextFields, String strTable) {
        //установить соединение с бд
        DataBase db = new DataBase();
        String strSql = "SELECT * FROM " + strTable + ";";

        ArrayList <String> stringArrayList = new ArrayList<>();

        //получить данные из указанной таблицы
        try {
            PreparedStatement statement = db.connect().prepareStatement(strSql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                stringArrayList.add(resultSet.getString("value"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            db.disconnect();
        }

        //заполнить выпадающий список подсказок
        autoTextFields.getEntries().addAll(stringArrayList);
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

        if (textField == tfDeviceType || textField == tfBrand || textField == tfModel) {
            //цифры, символы, пробел нельзя, только буквы
            if (!isOnlyLetters(strTfText)) {
                MsgBox.show("Можно вводить только буквы.", MsgBox.Type.MB_ERROR);
                textField.requestFocus();
                return;
            }
        }
        else {
            //то можно запятую и пробел
            if (!strTfText.matches("[a-zA-Zа-яА-Я, ]+")) {
                MsgBox.show("Можно вводить только буквы, пробел и запятую", MsgBox.Type.MB_ERROR);
                textField.requestFocus();
                return;
            }
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

    @FXML
    private void onBtnAdd(ActionEvent actionEvent) {
        Button clickedBtn = (Button) actionEvent.getSource();

        //Object source = actionEvent.getSource();
        //
        //        if(!(source instanceof Button)){
        //            return;
        //        }

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

    @FXML
    private void onBtnOk(ActionEvent actionEvent) {
        //получить DeviceID

        //получить текст из всех полей ввода

        //создадим список полей ввода
        ArrayList <AutoSuggestTextField> arrayList = new ArrayList<>();

        arrayList.addAll(Arrays.asList(tfDeviceType, tfBrand, tfModel, tfSerialNumber, tfCompleteness, tfAppearance,
        tfDefect, tfSurname, tfName, tfPatronymic, tfPhone));

        for (AutoSuggestTextField tf : arrayList) {
            if (tf.getText().isEmpty()) {
                MsgBox.show("В одно из полей ввода не введены данные", MsgBox.Type.MB_ERROR);
                tf.requestFocus();
                return;
            }
        }

        //записать данные в таблицы
        /* https://dev.mysql.com/doc/refman/5.7/en/getting-unique-id.html
         INSERT INTO foo (auto,text)
            VALUES(NULL,'text');         # generate ID by inserting NULL
            INSERT INTO foo2 (id,text)
            VALUES(LAST_INSERT_ID(),'text');  # use ID in second table

        Пока что алгоритм таков:
        Вставить данные в первую таблицу
            Получить id первой таблицы (SELECT id FROM tbl_1 WHERE string='$string')
        Зная родительский id вставить данные во вторую таблицу.

        */

        closeDlg(actionEvent);
    }
}

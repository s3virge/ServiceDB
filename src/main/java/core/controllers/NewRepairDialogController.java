package core.controllers;

import core.utils.AutoSuggestTextField;
import core.utils.DataBase;
import core.utils.HashtableValues;
import core.utils.MsgBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;


public class NewRepairDialogController {

    @FXML private Label lDeviceID;

    @FXML private Label lDeviType;
    @FXML private Label lBrand;
    @FXML private Label lModel;
    @FXML private Label lSerialNumber;
    @FXML private Label lCompleteness;
    @FXML private Label lAppearance;
    @FXML private Label lDefect;
    @FXML private Label lNote;
    @FXML private Label lSurname;
    @FXML private Label lName;
    @FXML private Label lPatronymic;
    @FXML private Label lPhone;

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

    // Creating a Hashtable
    Hashtable<AutoSuggestTextField, HashtableValues> hashtFields = new Hashtable<>();

    /**
    //еще в таблицу device нужно добавить поле device_id в которое будет записываться
    //номер устройства*/

    @FXML
    private void initialize() {
        hashtFields.put(tfDeviceType,   new HashtableValues("devicetype",   lDeviType.getText()));
        hashtFields.put(tfBrand,        new HashtableValues("brand",        lBrand.getText()));
        hashtFields.put(tfModel,        new HashtableValues("devicemodel",  lModel.getText()));
        hashtFields.put(tfCompleteness, new HashtableValues("completeness", lCompleteness.getText()));
        hashtFields.put(tfAppearance,   new HashtableValues("appearance",   lAppearance.getText()));
        hashtFields.put(tfDefect,       new HashtableValues("defect",       lDefect.getText()) );
        hashtFields.put(tfSurname,      new HashtableValues("surname",      lSurname.getText()));
        hashtFields.put(tfName,         new HashtableValues("name",         lName.getText()));
        hashtFields.put(tfPatronymic,   new HashtableValues("patronymic",   lPatronymic.getText()));
        hashtFields.put(tfPhone,        new HashtableValues("owner.telephone_number",        lPhone.getText()));

        getEntries();
    }

    //  получаем из базы подсказки
    private void getEntries() {

        Enumeration enumTextField;
        AutoSuggestTextField textField;

        enumTextField = hashtFields.keys();

        while (enumTextField.hasMoreElements()) {
            textField = (AutoSuggestTextField) enumTextField.nextElement();
            if (textField == tfPhone) continue;
            dbGetEntries(textField,    hashtFields.get(textField).getDbTable());
        }
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

    private boolean isEnteredCorrectly() {
        Enumeration enumeration;
        AutoSuggestTextField textField;

        enumeration = hashtFields.keys();

        while (enumeration.hasMoreElements()) {
            textField = (AutoSuggestTextField) enumeration.nextElement();
            if (textField.getText().isEmpty()) {
                MsgBox.show("Не введены данные в поле" + hashtFields.get(textField).getTaxtFieldLabal(), MsgBox.Type.MB_ERROR);
                textField.requestFocus();
                return false;
            }
        }
        return true;
    }

    //make the first letter big а остальные маленькие
    private String makeFirstLetterBig (String strToMod){
        char firstChar = Character.toUpperCase(strToMod.charAt(0));
        String strReuslt = String.valueOf(firstChar);
        //String characterToString = Character.toString('c');
        return strReuslt += strToMod.substring(1).toLowerCase();
    }

    private void dbInsert (String strTable, String strValue) {
        DataBase dataBase = new DataBase();

        //String sql = "INSERT INTO devicetype (value) VALUE ('Планшет')";
        // String sql = "INSERT INTO devicetype (value) VALUE (?)";
        String sql = "INSERT INTO " + strTable + " (value) VALUE ('" + strValue + "')";        

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

    private void dbGetEntries (AutoSuggestTextField asTextField, String strTable) {
        //установить соединение с бд
        DataBase db = new DataBase();
        String strSql = "SELECT * FROM " + strTable + ";";

        ArrayList <String> alEntries = new ArrayList<>();

        //получить данные из указанной таблицы
        try {
            PreparedStatement statement = db.connect().prepareStatement(strSql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                alEntries.add(resultSet.getString("value"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            db.disconnect();
        }

        //заполнить выпадающий список подсказок
        asTextField.getEntries().addAll(alEntries);
    }

    private void dbRecord(AutoSuggestTextField textField, String strDbTable ) {
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
        dbInsert(strDbTable, strDbValue);

        //если в базу все записалось        
        //то вставить в поле ввода новую строку с большой первой буквой
        textField.setText(strDbValue);
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
                dbRecord(tfDeviceType, "devicetype");
                break;

            case "btnAddBrand":
                dbRecord(tfBrand, "brand");
                break;

            case "btnAddModel":
                dbRecord(tfModel, "devicemodel");
                break;

            case "btnAddCompleteness":
                dbRecord(tfCompleteness, "completeness");
                break;

            case "btnAddAppearance":
                dbRecord(tfAppearance, "appearance");
                break;

            case "btnAddDefect":
                dbRecord(tfModel, "defect");
                break;
        }
    }

    @FXML
    private void onBtnOk(ActionEvent actionEvent) {
        //получить DeviceID

        //если данные вводятся неправильно
        if (!isEnteredCorrectly())
            return;

        //записать данные в таблицы
        //https://dev.mysql.com/doc/refman/5.7/en/getting-unique-id.html
         /*
         INSERT INTO foo (auto,text)
            VALUES(NULL,'text');         # generate ID by inserting NULL
            INSERT INTO foo2 (id,text)
            VALUES(LAST_INSERT_ID(),'text');  # use ID in second table
        */
        
        ///////// Пока что алгоритм таков:
        //Вставить данные в первую таблицу
        //String sqlInsert = "INSERT INTO " + strTable + " (value) VALUE ('" + strValue + "')";
        
        //Получить id первой таблицы (SELECT id FROM tbl_1 WHERE string='$string')
        //Зная родительский id вставить данные во вторую таблицу.

        closeDlg(actionEvent);
    }
}

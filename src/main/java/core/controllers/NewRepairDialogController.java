package core.controllers;

import core.utils.AutoSuggestTextField;
import core.database.DataBase;
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
import java.util.*;


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
    Hashtable<AutoSuggestTextField, HashtableValues> htFields = new Hashtable<>();

    /**
    //еще в таблицу device нужно добавить поле device_id в которое будет записываться
    //номер устройства*/

    @FXML
    private void initialize() {

        tfSurname.setText("Кобзарь");
        tfName.setText("Виталий");
        tfPatronymic.setText("Владимирович");
        tfPhone.setText("0506831226");

        htFields.put(tfDeviceType,   new HashtableValues("devicetype",  "value", lDeviType.getText()));
        htFields.put(tfBrand,        new HashtableValues("brand",       "value", lBrand.getText()));
        htFields.put(tfModel,        new HashtableValues("devicemodel", "value", lModel.getText()));
        htFields.put(tfCompleteness, new HashtableValues("completeness","value", lCompleteness.getText()));
        htFields.put(tfAppearance,   new HashtableValues("appearance",  "value", lAppearance.getText()));
        htFields.put(tfDefect,       new HashtableValues("defect",      "value", lDefect.getText()) );
        htFields.put(tfSurname,      new HashtableValues("surname",     "value", lSurname.getText()));
        htFields.put(tfName,         new HashtableValues("name",        "value", lName.getText()));
        htFields.put(tfPatronymic,   new HashtableValues("patronymic",  "value", lPatronymic.getText()));
        htFields.put(tfPhone,        new HashtableValues("owner",       "telephone_number", lPhone.getText()));
        htFields.put(tfSerialNumber, new HashtableValues("device",      "serial_number", lSerialNumber.getText()));

        getEntries();
    }

    //  получаем из базы подсказки Используем итератор для перебора елементов
    private void getEntries() {

        Set<AutoSuggestTextField> keys = htFields.keySet();
        AutoSuggestTextField suggestTextField;

        //Obtaining iterator over set entries
        Iterator<AutoSuggestTextField> itr = keys.iterator();

        while (itr.hasNext()) {
            suggestTextField = itr.next();

            if (suggestTextField == tfPhone)
                continue;

            dbGetEntries(suggestTextField,    htFields.get(suggestTextField).getDbTable());
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
        Set<AutoSuggestTextField> keys = htFields.keySet();
        AutoSuggestTextField suggestTextField;

        //Obtaining iterator over set entries
        Iterator<AutoSuggestTextField> itr = keys.iterator();

        while (itr.hasNext()) {
            suggestTextField = itr.next();

            if (suggestTextField.getText().isEmpty()) {
                MsgBox.show("Не введены данные в поле " + htFields.get(suggestTextField).getTaxtFieldLabal(), MsgBox.Type.MB_ERROR);
                suggestTextField.requestFocus();
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

   //получить из базы данных подсказки
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

////    private void makeDataBaseRecord(AutoSuggestTextField textField, String strDbTable ) {
////        //получить введенный текст из textfield
////        String strTfText = textField.getText();
////        //если ничего не введено
////        if (strTfText.isEmpty()) {
////            MsgBox.show("Для начала нужно что-то написать в поле ввода", MsgBox.Type.MB_ERROR);
////            textField.requestFocus();
////            return;
////        }
////
////        if (textField == tfDeviceType || textField == tfBrand) {
////            //цифры, символы, пробел нельзя, только буквы
////            if (!isOnlyLetters(strTfText)) {
////                MsgBox.show("Можно вводить только буквы.", MsgBox.Type.MB_ERROR);
////                textField.requestFocus();
////                return;
////            }
////        }
////        else {
////            //то можно запятую и пробел
////            if (!strTfText.matches("[a-zA-Zа-яА-Я, ]+")) {
////                MsgBox.show("Можно вводить только буквы, пробел и запятую", MsgBox.Type.MB_ERROR);
////                textField.requestFocus();
////                return;
////            }
////        }
////
////        //Сделать первую букву заглавной, а остальные прописными
////        String strDbValue = makeFirstLetterBig(strTfText);
////
////        //если такая запись в базе уже есть
////        //Сказать что запись есть и дубликатов быть не может
////
////        //сделать запись в таблицу базы данных
////        DataBase.insert(strDbTable, strDbValue);
////
////        //если в базу все записалось
////        //то вставить в поле ввода новую строку с большой первой буквой
////        textField.setText(strDbValue);
////
////        //передать фокус ввода следующему полю ввода (программно нажать Tab кнопку)
////    }

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
//                makeDataBaseRecord(tfDeviceType, "devicetype");
                break;

            case "btnAddBrand":
//                makeDataBaseRecord(tfBrand, "brand");
                break;

            case "btnAddModel":
//                makeDataBaseRecord(tfModel, "devicemodel");
                break;

            case "btnAddCompleteness":
//                makeDataBaseRecord(tfCompleteness, "completeness");
                break;

            case "btnAddAppearance":
//                makeDataBaseRecord(tfAppearance, "appearance");
                break;

            case "btnAddDefect":
//                makeDataBaseRecord(tfModel, "defect");
                break;
        }
    }

    @FXML
    private void onBtnOk(ActionEvent actionEvent) {

        //если данные вводятся неправильно
        /*if (!isEnteredCorrectly())
            return;*/

        // Пока что алгоритм таков:
        //Вставить данные в первую таблицу
        //String sqlInsert = "INSERT INTO " + strTable + " (value) VALUE ('" + strValue + "')";
        //Получить id первой таблицы (SELECT id FROM tbl_1 WHERE string='$string')
        //Зная родительский id вставить данные во вторую таблицу.

       /* int typeId = dbGetIdPutIfAbsent(tfDeviceType);
        int brandId = dbGetIdPutIfAbsent(tfBrand);
        int modelId = dbGetIdPutIfAbsent(tfModel);

        int completenessId = dbGetIdPutIfAbsent(tfCompleteness);
        int appearanceId = dbGetIdPutIfAbsent(tfAppearance);
        int defectId = dbGetIdPutIfAbsent(tfDefect);

        //нужно добавить в какую то таблицу колонку для заметок
        //int noteId = dbGetIdPutIfAbsent(tfNote);
*/
        int surnameId = dbGetIdPutIfAbsent(tfSurname);
        int nameId = dbGetIdPutIfAbsent(tfName);
        int patronymicId = dbGetIdPutIfAbsent(tfPatronymic);

        //в таблице owner 4 колонки. Нужно все колонки заполнить данными
        //и записать в неё телефонный номер
        //int phoneId = dbGetIdPutIfAbsent(tfPhone);
        int ownerId = dbPutOwner(surnameId, nameId, patronymicId, tfPhone.getText());

        //когда номер телефона не из цифр вываливается ошибка

        if (ownerId == -1)
            return;

        //dbPutDevice(typeId, brandId, modelId, tfSerialNumber.getText(), defectId, ownerId);

        closeDlg(actionEvent);
    }

    private void dbPutDevice(int typeId, int brandId, int modelId, String text, int defectId, int ownerId) {
    }

    /**
     *  заполняем данными таблицу Owner*/
    private int dbPutOwner(int surnameId, int nameId, int patronymicId, String strPhoneNumber) {
        String sql = "INSERT INTO owner (surname_id, name_id, patronymic_id, telephone_number)" +
                " VALUES (" + surnameId + "," + nameId + "," + patronymicId + "," + strPhoneNumber + ")";

        int insertedId = -1;

        if (DataBase.insert(sql)) {
            sql = "Select max(id) as id from owner";
            insertedId = DataBase.getId(sql);
        }
        else {
            MsgBox.show("Облом с dbPutOwner()", MsgBox.Type.MB_ERROR);
        }

        return insertedId;
    }

    /**
     * @return id данных введенных в поле textField, если такого значения в соответстующей таблице
     * связанной с текстовым полем нет, то записать его в таблицу
     */
    private int dbGetIdPutIfAbsent(AutoSuggestTextField textF) {
        String strTable = htFields.get(textF).getDbTable();
        String strColumn = htFields.get(textF).getsDbColumn();
        String strText = makeFirstLetterBig(textF.getText());

        int id = DataBase.getId(strTable, strColumn, strText);

        if (id == 0) {
            DataBase.insert(strTable, strColumn, strText);
            id = DataBase.getId(strTable, strColumn, strText);
        }
        return id;
    }
}

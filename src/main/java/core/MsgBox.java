package core;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MsgBox {

   public static void show(String msgText,  Type msgType) {

       AlertType alertType = AlertType.NONE;
       String titleBar = "None";

       if (msgType != null) {
           switch (msgType) {
               case MB_ERROR:
                   alertType = AlertType.ERROR;
                   titleBar = "Ошибка";
                   break;
               case MB_INFO:
                   alertType = AlertType.INFORMATION;
                   titleBar = "Информация";
                   break;
               case MB_WARNING:
                   alertType = AlertType.WARNING;
                   titleBar = "Внимание";
                   break;
           }
       }

       Alert alert = new Alert(alertType);
       alert.setTitle(titleBar);
       alert.setHeaderText(null);
       alert.setContentText(msgText);
       alert.showAndWait();
   }

    public enum Type {
        MB_ERROR,
        MB_INFO,
        MB_WARNING
    }
}
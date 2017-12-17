package core.controllers;

import core.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;

public class MainWndController {
    private static final Logger logger = Logger.getLogger(LoginWndController.class);

    @FXML
    private MenuBar MainMenuBar; //fx:id главного меню

    public MainWndController() {
        logger.debug("execute core.MainWndController constructor");
    }

    /*
    FXMLLoader f = new FXMLLoader();
     final Parent fxmlRoot = (Parent)f.load(new FileInputStream(new File("JavaFx2Menus.fxml")));
      stage.setScene(new Scene(fxmlRoot));
      stage.show();
      */

    @FXML
    private void menuItemNewRepair(){
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/Dialogs/NewRepairDlg.fxml"));

            AnchorPane repairDlgLayout = loader.load();

            // Создаём подмостки для диалогового окна.
            Stage dialogStage = new Stage();
            //подготавливаем их
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(MainMenuBar.getScene().getWindow());

            //расставляем декорации на сцене согласно плану
            Scene scene = new Scene(repairDlgLayout);
            dialogStage.setScene(scene);

            dialogStage.setResizable(false);
            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            dialogStage.showAndWait();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

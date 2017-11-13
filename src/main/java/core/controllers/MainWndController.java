package core.controllers;

import core.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;

public class MainWndController {
    private static final Logger logger = Logger.getLogger(LoginWndController.class);

    // Reference to the main application
    private MainApp mainApp;

    /**
     * Получить доступ к главному классу приложения для возможности получения ссылки на
     * primaryStage
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public MainWndController() {
        logger.debug("execute core.MainWndController constructor");
    }

    @FXML
    private boolean handleNewRepair(){
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/Dialogs/NewRepairDlg.fxml"));
            AnchorPane page = loader.load();

            // Создаём диалоговое окно Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Передаём адресата в контроллер.
            NewRepairDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            dialogStage.showAndWait();

            return controller.isOkClicked();
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

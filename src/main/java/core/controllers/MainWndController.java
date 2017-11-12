package core.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import org.apache.log4j.Logger;

public class MainWndController {
    private static final Logger logger = Logger.getLogger(LoginWndController.class);

    public MainWndController() {
        logger.debug("execute core.MainWndController constructor");
    }

    @FXML
    private void handleNewRepair(){
        Alert msg = new Alert(Alert.AlertType.INFORMATION);
        msg.setHeaderText(null);
        msg.setContentText("Нажали на кнопку Новый ремонт.");
        msg.showAndWait();
    }
}

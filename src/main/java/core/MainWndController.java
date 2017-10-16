package core;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.apache.log4j.Logger;

public class MainWndController {
    private static final Logger logger = Logger.getLogger(LoginWndController.class);

    @FXML private ListView deviceNumbersList;
    @FXML private Button btnFindDeviceNum;
    @FXML private TextField devNumToFind;

    public MainWndController() {
        logger.debug("execute core.MainWndController constructor");
    }

    @FXML
    private void handleFindDeviceNumber(){
        Alert msg = new Alert(Alert.AlertType.INFORMATION);
        msg.setHeaderText(null);
        msg.setContentText("Нажали на кнопку найти номер ремонта.");
        msg.showAndWait();
    }
}

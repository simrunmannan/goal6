package team.login;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import team.AbstractFXController;
import team.model.User;

public class LoginController extends AbstractFXController {
    
    @FXML
    TextField userField;

    @FXML
    TextField passField;

    @FXML
    Text errorMessage;

    @FXML
    private void goBack() {
        mainFxApplication.showMainScreen();
    }

    @FXML
    private void logIn() {
        //if validation fails, show error message
        User user = LoginBS.login(userField.getText(), passField.getText());
        if (user == null) {
            errorMessage.setVisible(true);
        } else {
            mainFxApplication.showDashboard(user);
        }

    }
}

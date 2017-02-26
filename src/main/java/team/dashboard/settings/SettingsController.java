package team.dashboard.settings;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import team.dashboard.AbstractDashboardController;
import team.model.User;

public class SettingsController extends AbstractDashboardController {

    private User user;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passField;

    @Override
    public void initController() {
        user = dashboardController.getUser();

        if (user.getEmail() == null) {
            emailField.setPromptText("example@example.com");
        } else {
            emailField.setText(user.getEmail());
        }

        passField.setText(user.getPassword());

    }

    @FXML
    private void save() {
        if ((user.getEmail() == null) || !user.getEmail().equals(emailField.getText())) {
            user.setEmail(emailField.getText());
        }
        if (!user.getPassword().equals(passField.getText())) {
            user.setPassword(passField.getText());
        }
        SettingsBS.updateUser(user.getUsername(), emailField.getText(), passField.getText());
    }
}

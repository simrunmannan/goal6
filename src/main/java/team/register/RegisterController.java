package team.register;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import team.AbstractFXController;
import team.model.User;
import team.model.enums.UserType;

public class RegisterController extends AbstractFXController {

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passField;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<UserType> userTypeComboBox;

    @FXML
    private Text errorText;

    private final ObservableList<UserType> userTypes = FXCollections.observableArrayList(UserType.values());

    @FXML private void initialize() {
        userTypeComboBox.setItems(userTypes);
        userTypeComboBox.setValue(UserType.WORKER);
    }

    @FXML
    private void goBack() {
        mainFxApplication.showMainScreen();
    }

    @FXML
    private void register() {
        String username = userField.getText();
        String password = passField.getText();
        String email = emailField.getText().isEmpty() ? null : emailField.getText();
        UserType userType = userTypeComboBox.getValue();

        if (username.isEmpty() || password.isEmpty() || (userType == null)) {
            errorText.setVisible(true);
            errorText.setText("Please fill out all fields");

        } else if ((email != null) && !email.toUpperCase().matches("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$")) {
            errorText.setText("Not a valid email address");
            errorText.setVisible(true);
        /*} else if (!registerService.isUniqueUsername(username)) {
            errorText.setVisible(true);
            errorText.setText("User Name already exists");
            userField.setStyle("-fx-background-color: salmon;");
        */} else {
            User newUser = RegisterBS.addUser(username, password, userType, email);
            if (newUser == null) {
                errorText.setVisible(true);
                errorText.setText("User Name already exists");
                userField.setStyle("-fx-background-color: salmon;");
            } else {
                mainFxApplication.showDashboard(newUser);
            }
        }
    }

}

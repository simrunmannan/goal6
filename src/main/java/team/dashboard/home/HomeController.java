package team.dashboard.home;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import team.dashboard.AbstractDashboardController;

public class HomeController extends AbstractDashboardController {

    @FXML
    private Label welcomeText;

    @FXML
    private Pane mainPane;

    @Override
    public void initController() {
        welcomeText.setText("Welcome, " + dashboardController.getUser().getUsername());
    }

}

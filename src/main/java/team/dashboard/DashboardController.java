package team.dashboard;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import team.AbstractFXController;
import team.model.User;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardController extends AbstractFXController {

    private static final Logger LOGGER = Logger.getLogger("DashboardController");

    private User user;
    private DashboardService dashboardService;


    public void initController(User user) {
        this.user = user;
        dashboardService = new DashboardService(user);
        menuPane.setPrefWidth(0);
        showReports();
        try {
            userImg.setImage(new Image(mainFxApplication.getClass().getResourceAsStream("/icons/Manager.png")));
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "User image not loaded" + e.getClass());
        }
    }

    public User getUser() {
        return user;
    }

    @FXML
    private ImageView userImg;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private AnchorPane menuPane;
    private boolean menuToggled = true;

    @FXML
    private void logOff() {
        dashboardService.logout();
        mainFxApplication.showMainScreen();
    }

    @FXML
    private void toggleMenu() {
        if (menuToggled) {
            animatePane(menuPane, false);
            menuToggled = false;
        } else {
            animatePane(menuPane, true);
            menuToggled = true;
        }
    }

    @FXML
    private void showSettings() {
        switchStackPane("settings.fxml");
    }

    @FXML
    private void showHome() {
        switchStackPane("home.fxml");
    }

    @FXML
    private void showReports() {
        switchStackPane("report.fxml");
    }

    private void switchStackPane(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(mainFxApplication.getClass().getResource("/fxml/dashboard/" + fxml));
            if (!anchorPane.getChildren().isEmpty()) {
                anchorPane.getChildren().remove(0);
            }
            Parent newPane = loader.load();
            anchorPane.getChildren().add(newPane);
            AnchorPane.setTopAnchor(newPane, 0.0);
            AnchorPane.setBottomAnchor(newPane, 0.0);
            AnchorPane.setLeftAnchor(newPane, 0.0);
            AnchorPane.setRightAnchor(newPane, 0.0);
            AbstractDashboardController controller = loader.getController();
            controller.setDashboardController(this);
            controller.initController();
            toggleMenu();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not load " + fxml);
            e.printStackTrace();
        }
    }

    AnchorPane getStackPane() {
        return anchorPane;
    }

    private void animatePane(AnchorPane anchorPane, boolean in) {
        Timeline tm = new Timeline();
        KeyValue kv1 = new KeyValue(anchorPane.prefWidthProperty(), in?0:226);
        KeyValue kv2 = new KeyValue(anchorPane.prefWidthProperty(), in?226:0);
        KeyFrame kf = new KeyFrame(Duration.millis(150), kv1, kv2);
        tm.getKeyFrames().addAll(kf);
        tm.play();
    }
}

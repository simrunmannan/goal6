package team;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import team.dashboard.DashboardController;
import team.dashboard.report.ReportBS;
import team.model.User;
import team.register.RegisterBS;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main Application, will start the app and optionally spawn one of the following views:
 * Login, Register, Dashboard. Main Screen
 */
public class MainFxApplication extends Application {

    private static final Logger LOGGER = Logger.getLogger("MainFXApplication");
    private static final int INIT_WIDTH = 1200;
    private static final int INIT_HEIGHT = 720;

    private Stage primaryStage;

    /**
     * Initialize main data of App
     * @param stage javafx main stage of entire app
     * @throws Exception javafx exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        RegisterBS.createUserTable();
        ReportBS.createTables();
        this.primaryStage = stage;
        primaryStage.setScene(new Scene(new Pane(), INIT_WIDTH, INIT_HEIGHT));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/compass.png")));
        showMainScreen();
         /*
        //The following code is to be removed in the release branch, this is only for ease of testing.
        RegisterBS.addUser("user", "pass", UserType.MANAGER, "example@example.com");
        SettingsBS.updateUser("user", "example@example.com", "pass");
        showDashboard(LoginBS.login("user", "pass"));
        */
    }

    public void showMainScreen() {
        primaryStage.setTitle("Welcome!");
        setScene("MainScreen.fxml");
    }

    public void showLogin() {
        primaryStage.setTitle("Login");
        setScene("Login.fxml");
    }

    public void showDashboard(User user) {
        primaryStage.setTitle("Dashboard");
        DashboardController dashboardController = ((DashboardController) setScene("Dashboard.fxml"));
        dashboardController.initController(user);
    }

    public void showRegister() {
        primaryStage.setTitle("Registration");
        setScene("Register.fxml");
    }


    /**
     * Custom method to switch screens of the app very quickly.
     * @param fxml  screen to display
     * @return  AbstractFXController instance to send instance data
     */
    private AbstractFXController setScene(String fxml) {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/" + fxml));
            Parent layout = loader.load();

            AbstractFXController controller = loader.getController();
            controller.setMainFxApplication(this);

            // Show the scene containing the root layout
            primaryStage.getScene().setRoot(layout);
            primaryStage.show();
            return controller;

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to find fxml file for " + fxml);
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

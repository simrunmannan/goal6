package team.mainscreen;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import team.AbstractFXController;


public class MainScreenController extends AbstractFXController {

    @FXML
    private void initialize() {
        animate(compass);
    }

    @FXML
    private ImageView compass;

    @FXML
    private void showLogin() {
        mainFxApplication.showLogin();
    }

    @FXML
    private void showRegister() {
        mainFxApplication.showRegister();
    }


    private void animate(ImageView image) {
        image.setRotate(0);
        Timeline tm = new Timeline();
        KeyValue kv1 = new KeyValue(image.rotateProperty(), 0);
        KeyValue kv2 = new KeyValue(image.rotateProperty(), 360);
        KeyFrame kf = new KeyFrame(Duration.millis(15000), kv1, kv2);
        tm.getKeyFrames().addAll(kf);
        tm.play();
    }

    private void animateWater(ImageView image) {
        FadeTransition wateranimate = new FadeTransition(Duration.millis(3000), image);
        wateranimate.setFromValue(1.0);
        wateranimate.setToValue(0.3);
        wateranimate.setCycleCount(4);
        wateranimate.setAutoReverse(true);
        wateranimate.play();
    }
}

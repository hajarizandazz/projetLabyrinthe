package Controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.application.Platform;

public class LaunchController {

    @FXML
    private Label countdownLabel;

    private String pseudo;

    @FXML
    public void initialize() {
        Timeline countdown = new Timeline(
                new KeyFrame(Duration.seconds(0), event -> countdownLabel.setText("3")),
                new KeyFrame(Duration.seconds(1), event -> countdownLabel.setText("2")),
                new KeyFrame(Duration.seconds(2), event -> countdownLabel.setText("1")),
                new KeyFrame(Duration.seconds(3), event -> loadPlateau())
        );
        countdown.setCycleCount(1);
        countdown.play();
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    private void loadPlateau() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/plateau.fxml"));
            Parent root = loader.load();

            ControllerPlateau controllerPlateau = loader.getController();
            controllerPlateau.setPseudo(pseudo);

            Scene scene = new Scene(root, 800, 600);
            Stage stage = (Stage) countdownLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Plateau");
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

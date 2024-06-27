package Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ControllerMenu {
    @FXML
    private Label bonnechance;
    @FXML
    private Button btnJouer;
    @FXML
    private Button btnRejouer;
    @FXML
    private Button btnquitter;
    @FXML
    private Button btnInstructions;
    @FXML
    private Button btnQuitter;

    @FXML
    private Button validerButton;
    @FXML
    private Button btnAccueilJ;
    @FXML
    private TextField TextPseudo;
    @FXML
    private Label Pseudo;
    @FXML
    private Label Difficulté;
    @FXML
    private ImageView imageViewJoueur;
    @FXML
    private List<Image> images;
    @FXML
    private String pseudo;

    private int currentIndex = 0;
    private int currentImageIndex = 0;
    private Image selectedCharacterImage;

    @FXML
    public void initialize() {
        images = new ArrayList<>();

        images.add(new Image(getClass().getResource("/application/images/image2.png").toExternalForm()));
        images.add(new Image(getClass().getResource("/application/images/mario.png").toExternalForm()));
        images.add(new Image(getClass().getResource("/application/images/pers.png").toExternalForm()));
        if (!images.isEmpty() && imageViewJoueur != null) {
            imageViewJoueur.setImage(images.get(currentIndex));
        }
    }

    @FXML
    private void handleValiderButtonActions(ActionEvent event) {
        pseudo = TextPseudo.getText();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/Launch.fxml"));
            Parent root = loader.load();

            // Passer le pseudo au ControllerPlateau
            LaunchController launchController = loader.getController();
            launchController.setPseudo(pseudo);

            Scene scene = new Scene(root, 550, 490);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Lancement");
            stage.setResizable(false);
            stage.show();

            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePrecedentButtonAction() {
        if (currentIndex > 0) {
            currentIndex--;
            imageViewJoueur.setImage(images.get(currentIndex));
        }
    }

    @FXML
    private void handlePlayButtonAction(ActionEvent event) {
        System.out.println("Le bouton Jouer a été cliqué");
        loadView("/application/jeu.fxml", event);
    }

    @FXML
    private void handleSuivantButtonAction() {
        if (currentIndex < images.size() - 1) {
            currentIndex++;
            imageViewJoueur.setImage(images.get(currentIndex));
        }
    }

    @FXML
    private void handleReplayButtonActions(ActionEvent event) {
        System.out.println("Rejouer cliqué");
        loadView("/application/SauvgardeView.fxml", event);
    }

    @FXML
    private void handleInstructionsButtonAction(ActionEvent event) {
        System.out.println("Instructions cliqué");
        loadView("/application/InstructionsView.fxml", event);
    }

    @FXML
    private void handleQuitButtonAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void handleAccueilButtonActions(ActionEvent event) {
        loadView("/application/test1.fxml", event);
    }

    private void loadView(String fxml, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent view = loader.load();

            Scene scene = new Scene(view);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openClothingCustomization(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/clothing_customization.fxml"));
            Parent root = loader.load();

            ClothingCustomizationController customizationController = loader.getController();
            customizationController.setCharacterViewImage(getSelectedCharacter());
            customizationController.loadCharacterOptions(currentIndex);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Personnalisation des vêtements");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Image getSelectedCharacter() {
        return images.get(currentIndex);
    }
}

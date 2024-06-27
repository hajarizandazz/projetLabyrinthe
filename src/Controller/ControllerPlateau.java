package Controller;

import Modele.Labyrinthe;
import Modele.Mechants;
import Modele.Player;
import Modele.Niveau;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ControllerPlateau {

    @FXML
    private GridPane gridPane;

    private Labyrinthe labyrinthe;
    private Mechants mechants;

    @FXML
    private Label labelTimer;
    @FXML
    private Label labelLives;

    private Player player;
    private Timeline timer;
    private int secondsElapsed;
    private Niveau niveauActuel;

    private Timeline mechantMovementTimeline;

    private final Set<Position> bonuses = new HashSet<>();

    private boolean isPaused = false;

    // Définir les niveaux
    private final Niveau niveauFacile = new Niveau(21, 21, 3, 1.0, 4);
    private final Niveau niveauMoyen = new Niveau(21, 21, 5, 0.75, 3);
    private final Niveau niveauDifficile = new Niveau(21, 21, 7, 0.3, 2);
    private Image playerImage;
    private String pseudo;

    // Variables pour stocker les valeurs initiales
    private final int initialLives = 3;
    private int initialTime;

    public void setPlayerImage(Image playerImage) {
        this.playerImage = playerImage;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    @FXML
    public void initialize() {
        player = new Player("Joueur1", initialLives);
        niveauActuel = niveauFacile; // Commencer par le niveau facile
        initialTime = niveauActuel.getTempsMinuteur();
        setupNiveau(niveauActuel);
        gridPane.setOnKeyPressed(this::handleMovement);
        gridPane.setFocusTraversable(true);
    }

    private void setupNiveau(Niveau niveau) {
        labyrinthe = new Labyrinthe(niveau.getLargeur(), niveau.getHauteur());
        mechants = new Mechants();
        mechants.getPositions().addAll(labyrinthe.getMechants(niveau.getNombreMechants()));
        placeBonuses();
        player.setPosition(0, 0); // Réinitialiser la position du joueur
        movePlayerToStartPosition(); // Déplacer le joueur à une position valide de départ
        afficherLabyrinthe();
        startMechantMovement(niveau.getVitesseMechants());
        startTimer(niveau.getTempsMinuteur());
    }

    private void movePlayerToStartPosition() {
        int startRow = 0;
        int startCol = 0;

        // Chercher une position de départ valide (chemin)
        while (labyrinthe.getGrid()[startRow][startCol] != Labyrinthe.PATH) {
            startCol++;
            if (startCol >= labyrinthe.getGrid()[0].length) {
                startCol = 0;
                startRow++;
            }
        }
        player.setPosition(startRow, startCol);
    }

    private Position getRandomValidPosition() {
        int rows = labyrinthe.getGrid().length;
        int cols = labyrinthe.getGrid()[0].length;
        int row, col;
        do {
            row = (int) (Math.random() * rows);
            col = (int) (Math.random() * cols);
        } while (labyrinthe.getGrid()[row][col] != Labyrinthe.PATH || (row == player.getRow() && col == player.getCol()));
        return new Position(row, col, "");
    }

    private void placeBonuses() {
        bonuses.clear();
        // Placer le premier bonus
        Position bonus1 = getRandomValidPosition();
        bonus1.setType("bonus1");
        bonuses.add(bonus1);

        // Placer le deuxième bonus
        Position bonus2 = getRandomValidPosition();
        bonus2.setType("bonus2");
        bonuses.add(bonus2);
    }

    private void afficherLabyrinthe() {
        int[][] grid = labyrinthe.getGrid();
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getChildren().clear();


        Image wallImage = new Image("/application/images/coquillages.png");
        BackgroundImage wallBackgroundImage = new BackgroundImage(wallImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(20, 20, false, false, false, false));


        Image pathImage = new Image("/application/images/mer.png");
        BackgroundImage pathBackgroundImage = new BackgroundImage(pathImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(20, 20, false, false, false, false));

        for (int i = 0; i < grid[0].length; i++) {
            gridPane.getColumnConstraints().add(new ColumnConstraints(20));
        }
        for (int i = 0; i < grid.length; i++) {
            gridPane.getRowConstraints().add(new RowConstraints(20));
        }

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == Labyrinthe.WALL) {
                    // Utiliser l'image pour les murs
                    StackPane cell = new StackPane();
                    cell.setBackground(new Background(wallBackgroundImage));
                    cell.setPrefSize(20, 20);
                    gridPane.add(cell, j, i);
                } else if (grid[i][j] == Labyrinthe.PATH) {
                    // Utiliser l'image pour les chemins
                    StackPane cell = new StackPane();
                    cell.setBackground(new Background(pathBackgroundImage));
                    cell.setPrefSize(20, 20);
                    gridPane.add(cell, j, i);
                }
            }
        }

        for (Mechants.Position pos : mechants.getPositions()) {
            ImageView imageView = new ImageView(new Image("/application/images/image5.png"));
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            gridPane.add(imageView, pos.getCol(), pos.getRow());
        }

        for (Position pos : bonuses) {
            ImageView imageView = new ImageView(new Image("/application/images/gift.png"));
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            gridPane.add(imageView, pos.getCol(), pos.getRow());
        }

        // Place the player
        ImageView playerImageView = new ImageView(playerImage != null ? playerImage : new Image("/application/images/image2.png"));
        playerImageView.setFitWidth(20);
        playerImageView.setFitHeight(20);
        gridPane.add(playerImageView, player.getCol(), player.getRow());


        ImageView imageArrivee = new ImageView(new Image("/application/images/arrivee.png"));
        imageArrivee.setFitWidth(20);
        imageArrivee.setFitHeight(20);
        gridPane.add(imageArrivee, grid[0].length - 2, grid.length);
    }

    private void handleMovement(KeyEvent event) {
        if (!isPaused) {
            switch (event.getCode()) {
                case UP:
                    movePlayer(player.getRow() - 1, player.getCol());
                    break;
                case DOWN:
                    movePlayer(player.getRow() + 1, player.getCol());
                    break;
                case LEFT:
                    movePlayer(player.getRow(), player.getCol() - 1);
                    break;
                case RIGHT:
                    movePlayer(player.getRow(), player.getCol() + 1);
                    break;
            }
            event.consume();
        }
    }

    private void movePlayer(int newRow, int newCol) {
        if (newRow >= 0 && newRow < labyrinthe.getGrid().length &&
                newCol >= 0 && newCol < labyrinthe.getGrid()[0].length &&
                labyrinthe.getGrid()[newRow][newCol] == Labyrinthe.PATH) {
            player.setPosition(newRow, newCol);
            checkCollisions();
            checkGameStatus();
            afficherLabyrinthe();
        }
    }

    private void checkCollisions() {
        boolean collisionWithMechant = false;

        for (Mechants.Position pos : mechants.getPositions()) {
            if (pos.getRow() == player.getRow() && pos.getCol() == player.getCol()) {
                player.loseLife();
                updateLivesLabel();
                collisionWithMechant = true;
                if (player.getLives() <= 0) {
                    stopAllTimers();
                    Platform.runLater(this::showGameOverDialog);
                    return;
                } else {
                    Platform.runLater(() -> showTemporaryNotification("Vous avez rencontré un méchant! Vous avez perdu une vie.", 200, 200, 3));
                }
            }
        }

        if (collisionWithMechant) {
            return;
        }

        bonuses.removeIf(pos -> {
            if (pos.getRow() == player.getRow() && pos.getCol() == player.getCol()) {
                String message = "";
                if (pos.getType().equals("bonus1")) {
                    player.gainLife();
                    updateLivesLabel();
                    message = "Bravo! Vous avez gagné une vie.";
                } else if (pos.getType().equals("bonus2")) {
                    adjustTimer(120); // Adds 2 minutes
                    message = "Bravo! Vous avez gagné 2 minutes supplémentaires.";
                }

                String finalMessage = message;
                Platform.runLater(() -> showTemporaryNotification(finalMessage, 200, 200, 3));
                return true;
            }
            return false;
        });
    }

    private void startMechantMovement(double vitesse) {
        mechantMovementTimeline = new Timeline(new KeyFrame(Duration.seconds(vitesse), event -> {
            mechants.moveMechants(labyrinthe.getGrid());
            checkCollisions();
            afficherLabyrinthe();
        }));
        mechantMovementTimeline.setCycleCount(Timeline.INDEFINITE);
        mechantMovementTimeline.play();
    }

    private void startTimer(int tempsMinuteur) {
        secondsElapsed = 0;
        int totalSeconds = tempsMinuteur * 60;
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondsElapsed++;
            int minutes = (totalSeconds - secondsElapsed) / 60;
            int seconds = (totalSeconds - secondsElapsed) % 60;
            labelTimer.setText(String.format("%02d:%02d", minutes, seconds));
            if (secondsElapsed >= totalSeconds) {
                timer.stop();
                stopAllTimers();
                Platform.runLater(this::showGameOverDialog);
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void stopAllTimers() {
        if (timer != null) {
            timer.stop();
        }
        if (mechantMovementTimeline != null) {
            mechantMovementTimeline.stop();
        }
    }

    private void adjustTimer(int secondsToAdd) {
        secondsElapsed -= secondsToAdd;
        if (secondsElapsed < 0) {
            secondsElapsed = 0;
        }
    }

    private void updateLivesLabel() {
        labelLives.setText(String.valueOf(player.getLives()));
    }

    private void checkGameStatus() {
        if (player.getRow() == labyrinthe.getGrid().length - 1 && player.getCol() == labyrinthe.getGrid()[0].length - 2) {
            stopAllTimers();
            Platform.runLater(this::showWinDialog);
        }
    }

    private void showWinDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Victoire");
        alert.setHeaderText("Félicitations, " + pseudo + "!");
        alert.setContentText("Vous avez gagné le niveau!");

        ButtonType buttonNextLevel = new ButtonType("Niveau Suivant");
        ButtonType buttonQuit = new ButtonType("Quitter");
        alert.getButtonTypes().setAll(buttonNextLevel, buttonQuit);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonNextLevel) {
            passerAuNiveauSuivant();
        } else if (result.isPresent() && result.get() == buttonQuit) {
            navigateToTestFXML();
        }
    }

    private void showGameOverDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Défaite");
        alert.setHeaderText("Dommage, " + pseudo + "!");
        alert.setContentText("Vous avez perdu!");

        ButtonType buttonRetry = new ButtonType("Recommencer");
        ButtonType buttonQuit = new ButtonType("Quitter");
        alert.getButtonTypes().setAll(buttonRetry, buttonQuit);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonRetry) {
            setupNiveau(niveauActuel);
        } else if (result.isPresent() && result.get() == buttonQuit) {
            navigateToTestFXML();
        }
    }

    private void showTemporaryNotification(String content, double x, double y, int durationSeconds) {
        Popup popup = new Popup();

        Label label = new Label(content);
        label.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-padding: 10px;");

        popup.getContent().add(label);
        popup.setX(x);
        popup.setY(y);
        popup.show(gridPane.getScene().getWindow());

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(durationSeconds), event -> popup.hide()));
        timeline.setCycleCount(1);
        timeline.play();
    }

    private void navigateToTestFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/test1.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) gridPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void passerAuNiveauSuivant() {
        if (niveauActuel == niveauFacile) {
            niveauActuel = niveauMoyen;
        } else if (niveauActuel == niveauMoyen) {
            niveauActuel = niveauDifficile;
        } else {
            // Le joueur a terminé tous les niveaux
            showWinDialog();
            return;
        }
        setupNiveau(niveauActuel);
    }

    public void updateLives(int lives) {
        player.setLives(player.getLives() + lives);
        labelLives.setText(String.valueOf(player.getLives()));
    }

    private static class Position {
        private int row;
        private int col;
        private String type;

        public Position(int row, int col, String type) {
            this.row = row;
            this.col = col;
            this.type = type;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    @FXML
    private void handleRestartButtonAction() {
        stopAllTimers();
        player = new Player("Joueur1", initialLives);
        labelLives.setText(String.valueOf(initialLives));
        setupNiveau(niveauActuel);
    }

    @FXML
    private void handlePauseButtonAction() {
        if (!isPaused) {
            isPaused = true;
            stopAllTimers();
            showPauseDialog();
        }
    }

    private void handleResumeButtonAction() {
        isPaused = false;
        startTimer(niveauActuel.getTempsMinuteur() - secondsElapsed / 60);
        startMechantMovement(niveauActuel.getVitesseMechants());
    }

    private void showPauseDialog() {
        VBox pauseLayout = new VBox(10);
        pauseLayout.setPadding(new Insets(10));
        pauseLayout.setAlignment(Pos.CENTER);

        Label pauseLabel = new Label("Jeu en pause");
        pauseLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Button resumeButton = new Button("Reprendre");
        resumeButton.setOnAction(e -> {
            handleResumeButtonAction();
            ((Stage) resumeButton.getScene().getWindow()).close();
        });
        Button quitButton = new Button("Quitter");
        quitButton.setOnAction(e -> {
            ((Stage) quitButton.getScene().getWindow()).close();
            navigateToTestFXML();
        });

        pauseLayout.getChildren().addAll(pauseLabel, resumeButton, quitButton);

        Scene pauseScene = new Scene(pauseLayout, 200, 100);
        Stage pauseStage = new Stage();
        pauseStage.setScene(pauseScene);
        pauseStage.show();
    }

    @FXML
    private void handleQuitButtonAction() {
        stopAllTimers();
        navigateToTestFXML();
    }
}

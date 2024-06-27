package Controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;

public class ClothingCustomizationController {

    @FXML
    private HBox headContainer;

    @FXML
    private ImageView characterView;

    private ImageView headImage;
    private Image selectedCharacterImage;

    @FXML
    public void initialize() {
        setupDragAndDrop();
    }


    public void loadCharacterOptions(int characterIndex) {
        headContainer.getChildren().clear();

        switch (characterIndex) {
            case 0:
                headContainer.getChildren().addAll(createImageView("/application/images/sonic1.png"),
                        createImageView("/application/images/sonic2.png"));
                break;
            case 1:
                headContainer.getChildren().addAll(createImageView("/application/images/mario1.png"),
                        createImageView("/application/images/mario2.png"));
                break;
            case 2:
                headContainer.getChildren().addAll(createImageView("/application/images/pers1.png"),
                        createImageView("/application/images/pers2.png"));
                break;

        }

        enableDragAndDrop(headContainer);
    }


    private ImageView createImageView(String imagePath) {
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        return imageView;
    }

    private void setupDragAndDrop() {
        characterView.setOnDragOver(event -> {
            if (event.getGestureSource() != characterView && event.getDragboard().hasImage()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        characterView.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasImage()) {
                Image image = db.getImage();
                headImage = new ImageView(image);
                characterView.setImage(image);
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }




    private void enableDragAndDrop(HBox container) {
        for (ImageView imageView : container.getChildren().filtered(node -> node instanceof ImageView).toArray(new ImageView[0])) {
            imageView.setOnDragDetected(event -> {
                Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putImage(imageView.getImage());
                db.setContent(content);
                event.consume();
            });
        }
    }



    @FXML
    private void handleValidation() {
        System.out.println("Personnalisation validée !");
    }
    public void setCharacterViewImage(Image image) {
        this.selectedCharacterImage = image;
        if (characterView != null) {
            characterView.setImage(image);
        }
    }

}

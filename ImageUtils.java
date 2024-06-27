package Controller;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import java.awt.image.BufferedImage;
public class ImageUtils {
        public static ImageView removeBackground(Image defaultImage, java.awt.Color backgroundColor) {

            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(defaultImage, null);

            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();


            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {

                    int rgb = bufferedImage.getRGB(x, y);
                    java.awt.Color pixelColor = new java.awt.Color(rgb, true);

                    if (pixelColor.equals(backgroundColor)) {
                        bufferedImage.setRGB(x, y, 0x00000000);
                    }
                }
            }


            Image newImage = SwingFXUtils.toFXImage(bufferedImage, null);

            ImageView imageView = new ImageView(newImage);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);

            return imageView;
        }
}

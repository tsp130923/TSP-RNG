package game.tsprng.assets;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class ImageLoader {

    public static BufferedImage bg1;
    public static BufferedImage cat;

    public static void load() {

        try {

            bg1 = ImageIO.read(
                Objects.requireNonNull(ImageLoader.class.getResourceAsStream(
                    "/background/bg1.png")));

            cat = ImageIO.read(
                Objects.requireNonNull(ImageLoader.class.getResourceAsStream(
                    "/cat/cat.png")));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

package eyesimo.bankmanager.io;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.*;

/**
 * Created by Anton_Erde on 03.12.2015.
 */
public class ImageWriter {

    public void writeImage(File imageCatalogue, BufferedImage image, String imageName) {

        File imageFile = new File(imageCatalogue + "/" + imageName + ".png");
        try {
            imageFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ImageIO.write(image, "png", imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

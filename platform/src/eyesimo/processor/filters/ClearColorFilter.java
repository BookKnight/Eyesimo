package eyesimo.processor.filters;

/**
 * Created by Anton_Erde on 03.12.2015.
 *
 * Implements methods, that perform image filtering.
 */

import java.awt.image.BufferedImage;

public class ClearColorFilter {

    private BufferedImage currentImage;
    private int[] currentImageData;

    public ClearColorFilter() {
        currentImage = null;
        currentImageData = null;
    }

    public BufferedImage filterImage(BufferedImage image) {
        currentImage = image;

        currentImageData = currentImage.getRGB(0, 0, currentImage.getWidth(), currentImage.getHeight(), null, 0, currentImage.getWidth());

        filter();

        currentImage.setRGB(0, 0, currentImage.getWidth(), currentImage.getHeight(), currentImageData, 0, currentImage.getWidth());

        return currentImage;
    }

    private void filter() {

        int width  = currentImage.getWidth();
        int height = currentImage.getHeight();
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int pixel = currentImageData[j + i * width];
                currentImageData[j + i * width] = filterPixel(pixel);
            }
    }

    private int filterPixel(int rgb) {

        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;

        r =  r > 127 ? 255 : 0;
        g =  g > 127 ? 255 : 0;
        b =  b > 127 ? 255 : 0;

        rgb = 0xff000000 | r << 16 | g << 8 | b;

        return rgb;
    }


}

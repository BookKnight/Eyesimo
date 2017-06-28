//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eyesimo.processor.segmentators;

import eyesimo.util.AnalystColors;

public class ImageFullChars implements AnalystColors {
    String imageName;
    int width;
    int height;
    ImageColorChars[] colorInfos;

    public ImageFullChars(String n, ImageColorChars[] ci) {
        this.imageName = n;
        this.width = 0;
        this.height = 0;
        this.colorInfos = ci;
    }

    public ImageFullChars(String n, int w, int h, ImageColorChars[] ci) {
        this.imageName = n;
        this.width = w;
        this.height = h;
        this.colorInfos = ci;
    }

    public void printInfo() {
        if ( width != 0 && height != 0)System.out.println(this.imageName + " " + "Size: "+ this.width + "x" + this.height + " ");


        for(int i = 0; i < this.colorInfos.length; ++i) {
            this.colorInfos[i].printInfo();
        }

        System.out.println("___________________________________________");
        System.out.println();

    }

    public String getImageName() {
        return imageName;
    }

    public ImageColorChars[] getColorInfos() {
        return colorInfos;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

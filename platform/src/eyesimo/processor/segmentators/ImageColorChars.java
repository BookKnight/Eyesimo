//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eyesimo.processor.segmentators;

import eyesimo.util.AnalystColors;

public class ImageColorChars implements AnalystColors {
    private int colorInd;
    private int fieldsCount;
    private int middleArea;
    private int middlePeriphery;

    public ImageColorChars(int _colorInd, int _fieldsCount, int _middleArea, int _middlePeriphery) {
        this.colorInd = _colorInd;
        this.fieldsCount = _fieldsCount;
        this.middleArea = _middleArea;
        this.middlePeriphery = _middlePeriphery;
    }

    public void printInfo() {
        System.out.println(colorPref[this.colorInd] + " " + this.fieldsCount + " " + this.middleArea + " " + this.middlePeriphery);
    }

    public int getColorInd() {
        return colorInd;
    }

    public int getFieldsCount() {
        return fieldsCount;
    }

    public int getMiddleArea() {
        return middleArea;
    }

    public int getMiddlePeriphery() {
        return middlePeriphery;
    }
}

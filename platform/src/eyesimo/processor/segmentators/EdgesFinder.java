//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package eyesimo.processor.segmentators;

import java.util.Vector;
import eyesimo.util.AnalystColors;

public class EdgesFinder implements AnalystColors {
    private int width;
    private int height;
    private int[] data;
    private String colorName;
    private int edgeColor;
    private Vector<Edge> edges;
    private int middleArea;

    private int fieldsCount;
    private int[] pixPerField;

    public EdgesFinder(int colorInd, int[] _data, int w, int h) {
        this.width = w;
        this.height = h;
        this.data = _data;
        colorName = colorPref[colorInd];
        this.edgeColor = colors[colorInd].getRGB();
        this.edges = new Vector();

        fieldsCount = 0;
        pixPerField = null;
    }

    //Find edges characteristics of connected fields (fields count and count of pixels per edge). NOT EDGES!
    // To find current edges use "getEdges()".
    public void findEdgesChars() {

        int[] labels = initLabelMatrix();

        Vector<Integer> fieldsIndexes = new Vector<>();

            for (int i = 0; i < labels.length; i++) {

                if( labels[i] != 0)
                    if ( !fieldsIndexes.contains( labels[i] ) )
                        fieldsIndexes.add(labels[i]);

            }

        //fieldsIndexes.sort( Integer::compareTo );

        for (int i = 0; i < labels.length; i++) {

            if ( labels[i] != 0)
                labels[i] = fieldsIndexes.indexOf( labels[i] ) + 1;
        }

        fieldsIndexes.sort( Integer::compareTo );

        fieldsCount = fieldsIndexes.size();
        pixPerField = new int[ fieldsCount ];

        for (int i = 1; i <= pixPerField.length; i++)
                for (int k = 0; k < labels.length; k++)
                    if ( labels[k] != 0 )
                       if (i == labels[k]) ++pixPerField[i-1];
    }

    private int[] initLabelMatrix() {

        int[] labels = new int[width * height];

        int labelCount = 0;
        boolean insideComponent = false;
        for (int i = 0; i < height; i++) {

            insideComponent = false;

            for (int j = 0; j < width; j++) {

                if ( data[j + i*width] == edgeColor )

                    if (insideComponent == false) {
                        insideComponent = true;
                        labelCount++;

                        labels[j + i * width] = labelCount;

                    } else labels[j + i * width] = labelCount;

                else if ( insideComponent == true) {
                    insideComponent = false;

                    labels[j + i*width] = 0;
                } else {
                    labels[j + i*width] = 0;
                }
            }
        }

        fixConflicts( labels );

        markContours( labels );




        return labels;
    }

    private void fixConflicts(int[] labels) {


        boolean repeatFlag = true;

        while ( repeatFlag == true ) {

            repeatFlag = false;

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {

                    if (j + 1 != width)
                        if (labels[j + i * width] > labels[j + 1 + i * width] && labels[j + 1 + i * width] != 0) {
                            labels[j + i * width] = labels[j + 1 + i * width];
                            repeatFlag = true;
                        }

                    if (j - 1 != -1)
                        if (labels[j + i * width] > labels[j - 1 + i * width] && labels[j - 1 + i * width] != 0) {
                            labels[j + i * width] = labels[j - 1 + i * width];
                            repeatFlag = true;
                        }

                    if (i + 1 != height)
                        if (labels[j + i * width] > labels[j + (i + 1) * width] && labels[j + (i + 1) * width] != 0) {
                            labels[j + i * width] = labels[j + (i + 1) * width];
                            repeatFlag = true;
                        }

                    if (i - 1 != -1)
                        if (labels[j + i * width] > labels[j + (i - 1) * width] && labels[j + (i - 1) * width] != 0) {
                            labels[j + i * width] = labels[j + (i - 1) * width];
                            repeatFlag = true;
                        }

                }
            }
        }
    }

    public void markContours( int[] labels ) {

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                if ( j + 1 != width && j - 1 != -1 && i + 1 != height && i - 1 != -1)
                    if ( data[j + i * width] - data[j + 1 + i * width] == 0
                            && data[j + i * width] - data[j - 1 + i * width] == 0
                            && data[j + i * width] - data[j + (i+1) * width] == 0
                            && data[j + i * width] - data[j + (i-1) * width] == 0
                            && data[j + i * width] - data[j + 1 + (i+1) * width] == 0
                            && data[j + i * width] - data[j - 1 + (i+1) * width] == 0
                            && data[j + i * width] - data[j + 1 + (i-1) * width] == 0
                            && data[j + i * width] - data[j - 1 + (i-1) * width] == 0)
                        labels[j + i * width] = 0;

            }
        }


    }

    public Vector<Edge> getEdges() {

        int[] labels = initLabelMatrix();

        Vector<Integer> fieldsIndexes = new Vector<>();

        for (int i = 0; i < labels.length; i++) {

            if( labels[i] != 0)
                if ( !fieldsIndexes.contains( labels[i] ) )
                    fieldsIndexes.add(labels[i]);

        }

        //fieldsIndexes.sort( Integer::compareTo );

        for (int i = 0; i < labels.length; i++) {

            if ( labels[i] != 0)
                labels[i] = fieldsIndexes.indexOf( labels[i] ) + 1;
        }

        fieldsIndexes.sort( Integer::compareTo );

        edges = new Vector<>( fieldsIndexes.size() );
        for (int i= 0; i < edges.capacity(); i++) edges.add( new Edge() );

        for (int i = 1; i <= edges.capacity(); i++)
            for (int j = 0; j < height; j++)
                for (int k = 0; k < width; k++) {

                    if ( labels[k + j*width] != 0 ) {
                        if (i == labels[k + j*width]) edges.get(i-1).put(k, j);
                    }
                }

        return this.edges;
    }

    public int getFieldsCount() {
        return this.fieldsCount;
    }

    public int getMiddlePeriphery() {
        int pixCount = 0;
        if(this.fieldsCount == 0) {
            return 0;
        } else {
            for(int i = 0; i < this.fieldsCount; ++i) {
                pixCount += pixPerField[i];
            }

            return (int) Math.round( (double) (pixCount) / this.fieldsCount );
        }
    }

    public int getMiddleArea() {
        int pixelsCount = 0;
        if(this.fieldsCount == 0) {
            return 0;
        } else {
            for(int i = 0; i < this.height; ++i) {
                for(int j = 0; j < this.width; ++j) {
                    if(this.data[j + i * this.width] == this.edgeColor) {
                        ++pixelsCount;
                    }
                }
            }

            return (int) Math.round( (double) (pixelsCount) / this.fieldsCount );
        }
    }
}

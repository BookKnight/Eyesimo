package eyesimo.processor.segmentators;

import java.util.Vector;

/**
 * Created by Anton_Erde on 10.12.2015.
 */
public class Edge {

    int pixCount;

    Vector<Integer> x;
    Vector<Integer> y;

    public Edge() {

        pixCount = 0;

        x = new Vector<>();
        y = new Vector<>();
    }

    public boolean contains(int xCoor, int yCoor) {

        for (int i = 0; i < x.size(); i++) {

            if (x.get(i) == xCoor && y.get(i) == yCoor) return true;

        }

        return false;
    }

    public void put(int xCoor, int yCoor) {

        x.add(xCoor);
        y.add(yCoor);

        pixCount++;
    }

    public int getPixCount() {
        return pixCount;
    }

}

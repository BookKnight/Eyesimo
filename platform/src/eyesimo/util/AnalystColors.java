package eyesimo.util;

import java.awt.*;

/**
 * Created by Anton_Erde on 04.12.2015.
 */
public interface AnalystColors {

    int backgroundColor = Color.lightGray.getRGB();

    final int colorCount = 8;
    final Color[] colors = {
            Color.red,
            Color.green,
            Color.blue,
            Color.magenta,
            Color.yellow,
            Color.cyan,
            Color.black,
            Color.white
    };

    final String colorPref[] = { "red","green","blue","magenta","yellow","cyan","black","white" };
}

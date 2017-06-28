package eyesimo.bankmanager.io;

import java.io.*;

import eyesimo.processor.segmentators.ImageColorChars;
import eyesimo.processor.segmentators.ImageFullChars;

/**
 * Created by Anton_Erde on 12.03.2016.
 */
public class CharsWriter {

    public void write(File bankCataloguePath, ImageFullChars fullInfo, String fileName) {

        if (!bankCataloguePath.exists()) bankCataloguePath.mkdir();

        try {
            File charsFile = new File(bankCataloguePath.getAbsolutePath() + "/" + fileName + ".txt");
            charsFile.createNewFile();

            BufferedOutputStream charsBOS = new BufferedOutputStream(new FileOutputStream(charsFile, false));
            PrintWriter charsPW = new PrintWriter(charsBOS);

            charsPW.print(fullInfo.getWidth() + " ");
            charsPW.println(fullInfo.getHeight());

            ImageColorChars[] tmp = fullInfo.getColorInfos();
            for (int i = 0; i < tmp.length; i++) {
                charsPW.print(tmp[i].getColorInd() + " ");
                charsPW.print(tmp[i].getFieldsCount() + " ");
                charsPW.print(tmp[i].getMiddleArea() + " ");
                charsPW.println(tmp[i].getMiddlePeriphery());
                charsPW.flush();
            }

            charsPW.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

package eyesimo.bankmanager.io;

import eyesimo.processor.segmentators.ImageColorChars;
import eyesimo.processor.segmentators.ImageFullChars;
import eyesimo.util.AnalystColors;

import java.io.*;

/**
 * Created by Anton_Erde on 12.03.2016.
 */
public class CharsReader implements AnalystColors{


    public ImageFullChars readFullInfo(File charsFile) {

        DataInputStream charsDIS = null;
        try {
            charsDIS = new DataInputStream(new BufferedInputStream(new FileInputStream(charsFile)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int imgSize[] = parseSize(charsDIS);

        ImageColorChars[] tmp = new ImageColorChars[colorCount];
        for (int i = 0; i < colorCount; i++) {
            tmp[i] = parseColorInfo(charsDIS);
        }

        try {
            charsDIS.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        String fileName = charsFile.getName().substring(0, charsFile.getName().length() - 4);

        return new ImageFullChars(fileName,imgSize[0], imgSize[1], tmp);
    }

    private int[] parseSize(DataInputStream di) {

        int[] vals = new int[2];

        byte arr[] = new byte[10];
        for (int i = 0; i < 2; i++) {

            try {
                if (di.available() == 0) return null;

            } catch (IOException e) {
                e.printStackTrace();
            }

            int count  = 0;
            byte temp = 0;

            while (true) {
                try {
                    temp = di.readByte();
                    if (temp == 32) break;
                    if (temp == 10) {
                    	//di.readByte();
                    	break;
                    }
                    if (temp == 13) {
                        di.readByte();
                        break;
                    }
                    if (di.available() == 0) {
                        arr[count] = temp;
                        count++;
                        break;
                    }

                    arr[count] = temp;
                    count++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                if (di.available() == 0) {
                    String str  = new String();
                    for (int j = 0; j < count; j++) str += (char)arr[j];

                    vals[i] = Integer.parseInt(str);
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            String str  = new String();
            for (int j = 0; j < count; j++) str += (char)arr[j];

            vals[i] = Integer.parseInt(str);
        }

        return vals;
    }

    private ImageColorChars parseColorInfo(DataInputStream di) {

        int[] vals = new int[4];

        byte arr[] = new byte[10];
        for (int i = 0; i < 4; i++) {

            try {
                if (di.available() == 0) return null;

            } catch (IOException e) {
                e.printStackTrace();
            }

            int count  = 0;
            byte temp = 0;

            while (true) {
                try {
                    temp = di.readByte();
                    if (temp == 32) break;
                    if (temp == 13) {
                        di.readByte();
                        break;
                    }
                    if (temp == 10 ) break;
                    if (di.available() == 0) {
                        arr[count] = temp;
                        count++;
                        break;
                    }

                    arr[count] = temp;
                    count++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                if (di.available() == 0) {
                    String str  = new String();
                    for (int j = 0; j < count; j++) str += (char)arr[j];

                    vals[i] = Integer.parseInt(str);
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            String str  = new String();
            for (int j = 0; j < count; j++) str += (char)arr[j];

            vals[i] = Integer.parseInt(str);
        }

        return new ImageColorChars(vals[0], vals[1], vals[2], vals[3]);
    }

    public String[] getFileNamesInsideCatalogue(File charsDir) {

        String[] names = charsDir.list();

        for (int i = 0; i < names.length; i++) {
            names[i] = names[i].substring(0, names[i].length() - 4);
        }

        return names;
    }
}

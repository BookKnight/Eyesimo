package eyesimo.settingsmanager;

import java.io.File;
import java.io.IOException;

/**
 * Created by Anton_Erde on 10.03.2017.
 */
public class EyesimoSettings {

    private int k;
    private int stWidth;
    private int stHeight;

    private File imageBankDir;
    private File charsBankDir;
    private File backupsBankDir;

    public EyesimoSettings() {

        setK(24);
        setStWidth(300);
        setStHeight(300);

        setImageBankDir( new File ("./image-bank/" ) );
        setCharsBankDir( new File( "./characteristics-bank/" ) );
        setBackupsBankDir( new File( "./backups-bank/" ));

    }


    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public int getStWidth() {
        return stWidth;
    }

    public void setStWidth(int stWidth) {
        this.stWidth = stWidth;
    }

    public int getStHeight() {
        return stHeight;
    }

    public void setStHeight(int stHeight) {
        this.stHeight = stHeight;
    }

    public File getImageBankDir() {
        return imageBankDir;
    }

    public void setImageBankDir(File imageBankDir) {

        this.imageBankDir = imageBankDir;
    }

    public File getCharsBankDir() {
        return charsBankDir;
    }

    public void setCharsBankDir(File charsBankDir) {

        this.charsBankDir = charsBankDir;
    }

    public void setBackupsBankDir ( File backupsBankDir ) {
        this.backupsBankDir = backupsBankDir;
    }

    public File getBackupsBankDir() {
        return backupsBankDir;
    }

    public void printSettings() {

        System.out.println("k: " + k);
        System.out.println("Sizes: " + stWidth + " x " + stHeight);
        System.out.println("Image bank path: " + imageBankDir.getPath());
        System.out.println("Chars bank path: " + charsBankDir.getPath());
        System.out.println("Backups bank path: " + backupsBankDir.getPath());
    }
}

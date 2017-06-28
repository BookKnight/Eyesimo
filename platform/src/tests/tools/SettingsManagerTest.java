package tests.tools;

import eyesimo.settingsmanager.EyesimoSettings;
import eyesimo.settingsmanager.SettingsManager;

import java.io.File;

/**
 * Created by Anton_Erde on 13.03.2017.
 */
public class SettingsManagerTest extends SettingsManager implements Runnable{

    EyesimoSettings eyesimoSettings;

    public SettingsManagerTest() {

        super();

        eyesimoSettings = new EyesimoSettings();

        //setTestSettings();

        Thread testSettingsManagerThread = new Thread(this);
        testSettingsManagerThread.start();

    }

    @Override
    final protected void setTestSettings() {

        super.eyesimoHomeDir = "./platform/src/tests/";

        eyesimoSettings.setK(30);
        eyesimoSettings.setCharsBankDir( new File(super.eyesimoHomeDir + "/Test Charsbank/" ) );
        eyesimoSettings.setImageBankDir( new File(super.eyesimoHomeDir + "/Test Imagebank/" ) );
        eyesimoSettings.setStWidth(5000);
        eyesimoSettings.setStHeight(10000);

    }

    @Override
    public void run() {

        super.writeSettings( this.eyesimoSettings );

        super.readSettings();
    }


    public static void main(String[] args) {

        SettingsManagerTest settingsManagerTest = new SettingsManagerTest();

    }

}

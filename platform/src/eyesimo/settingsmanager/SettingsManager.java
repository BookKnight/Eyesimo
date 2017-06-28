package eyesimo.settingsmanager;

import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;
import com.sun.webkit.dom.NodeListImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class SettingsManager {

    protected String eyesimoHomeDir;
    protected String settingsFileName;

    public SettingsManager() {

        eyesimoHomeDir = "/home/egorchnegov/MyProgs/workspace/Eyesimo/";
        settingsFileName = "settings.xml";

    }

    public void writeSettings(EyesimoSettings eyesimoSettings) {

        try {

            //create settings document as DOM object
            DocumentBuilderFactoryImpl documentBuilderFactory = new DocumentBuilderFactoryImpl();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document settingsDocument = documentBuilder.newDocument();

            //create root element
            Element rootElement = settingsDocument.createElement("eyesiomoSettings");
            settingsDocument.appendChild(rootElement);

            //append k neighbours parameter
            Element neighbours = settingsDocument.createElement("kNeighbours");
            Integer kNei = eyesimoSettings.getK();
            neighbours.appendChild( settingsDocument.createTextNode( kNei.toString() ));
            rootElement.appendChild( neighbours );

            //append standard width of images
            Element stWidth = settingsDocument.createElement("stWidth");
            Integer stW   = eyesimoSettings.getStWidth();
            stWidth.appendChild( settingsDocument.createTextNode( stW.toString() ));
            rootElement.appendChild(stWidth);

            //append standard height of images
            Element stHeight = settingsDocument.createElement("stHeight");
            Integer stH   = eyesimoSettings.getStHeight();
            stHeight.appendChild( settingsDocument.createTextNode( stH.toString() ));
            rootElement.appendChild(stHeight);

            //append path of image bank
            Element imageBankPath = settingsDocument.createElement("imageBankPath");
            String imgPath = eyesimoSettings.getImageBankDir().getPath();
            imageBankPath.appendChild( settingsDocument.createTextNode( imgPath ));
            rootElement.appendChild( imageBankPath );

            //append path of characteristics bank
            Element charsBankPath = settingsDocument.createElement("charsBankPath");
            String charsPath = eyesimoSettings.getCharsBankDir().getPath();
            charsBankPath.appendChild( settingsDocument.createTextNode( charsPath ));
            rootElement.appendChild( charsBankPath );

            //append path of backups bank
            Element backupsBankPath = settingsDocument.createElement("backupsBankPath");
            String backupsPath = eyesimoSettings.getBackupsBankDir().getPath();
            backupsBankPath.appendChild( settingsDocument.createTextNode( backupsPath ));
            rootElement.appendChild( backupsBankPath );

            //create settings file and write there settings
            TransformerFactoryImpl transformerFactory = new TransformerFactoryImpl();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource srcDoc = new DOMSource( settingsDocument );
            StreamResult result = new StreamResult( new File(eyesimoHomeDir + settingsFileName) );
            transformer.transform(srcDoc, result);



        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public EyesimoSettings readSettings() {

        EyesimoSettings readedSettings = new EyesimoSettings();

        try {
            File settingsFile = new File(eyesimoHomeDir + "settings.xml");

            if ( !settingsFile.exists() ) writeSettings( readedSettings );

            DocumentBuilderFactoryImpl documentBuilderFactory = new DocumentBuilderFactoryImpl();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document settingsDocument = documentBuilder.parse( settingsFile );

            settingsDocument.getDocumentElement().normalize();

            Element rootElem = settingsDocument.getDocumentElement();

            NodeList nodeList = rootElem.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {

                String readedTextData = null;
                String nodeName = nodeList.item(i).getNodeName();

                switch ( nodeName ) {

                    case "kNeighbours" :
                        readedTextData = nodeList.item(i).getFirstChild().getTextContent();
                        readedSettings.setK( Integer.parseInt( readedTextData ) );
                        break;
                    case "stWidth" :
                        readedTextData = nodeList.item(i).getFirstChild().getTextContent();
                        readedSettings.setStWidth( Integer.parseInt( readedTextData ) );
                        break;
                    case "stHeight" :
                        readedTextData = nodeList.item(i).getFirstChild().getTextContent();
                        readedSettings.setStHeight( Integer.parseInt( readedTextData ) );
                        break;
                    case "imageBankPath" :
                        readedTextData = nodeList.item(i).getFirstChild().getTextContent();
                        readedSettings.setImageBankDir( new File( readedTextData ) );
                        break;
                    case "charsBankPath" :
                        readedTextData = nodeList.item(i).getFirstChild().getTextContent();
                        readedSettings.setCharsBankDir( new File( readedTextData ) );
                        break;
                    case "backupsBankPath" :
                        readedTextData = nodeList.item(i).getFirstChild().getTextContent();
                        readedSettings.setBackupsBankDir( new File( readedTextData ) );
                        break;
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return readedSettings;
    }

    protected void setTestSettings(){};
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitario;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Glory
 */
public class Utilitario {

    public static String contexto = "";

    public static void main(String argv[]) {

        try {
            File fXmlFile = new File("C:/Temp/R-20498455370-01-F003-00056647.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            System.out.println("rrrr" + findElement(doc, "cbc:ResponseCode"));
            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work

            /*          DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
             .newDocumentBuilder();

             Document doc = dBuilder.parse(fXmlFile);
             */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String findElement(Document doc, String elementSearch) {
        String valor = "";
        doc.getDocumentElement().getNodeName();
        Boolean flag = true;
        if (doc.hasChildNodes() && flag == true) {

            if (flag == true) {
                valor = searchNode(doc.getChildNodes(), elementSearch);
            }
            if (!valor.equals("")) {
                flag = false;

            }
        }
        return valor;
    }

    private static String searchNode(NodeList nodeList, String elementoBuscar) {

        for (int count = 0; count < nodeList.getLength(); count++) {
            Node tempNode = nodeList.item(count);
            // make sure it's element node.
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                if (tempNode.getNodeName().equals(elementoBuscar)) {
                    System.out.println("Node Value rpta =" + tempNode.getTextContent());
                    contexto = tempNode.getTextContent();
                }
                if (tempNode.hasChildNodes()) {
                    // loop again if has child nodes
                    searchNode(tempNode.getChildNodes(), elementoBuscar);
                }
            }
        }
        return contexto;
    }

    public static String searchAnswer(byte[] resultado, String node) {
        String rpta = "";
        try {
            ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(resultado));
            ZipEntry entry = null;
            //  byte[] resu = null;
            while ((entry = zipStream.getNextEntry()) != null) {

                // String entryName = entry.getName();
                ByteArrayOutputStream bos = new ByteArrayOutputStream(entry.getName().length());
                //  FileOutputStream out = new FileOutputStream(entryName);
                byte[] byteBuff = new byte[4096];
                int bytesRead = 0;
                while ((bytesRead = zipStream.read(byteBuff)) != -1) {
                    bos.write(byteBuff, 0, bytesRead);
                    //  out.write(byteBuff, 0, bytesRead);
                }
                bos.close();
                byte[] docBytes = bos.toByteArray();
                String roundTrip = new String(bos.toByteArray(), "UTF8");

                if (!roundTrip.equals("")) {
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    dbf.setNamespaceAware(true);
                    InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
                    //ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());
                    Document doc = dbf.newDocumentBuilder().parse(inputStream);
                    rpta = findElement(doc, node);
                }
                zipStream.closeEntry();
            }
            zipStream.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        }
        return rpta;
    }

    public static String searchAnswer(String ruta, String nombrearchivo, String codigo) {
        String rpta = "-1";
        try {

            File file = new File(ruta);
            System.out.println("Mostar CPE ... " + ruta);
            if (file.exists()) {
                ZipInputStream zipStream = new ZipInputStream(new FileInputStream(ruta));
                ZipEntry entry = null;
                //  byte[] resu = null;
                while ((entry = zipStream.getNextEntry()) != null) {

                    // String entryName = entry.getName();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream(entry.getName().length());
                    //  FileOutputStream out = new FileOutputStream(entryName);
                    byte[] byteBuff = new byte[4096];
                    int bytesRead = 0;
                    while ((bytesRead = zipStream.read(byteBuff)) != -1) {
                        bos.write(byteBuff, 0, bytesRead);
                        //  out.write(byteBuff, 0, bytesRead);
                    }
                    bos.close();
                    byte[] docBytes = bos.toByteArray();
                    String roundTrip = new String(bos.toByteArray(), "UTF8");
                    if (!roundTrip.equals("")) {
                        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                        dbf.setNamespaceAware(true);
                        InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
                        //ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());
                        Document doc = dbf.newDocumentBuilder().parse(inputStream);
                        rpta = Utilitario.findElement(doc, codigo);
                    }
                    zipStream.closeEntry();
                }
                zipStream.close();
            }

        } catch (Exception ex) {
            System.out.println(String.valueOf(ex.getMessage()));
            ex.printStackTrace();
        }
        return rpta;
    }

    public static void unZipIt(String zipFile, String outputFolder) {

        byte[] buffer = new byte[1024];

        try {

            //create output directory is not exists
            File folder = new File(outputFolder);
            if (!folder.exists()) {
                folder.mkdir();
            }

            //get the zip file content
            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {

                String fileName = ze.getName();
                File newFile = new File(outputFolder + File.separator + fileName);

                System.out.println("file unzip : " + newFile.getAbsoluteFile());

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

            System.out.println("Done");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Boolean validateEmail(String email) {

        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(email);
        return mather.find() == true;
    }

    public static String ubicacionProyecto() {
        String ruta_retur = "";
        try {

            String ruta = new File(Utilitario.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();//System.getProperties().getProperty("user.dir");

            System.out.println(ruta);
            //   out.println(ruta);

            int a = 0;
            for (int i = 0; i < ruta.length(); i++) {
                if (ruta.charAt((ruta.length() - 1) - i) == '\\') {
                    ruta_retur = ruta.substring(0, (ruta.length() - 1) - i);
                    System.out.println(ruta_retur);
                    if (a > 1) {
                        break;
                    }
                    a++;
                }
            }

        } catch (URISyntaxException ex) {
            Logger.getLogger(Utilitario.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return ruta_retur;
        }

    }

}

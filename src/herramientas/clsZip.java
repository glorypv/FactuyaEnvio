package herramientas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class clsZip {

    private clsConexion varClsConexion;

    public clsZip() {
        varClsConexion = new clsConexion();
    }

    public static void zipFolder(ZipOutputStream zipOutputStream, File inputFolder, String parentName) throws IOException {

        String myname = parentName + inputFolder.getName() + "\\";

        ZipEntry folderZipEntry = new ZipEntry(myname);
        zipOutputStream.putNextEntry(folderZipEntry);

        File[] contents = inputFolder.listFiles();

        for (File f : contents) {
            if (f.isFile()) {
                zipFile(f, myname, zipOutputStream);
            } else if (f.isDirectory()) {
                zipFolder(zipOutputStream, f, myname);
            }
        }
        zipOutputStream.closeEntry();
    }

    public static void zipFile(File inputFile, String parentName, ZipOutputStream zipOutputStream) throws IOException {

        // A ZipEntry represents a file entry in the zip archive
        // We name the ZipEntry after the original file's name
        ZipEntry zipEntry = new ZipEntry(parentName + inputFile.getName());
        zipOutputStream.putNextEntry(zipEntry);

        FileInputStream fileInputStream = new FileInputStream(inputFile);
        byte[] buf = new byte[1024];
        int bytesRead;

        // Read the input file by chucks of 1024 bytes
        // and write the read bytes to the zip stream
        while ((bytesRead = fileInputStream.read(buf)) > 0) {
            zipOutputStream.write(buf, 0, bytesRead);
        }

        // close ZipEntry to store the stream to the file
        zipOutputStream.closeEntry();
        fileInputStream.close();
        System.out.println("file:" + parentName + inputFile.getName());
    }

    public static void zip(String inputFolder, String targetZippedFolder) throws IOException {

        FileOutputStream fileOutputStream = null;

        fileOutputStream = new FileOutputStream(targetZippedFolder);
        ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

        File inputFile = new File(inputFolder);

        if (inputFile.isFile()) {
            zipFile(inputFile, "", zipOutputStream);
        } else if (inputFile.isDirectory()) {
            zipFolder(zipOutputStream, inputFile, "");
        }

        zipOutputStream.close();
        fileOutputStream.close();
    }

    public String metZipIncrementalArbol(String varDirId) {
        String resultado = "-1";
        String varDirPath = varDirId;
        String rutaFolder, rutaZip;
        System.out.println("cc " + varDirId);
        try {
            URL resource = getClass().getResource("/");
            String root = resource.getPath();
            root = root.substring(1);
            root = root.replace("%20", " ");
            root = root.replace("WEB-INF/classes/", "");
            System.out.println("root " + root);

            System.out.println("path " + varDirPath);

            rutaFolder = varDirPath;// root + "../../../../../home/sgp-pid/" + varDirPath;
            // rutaFolder = rutaFolder.substring(0, rutaFolder.length() - 1);
            System.out.println("ruta folder" + rutaFolder.trim().substring(0, rutaFolder.length() - 5));
            rutaZip = rutaFolder.trim().substring(0, rutaFolder.length() - 5) + ".zip";

            zip(rutaFolder, rutaZip);
            resultado = "1";
            System.out.println("termino zip");
            File fichero = new File(rutaFolder);
            eliminarFichero(fichero);
            System.out.println("Eliminado " + rutaFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public static void eliminarFichero(File fichero) {

        if (!fichero.exists()) {
            System.out.println("El archivo data no existe.");
        } else {
            fichero.delete();
            System.out.println("El archivo data fue eliminado.");
        }

    }
}

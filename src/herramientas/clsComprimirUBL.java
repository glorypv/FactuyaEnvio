/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package herramientas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author Glory
 */
public class clsComprimirUBL {

    public void compressXMLFile(File entrada, File salida) {
        try {
            FileInputStream in = new FileInputStream(entrada);
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(salida));

            // name the file inside the zip  file 
            System.out.println(entrada.getName());
            out.putNextEntry(new ZipEntry(entrada.getName()));

            // buffer size
            // byte[] b = new byte[1024];
            byte[] b = new byte[4096];
            int count;
            while ((count = in.read(b)) > 0) {
                System.out.println();
                out.write(b, 0, count);
            }
            out.close();
            in.close();

        } catch (Exception e) {
            //JOptionPane.showMessageDialog(this, "Lo sentimos, ha ocurrido un error.\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void comprimirXMLByte(byte[] entrada, String name, File fileZip) {

        try {
            InputStream inn = new ByteArrayInputStream(entrada);
            BufferedReader in = new BufferedReader(new InputStreamReader(inn));

            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(fileZip));
            // ZipOutputStream out = new ZipOutputStream(new FileOutputStream(salida));
            Writer writer = new BufferedWriter(new OutputStreamWriter(out, Charset.forName("ISO-8859-1")));
            // name the file inside the zip  file 
            System.out.println(name);
            out.putNextEntry(new ZipEntry(name));

            String line = null;
            while ((line = in.readLine()) != null) {
                writer.append(line).append('\n');
            }
            writer.flush(); // i've used a buffered writer, so make sure to flush to the
            // underlying zip output stream

            out.closeEntry();
            out.finish();

            in.close();
            writer.close();

        } catch (Exception e) {
            //JOptionPane.showMessageDialog(this, "Lo sentimos, ha ocurrido un error.\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            /* if (Profile.getInstance().getSendType() == false) {

             Main.invoice.dP.p.getTxtarea().append("Error  " + e.getMessage() + "\n");
             Main.invoice.dP.p.getTxtarea().setCaretPosition(Main.invoice.dP.p.getTxtarea().getDocument().getLength());
             }*/
            e.printStackTrace();
        }
    }
}

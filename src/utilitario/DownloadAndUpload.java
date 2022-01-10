/*
 * Creado por: GLORIA PERALTA VILLASANTE
 * Correo: gloria.ypv@gmail.com
 */
/*
 * dia_usu_insert.java
 *                   
 * Created on 27/01/2011, 12:20:52 PM
 */
package utilitario;

import com.jcraft.jsch.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;

public class DownloadAndUpload {

    private static String user = "";
    private static String host = "";
    private static Integer port = null;
    private static String pass = "";
    public String msj = "";
    private static Session session;
    private static Channel channel;

    public String beginSFP(String host, String puerto, String usuario, String contraseña) {
        try {
            this.host = host;
            this.port = Integer.parseInt(puerto);
            this.user = usuario;
            this.pass = contraseña;

            JSch ssh = new JSch();
            session = ssh.getSession(user, host, port);
            // Remember that this is just for testing and we need a quick access, you can add an identity and known_hosts file to prevent
            // Man In the Middle attacks

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword(pass);

            session.connect();
            if (session != null) {
                channel = session.openChannel("sftp");
                if (channel != null) {

                    msj = "\n ... CONECTAR A SERVIDOR ... \n";
                    channel.connect();
                }
            }

        } catch (JSchException e) {
            msj = e.getMessage().toString();
            System.out.println(e.getMessage().toString());
            session.disconnect();
            /*if (Main.invoice.dP != null) {
             Main.invoice.dP.p.getTxtarea().append("Error  " + e.getMessage() + "\n");
             Main.invoice.dP.p.getTxtarea().setCaretPosition(Main.invoice.dP.p.getTxtarea().getDocument().getLength());
             }*/
            e.printStackTrace();
        } finally {
            return msj;
        }
    }

    public String endSFP() {
        if (channel != null) {
            channel.disconnect();
            session.disconnect();
            msj = "\n ... CERRAR CONEXION A SERVIDOR ... \n";
        }
        return msj;
    }

    public void downloadFileSFP(String dirRemote, String fileRemote, String dirDownload) {
        try {
            ChannelSftp sftp = (ChannelSftp) channel;

            sftp.cd(dirRemote);
            System.out.println("Dir Remote" + dirRemote);
            System.out.println("Dir Download" + dirDownload);

            sftp.get(fileRemote, dirDownload);
            Boolean success = true;

            if (success) {
                msj = "Archivo descargado con  ... " + fileRemote;
            }
        } catch (SftpException e) {
            /*  if (Main.invoice.dP != null) {
             Main.invoice.dP.p.getTxtarea().append("Error  " + e.getMessage() + "\n");
             Main.invoice.dP.p.getTxtarea().setCaretPosition(Main.invoice.dP.p.getTxtarea().getDocument().getLength());
             }*/
            System.out.println(e.getMessage().toString());
            e.printStackTrace();
        }
    }

    public Boolean moveFile(String nameFile, String dirSendRpta, File dirDownload, String ubicacionServidor) {

        String locate = ubicacionServidor;
        File file = new File(locate + dirSendRpta, nameFile);
        System.out.println("Dir Download" + dirDownload);

        if (file.exists()) {
            try {
                Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(dirDownload.getAbsolutePath() + "/" + file.getName()), StandardCopyOption.REPLACE_EXISTING);//poner rutaaaaaaaaa

            } catch (IOException e) {
                System.err.println(e);
                /* if (Main.invoice.dP != null) {
                 Main.invoice.dP.p.getTxtarea().append("Error  " + e.getMessage() + "\n");
                 Main.invoice.dP.p.getTxtarea().setCaretPosition(Main.invoice.dP.p.getTxtarea().getDocument().getLength());
                 }*/
            }
            return true;
        }
        return false;
    }

    public void copyFile(String origenArchivo, String destinoArchivo) {
        try {
            Path origenPath = Paths.get(origenArchivo);
            Path destinoPath = Paths.get(destinoArchivo);
            //sobreescribir el fichero de destino si existe y lo copia
            Files.copy(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public String uploadSFTPs(String ubicacion, String ubicacionServidor, String ubicacionEnvio, String ubicacionRespuesta) {
        try {

            File SunatSend = new File(ubicacion + ubicacionEnvio);
            File SunatAnwser = new File(ubicacion + ubicacionRespuesta);
            /*
             SunatSend.mkdir();
             SunatAnwser.mkdir();
             */
            if (channel != null) {

                String dirSend = SunatSend.getPath();
                String[] listFileSend = new File(dirSend).list(new Filtre(".zip"));
                int numFileSend = listFileSend.length;

                String dirAnwser = SunatAnwser.getPath();
                String[] listFileAnswer = new File(dirAnwser).list(new Filtre(".zip"));
                int numFileAnwser = listFileAnswer.length;
                ChannelSftp sftp = (ChannelSftp) channel;

                msj = msj + "\n ... INICIAR SUBIDA A SERVIDOR ... \n";
                /*  if (Main.invoice.dP != null) {
                 Main.invoice.dP.p.getTxtarea().append("\n ... INICIAR SUBIDA A SERVIDOR ... \n");
                 Main.invoice.dP.p.getTxtarea().setCaretPosition(Main.invoice.dP.p.getTxtarea().getDocument().getLength());
                 }*/
                for (int i = 0; i < numFileSend; i++) {
                    File file = new File(dirSend, listFileSend[i]);
                    if (file.exists()) {
                        // System.out.println(Profile.getInstance().getLocateServer() + Main.pathSunatSend);
                        sftp.cd(ubicacionServidor + ubicacionEnvio);
                        System.out.println(ubicacionServidor + ubicacionEnvio);

                        sftp.put(dirSend + "/" + file.getName(), ubicacionServidor + ubicacionEnvio + file.getName());
                       // sftp.put(dirSend + "/" + file.getName(), file.getName());//poner rutaaaaaaaaa

                        //  sftp.chmod(Integer.parseInt("0777", 8), ubicacionServidor + ubicacionEnvio + file.getName());
                        System.out.println("... Subiendo ENVIO ..." + file.getName());
                        msj = msj + "... Subiendo ENVIO ..." + file.getName() + "\n";
                        /*    if (Main.invoice.dP != null) {
                         msj =msj+
                         Main.invoice.dP.p.getTxtarea().append("... Subiendo ENVIO ..." + file.getName() + "\n");
                         Main.invoice.dP.p.getTxtarea().setCaretPosition(Main.invoice.dP.p.getTxtarea().getDocument().getLength());
                         }*/
                        File filen = new File(dirSend, file.getName());
                        if (filen.exists()) {
                            filen.delete();
                        }
                        file.renameTo(filen);

                    }
                }

                for (int i = 0; i < numFileAnwser; i++) {
                    File file = new File(dirAnwser, listFileAnswer[i]);
                    if (file.exists()) {
                        sftp.cd(ubicacionServidor + ubicacionRespuesta);
                        sftp.put(dirAnwser + "/" + file.getName(), file.getName());//poner rutaaaaaaaaa
                        sftp.chmod(Integer.parseInt("0777", 8), ubicacionServidor + ubicacionRespuesta + file.getName());
                        msj = msj + "... Subiendo RPTA ..." + file.getName() + "\n";
                        /*   if (Main.invoice.dP != null) {
                         Main.invoice.dP.p.getTxtarea().append("... Subiendo ENVIO ..." + file.getName() + "\n");
                         Main.invoice.dP.p.getTxtarea().setCaretPosition(Main.invoice.dP.p.getTxtarea().getDocument().getLength());
                         }*/
                        File filen = new File(dirAnwser, file.getName());
                        if (filen.exists()) {
                            filen.delete();
                        }
                        file.renameTo(filen);

                    }
                }
                msj = msj + "\n... FINALIZAR SUBIDA A SERVIDOR ... \n";
                /*  if (Main.invoice.dP != null) {
                 Main.invoice.dP.p.getTxtarea().append("\n... FINALIZAR SUBIDA A SERVIDOR ... \n");
                 Main.invoice.dP.p.getTxtarea().setCaretPosition(Main.invoice.dP.p.getTxtarea().getDocument().getLength());
                 }*/
            }
        } catch (SftpException ex) {
            msj = msj + "Error  " + ex.getMessage() + "\n";
            /*    if (Main.invoice.dP != null) {
             Main.invoice.dP.p.getTxtarea().append("Error  " + ex.getMessage() + "\n");
             Main.invoice.dP.p.getTxtarea().setCaretPosition(Main.invoice.dP.p.getTxtarea().getDocument().getLength());
             }*/
            java.util.logging.Logger.getLogger(DownloadAndUpload.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return msj;
        }
    }

}

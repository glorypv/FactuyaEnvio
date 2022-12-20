/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import factuyaenvio.herramientas.clsParametrosFactuya;
import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import main.utilitario.DownloadAndUpload;
import main.utilitario.Utilitario;
import main.utilitario.clsEnviarCorreo;

/**
 *
 * @author Gloria Peralta <gloria.ypv@gmail.com>
 */
public class clsEnviarCorreoPendientes {

    private static clsParametrosFactuya parametrosFactuya;
    private static String origen;
    private static String usuario;
    private static Connection conPostgres;

    public clsEnviarCorreoPendientes(String origen, String usuario, Connection conPostgres, clsParametrosFactuya parametrosFactuya) {
        this.origen = origen;
        this.usuario = usuario;
        this.conPostgres = conPostgres;
        this.parametrosFactuya = parametrosFactuya;
    }

    public static void enviarCorreoPendientes() {

        try {
            String mensaje = "";
            String dirRemote = parametrosFactuya.ubicacionServidor;
            String dirTemporal = parametrosFactuya.ubicacionSunatTemporal;
            Boolean download = false;
            String rpta = "";
            String idInvoiceSend = "";

            CallableStatement callsec;
            ResultSet rssec;
            PreparedStatement pstsec = null;

            new File(dirRemote + dirTemporal).mkdir();
            new File(dirRemote + dirTemporal + usuario).mkdir();
            new File(dirRemote + dirTemporal + usuario + "/CORREO/").mkdir();
            String dirDownload = dirRemote + dirTemporal + usuario + "/CORREO/";

            File file = new File(dirDownload);

            if (file.exists()) {
                File[] listOfFiles = file.listFiles();
                for (int i = 0; i < listOfFiles.length; i++) {
                    if (listOfFiles[i].isFile()) {
                        String files = listOfFiles[i].getName();
                        // System.out.println(files);
                        boolean issuccess = new File(listOfFiles[i].toString()).delete();
                        System.out.println("Deletion Success " + files);

                    }
                }
            }

            mensaje = mensaje + "\n--- INICIANDO DESCARGA --- \n";

            System.out.println("\n INICIO DE ENVIO CORREO \n");
            String sql = "";

            sql = "SELECT * FROM " + parametrosFactuya.nombreEsquema + ".fun_fel_consulta_facturacionelectronica_pendiente_email ("
                    + "'" + parametrosFactuya.numerRUC + "')";
            System.out.println(sql);
            callsec = conPostgres.prepareCall(sql);
            if (callsec.execute()) {
                rssec = callsec.getResultSet();
                if (rssec != null) {
                    while (rssec.next()) {
                        String email = rssec.getString("ent_email");
                        String filepdf = rssec.getString("emp_ruc") + "-" + rssec.getString("cvt_codigo") + "-" + rssec.getString("cvc_serie") + "-" + rssec.getString("cvc_numero");
                        String filexml = rssec.getString("fel_rutaxml");
                        String filecdr = rssec.getString("fel_rutacdr");
                        String estado = rssec.getString("estadofel");
                        String idInvoice = rssec.getString("cvt_codigo") + " " + rssec.getString("cvc_serie") + "-" + rssec.getString("cvc_numero");
                        Integer idComprobante = rssec.getInt("cvc_id");
                        if (!email.equals("") && estado.equals("ACEPTADO")) {
                            DownloadAndUpload down = new DownloadAndUpload();

                            String nameFormat = parametrosFactuya.numerRUC + rssec.getString("cvt_codigo");
                            String nameFile = filepdf;
                            Utilitario.exportarComprobantePDF(nameFormat, idComprobante, dirDownload, nameFile, ".pdf", parametrosFactuya.ubicacionFormatos, parametrosFactuya.ubicacionImagenes, conPostgres);

                            if (!filexml.equals("")) {

                                down.moveFile(filexml, parametrosFactuya.ubicacionSunatEnvio, new File(dirDownload), parametrosFactuya.ubicacionServidor);
                                download = true;

                                Utilitario.unZipIt(dirDownload + filexml, dirDownload);
                                filexml = filexml.replace("zip", "xml");
                                System.out.println(filexml);
                            }
                            if (!filecdr.equals("")) {
                                down.moveFile(filecdr, parametrosFactuya.ubicacionSunatRespuesta, new File(dirDownload), parametrosFactuya.ubicacionServidor);
                                download = true;

                                Utilitario.unZipIt(dirDownload + filecdr, dirDownload);
                                filecdr = filexml.replace("zip", "xml");
                                System.out.println(filecdr);
                                filecdr = filecdr.replace("-R.", ".");
                                filecdr = "R-" + filecdr;
                            }
                            String file1 = "";
                            String file2 = "";
                            String file3 = "";
                            int count = 0;

                            if (new File(dirDownload + nameFile + ".pdf").exists()) {
                                file1 = dirDownload + nameFile + ".pdf";
                            }

                            if (new File(dirDownload + filexml).exists()) {
                                file2 = dirDownload + filexml;
                                count = count + 1;
                            }
                            if (new File(dirDownload + filecdr).exists()) {
                                file3 = dirDownload + filecdr;
                                count = count + 1;
                            } else {
                                if (estado.equals("ACEPTADO")) {
                                    filecdr = "R-" + filexml;
                                    down.copyFile(dirDownload + filexml, dirDownload + filecdr);
                                    file3 = dirDownload + filecdr;
                                    count = count + 1;
                                }
                            }

                            String[] fileAttach = {file1, file2, file3};

                            if (!file1.equals("") && !file2.equals("") && !file3.equals("")) {

                                String nameCustomer = rssec.getString("ent_nombre");
                                String idCustomer = rssec.getString("ent_nrodocumento");
                                String nameSupplier = rssec.getString("emp_razonsocial");
                                String idSupplier = rssec.getString("emp_ruc");
                                String date = rssec.getString("cvc_fechaemision");
                                String dateTransmission = rssec.getString("fel_fechaenvio");
                                String total = rssec.getString("cvc_total");
                                String pagWeb = rssec.getString("emp_paginaweb");
                                String ubicacionPrincial = parametrosFactuya.ubicacionPrincipal;
                                String hostCorreo = parametrosFactuya.hostCorreo;
                                String portCorreo = parametrosFactuya.puertoCorreo;
                                String usuarioCorreo = parametrosFactuya.usuarioCorreo;
                                String passwordCorreo = parametrosFactuya.passwordCorreo;
                                String usuarioCorreoRemitente=parametrosFactuya.usuarioCorreoRemitente;

                                clsEnviarCorreo enviarCorreo = new clsEnviarCorreo(
                                        idInvoice,
                                        email,
                                        fileAttach,
                                        nameCustomer,
                                        idCustomer,
                                        nameSupplier,
                                        idSupplier,
                                        date,
                                        dateTransmission,
                                        total,
                                        pagWeb,
                                        ubicacionPrincial,
                                        hostCorreo,
                                        portCorreo,
                                        usuarioCorreo,
                                        passwordCorreo,
                                usuarioCorreoRemitente);
                                Boolean envioCorrecto = enviarCorreo.enviarCorreo();

                                if (envioCorrecto) {
                                    idInvoiceSend = idInvoiceSend + idComprobante + ",";
                                }

                            } else {

                                System.out.println("... No existe archivo(s) para el envio. (PDF o XML o CDR) ..." + idInvoice + ".\n");
                                mensaje = mensaje + "... No existe archivo(s) para el envio. (PDF o XML o CDR) ..." + idInvoice + ".\n";
                            }

                        } else {
                            if (!estado.equals("ACEPTADO")) {
                                mensaje = mensaje + "... Comprobante no ha sido aceptado: " + idInvoice + ".\n";

                            }
                            if (email.equals("")) {
                                mensaje = mensaje + "... No se encontro correo electronico  " + idInvoice + ".\n";

                            }
                        }

                    }
                }
            }

            if (!idInvoiceSend.equals("")) {
                sql = "UPDATE " + parametrosFactuya.nombreEsquemaCPE + ".tblfacturacionelectronica_fel \n"
                        + "SET \n"
                        + "  fel_fechaenvioemail =now()\n"
                        + "WHERE  fel_iddocreferencia in (" + idInvoiceSend.substring(0, idInvoiceSend.length() - 1) + ")";
                System.out.println(sql);
                pstsec = conPostgres.prepareStatement(sql);
                pstsec.executeUpdate();
                pstsec.close();
            }

            mensaje = mensaje + "\n--- FIN DESCARGA --- \n";
            System.out.println("\n FIN DE ENVIO CORREO \n");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();

            System.out.println(e.getMessage());

        }
    }

}

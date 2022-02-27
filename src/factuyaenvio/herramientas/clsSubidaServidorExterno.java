/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package factuyaenvio.herramientas;

import factuya.factura.clsFactura;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import factuyaenvio.utilitario.DownloadAndUpload;
import factuyaenvio.utilitario.Utilitario;
import java.sql.CallableStatement;

/**
 *
 * @author Gloria Peralta <gloria.ypv@gmail.com>
 */
public class clsSubidaServidorExterno {

    public static String mensaje = "";

    private static String ubicacionServidorExternoTemp = "";
    private static String nombreArchivoXML = "";
    private static String nombreEsquema = "";
   
    private static String nombreEsquemaCPE = ""; 
    private static clsFactura factura;
    private static Connection con;
    private static Connection conExterna;
    private static String usuario;
    private static Integer idComprobante = null;
    private static String tipoComprobante = "";
    private static String ruc = "";

    public static void main(String[] args) {

    }

    public clsSubidaServidorExterno(String nombreArchivoXML, String ruc, String usuario, Integer idComprobante, String tipoComprobante, Connection con) {
        this.ruc = ruc;
        this.usuario = usuario;
        this.nombreArchivoXML = nombreArchivoXML;
        this.idComprobante = idComprobante;
        this.tipoComprobante = tipoComprobante;
        this.con = con;

    }

    public clsSubidaServidorExterno(String ubicacionServidorExternoTemp, String ruc, String usuario, String nombreEsquema,String nombreEsquemaCPE, Connection con, Connection conExterna) {
        this.ubicacionServidorExternoTemp = ubicacionServidorExternoTemp;
        this.ruc = ruc;
        this.factura = factura;
        this.usuario = usuario;
        this.nombreEsquema = nombreEsquema;
        this.nombreEsquemaCPE=nombreEsquemaCPE;
        this.con = con;
        this.conExterna = conExterna;

    }

    public void crearUbicacion(String ubicacionPrincipal) {

        new File(ubicacionPrincipal).mkdir();
        new File(ubicacionPrincipal + usuario).mkdir();
        new File(ubicacionPrincipal + usuario + "/SUNAT/").mkdir();
        new File(ubicacionPrincipal + usuario + "/SUNAT/ENVIO/").mkdir();
        new File(ubicacionPrincipal + usuario + "/SUNAT/RPTA/").mkdir();
        new File(ubicacionPrincipal + usuario + "/SUNAT/PDF/").mkdir();
        ubicacionServidorExternoTemp = ubicacionPrincipal + usuario;

    }

    public void copiarParaServidorExterno(Boolean subirServidor, String ubicacionPrincipal, String ubicacionServidor, String ubicacionSunatEnvio, String ubicacionSunatRespuesta, String ubicacionFormatos, String ubicacionImagenes) {

        /**
         * ***************** SUBIR A SERVIDOR EXTERNO
         * *********************************************************************************
         */
        String ubicacionZip = subirServidor == true ? ubicacionPrincipal + ubicacionSunatEnvio : ubicacionServidor + ubicacionSunatEnvio;
        String ubicacionZipRespuesta = subirServidor == true ? ubicacionPrincipal + ubicacionSunatRespuesta : ubicacionServidor + ubicacionSunatRespuesta;
        String nombreArchivoZip = nombreArchivoXML + ".zip";
        String nombreArvhicoRespuestaZip = nombreArchivoXML + "-R.zip";

        DownloadAndUpload copiarATemp = new DownloadAndUpload();
        copiarATemp.copyFile(ubicacionZip + nombreArchivoZip, ubicacionServidorExternoTemp + "/SUNAT/ENVIO/" + nombreArchivoZip);
        copiarATemp.copyFile(ubicacionZipRespuesta + nombreArvhicoRespuestaZip, ubicacionServidorExternoTemp + "/SUNAT/RPTA/" + nombreArvhicoRespuestaZip);

        String nameFormat = ruc + tipoComprobante;
        String nameFile = nombreArchivoXML;
        Utilitario.exportarComprobantePDF(nameFormat, idComprobante, ubicacionServidorExternoTemp + "/SUNAT/PDF/", nameFile, ".pdf", ubicacionFormatos, ubicacionImagenes, con);

        /**
         * ***************************************************************************************************************************
         */
    }

    public static void subirServidorExteno(String hostSHExterno, String puertoSHExterno, String usuarioSHExterno, String passwordSHExterno, String ubicacionServidorExterno) {
        mensaje = "";
        DownloadAndUpload copiarATemp = new DownloadAndUpload();
        mensaje = mensaje + copiarATemp.beginSFP(hostSHExterno, puertoSHExterno, usuarioSHExterno, passwordSHExterno);

        mensaje = mensaje + copiarATemp.uploadSFTPs(ubicacionServidorExternoTemp + "/SUNAT/ENVIO/", ".zip", ubicacionServidorExterno + "/SUNAT/ENVIO/");
        mensaje = mensaje + copiarATemp.uploadSFTPs(ubicacionServidorExternoTemp + "/SUNAT/RPTA/", ".zip", ubicacionServidorExterno + "/SUNAT/RPTA/");
        mensaje = mensaje + copiarATemp.uploadSFTPs(ubicacionServidorExternoTemp + "/SUNAT/PDF/", ".pdf", ubicacionServidorExterno + "/SUNAT/PDF/");

        mensaje = mensaje + copiarATemp.endSFP();
    }

    public static Boolean insertarServidorExterno() {

        Boolean rpta = false;
        CallableStatement call = null;

        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            // Enviar todo

            String idFel = "";
            String felInsertar = "";

            /* String sql = "SELECT\n"
             + "fel.fel_id,\n"
             + "'RUC' ruc,\n"
             + "emp.emp_ruc,\n"
             + "ent.dxt_codigo,\n"
             + "ent.ent_nrodocumento,\n"
             + "cvc.cvt_codigo,\n"
             + "cvc.cvc_serie,\n"
             + "cvc.cvc_numero,\n"
             + "cvc.cvc_fechaemision,\n"
             + "cvc.cvc_total,\n"
             + "'admin' usuario\n"
             + " FROM ventas.tblfacturacionelectronica_fel fel\n"
             + " INNER JOIN ventas.tblcomprobantesventascab_cvc cvc  ON (cvc.cvc_id=fel.fel_iddocreferencia)\n"
             + " INNER JOIN public.tblempresas_emp emp ON (cvc.emp_id = emp.emp_id)\n"
             + " INNER JOIN public.tblentidades_ent ent ON (cvc.ent_id = ent.ent_id AND emp.emp_id=ent.emp_id )\n"
             + " WHERE fel_fechasubidaservidor IS NULL\n"
             + " AND fee_id='ACE'"
             + " AND emp.emp_ruc='" + ruc + "'";*/
            String sql = "SELECT * FROM " + nombreEsquema + ".fun_fel_consultar_pendientes_subir_servidor_externo"
                    + "('" + ruc + "')";
            System.out.println(sql);
            call = con.prepareCall(sql);
            if (call.execute()) {
                rs = call.getResultSet();
                if (rs != null) {
                    while (rs.next()) {
                        idFel = idFel + rs.getString("fel_id") + ",";
                        felInsertar = felInsertar + "('" + rs.getString("tipodocumentoemisor") + "',"
                                + "'" + rs.getString("nrodocumentoemisor") + "',"
                                + "'" + rs.getString("tipodocumentoreceptor") + "',"
                                + "'" + rs.getString("nrodocumentoreceptor") + "',"
                                + "'" + rs.getString("tipodocumento") + "',"
                                + "'" + rs.getString("serie") + "',"
                                + "'" + rs.getString("numero") + "',"
                                + "'" + rs.getString("fechaemision") + "',"
                                + "'" + rs.getString("total") + "',"
                                + "'" + usuario + "'),";

                    }
                }
            }
            rs.close();
            call.close();
            if (!idFel.equals("")) {
                clsConexion.inicarTransaccion(conExterna);
                clsConexion.inicarTransaccion(con);
                sql = "INSERT INTO \n"
                        + " ventas.tblfacturacionelectronicaservidor_fes\n"
                        + "(\n"
                        + "  fes_tipodocumentoemisor,\n"
                        + "  fes_nrodocumentoemisor,\n"
                        + "  fes_tipodocumentoreceptor,\n"
                        + "  fes_nrodocumentoreceptor,\n"
                        + "  fes_tipodocumento,\n"
                        + "  fes_serie,\n"
                        + "  fes_numero,\n"
                        + "  fes_fechaemision,\n"
                        + "  fes_total,\n"
                        + "  fes_usucreacion )\n"
                        + "  VALUES "
                        + felInsertar.substring(0, felInsertar.length() - 1)
                        + "  ON CONFLICT ON CONSTRAINT tblfacturacionelectronicaservidor_fes_idx \n"
                        + "  DO NOTHING";
                System.out.println(sql);
                st = conExterna.prepareStatement(sql);
                st.executeUpdate();
                st.close();

                sql = "UPDATE " + nombreEsquemaCPE + ".tblfacturacionelectronica_fel \n"
                        + "SET \n"
                        + "  fel_fechasubidaservidor =now()\n"
                        + "WHERE fel_id in (" + idFel.substring(0, idFel.length() - 1) + ")";
                System.out.println(sql);
                st = con.prepareStatement(sql);
                st.executeUpdate();
                st.close();

                clsConexion.finalizarTransaccion(conExterna);
                clsConexion.finalizarTransaccion(con);
            }
            rpta = true;

        } catch (SQLException e) {
            e.printStackTrace();
            rpta = false;
            clsConexion.deshacerTransaccion(conExterna);
            clsConexion.deshacerTransaccion(con);
            mensaje = mensaje + "... Error SQL " + e.getMessage() + "...\n";
        } catch (Exception e) {
            e.printStackTrace();
            rpta = false;
            clsConexion.deshacerTransaccion(conExterna);
            clsConexion.deshacerTransaccion(con);
            mensaje = mensaje + "... Error " + e.getMessage() + "...\n";
        } finally {
            return rpta;
            //clsConexion.cerrarConexion(conPostgres);
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package herramientas;

import factuya.factura.clsFactura;
import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import utilitario.DownloadAndUpload;
import utilitario.Utilitario;

/**
 *
 * @author Gloria Peralta <gloria.ypv@gmail.com>
 */
public class clsSubidaServidorExterno {

    public static String mensaje = "";

    private static String ubicacionServidorExternoTemp = "";
    private static String nombreArchivoXML = "";
    private static clsFactura factura;
    private static Connection con;
    private static Connection conExterna;
    private static String usuario;
    private static Integer idComprobante = null;
    private static String tipoComprobante = "";

    public static void main(String[] args) {

    }

    public clsSubidaServidorExterno(String nombreArchivoXML, Integer idComprobante, String tipoComprobante, Connection con) {
        this.nombreArchivoXML = nombreArchivoXML;
        this.idComprobante = idComprobante;
        this.tipoComprobante = tipoComprobante;
        this.con = con;

    }

    public clsSubidaServidorExterno(String ubicacionServidorExternoTemp, String usuario, Connection con, Connection conExterna) {
        this.nombreArchivoXML = nombreArchivoXML;
        this.factura = factura;
        this.usuario = usuario;
        this.con = con;
        this.conExterna = conExterna;

    }

    public void crearUbicacion(String ubicacionPrincipal) {
        new File(ubicacionPrincipal + "/TEMP/").mkdir();
        new File(ubicacionPrincipal + "/TEMP/SUNAT/").mkdir();
        new File(ubicacionPrincipal + "/TEMP/SUNAT/ENVIO/").mkdir();
        new File(ubicacionPrincipal + "/TEMP/SUNAT/RPTA/").mkdir();
        new File(ubicacionPrincipal + "/TEMP/SUNAT/PDF/").mkdir();
        ubicacionServidorExternoTemp = ubicacionPrincipal + "/TEMP";

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
        copiarATemp.copyFile(ubicacionZip + nombreArchivoZip, ubicacionServidorExternoTemp + ubicacionSunatEnvio + nombreArchivoZip);
        copiarATemp.copyFile(ubicacionZipRespuesta + nombreArvhicoRespuestaZip, ubicacionServidorExternoTemp + ubicacionSunatRespuesta + nombreArvhicoRespuestaZip);

        String nameFormat = clsParametrosFactuya.numerRUC + tipoComprobante;
        String nameFile = nombreArchivoXML;
        Utilitario.exportarComprobantePDF(nameFormat, idComprobante, ubicacionServidorExternoTemp + "/SUNAT/PDF/", nameFile, ".pdf", ubicacionFormatos, ubicacionImagenes, con);

        /**
         * ***************************************************************************************************************************
         */
    }

    public static void subirServidorExteno(String hostSHExterno, String puertoSHExterno, String usuarioSHExterno, String passwordSHExterno, String ubicacionServidorExterno) {
        DownloadAndUpload copiarATemp = new DownloadAndUpload();
        mensaje = mensaje + copiarATemp.beginSFP(hostSHExterno, puertoSHExterno, usuarioSHExterno, passwordSHExterno);

        mensaje = mensaje + copiarATemp.uploadSFTPs(ubicacionServidorExternoTemp + "/SUNAT/ENVIO/", ".zip", ubicacionServidorExterno + "/SUNAT/ENVIO/");
        mensaje = mensaje + copiarATemp.uploadSFTPs(ubicacionServidorExternoTemp + "/SUNAT/RPTA/", ".zip", ubicacionServidorExterno + "/SUNAT/RPTA/");
        mensaje = mensaje + copiarATemp.uploadSFTPs(ubicacionServidorExternoTemp + "/SUNAT/PDF/", ".pdf", ubicacionServidorExterno + "/SUNAT/PDF/");

        mensaje = mensaje + copiarATemp.endSFP();
    }

    public static Boolean insertarServidorExterno() {

        Boolean rpta = false;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            // Enviar todo

            String idFel = "";
            String felInsertar = "";
            String sql = "SELECT\n"
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
                    + " AND fee_id='ACE'";
            System.out.println(sql);
            st = con.prepareCall(sql);
            if (st.execute()) {
                rs = st.getResultSet();
                if (rs != null) {
                    while (rs.next()) {
                        idFel = idFel + rs.getString("fel_id")+",";
                        felInsertar = felInsertar + "('" + rs.getString("ruc") + "',"
                                + "'" + rs.getString("emp_ruc") + "',"
                                + "'" + rs.getString("dxt_codigo") + "',"
                                + "'" + rs.getString("ent_nrodocumento") + "',"
                                + "'" + rs.getString("cvt_codigo") + "',"
                                + "'" + rs.getString("cvc_serie") + "',"
                                + "'" + rs.getString("cvc_numero") + "',"
                                + "'" + rs.getString("cvc_fechaemision") + "',"
                                + "'" + rs.getString("cvc_total") + "',"
                                + "'" + rs.getString("usuario") + "'),";
                    }
                }
            }
            rs.close();
            st.close();
            if (!idFel.equals("")) {
                clsConexion.inicarTransaccion(conExterna);
                clsConexion.inicarTransaccion(con);
                sql = "INSERT INTO \n"
                        + clsParametrosFactuya.nombreEsquema + " .tblfacturacionelectronicaservidor_fes\n"
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

                sql = "UPDATE ventas.tblfacturacionelectronica_fel \n"
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

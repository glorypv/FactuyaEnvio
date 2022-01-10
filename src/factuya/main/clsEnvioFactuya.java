/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package factuya.main;

import factuya.factura.clsFactura;
import factuya.factura.clsFacturaItem;
import factuya.factura.clsResumenDiario;
import factuya.factura.clsResumenDiarioItem;
import factuya.factura.clsComunicacionBaja;
import factuya.factura.clsComunicacionBajaItem;
import factuya.factura.clsFacturaFormaPago;
import java.io.File;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import herramientas.clsComprimirUBL;
import factuya.ubl.clsGenerarUBLFactura;
import factuya.ubl.clsFirmarUBL;
import factuya.ubl.clsGenerarUBLComunicacionBaja;
import factuya.ubl.clsGenerarUBLResumenDiario;
import factuya.webservice.clsEnvioCPE;
import factuya.webservice.ose.clsEnvioCPEOSE;
import herramientas.clsConexion;
import herramientas.clsParametrosFactuya;
import java.util.ArrayList;
import java.util.Vector;
import utilitario.Utilitario;

/**
 *
 * @author glory
 */
public class clsEnvioFactuya {

    private static String sql;
    private static ResultSet rs;
    private static CallableStatement call;
    private static String nombreArchivoXML;
    private static String ruc;
    private static String serie;
    private static String numero;
    private static String usuario;
    private static String fechaAnulacion;
    private static String tipoEnvio;
    public static String mensaje = "";
    private static String fecha;
    private static Integer bloque;
    private static Connection conPostgres = null;
    private static Boolean ordenId = Boolean.valueOf(false);

    private static Boolean enviarResumenDiario = false;
    private static String ubicacionCertificado;
    private static String nombreCertificado;
    private static String passwordCertificado;
    private static String usuarioSol;
    private static String passwordSol;

    private static Boolean subirServidor = false;
    private static String ubicacionPrincipal;
    private static String ubicacionSunatEnvio;
    private static String ubicacionServidor;
    private static String ubicacionSunatRespuesta;

    private static Integer enviarSUNAT = null;
    private static String nombreEsquema = "";
    private static String detracionCuenta = "";
    private static ArrayList idComprobantes = null;

    public static void main(String[] args) {
        String empresa = args[0]; //"20498596356";// //
        String ubicacionContext = Utilitario.ubicacionProyecto() + "/META-INF/context.xml";//"C:\\Program Files\\Apache Software Foundation\\Apache Tomcat 7.0.34\\webapps\\Factuya"
        clsParametrosFactuya obtenerParametros = new clsParametrosFactuya(empresa, ubicacionContext);
        obtenerParametros.cargarParametros();
        obtenerParametros.cargarConexion();

        conPostgres = clsConexion.obtenerConexion(clsParametrosFactuya.host, clsParametrosFactuya.usuarioBD, clsParametrosFactuya.passwordBD);
        clsEnvioFactuya enviar = new clsEnvioFactuya(
                clsParametrosFactuya.numerRUC,//"20498596356",
                null,//"B001",
                null,//"12041",
                null,// ARRARY ID
                null,//"admin",
                clsParametrosFactuya.enviarSUNAT,// 0,
                clsParametrosFactuya.enviarResumenDiario,//false,
                clsParametrosFactuya.ubicacionCertificado,//"C:\\HOME\\LLAVE",
                clsParametrosFactuya.nombreCertificado,// "certificado_chivay.pfx",
                clsParametrosFactuya.passwordCertificado,// "chivaycomet2021",
                clsParametrosFactuya.usuarioSol,// "20498596356C0M3TCP3",
                clsParametrosFactuya.passwordSol,//"C0M3T007",
                clsParametrosFactuya.subirServidor,// false,
                clsParametrosFactuya.ubicacionPrincipal,// "C:\\HOME\\",
                clsParametrosFactuya.ubicacionServidor,// "C:\\HOME\\",
                clsParametrosFactuya.ubicacionSunatEnvio,// "\\SUNAT\\ENVIO\\",
                clsParametrosFactuya.ubicacionSunatRespuesta,//  "\\SUNAT\\\\RPTA\\",
                clsParametrosFactuya.nombreEsquema,
                clsParametrosFactuya.detracionCuenta,
                conPostgres
        );
        //enviarUnaFactura();
        enviar.enviarFacturasPendientes(clsParametrosFactuya.numerRUC);

        clsConexion.cerrarConexion(conPostgres);
    }

    // <editor-fold defaultstate="collapsed" desc="clsEnvioFactuya constructor">
    public clsEnvioFactuya(String _ruc, String _serie, String _numero, ArrayList idComprobantes, String _usuario, Integer enviarSUNAT, Boolean enviarResumenDiario, String ubicacionCertificado, String nombreCertificado, String passwordCertificado, String usuarioSol, String passwordSol, Boolean subirServidor, String ubicacionPrincipal, String ubicacionServidor, String ubicacionSunatEnvio, String ubicacionSunatRespuesta, String nombreEsquema, String detracionCuenta, Connection conx) {

        this.ruc = _ruc;
        this.serie = _serie;
        this.numero = _numero;
        this.usuario = _usuario;
        this.enviarSUNAT = enviarSUNAT;
        this.enviarResumenDiario = enviarResumenDiario;
        this.ubicacionCertificado = ubicacionCertificado;
        this.nombreCertificado = nombreCertificado;
        this.passwordCertificado = passwordCertificado;
        this.usuarioSol = usuarioSol;
        this.passwordSol = passwordSol;
        this.subirServidor = subirServidor;
        this.ubicacionPrincipal = ubicacionPrincipal;
        this.ubicacionSunatEnvio = ubicacionSunatEnvio;
        this.ubicacionServidor = ubicacionServidor;
        this.ubicacionSunatRespuesta = ubicacionSunatRespuesta;
        this.nombreEsquema = nombreEsquema;
        this.detracionCuenta = detracionCuenta;
        this.conPostgres = conx;
        this.idComprobantes = idComprobantes;
        mensaje = "";
        ordenId = Boolean.valueOf(false);

        System.out.println("aqui");

    }
   // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="enviarUnaFactura">
    public static String enviarUnaFactura() {

        if (conPostgres == null) {
            mensaje = mensaje + "\n...No existe conexion a DB ...";
            return mensaje;
        }
        mensaje = mensaje + "\n... Generando ..." + ruc + " " + serie + "-" + numero + " \n";

        try {
            clsGenerarUBLFactura generarUBL = new clsGenerarUBLFactura(cargarFactura(ruc, serie, numero));

            byte[] escritoXML = generarUBL.escribirXMLByte();
            if (escritoXML.length > 0) {
                byte[] firmaXML = firmarByteCPE(escritoXML);
                enviarCPE("invoice", firmaXML, null, null, enviarSUNAT);
            } else {
                mensaje = mensaje + "... Error ... Generar UBL " + ruc + " " + serie + "-" + numero + "\n";

            }
            mensaje = mensaje + "\n";
        } catch (Exception ex) {
            mensaje = mensaje + "... Error " + ex.getMessage() + "...\n";
        } finally {
            // clsConexion.cerrarConexion(conPostgres);
            return mensaje;
        }
    }
     // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="enviarResumenDiarioComunicacionBaja">
    public static String enviarResumenDiarioComunicacionBaja() {
        try {
            Array resultArr = conPostgres.createArrayOf("int8", idComprobantes.toArray());

            if (resultArr != null) {
                liberarEnvios(ruc, resultArr);
                enviarResumenYBajaPendiente(ruc, resultArr);
            }

        } catch (SQLException ex) {
            mensaje = mensaje + "... Error " + ex.getMessage() + "...\n";
        } finally {
            clsConexion.cerrarConexion(conPostgres);
            return mensaje;
        }
    }
     // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="enviarResumenYBajaPendiente">
    private static void enviarResumenYBajaPendiente(String empresa, Array id) {
        try {
            // Enviar todo
            CallableStatement callsec;
            ResultSet rssec;
            Connection con = clsConexion.obtenerConexion();

            String arrId = id == null ? null : "'" + id + "'";
            sql = "SELECT * FROM " + nombreEsquema + ".fun_fel_consultar_pendiente ("
                    + "'" + empresa + "',"
                    + arrId + ""
                    + ")";

            System.out.println(sql);
            mensaje = mensaje + "\n... Inicio Resumen Diario y Comunicacion de Baja ...\n\n";
            //  progress(mensaje);
            callsec = con.prepareCall(sql);
            if (callsec.execute()) {
                rssec = callsec.getResultSet();
                if (rssec != null) {
                    while (rssec.next()) {

                        String dateSec = rssec.getString("fechaemision");
                        String classification = rssec.getString("clasificacion");

                        if (rssec.getString("nroticket") == null) {
                            String tipo = rssec.getString("tipo");

                            if (tipo.equals("RC")) {
                                Double quantity = rssec.getDouble("cantidad");
                                while (quantity > 0) {
                                    //  System.out.println("Crear CPE ... " + Profile.getInstance().getLocateServer() + rssec.getString("nombrearchivo"));
                                    clsGenerarUBLResumenDiario generate = new clsGenerarUBLResumenDiario(cargarResumenDiario(empresa, dateSec, classification, id));
                                    if (generate != null) {
                                        fecha = dateSec;

                                        byte[] writeXML = generate.escribirXMLByte();

                                        if (writeXML.length > 0) {
                                            byte[] signXML = firmarByteCPE(writeXML);

                                            clsEnvioFactuya.enviarCPE(classification.equals("") || classification.equals("NC") ? "RC" : classification, signXML, classification, bloque, enviarSUNAT);
                                        } else {
                                            mensaje = mensaje + "... Error ... Generar UBL " + ruc + " " + serie + "-" + numero + "\n";

                                        }
                                    }
                                    quantity = quantity - 1;
                                }
                            }

                            if (tipo.equals("RA")) {
                                // File filerc = new File(parametroRuta + rssec.getString("nombrearchivorc") + "-1-R.zip");
                                // LEER AACEPTADO
                                //  System.out.println("Crepar CPE RC ... " + Profile.getInstance().getLocateServer() + rssec.getString("nombrearchivorc") + "-1-R.zip");
                                clsComunicacionBaja baja = cargarComunicacionBaja(empresa, dateSec, id);
                                if (baja.getCodigo() != null) {
                                    clsGenerarUBLComunicacionBaja generateVoided = new clsGenerarUBLComunicacionBaja(baja);
                                    fecha = fechaAnulacion;
                                    byte[] writeXMLVoided = generateVoided.escribirXMLByte();
                                    if (writeXMLVoided.length > 0) {
                                        byte[] signXMLVoided = firmarByteCPE(writeXMLVoided);
                                        clsEnvioFactuya.enviarCPE("RA", signXMLVoided, "", bloque, enviarSUNAT);
                                    } else {
                                        mensaje = mensaje + "... Error ... Generar UBL " + ruc + " " + serie + "-" + numero + "\n";

                                    }
                                }
                            }
                        }

                    }
                }
            }
            mensaje = mensaje + "\n...  Fin Resumen Diario y Comunicacion de Baja ...\n";
            clsConexion.cerrarConexion(con);
        } catch (SQLException ex) {
            ex.printStackTrace();
            mensaje = mensaje + "... Error SQL " + ex.getMessage() + "...\n";
            // progress(mensaje);
        } catch (Exception ex) {
            ex.printStackTrace();
            mensaje = mensaje + "... Error " + ex.getMessage() + "...\n";
            //progress(mensaje);
        }
    }
     // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="enviarFacturasPendientes">
    private static void enviarFacturasPendientes(String empresa) {
        try {
            // Enviar Facturas Pendientes
            CallableStatement callsec;
            ResultSet rssec;

            Connection con = clsConexion.obtenerConexion(clsParametrosFactuya.host, clsParametrosFactuya.usuarioBD, clsParametrosFactuya.passwordBD);

            String tipo = "'{F,B}'";
            if (enviarResumenDiario) {
                tipo = "'{F}'";
            }

            sql = "SELECT * FROM " + nombreEsquema + ".fun_fel_consulta_facturacionelectronica_pendiente ("
                    + "'" + empresa + "',"
                    + tipo + ")";
            callsec = con.prepareCall(sql);
            if (callsec.execute()) {
                rssec = callsec.getResultSet();
                if (rssec != null) {
                    while (rssec.next()) {
                        serie = rssec.getString("serie");
                        numero = rssec.getString("numero");
                        ruc = rssec.getString("ruc");
                        usuario = rssec.getString("usuario");
                        String rpta = "-1";
                        // FALTA
                        File file = new File(ubicacionServidor + rssec.getString("nombrearchivo") + ".zip");
                        // COMPROBAR SI EXISTE ARCHIVO DE REPUESTA de FACTURA
                        if (serie.substring(0, 1).equals("F")) {
                            File filerpta = new File(ubicacionServidor + rssec.getString("nombrearchivo") + "-R.zip");
                            if (filerpta.exists()) {
                                rpta = Utilitario.searchAnswer(ubicacionServidor, rssec.getString("nombrearchivo") + "-R.zip", "cbc:ResponseCode");

                            }
                        }
                        if (!rpta.equals("0")) {// archivo de respuesta diferente de aceptado
                            System.out.println("Mostar CPE ... " + ubicacionServidor + rssec.getString("nombrearchivo") + ".zip");
                            if (file.exists() && rssec.getString("estado") != null) {
                                System.out.println("Enviar Archivo CPE ... " + rssec.getString("nombrearchivo") + ".zip");
                                nombreArchivoXML = rssec.getString("nombrearchivo");
                                //   usuario = "SISTEMA";
                                enviarFacturaCPE();
                            } else if (!file.exists() && rssec.getString("estado") == null) { // SI NO EXISTE EN COMPROBNTESDEPAGOELECTRONICO
                                System.out.println("Insertar CPE EN BD ... " + rssec.getString("nombrearchivo") + ".zip");
                                clsEnvioCPE cpe = new clsEnvioCPE(usuarioSol, passwordSol, conPostgres);
                                cpe.insertarCPE(ruc, serie, numero, "", "INI", "NOW()", "NULL", usuario, usuario);
                            } else if (!file.exists() && rssec.getString("rutaxml") == null) {
                                /*  System.out.println("Crear Archivo CPE ... " + rssec.getString("nombrearchivo") + ".zip");
                                 clsGenerarUBLFactura generate = new clsGenerarUBLFactura(cargarFactura(ruc, serie, numero));
                                 byte[] writeXML = generate.escribirXMLByte();
                                 byte[] signXML = firmarByteCPE(writeXML);
                                 //   usuario = "SISTEMA";
                                 clsEnvioFactuya.enviarCPE("invoice", signXML, null, null, enviarSUNAT);*/

                                if (rssec.getString("estadocom").equals("ANU") && rssec.getString("estado").equals("INI")) {
                                    actualizarAnulados(ruc, serie, numero, null, usuario);

                                } else {
                                    enviarUnaFactura();
                                }

                            }

                        } else {
                            clsEnvioCPE.actualizarCPE(ruc, serie, numero, "", "ACE", "", "", "", rssec.getString("nombrearchivo") + "-R.zip", "NOW()", usuario, "");
                        }

                        serie = null;
                        numero = null;
                    }
                }
            }
            clsConexion.cerrarConexion(con);
        } catch (SQLException e) {
            mensaje = mensaje + "... Error SQL " + e.getMessage() + "...\n";
            // progress(mensaje);
        } catch (Exception e) {
            e.printStackTrace();
            mensaje = mensaje + "... Error " + e.getMessage() + "...\n";
            // progress(mensaje);
        }
    }
     // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="enviarCPE">
    private static void enviarCPE(String tipoEnvio, byte[] archivoFirmadoXML, String clasificacionResumen, Integer bloqueResumen, Integer enviarSUNAT) {
        String nombreArchivoZip = nombreArchivoXML + ".zip";
        String nombreArvhicoRespuestaZip = nombreArchivoXML + "-R.zip";

        String ubicacionZip = subirServidor == true ? ubicacionPrincipal + ubicacionSunatEnvio : ubicacionServidor + ubicacionSunatEnvio;
        String ubicacionZipRespuesta = subirServidor == true ? ubicacionPrincipal + ubicacionSunatRespuesta : ubicacionServidor + ubicacionSunatRespuesta;

        clsComprimirUBL comprimidoUBL = new clsComprimirUBL();
        comprimidoUBL.comprimirXMLByte(archivoFirmadoXML, nombreArchivoXML + ".xml", new File(ubicacionZip, nombreArchivoZip));

        clsEnvioCPE cpe = null;
        clsEnvioCPEOSE cpeOSE = null;
        if (enviarSUNAT == 0) {
            cpe = new clsEnvioCPE(usuarioSol, passwordSol, enviarResumenDiario, nombreEsquema, conPostgres);
            if (tipoEnvio.equals("invoice")) {

                mensaje = mensaje + "... Enviando ..." + nombreArchivoZip + " \n";

                // progress(mensaje);
                String codigoRespuesta = cpe.enviarFactura(new File(ubicacionZip, nombreArchivoZip), new File(ubicacionZipRespuesta, nombreArvhicoRespuestaZip), ruc, serie, numero, usuario);

                mensaje = mensaje + "... Respuesta ..." + nombreArchivoZip + " \n" + codigoRespuesta + " \n";

                // progress(mensaje);
            } else {
                mensaje = mensaje + "... Enviando ..." + nombreArchivoZip + " \n";
                //progress(mensaje);
                String codigoRespuesta = cpe.enviarResumenDiario(tipoEnvio, clasificacionResumen, bloqueResumen, new File(ubicacionZip, nombreArchivoZip), new File(ubicacionZipRespuesta, nombreArvhicoRespuestaZip), ruc, fecha, usuario);
                mensaje = mensaje + "... Respuesta ..." + nombreArchivoZip + " " + codigoRespuesta + " \n";
                // progress(mensaje);
            }

        }
        if (enviarSUNAT == 1) {
            cpeOSE = new clsEnvioCPEOSE(usuarioSol, passwordSol, conPostgres);

            if (tipoEnvio.equals("invoice")) {
                mensaje = mensaje + "... Enviando ..." + nombreArchivoZip + " \n";
                // progress(mensaje);
                String codigoRespuesta = cpeOSE.enviarFactura(new File(ubicacionZip, nombreArchivoZip), new File(ubicacionZipRespuesta, nombreArvhicoRespuestaZip), ruc, serie, numero, usuario);
                mensaje = mensaje + "... Respuesta ..." + nombreArchivoZip + " \n" + codigoRespuesta + " \n";
                // progress(mensaje);
            } else {
                mensaje = mensaje + "... Enviando ..." + nombreArchivoZip + " \n";
                //progress(mensaje);
                String codigoRespuesta = cpeOSE.enviarResumenDiario(tipoEnvio, clasificacionResumen, bloqueResumen, new File(ubicacionZip, nombreArchivoZip), new File(ubicacionZipRespuesta, nombreArvhicoRespuestaZip), ruc, fecha, usuario);
                mensaje = mensaje + "... Respuesta ..." + nombreArchivoZip + " " + codigoRespuesta + " \n";
                // progress(mensaje);
            }
        }

        /**
         * ***************** SUBIR A SERVIDOR EXTERNO
         * *********************************************************************************
         */
        /* new File(ubicacionPrincipal + "/TEMP/").mkdir();
         new File(ubicacionPrincipal + "/TEMP/SUNAT/").mkdir();
         new File(ubicacionPrincipal + "/TEMP/SUNAT/ENVIO/").mkdir();
         new File(ubicacionPrincipal + "/TEMP/SUNAT/RPTA/").mkdir();
         String ubicacionServidorExternoTemp = ubicacionPrincipal + "/TEMP";

         DownloadAndUpload copiarATemp = new DownloadAndUpload();
         copiarATemp.copyFile(ubicacionZip + nombreArchivoZip, ubicacionServidorExternoTemp + ubicacionSunatEnvio + nombreArchivoZip);
         copiarATemp.copyFile(ubicacionZip + nombreArvhicoRespuestaZip, ubicacionServidorExternoTemp + ubicacionSunatRespuesta + nombreArvhicoRespuestaZip);

         mensaje = mensaje + copiarATemp.beginSFP(clsParametrosFactuya.hostSHExterno, clsParametrosFactuya.puertoSHExterno, clsParametrosFactuya.usuarioSHExterno, clsParametrosFactuya.passwordSHExterno);
         mensaje = mensaje + copiarATemp.uploadSFTPs(ubicacionServidorExternoTemp, clsParametrosFactuya.ubicacionServidorExterno, ubicacionSunatEnvio, ubicacionSunatRespuesta);
         mensaje = mensaje + copiarATemp.endSFP();*/
        /**
         * ***************************************************************************************************************************
         */
    }
     // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="enviarFacturaCPE">
    private static void enviarFacturaCPE() {
        String nombreArchivoZip = nombreArchivoXML + ".zip";
        String nombreArvhicoRespuestaZip = nombreArchivoXML + "-R.zip";

        String ubicacionZip = subirServidor == true ? ubicacionPrincipal + ubicacionSunatEnvio : ubicacionServidor + ubicacionSunatEnvio;
        String ubicacionZipRespuesta = subirServidor == true ? ubicacionPrincipal + ubicacionSunatRespuesta : ubicacionServidor + ubicacionSunatRespuesta;

        if (enviarSUNAT == 0) {

            clsEnvioCPE cpe = new clsEnvioCPE(usuarioSol, passwordSol, conPostgres);
            String codigoRespuesta = cpe.enviarFactura(new File(ubicacionZip, nombreArchivoZip), new File(ubicacionZipRespuesta, nombreArvhicoRespuestaZip), ruc, serie, numero, usuario);
        } else if (enviarSUNAT == 1) {
            clsEnvioCPEOSE cpe = new clsEnvioCPEOSE(usuarioSol, passwordSol, conPostgres);
            String codigoRespuesta = cpe.enviarFactura(new File(ubicacionZip, nombreArchivoZip), new File(ubicacionZipRespuesta, nombreArvhicoRespuestaZip), ruc, serie, numero, usuario);

        }
    }
     // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="firmarByteCPE">
    private static byte[] firmarByteCPE(byte[] archivoXMLByte) {
        clsFirmarUBL firma = new clsFirmarUBL();
        try {
            firma.cargarXMLByte(archivoXMLByte);
            firma.firmarUBL("PKCS12", new File(ubicacionCertificado, nombreCertificado), passwordCertificado, ordenId);
        } catch (Exception e) {

            System.out.println(e.getMessage());
            mensaje = mensaje + "... Error " + e.getMessage() + "...\n";
            // progress(mensaje);
        }
        return firma.outputXMLByte();
    }
     // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="cargarFactura">
    private static clsFactura cargarFactura(String empresa, String serie, String numero) {
        Boolean flag = true;
        clsFactura factura = null;
        Vector<clsFacturaItem> Items = new Vector<clsFacturaItem>();
        // int i=0;
        try {
            sql = "SELECT * FROM " + nombreEsquema + ".fun_fel_consulta_facturacionelectronica("
                    + "'" + empresa + "',"
                    + "'" + serie + "',"
                    + "'" + numero + "')";
            System.out.println(sql);
            call = conPostgres.prepareCall(sql);
            if (call.execute()) {
                rs = call.getResultSet();
                if (rs != null) {
                    while (rs.next()) {
                        if (flag) {
                            factura = new clsFactura(rs.getString("tipocodigosunat"));
                            factura.setEmisor_RUC(rs.getString("ruc"));
                            factura.setEmisor_RazonSocial(rs.getString("razonsocial"));
                            factura.setEmisor_DomicilioFiscal(rs.getString("direccion"));
                            factura.setEmisor_DomicilioFiscalDepartamento(rs.getString("departamento"));
                            factura.setEmisor_DomicilioFiscalDistrito(rs.getString("distrito"));
                            factura.setEmisor_DomicilioFiscalProvincia(rs.getString("provincia"));
                            factura.setEmisor_DomicilioFiscalUBIGEO(rs.getString("ubigeo"));

                            factura.setAdquirente_TipoDoc(rs.getString("adquirientetipodocodigosunat"));
                            factura.setAdquirente_NroDocumento(rs.getString("adquirientenrodocumento"));
                            factura.setAdquirente_RazonSocial(rs.getString("adquirienterazonsocial"));
                            // comprobante.setAdquirente_DomicilioFiscal(rs.getString("cli_direccion"));
                            factura.setAdquirente_DomicilioFiscal("---");

                            factura.setAdquirente_DomicilioFiscalDepartamento("-");
                            factura.setAdquirente_DomicilioFiscalDistrito("-");
                            factura.setAdquirente_DomicilioFiscalProvincia("-");
                            factura.setAdquirente_DomicilioFiscalUBIGEO("");

                            factura.setFechaEmision(rs.getString("fechaemision"));
                            factura.setFechaVencimiento("");
                            factura.setMoneda(rs.getString("moneda"));
                            factura.setSerieNumero(rs.getString("numerocpe"));

                            factura.setPorcentaje(rs.getString("porcentaje"));
                            factura.setTotalVenta(rs.getString("total"));
                            factura.setVVenta(rs.getString("valorventa"));
                            factura.setIGV(rs.getString("impuesto"));
                            factura.setRecargo(rs.getString("recargo"));
                            factura.setValorVentaBruto(rs.getString("valorventabruto"));
                            factura.setDescuento(rs.getString("descuento"));

                            factura.setTipoTributario(rs.getString("tipotributario"));
                            factura.setObservaciones(rs.getString("observaciones"));
                            factura.setTipoTributarioGratuito(rs.getString("tipotributogratuito"));
                            factura.setTotalVentaGratuito(rs.getString("totalventagratuito"));

                            factura.setNumeroOrdenCompra(rs.getString("ordencompra"));
                            if (!rs.getString("ordencompra").equals("")) {
                                ordenId = Boolean.valueOf(true);
                            }

                            if (rs.getString("tipocodigosunat").equals("07") || rs.getString("tipocodigosunat").equals("08")) {
                                factura.setTipoNota(rs.getString("tiponota"));
                                factura.setSerieNumeroReferencia(rs.getString("numerocperef"));
                                factura.setDescripcion(rs.getString("observacionesref"));
                                factura.setTipoDocReferencia(rs.getString("codigosunatref"));
                            }

                            factura.setDetraccionCod(rs.getString("retenciontipo"));
                            factura.setDetraccionPorc(rs.getString("retencionporc"));
                            factura.setDetraccionMonto(rs.getString("retencionmonto"));
                            factura.setDetraccionCuenta(detracionCuenta);
                            factura.setRegimenCodigo(rs.getString("rtt_codigo"));

                            flag = false;
                            nombreArchivoXML = rs.getString("ruc") + "-" + rs.getString("tipocodigosunat") + "-" + rs.getString("numerocpe");
                            //  usuario = rs.getString("usuario");

                        }

                        clsFacturaItem item = new clsFacturaItem();
                        item.setCodigo(rs.getString("codigoproductodet"));
                        item.setCodigoSunat(rs.getString("codigoproductosunatdet"));
                        item.setDescripcion(rs.getString("descripciondet"));
                        item.setCantidad(rs.getString("cantidaddet"));
                        item.setPUnitario(rs.getString("preciounitariodet"));
                        // item.setUnidad(rs.getString("uni_idunidad"));
                        item.setUnidad("NIU");

                        item.setVVenta(rs.getString("valorfinaldet"));
                        item.setIGV(rs.getString("impuestodet"));
                        item.setIGVTipo(rs.getString("igvtipo"));
                        item.setISC("0.00");
                        item.setNroOrden("1");
                        item.setPVenta(rs.getString("precioventadet"));
                        item.setDescuento(rs.getString("descuentodet"));
                        item.setPorcentajeDescuento(rs.getString("porcentajedescuentodet"));

                        item.setVVentaBruto(rs.getString("valorbrutodet"));
                        item.setTipoPrecioVentaDet(rs.getString("tipoprecioventadet"));
                        item.setTipoTributoDet(rs.getString("tipotributodetalle"));

                        Items.add(item);
                        factura.setItems(Items);
                    }

                }
            }
            sql = "";
            Vector<clsFacturaFormaPago> ItemsFormaPago = new Vector<clsFacturaFormaPago>();

            sql = "SELECT * FROM " + nombreEsquema + ".fun_fel_consulta_facturacionelectronica_formapago("
                    + "'" + empresa + "',"
                    + "'" + serie + "',"
                    + "'" + numero + "')";
            System.out.println(sql);
            call = conPostgres.prepareCall(sql);
            if (call.execute()) {
                rs = call.getResultSet();
                if (rs != null) {
                    while (rs.next()) {
                        clsFacturaFormaPago item = new clsFacturaFormaPago();
                        item.setIndicador(rs.getString("indicador"));
                        item.setTipoTransaccion(rs.getString("tipotransaccion"));
                        item.setMoneda(rs.getString("moneda"));
                        item.setMonto(rs.getString("monto"));
                        item.setFecha(rs.getString("fecha"));
                        ItemsFormaPago.add(item);
                        factura.setItemsFormaPago(ItemsFormaPago);
                    }
                }
            }
        } catch (SQLException e) {
            factura = null;
            System.out.println(e.getMessage());
            e.printStackTrace();
            mensaje = mensaje + "... Error SQL " + e.getMessage() + "...\n";
            //progress(mensaje);
        } catch (Exception e) {
            factura = null;
            System.out.println(e.getMessage());
            e.printStackTrace();
            mensaje = mensaje + "... Error " + e.getMessage() + "...\n";
            //progress(mensaje);
        }
        return factura;
    }
     // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="cargarResumenDiario">
    private static clsResumenDiario cargarResumenDiario(String empresa, String fecha, String clasificacion, Array id) {
        clsResumenDiario resumenDiario = new clsResumenDiario();
        Boolean flag = true;
        Vector<clsResumenDiarioItem> items = new Vector<clsResumenDiarioItem>();
        try {
            String arrId = id == null ? null : "'" + id + "'";

            sql = "SELECT * FROM  " + nombreEsquema + ".fun_fel_consultar_resumen("
                    + "'" + empresa + "',"
                    + "'" + fecha + "',"
                    + "'" + clasificacion + "',"
                    + arrId + ")";

            System.out.println(sql);
            call = conPostgres.prepareCall(sql);
            if (call.execute()) {
                rs = call.getResultSet();
                if (rs != null) {
                    while (rs.next()) {
                        if (rs.getString("numerocpe") != (null)) {
                            if (flag) {
                                resumenDiario.setCodigo(rs.getString("nombrearchivo"));
                                resumenDiario.setTipoDoc("6");
                                resumenDiario.setEmisor_RUC(rs.getString("ruc"));
                                resumenDiario.setEmisor_RazonSocial(rs.getString("razonsocial"));
                                resumenDiario.setFechaEmision(rs.getString("fechaemision"));
                                resumenDiario.setFechaGeneracion(rs.getString("fechageneracion"));
                                nombreArchivoXML = rs.getString("ruc") + "-" + rs.getString("nombrearchivo");
                                //  usuario = "SISTEMA";
                                bloque = rs.getInt("bloque");
                                ruc = rs.getString("ruc");
                                flag = false;
                            }

                            clsResumenDiarioItem rbi = new clsResumenDiarioItem();
                            rbi.setSerieNumero(rs.getString("numerocpe"));
                            rbi.setTipoDoc(rs.getString("tipocodigosunat"));
                            rbi.setAdquirente_NroDocumento(rs.getString("adquirientenrodocumento"));
                            rbi.setEstado(rs.getString("estado"));
                            rbi.setAdquirente_TipoDoc(rs.getString("adquirientetipodocodigosunat"));
                            if (rs.getString("tipocodigosunatref").equals("07") || rs.getString("tipocodigosunatref").equals("08")) {
                                rbi.setSerieNumeroReferencia(rs.getString("numerocperef"));
                                rbi.setTipoDocReferencia(rs.getString("tipocodigosunatref"));
                            }
                            rbi.setTotalIGV(rs.getString("totalimpuesto"));
                            rbi.setTotalISC("0.00");
                            rbi.setTotalVVentaOpeExoneradas(rs.getString("totalexonerado"));
                            rbi.setTotalVVentaOpeGravadas(rs.getString("totalgravado"));
                            rbi.setTotalVVentaOpeInafectas("0.00");
                            rbi.setTotalVVentaOpeGratuitas(rs.getString("totalgratuito"));
                            rbi.setTotalVenta(rs.getString("totalventa"));
                            rbi.setTipoValorVenta(rs.getString("tipovalorventa"));

                            items.add(rbi);
                            resumenDiario.setItems(items);
                        }
                    }
                }
            }
            rs.close();
        } catch (SQLException e) {
            resumenDiario = null;
            e.printStackTrace();
            System.out.println(e.getMessage());
            mensaje = mensaje + "... Error SQL " + e.getMessage() + "...\n";
            // progress(mensaje);
        } catch (Exception e) {
            resumenDiario = null;
            e.printStackTrace();
            System.out.println(e.getMessage());
            mensaje = mensaje + "... Error " + e.getMessage() + "...\n";
            //progress(mensaje);

        }
        return resumenDiario;
    }
     // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="cargarComunicacionBaja">
    private static clsComunicacionBaja cargarComunicacionBaja(String empresa, String fecha, Array id) {
        clsComunicacionBaja baja = new clsComunicacionBaja();
        Boolean flag = true;
        String arrId = id == null ? null : "'" + id + "'";
        Vector<clsComunicacionBajaItem> items = new Vector<clsComunicacionBajaItem>();
        try {
            sql = "SELECT * FROM " + nombreEsquema + ".fun_fel_consultar_baja("
                    + "'" + empresa + "',"
                    + "'" + fecha + "',"
                    + arrId + ")";
            System.out.println(sql);
            call = conPostgres.prepareCall(sql);
            if (call.execute()) {
                rs = call.getResultSet();
                if (rs != null) {
                    while (rs.next()) {
                        if (flag) {
                            baja.setCodigo(rs.getString("nombrearchivo"));
                            baja.setEmisorNroDocumento(rs.getString("ruc"));
                            baja.setEmisorRazonSocial(rs.getString("razonsocial"));
                            baja.setFechaGeneracionDocumento(rs.getString("fechageneraciondocumento"));
                            baja.setFechaGeneracionComunicado(rs.getString("fechageneracioncomunicado"));
                            nombreArchivoXML = rs.getString("ruc") + "-" + rs.getString("nombrearchivo");
                            //  usuario = "SISTEMA";
                            fechaAnulacion = rs.getString("fechageneraciondocumento");
                            bloque = rs.getInt("bloque");

                            flag = false;
                        }
                        if (rs.getString("serie") != (null)) {
                            clsComunicacionBajaItem voideditem = new clsComunicacionBajaItem();

                            voideditem.setTipoDoc(rs.getString("tipocodigosunat"));
                            voideditem.setSerie(rs.getString("serie"));
                            voideditem.setNumero(rs.getString("numero"));
                            voideditem.setMotivoBaja(rs.getString("observacion"));

                            items.add(voideditem);
                            baja.setItems(items);
                        }
                    }
                }
            }
            rs.close();
        } catch (SQLException e) {
            baja = null;
            mensaje = "... Error SQL " + e.getMessage() + "...\n";
            // progress(mensaje);
        } catch (Exception e) {
            baja = null;
            e.printStackTrace();
            mensaje = "... Error " + e.getMessage() + "...\n";
            // progress(mensaje);
        }
        return baja;

    }
     // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="enviarTicketPendientes">
    public static void enviarTicketPendientes(String empresa) {
        if (empresa != null && empresa.equals("")) {
            return;
        }
        try {
            // Enviar todo
            CallableStatement callsec;
            ResultSet rssec;

            conPostgres = clsConexion.obtenerConexion();
            String pathZip = subirServidor == true ? ubicacionServidor + ubicacionSunatEnvio : ubicacionServidor + ubicacionSunatRespuesta;
            String pathZipAnwser = subirServidor == true ? ubicacionServidor + ubicacionSunatEnvio : ubicacionServidor + ubicacionSunatRespuesta;

            sql = "SELECT * FROM " + nombreEsquema + ".fun_fel_consultar_ticket_pendiente ("
                    + "'" + empresa + "')";
            System.out.println(sql);
            callsec = conPostgres.prepareCall(sql);
            if (callsec.execute()) {
                rssec = callsec.getResultSet();
                if (rssec != null) {
                    while (rssec.next()) {
                        String salidaRespuesta = rssec.getString("rutaxml") + "-R.zip";
                        if (enviarSUNAT == 0) {
                            clsEnvioCPE cpe = new clsEnvioCPE(usuarioSol, passwordSol, conPostgres);
                            cpe.actualizarResumenDiario(pathZipAnwser, salidaRespuesta, empresa, rssec.getString("tipo"), rssec.getString("nroticket"), "NOW()", usuario, "");
                        } else if (enviarSUNAT == 1) {
                            clsEnvioCPEOSE cpe = new clsEnvioCPEOSE(usuarioSol, passwordSol, conPostgres);
                            cpe.actualizarResumenDiario(pathZipAnwser, salidaRespuesta, empresa, rssec.getString("tipo"), rssec.getString("nroticket"), "NOW()", usuario, "");

                        }
                    }
                }
            }
            clsConexion.cerrarConexion(conPostgres);
        } catch (SQLException e) {
            e.printStackTrace();
            clsConexion.deshacerTransaccion();
            mensaje = mensaje + "... Error SQL " + e.getMessage() + "...\n";
        } catch (Exception e) {
            e.printStackTrace();
            clsConexion.deshacerTransaccion();
            mensaje = mensaje + "... Error " + e.getMessage() + "...\n";
        }
    }
     // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="liberarEnvios">
    public static void liberarEnvios(String empresa) {

        if (empresa != null && empresa.equals("")) {
            return;
        }
        try {
            // Enviar todo
            CallableStatement callsec;
            ResultSet rssec;
            conPostgres = clsConexion.obtenerConexion();
            sql = "SELECT * FROM " + nombreEsquema + ".fun_fel_liberar_envio ("
                    + "'" + empresa + "')";
            System.out.println(sql);
            callsec = conPostgres.prepareCall(sql);
            callsec.execute();
            callsec.close();
            clsConexion.cerrarConexion(conPostgres);

        } catch (SQLException e) {
            e.printStackTrace();
            clsConexion.deshacerTransaccion();
            mensaje = mensaje + "... Error SQL " + e.getMessage() + "...\n";
        } catch (Exception e) {
            e.printStackTrace();
            clsConexion.deshacerTransaccion();
            mensaje = mensaje + "... Error " + e.getMessage() + "...\n";
        } finally {
            clsConexion.cerrarConexion(conPostgres);
        }
    }
     // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="liberarEnvios">
    public static void liberarEnvios(String empresa, Array id) {

        if (empresa != null && empresa.equals("")) {
            return;
        }

        try {
            // Enviar todo
            CallableStatement callsec;
            ResultSet rssec;
            // conPostgres = clsConexion.obtenerConexion();

            String arrId = id == null ? null : "'" + id + "'";
            sql = "SELECT * FROM " + nombreEsquema + ".fun_fel_liberar_envio ("
                    + "'" + empresa + "',"
                    + arrId + ","
                    + " null"
                    + ")";
            System.out.println(sql);
            callsec = conPostgres.prepareCall(sql);
            callsec.execute();
            callsec.close();
            //  clsConexion.cerrarConexion(conPostgres);

        } catch (SQLException e) {
            e.printStackTrace();
            clsConexion.deshacerTransaccion();
            mensaje = mensaje + "... Error SQL " + e.getMessage() + "...\n";
        } catch (Exception e) {
            e.printStackTrace();
            clsConexion.deshacerTransaccion();
            mensaje = mensaje + "... Error " + e.getMessage() + "...\n";
        }
    }
     // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="enviarPendientesAutomatico">
    public static boolean enviarPendientesAutomatico(String empresa, String tipo, String serie, String numero, String _usuario, Integer enviarSUNAT) {
        Boolean rpta = false;
        try {

            ruc = empresa;
            usuario = _usuario;
            if (tipo.equals("-")) { // enviar 1 a 1
                enviarUnaFactura();
            }
            if (tipo.equals("PEN")) {
                enviarFacturasPendientes(empresa);
            }
            if (tipo.equals("RR")) {

                enviarTicketPendientes(empresa);
                enviarResumenYBajaPendiente(empresa, null);
                enviarTicketPendientes(empresa);
            }
        } catch (Exception ex) {
            System.out.println(" " + ex);
            ex.printStackTrace();

        }
        return rpta;

    }
     // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="liberarTicket">
    public static void liberarTicket(String empresa, Array id, String rpta) {
        try {
            String sql;
            sql = " { call " + nombreEsquema + ".fun_fel_liberar_ticket ("
                    + "'" + empresa + "',"
                    + "'" + id + "',"
                    + rpta + ""
                    + ")}";
            System.out.println(sql);
            call = conPostgres.prepareCall(sql);
            call.execute();
            call.close();
            if (rpta != null) {
                mensaje = mensaje + "\n... Comprobante(s) ya informado(s) ...  \n";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mensaje = mensaje + "... Error SQL " + e.getMessage() + "...\n";
        } catch (Exception e) {
            e.printStackTrace();
            mensaje = mensaje + "... Error " + e.getMessage() + "...\n";
        }
    }
     // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="actualizarAnulados">
    public static String actualizarAnulados(String empresa, String range, String number, Integer id, String usuario) {
        try {
            ArrayList<Integer> i = new ArrayList();
            i.add(id);

            Array resultArr = conPostgres.createArrayOf("int8", i.toArray());

            clsEnvioCPE cpe = new clsEnvioCPE(usuarioSol, passwordSol, conPostgres);
            clsEnvioCPE.actualizarCPE(empresa, range, number, "", "ENV", "COMPROBANTEANULADO.zip", "NOW()", usuario, "", "", "", "");
            clsEnvioCPE.actualizarCPE(empresa, range, number, "", "PEN", "", "NOW()", usuario, "COMPROBANTEANULADO.zip", "NOW()", usuario, "");
            liberarTicket(empresa, resultArr, "'Comprobante Anulado'");// liberar anulados o resumen para volver a enviar

            mensaje = mensaje + "\n ...Comprobante Anulado  ... " + range + "-" + number + " \n";

        } catch (Exception e) {
            e.printStackTrace();
            mensaje = mensaje + "... Error " + e.getMessage() + "...\n";
        } finally {
            return mensaje;
            //  clsConexion.cerrarConexion(conPostgres);
        }
    }
     // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="insertarServidorExterno">
    public static void insertarServidorExterno(String empresa, String tipo, String serie, String numero, Array id) {

        try {
            // Enviar todo
            CallableStatement callsec;
            ResultSet rssec;
            sql = "SELECT * FROM " + nombreEsquema + ".fun_fel_liberar_envio ("
                    + "'" + empresa + "')";
            System.out.println(sql);
            callsec = conPostgres.prepareCall(sql);
            callsec.execute();
            callsec.close();

        } catch (SQLException e) {
            e.printStackTrace();
            clsConexion.deshacerTransaccion();
            mensaje = mensaje + "... Error SQL " + e.getMessage() + "...\n";
        } catch (Exception e) {
            e.printStackTrace();
            clsConexion.deshacerTransaccion();
            mensaje = mensaje + "... Error " + e.getMessage() + "...\n";
        } finally {
            clsConexion.cerrarConexion(conPostgres);
        }
    }
    // </editor-fold>

}

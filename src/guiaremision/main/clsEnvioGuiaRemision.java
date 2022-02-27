/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guiaremision.main;

import guiaremision.guiaremision.clsComunicacionBaja;
import guiaremision.guiaremision.clsComunicacionBajaItem;
import guiaremision.guiaremision.clsGuia;
import guiaremision.guiaremision.clsGuiaItem;
import guiaremision.ubl.clsFirmarUBL;
import guiaremision.ubl.clsGenerarUBLComunicacionBaja;
import guiaremision.ubl.clsGenerarUBLGuia;
import guiaremision.webservice.clsEnvioGuiaCPE;
import factuyaenvio.herramientas.clsComprimirUBL;
import factuyaenvio.herramientas.clsConexion;
import factuyaenvio.herramientas.clsParametrosFactuya;
import java.io.File;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import factuyaenvio.utilitario.Utilitario;

/**
 *
 * @author glory
 */
public class clsEnvioGuiaRemision {

    private static String sql;
    private static ResultSet rs;
    public boolean rpta = true;
    private static CallableStatement call;
    private static String nameFileXML;
    private static String ruc;
    private static String serie;
    private static String numero;
    private static String usuario;
    private static String fechaBaja;
    private static String tipoEnvio;
    private static String fecha;
    private static Integer bloque;

    private static Connection conPostgres = null;

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
    
    public static String mensaje = "";

    public static void main(String[] args) {
        try {
            String empresa = args[0]; ////  "20498596356";
              String ubicacionContext = Utilitario.ubicacionProyecto() + "/META-INF/context.xml";//"C:\\Program Files\\Apache Software Foundation\\Apache Tomcat 7.0.34\\webapps\\Factuya"

           // String empresa = "20603132271"; //args[0]; ////  "20498596356";
         //   String ubicacionContext = "C:\\Program Files\\Apache Software Foundation\\Apache Tomcat 7.0.34\\webapps\\Factuya" /*Utilitario.ubicacionProyecto() */ + "/META-INF/context.xml";//"C:\\Program Files\\Apache Software Foundation\\Apache Tomcat 7.0.34\\webapps\\Factuya"
            clsParametrosFactuya obtenerParametros = new clsParametrosFactuya(empresa, ubicacionContext);
            obtenerParametros.cargarParametros();
            obtenerParametros.cargarConexion("jdbc/pgpool_factuya");

            conPostgres = clsConexion.obtenerConexion(clsParametrosFactuya.host, clsParametrosFactuya.usuarioBD, clsParametrosFactuya.passwordBD);

            clsEnvioGuiaRemision enviar = new clsEnvioGuiaRemision(
                    clsParametrosFactuya.numerRUC,//"20498596356",
                    "T001",//"B001",
                    "794",//"12041",
                    null,// ARRARY ID
                    "admin",//"admin",
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

            enviarUnaGuia();
           // enviar.enviarFacturasPendientes(clsParametrosFactuya.numerRUC);

        } finally {
            clsConexion.cerrarConexion(conPostgres);

        }
    }


    // <editor-fold defaultstate="collapsed" desc="clsEnvioGuiaRemision">
    public clsEnvioGuiaRemision(String _ruc, String _serie, String _numero, ArrayList idComprobantes, String _usuario,
            Integer enviarSUNAT,
            Boolean enviarResumenDiario,
            String ubicacionCertificado,
            String nombreCertificado,
            String passwordCertificado,
            String usuarioSol,
            String passwordSol,
            Boolean subirServidor,
            String ubicacionPrincipal,
            String ubicacionServidor,
            String ubicacionSunatEnvio,
            String ubicacionSunatRespuesta,
            String nombreEsquema,
            String detracionCuenta,
            Connection conx) {

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

        System.out.println("aqui");

    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="enviarUnaGuia">
    public static String enviarUnaGuia() {
        if (conPostgres == null) {
            mensaje = mensaje + "\n...No existe conexion a DB ...";
            return mensaje;
        }
        try {
            mensaje = mensaje + "\n... Generando ..." + ruc + " " + serie + "-" + numero + " \n";
            clsGenerarUBLGuia guia = new clsGenerarUBLGuia(cargarGuia(ruc, serie, numero));
            byte[] escribirXML = guia.escribirXMLByte();

            byte[] firmarXML = firmarByteCPE(escribirXML);
            clsEnvioGuiaRemision.enviarCPE("invoice", firmarXML, null, null);
            mensaje = mensaje + "\n";

        } catch (Exception ex) {
            mensaje = mensaje + "... Error " + ex.getMessage() + "...\n";
        } finally {
            clsConexion.cerrarConexion(conPostgres);
            return mensaje;
        }
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="enviarGuiaResumenDiarioComunicacionBaja">
    public static String enviarGuiaResumenDiarioComunicacionBaja() {

        try {
            if (idComprobantes != null) {
                conPostgres = clsConexion.obtenerConexion();
                Array resultArr = conPostgres.createArrayOf("int8", idComprobantes.toArray());
                enviarResumen(ruc, resultArr);
                clsConexion.cerrarConexion(conPostgres);
            }
        } catch (SQLException ex) {
            mensaje = mensaje + "... Error " + ex.getMessage() + "...\n";
        }

        try {
            Array resultArr = conPostgres.createArrayOf("int8", idComprobantes.toArray());

            if (resultArr != null) {

                enviarResumen(ruc, resultArr);
            }

        } catch (SQLException ex) {
            mensaje = mensaje + "... Error " + ex.getMessage() + "...\n";
        } finally {
            clsConexion.cerrarConexion(conPostgres);
            return mensaje;
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="firmarByteCPE">

    private static byte[] firmarByteCPE(byte[] writeXML) {
        clsFirmarUBL sign = new clsFirmarUBL();
        try {
            sign.uploadXMLByte(writeXML);
            sign.singUBL("PKCS12", new File(ubicacionCertificado, nombreCertificado), passwordCertificado);

        } catch (Exception e) {

            mensaje = mensaje + "... Error " + e.getMessage() + "...\n";

        }
        return sign.outputXMLByte();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="enviarCPE">
    private static void enviarCPE(String type, byte[] signXML, String classification, Integer block) {
        String nameFileZip = nameFileXML + ".zip";
        String nameFileZipAnswer = nameFileXML + "-R.zip";

        String pathZip = subirServidor == true ? ubicacionPrincipal + ubicacionSunatEnvio : ubicacionServidor + ubicacionSunatEnvio;
        String pathZipAnwser = subirServidor == true ? ubicacionPrincipal + ubicacionSunatRespuesta : ubicacionServidor + ubicacionSunatRespuesta;

        clsComprimirUBL compress = new clsComprimirUBL();
        compress.comprimirXMLByte(signXML, nameFileXML + ".xml", new File(pathZip, nameFileZip));

        clsEnvioGuiaCPE cpe = new clsEnvioGuiaCPE(usuarioSol, passwordSol,enviarResumenDiario,nombreEsquema, conPostgres);
        if (type.equals("invoice")) {
            mensaje = mensaje + "... Enviando ..." + nameFileZip + " \n";
            String answercode = cpe.sendInvoice(new File(pathZip, nameFileZip), new File(pathZipAnwser, nameFileZipAnswer), ruc, serie, numero, usuario);
            mensaje = mensaje + "... Respuesta ..." + nameFileZip + " \n" + answercode + " \n";

        } else {
            mensaje = mensaje + "... Enviando ..." + nameFileZip + " \n";
            String answercode = cpe.sendSummary(type, classification, block, new File(pathZip, nameFileZip), pathZipAnwser, nameFileZipAnswer, ruc, fecha, usuario);
            mensaje = mensaje + "... Respuesta ..." + nameFileZip + " " + answercode + " \n";

        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="enviarCPE">
    private static void enviarCPE() {
        String nameFileZip = nameFileXML + ".zip";
        String nameFileZipAnswer = nameFileXML + "-R.zip";
        String pathZip = subirServidor == true ? ubicacionPrincipal + ubicacionSunatEnvio : ubicacionServidor + ubicacionSunatEnvio;
        String pathZipAnwser = subirServidor == true ? ubicacionPrincipal + ubicacionSunatRespuesta : ubicacionServidor + ubicacionSunatEnvio;

        clsEnvioGuiaCPE cpe = new clsEnvioGuiaCPE(usuarioSol, passwordSol, conPostgres);
        cpe.sendInvoice(new File(pathZip, nameFileZip), new File(pathZipAnwser, nameFileZipAnswer), ruc, serie, numero, usuario);

    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="cargarGuia">
    private static clsGuia cargarGuia(String empresa, String serie, String numero) {
        Boolean flag = true;
        clsGuia guia = null;
        Vector<clsGuiaItem> Items = new Vector<clsGuiaItem>();
        try {
            sql = "SELECT * FROM " + nombreEsquema + ".fun_gel_consulta_guiaremision("
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
                            guia = new clsGuia();
                            guia.setEmisor_RUC(rs.getString("empresa_ruc"));
                            guia.setEmisor_RazonSocial(rs.getString("empresa_razonsocial"));
                            guia.setEmisor_DomicilioFiscal(rs.getString("empresa_direccion"));
                            guia.setEmisor_DomicilioFiscalDepartamento(rs.getString("empresa_departamento"));
                            guia.setEmisor_DomicilioFiscalDistrito(rs.getString("empresa_distrito"));
                            guia.setEmisor_DomicilioFiscalProvincia(rs.getString("empresa_provincia"));
                            guia.setEmisor_DomicilioFiscalUBIGEO(rs.getString("empresa_ubigeo"));
                            guia.setSerie_numero(rs.getString("serie_numero"));
                            guia.setFecha_emision(rs.getString("fecha_emision"));
                            guia.setTipo_documento(rs.getString("tipo_documento"));
                            guia.setObservacion(rs.getString("observacion"));
                            guia.setBaja_numero_documento(rs.getString("baja_numero_documento"));
                            guia.setBaja_codigo_tipo_documento(rs.getString("baja_codigo_tipo_documento"));
                            guia.setBaja_tipo_documento(rs.getString("baja_tipo_documento"));
                            guia.setAdicional_numero_documento(rs.getString("adicional_numero_documento"));
                            guia.setAdicional_codigo_tipo_documento(rs.getString("adicional_codigo_tipo_documento"));
                            guia.setRemitente_numero_identidad(rs.getString("remitente_numero_identidad"));
                            guia.setRemitente_tipo_documento(rs.getString("remitente_tipo_documento"));
                            guia.setRemitente_apellidos_nombres_razon_social(rs.getString("remitente_apellidos_nombres_razon_social"));
                            guia.setDestinatario_numero(rs.getString("destinatario_numero"));
                            guia.setDestinatario_tipo_documento(rs.getString("destinatario_tipo_documento"));
                            guia.setDestinatario_apellidos_nombres_razon_social(rs.getString("destinatario_apellidos_nombres_razon_social"));
                            guia.setEstablecimiento_numero_identidad(rs.getString("establecimiento_numero_identidad"));
                            guia.setEstablecimiento_tipo_documento(rs.getString("establecimiento_tipo_documento"));
                            guia.setEstablecimiento_apellidos_nombres_razon_social(rs.getString("establecimiento_apellidos_nombres_razon_social"));
                            guia.setEnvio_motivo_traslado(rs.getString("envio_motivo_traslado"));
                            guia.setEnvio_motivo_traslado_descripcion(rs.getString("envio_motivo_traslado_descripcion"));
                            guia.setEnvio_indicador_transbordo(rs.getString("envio_indicador_transbordo"));
                            guia.setEnvio_unidad_medida(rs.getString("envio_unidad_medida"));
                            guia.setEnvio_peso_bruto(rs.getString("envio_peso_bruto"));
                            guia.setEnvio_numero_bultos(rs.getString("envio_numero_bultos"));
                            guia.setEnvio_modalidad_traslado(rs.getString("envio_modalidad_traslado"));
                            guia.setEnvio_fecha_traslado(rs.getString("envio_fecha_traslado"));
                            guia.setTransportista_numero_identidad(rs.getString("transportista_numero_identidad"));
                            guia.setTransportista_tipo_documento(rs.getString("transportista_tipo_documento"));
                            guia.setTransportista_apellidos_nombres_razon_social(rs.getString("transportista_apellidos_nombres_razon_social"));

                            guia.setConductor_numero_identidad(rs.getString("conductor_numero_identidad"));
                            guia.setConductor_tipo_documento(rs.getString("conductor_tipo_documento"));
                            guia.setVehiculo_numero_placa(rs.getString("vehiculo_numero_placa"));
                            guia.setLlegada_ubigeo(rs.getString("llegada_ubigeo"));
                            guia.setLlegada_direccion(rs.getString("llegada_direccion"));
                            guia.setContenedor_datos(rs.getString("contenedor_datos"));
                            guia.setPartida_ubigueo(rs.getString("partida_ubigueo"));
                            guia.setPartida_direccion(rs.getString("partida_direccion"));
                            guia.setPuerto_codigo(rs.getString("puerto_codigo"));

                            flag = false;
                            nameFileXML = rs.getString("empresa_ruc") + "-" + rs.getString("tipo_documento") + "-" + rs.getString("serie_numero");
                            // usuario = rs.getString("usuario");

                        }

                        clsGuiaItem item = new clsGuiaItem();

                        item.setBien_cantidad(rs.getString("bien_cantidad"));
                        item.setBien_unidad(rs.getString("bien_unidad"));
                        item.setBien_descripcion(rs.getString("bien_descripcion"));
                        item.setBien_codigo("1000");
                        Items.add(item);
                        guia.setItems(Items);
                    }

                }
            }
        } catch (SQLException e) {
            mensaje = mensaje + "... Error SQL " + e.getMessage() + "...\n";
            //progress(message);
        } catch (Exception e) {
            e.printStackTrace();
            mensaje = mensaje + "... Error " + e.getMessage() + "...\n";
            // progress(message);
        }
        return guia;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="clsComunicacionBaja">
    private static clsComunicacionBaja loadVoided(String empresa, String fecha, Array id) {
        clsComunicacionBaja voided = new clsComunicacionBaja();
        Boolean flag = true;
        String arrId = id == null ? null : "'" + id + "'";
        Vector<clsComunicacionBajaItem> items = new Vector<clsComunicacionBajaItem>();
        try {
            sql = "SELECT * FROM " + nombreEsquema + ".fun_gel_consultar_baja_guia("
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
                            voided.setCodigo(rs.getString("nombrearchivo"));
                            voided.setEmisorNroDocumento(rs.getString("ruc"));
                            voided.setEmisorRazonSocial(rs.getString("razonsocial"));
                            voided.setFechaGeneracionDocumento(rs.getString("fechageneraciondocumento"));
                            voided.setFechaGeneracionComunicado(rs.getString("fechageneracioncomunicado"));
                            nameFileXML = rs.getString("ruc") + "-" + rs.getString("nombrearchivo");
                            usuario = "SISTEMA";
                            fechaBaja = rs.getString("fechageneraciondocumento");
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
                            voided.setItems(items);
                        }
                    }
                }
            }
            rs.close();
        } catch (SQLException e) {
            mensaje = mensaje + "... Error SQL " + e.getMessage() + "...\n";
            //   progress(message);
        } catch (Exception e) {
            e.printStackTrace();
            mensaje = mensaje + "... Error " + e.getMessage() + "...\n";
            //progress(message);
        }
        return voided;

    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="enviarResumen">
    private static void enviarResumen(String empresa, Array id) {
        try {
            // Enviar todo
            CallableStatement callsec;
            ResultSet rssec;
            Connection con = clsConexion.obtenerConexion();

            String arrId = id == null ? null : "'" + id + "'";
            sql = "SELECT * FROM " + nombreEsquema + ".fun_gel_consultar_pendiente ("
                    + "'" + empresa + "',"
                    + arrId + ""
                    + ")";

            System.out.println(sql);
            mensaje = mensaje + "\n... Inicio Comunicacion de Baja ...\n";
            // progress(message);
            callsec = conPostgres.prepareCall(sql);
            if (callsec.execute()) {
                rssec = callsec.getResultSet();
                if (rssec != null) {
                    while (rssec.next()) {

                        String dateSec = rssec.getString("fechaemision");
                        String classification = rssec.getString("clasificacion");

                        if (rssec.getString("nroticket") == null) {
                            String tipo = rssec.getString("tipo");

                            /* if (tipo.equals("RC")) {
                             Double quantity = rssec.getDouble("cantidad");
                             while (quantity > 0) {
                             System.out.println("Crear CPE ... " + Profile.getInstance().getLocateServer() + rssec.getString("nombrearchivo"));
                             GenerateUBLSummaryDocument generate = new GenerateUBLSummaryDocument(loadSummaryDocument(empresa, dateSec, classification, id));
                             if (generate != null) {
                             fecha = dateSec;

                             byte[] writeXML = generate.writeXMLByte();
                             byte[] signXML = signByteCPE(writeXML);
                             sendCPE(classification.equals("") || classification.equals("NC") ? "RC" : classification, signXML, classification, bloque);
                             }
                             quantity = quantity - 1;
                             }
                             }*/
                            if (tipo.equals("RA")) {
                                // File filerc = new File(parametroRuta + rssec.getString("nombrearchivorc") + "-1-R.zip");
                                // LEER AACEPTADO
                                // System.out.println("Crepar CPE RC ... " + Profile.getInstance().getLocateServer() + rssec.getString("nombrearchivorc") + "-1-R.zip");
                                clsComunicacionBaja baja = loadVoided(empresa, dateSec, id);
                                if (baja.getCodigo() != null) {
                                    clsGenerarUBLComunicacionBaja generateVoided = new clsGenerarUBLComunicacionBaja(baja);
                                    fecha = fechaBaja;
                                    byte[] writeXMLVoided = generateVoided.writeXMLByte();
                                    byte[] signXMLVoided = firmarByteCPE(writeXMLVoided);
                                    enviarCPE("RA", signXMLVoided, "", bloque);
                                }
                            }
                        }

                    }
                }
            }
            mensaje = mensaje + "\n...  Fin Resumen Diario y Comunicacion de Baja ...\n";
            //progress(message);
            clsConexion.cerrarConexion(con);

        } catch (SQLException ex) {
            System.out.println("SQL E " + ex.getSQLState());
            ex.printStackTrace();
            mensaje = mensaje + "... Error SQL " + ex.getMessage() + "...\n";
            // progress(message);
        } catch (Exception ex) {
            System.out.println("E " + ex.getMessage());
            ex.printStackTrace();
            mensaje = mensaje + "... Error " + ex.getMessage() + "...\n";
            // progress(message);
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="seeMissingInvoice">
    private static void seeMissingInvoice(String empresa) {
        try {
            // Enviar todo
            CallableStatement callsec;
            ResultSet rssec;
            Connection con = clsConexion.obtenerConexion();
            sql = "SELECT * FROM " + nombreEsquema + ".fun_gel_consulta_facturacionelectronica_pendiente ("
                    + "'" + empresa + "')";
            callsec = con.prepareCall(sql);
            if (callsec.execute()) {
                rssec = callsec.getResultSet();
                if (rssec != null) {
                    while (rssec.next()) {
                        serie = rssec.getString("serie");
                        numero = rssec.getString("numero");
                        ruc = rssec.getString("ruc");
                        String rpta = "-1";
                        // FALTA
                        File file = new File(ubicacionPrincipal + rssec.getString("nombrearchivo") + ".zip");
                        // COMPROBAR SI EXISTE ARCHIVO DE REPUESTA de FACTURA
                        if (serie.substring(0, 1).equals("F")) {
                            File filerpta = new File(ubicacionPrincipal + rssec.getString("nombrearchivo") + "-R.zip");
                            if (filerpta.exists()) {
                                rpta = Utilitario.searchAnswer(ubicacionPrincipal, rssec.getString("nombrearchivo") + "-R.zip", "cbc:ResponseCode");

                            }
                        }
                        if (!rpta.equals("0")) {// archivo de respuesta diferente de aceptado
                            //  System.out.println("Mostar CPE ... " + Profile.getInstance().getLocateServer() + rssec.getString("nombrearchivo") + ".zip");
                            if (file.exists() && rssec.getString("estado") != null) {
                                System.out.println("Enviar Archivo CPE ... " + rssec.getString("nombrearchivo") + ".zip");
                                nameFileXML = rssec.getString("nombrearchivo");
                                usuario = "SISTEMA";
                                enviarCPE();
                            } else if (!file.exists() && rssec.getString("estado") == null) { // SI NO EXISTE EN COMPROBNTESDEPAGOELECTRONICO
                                System.out.println("Insertar CPE EN BD ... " + rssec.getString("nombrearchivo") + ".zip");
                                clsEnvioGuiaCPE cpe = new clsEnvioGuiaCPE(usuarioSol, passwordSol, conPostgres);

                                cpe.insertCPE(ruc, serie, numero, "", "INI", "NOW()", "NULL", "SISTEMA", "SISTEMA");

                            } else if (!file.exists() && rssec.getString("rutaxml") == null) {
                                /*
                                 System.out.println("Crear Archivo CPE ... " + rssec.getString("nombrearchivo") + ".zip");
                                 GenerateUBLGuia generate = new GenerateUBLGuia(loadInvoice(ruc, serie, numero));
                                 byte[] writeXML = generate.writeXMLByte();
                                 byte[] signXML = signByteCPE(writeXML);
                                 usuario = "SISTEMA";
                                 sendCPE("invoice", signXML, null, null);*/

                            }
                        } else {
                            clsEnvioGuiaCPE.updateCPE(ruc, serie, numero, "", "ACE", "", "", "", rssec.getString("nombrearchivo") + "-R.zip", "NOW()", "SISTEMA", "");

                        }

                        serie = null;
                        numero = null;
                    }
                }
            }

            clsConexion.cerrarConexion(con);
        } catch (SQLException e) {
            mensaje = mensaje + "... Error SQL " + e.getMessage() + "...\n";
            // progress(message);
        } catch (Exception e) {
            e.printStackTrace();
            mensaje = mensaje + "... Error " + e.getMessage() + "...\n";
            //  progress(message);
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="seeMissingTicket">
    public static void seeMissingTicket(String empresa) {
        try {
            // Enviar todo
            CallableStatement callsec;
            ResultSet rssec;
            Connection con = clsConexion.obtenerConexion();
            String pathZip = subirServidor == true ? ubicacionPrincipal + ubicacionSunatEnvio : ubicacionServidor + ubicacionSunatEnvio;
            String pathZipAnwser = subirServidor == true ? ubicacionPrincipal + ubicacionSunatRespuesta : ubicacionServidor + ubicacionSunatEnvio;

            sql = "SELECT * FROM " + nombreEsquema + ".fun_gel_consultar_ticket_pendiente ("
                    + "'" + empresa + "')";
            System.out.println(sql);
            callsec = con.prepareCall(sql);
            if (callsec.execute()) {
                rssec = callsec.getResultSet();
                if (rssec != null) {
                    while (rssec.next()) {
                        String salidaRespuesta = rssec.getString("rutaxml") + "-R.zip";
                        clsEnvioGuiaCPE cpe = new clsEnvioGuiaCPE(usuarioSol, passwordSol, conPostgres);
                        cpe.updateSummary(pathZipAnwser, salidaRespuesta, empresa, rssec.getString("tipo"), rssec.getString("nroticket"), "NOW()", "SISTEMA", "");
                    }
                }

            }

            clsConexion.cerrarConexion(con);
        } catch (SQLException e) {
            e.printStackTrace();
            mensaje = mensaje + "... Error SQL " + e.getMessage() + "...\n";
            //  progress(message);
        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
            mensaje = mensaje + "... Error " + e.getMessage() + "...\n";
            // progress(message);
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="seeMissing">
    public static void seeMissing(String empresa, String tipo, String serie, String numero) {
      /*  try {
            ruc = empresa;
            if (tipo.equals("-")) { // enviar 1 a 1
                new clsEnvioGuiaRemision(empresa, serie, numero);
            }
            if (tipo.equals("PEN")) {
                seeMissingInvoice(empresa);
            }
            if (tipo.equals("RR")) {

                seeMissingTicket(empresa);
                enviarResumen(empresa, null);
                seeMissingTicket(empresa);
            }
        } catch (Exception ex) {
            System.out.println(" " + ex);
            ex.printStackTrace();
           
        }*/

    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="updateFreeTicket">
    public static void updateFreeTicket(String empresa, Array id, String rpta) {
        try {

            String sql;
            sql = " { call " + nombreEsquema + ".fun_gel_liberar_ticket ("
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
    
    // <editor-fold defaultstate="collapsed" desc="updateVoided">
    public static void updateVoided(String empresa, String range, String number, Integer id) {
        try {
            ArrayList<Integer> i = new ArrayList();
            i.add(id);
            Array resultArr = conPostgres.createArrayOf("int8", i.toArray());
            clsEnvioGuiaCPE.updateCPE(empresa, range, number, "", "ENV", "COMPROBANTEANULADO.zip", "NOW()", "SISTEMA", "", "", "", "");
            clsEnvioGuiaCPE.updateCPE(empresa, range, number, "", "PEN", "", "NOW()", "SISTEMA", "COMPROBANTEANULADO.zip", "NOW()", "SISTEMA", "");
            updateFreeTicket(empresa, resultArr, "'Comprobante Anulado'");// liberar anulados o resumen para volver a enviar

            mensaje = mensaje + "\n ...Comprobante Anulado  ... " + range + "-" + number + " \n";

            // updateCPE(ruc, serie, numero, "", state, "", "", "", answerFile.getName(), "NOW()", "SISTEMA", errDesc);
        } catch (Exception e) {
            e.printStackTrace();
            mensaje = mensaje + "... Error " + e.getMessage() + "...\n";
        }
    }
    // </editor-fold>

}

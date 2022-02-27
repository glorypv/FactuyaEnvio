/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package factuya.webservice;

import factuyaenvio.herramientas.clsConexion;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import java.sql.Connection;
import sunat.StatusResponse;
import factuyaenvio.utilitario.Utilitario;

/**
 *
 * @author Glory
 */
public class clsEnvioCPE {

    private static String userSOL = "";
    private static String passwordSOL = "";
    private static String signatureValue = null;
    private static String digestValue = null;

    private static String ruc;
    private static String range;
    private static String number;
    private static String user;
    private static CallableStatement call;

    private static PreparedStatement st;
    private static String sql;
    private static ResultSet rs;

    private static Connection conPostgres = null;

    private static Boolean enviarResumenDiario=false;
    private static String nombreEsquema="";
    public static void main(String[] args) {
        //  enviarFactura(new File("C:/Temp/", "20498455370-01-F003-00056647.zip"), new File("C:/Temp/", "R-20498455370-01-F003-00056647.zip"));
    }

    public clsEnvioCPE(String user, String password, Connection conx) {
        this.userSOL = user;
        this.passwordSOL = password;
        this.conPostgres = conx;
    }
    
     public clsEnvioCPE(String user, String password, Boolean enviarResumenDiario,String nombreEsquema,Connection conx) {
        this.userSOL = user;
        this.passwordSOL = password;
        this.conPostgres = conx;
        this.enviarResumenDiario=enviarResumenDiario;
        this.nombreEsquema=nombreEsquema;
    }

    public String enviarFactura(File invoiceFile, File answerFile, String ruc, String range, String number, String user) {
        String answerCode = "", detail = "", answer = "";
        byte[] answerByte = null;
        signatureValue = null;
        digestValue = null;
        try {
            try {
                //  insertarCPE(ruc, range, number, "", "ENV", "NOW()", invoiceFile.getName(), user, user);
                obtainValue(invoiceFile);
                clsConexion.inicarTransaccion();
                if (range.substring(0, 1).equals("B") && enviarResumenDiario) {
                    actualizarCPE(ruc, range, number, "", "INI", invoiceFile.getName(), "", user, "", "", "", "");
                    answerCode = "0";
                } else {
                    DataSource fds = new FileDataSource(invoiceFile);//C:/Temp/20100113612-RC-20160824-001.zip
                    DataHandler handler = new DataHandler(fds);

                    actualizarCPE(ruc, range, number, "", "ENV", invoiceFile.getName(), "NOW()", user, "", "", "", "");

                    answerByte = sendBill(invoiceFile.getName(), handler, null);//20100113612-RC-20160824-001.zip

                    FileOutputStream fos = new FileOutputStream(answerFile);
                    fos.write(answerByte);
                    fos.close();
                    Utilitario.contexto = "";
                    answerCode = Utilitario.searchAnswer(answerByte, "cbc:ResponseCode");

                    Utilitario.contexto = "";
                    detail = Utilitario.searchAnswer(answerByte, "cbc:Note");
                    detail = detail.replace("'", "");
                    Utilitario.contexto = "";
                    answer = Utilitario.searchAnswer(answerByte, "cbc:Description");
                    answer = answer.replace("'", "");

                    System.out.print(answer);
                    if (answerCode.equals("")) {
                        actualizarCPE(ruc, range, number, "", "ERR", "", "", "", answerFile.getName(), "NOW()", user, answerCode + " - " + "" + " " + answer + " " + detail);
                    } else if (answerCode.equals("0") || answerCode.equals("2380")) {
                        String state = answerCode.equals("0") || answerCode.equals("1033") ? "ACE" : "OBS";
                        actualizarCPE(ruc, range, number, "", state, "", "", "", answerFile.getName(), "NOW()", user, answerCode + " " + answer + " " + detail);
                    } else if (Integer.valueOf(answerCode.toString()) >= 2000 && Integer.valueOf(answerCode.toString()) <= 3999) {
                        actualizarCPE(ruc, range, number, "", "REC", "", "", "", answerFile.getName(), "NOW()", user, answerCode + " " + answer + " " + detail);
                    } else {
                        actualizarCPE(ruc, range, number, "", "PEN", "", "", "", "", "NOW()", user, answerCode + " " + answer);
                    }

                }

                clsConexion.finalizarTransaccion();
                System.out.println("---" + answerCode);
            } catch (javax.xml.ws.soap.SOAPFaultException ex) {
                String errorCode = ex.getFault().getFaultCodeAsQName().getLocalPart();
                answerCode = errorCode;
                System.out.println("Error" + errorCode);

                System.out.println(ex.getMessage());
                if (answerCode.equals("Client.1033")) {
                    String state = "ACE";
                    String errDesc = buscarCodigoError(answerCode.replace("Client.", ""));

                    actualizarCPE(ruc, range, number, "", state, "", "", "", answerFile.getName(), "NOW()", user, errDesc);

                    clsConexion.finalizarTransaccion();
                    answerCode = errDesc;

                } else if (answerCode.equals("Client.1032")) {// comunicacion debaja rechazado
                    String errDesc = buscarCodigoError(answerCode.replace("Client.", ""));

                    String state = "ACE";
                    actualizarCPE(ruc, range, number, "", state, "", "", "", answerFile.getName(), "NOW()", user, errDesc);

                    clsConexion.finalizarTransaccion();
                    answerCode = errDesc;

                } else {
                    answerCode = answerCode.replace("Client.", "");
                    String state = "ERR";
                    String errDesc = buscarCodigoError(answerCode.replace("Client.", ""));
                    if (errDesc.equals("")) {
                        errDesc = "SIN RPTA WS";
                    }

                    if (Integer.valueOf(answerCode.replace("Client.", "")) >= 2000 && Integer.valueOf(answerCode.replace("Client.", "")) <= 3999) {
                        state = "REC";
                    }
                    actualizarCPE(ruc, range, number, "", state, "", "", "", answerFile.getName(), "NOW()", user, answerCode + " - " + errDesc + " " + answer + " " + detail);
                    answerCode = errDesc;
                    clsConexion.finalizarTransaccion();

                    //  Main.roolbackTransaction();
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            answerCode = " ERROR DE SUNAT :" + e.getMessage();

        }
        return answerCode + " " + answer + " " + detail;
    }

    public String insertarCPE(String ruc, String serie, String numero, String fecha, String estado, String fechaenvio, String ruta, String idusuario, String usucreacion) {
        String codigo = "";
        try {
            PreparedStatement st;
            String sql;
            sql = " CALL  insert_comprobantepagoelectronico ("
                    + "'" + ruc + "',"
                    + "'" + serie + "',"
                    + "" + String.valueOf(numero.equals("") ? "null" : numero) + ","
                    + "" + String.valueOf(fecha.equals("") ? "null" : "'" + fecha + "'") + ","
                    + "'" + estado + "',"
                    + "" + String.valueOf(fechaenvio.equals("") ? "null" : "" + fechaenvio + "") + ","
                    + "'" + ruta + "',"
                    + "'" + idusuario + "',"
                    + "'" + usucreacion + "')";
            System.out.println(sql);
            st = conPostgres.prepareStatement(sql);
            st.executeUpdate();
            st.close();
            codigo = "OK";
        } catch (SQLException ex) {

            ex.printStackTrace();
            codigo = String.valueOf(ex.getErrorCode());
        }
        return codigo;
    }

    public static String actualizarCPE(String ruc, String serie, String numero, String fecha, String estado, String rutaxml, String fechaenvio, String idusuario, String ruta, String fecharecepcion, String usumodificacion, String observacion) {
        try {
            sql = "{ call " + nombreEsquema + ".fun_fel_actualizar_facturaelectronica ("
                    + "'" + ruc + "',"
                    + "'" + serie + "',"
                    + "" + String.valueOf(numero.equals("") ? "null" : "'" + numero + "'") + ","
                    + "" + String.valueOf(fecha.equals("") ? "null" : "'" + fecha + "'") + ","
                    + "'" + estado + "',"
                    + "" + String.valueOf(rutaxml.equals("") ? "null" : "'" + rutaxml + "'") + ","
                    + "" + String.valueOf(fechaenvio.equals("") ? "null" : fechaenvio.equals("NOW()") ? "now()::timestamp" : "'" + fechaenvio + "'") + ","
                    + "'" + ruta + "',"
                    + "" + String.valueOf(fecharecepcion.equals("") ? "null" : fecharecepcion.equals("NOW()") ? "now()::timestamp" : "'" + fecharecepcion + "'") + ","
                    + "" + String.valueOf(signatureValue == (null) ? "null" : "'" + signatureValue + "'") + ","
                    + "" + String.valueOf(digestValue == (null) ? "null" : "'" + digestValue + "'") + ","
                    + "'" + idusuario + "',"
                    + "'" + usumodificacion + "',"
                    + "'" + observacion + "'"
                    + ")}";
            System.out.println(sql);
            call = conPostgres.prepareCall(sql);
            call.execute();
            call.close();

            return "OK";
        } catch (SQLException ex) {

            ex.printStackTrace();
            return String.valueOf(ex.getErrorCode());
        }
    }

    public String enviarResumenDiario(String tipo, String clasificacion, Integer bloque, File comprobante, File comprobaterespuesta, String ruc, String fecha, String idusuario) {
        String answerCode = null;
        String ticket = null;
        try {
            DataSource fds = new FileDataSource(comprobante);//C:/Temp/20100113612-RC-20160824-001.zip
            DataHandler handler = new DataHandler(fds);
            try {

                clsConexion.inicarTransaccion();

                actualizarTicket(ruc, tipo, clasificacion, bloque, fecha, "", "ENV", comprobante.getName(), "NOW()", idusuario, "", "", "", "");

                ticket = sendSummary(comprobante.getName(), handler, null);
                actualizarTicket(ruc, tipo, clasificacion, bloque, fecha, ticket, "PRO", "", "", "", "", "NOW()", idusuario, "");
                System.out.println("Ticket" + ticket);
                clsConexion.finalizarTransaccion();
                // getStatus(answerCode);

                String rpta = getStatus(ticket).getStatusCode();
                answerCode = rpta;
                System.out.println("Rpta.." + rpta);
                clsConexion.inicarTransaccion();

                if (rpta.equals("0") || rpta.equals("99")) {

                    FileOutputStream fos = new FileOutputStream(comprobaterespuesta);
                    fos.write(getStatus(ticket).getContent());
                    fos.close();

                    Utilitario.contexto = "";
                    String msjRpta = Utilitario.searchAnswer(comprobaterespuesta.getAbsolutePath(), comprobaterespuesta.getName(), "cbc:Description");
                    Utilitario.contexto = "";
                    String codigoRpta = Utilitario.searchAnswer(comprobaterespuesta.getAbsolutePath(), comprobaterespuesta.getName(), "cbc:ResponseCode");
                    msjRpta = msjRpta.replace("'", "");

                    String estado = codigoRpta.equals("0") ? "ACE" : "REC";
                    if (rpta.equals("99")) {
                        if (Integer.valueOf(codigoRpta.toString()) >= 2000 && Integer.valueOf(codigoRpta.toString()) <= 3999) {
                            estado = "REC";
                        } else {
                            estado = "PEN";
                        }
                    }
                    actualizarTicket(ruc, tipo, clasificacion, bloque, fecha, ticket, estado, "", "", "", comprobaterespuesta.getName(), "NOW()", idusuario, codigoRpta + " " + msjRpta);

                }
                if (rpta.equals("0098")) {
                    String estado = "PRO";
                    actualizarTicket(ruc, tipo, clasificacion, bloque, fecha, ticket, estado, "", "", "", "", "NOW()", idusuario, "");

                }
                clsConexion.finalizarTransaccion();

            } catch (javax.xml.ws.soap.SOAPFaultException ex) {
                String errorCode = ex.getFault().getFaultCodeAsQName().getLocalPart();
                answerCode = errorCode;
                System.out.println(errorCode);
                if (errorCode.equals("0130")) {
                    clsConexion.deshacerTransaccion();
                }
                // Main.endTransaction();
            }

        } catch (Exception e) {
            e.printStackTrace();
            clsConexion.deshacerTransaccion();
        }
        return answerCode;
    }

    public void actualizarResumenDiario(String ruta, String archivo, String ruc, String tipo, String ticket, String fecharecepcion, String usumodificacion, String observacion) {
        try {
            clsConexion.inicarTransaccion();
            String rpta = "";
            System.out.println("ticket" + (ticket));
            rpta = getStatus(ticket).getStatusCode();
            System.out.println("rpta" + rpta);
            if (rpta.equals("0") || rpta.equals("99")) {
                // leer el archivo
                FileOutputStream fos;
                fos = new FileOutputStream(ruta + archivo);
                fos.write(getStatus(ticket).getContent());
                fos.close();
                Utilitario.contexto = "";
                String msjRpta = Utilitario.searchAnswer(ruta, archivo, "cbc:Description");
                Utilitario.contexto = "";
                String codigoRpta = Utilitario.searchAnswer(ruta, archivo, "cbc:ResponseCode");
                msjRpta = msjRpta.replace("'", "");
                String estado = rpta.equals("0") ? "ACE" : "REC";
                if (rpta.equals("99")) {
                    if (Integer.valueOf(codigoRpta.toString()) >= 2000 && Integer.valueOf(codigoRpta.toString()) <= 3999) {
                        estado = "REC";
                    } else {
                        estado = "PEN";
                    }
                }
                actualizarTicket(ruc, tipo, "", null, "", ticket, estado, "", "", "", archivo, "NOW()", usumodificacion, codigoRpta + " " + msjRpta);
            }
            if (rpta.equals("0098")) {
                String estado = "PRO";
                actualizarTicket(ruc, tipo, "", null, "", ticket, estado, "", "", "", "", "", usumodificacion, "");
            }
            clsConexion.finalizarTransaccion();
        } catch (FileNotFoundException ex) {
            clsConexion.deshacerTransaccion();
        } catch (IOException ex) {
            clsConexion.deshacerTransaccion();
            ex.printStackTrace();
        } catch (Exception ex) {
            clsConexion.deshacerTransaccion();
            ex.printStackTrace();
        }
    }

    private String actualizarTicket(String ruc, String tipo, String clasificacion, Integer bloque, String fecha, String ticket, String estado, String rutaxml, String fechaenvio, String idusuario, String rutacdr, String fecharecepcion, String usumodificacion, String observacion) {
        String codigo = "";

        try {
            PreparedStatement st;
            String sql;
            sql = " { call " + nombreEsquema + ".fun_fel_actualizar_ticket ("
                    + "'" + ruc + "',"
                    + "'" + tipo + "',"
                    + "'" + clasificacion + "',"
                    + "" + String.valueOf(fecha.equals("") ? "null" : "'" + fecha + "'") + ","
                    + "" + bloque + ","
                    + "" + String.valueOf(ticket.equals("") ? "null" : "'" + ticket + "'") + ","
                    + "'" + estado + "',"
                    + "" + String.valueOf(rutaxml.equals("") ? "null" : "'" + rutaxml + "'") + ","
                    + "" + String.valueOf(fechaenvio.equals("") ? "null" : fechaenvio.equals("NOW()") ? "now()::timestamp" : "'" + fechaenvio + "'") + ","
                    + "" + String.valueOf(idusuario.equals("") ? "null" : "'" + idusuario + "'") + ","
                    + "" + String.valueOf(rutacdr.equals("") ? "null" : "'" + rutacdr + "'") + ","
                    + "" + String.valueOf(fecharecepcion.equals("") ? "null" : fecharecepcion.equals("NOW()") ? "now()::timestamp" : "'" + fecharecepcion + "'") + ","
                    + "" + String.valueOf(signatureValue == (null) ? "null" : "'" + signatureValue + "'") + ","
                    + "" + String.valueOf(digestValue == (null) ? "null" : "'" + digestValue + "'") + ","
                    // + "'" + usumodificacion + "',"
                    + "" + String.valueOf(idusuario.equals("") ? "null" : "'" + idusuario + "'") + ","
                    + "'" + observacion + "'"
                    + ")}";
            System.out.println(sql);
            call = conPostgres.prepareCall(sql);
            call.execute();
            call.close();

            codigo = "OK";
            System.out.println(codigo);
        } catch (SQLException ex) {
            ex.printStackTrace();
            codigo = String.valueOf(ex.getErrorCode());
        }
        return codigo;
    }

    private String buscarCodigoError(String codigo) {
        String rpta = "";

        try {
            CallableStatement callsec;
            ResultSet rssec;
            String sql;
            sql = " SELECT * FROM " + nombreEsquema + ".fun_fel_consultar_error('" + codigo + "')";
            System.out.println(sql);
            callsec = conPostgres.prepareCall(sql);
            if (callsec.execute()) {
                rssec = callsec.getResultSet();
                if (rssec != null) {
                    while (rssec.next()) {
                        rpta = rssec.getString(1);
                    }
                }
            }
            System.out.println(rpta);
        } catch (SQLException ex) {
            ex.printStackTrace();
            codigo = String.valueOf(ex.getErrorCode());
        }
        return rpta;
    }

    private static String analyseAnswer(byte[] resultado) throws XPathExpressionException {
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
                    rpta = Utilitario.findElement(doc, "cbc:ResponseCode");
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

    private static String obtainValue(File comprobante) {
        signatureValue = null;
        digestValue = null;
        String rpta = "";
        try {
            InputStream comprobantezip = new FileInputStream(comprobante);
            ZipInputStream zipStream = new ZipInputStream(comprobantezip);
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
                }
                bos.close();
                byte[] docBytes = bos.toByteArray();
                String roundTrip = new String(bos.toByteArray(), "UTF8");
                if (!roundTrip.equals("")) {
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    dbf.setNamespaceAware(true);
                    InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
                    Document doc = dbf.newDocumentBuilder().parse(inputStream);
                    signatureValue = Utilitario.findElement(doc, "ds:SignatureValue");
                    digestValue = Utilitario.findElement(doc, "ds:DigestValue");
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
/*
    private static byte[] sendBill(java.lang.String fileName, javax.activation.DataHandler contentFile, java.lang.String partyType) {
        sunat.BillService_Service service = new sunat.BillService_Service();
        sunat.BillService port = service.getBillServicePort();

        BindingProvider bindingProvider = (BindingProvider) port;
        @SuppressWarnings("request-headers")
        List<Handler> handlerChain = new ArrayList<Handler>();
        handlerChain.add(new WSSecurityHeaderSOAPHandler(userSOL, passwordSOL));
        bindingProvider.getBinding().setHandlerChain(handlerChain);

        return port.sendBill(fileName, contentFile, partyType);
    }

    private static String sendSummary(java.lang.String fileName, javax.activation.DataHandler contentFile, java.lang.String partyType) {
        sunat.BillService_Service service = new sunat.BillService_Service();
        sunat.BillService port = service.getBillServicePort();

        BindingProvider bindingProvider = (BindingProvider) port;
        @SuppressWarnings("request-headers")
        List<Handler> handlerChain = new ArrayList<Handler>();
        handlerChain.add(new WSSecurityHeaderSOAPHandler(userSOL, passwordSOL));
        bindingProvider.getBinding().setHandlerChain(handlerChain);

        return port.sendSummary(fileName, contentFile, partyType);
    }

    private static StatusResponse getStatus(java.lang.String ticket) {
        sunat.BillService_Service service = new sunat.BillService_Service();
        sunat.BillService port = service.getBillServicePort();

        BindingProvider bindingProvider = (BindingProvider) port;
        @SuppressWarnings("request-headers")
        List<Handler> handlerChain = new ArrayList<Handler>();
        handlerChain.add(new WSSecurityHeaderSOAPHandler(userSOL, passwordSOL));
        bindingProvider.getBinding().setHandlerChain(handlerChain);

        return port.getStatus(ticket);
    }*/

    private static byte[] sendBill(java.lang.String fileName, javax.activation.DataHandler contentFile, java.lang.String partyType) {
        sunat.BillService_Service service = new sunat.BillService_Service();
        sunat.BillService port = service.getBillServicePort();
        
        
        BindingProvider bindingProvider = (BindingProvider) port;
        @SuppressWarnings("request-headers")
        List<Handler> handlerChain = new ArrayList<Handler>();
        handlerChain.add(new WSSecurityHeaderSOAPHandler(userSOL, passwordSOL));
        bindingProvider.getBinding().setHandlerChain(handlerChain);
        
        return port.sendBill(fileName, contentFile, partyType);
    }

    private static String sendSummary(java.lang.String fileName, javax.activation.DataHandler contentFile, java.lang.String partyType) {
        sunat.BillService_Service service = new sunat.BillService_Service();
        sunat.BillService port = service.getBillServicePort();
        
        
        BindingProvider bindingProvider = (BindingProvider) port;
        @SuppressWarnings("request-headers")
        List<Handler> handlerChain = new ArrayList<Handler>();
        handlerChain.add(new WSSecurityHeaderSOAPHandler(userSOL, passwordSOL));
        bindingProvider.getBinding().setHandlerChain(handlerChain);
        
        return port.sendSummary(fileName, contentFile, partyType);
    }

    private static StatusResponse getStatus(java.lang.String ticket) {
        sunat.BillService_Service service = new sunat.BillService_Service();
        sunat.BillService port = service.getBillServicePort();
        
        
        BindingProvider bindingProvider = (BindingProvider) port;
        @SuppressWarnings("request-headers")
        List<Handler> handlerChain = new ArrayList<Handler>();
        handlerChain.add(new WSSecurityHeaderSOAPHandler(userSOL, passwordSOL));
        bindingProvider.getBinding().setHandlerChain(handlerChain);
        
        return port.getStatus(ticket);
    }
    
    

}

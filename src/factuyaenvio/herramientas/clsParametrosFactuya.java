package factuyaenvio.herramientas;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author GloriaPC
 */
public class clsParametrosFactuya {

    private static String empresa = null;
    private static String ubicacionContext = "";

    // <editor-fold defaultstate="collapsed" desc="Variables context parametros">
    public static String host;
    public static String puertoBD;
    public static String usuarioBD;
    public static String passwordBD;
    public static String nombreBD;

    public static boolean subirServidor;
    public static String ubicacionServidor;

    public static Boolean cargo = false;

    public static String hostSH;
    public static String puertoSH;
    public static String usuarioSH;
    public static String passwordSH;

    public static Boolean tipoEnvio = false;

    public static String numerRUC;
    public static String usuarioSol;
    public static String passwordSol;
    public static String nombreCertificado;
    public static String passwordCertificado;

    public static String ubicacionCertificado;
    public static boolean enviarResumenDiario = false;

    public static boolean enviarAnulados = false;
    public static Boolean enviarCorreo = false;
    public static String usuarioCorreo;
    public static String passwordCorreo;
    public static String ubicacionFormatos;

    public static String ubicacionImagenes;

    public static String hostCorreoPrincipal;
    public static String hostCorreo;
    public static String puertoCorreo;
    public static boolean reenviarCPEAcpetados;

    public static String ubicacionPrincipal;
    public static String ubicacionSunatEnvio;
    public static String ubicacionSunatRespuesta;
    public static String ubicacionSunatTemporal;

    public static String nombreEsquema = "ventas";
    public static String nombreTablaComprobantes = "tblcomprobantesventascab_cvc";
    public static String nombreEsquemaCPE = "ventas";
    public static String campoEmpresa = "cvc.emp_id=emp.emp_id";
    public static String campoMoneda = "cvc.mon_codigo";
    public static String campoFechaemision = "cvc_fechaemision";
    public static String campoTipoDocumento = "cvt_codigo";

    public static String campoEmpresaId = "emp_id";
    public static String campoEmpresaRuc = "emp_ruc";
    public static String campoEmpresaRazonSocial = "emp_razonsocial";

    public static String campoEntidadTipo = "dxt_codigo";

    public static Integer enviarSUNAT;

    public static String menu = "0";
    public static String detracionCuenta = "";

    public static Boolean subirServidorExterno = false;
    public static String ubicacionServidorExterno = "";
    public static String hostSHExterno = "";
    public static String puertoSHExterno = "";
    public static String usuarioSHExterno = "";
    public static String passwordSHExterno = "";
   public static String usuarioCorreoRemitente="";

    
// </editor-fold>
    public clsParametrosFactuya(String _empresa, String context) {
        this.empresa = _empresa;
        this.ubicacionContext = context;
    }

    public void cargarParametros() {
        empresa = empresa + "_";
        try {

            NodeList nodeListConceptos = cargarDocumento("/Context/Parameter");

            // Avanzamos por la lista para presentar los conceptos
            for (int temp = 0; temp < nodeListConceptos.getLength(); temp++) {

                // Obtenemos un nodo
                Node nodoConcepto = nodeListConceptos.item(temp);

                // Verificamos que el nodo sea un elemento, para prevenir errores
                if (nodoConcepto.getNodeType() == Node.ELEMENT_NODE) {

                    // Convertimos de Node a elemento
                    Element elementoConcepto = (Element) nodoConcepto;
                    //  System.out.println("" + elementoConcepto.getAttribute("name") + "  :  " + elementoConcepto.getAttribute("value"));

                    if (elementoConcepto.getAttribute("name").equals(empresa + "subirServidor")) {
                        subirServidor = Boolean.valueOf(elementoConcepto.getAttribute("value"));
                    }
                    if (elementoConcepto.getAttribute("name").equals(empresa + "ubicacionServidor")) {
                        ubicacionServidor = elementoConcepto.getAttribute("value");
                    }
                    if (elementoConcepto.getAttribute("name").equals(empresa + "cargo")) {
                        cargo = Boolean.valueOf(elementoConcepto.getAttribute("value"));
                    }

                    if (elementoConcepto.getAttribute("name").equals(empresa + "hostSH")) {
                        hostSH = elementoConcepto.getAttribute("value");
                    }
                    if (elementoConcepto.getAttribute("name").equals(empresa + "puertoSH")) {
                        puertoSH = elementoConcepto.getAttribute("value");
                    }
                    if (elementoConcepto.getAttribute("name").equals(empresa + "usuarioSH")) {
                        usuarioSH = elementoConcepto.getAttribute("value");
                    }
                    if (elementoConcepto.getAttribute("name").equals(empresa + "passwordSH")) {
                        passwordSH = elementoConcepto.getAttribute("value");
                    }
                    if (elementoConcepto.getAttribute("name").equals(empresa + "tipoEnvio")) {
                        tipoEnvio = Boolean.valueOf(elementoConcepto.getAttribute("value"));
                    }

                    if (elementoConcepto.getAttribute("name").equals(empresa + "numerRUC")) {
                        numerRUC = elementoConcepto.getAttribute("value");
                    }
                    if (elementoConcepto.getAttribute("name").equals(empresa + "usuarioSol")) {
                        usuarioSol = elementoConcepto.getAttribute("value");
                    }
                    if (elementoConcepto.getAttribute("name").equals(empresa + "passwordSol")) {
                        passwordSol = elementoConcepto.getAttribute("value");
                    }
                    if (elementoConcepto.getAttribute("name").equals(empresa + "nombreCertificado")) {
                        nombreCertificado = elementoConcepto.getAttribute("value");
                    }
                    if (elementoConcepto.getAttribute("name").equals(empresa + "passwordCertificado")) {
                        passwordCertificado = elementoConcepto.getAttribute("value");
                    }
                    if (elementoConcepto.getAttribute("name").equals(empresa + "ubicacionCertificado")) {
                        ubicacionCertificado = elementoConcepto.getAttribute("value");
                    }

                    if (elementoConcepto.getAttribute("name").equals(empresa + "enviarResumenDiario")) {
                        enviarResumenDiario = Boolean.valueOf(elementoConcepto.getAttribute("value"));
                    }

                    if (elementoConcepto.getAttribute("name").equals(empresa + "enviarAnulados")) {
                        enviarAnulados = Boolean.valueOf(elementoConcepto.getAttribute("value"));
                    }

                    if (elementoConcepto.getAttribute("name").equals(empresa + "enviarCorreo")) {
                        enviarCorreo = Boolean.valueOf(elementoConcepto.getAttribute("value"));
                    }

                    if (elementoConcepto.getAttribute("name").equals(empresa + "usuarioCorreo")) {
                        usuarioCorreo = elementoConcepto.getAttribute("value");
                    }
                    if (elementoConcepto.getAttribute("name").equals(empresa + "passwordCorreo")) {
                        passwordCorreo = elementoConcepto.getAttribute("value");
                    }
                    if (elementoConcepto.getAttribute("name").equals(empresa + "ubicacionFormatos")) {
                        ubicacionFormatos = elementoConcepto.getAttribute("value");
                    }
                    if (elementoConcepto.getAttribute("name").equals(empresa + "ubicacionImagenes")) {
                        ubicacionImagenes = elementoConcepto.getAttribute("value");
                    }

                    if (elementoConcepto.getAttribute("name").equals(empresa + "hostCorreoPrincipal")) {
                        hostCorreoPrincipal = elementoConcepto.getAttribute("value");
                    }
                    if (elementoConcepto.getAttribute("name").equals(empresa + "hostCorreo")) {
                        hostCorreo = elementoConcepto.getAttribute("value");
                    }
                    if (elementoConcepto.getAttribute("name").equals(empresa + "puertoCorreo")) {
                        puertoCorreo = elementoConcepto.getAttribute("value");
                    }
                    if (elementoConcepto.getAttribute("name").equals(empresa + "reenviarCPEAcpetados")) {
                        reenviarCPEAcpetados = Boolean.valueOf(elementoConcepto.getAttribute("value"));
                    }
                    if (elementoConcepto.getAttribute("name").equals(empresa + "ubicacionPrincipal")) {
                        ubicacionPrincipal = elementoConcepto.getAttribute("value");
                    }

                    if (elementoConcepto.getAttribute("name").equals(empresa + "ubicacionSunatEnvio")) {
                        ubicacionSunatEnvio = elementoConcepto.getAttribute("value");
                    }

                    if (elementoConcepto.getAttribute("name").equals(empresa + "ubicacionSunatRespuesta")) {
                        ubicacionSunatRespuesta = elementoConcepto.getAttribute("value");
                    }

                    if (elementoConcepto.getAttribute("name").equals(empresa + "ubicacionSunatTemporal")) {
                        ubicacionSunatTemporal = elementoConcepto.getAttribute("value");
                    }

                    if (elementoConcepto.getAttribute("name").equals(empresa + "enviarSUNAT")) {
                        enviarSUNAT = Integer.valueOf(elementoConcepto.getAttribute("value"));
                    }

                    if (elementoConcepto.getAttribute("name").equals(empresa + "detraccioncuenta")) {
                        detracionCuenta = elementoConcepto.getAttribute("value");
                    }
                    if (elementoConcepto.getAttribute("name").equals(empresa + "menu")) {
                        menu = elementoConcepto.getAttribute("value");
                    }

                    if (elementoConcepto.getAttribute("name").equals(empresa + "subirServidorExterno")) {
                        subirServidorExterno = Boolean.valueOf(elementoConcepto.getAttribute("value"));
                    }

                    if (elementoConcepto.getAttribute("name").equals(empresa + "ubicacionServidorExterno")) {
                        ubicacionServidorExterno = elementoConcepto.getAttribute("value");
                    }

                    if (elementoConcepto.getAttribute("name").equals(empresa + "hostSHExterno")) {
                        hostSHExterno = elementoConcepto.getAttribute("value");
                    }
                    if (elementoConcepto.getAttribute("name").equals(empresa + "puertoSHExterno")) {
                        puertoSHExterno = elementoConcepto.getAttribute("value");
                    }
                    if (elementoConcepto.getAttribute("name").equals(empresa + "usuarioSHExterno")) {
                        usuarioSHExterno = elementoConcepto.getAttribute("value");
                    }
                    if (elementoConcepto.getAttribute("name").equals(empresa + "passwordSHExterno")) {
                        passwordSHExterno = elementoConcepto.getAttribute("value");
                    }
                    if (elementoConcepto.getAttribute("name").equals(empresa + "usuarioCorreoRemitente")) {
                        usuarioCorreoRemitente = elementoConcepto.getAttribute("value");
                    }
                    

                    if (cargo) {
                        nombreEsquema = "cargo";
                        nombreTablaComprobantes = "tblcomprobantesventacabecera_cvc";
                        nombreEsquemaCPE = "public";
                        campoEmpresa = "cvc.emp_codigo=emp.emp_codigo";;
                        campoMoneda = "'SOL'";
                        campoFechaemision = "cvc_fecemision";
                        campoTipoDocumento = "dti_codigo";
                        campoEmpresaId = "emp_codigo";
                        campoEmpresaRuc = "emp_codigo";
                        campoEmpresaRazonSocial = "emp_nombre";
                        campoEntidadTipo = "ent_tipodocumento";
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    public void cargarConexion(String nombreConexion) {

        // Generador de constructor de objetos XML
        NodeList nodeListConceptos = cargarDocumento("/Context/Resource");

        // Avanzamos por la lista para presentar los conceptos
        for (int temp = 0; temp < nodeListConceptos.getLength(); temp++) {

            // Obtenemos un nodo
            Node nodoConcepto = nodeListConceptos.item(temp);

            // Verificamos que el nodo sea un elemento, para prevenir errores
            if (nodoConcepto.getNodeType() == Node.ELEMENT_NODE) {

                // Convertimos de Node a elemento
                Element elementoConcepto = (Element) nodoConcepto;
                /*   System.out.println("" + elementoConcepto.getAttribute("name") + "  :  " + elementoConcepto.getAttribute("url"));

                 System.out.println("" + elementoConcepto.getAttribute("name") + "  :  " + elementoConcepto.getAttribute("username"));
                 System.out.println("" + elementoConcepto.getAttribute("name") + "  :  " + elementoConcepto.getAttribute("password"));
                 */
                if (elementoConcepto.getAttribute("name").equals(nombreConexion)) {
                    host = elementoConcepto.getAttribute("url");
                    usuarioBD = elementoConcepto.getAttribute("username");
                    passwordBD = elementoConcepto.getAttribute("password");
                }

            }
        }

    }

    private NodeList cargarDocumento(String expresionConcepto) {
        NodeList nodeListConceptos = null;
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

            // Esto es para agilizar la lectura de archivos grandes
            documentBuilderFactory.setNamespaceAware(false);
            documentBuilderFactory.setValidating(false);
            documentBuilderFactory.setFeature("http://xml.org/sax/features/namespaces", false);
            documentBuilderFactory.setFeature("http://xml.org/sax/features/validation", false);
            documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            // constructor de objetos XML
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            // Ruta del archivo XML
            String nombreArchivo = ubicacionContext;
            File archivo = new File(nombreArchivo);

            // Objeto Documento XML
            Document documento = documentBuilder.parse(archivo);

            // Esto ayuda al procesamiento
            documento.getDocumentElement().normalize();

            // XPath nos permite seleccionar objetos via su ubicacion en la estructura del XML
            XPath xPath = XPathFactory.newInstance().newXPath();

            // Ruta de los conceptos d ela factura
            String expresionConceptos = expresionConcepto;//"/Context/Resource";

            // Lista de nodos de conceptos
            nodeListConceptos = (NodeList) xPath.compile(expresionConceptos).evaluate(documento, XPathConstants.NODESET);

        } catch (SAXException ex) {
            Logger.getLogger(clsParametrosFactuya.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(clsParametrosFactuya.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(clsParametrosFactuya.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(clsParametrosFactuya.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return nodeListConceptos;
        }
    }

}

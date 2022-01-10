/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guiaremision.ubl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Glory
 * @email gloria.pv@reyna.com.pe
 * @date 2016-08-18
 *
 */
public class clsFirmarUBL {

    private static Document doc;

    public void uploadXML(File entradaXML) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);

            DocumentBuilder dBuilder = dbf.newDocumentBuilder();
            InputStream inputStream = new FileInputStream(entradaXML);
            doc = dBuilder.parse(new InputSource(new InputStreamReader(inputStream, "ISO-8859-1")));

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(clsFirmarUBL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(clsFirmarUBL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(clsFirmarUBL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(clsFirmarUBL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void uploadXMLByte(byte[] entradaXML) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            doc = dbf.newDocumentBuilder().parse(new ByteArrayInputStream(entradaXML));
        } catch (SAXException ex) {
            Logger.getLogger(clsFirmarUBL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(clsFirmarUBL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(clsFirmarUBL.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public byte[] outputXMLByte() {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            trans.transform(new DOMSource(doc), new StreamResult(baos));

            //  org.apache.xml.security.utils.XMLUtils.outputDOM(doc, baos, true);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(clsFirmarUBL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(clsFirmarUBL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return baos.toByteArray();

    }

    public void outputXML(File salidaXML) {
        OutputStream os = null;
        try {

            os = new FileOutputStream(salidaXML);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            // trans.setOutputProperty(OutputKeys.INDENT, "yes");
            trans.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            trans.transform(new DOMSource(doc), new StreamResult(os));

        } catch (FileNotFoundException ex) {
            Logger.getLogger(clsFirmarUBL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(clsFirmarUBL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(clsFirmarUBL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(clsFirmarUBL.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void singUBL(String tipoAlmacen, File rutaAlmacen, String clavePrivada) {
        try {
            Integer nodoId = 0, nodoFirma = 0;
            String nodoPrincipal = doc.getDocumentElement().getNodeName();
            if (nodoPrincipal.equals("Invoice")) {
                nodoFirma = 1;
                nodoId = 3;
            } else if (nodoPrincipal.equals("SummaryDocuments") || nodoPrincipal.equals("VoidedDocuments")) {
                nodoFirma = 0;
                nodoId = 0;
            } else if (nodoPrincipal.equals("CreditNote") || nodoPrincipal.equals("DebitNote")) {
                nodoFirma = 1;
                nodoId = 4;
            }

            XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
            Reference ref = fac.newReference("", fac.newDigestMethod(DigestMethod.SHA1, null),
                    Collections.singletonList(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)),
                    null, null);
            SignedInfo si = fac.newSignedInfo(fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE,
                    (C14NMethodParameterSpec) null),
                    fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
                    Collections.singletonList(ref));

            // Cargamos el almacen de claves
            // KeyStore ks = KeyStore.getInstance(tipoAlmacen);//"JKS"
            //  ks.load(new FileInputStream(rutaAlmacen), clavePrivada.toCharArray());//CPE\\Factura\\treyna.keystore,queen@yabe
            // Obtenemos la clave privada, pues la necesitaremos para encriptar.
            //   KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias, new KeyStore.PasswordProtection(claveAlmacen.toCharArray()));//treyna,queen@yabe
            // Añadimos el KeyInfo del certificado cuya clave privada usamos
            //   X509Certificate cert = (X509Certificate) keyEntry.getCertificate();
            KeyStore ks = KeyStore.getInstance(tipoAlmacen);
            ks.load(new FileInputStream(rutaAlmacen), clavePrivada.toCharArray());

            String alias = ks.aliases().nextElement();
            KeyStore.PrivateKeyEntry pKey = (KeyStore.PrivateKeyEntry) ks.getEntry(alias, new KeyStore.PasswordProtection(clavePrivada.toCharArray()));
            X509Certificate cert = (X509Certificate) pKey.getCertificate();

            // X509Certificate cert = (X509Certificate) keyEntry.getCertificate();
            KeyInfoFactory kif = fac.getKeyInfoFactory();

            List x509Content = new ArrayList();
            x509Content.add(cert);//añadir la data del certificado
            x509Content.add(cert.getSubjectX500Principal().getName());//añadir cabecera del certificado
            X509Data xd = kif.newX509Data(x509Content);
            KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xd));

            // Obtenemos el nodo para agregar la firma.
            String expression = "//*[name() ='ext:ExtensionContent']";
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
            Node signatureInfoNode = (Node) nodeList.item(nodoFirma);

            //   DOMSignContext dsc = new DOMSignContext(keyEntry.getPrivateKey(), signatureInfoNode);
            DOMSignContext dsc = new DOMSignContext(pKey.getPrivateKey(), signatureInfoNode);

            //Agregarmos prefijo a la firmar
            dsc.setDefaultNamespacePrefix("ds");
            // Obtenemos el valor del nodo ID
            expression = "//*[name() = 'cbc:ID']";
            NodeList nodeId = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
            String id = nodeId.item(nodoId).getTextContent();// item que se desea
            // id = "TTRF010-00104085";
            // Realizamos la firma
            XMLSignature signature = fac.newXMLSignature(si, ki, null, id, null);
            signature.sign(dsc);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            /*  if (Profile.getInstance().getSendType() == false) {
             Main.invoice.dP.p.getTxtarea().append("Error  " + ex.getMessage() + "\n");
             Main.invoice.dP.p.getTxtarea().setCaretPosition(Main.invoice.dP.p.getTxtarea().getDocument().getLength());
             Logger.getLogger(SignUBL.class.getName()).log(Level.SEVERE, null, ex);
             }*/
        } catch (InvalidAlgorithmParameterException ex) {
            ex.printStackTrace();
            /*  if (Profile.getInstance().getSendType() == false) {
             Main.invoice.dP.p.getTxtarea().append("Error  " + ex.getMessage() + "\n");
             Main.invoice.dP.p.getTxtarea().setCaretPosition(Main.invoice.dP.p.getTxtarea().getDocument().getLength());
             Logger.getLogger(SignUBL.class.getName()).log(Level.SEVERE, null, ex);
             }*/
        } catch (KeyStoreException ex) {
            ex.printStackTrace();
            /*if (Profile.getInstance().getSendType() == false) {
             Main.invoice.dP.p.getTxtarea().append("Error  " + ex.getMessage() + "\n");
             Main.invoice.dP.p.getTxtarea().setCaretPosition(Main.invoice.dP.p.getTxtarea().getDocument().getLength());
             Logger.getLogger(SignUBL.class.getName()).log(Level.SEVERE, null, ex);
             }*/
        } catch (IOException ex) {
            ex.printStackTrace();
            /*  if (Profile.getInstance().getSendType() == false) {
             Main.invoice.dP.p.getTxtarea().append("Error  " + ex.getMessage() + "\n");
             Main.invoice.dP.p.getTxtarea().setCaretPosition(Main.invoice.dP.p.getTxtarea().getDocument().getLength());
             Logger.getLogger(SignUBL.class.getName()).log(Level.SEVERE, null, ex);
             }*/
        } catch (CertificateException ex) {
            ex.printStackTrace();
            /*if (Profile.getInstance().getSendType() == false) {
             Main.invoice.dP.p.getTxtarea().append("Error  " + ex.getMessage() + "\n");
             Main.invoice.dP.p.getTxtarea().setCaretPosition(Main.invoice.dP.p.getTxtarea().getDocument().getLength());
             Logger.getLogger(SignUBL.class.getName()).log(Level.SEVERE, null, ex);
             }*/
        } catch (UnrecoverableEntryException ex) {
            ex.printStackTrace();
            /*  if (Profile.getInstance().getSendType() == false) {
             Main.invoice.dP.p.getTxtarea().append("Error  " + ex.getMessage() + "\n");
             Main.invoice.dP.p.getTxtarea().setCaretPosition(Main.invoice.dP.p.getTxtarea().getDocument().getLength());
             Logger.getLogger(SignUBL.class.getName()).log(Level.SEVERE, null, ex);
             }*/
        } catch (XPathExpressionException ex) {
            ex.printStackTrace();
            /*  if (Profile.getInstance().getSendType() == false) {
             Main.invoice.dP.p.getTxtarea().append("Error  " + ex.getMessage() + "\n");
             Main.invoice.dP.p.getTxtarea().setCaretPosition(Main.invoice.dP.p.getTxtarea().getDocument().getLength());
             Logger.getLogger(SignUBL.class.getName()).log(Level.SEVERE, null, ex);
             }*/
        } catch (MarshalException ex) {
            ex.printStackTrace();
            /* if (Profile.getInstance().getSendType() == false) {
             Main.invoice.dP.p.getTxtarea().append("Error  " + ex.getMessage() + "\n");
             Main.invoice.dP.p.getTxtarea().setCaretPosition(Main.invoice.dP.p.getTxtarea().getDocument().getLength());
             Logger.getLogger(SignUBL.class.getName()).log(Level.SEVERE, null, ex);
             }*/
        } catch (XMLSignatureException ex) {
            ex.printStackTrace();
            /*  if (Profile.getInstance().getSendType() == false) {
             Main.invoice.dP.p.getTxtarea().append("Error  " + ex.getMessage() + "\n");
             Main.invoice.dP.p.getTxtarea().setCaretPosition(Main.invoice.dP.p.getTxtarea().getDocument().getLength());
             Logger.getLogger(SignUBL.class.getName()).log(Level.SEVERE, null, ex);
             }
             ex.printStackTrace();*/
        }

    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guiaremision.ubl;

import guiaremision.guiaremision.clsComunicacionBaja;
import guiaremision.guiaremision.clsComunicacionBajaItem;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 *
 * @author Jose Ayala
 */
public class clsGenerarUBLComunicacionBaja {

    private clsComunicacionBaja baja;

    public clsGenerarUBLComunicacionBaja(clsComunicacionBaja baja) {

        this.baja = baja;

    }

    public void writeXML(String NombreArchivo) {
        String linea = "";
        FileWriter fw = null;
        try {
            fw = new FileWriter(NombreArchivo);
            OutputStream outputStream = new FileOutputStream(NombreArchivo);
            Writer archiXML = new OutputStreamWriter(outputStream, "ISO-8859-1");
            //  BufferedWriter bw = new BufferedWriter(fw);
            // PrintWriter archiXML = new PrintWriter(bw);

            /*   CABECERA UBL  */
            linea = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>"
                    + "<VoidedDocuments xmlns=\"urn:sunat:names:specification:ubl:peru:schema:xsd:VoidedDocuments-1\" xmlns:cac=\"urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2\" xmlns:cbc=\"urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ext=\"urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2\" xmlns:sac=\"urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";
            archiXML.write(linea + "\n");

            /* PARA FIRMA */
            linea = "        <ext:UBLExtensions>\n"
                    + "	        <ext:UBLExtension>\n"
                    + "			<ext:ExtensionContent>";
            archiXML.write(linea + "\n");

            linea = "			</ext:ExtensionContent>\n"
                    + "		</ext:UBLExtension>\n"
                    + "	</ext:UBLExtensions>";
            archiXML.write(linea + "\n");

            linea = "	<cbc:UBLVersionID>2.0</cbc:UBLVersionID>\n"
                    + "	<cbc:CustomizationID>1.0</cbc:CustomizationID>\n"
                    + "	<cbc:ID>" + baja.getCodigo() + "</cbc:ID>\n"
                    + "	<cbc:ReferenceDate>" + baja.getFechaGeneracionDocumento() + "</cbc:ReferenceDate>\n"
                    + "	<cbc:IssueDate>" + baja.getFechaGeneracionComunicado() + "</cbc:IssueDate>";
            archiXML.write(linea + "\n");

            linea = "	<cac:Signature>\n"
                    + "		<cbc:ID>IDSignKG</cbc:ID>\n"
                    + "		<cac:SignatoryParty>\n"
                    + "			<cac:PartyIdentification>\n"
                    + "				<cbc:ID>" + baja.getEmisorNroDocumento() + "</cbc:ID>\n"
                    + "			</cac:PartyIdentification>\n"
                    + "			<cac:PartyName>\n"
                    + "				<cbc:Name><![CDATA[" + baja.getEmisorRazonSocial() + "]]></cbc:Name>\n"
                    + "			</cac:PartyName>\n"
                    + "		</cac:SignatoryParty>\n"
                    + "		<cac:DigitalSignatureAttachment>\n"
                    + "			<cac:ExternalReference>\n"
                    + "				<cbc:URI>#signatureKG</cbc:URI>\n"
                    + "			</cac:ExternalReference>\n"
                    + "		</cac:DigitalSignatureAttachment>\n"
                    + "	</cac:Signature>";
            archiXML.write(linea + "\n");

            linea = "	<cac:AccountingSupplierParty>\n"
                    + "		<cbc:CustomerAssignedAccountID>" + baja.getEmisorNroDocumento() + "</cbc:CustomerAssignedAccountID>\n"
                    + "		<cbc:AdditionalAccountID>6</cbc:AdditionalAccountID>\n"
                    + "		<cac:Party>\n"
                    + "			<cac:PartyLegalEntity>\n"
                    + "				<cbc:RegistrationName><![CDATA[" + baja.getEmisorRazonSocial() + "]]></cbc:RegistrationName>\n"
                    + "			</cac:PartyLegalEntity>\n"
                    + "		</cac:Party>\n"
                    + "	</cac:AccountingSupplierParty>";
            archiXML.write(linea + "\n");

            for (int i = 0; i < baja.getItems().size(); i++) {
                //((VoidedDocumentItem)baja.getItems().elementAt(i))
                linea = "	<sac:VoidedDocumentsLine>\n"
                        + "		<cbc:LineID>" + String.valueOf(i + 1) + "</cbc:LineID>\n"
                        + "		<cbc:DocumentTypeCode>" + ((clsComunicacionBajaItem) baja.getItems().elementAt(i)).getTipoDoc() + "</cbc:DocumentTypeCode>\n"
                        + "		<sac:DocumentSerialID>" + ((clsComunicacionBajaItem) baja.getItems().elementAt(i)).getSerie() + "</sac:DocumentSerialID>\n"
                        + "		<sac:DocumentNumberID>" + ((clsComunicacionBajaItem) baja.getItems().elementAt(i)).getNumero() + "</sac:DocumentNumberID>\n"
                        + "		<sac:VoidReasonDescription><![CDATA[" + ((clsComunicacionBajaItem) baja.getItems().elementAt(i)).getMotivoBaja() + "]]></sac:VoidReasonDescription>\n"
                        + "	</sac:VoidedDocumentsLine>";
                archiXML.write(linea + "\n");

            }

            linea = "</VoidedDocuments>";
            archiXML.write(linea + "\n");

            archiXML.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public byte[] writeXMLByte() {
        String linea = "";
        String temp = "";
        try {
            /*   CABECERA UBL  */
            linea = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>"
                    + "<VoidedDocuments xmlns=\"urn:sunat:names:specification:ubl:peru:schema:xsd:VoidedDocuments-1\" xmlns:cac=\"urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2\" xmlns:cbc=\"urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ext=\"urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2\" xmlns:sac=\"urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";
            temp = temp + (linea + "\n");

            /* PARA FIRMA */
            linea = "        <ext:UBLExtensions>\n"
                    + "	        <ext:UBLExtension>\n"
                    + "			<ext:ExtensionContent>";
            temp = temp + (linea + "\n");

            linea = "			</ext:ExtensionContent>\n"
                    + "		</ext:UBLExtension>\n"
                    + "	</ext:UBLExtensions>";
            temp = temp + (linea + "\n");

            linea = "	<cbc:UBLVersionID>2.0</cbc:UBLVersionID>\n"
                    + "	<cbc:CustomizationID>1.0</cbc:CustomizationID>\n"
                    + "	<cbc:ID>" + baja.getCodigo() + "</cbc:ID>\n"
                    + "	<cbc:ReferenceDate>" + baja.getFechaGeneracionDocumento() + "</cbc:ReferenceDate>\n"
                    + "	<cbc:IssueDate>" + baja.getFechaGeneracionComunicado() + "</cbc:IssueDate>";
            temp = temp + (linea + "\n");

            linea = "	<cac:Signature>\n"
                    + "		<cbc:ID>IDSignKG</cbc:ID>\n"
                    + "		<cac:SignatoryParty>\n"
                    + "			<cac:PartyIdentification>\n"
                    + "				<cbc:ID>" + baja.getEmisorNroDocumento() + "</cbc:ID>\n"
                    + "			</cac:PartyIdentification>\n"
                    + "			<cac:PartyName>\n"
                    + "				<cbc:Name><![CDATA[" + baja.getEmisorRazonSocial() + "]]></cbc:Name>\n"
                    + "			</cac:PartyName>\n"
                    + "		</cac:SignatoryParty>\n"
                    + "		<cac:DigitalSignatureAttachment>\n"
                    + "			<cac:ExternalReference>\n"
                    + "				<cbc:URI>#signatureKG</cbc:URI>\n"
                    + "			</cac:ExternalReference>\n"
                    + "		</cac:DigitalSignatureAttachment>\n"
                    + "	</cac:Signature>";
            temp = temp + (linea + "\n");

            linea = "	<cac:AccountingSupplierParty>\n"
                    + "		<cbc:CustomerAssignedAccountID>" + baja.getEmisorNroDocumento() + "</cbc:CustomerAssignedAccountID>\n"
                    + "		<cbc:AdditionalAccountID>6</cbc:AdditionalAccountID>\n"
                    + "		<cac:Party>\n"
                    + "			<cac:PartyLegalEntity>\n"
                    + "				<cbc:RegistrationName><![CDATA[" + baja.getEmisorRazonSocial() + "]]></cbc:RegistrationName>\n"
                    + "			</cac:PartyLegalEntity>\n"
                    + "		</cac:Party>\n"
                    + "	</cac:AccountingSupplierParty>";
            temp = temp + (linea + "\n");

            for (int i = 0; i < baja.getItems().size(); i++) {
                //((VoidedDocumentItem)baja.getItems().elementAt(i))
                linea = "	<sac:VoidedDocumentsLine>\n"
                        + "		<cbc:LineID>" + String.valueOf(i + 1) + "</cbc:LineID>\n"
                        + "		<cbc:DocumentTypeCode>" + ((clsComunicacionBajaItem) baja.getItems().elementAt(i)).getTipoDoc() + "</cbc:DocumentTypeCode>\n"
                        + "		<sac:DocumentSerialID>" + ((clsComunicacionBajaItem) baja.getItems().elementAt(i)).getSerie() + "</sac:DocumentSerialID>\n"
                        + "		<sac:DocumentNumberID>" + ((clsComunicacionBajaItem) baja.getItems().elementAt(i)).getNumero() + "</sac:DocumentNumberID>\n"
                        + "		<sac:VoidReasonDescription><![CDATA[" + ((clsComunicacionBajaItem) baja.getItems().elementAt(i)).getMotivoBaja().trim() + "]]></sac:VoidReasonDescription>\n"
                        + "	</sac:VoidedDocumentsLine>";
                temp = temp + (linea + "\n");

            }
            linea = "</VoidedDocuments>";
            temp = temp + (linea + "\n");
        } catch (Exception e) {
            /*  if (Profile.getInstance().getSendType() == false) {

             Main.invoice.dP.p.getTxtarea().append("Error  " + e.getMessage() + "\n");
             Main.invoice.dP.p.getTxtarea().setCaretPosition(Main.invoice.dP.p.getTxtarea().getDocument().getLength());

             }*/
            e.printStackTrace();
        }
        return temp.getBytes();
    }
}

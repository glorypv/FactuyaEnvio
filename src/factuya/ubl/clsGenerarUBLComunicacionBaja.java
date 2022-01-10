/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package factuya.ubl;

import factuya.factura.clsComunicacionBaja;
import factuya.factura.clsComunicacionBajaItem;

/**
 *
 * @author Jose Ayala
 */
public class clsGenerarUBLComunicacionBaja {

    private clsComunicacionBaja baja;

    public clsGenerarUBLComunicacionBaja(clsComunicacionBaja baja) {
        this.baja = baja;
    }

    public byte[] escribirXMLByte() {
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

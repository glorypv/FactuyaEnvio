/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package factuya.ubl;

import factuya.factura.clsResumenDiario;
import factuya.factura.clsResumenDiarioItem;

/**
 *
 * @author Jose Ayala
 */
public class clsGenerarUBLResumenDiario {

    private clsResumenDiario resumenBoletas;

    public clsGenerarUBLResumenDiario(clsResumenDiario resumenboletas) {
        resumenBoletas = resumenboletas;
    }

    public byte[] escribirXMLByte() {
        String linea = "";
        String temp = "";
        try {
            /*   CABECERA UBL  */
            linea = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";
            temp = temp + (linea + "\n");

            linea = "<SummaryDocuments xmlns=\"urn:sunat:names:specification:ubl:peru:schema:xsd:SummaryDocuments-1\" xmlns:cac=\"urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2\" xmlns:cbc=\"urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ext=\"urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2\" xmlns:sac=\"urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:sunat:names:specification:ubl:peru:schema:xsd:InvoiceSummary-1 D:\\UBL_SUNAT\\SUNAT_xml_20110112\\20110112\\xsd\\maindoc\\UBLPE-InvoiceSummary-1.0.xsd\">";
            temp = temp + (linea + "\n");


            /* PARA FIRMA */
            linea = "<ext:UBLExtensions>\n"
                    + "    <ext:UBLExtension>\n"
                    + "        <ext:ExtensionContent>";
            temp = temp + (linea + "\n");

            linea = "        </ext:ExtensionContent>\n"
                    + "    </ext:UBLExtension>\n"
                    + "</ext:UBLExtensions>";
            temp = temp + (linea + "\n");

            linea = "<cbc:UBLVersionID>2.0</cbc:UBLVersionID>\n"
                    + "<cbc:CustomizationID>1.1</cbc:CustomizationID>\n"
                    + "<cbc:ID>" + resumenBoletas.getCodigo() + "</cbc:ID>\n"
                    + "<cbc:ReferenceDate>" + resumenBoletas.getFechaEmision() + "</cbc:ReferenceDate>\n"
                    + "<cbc:IssueDate>" + resumenBoletas.getFechaGeneracion() + "</cbc:IssueDate>";
            temp = temp + (linea + "\n");

            linea = "<cac:Signature>\n"
                    + "    <cbc:ID>IDSignCA</cbc:ID>\n"
                    + "    <cac:SignatoryParty>\n"
                    + "        <cac:PartyIdentification>\n"
                    + "            <cbc:ID>" + resumenBoletas.getEmisor_RUC() + "</cbc:ID>\n"
                    + "        </cac:PartyIdentification>\n"
                    + "        <cac:PartyName>\n"
                    + "            <cbc:Name>" + resumenBoletas.getEmisor_RazonSocial() + "</cbc:Name>\n"
                    + "        </cac:PartyName>\n"
                    + "    </cac:SignatoryParty>\n"
                    + "    <cac:DigitalSignatureAttachment>\n"
                    + "        <cac:ExternalReference>\n"
                    + "            <cbc:URI>#SignatureCA</cbc:URI>\n"
                    + "        </cac:ExternalReference>\n"
                    + "    </cac:DigitalSignatureAttachment>\n"
                    + "</cac:Signature>";
            temp = temp + (linea + "\n");

            linea = "<cac:AccountingSupplierParty>\n"
                    + "    <cbc:CustomerAssignedAccountID>" + resumenBoletas.getEmisor_RUC() + "</cbc:CustomerAssignedAccountID>\n"
                    + "    <cbc:AdditionalAccountID>6</cbc:AdditionalAccountID>\n"
                    + "    <cac:Party>\n"
                    + "        <cac:PartyLegalEntity>\n"
                    + "            <cbc:RegistrationName>" + resumenBoletas.getEmisor_RazonSocial() + "</cbc:RegistrationName>\n"
                    + "        </cac:PartyLegalEntity>\n"
                    + "    </cac:Party>\n"
                    + "</cac:AccountingSupplierParty>";
            temp = temp + (linea + "\n");


            /* linea = "";
             archiXML.write(linea+"\n");*/
            for (int i = 0; i < resumenBoletas.getItems().size(); i++) {
                //((SummaryDocumentItem)resumenBoletas.getItems().elementAt(i))
                linea = "<sac:SummaryDocumentsLine>\n"
                        + "    <cbc:LineID>" + String.valueOf(i + 1) + "</cbc:LineID>\n"
                        + "    <cbc:DocumentTypeCode>" + ((clsResumenDiarioItem) resumenBoletas.getItems().elementAt(i)).getTipoDoc() + "</cbc:DocumentTypeCode>\n"
                        //+ "    <sac:DocumentSerialID>" + ((SummaryDocumentItem) resumenBoletas.getItems().elementAt(i)).getSerie() + "</sac:DocumentSerialID>\n"
                        //+ "    <sac:StartDocumentNumberID>" + ((SummaryDocumentItem) resumenBoletas.getItems().elementAt(i)).getIniRango() + "</sac:StartDocumentNumberID>\n"
                        //+ "    <sac:EndDocumentNumberID>" + ((SummaryDocumentItem) resumenBoletas.getItems().elementAt(i)).getFinRango() + "</sac:EndDocumentNumberID>\n"
                        + "    <cbc:ID>" + ((clsResumenDiarioItem) resumenBoletas.getItems().elementAt(i)).getSerieNumero() + "</cbc:ID>\n"
                        + "    <cac:AccountingCustomerParty>\n"
                        + "        <cbc:CustomerAssignedAccountID>" + ((clsResumenDiarioItem) resumenBoletas.getItems().elementAt(i)).getAdquirente_NroDocumento() + "</cbc:CustomerAssignedAccountID>\n"
                        + "        <cbc:AdditionalAccountID>" + ((clsResumenDiarioItem) resumenBoletas.getItems().elementAt(i)).getAdquirente_TipoDoc() + "</cbc:AdditionalAccountID>\n"
                        + "    </cac:AccountingCustomerParty>\n";
                if (((clsResumenDiarioItem) resumenBoletas.getItems().elementAt(i)).getTipoDoc().equals("07") || ((clsResumenDiarioItem) resumenBoletas.getItems().elementAt(i)).getTipoDoc().equals("08")) {
                    linea = linea + "    <cac:BillingReference>\n"
                            + "        <cac:InvoiceDocumentReference>\n"
                            + "            <cbc:ID>" + ((clsResumenDiarioItem) resumenBoletas.getItems().elementAt(i)).getSerieNumeroReferencia() + "</cbc:ID>\n"
                            + "            <cbc:DocumentTypeCode>" + ((clsResumenDiarioItem) resumenBoletas.getItems().elementAt(i)).getTipoDocReferencia() + "</cbc:DocumentTypeCode>\n"
                            + "        </cac:InvoiceDocumentReference>\n"
                            + "    </cac:BillingReference>\n";
                }

                linea = linea + "    <cac:Status>\n"
                        + "        <cbc:ConditionCode>" + ((clsResumenDiarioItem) resumenBoletas.getItems().elementAt(i)).getEstado() + "</cbc:ConditionCode>\n"
                        + "    </cac:Status>\n"
                        + "    <sac:TotalAmount currencyID=\"PEN\">" + ((clsResumenDiarioItem) resumenBoletas.getItems().elementAt(i)).getTotalVenta() + "</sac:TotalAmount>\n";
                if (((clsResumenDiarioItem) resumenBoletas.getItems().elementAt(i)).getTipoValorVenta().equals("01")) {
                    linea = linea + "    <sac:BillingPayment>\n"
                            + "        <cbc:PaidAmount currencyID=\"PEN\">" + ((clsResumenDiarioItem) resumenBoletas.getItems().elementAt(i)).getTotalVVentaOpeGravadas() + "</cbc:PaidAmount>\n"
                            + "        <cbc:InstructionID>01</cbc:InstructionID>\n"
                            + "    </sac:BillingPayment>\n";
                }
                if (((clsResumenDiarioItem) resumenBoletas.getItems().elementAt(i)).getTipoValorVenta().equals("02")) {
                    linea = linea + "    <sac:BillingPayment>\n"
                            + "        <cbc:PaidAmount currencyID=\"PEN\">" + ((clsResumenDiarioItem) resumenBoletas.getItems().elementAt(i)).getTotalVVentaOpeExoneradas() + "</cbc:PaidAmount>\n"
                            + "        <cbc:InstructionID>02</cbc:InstructionID>\n"
                            + "    </sac:BillingPayment>\n";
                }

                /*  + "    <sac:BillingPayment>\n"
                 + "        <cbc:PaidAmount currencyID=\"PEN\">" + ((SummaryDocumentItem) resumenBoletas.getItems().elementAt(i)).getTotalVVentaOpeInafectas() + "</cbc:PaidAmount>\n"
                 + "        <cbc:InstructionID>03</cbc:InstructionID>\n"
                 + "    </sac:BillingPayment>\n"*/
                if (((clsResumenDiarioItem) resumenBoletas.getItems().elementAt(i)).getTipoValorVenta().equals("05")) {

                    linea = linea + "    <sac:BillingPayment>\n"
                            + "        <cbc:PaidAmount currencyID=\"PEN\">" + ((clsResumenDiarioItem) resumenBoletas.getItems().elementAt(i)).getTotalVVentaOpeGratuitas() + "</cbc:PaidAmount>\n"
                            + "        <cbc:InstructionID>05</cbc:InstructionID>\n"
                            + "    </sac:BillingPayment>\n";
                }
                /*   + "    <cac:AllowanceCharge>\n"
                 + "        <cbc:ChargeIndicator>true</cbc:ChargeIndicator>\n"
                 + "        <cbc:Amount currencyID=\"PEN\">0.00</cbc:Amount>\n"
                 + "    </cac:AllowanceCharge>\n"*/
                /*   + "    <cac:TaxTotal>\n"
                 + "        <cbc:TaxAmount currencyID=\"PEN\">" + ((SummaryDocumentItem) resumenBoletas.getItems().elementAt(i)).getTotalISC() + "</cbc:TaxAmount>\n"
                 + "        <cac:TaxSubtotal>\n"
                 + "            <cbc:TaxAmount currencyID=\"PEN\">" + ((SummaryDocumentItem) resumenBoletas.getItems().elementAt(i)).getTotalISC() + "</cbc:TaxAmount>\n"
                 + "            <cac:TaxCategory>\n"
                 + "                <cac:TaxScheme>\n"
                 + "                    <cbc:ID>2000</cbc:ID>\n"
                 + "                    <cbc:Name>ISC</cbc:Name>\n"
                 + "                    <cbc:TaxTypeCode>EXC</cbc:TaxTypeCode>\n"
                 + "                </cac:TaxScheme>\n"
                 + "            </cac:TaxCategory>\n"
                 + "        </cac:TaxSubtotal>\n"
                 + "    </cac:TaxTotal>\n"*/
                //   if (((SummaryDocumentItem) resumenBoletas.getItems().elementAt(i)).getTipoValorVenta().equals("01")||((SummaryDocumentItem) resumenBoletas.getItems().elementAt(i)).getTipoValorVenta().equals("02")) {
                linea = linea + "    <cac:TaxTotal>\n"
                        + "        <cbc:TaxAmount currencyID=\"PEN\">" + ((clsResumenDiarioItem) resumenBoletas.getItems().elementAt(i)).getTotalIGV() + "</cbc:TaxAmount>\n"
                        + "        <cac:TaxSubtotal>\n"
                        + "            <cbc:TaxAmount currencyID=\"PEN\">" + ((clsResumenDiarioItem) resumenBoletas.getItems().elementAt(i)).getTotalIGV() + "</cbc:TaxAmount>\n"
                        + "            <cac:TaxCategory>\n"
                        + "                <cac:TaxScheme>\n"
                        + "                    <cbc:ID>1000</cbc:ID>\n"
                        + "                    <cbc:Name>IGV</cbc:Name>\n"
                        + "                    <cbc:TaxTypeCode>VAT</cbc:TaxTypeCode>\n"
                        + "                </cac:TaxScheme>\n"
                        + "            </cac:TaxCategory>\n"
                        + "        </cac:TaxSubtotal>\n"
                        + "    </cac:TaxTotal>\n";
                //    }
                /*   + "    <cac:TaxTotal>\n"
                 + "        <cbc:TaxAmount currencyID=\"PEN\">0.00</cbc:TaxAmount>\n"
                 + "        <cac:TaxSubtotal>\n"
                 + "            <cbc:TaxAmount currencyID=\"PEN\">0.00</cbc:TaxAmount>\n"
                 + "            <cac:TaxCategory>\n"
                 + "                <cac:TaxScheme>\n"
                 + "                    <cbc:ID>9999</cbc:ID>\n"
                 + "                    <cbc:Name>OTROS</cbc:Name>\n"
                 + "                    <cbc:TaxTypeCode>OTH</cbc:TaxTypeCode>\n"
                 + "                </cac:TaxScheme>\n"
                 + "            </cac:TaxCategory>\n"
                 + "        </cac:TaxSubtotal>\n"
                 + "    </cac:TaxTotal>\n"*/
                linea = linea + "</sac:SummaryDocumentsLine>";
                temp = temp + (linea + "\n");
            }
            linea = "</SummaryDocuments>";
            temp = temp + (linea + "\n");
        } catch (Exception e) {
            /*   if (Profile.getInstance().getSendType() == false) {

             Main.invoice.dP.p.getTxtarea().append("Error  " + e.getMessage() + "\n");
             Main.invoice.dP.p.getTxtarea().setCaretPosition(Main.invoice.dP.p.getTxtarea().getDocument().getLength());
             }*/
            temp = "";
            e.printStackTrace();
        }
        return temp.getBytes();
    }
}

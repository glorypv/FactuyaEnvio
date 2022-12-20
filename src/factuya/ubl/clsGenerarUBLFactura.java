/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package factuya.ubl;

import factuya.factura.clsFactura;
import factuya.factura.clsFacturaFormaPago;
import factuya.factura.clsFacturaItem;

/**
 *
 * @author Jose Ayala
 */
public class clsGenerarUBLFactura {

    private clsFactura factura;

    private String tributoDesc;
    private String typeCodeNameCode;
    private String typeCodeCategoryCode;
    private String typeCodeName;

    public clsGenerarUBLFactura(clsFactura _factura) {
        this.factura = _factura;
    }

    public byte[] escribirXMLByte() {
        String linea = "";
        String temp = "";
        String etiquetaCabecera = "";
        String etiquetaCantidad = "";
        String etiquetaTotal = "";
        String tipoDocumento = "";
        try {

            cargarCodigoCatalogo(factura.getTipoTributario());

            if (factura.getTipoDoc().equals("01") || factura.getTipoDoc().equals("03")) {//FacturaBoleta
                etiquetaCabecera = "Invoice";
                etiquetaCantidad = "InvoicedQuantity";
                etiquetaTotal = "LegalMonetaryTotal";

                if (!factura.getRegimenCodigo().equals("DET")) {
                    tipoDocumento = "    <cbc:InvoiceTypeCode listID=\"0101\">" + factura.getTipoDoc() + "</cbc:InvoiceTypeCode>\n";
                } else { //Factura con Detraccion
                    tipoDocumento = "    <cbc:InvoiceTypeCode listID=\"1001\">" + factura.getTipoDoc() + "</cbc:InvoiceTypeCode>\n";
                    tipoDocumento = tipoDocumento + "    <cbc:Note languageLocaleID=\"2006\"><![CDATA[Operacion sujeta a detraccion]]></cbc:Note>";
                }

            } else if (factura.getTipoDoc().equals("07")) {//Nota de Credito
                etiquetaCabecera = "CreditNote";
                etiquetaCantidad = "CreditedQuantity";
                etiquetaTotal = "LegalMonetaryTotal";
            } else if (factura.getTipoDoc().equals("08")) {//Nota de Debito
                etiquetaCabecera = "DebitNote";
                etiquetaCantidad = "DebitedQuantity";
                etiquetaTotal = "RequestedMonetaryTotal";
            }

            /*   CABECERA UBL  */
            linea = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>";
            temp = temp + (linea + "\n");

            linea = "<" + etiquetaCabecera + " xmlns=\"urn:oasis:names:specification:ubl:schema:xsd:" + etiquetaCabecera + "-2\" xmlns:cac=\"urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2\" xmlns:cbc=\"urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2\" xmlns:ccts=\"urn:un:unece:uncefact:documentation:2\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:ext=\"urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2\" xmlns:qdt=\"urn:oasis:names:specification:ubl:schema:xsd:QualifiedDatatypes-2\" xmlns:sac=\"urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1\" xmlns:udt=\"urn:un:unece:uncefact:data:specification:UnqualifiedDataTypesSchemaModule:2\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"> ";
            temp = temp + (linea + "\n");

            linea = "<ext:UBLExtensions>";
            temp = temp + (linea + "\n");

            linea = "        <ext:UBLExtension>\n"
                    + "            <ext:ExtensionContent>\n"
                    + "                <sac:AdditionalInformation xmlns:sac=\"urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1\">\n"
                    + "                    <sac:AdditionalMonetaryTotal>\n"
                    + "                        <cbc:ID>" + factura.getTipoTributario() + "</cbc:ID>\n"
                    + "                        <cbc:PayableAmount currencyID=\"" + factura.getMoneda() + "\">" + factura.getVVenta() + "</cbc:PayableAmount>\n"
                    + "                    </sac:AdditionalMonetaryTotal>\n"
                    + "                    <sac:AdditionalProperty>\n"
                    + "                        <cbc:ID>1000</cbc:ID>\n"
                    + "                        <cbc:Value>" + factura.getTotalEnLetras() + "</cbc:Value>\n"
                    + "                    </sac:AdditionalProperty>\n";

            linea = linea + "                </sac:AdditionalInformation>\n"
                    + "            </ext:ExtensionContent>\n"
                    + "        </ext:UBLExtension>";
            temp = temp + (linea + "\n");

            // Firma
            linea = "        <ext:UBLExtension>\n"
                    + "            <ext:ExtensionContent>"
                    + "            </ext:ExtensionContent>\n"
                    + "        </ext:UBLExtension>";
            temp = temp + (linea + "\n");
            // placa adicional
            linea = "        <ext:UBLExtension>\n"
                    + "            <ext:ExtensionContent>"
                    + "              <cts:AdditionalDocumentInformation xmlns:cts=\"urn:carvajal:names:specification:ubl:peru:schema:xsd:CarvajalAggregateComponents-1\">"
                    + "                <cts:Header>\n"
                    + "                  <cts:AdditionalProperty>\n"
                    + "                    <cts:ID>0114</cts:ID>\n"
                    + "                    <cts:Value>" + factura.getObservaciones() + "</cts:Value>\n"
                    + "                  </cts:AdditionalProperty>\n"
                    + "                </cts:Header>\n"
                    + "              </cts:AdditionalDocumentInformation>\n"
                    + "            </ext:ExtensionContent>\n"
                    + "        </ext:UBLExtension>";
            temp = temp + (linea + "\n");
            linea = "</ext:UBLExtensions>";
            temp = temp + (linea + "\n");

            linea = "    <cbc:UBLVersionID>2.1</cbc:UBLVersionID>\n"
                    + "    <cbc:CustomizationID>2.0</cbc:CustomizationID>\n"
                    + "    <cbc:ID>" + factura.getSerieNumero() + "</cbc:ID>\n"
                    + "    <cbc:IssueDate>" + factura.getFechaEmision() + "</cbc:IssueDate>\n"
                    + tipoDocumento;
            if (factura.getTipoTributario().equals("9996")) { //Operacion Gratuita

                linea = linea + "<cbc:Note languageLocaleID='1002'><![CDATA[TRANSFERENCIA GRATUITA DE UN BIEN Y/O SERVICIO PRESTADO GRATUITAMENTE]]></cbc:Note>\n";
            }
            linea = linea + "    <cbc:DocumentCurrencyCode>" + factura.getMoneda() + "</cbc:DocumentCurrencyCode>";
            temp = temp + (linea + "\n");
            // Activo para notas de credito y debito
            if (factura.getTipoDoc().equals("07") || factura.getTipoDoc().equals("08")) {//FacturaBoleta Nota dE Credito o Debito

                linea = "  <cac:DiscrepancyResponse>\n"
                        + "    <cbc:ReferenceID>" + factura.getSerieNumeroReferencia() + "</cbc:ReferenceID>\n"// FACTURA ERFERENCAI
                        + "    <cbc:ResponseCode>" + factura.getTipoNota() + "</cbc:ResponseCode>\n"// codigo tipo de nota motivo
                        + "    <cbc:Description><![CDATA[" + factura.getDescripcion() + "]]></cbc:Description>\n"
                        + "  </cac:DiscrepancyResponse>\n"
                        + "  <cac:BillingReference>\n"
                        + "    <cac:InvoiceDocumentReference>\n"
                        + "      <cbc:ID>" + factura.getSerieNumeroReferencia() + "</cbc:ID>\n"
                        + "      <cbc:DocumentTypeCode>" + factura.getTipoDocReferencia() + "</cbc:DocumentTypeCode>\n"
                        + "    </cac:InvoiceDocumentReference>\n"
                        + "  </cac:BillingReference>";

                temp = temp + (linea + "\n");
            }

            if (!factura.getNumeroOrdenCompra().equals("")) {
                linea = "  <cac:OrderReference>\n    <cbc:ID>" + factura.getNumeroOrdenCompra() + "</cbc:ID>\n  </cac:OrderReference>\n";

                temp = temp + linea + "\n";
            }
            if (!factura.getNumeroGuia().equals("")) {
                linea = "  <cac:DespatchDocumentReference>\n    "
                        + " <cbc:ID>" + factura.getNumeroGuia() + "</cbc:ID>\n  "
                        + " <cbc:DocumentTypeCode>" + factura.getTipoDocumentoGuia() + "</cbc:DocumentTypeCode>\n  "
                        + " </cac:DespatchDocumentReference>\n";

                temp = temp + linea + "\n";

            }

            //Identificador de la Firma
            linea = "  <cac:Signature>\n"
                    + "    <cbc:ID>TTR" + factura.getSerieNumero() + "</cbc:ID>\n" //IDFIRMA
                    + "    <cac:SignatoryParty>\n"
                    + "      <cac:PartyIdentification>\n"
                    + "        <cbc:ID>" + factura.getEmisor_RUC() + "</cbc:ID>\n"
                    + "      </cac:PartyIdentification>\n"
                    + "      <cac:PartyName>\n"
                    + "        <cbc:Name><![CDATA[" + factura.getEmisor_RazonSocial() + "]]></cbc:Name>\n"
                    + "      </cac:PartyName>\n"
                    + "    </cac:SignatoryParty>\n"
                    + "    <cac:DigitalSignatureAttachment>\n"
                    + "      <cac:ExternalReference>\n"
                    + "        <cbc:URI>#TTR" + factura.getSerieNumero() + "</cbc:URI>\n"//CAMBIAR
                    + "      </cac:ExternalReference>\n"
                    + "    </cac:DigitalSignatureAttachment>\n"
                    + "  </cac:Signature>";
            temp = temp + (linea + "\n");

            linea = "  <cac:AccountingSupplierParty>\n"
                    + "    <cac:Party>\n"
                    + "      <cac:PartyIdentification>\n"
                    + "        <cbc:ID schemeID=\"6\">" + factura.getEmisor_RUC() + "</cbc:ID>\n"
                    + "      </cac:PartyIdentification>\n"
                    + "      <cac:PartyName>\n"
                    + "        <cbc:Name><![CDATA[" + factura.getEmisor_RazonSocial() + "]]></cbc:Name>\n"
                    + "      </cac:PartyName>\n"
                    + "      <cac:PartyLegalEntity>\n"
                    + "        <cbc:RegistrationName><![CDATA[" + factura.getEmisor_RazonSocial() + "]]></cbc:RegistrationName>\n"
                    + "      <cac:RegistrationAddress>\n"
                    + "        <cbc:ID>" + factura.getEmisor_DomicilioFiscalUBIGEO() + "</cbc:ID>\n"
                    + "        <cbc:AddressTypeCode>0000</cbc:AddressTypeCode>\n"
                    + "        <cbc:CityName>" + factura.getEmisor_DomicilioFiscalProvincia() + "</cbc:CityName>\n"
                    + "        <cbc:CountrySubentity>" + factura.getEmisor_DomicilioFiscalDepartamento() + "</cbc:CountrySubentity>\n"
                    + "        <cbc:District>" + factura.getEmisor_DomicilioFiscalDistrito() + "</cbc:District>\n"
                    + "        <cac:AddressLine>\n"
                    + "          <cbc:Line><![CDATA[" + factura.getEmisor_DomicilioFiscal() + "]]></cbc:Line>\n"
                    + "        </cac:AddressLine>\n"
                    + "        <cac:Country>\n"
                    + "          <cbc:IdentificationCode>PE</cbc:IdentificationCode>\n"
                    + "        </cac:Country>\n"
                    + "      </cac:RegistrationAddress>\n"
                    + "      </cac:PartyLegalEntity>\n"
                    + "    </cac:Party>\n"
                    + "  </cac:AccountingSupplierParty>";
            temp = temp + (linea + "\n");

            linea = "  <cac:AccountingCustomerParty>\n"
                    + "    <cac:Party>\n"
                    + "      <cac:PartyIdentification>\n"
                    + "        <cbc:ID schemeID=\"" + factura.getAdquirente_TipoDoc() + "\">" + factura.getAdquirente_NroDocumento() + "</cbc:ID>\n"
                    + "      </cac:PartyIdentification>\n"
                    + "      <cac:PartyLegalEntity>\n"
                    + "        <cbc:RegistrationName><![CDATA[" + factura.getAdquirente_RazonSocial() + "]]></cbc:RegistrationName>\n"
                    + "      </cac:PartyLegalEntity>\n"
                    + "    </cac:Party>\n"
                    + "  </cac:AccountingCustomerParty>";
            temp = temp + (linea + "\n");

            /*DETRACCION ubl 2.1 2021-10-05*/
            if (factura.getRegimenCodigo().equals("DET")) {
                linea = "  <cac:PaymentMeans>\n"
                        + "      <cbc:ID>Detraccion</cbc:ID>\n"
                        + "      <cbc:PaymentMeansCode>001</cbc:PaymentMeansCode>\n"
                        + "      <cac:PayeeFinancialAccount>\n"
                        + "         <cbc:ID>" + factura.getDetraccionCuenta() + "</cbc:ID>\n"
                        + "      </cac:PayeeFinancialAccount>\n"
                        + "   </cac:PaymentMeans>\n"
                        + "   <cac:PaymentTerms>\n"
                        + "      <cbc:ID>Detraccion</cbc:ID>\n"
                        + "      <cbc:PaymentMeansID>" + factura.getDetraccionCod() + "</cbc:PaymentMeansID>\n"
                        + "      <cbc:PaymentPercent>" + factura.getDetraccionPorc() + "</cbc:PaymentPercent>\n"
                        + "      <cbc:Amount currencyID=\"PEN\">" + factura.getDetraccionMonto() + "</cbc:Amount>\n" //la detraccion siempre es en SOLES
                        + "  </cac:PaymentTerms>";
                temp = temp + (linea + "\n");
            }

            /*forma de pago*/
            if (factura.getItemsFormaPago() != null) {
                for (int i = 0; i < factura.getItemsFormaPago().size(); i++) {
                    linea = "  <cac:PaymentTerms>\n"
                            + "        <cbc:ID>" + ((clsFacturaFormaPago) factura.getItemsFormaPago().elementAt(i)).getIndicador() + "</cbc:ID>\n"
                            + "        <cbc:PaymentMeansID>" + ((clsFacturaFormaPago) factura.getItemsFormaPago().elementAt(i)).getTipoTransaccion() + "</cbc:PaymentMeansID>\n";

                    if (!((clsFacturaFormaPago) factura.getItemsFormaPago().elementAt(i)).getTipoTransaccion().equals("Contado")) {
                        linea = linea + "        <cbc:Amount currencyID=\"" + ((clsFacturaFormaPago) factura.getItemsFormaPago().elementAt(i)).getMoneda() + "\">" + ((clsFacturaFormaPago) factura.getItemsFormaPago().elementAt(i)).getMonto() + "</cbc:Amount>\n";

                        if (!((clsFacturaFormaPago) factura.getItemsFormaPago().elementAt(i)).getFecha().equals("")) {
                            linea = linea + "        <cbc:PaymentDueDate>" + ((clsFacturaFormaPago) factura.getItemsFormaPago().elementAt(i)).getFecha() + "</cbc:PaymentDueDate>";
                        }
                    }
                    linea = linea + "  </cac:PaymentTerms>";
                    temp = temp + (linea + "\n");

                }
            }

            /*RETENCION ubl 2.1 2021-12-15*/
            if (factura.getRegimenCodigo().equals("RET")) {

                linea = "  <cac:AllowanceCharge>\n"
                        + "      <cbc:ChargeIndicator>false</cbc:ChargeIndicator>\n"
                        + "      <cbc:AllowanceChargeReasonCode>" + factura.getDetraccionCod() + "</cbc:AllowanceChargeReasonCode>\n"// cvc_retenciontipo
                        + "      <cbc:MultiplierFactorNumeric>" + factura.getDetraccionPorc() + "</cbc:MultiplierFactorNumeric>\n"// //c_retencion porc
                        + "      <cbc:Amount currencyID=\"" + factura.getMoneda() + "\">" + factura.getDetraccionMonto() + "</cbc:Amount>\n" //cvc_retencionmonto
                        + "      <cbc:BaseAmount currencyID=\"" + factura.getMoneda() + "\">" + factura.getTotalVenta() + "</cbc:BaseAmount>\n" //cvc_total
                        + "  </cac:AllowanceCharge>";
                temp = temp + (linea + "\n");
            }


            /*recargo por consumo*/
            if (!factura.getRecargo().equals("0.00")) {
                linea = "  <cac:AllowanceCharge>\n"
                        + "    <cbc:ChargeIndicator>true</cbc:ChargeIndicator>\n"
                        + "    <cbc:AllowanceChargeReasonCode>50</cbc:AllowanceChargeReasonCode>\n"
                        + "    <cbc:AllowanceChargeReason><![CDATA[Recargo Consumo]]></cbc:AllowanceChargeReason>\n"
                        + "    <cbc:Amount currencyID=\"" + factura.getMoneda() + "\">" + factura.getRecargo() + "</cbc:Amount>\n"
                        + "    <cbc:BaseAmount currencyID=\"" + factura.getMoneda() + "\">" + factura.getTotalVenta() + "</cbc:BaseAmount>\n"
                        + "  </cac:AllowanceCharge>";
                temp = temp + (linea + "\n");

            }

            if (!factura.getTipoTributario().equals("9996")) {
                linea = "  <cac:TaxTotal>\n"
                        + "    <cbc:TaxAmount currencyID=\"" + factura.getMoneda() + "\">" + factura.getIGV() + "</cbc:TaxAmount>\n"
                        + "    <cac:TaxSubtotal>\n"
                        + "      <cbc:TaxableAmount currencyID=\"" + factura.getMoneda() + "\">" + factura.getVVenta() + "</cbc:TaxableAmount>\n"
                        + "      <cbc:TaxAmount currencyID=\"" + factura.getMoneda() + "\">" + factura.getIGV() + "</cbc:TaxAmount>\n"
                        + "      <cac:TaxCategory>\n"
                        + "        <cac:TaxScheme>\n"
                        + "          <cbc:ID>" + factura.getTipoTributario() + "</cbc:ID>\n"
                        + "          <cbc:Name>" + typeCodeName + "</cbc:Name>\n"
                        + "          <cbc:TaxTypeCode>" + typeCodeNameCode + "</cbc:TaxTypeCode>\n"
                        + "        </cac:TaxScheme>\n"
                        + "      </cac:TaxCategory>\n"
                        + "    </cac:TaxSubtotal>\n";
                // item gratuito 
                if (!factura.getTotalVentaGratuito().equals("0.00")) {
                    cargarCodigoCatalogo(factura.getTipoTributarioGratuito());
                    linea = linea + "    <cac:TaxSubtotal>\n"
                            + "      <cbc:TaxableAmount currencyID=\"" + factura.getMoneda() + "\">" + factura.getTotalVentaGratuito() + "</cbc:TaxableAmount>\n"
                            + "      <cbc:TaxAmount currencyID=\"" + factura.getMoneda() + "\">0.00</cbc:TaxAmount>\n"
                            + "      <cac:TaxCategory>\n"
                            + "        <cac:TaxScheme>\n"
                            + "          <cbc:ID>" + factura.getTipoTributarioGratuito() + "</cbc:ID>\n"
                            + "          <cbc:Name>" + typeCodeName + "</cbc:Name>\n"
                            + "          <cbc:TaxTypeCode>" + typeCodeNameCode + "</cbc:TaxTypeCode>\n"
                            + "        </cac:TaxScheme>\n"
                            + "      </cac:TaxCategory>\n"
                            + "    </cac:TaxSubtotal>";

                }

            }

            if (factura.getTipoTributario().equals("9996")) {
                cargarCodigoCatalogo(factura.getTipoTributarioGratuito());
                linea
                        = "  <cac:TaxTotal>\n"
                        + "    <cbc:TaxAmount currencyID=\"" + factura.getMoneda() + "\">0.00</cbc:TaxAmount>\n"
                        + "    <cac:TaxSubtotal>\n"
                        + "      <cbc:TaxableAmount currencyID=\"" + factura.getMoneda() + "\">" + factura.getVVenta() + "</cbc:TaxableAmount>\n"
                        + "      <cbc:TaxAmount currencyID=\"" + factura.getMoneda() + "\">" + factura.getIGV() + "</cbc:TaxAmount>\n" // se agrega GRATUTITOS 13-12
                        + "      <cac:TaxCategory>\n"
                        + "        <cac:TaxScheme>\n"
                        + "          <cbc:ID>" + factura.getTipoTributario() + "</cbc:ID>\n"
                        + "          <cbc:Name>" + typeCodeName + "</cbc:Name>\n"
                        + "          <cbc:TaxTypeCode>" + typeCodeNameCode + "</cbc:TaxTypeCode>\n"
                        + "        </cac:TaxScheme>\n"
                        + "      </cac:TaxCategory>\n"
                        + "    </cac:TaxSubtotal>\n";

            }

            linea = linea + "  </cac:TaxTotal>";
            temp = temp + (linea + "\n");

            if (!factura.getTipoTributario().equals("9996")) {

                linea = "  <cac:" + etiquetaTotal + ">\n";
                linea = linea + "    <cbc:LineExtensionAmount currencyID=\"" + factura.getMoneda() + "\">" + factura.getVVenta() + "</cbc:LineExtensionAmount>\n";
                linea = linea + "    <cbc:TaxInclusiveAmount currencyID=\"" + factura.getMoneda() + "\">" + factura.getTotalVenta() + "</cbc:TaxInclusiveAmount>\n";
                if (!factura.getRecargo().equals("0.00")) {
                    linea = linea + "    <cbc:ChargeTotalAmount currencyID=\"" + factura.getMoneda() + "\">" + factura.getRecargo() + "</cbc:ChargeTotalAmount>\n";
                }
                linea = linea + "    <cbc:PayableAmount currencyID=\"" + factura.getMoneda() + "\">" + factura.getTotalVenta() + "</cbc:PayableAmount>\n";
                linea = linea + "  </cac:" + etiquetaTotal + ">";
            }
            if (factura.getTipoTributario().equals("9996")) {//gratuito
                linea = "  <cac:" + etiquetaTotal + ">\n"
                        + "    <cbc:LineExtensionAmount currencyID=\"" + factura.getMoneda() + "\">0.00</cbc:LineExtensionAmount>\n"
                        + "    <cbc:TaxInclusiveAmount currencyID=\"" + factura.getMoneda() + "\">0.00</cbc:TaxInclusiveAmount>\n"// se agrega GRATUTITOS 13-12
                        + "    <cbc:PayableAmount currencyID=\"" + factura.getMoneda() + "\">0.00</cbc:PayableAmount>\n"
                        + "  </cac:" + etiquetaTotal + ">";

            }

            temp = temp + (linea + "\n");

            for (int i = 0; i < factura.getItems().size(); i++) {

                cargarCodigoCatalogo(((clsFacturaItem) factura.getItems().elementAt(i)).getTipoTributoDet());
                linea = "  <cac:" + etiquetaCabecera + "Line>\n"
                        + "    <cbc:ID>" + String.valueOf(i + 1) + "</cbc:ID>\n"
                        + "    <cbc:" + etiquetaCantidad + " unitCode=\"" + ((clsFacturaItem) factura.getItems().elementAt(i)).getUnidad() + "\">" + ((clsFacturaItem) factura.getItems().elementAt(i)).getCantidad() + "</cbc:" + etiquetaCantidad + ">\n";

                if (((clsFacturaItem) factura.getItems().elementAt(i)).getTipoTributoDet().equals("9996") && factura.getTipoTributario().equals("9996")) {

                    linea = linea + "    <cbc:LineExtensionAmount currencyID=\"" + factura.getMoneda() + "\">" + ((clsFacturaItem) factura.getItems().elementAt(i)).getVVenta() + "</cbc:LineExtensionAmount>\n"; // se agrega GRATUTITOS 13-12
                } else {
                    linea = linea + "    <cbc:LineExtensionAmount currencyID=\"" + factura.getMoneda() + "\">" + ((clsFacturaItem) factura.getItems().elementAt(i)).getVVenta() + "</cbc:LineExtensionAmount>\n";
                }

                linea = linea + "    <cac:PricingReference>\n";

                //   if (factura.getTipoTributario().equals("1004")) { //Operacion Gratuita
                linea = linea + "      <cac:AlternativeConditionPrice>\n"
                        + "        <cbc:PriceAmount currencyID=\"" + factura.getMoneda() + "\">" + ((clsFacturaItem) factura.getItems().elementAt(i)).getPUnitario() + "</cbc:PriceAmount>\n"
                        + "        <cbc:PriceTypeCode>" + ((clsFacturaItem) factura.getItems().elementAt(i)).getTipoPrecioVentaDet() + "</cbc:PriceTypeCode>\n"
                        + "      </cac:AlternativeConditionPrice>\n";

                linea = linea + "    </cac:PricingReference>\n";
                //DESCUENTO ITEM
                if (!((clsFacturaItem) factura.getItems().elementAt(i)).getDescuento().equals("0.00")) {
                    linea = linea + "    <cac:AllowanceCharge>\n"
                            + "      <cbc:ChargeIndicator>false</cbc:ChargeIndicator>\n"
                            + "    <cbc:AllowanceChargeReasonCode>00</cbc:AllowanceChargeReasonCode>\n"
                            + "    <cbc:MultiplierFactorNumeric>" + ((clsFacturaItem) factura.getItems().elementAt(i)).getPorcentajeDescuento() + "</cbc:MultiplierFactorNumeric>\n"
                            + "    <cbc:Amount currencyID=\"" + factura.getMoneda() + "\">" + ((clsFacturaItem) factura.getItems().elementAt(i)).getDescuento() + "</cbc:Amount>\n"
                            + "    <cbc:BaseAmount currencyID=\"" + factura.getMoneda() + "\">" + ((clsFacturaItem) factura.getItems().elementAt(i)).getVVentaBruto() + "</cbc:BaseAmount>\n"
                            + "    </cac:AllowanceCharge>\n";
                }
                // DESCUENTO
                linea = linea + "    <cac:TaxTotal>\n"
                        + "      <cbc:TaxAmount currencyID=\"" + factura.getMoneda() + "\">" + ((clsFacturaItem) factura.getItems().elementAt(i)).getIGV() + "</cbc:TaxAmount>\n"
                        + "      <cac:TaxSubtotal>\n";

                linea = linea + "        <cbc:TaxableAmount currencyID=\"" + factura.getMoneda() + "\">" + ((clsFacturaItem) factura.getItems().elementAt(i)).getVVenta() + "</cbc:TaxableAmount>\n"
                        + "        <cbc:TaxAmount currencyID=\"" + factura.getMoneda() + "\">" + ((clsFacturaItem) factura.getItems().elementAt(i)).getIGV() + "</cbc:TaxAmount>\n";

                linea = linea + "        <cac:TaxCategory>\n"
                        + "          <cbc:Percent>" + factura.getPorcentaje() + "</cbc:Percent>\n"
                        + "          <cbc:TaxExemptionReasonCode>" + ((clsFacturaItem) factura.getItems().elementAt(i)).getIGVTipo() + "</cbc:TaxExemptionReasonCode>\n"
                        + "          <cac:TaxScheme>\n"
                        + "            <cbc:ID>" + ((clsFacturaItem) factura.getItems().elementAt(i)).getTipoTributoDet() + "</cbc:ID>\n"
                        + "            <cbc:Name>" + typeCodeName + "</cbc:Name>\n"
                        + "            <cbc:TaxTypeCode>" + typeCodeNameCode + "</cbc:TaxTypeCode>\n"
                        + "          </cac:TaxScheme>\n"
                        + "        </cac:TaxCategory>\n"
                        + "      </cac:TaxSubtotal>\n"
                        + "    </cac:TaxTotal>\n"
                        + "    <cac:Item>\n"
                        + "      <cbc:Description><![CDATA[" + ((clsFacturaItem) factura.getItems().elementAt(i)).getDescripcion() + "]]></cbc:Description>\n";

                if (!((clsFacturaItem) factura.getItems().elementAt(i)).getCodigo().equals("")) {
                    // DescripciÃ³n del producto/servicio -->
                    linea = linea + "      <cac:SellersItemIdentification>\n"
                            // Codigo del producto -->
                            + "        <cbc:ID>" + ((clsFacturaItem) factura.getItems().elementAt(i)).getCodigo() + "</cbc:ID>\n"
                            + "      </cac:SellersItemIdentification>\n";
                }
                if (!((clsFacturaItem) factura.getItems().elementAt(i)).getCodigoSunat().equals("")) {
                    linea = linea + "      <cac:CommodityClassification>\n"
                            // Codigo del producto SUNAT (CatÃ¡logo 25)-->
                            + "      <cbc:ItemClassificationCode>" + ((clsFacturaItem) factura.getItems().elementAt(i)).getCodigoSunat() + "</cbc:ItemClassificationCode>\n"
                            + "      </cac:CommodityClassification>\n";
                }
                linea = linea + "    </cac:Item>\n"
                        + "    <cac:Price>\n";
                if (((clsFacturaItem) factura.getItems().elementAt(i)).getTipoTributoDet().equals("9996")) {

                    linea = linea + "      <cbc:PriceAmount currencyID=\"" + factura.getMoneda() + "\">0.00</cbc:PriceAmount>\n";
                } else {
                    linea = linea + "      <cbc:PriceAmount currencyID=\"" + factura.getMoneda() + "\">" + ((clsFacturaItem) factura.getItems().elementAt(i)).getPVenta() + "</cbc:PriceAmount>\n";

                }

                linea = linea + "    </cac:Price>\n"
                        + "  </cac:" + etiquetaCabecera + "Line>";
                temp = temp + (linea + "\n");

            }

            linea = "</" + etiquetaCabecera + ">";
            temp = temp + (linea + "\n");
            System.out.println("GENERO UBL CORRECTAMENTE");
        } catch (Exception e) {
            temp = "";
            System.out.println("ERROR UBL" + e.getMessage());
            e.printStackTrace();
        }
        return temp.getBytes();
    }

    private void cargarCodigoCatalogo(String code) {
        try {
            if (code.equals("1000")) {
                typeCodeNameCode = "VAT";
                typeCodeCategoryCode = "S";
                typeCodeName = "IGV";
            }
            if (code.equals("9997")) {
                typeCodeNameCode = "VAT";
                typeCodeCategoryCode = "E";
                typeCodeName = "EXO";
            }
            if (code.equals("9996")) {
                typeCodeNameCode = "FRE";
                typeCodeCategoryCode = "Z";
                typeCodeName = "GRA";
            }
            if (code.equals("9998")) {
                typeCodeNameCode = "FRE";
                typeCodeCategoryCode = "O";
                typeCodeName = "INA";
            }
            if (code.equals("2000")) {
                typeCodeNameCode = "EXC";
                typeCodeCategoryCode = "S";
                typeCodeName = "EXC";
            }
            if (code.equals("9999")) {
                typeCodeNameCode = "OTH";
                typeCodeCategoryCode = "S";
                typeCodeName = "OTH";
            }
        } catch (Exception e) {
            typeCodeNameCode = "";
            typeCodeCategoryCode = "";
            typeCodeName = "";
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

}

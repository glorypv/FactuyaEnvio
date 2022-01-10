/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guiaremision.ubl;

import guiaremision.guiaremision.clsGuia;
import guiaremision.guiaremision.clsGuiaItem;

/**
 *
 * @author Gloria Peralta
 *
 */
public class clsGenerarUBLGuia {

    private clsGuia guia;

    public clsGenerarUBLGuia(clsGuia guia) {
        this.guia = guia;
    }

    public byte[] escribirXMLByte() {
        String linea = "";
        String temp = "";

        try {

            /*   CABECERA UBL  */
            linea = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>";
            temp = temp + (linea + "\n");

            linea = "<DespatchAdvice xmlns=\"urn:oasis:names:specification:ubl:schema:xsd:DespatchAdvice-2\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:cac=\"urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2\" xmlns:cbc=\"urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2\" xmlns:ext=\"urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2\">";

            temp = temp + (linea + "\n");

            linea = "<ext:UBLExtensions>";
            temp = temp + (linea + "\n");
            // Firma
            linea = "        <ext:UBLExtension>\n"
                    + "            <ext:ExtensionContent>\n"
                    + "            </ext:ExtensionContent>\n"
                    + "        </ext:UBLExtension>";
            temp = temp + (linea + "\n");

            linea = "</ext:UBLExtensions>";
            temp = temp + (linea + "\n");

            linea = "    <cbc:UBLVersionID>2.1</cbc:UBLVersionID>\n"
                    + "    <cbc:CustomizationID>1.0</cbc:CustomizationID>";
            temp = temp + (linea + "\n");
            /*
             //Identificador de la Firma
             linea = "  <cac:Signature>\n"
             + "    <cbc:ID>TTR" + guia.getSerie_numero() + "</cbc:ID>\n" //IDFIRMA
             + "    <cac:SignatoryParty>\n"
             + "      <cac:PartyIdentification>\n"
             + "        <cbc:ID><![CDATA[" + guia.getEmisor_RUC() + "]]></cbc:ID>\n"
             + "      </cac:PartyIdentification>\n"
             + "      <cac:PartyName>\n"
             + "        <cbc:Name><![CDATA[" + guia.getEmisor_RazonSocial() + "]]></cbc:Name>\n"
             + "      </cac:PartyName>\n"
             + "    </cac:SignatoryParty>\n"
             + "    <cac:DigitalSignatureAttachment>\n"
             + "      <cac:ExternalReference>\n"
             + "        <cbc:URI>#TTR" + guia.getSerie_numero() + "</cbc:URI>\n"//CAMBIAR
             + "      </cac:ExternalReference>\n"
             + "    </cac:DigitalSignatureAttachment>\n"
             + "  </cac:Signature>";
             temp = temp + (linea + "\n");
             */
            linea = "  <cbc:ID>" + guia.getSerie_numero() + "</cbc:ID>\n"
                    + "  <cbc:IssueDate>" + guia.getFecha_emision() + "</cbc:IssueDate>\n"
                    + "  <cbc:DespatchAdviceTypeCode>" + guia.getTipo_documento() + "</cbc:DespatchAdviceTypeCode>";
            temp = temp + (linea + "\n");

            if (!guia.getObservacion().equals("")) {

                linea = "  <cbc:Note><![CDATA[{{ " + guia.getObservacion() + "}}]]></cbc:Note>";
                temp = temp + (linea + "\n");

            }

            if (!guia.getBaja_numero_documento().equals("")) {
                linea = "  <cac:OrderReference>\n"
                        + "  <cbc:ID>" + guia.getBaja_numero_documento() + "</cbc:ID>\n"
                        + "  <cbc:OrderTypeCode>" + guia.getBaja_tipo_documento() + "</cbc:OrderTypeCode>\n"
                        + "  </cac:OrderReference>";
                temp = temp + (linea + "\n");
            }
            if (guia.getAdicional_numero_documento() != null) {
                linea = "  <cac:AdditionalDocumentReference>\n"
                        + "  <cbc:ID>" + guia.getAdicional_numero_documento() + "</cbc:ID>\n"
                        + "  <cbc:DocumentTypeCode>" + guia.getAdicional_codigo_tipo_documento() + "</cbc:DocumentTypeCode>\n"
                        + "  </cac:AdditionalDocumentReference>";
                temp = temp + (linea + "\n");
            }

            linea = "       <cac:DespatchSupplierParty>\n"
                    + "    <cbc:CustomerAssignedAccountID schemeID=\"6\"><![CDATA[" + guia.getRemitente_numero_identidad() + "]]></cbc:CustomerAssignedAccountID>\n"
                    + "      <cac:Party>\n"
                    + "        <cac:PartyLegalEntity>\n"
                    + "          <cbc:RegistrationName><![CDATA[" + guia.getRemitente_apellidos_nombres_razon_social() + "]]></cbc:RegistrationName>\n"
                    + "        </cac:PartyLegalEntity>\n"
                    + "      </cac:Party>\n"
                    + "    </cac:DespatchSupplierParty>";

            temp = temp + (linea + "\n");

            linea = "      <cac:DeliveryCustomerParty>\n"
                    + "    <cbc:CustomerAssignedAccountID schemeID=\"" + guia.getDestinatario_tipo_documento() + "\" ><![CDATA[" + guia.getDestinatario_numero() + "]]></cbc:CustomerAssignedAccountID>\n"
                    + "      <cac:Party>\n"
                    + "        <cac:PartyLegalEntity>\n"
                    + "          <cbc:RegistrationName><![CDATA[" + guia.getDestinatario_apellidos_nombres_razon_social() + "]]></cbc:RegistrationName>\n"
                    + "        </cac:PartyLegalEntity>\n"
                    + "      </cac:Party>\n"
                    + "    </cac:DeliveryCustomerParty>\n";

            temp = temp + (linea + "\n");
            if (!guia.getEstablecimiento_numero_identidad().equals("")) {
                linea = "      <cac:SellerSupplierParty>\n"
                        + "    <cbc:CustomerAssignedAccountID schemeID=\"" + guia.getEstablecimiento_tipo_documento() + "\"><![CDATA[" + guia.getEstablecimiento_numero_identidad() + "]]></cbc:CustomerAssignedAccountID>\n"
                        + "      <cac:Party>\n"
                        + "        <cac:PartyLegalEntity>\n"
                        + "          <cbc:RegistrationName><![CDATA[" + guia.getEstablecimiento_apellidos_nombres_razon_social() + "]]></cbc:RegistrationName>\n"
                        + "        </cac:PartyLegalEntity>\n"
                        + "      </cac:Party>\n"
                        + "    </cac:SellerSupplierParty>\n";

                temp = temp + (linea + "\n");
            }

            // Datos del envio obligatorio
            linea = "  <cac:Shipment>\n"
                    + "         <cbc:ID>1</cbc:ID>\n"
                    + "         <cbc:HandlingCode>" + guia.getEnvio_motivo_traslado() + "</cbc:HandlingCode>\n"
                    + "         <cbc:Information>" + guia.getEnvio_motivo_traslado_descripcion() + "</cbc:Information>\n"
                    + "         <cbc:GrossWeightMeasure unitCode=\"" + guia.getEnvio_unidad_medida() + "\">" + guia.getEnvio_peso_bruto() + "</cbc:GrossWeightMeasure>\n";
            temp = temp + (linea + "\n");
            if (!guia.getEnvio_numero_bultos().equals("")) {
                linea = "         <cbc:TotalTransportHandlingUnitQuantity>" + guia.getEnvio_numero_bultos() + "</cbc:TotalTransportHandlingUnitQuantity>\n";
                temp = temp + (linea + "\n");
            }

            linea = "         <cbc:SplitConsignmentIndicator>" + guia.getEnvio_indicador_transbordo() + "</cbc:SplitConsignmentIndicator>\n"
                    + "         <cac:ShipmentStage>\n"
                    + "             <cbc:TransportModeCode>" + guia.getEnvio_modalidad_traslado() + "</cbc:TransportModeCode>\n"
                    + "                 <cac:TransitPeriod>\n"
                    + "                     <cbc:StartDate>" + guia.getEnvio_fecha_traslado() + "</cbc:StartDate>\n"
                    + "                 </cac:TransitPeriod>\n";
            temp = temp + (linea + "\n");
            if (guia.getTransportista_numero_identidad() != null) {
                linea = "         <cac:CarrierParty>\n"
                        + "             <cac:PartyIdentification>\n"
                        + "                 <cbc:ID schemeID=\"" + guia.getTransportista_tipo_documento() + "\">" + guia.getTransportista_numero_identidad() + "</cbc:ID>\n"
                        + "             </cac:PartyIdentification>\n"
                        + "             <cac:PartyName>\n"
                        + "                 <cbc:Name><![CDATA[" + guia.getTransportista_apellidos_nombres_razon_social() + "]]></cbc:Name>\n"
                        + "             </cac:PartyName>\n"
                        + "         </cac:CarrierParty>\n"
                        + "         <cac:TransportMeans>\n"
                        + "             <cac:RoadTransport>\n"
                        + "                 <cbc:LicensePlateID>" + guia.getVehiculo_numero_placa() + "</cbc:LicensePlateID>\n"
                        + "             </cac:RoadTransport>\n"
                        + "         </cac:TransportMeans>\n";
                if (guia.getConductor_numero_identidad() != null) {
                    linea = linea + "           <cac:DriverPerson>\n"
                            + "             <cbc:ID schemeID=\"" + guia.getConductor_tipo_documento() + "\">" + guia.getConductor_numero_identidad() + "</cbc:ID>\n"
                            + "           </cac:DriverPerson>\n";
                }
                temp = temp + (linea + "\n");
            }
            linea = "      </cac:ShipmentStage>\n";

            temp = temp + (linea + "\n");

            if (!guia.getLlegada_ubigeo().equals("")) {
                linea = "      <cac:Delivery>\n"
                        + "         <cac:DeliveryAddress>\n"
                        + "             <cbc:ID>" + guia.getLlegada_ubigeo() + "</cbc:ID>\n"
                        + "             <cbc:StreetName>" + guia.getLlegada_direccion() + "</cbc:StreetName>\n"
                        + "         </cac:DeliveryAddress>\n"
                        + "      </cac:Delivery>\n";
                temp = temp + (linea + "\n");
            }
            if (!guia.getContenedor_datos().equals("")) {
                linea = "<cac:TransportHandlingUnit>\n"
                        + "         <cbc:ID>" + guia.getContenedor_datos() + "</cbc:ID>\n"
                        + "      </cac:TransportHandlingUnit>\n";
                temp = temp + (linea + "\n");
            }
            if (!guia.getPartida_ubigueo().equals("")) {
                linea = "       <cac:OriginAddress>\n"
                        + "         <cbc:ID>" + guia.getPartida_ubigueo() + "</cbc:ID>\n"
                        + "         <cbc:StreetName>" + guia.getPartida_direccion() + "</cbc:StreetName>\n"
                        + "      </cac:OriginAddress>";

                temp = temp + (linea + "\n");
            }

            if (!guia.getPuerto_codigo().equals("")) {
                linea = "       <cac:FirstArrivalPortLocation>\n"
                        + "         <cbc:ID>" + guia.getPuerto_codigo() + "</cbc:ID>\n"
                        + "      </cac:FirstArrivalPortLocation>\n";
                temp = temp + (linea + "\n");
            }
            linea = "  </cac:Shipment>";
            temp = temp + (linea + "\n");
            for (int i = 0; i < guia.getItems().size(); i++) {
                linea = "     <cac:DespatchLine>\n"
                        + "         <cbc:ID>" + (i + 1) + "</cbc:ID>\n"
                        + "         <cbc:DeliveredQuantity unitCode=\"" + ((clsGuiaItem) guia.getItems().elementAt(i)).getBien_unidad() + "\">" + ((clsGuiaItem) guia.getItems().elementAt(i)).getBien_cantidad() + "</cbc:DeliveredQuantity>\n"
                        + "         <cac:OrderLineReference>\n"
                        + "             <cbc:LineID>" + (i + 1) + "</cbc:LineID>\n"
                        + "         </cac:OrderLineReference>\n"
                        + "         <cac:Item>\n"
                        + "             <cbc:Name><![CDATA[" + ((clsGuiaItem) guia.getItems().elementAt(i)).getBien_descripcion() + "]]></cbc:Name>\n"
                        + "             <cac:SellersItemIdentification>\n"
                        + "                 <cbc:ID>" + ((clsGuiaItem) guia.getItems().elementAt(i)).getBien_codigo() + "</cbc:ID>\n"
                        + "             </cac:SellersItemIdentification>\n"
                        + "         </cac:Item>\n"
                        + "      </cac:DespatchLine>";
                temp = temp + (linea + "\n");
            }
            linea = "      </DespatchAdvice>\n";
            temp = temp + (linea + "\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp.getBytes();

    }
}

/*
    
 SELECT
 gui.gui_serie || gui.gui_numero as gui_numeracion,
 gui.gui_fechaemision,
 gxm.gxm_descripcion,
 --DOCUMENTO RELACIONADO - CONDICIONAL
 cvc.cvc_serie || '-' || cvc.cvc_numero as "10",
 cvc.cvt_codigo as "11",
 --DATOS DEL REMITENTE
 emp.emp_ruc as "13",
 6 as "14",
 emp.emp_razonsocial as "15",
 --DATOS DEL DESTINATARIO
 ent.ent_nrodocumento as "16",
 dxt.dxt_codigosunat as "17",
 COALESCE(ent.ent_apellidopaterno, '') || ' ' || COALESCE(ent.ent_apellidomaterno, '') || ' ' || COALESCE(ent.ent_nombre, '') as "18",
 --DATOS DEL PROVEEDOR
 '' as "19",
 '' as "20",
 '' as "21",
 --DATOS DEL ENVÍO
 '' as "22",
 '' as "23",
 '' as "24",
 '' as "25",
 '' as "26",
 '' as "27",
 '' as "28",
 '' as "29",
 --DATOS DEL TRANSPORTE
 enttra.ent_nrodocumento as "30",
 dxttra.dxt_codigosunat as "31",
 COALESCE(enttra.ent_apellidopaterno, '') || ' ' || COALESCE(enttra.ent_apellidomaterno, '') || ' ' || COALESCE(enttra.ent_nombre, '') as "32",
 --DATOS DEL VEHÍCULO
 vcl.vcl_placa as "33",
 --DATOS DEL CONDUCTOR
 ent.ent_nrodocumento as "34",
 dxtcon.dxt_codigosunat as "35",

 '' as "36",
 gui.gui_destino as "37",
 '' as "38",
 '' as "39",
 '' as "40",
 '' as "41",
 '' as "42",
 '' as "43",
 '' as "44",
 '' as "45",
 '' as "46",
 FROM
 ventas.tblguiasremision_gui as gui
 INNER JOIN ventas.tblguiamotivo_gxm as gxm
 ON gui.gxm_id = gxm.gxm_id
 INNER JOIN ventas.tblguiacomprobantedetalle_gcd as gcd
 ON gui.gui_id = gcd.gui_id
 INNER JOIN ventas.tblcomprobantesventascab_cvc as cvc
 ON gcd.cvc_id = cvc.cvc_id
 INNER JOIN public.tblentidades_ent as ent
 ON gui.gui_entdestinatario = ent.ent_id
 INNER JOIN public.tbldocumentostipos_dxt as dxt
 ON ent.dxt_codigo = dxt.dxt_codigo
 INNER JOIN public.tblempresas_emp as emp
 ON gui.emp_id = emp.emp_id
 INNER JOIN public.tblentidades_ent as enttra
 ON gui.gui_enttransporte = enttra.ent_id
 INNER JOIN public.tbldocumentostipos_dxt as dxttra
 ON enttra.dxt_codigo = dxttra.dxt_codigo
 INNER JOIN public.tblvehiculos_vcl as vcl
 ON gui.vcl_id = vcl.vcl_id
 INNER JOIN public.tblentidades_ent as entcon
 ON gui.gui_entconductor = entcon.ent_id
 INNER JOIN public.tbldocumentostipos_dxt as dxtcon
 ON entcon.dxt_codigo = dxtcon.dxt_codigo;

 TENGO DUDAS CON LOS TIPOS
 EN LA TABLA MOTIVO GUIA TENGO ESTO
 gxm_id emp_id gxm_descripcion
 1 1 VENTA
 2 1 VENTA SUJETA A CONFIRMACION DEL COMPRADOR
 3 1 COMPRA
 4 1 CONSIGNACIÓN
 5 1 DEVOLUCIÓN
 6 1 TRASLADO ENTRE ESTABLECIMIENTO DE UNA MISMA EMPRESA
 7 1 TRASLADO DE BIENES PARA CONFIRMACIÓN
 8 1 TRASLADO POR EMISOR ITINERANTEDE COMPROBANTES DE PAGO
 9 1 RECOJO DE BIENES
 10 1 TRASLADO ZONA PRIMARIA
 11 1 IMPORTACIÓN
 12 1 EXPORTACIÓN
 13 1 VENTA CON ENTREGA A TERCEROS
 14 1 OTROS
 */

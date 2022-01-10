/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package factuya.factura;

;

import java.util.Vector;

/**
 *
 * @author Jose Ayala
 */


public class clsFacturaItem {

    private String Codigo; // 30
    private String CodigoSunat; // 30
    private String Descripcion; // 250
    private String Cantidad; // 16 (12,3)
    private String PUnitario; // 15 (12,2)
    private String PVenta; // 15 (12,2)
    private String Unidad; // 3 
    private String IGV; //15 (12,2) 
    private String ISC; // 15 (12,2)
    private String VVenta; // 15 (12,2)
    private String NroOrden; // 3
    private String IGVTipo; // Catalogo 7 30=transporte pasajeros
    private String PorcentajeDescuento;
    private String Descuento;
    private String VVentaBruto; // 15 (12,2)
    private String TipoPrecioVentaDet; // 15 (12,2)
    private String TipoTributoDet; // 15 (12,2)

    public String getTipoTributoDet() {
        return TipoTributoDet;
    }

    public void setTipoTributoDet(String TipoTributoDet) {
        this.TipoTributoDet = TipoTributoDet;
    }

    public String getVVentaBruto() {
        return VVentaBruto;
    }

    public void setVVentaBruto(String VVentaBruto) {
        this.VVentaBruto = VVentaBruto;
    }

    public String getPorcentajeDescuento() {
        return PorcentajeDescuento;
    }

    public void setPorcentajeDescuento(String PorcentajeDescuento) {
        this.PorcentajeDescuento = PorcentajeDescuento;
    }

    public String getDescuento() {
        return Descuento;
    }

    public void setDescuento(String Descuento) {
        this.Descuento = Descuento;
    }

    public String getIGVTipo() {
        return IGVTipo;
    }

    public void setIGVTipo(String IGVTipo) {
        this.IGVTipo = IGVTipo;

        //    this.IGVTipo = tipoIGVSUNAT(IGVTipo);
    }

    public String getNroOrden() {
        return NroOrden;
    }

    public void setNroOrden(String NroOrden) {
        this.NroOrden = NroOrden;
    }

    public String getVVenta() {
        return VVenta;
    }

    public void setVVenta(String VVenta) {
        this.VVenta = VVenta;
    }

    public String getISC() {
        return ISC;
    }

    public void setISC(String ISC) {
        this.ISC = ISC;
    }

    public String getIGV() {
        return IGV;
    }

    public void setIGV(String IGV) {
        this.IGV = IGV;
    }

    public String getUnidad() {
        return Unidad;
    }

    public void setUnidad(String Unidad) {
        this.Unidad = Unidad;
    }

    public String getTotal() {
        return PVenta;
    }

    public void setTotal(String Total) {
        this.PVenta = Total;
    }

    public String getPUnitario() {
        return PUnitario;
    }

    public void setPUnitario(String PUnitario) {
        this.PUnitario = PUnitario;
    }

    public void setPVenta(String PVenta) {
        this.PVenta = PVenta;
    }

    public String getPVenta() {
        return PVenta;
    }

    public String getCantidad() {
        return Cantidad;
    }

    public void setCantidad(String Cantidad) {
        this.Cantidad = Cantidad;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String Codigo) {
        this.Codigo = Codigo;
    }

    public String getCodigoSunat() {
        return CodigoSunat;
    }

    public void setCodigoSunat(String CodigoSunat) {
        this.CodigoSunat = CodigoSunat;
    }

    public String getTipoPrecioVentaDet() {
        return TipoPrecioVentaDet;
    }

    public void setTipoPrecioVentaDet(String TipoPrecioVentaDet) {
        this.TipoPrecioVentaDet = TipoPrecioVentaDet;
    }

    private class Campo {

        public String ValorOriginal = "";
        public String ValorSUNAT = "";

        Campo(String valorOriginal, String valorSUNAT) {
            ValorOriginal = valorOriginal;
            ValorSUNAT = valorSUNAT;
        }
    }

    /*
     private String tipoIGVSUNAT(String tipoIGV) {
     String tipoIGVSUNAT = tipoIGV;
     Vector tiposIGV = new Vector();

     // INICIALIZAR tipos igv DE SUNAT CATALOGO No. 07 ISO 4217 – Currency 
     tiposIGV.add(new Campo("IOO", "30"));
     tiposIGV.add(new Campo("GOO", "10"));
     //

     //BUSCAR CODIGO EQUIVALENTE
     for (int i = 0; i < tiposIGV.size(); i++) {
     if (tipoIGV.endsWith(((Campo) tiposIGV.elementAt(i)).ValorOriginal)) {
     tipoIGV = ((Campo) tiposIGV.elementAt(i)).ValorSUNAT;
     break;
     }
     }
     //

     return tipoIGV;
     }*/
    private String unidadSUNAT(String Unidad) {
        String unidad = Unidad;
        Vector unidades = new Vector();

        // INICIALIZAR UNIDADESDE SUNAT CATALOGO No. 02 ISO 4217 – Currency 
        unidades.add(new clsFacturaItem.Campo("UND", "NIU"));
        unidades.add(new clsFacturaItem.Campo("CAJ", "BX"));//BX
        unidades.add(new clsFacturaItem.Campo("SOB", "CG"));
        //
        //BUSCAR CODIGO EQUIVALENTE
        for (int i = 0; i < unidades.size(); i++) {
            if (unidad.equals(((clsFacturaItem.Campo) unidades.elementAt(i)).ValorOriginal)) {
                unidad = ((clsFacturaItem.Campo) unidades.elementAt(i)).ValorSUNAT;
                break;
            }
        }
        //

        return unidad;
    }
}

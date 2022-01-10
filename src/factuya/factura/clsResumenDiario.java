/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package factuya.factura;

import java.util.Vector;

/**
 *
 * @author Jose Ayala
 */
public class clsResumenDiario {

    private String TipoDoc; // Catalogo 1 01=FACTURA 03=BOLETA 07=NOTA DE CREDITO 08=NOTA DE DEBITO
    private String Emisor_RazonSocial; // 100 *
    private String Emisor_RUC; // 11 *
    private String FechaEmision; // AAAA-MM-DD *
    private String FechaGeneracion; // AAAA-MM-DD
    private String Codigo;

    private Vector<clsResumenDiarioItem> Items;

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String Codigo) {
        this.Codigo = Codigo;
    }

    public String getTipoDoc() {
        return TipoDoc;
    }

    public void setTipoDoc(String TipoDoc) {
        this.TipoDoc = TipoDoc;
    }

    public String getEmisor_RazonSocial() {
        return Emisor_RazonSocial;
    }

    public void setEmisor_RazonSocial(String Emisor_RazonSocial) {
        this.Emisor_RazonSocial = Emisor_RazonSocial;
    }

    public String getEmisor_RUC() {
        return Emisor_RUC;
    }

    public void setEmisor_RUC(String Emisor_RUC) {
        this.Emisor_RUC = Emisor_RUC;
    }

    public String getFechaEmision() {
        return FechaEmision;
    }

    public void setFechaEmision(String FechaEmision) {
        this.FechaEmision = FechaEmision;
    }

    public String getFechaGeneracion() {
        return FechaGeneracion;
    }

    public void setFechaGeneracion(String FechaGeneracion) {
        this.FechaGeneracion = FechaGeneracion;
    }

    public Vector<clsResumenDiarioItem> getItems() {
        return Items;
    }

    public void setItems(Vector<clsResumenDiarioItem> Items) {
        this.Items = (Vector<clsResumenDiarioItem>) Items.clone();
    }
}

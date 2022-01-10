/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package guiaremision.guiaremision;

import java.util.Vector;

/**
 *
 * @author Jose Ayala
 */
public class clsComunicacionBaja {

    private String EmisorRazonSocial;
    private String EmisorTipoDoc;
    private String EmisorNroDocumento;
    private String FechaGeneracionDocumento;
    private String FechaGeneracionComunicado;
    private String Codigo;
    private Vector<clsComunicacionBajaItem> Items;

    public Vector<clsComunicacionBajaItem> getItems() {
        return Items;
    }

    public void setItems(Vector<clsComunicacionBajaItem> Items) {
        this.Items = (Vector<clsComunicacionBajaItem>) Items.clone();
    }

    public String getFechaGeneracionComunicado() {
        return FechaGeneracionComunicado;
    }

    public void setFechaGeneracionComunicado(String FechaGeneracionComunicado) {
        this.FechaGeneracionComunicado = FechaGeneracionComunicado;
    }

    public String getFechaGeneracionDocumento() {
        return FechaGeneracionDocumento;
    }

    public void setFechaGeneracionDocumento(String FechaGeneracionDocumento) {
        this.FechaGeneracionDocumento = FechaGeneracionDocumento;
    }

    public String getEmisorNroDocumento() {
        return EmisorNroDocumento;
    }

    public void setEmisorNroDocumento(String EmisorNroDocumento) {
        this.EmisorNroDocumento = EmisorNroDocumento;
    }

    public String getEmisorTipoDoc() {
        return EmisorTipoDoc;
    }

    public void setEmisorTipoDoc(String EmisorTipoDoc) {
        this.EmisorTipoDoc = EmisorTipoDoc;
    }

    public String getEmisorRazonSocial() {
        return EmisorRazonSocial;
    }

    public void setEmisorRazonSocial(String EmisorRazonSocial) {
        this.EmisorRazonSocial = EmisorRazonSocial;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String Codigo) {
        this.Codigo = Codigo;
    }
}

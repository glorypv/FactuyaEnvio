/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package factuya.factura;

/**
 *
 * @author Jose Ayala
 */
public class clsComunicacionBajaItem {

    private String TipoDoc;
    private String Serie;
    private String Numero;
    private String MotivoBaja;

    public String getMotivoBaja() {
        return MotivoBaja;
    }

    public void setMotivoBaja(String MotivoBaja) {
        this.MotivoBaja = MotivoBaja;
    }

    public String getNumero() {
        return Numero;
    }

    public void setNumero(String Numero) {
        this.Numero = Numero;
    }

    public String getSerie() {
        return Serie;
    }

    public void setSerie(String Serie) {
        this.Serie = Serie;
    }

    public String getTipoDoc() {
        return TipoDoc;
    }

    public void setTipoDoc(String TipoDoc) {
        this.TipoDoc = TipoDoc;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package factuya.factura;

;

/**
 *
 * @author Jose Ayala
 */
public class clsResumenDiarioItem {

    private String SerieNumero; // F123-12345678 long 13
    private String SerieNumeroReferencia; // F123-12345678 long 13
    private String TipoDocReferencia; // Catalogo 1 01=FACTURA 03=BOLETA 07=NOTA DE CREDITO 08=NOTA DE DEBITO
    private String Adquirente_TipoDoc; // 1  Catalogo 6*
    private String Adquirente_NroDocumento;
    private String TipoDoc;
    private String TotalVVentaOpeGravadas;
    private String TotalVVentaOpeExoneradas;
    private String TotalVVentaOpeInafectas;
    private String TotalVVentaOpeGratuitas;
    private String TotalISC;
    private String TotalIGV;
    private String TotalVenta;
    private String Estado;
    private String TipoValorVenta;

    public String getTipoValorVenta() {
        return TipoValorVenta;
    }

    public void setTipoValorVenta(String TipoValorVenta) {
        this.TipoValorVenta = TipoValorVenta;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }

    public String getSerieNumeroReferencia() {
        return SerieNumeroReferencia;
    }

    public void setSerieNumeroReferencia(String SerieNumeroReferencia) {
        this.SerieNumeroReferencia = SerieNumeroReferencia;
    }

    public String getSerieNumero() {
        return SerieNumero;
    }

    public void setSerieNumero(String SerieNumero) {
        this.SerieNumero = SerieNumero;
    }

    public String getTipoDocReferencia() {
        return TipoDocReferencia;
    }

    public void setTipoDocReferencia(String TipoDocReferencia) {
        this.TipoDocReferencia = TipoDocReferencia;
    }

    public String getAdquirente_TipoDoc() {
        return Adquirente_TipoDoc;
    }

    public void setAdquirente_TipoDoc(String Adquirente_TipoDoc) {
        this.Adquirente_TipoDoc = Adquirente_TipoDoc;
    }

    public String getAdquirente_NroDocumento() {
        return Adquirente_NroDocumento;
    }

    public void setAdquirente_NroDocumento(String Adquirente_NroDocumento) {
        this.Adquirente_NroDocumento = Adquirente_NroDocumento;
    }

    public String getTotalVVentaOpeGratuitas() {
        return TotalVVentaOpeGratuitas;
    }

    public void setTotalVVentaOpeGratuitas(String TotalVVentaOpeGratuitas) {
        this.TotalVVentaOpeGratuitas = TotalVVentaOpeGratuitas;
    }

    public String getTotalVenta() {
        return TotalVenta;
    }

    public void setTotalVenta(String totalVenta) {
        this.TotalVenta = totalVenta;
    }

    public String getTotalIGV() {
        return TotalIGV;
    }

    public void setTotalIGV(String totalIGV) {
        this.TotalIGV = totalIGV;
    }

    public String getTotalISC() {
        return TotalISC;
    }

    public void setTotalISC(String totalISC) {
        this.TotalISC = totalISC;
    }

    public String getTotalVVentaOpeInafectas() {
        return TotalVVentaOpeInafectas;
    }

    public void setTotalVVentaOpeInafectas(String totalVVentaOpeInafectas) {
        this.TotalVVentaOpeInafectas = totalVVentaOpeInafectas;
    }

    public String getTotalVVentaOpeExoneradas() {
        return TotalVVentaOpeExoneradas;
    }

    public void setTotalVVentaOpeExoneradas(String totalVVentaOpeExoneradas) {
        this.TotalVVentaOpeExoneradas = totalVVentaOpeExoneradas;
    }

    public String getTotalVVentaOpeGravadas() {
        return TotalVVentaOpeGravadas;
    }

    public void setTotalVVentaOpeGravadas(String totalVVentaOpeGravadas) {
        this.TotalVVentaOpeGravadas = totalVVentaOpeGravadas;
    }

    public String getTipoDoc() {
        return TipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.TipoDoc = tipoDoc;
    }
}

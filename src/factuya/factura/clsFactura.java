package factuya.factura;

;

import java.util.Vector;
import herramientas.clsConvertirNumeroAletra;

/**
 *
 * @author Jose Ayala
 */


public class clsFactura {

    private String TipoDoc; // Catalogo 1 01=FACTURA 03=BOLETA 07=NOTA DE CREDITO 08=NOTA DE DEBITO
    private String TipoDocReferencia; // Catalogo 1 01=FACTURA 03=BOLETA 07=NOTA DE CREDITO 08=NOTA DE DEBITO
    private String TipoNota;// Codigo de Tipo de nota credito 01...09 / debito 01-02
    private String Porcentaje; //0.00

    private String ValorVenta; //0.00
    private String ValorVentaBruto; //0.00
    private String Descuento; //0.00

    private String IGV; //0.00
    private String Recargo;
    private String tipoTributario;// Catalog 14  1001:gravado 1002:inafecto 1003:exonerado 1004:gratuito  
    private String TipoTributarioGratuito;// Catalog 14  1001:gravado 1002:inafecto 1003:exonerado 1004:gratuito  
    private String TotalVentaGratuito;
    private String TotalVenta; //0.00
    private String Moneda;  //default PEN long 3
    private String SerieNumero; // F123-12345678 long 13
    private String SerieNumeroReferencia; // F123-12345678 long 13
    private String Descripcion; //  long n
    private String IssueDate; // AAAA-MM-DD *
    private String FechaVencimiento; // AAAA-MM-DD
    private Vector<clsFacturaItem> Items;
    private Vector<clsFacturaFormaPago> ItemsFormaPago;

    private String AccountingSupplierParty_Name; // 100 *
    private String AccountingSupplierParty_PostalAddress; // 100 *
    private String Emisor_DomicilioFiscalUBIGEO;
    private String Emisor_DomicilioFiscalDepartamento;
    private String Emisor_DomicilioFiscalProvincia;
    private String Emisor_DomicilioFiscalDistrito;
    private String AccountingSupplierParty_ID; // 11 *
    private String Adquirente_TipoDoc; // 1  Catalogo 6*
    private String Adquirente_NroDocumento;
    private String Adquirente_RazonSocial;
    private String Adquirente_DomicilioFiscal;
    private String Adquirente_DomicilioFiscalUBIGEO;
    private String Adquirente_DomicilioFiscalDepartamento;
    private String Adquirente_DomicilioFiscalProvincia;
    private String Adquirente_DomicilioFiscalDistrito;
    private String Observaciones;
    private String NumeroOrdenCompra;

    private String detraccionCod;
    private String detraccionPorc;
    private String detraccionMonto;
    private String detraccionCuenta;

    private String regimenCodigo;
    


    public Vector<clsFacturaItem> getItems() {
        return Items;
    }

    public void setItems(Vector<clsFacturaItem> Items) {
        this.Items = (Vector<clsFacturaItem>) Items.clone();
    }

    public Vector<clsFacturaFormaPago> getItemsFormaPago() {
        return ItemsFormaPago;
    }

    public void setItemsFormaPago(Vector<clsFacturaFormaPago> ItemsFormaPago) {
        this.ItemsFormaPago = ItemsFormaPago;
    }

    public String getAdquirente_DomicilioFiscalUBIGEO() {
        return Adquirente_DomicilioFiscalUBIGEO;
    }

    public void setAdquirente_DomicilioFiscalUBIGEO(String Adquirente_DomicilioFiscalUBIGEO) {
        this.Adquirente_DomicilioFiscalUBIGEO = Adquirente_DomicilioFiscalUBIGEO;
    }

    public String getAdquirente_DomicilioFiscalDepartamento() {
        return Adquirente_DomicilioFiscalDepartamento;
    }

    public void setAdquirente_DomicilioFiscalDepartamento(String Adquirente_DomicilioFiscalDepartamento) {
        this.Adquirente_DomicilioFiscalDepartamento = Adquirente_DomicilioFiscalDepartamento;
    }

    public String getAdquirente_DomicilioFiscalProvincia() {
        return Adquirente_DomicilioFiscalProvincia;
    }

    public void setAdquirente_DomicilioFiscalProvincia(String Adquirente_DomicilioFiscalProvincia) {
        this.Adquirente_DomicilioFiscalProvincia = Adquirente_DomicilioFiscalProvincia;
    }

    public String getAdquirente_DomicilioFiscalDistrito() {
        return Adquirente_DomicilioFiscalDistrito;
    }

    public void setAdquirente_DomicilioFiscalDistrito(String Adquirente_DomicilioFiscalDistrito) {
        this.Adquirente_DomicilioFiscalDistrito = Adquirente_DomicilioFiscalDistrito;
    }

    public String getEmisor_DomicilioFiscalDepartamento() {
        return Emisor_DomicilioFiscalDepartamento;
    }

    public void setEmisor_DomicilioFiscalDepartamento(String Emisor_DomicilioFiscalDepartamento) {
        this.Emisor_DomicilioFiscalDepartamento = Emisor_DomicilioFiscalDepartamento;
    }

    public String getEmisor_DomicilioFiscalProvincia() {
        return Emisor_DomicilioFiscalProvincia;
    }

    public void setEmisor_DomicilioFiscalProvincia(String Emisor_DomicilioFiscalProvincia) {
        this.Emisor_DomicilioFiscalProvincia = Emisor_DomicilioFiscalProvincia;
    }

    public String getEmisor_DomicilioFiscalDistrito() {
        return Emisor_DomicilioFiscalDistrito;
    }

    public void setEmisor_DomicilioFiscalDistrito(String Emisor_DomicilioFiscalDistrito) {
        this.Emisor_DomicilioFiscalDistrito = Emisor_DomicilioFiscalDistrito;
    }

    public String getEmisor_DomicilioFiscalUBIGEO() {
        return Emisor_DomicilioFiscalUBIGEO;
    }

    public void setEmisor_DomicilioFiscalUBIGEO(String Emisor_DomicilioFiscalUBIGEO) {
        this.Emisor_DomicilioFiscalUBIGEO = Emisor_DomicilioFiscalUBIGEO;
    }

    public String getAdquirente_DomicilioFiscal() {
        return Adquirente_DomicilioFiscal;
    }

    public void setAdquirente_DomicilioFiscal(String Adquirente_DomicilioFiscal) {
        this.Adquirente_DomicilioFiscal = Adquirente_DomicilioFiscal;
    }
    private String TotalEnLetras;

    public String getTotalEnLetras() {
        TotalEnLetras = clsConvertirNumeroAletra.convertNumberToLetter(Double.valueOf(TotalVenta), Moneda == "PEN" ? "SOLES" : "DOLARES");
        return TotalEnLetras;
    }

    private class Campo {

        public String ValorOriginal = "";
        public String ValorSUNAT = "";

        Campo(String valorOriginal, String valorSUNAT) {
            ValorOriginal = valorOriginal;
            ValorSUNAT = valorSUNAT;
        }
    }

    public String getAdquirente_RazonSocial() {
        return Adquirente_RazonSocial;
    }

    public void setAdquirente_RazonSocial(String Adquirente_RazonSocial) {
        this.Adquirente_RazonSocial = Adquirente_RazonSocial;
    }

    public String getAdquirente_NroDocumento() {
        return Adquirente_NroDocumento;
    }

    public void setAdquirente_NroDocumento(String Adquirente_NroDocumento) {
        this.Adquirente_NroDocumento = Adquirente_NroDocumento;
    }

    public String getAdquirente_TipoDoc() {
        return Adquirente_TipoDoc;
    }

    public void setAdquirente_TipoDoc(String Adquirente_TipoDoc) {
        this.Adquirente_TipoDoc = Adquirente_TipoDoc;
    }

    public String getEmisor_RUC() {
        return AccountingSupplierParty_ID;
    }

    public void setEmisor_RUC(String Emisor_RUC) {
        this.AccountingSupplierParty_ID = Emisor_RUC;
    }

    public String getEmisor_DomicilioFiscal() {
        return AccountingSupplierParty_PostalAddress;
    }

    public void setEmisor_DomicilioFiscal(String Emisor_DomicilioFiscal) {
        this.AccountingSupplierParty_PostalAddress = Emisor_DomicilioFiscal;
    }

    public String getEmisor_RazonSocial() {
        return AccountingSupplierParty_Name;
    }

    public void setEmisor_RazonSocial(String Emisor_RazonSocial) {
        this.AccountingSupplierParty_Name = Emisor_RazonSocial;
    }

    public String getFechaVencimiento() {
        return FechaVencimiento;
    }

    public void setFechaVencimiento(String FechaVencimiento) {
        this.FechaVencimiento = FechaVencimiento;
    }

    public String getFechaEmision() {
        return IssueDate;
    }

    public void setFechaEmision(String FechaEmision) {
        this.IssueDate = FechaEmision;
    }

    public String getSerieNumero() {
        return SerieNumero;
    }

    public void setSerieNumero(String SerieNumero) {
        this.SerieNumero = SerieNumero;
    }

    public String getPorcentaje() {
        return Porcentaje;
    }

    public void setPorcentaje(String Porcentaje) {
        this.Porcentaje = Porcentaje;
    }

    public String getMoneda() {
        return Moneda;
    }

    public void setMoneda(String Moneda) {
        String MonedaSUNAT;
        MonedaSUNAT = monedaSUNAT(Moneda);
        this.Moneda = MonedaSUNAT;
    }

    private String monedaSUNAT(String Moneda) {
        String moneda = Moneda;
        Vector monedas = new Vector();

        // INICIALIZAR MONEDAS DE SUNAT CATALOGO No. 02 ISO 4217 – Currency 
        monedas.add(new Campo("SOL", "PEN"));
        monedas.add(new Campo("DOL", "USD"));
        //
        //BUSCAR CODIGO EQUIVALENTE
        for (int i = 0; i < monedas.size(); i++) {
            if (moneda.equals(((Campo) monedas.elementAt(i)).ValorOriginal)) {
                moneda = ((Campo) monedas.elementAt(i)).ValorSUNAT;
                break;
            }
        }
        //

        return moneda;
    }

    public String getTotalVenta() {
        return TotalVenta;
    }

    public void setTotalVenta(String TotalVenta) {
        this.TotalVenta = TotalVenta;
    }

    public String getIGV() {
        return IGV;
    }

    public void setIGV(String IGV) {
        this.IGV = IGV;
    }

    public String getVVenta() {
        return ValorVenta;
    }

    public void setVVenta(String VVenta) {
        this.ValorVenta = VVenta;
    }

    public String getRecargo() {
        return Recargo;
    }

    public void setRecargo(String Recargo) {
        this.Recargo = Recargo;
    }

    public String getTipoDoc() {
        return TipoDoc;
    }

    public clsFactura(String TipoDoc) {
        this.TipoDoc = TipoDoc;
    }

    public void setValorVenta(String ValorVenta) {
        this.ValorVenta = ValorVenta;
    }

    public void setTotalEnLetras(String TotalEnLetras) {
        this.TotalEnLetras = TotalEnLetras;
    }

    public String getTipoDocReferencia() {
        return TipoDocReferencia;
    }

    public void setTipoDocReferencia(String TipoDocReferencia) {
        this.TipoDocReferencia = TipoDocReferencia;
    }

    public String getSerieNumeroReferencia() {
        return SerieNumeroReferencia;
    }

    public void setSerieNumeroReferencia(String SerieNumeroReferencia) {
        this.SerieNumeroReferencia = SerieNumeroReferencia;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public String getTipoNota() {
        return TipoNota;
    }

    public void setTipoNota(String TipoNota) {
        this.TipoNota = TipoNota;
    }

    public String getTipoTributario() {
        return tipoTributario;
    }

    public void setTipoTributario(String tipoTributario) {
        this.tipoTributario = tipoTributario;
    }

    public String getDescuento() {
        return Descuento;
    }

    public void setDescuento(String Descuento) {
        this.Descuento = Descuento;
    }

    public String getValorVentaBruto() {
        return ValorVentaBruto;
    }

    public void setValorVentaBruto(String ValorVentaBruto) {
        this.ValorVentaBruto = ValorVentaBruto;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String Observaciones) {
        this.Observaciones = Observaciones;
    }

    public String getTipoTributarioGratuito() {
        return TipoTributarioGratuito;
    }

    public void setTipoTributarioGratuito(String TipoTributarioGratuito) {
        this.TipoTributarioGratuito = TipoTributarioGratuito;
    }

    public String getTotalVentaGratuito() {
        return TotalVentaGratuito;
    }

    public void setTotalVentaGratuito(String TotalVentaGratuito) {
        this.TotalVentaGratuito = TotalVentaGratuito;
    }

    public String getNumeroOrdenCompra() {
        return NumeroOrdenCompra;
    }

    public void setNumeroOrdenCompra(String NumeroOrdenCompra) {
        this.NumeroOrdenCompra = NumeroOrdenCompra;
    }

    public String getDetraccionCod() {
        return detraccionCod;
    }

    public void setDetraccionCod(String detraccionCod) {
        this.detraccionCod = detraccionCod;
    }

    public String getDetraccionPorc() {
        return detraccionPorc;
    }

    public void setDetraccionPorc(String detraccionPorc) {
        this.detraccionPorc = detraccionPorc;
    }

    public String getDetraccionMonto() {
        return detraccionMonto;
    }

    public void setDetraccionMonto(String detraccionMonto) {
        this.detraccionMonto = detraccionMonto;
    }

    public String getDetraccionCuenta() {
        return detraccionCuenta;
    }

    public void setDetraccionCuenta(String detraccionCuenta) {
        this.detraccionCuenta = detraccionCuenta;
    }

    public String getRegimenCodigo() {
        return regimenCodigo;
    }

    public void setRegimenCodigo(String regimenCodigo) {
        this.regimenCodigo = regimenCodigo;
    }

}

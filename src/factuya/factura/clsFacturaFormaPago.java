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


public class clsFacturaFormaPago {

    private String Indicador; // FORMAPAGO
    private String TipoTransaccion; // Contado,Credito Cuota001
    private String Moneda; // 16 (12,3)
    private String Monto; // 15 (12,2)
    private String Fecha; // 15 (12,2)

    public String getIndicador() {
        return Indicador;
    }

    public void setIndicador(String Indicador) {
        this.Indicador = Indicador;
    }

    public String getTipoTransaccion() {
        return TipoTransaccion;
    }

    public void setTipoTransaccion(String TipoTransaccion) {
        this.TipoTransaccion = TipoTransaccion;
    }

    public String getMonto() {
        return Monto;
    }

    public void setMonto(String Monto) {
        this.Monto = Monto;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }

    private class Campo {

        public String ValorOriginal = "";
        public String ValorSUNAT = "";

        Campo(String valorOriginal, String valorSUNAT) {
            ValorOriginal = valorOriginal;
            ValorSUNAT = valorSUNAT;
        }
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
        monedas.add(new clsFacturaFormaPago.Campo("SOL", "PEN"));
        monedas.add(new clsFacturaFormaPago.Campo("DOL", "USD"));
        //
        //BUSCAR CODIGO EQUIVALENTE
        for (int i = 0; i < monedas.size(); i++) {
            if (moneda.equals(((clsFacturaFormaPago.Campo) monedas.elementAt(i)).ValorOriginal)) {
                moneda = ((clsFacturaFormaPago.Campo) monedas.elementAt(i)).ValorSUNAT;
                break;
            }
        }
        //

        return moneda;
    }

}

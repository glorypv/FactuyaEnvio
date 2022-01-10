package guiaremision.guiaremision;

import java.util.Vector;

/**
 *
 * @author Gloria Peralta
 * @email gloria.ypv@gmail.com
 */
public class clsGuia {

    private String Emisor_RUC;
    private String Emisor_RazonSocial;
    private String Emisor_DomicilioFiscal;

    private String Emisor_DomicilioFiscalDepartamento;
    private String Emisor_DomicilioFiscalDistrito;
    private String Emisor_DomicilioFiscalProvincia;
    private String Emisor_DomicilioFiscalUBIGEO;

    private String serie_numero;
    private String fecha_emision;
    private String tipo_documento;
    private String observacion;

    private String baja_numero_documento;
    private String baja_codigo_tipo_documento;
    private String baja_tipo_documento;

    private String adicional_numero_documento;
    private String adicional_codigo_tipo_documento;

    private String remitente_numero_identidad;
    private String remitente_tipo_documento;
    private String remitente_apellidos_nombres_razon_social;

    private String destinatario_numero;
    private String destinatario_tipo_documento;
    private String destinatario_apellidos_nombres_razon_social;

    private String establecimiento_numero_identidad;
    private String establecimiento_tipo_documento;
    private String establecimiento_apellidos_nombres_razon_social;

    private String envio_motivo_traslado;
    private String envio_motivo_traslado_descripcion;
    private String envio_indicador_transbordo;
    private String envio_unidad_medida;
    private String envio_peso_bruto;
    private String envio_numero_bultos;
    private String envio_modalidad_traslado;
    private String envio_fecha_traslado;

    private String transportista_numero_identidad;
    private String transportista_tipo_documento;
    private String transportista_apellidos_nombres_razon_social;

    private String conductor_numero_identidad;
    private String conductor_tipo_documento;

    private String vehiculo_numero_placa;
    private String llegada_ubigeo;
    private String llegada_direccion;

    private String contenedor_datos;

    private String partida_ubigueo;
    private String partida_direccion;

    private String puerto_codigo;

    private Vector<clsGuiaItem> Items;

    public Vector<clsGuiaItem> getItems() {
        return Items;
    }

    public void setItems(Vector<clsGuiaItem> Items) {
        this.Items = (Vector<clsGuiaItem>) Items.clone();
    }

    public String getEmisor_RUC() {
        return Emisor_RUC;
    }

    public void setEmisor_RUC(String Emisor_RUC) {
        this.Emisor_RUC = Emisor_RUC;
    }

    public String getEmisor_RazonSocial() {
        return Emisor_RazonSocial;
    }

    public void setEmisor_RazonSocial(String Emisor_RazonSocial) {
        this.Emisor_RazonSocial = Emisor_RazonSocial;
    }

    public String getEmisor_DomicilioFiscal() {
        return Emisor_DomicilioFiscal;
    }

    public void setEmisor_DomicilioFiscal(String Emisor_DomicilioFiscal) {
        this.Emisor_DomicilioFiscal = Emisor_DomicilioFiscal;
    }

    public String getEmisor_DomicilioFiscalDepartamento() {
        return Emisor_DomicilioFiscalDepartamento;
    }

    public void setEmisor_DomicilioFiscalDepartamento(String Emisor_DomicilioFiscalDepartamento) {
        this.Emisor_DomicilioFiscalDepartamento = Emisor_DomicilioFiscalDepartamento;
    }

    public String getEmisor_DomicilioFiscalDistrito() {
        return Emisor_DomicilioFiscalDistrito;
    }

    public void setEmisor_DomicilioFiscalDistrito(String Emisor_DomicilioFiscalDistrito) {
        this.Emisor_DomicilioFiscalDistrito = Emisor_DomicilioFiscalDistrito;
    }

    public String getEmisor_DomicilioFiscalProvincia() {
        return Emisor_DomicilioFiscalProvincia;
    }

    public void setEmisor_DomicilioFiscalProvincia(String Emisor_DomicilioFiscalProvincia) {
        this.Emisor_DomicilioFiscalProvincia = Emisor_DomicilioFiscalProvincia;
    }

    public String getEmisor_DomicilioFiscalUBIGEO() {
        return Emisor_DomicilioFiscalUBIGEO;
    }

    public void setEmisor_DomicilioFiscalUBIGEO(String Emisor_DomicilioFiscalUBIGEO) {
        this.Emisor_DomicilioFiscalUBIGEO = Emisor_DomicilioFiscalUBIGEO;
    }

    public void setSerie_numero(String serie_numero) {
        this.serie_numero = serie_numero;
    }

    public void setFecha_emision(String fecha_emision) {
        this.fecha_emision = fecha_emision;
    }

    public void setTipo_documento(String tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public void setBaja_numero_documento(String baja_numero_documento) {
        this.baja_numero_documento = baja_numero_documento;
    }

    public void setBaja_codigo_tipo_documento(String baja_codigo_tipo_documento) {
        this.baja_codigo_tipo_documento = baja_codigo_tipo_documento;
    }

    public void setBaja_tipo_documento(String baja_tipo_documento) {
        this.baja_tipo_documento = baja_tipo_documento;
    }

    public void setAdicional_numero_documento(String adicional_numero_documento) {
        this.adicional_numero_documento = adicional_numero_documento;
    }

    public void setAdicional_codigo_tipo_documento(String adicional_codigo_tipo_documento) {
        this.adicional_codigo_tipo_documento = adicional_codigo_tipo_documento;
    }

    public void setRemitente_numero_identidad(String remitente_numero_identidad) {
        this.remitente_numero_identidad = remitente_numero_identidad;
    }

    public void setRemitente_tipo_documento(String remitente_tipo_documento) {
        this.remitente_tipo_documento = remitente_tipo_documento;
    }

    public void setRemitente_apellidos_nombres_razon_social(String remitente_apellidos_nombres_razon_social) {
        this.remitente_apellidos_nombres_razon_social = remitente_apellidos_nombres_razon_social;
    }

    public void setDestinatario_numero(String destinatario_numero) {
        this.destinatario_numero = destinatario_numero;
    }

    public void setDestinatario_tipo_documento(String destinatario_tipo_documento) {
        this.destinatario_tipo_documento = destinatario_tipo_documento;
    }

    public void setDestinatario_apellidos_nombres_razon_social(String destinatario_apellidos_nombres_razon_social) {
        this.destinatario_apellidos_nombres_razon_social = destinatario_apellidos_nombres_razon_social;
    }

    public void setEstablecimiento_numero_identidad(String establecimiento_numero_identidad) {
        this.establecimiento_numero_identidad = establecimiento_numero_identidad;
    }

    public void setEstablecimiento_tipo_documento(String establecimiento_tipo_documento) {
        this.establecimiento_tipo_documento = establecimiento_tipo_documento;
    }

    public void setEstablecimiento_apellidos_nombres_razon_social(String establecimiento_apellidos_nombres_razon_social) {
        this.establecimiento_apellidos_nombres_razon_social = establecimiento_apellidos_nombres_razon_social;
    }

    public void setEnvio_motivo_traslado(String envio_motivo_traslado) {
        this.envio_motivo_traslado = envio_motivo_traslado;
    }

    public void setEnvio_motivo_traslado_descripcion(String envio_motivo_traslado_descripcion) {
        this.envio_motivo_traslado_descripcion = envio_motivo_traslado_descripcion;
    }

    public void setEnvio_indicador_transbordo(String envio_indicador_transbordo) {
        this.envio_indicador_transbordo = envio_indicador_transbordo;
    }

    public void setEnvio_unidad_medida(String envio_unidad_medida) {
        this.envio_unidad_medida = envio_unidad_medida;
    }

    public void setEnvio_peso_bruto(String envio_peso_bruto) {
        this.envio_peso_bruto = envio_peso_bruto;
    }

    public void setEnvio_numero_bultos(String envio_numero_bultos) {
        this.envio_numero_bultos = envio_numero_bultos;
    }

    public void setEnvio_modalidad_traslado(String envio_modalidad_traslado) {
        this.envio_modalidad_traslado = envio_modalidad_traslado;
    }

    public void setEnvio_fecha_traslado(String envio_fecha_traslado) {
        this.envio_fecha_traslado = envio_fecha_traslado;
    }

    public void setTransportista_numero_identidad(String transportista_numero_identidad) {
        this.transportista_numero_identidad = transportista_numero_identidad;
    }

    public void setTransportista_tipo_documento(String transportista_tipo_documento) {
        this.transportista_tipo_documento = transportista_tipo_documento;
    }

    public void setTransportista_apellidos_nombres_razon_social(String transportista_apellidos_nombres_razon_social) {
        this.transportista_apellidos_nombres_razon_social = transportista_apellidos_nombres_razon_social;
    }

    public void setConductor_numero_identidad(String conductor_numero_identidad) {
        this.conductor_numero_identidad = conductor_numero_identidad;
    }

    public void setConductor_tipo_documento(String conductor_tipo_documento) {
        this.conductor_tipo_documento = conductor_tipo_documento;
    }

    public void setVehiculo_numero_placa(String vehiculo_numero_placa) {
        this.vehiculo_numero_placa = vehiculo_numero_placa;
    }

    public void setLlegada_ubigeo(String llegada_ubigeo) {
        this.llegada_ubigeo = llegada_ubigeo;
    }

    public void setLlegada_direccion(String llegada_direccion) {
        this.llegada_direccion = llegada_direccion;
    }

    public void setContenedor_datos(String contenedor_datos) {
        this.contenedor_datos = contenedor_datos;
    }

    public void setPartida_ubigueo(String partida_ubigueo) {
        this.partida_ubigueo = partida_ubigueo;
    }

    public void setPartida_direccion(String partida_direccion) {
        this.partida_direccion = partida_direccion;
    }

    public void setPuerto_codigo(String puerto_codigo) {
        this.puerto_codigo = puerto_codigo;
    }

    public String getSerie_numero() {
        return serie_numero;
    }

    public String getFecha_emision() {
        return fecha_emision;
    }

    public String getTipo_documento() {
        return tipo_documento;
    }

    public String getObservacion() {
        return observacion;
    }

    public String getBaja_numero_documento() {
        return baja_numero_documento;
    }

    public String getBaja_codigo_tipo_documento() {
        return baja_codigo_tipo_documento;
    }

    public String getBaja_tipo_documento() {
        return baja_tipo_documento;
    }

    public String getAdicional_numero_documento() {
        return adicional_numero_documento;
    }

    public String getAdicional_codigo_tipo_documento() {
        return adicional_codigo_tipo_documento;
    }

    public String getRemitente_numero_identidad() {
        return remitente_numero_identidad;
    }

    public String getRemitente_tipo_documento() {
        return remitente_tipo_documento;
    }

    public String getRemitente_apellidos_nombres_razon_social() {
        return remitente_apellidos_nombres_razon_social;
    }

    public String getDestinatario_numero() {
        return destinatario_numero;
    }

    public String getDestinatario_tipo_documento() {
        return destinatario_tipo_documento;
    }

    public String getDestinatario_apellidos_nombres_razon_social() {
        return destinatario_apellidos_nombres_razon_social;
    }

    public String getEstablecimiento_numero_identidad() {
        return establecimiento_numero_identidad;
    }

    public String getEstablecimiento_tipo_documento() {
        return establecimiento_tipo_documento;
    }

    public String getEstablecimiento_apellidos_nombres_razon_social() {
        return establecimiento_apellidos_nombres_razon_social;
    }

    public String getEnvio_motivo_traslado() {
        return envio_motivo_traslado;
    }

    public String getEnvio_motivo_traslado_descripcion() {
        return envio_motivo_traslado_descripcion;
    }

    public String getEnvio_indicador_transbordo() {
        return envio_indicador_transbordo;
    }

    public String getEnvio_unidad_medida() {
        return envio_unidad_medida;
    }

    public String getEnvio_peso_bruto() {
        return envio_peso_bruto;
    }

    public String getEnvio_numero_bultos() {
        return envio_numero_bultos;
    }

    public String getEnvio_modalidad_traslado() {
        return envio_modalidad_traslado;
    }

    public String getEnvio_fecha_traslado() {
        return envio_fecha_traslado;
    }

    public String getTransportista_numero_identidad() {
        return transportista_numero_identidad;
    }

    public String getTransportista_tipo_documento() {
        return transportista_tipo_documento;
    }

    public String getTransportista_apellidos_nombres_razon_social() {
        return transportista_apellidos_nombres_razon_social;
    }

    public String getConductor_numero_identidad() {
        return conductor_numero_identidad;
    }

    public String getConductor_tipo_documento() {
        return conductor_tipo_documento;
    }

    public String getVehiculo_numero_placa() {
        return vehiculo_numero_placa;
    }

    public String getLlegada_ubigeo() {
        return llegada_ubigeo;
    }

    public String getLlegada_direccion() {
        return llegada_direccion;
    }

    public String getContenedor_datos() {
        return contenedor_datos;
    }

    public String getPartida_ubigueo() {
        return partida_ubigueo;
    }

    public String getPartida_direccion() {
        return partida_direccion;
    }

    public String getPuerto_codigo() {
        return puerto_codigo;
    }

}

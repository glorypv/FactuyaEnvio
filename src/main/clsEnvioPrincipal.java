/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import factuya.main.clsEnvioFactuya;
import factuyaenvio.herramientas.clsConexion;
import factuyaenvio.herramientas.clsParametrosFactuya;
import factuyaenvio.herramientas.clsSubidaServidorExterno;
import factuyaenvio.utilitario.Utilitario;
import guiaremision.main.clsEnvioGuiaRemision;
import static guiaremision.main.clsEnvioGuiaRemision.enviarUnaGuia;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author Gloria Peralta <gloria.ypv@gmail.com>
 */
public class clsEnvioPrincipal {

    private static Connection conPostgres = null;
    private static Connection conPostgresExterno = null;

    private static String empresa = "";
    private static String ubicacionContext = "";
    private static String usuario = "";

    public static void main(String[] args) {
        try {

            System.out.println(" -- INICIO DE ENVIO  -- \n\n");

            empresa = args[0]; //  "20498596356";
            String tipoEnvio = "";
            System.out.println(args.length);
            if (args.length > 1) {
                tipoEnvio = args[1];
            }
            ubicacionContext = Utilitario.ubicacionProyecto() + "/META-INF/context.xml";

            /*          empresa = "20454405324"; //args[0]; ////  "20498596356";
             String tipoEnvio = ""; //F factura G guia C correo*/
            /*  ubicacionContext = "C:\\Program Files\\Apache Software Foundation\\Apache Tomcat 7.0.34\\webapps\\Factuya" + "/META-INF/context.xml";
             System.out.println(empresa);

             System.out.println(ubicacionContext);

             usuario = "admin";*/
            enviarFactuas();
            enviarGuias();
            if (tipoEnvio.equals("C")) {
                enviarCorreoFacturas();
            }

        } finally {

            System.out.println("\n\n  --  FIN DE ENVIO  --");

        }
    }

    private static void enviarFactuas() {

        try {

            clsParametrosFactuya obtenerParametros = new clsParametrosFactuya(empresa, ubicacionContext);
            obtenerParametros.cargarParametros();

            if (clsParametrosFactuya.subirServidorExterno) {
                obtenerParametros.cargarConexion("jdbc/pgpool_factuya_servidor_externo");
                conPostgresExterno = clsConexion.obtenerConexion(clsParametrosFactuya.host, clsParametrosFactuya.usuarioBD, clsParametrosFactuya.passwordBD);
            }

            obtenerParametros.cargarConexion("jdbc/pgpool_factuya");
            conPostgres = clsConexion.obtenerConexion(clsParametrosFactuya.host, clsParametrosFactuya.usuarioBD, clsParametrosFactuya.passwordBD);

            System.out.println("EnviarFacturas Conexión PostgreSQL: " + conPostgres);

            clsEnvioFactuya enviar = new clsEnvioFactuya(
                    clsParametrosFactuya.numerRUC,//"20498596356",
                    "",//"B001",
                    "",//"12041",
                    null,// ARRARY ID
                    usuario,//"admin",
                    clsParametrosFactuya.enviarSUNAT,// 0,
                    clsParametrosFactuya.enviarResumenDiario,//false,
                    clsParametrosFactuya.ubicacionCertificado,//"C:\\HOME\\LLAVE",
                    clsParametrosFactuya.nombreCertificado,// "certificado_chivay.pfx",
                    clsParametrosFactuya.passwordCertificado,// "chivaycomet2021",
                    clsParametrosFactuya.usuarioSol,// "20498596356C0M3TCP3",
                    clsParametrosFactuya.passwordSol,//"C0M3T007",
                    clsParametrosFactuya.subirServidor,// false,
                    clsParametrosFactuya.ubicacionPrincipal,// "C:\\HOME\\",
                    clsParametrosFactuya.ubicacionServidor,// "C:\\HOME\\",
                    clsParametrosFactuya.ubicacionSunatEnvio,// "\\SUNAT\\ENVIO\\",
                    clsParametrosFactuya.ubicacionSunatRespuesta,//  "\\SUNAT\\\\RPTA\\",
                    clsParametrosFactuya.ubicacionSunatTemporal,//  "\\SUNAT\\\\RPTA\\",
                    clsParametrosFactuya.nombreEsquema,
                    clsParametrosFactuya.nombreEsquemaCPE,
                    clsParametrosFactuya.detracionCuenta,
                    clsParametrosFactuya.subirServidorExterno,
                    clsParametrosFactuya.ubicacionServidorExterno,
                    clsParametrosFactuya.hostSHExterno,
                    clsParametrosFactuya.puertoSHExterno,
                    clsParametrosFactuya.usuarioSHExterno,
                    clsParametrosFactuya.passwordSHExterno,
                    clsParametrosFactuya.ubicacionFormatos,
                    clsParametrosFactuya.ubicacionImagenes,
                    conPostgres,
                    conPostgresExterno
            );

            // enviar.enviarUnaFactura();
            //  enviar.enviarResumenDiarioComunicacionBaja();
            enviar.enviarFacturasPendientes(clsParametrosFactuya.numerRUC);
            if (clsParametrosFactuya.enviarResumenDiario) {
                enviar.enviarResumenDiarioComunicacionBaja();
            }

            if (clsParametrosFactuya.subirServidorExterno) {
                String ubicacionServidorExternoTemp = clsParametrosFactuya.ubicacionPrincipal + clsParametrosFactuya.ubicacionSunatTemporal + usuario;
                clsSubidaServidorExterno subirServidorExterno = new clsSubidaServidorExterno(ubicacionServidorExternoTemp, clsParametrosFactuya.numerRUC, usuario, clsParametrosFactuya.nombreEsquema, clsParametrosFactuya.nombreEsquemaCPE, conPostgres, conPostgresExterno);
                subirServidorExterno.insertarServidorExterno();
                subirServidorExterno.subirServidorExteno(clsParametrosFactuya.hostSHExterno, clsParametrosFactuya.puertoSHExterno, clsParametrosFactuya.usuarioSHExterno, clsParametrosFactuya.passwordSHExterno, clsParametrosFactuya.ubicacionServidorExterno);

                System.out.println(subirServidorExterno.mensaje);
            }

        } finally {

            System.out.println("Cerrar Conexión PostgreSQL: " + conPostgres);
            clsConexion.cerrarConexion(conPostgres);
            if (clsParametrosFactuya.subirServidorExterno) {
                clsConexion.cerrarConexion(conPostgresExterno);
            }
        }
    }

    private static void enviarGuias() {

        try {

            clsParametrosFactuya obtenerParametros = new clsParametrosFactuya(empresa, ubicacionContext);
            obtenerParametros.cargarParametros();

            obtenerParametros.cargarConexion("jdbc/pgpool_factuya");

            conPostgres = clsConexion.obtenerConexion(clsParametrosFactuya.host, clsParametrosFactuya.usuarioBD, clsParametrosFactuya.passwordBD);

            System.out.println("Cerrar Conexión PostgreSQL: " + conPostgres);
            clsEnvioGuiaRemision enviar = new clsEnvioGuiaRemision(
                    clsParametrosFactuya.numerRUC,//"20498596356",
                    "",//"B001",
                    "",//"12041",
                    null,// ARRARY ID
                    "admin",//"admin",
                    clsParametrosFactuya.enviarSUNAT,// 0,
                    clsParametrosFactuya.enviarResumenDiario,//false,
                    clsParametrosFactuya.ubicacionCertificado,//"C:\\HOME\\LLAVE",
                    clsParametrosFactuya.nombreCertificado,// "certificado_chivay.pfx",
                    clsParametrosFactuya.passwordCertificado,// "chivaycomet2021",
                    clsParametrosFactuya.usuarioSol,// "20498596356C0M3TCP3",
                    clsParametrosFactuya.passwordSol,//"C0M3T007",
                    clsParametrosFactuya.subirServidor,// false,
                    clsParametrosFactuya.ubicacionPrincipal,// "C:\\HOME\\",
                    clsParametrosFactuya.ubicacionServidor,// "C:\\HOME\\",
                    clsParametrosFactuya.ubicacionSunatEnvio,// "\\SUNAT\\ENVIO\\",
                    clsParametrosFactuya.ubicacionSunatRespuesta,//  "\\SUNAT\\\\RPTA\\",
                    clsParametrosFactuya.nombreEsquema,
                    clsParametrosFactuya.detracionCuenta,
                    conPostgres
            );

            enviar.enviarGuiasPendientes(clsParametrosFactuya.numerRUC);
            // enviar.enviarFacturasPendientes(clsParametrosFactuya.numerRUC);

        } finally {

            System.out.println("Cerrar Conexión PostgreSQL: " + conPostgres);
            clsConexion.cerrarConexion(conPostgres);

        }
    }

    private static void enviarCorreoFacturas() {
        try {
            clsParametrosFactuya obtenerParametros = new clsParametrosFactuya(empresa, ubicacionContext);
            obtenerParametros.cargarParametros();
            obtenerParametros.cargarConexion("jdbc/pgpool_factuya");
            conPostgres = clsConexion.obtenerConexion(clsParametrosFactuya.host, clsParametrosFactuya.usuarioBD, clsParametrosFactuya.passwordBD);
            clsEnviarCorreoPendientes correoPendientes = new clsEnviarCorreoPendientes("factuya", usuario, conPostgres, obtenerParametros);
            correoPendientes.enviarCorreoPendientes();

        } finally {
            System.out.println("Cerrar Conexión PostgreSQL: " + conPostgres);
            clsConexion.cerrarConexion(conPostgres);

        }
    }

}

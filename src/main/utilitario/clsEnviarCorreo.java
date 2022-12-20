/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.utilitario;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author GloriaPC
 */
public class clsEnviarCorreo {

    private String idInvoice;
    private String emailTo;
    private String[] fileAttach;

    private String nameCustomer;
    private String idCustomer;
    private String nameSupplier;
    private String idSupplier;
    private String date;
    private String dateTransmission;
    private String total;
    private String pagWeb;

    private String ubicacionPrincial;
    private String hostCorreo;
    private String portCorreo;
    private String usuarioCorreo;
    private String passwordCorreo;
    private String usuarioCorreoRemitente;

    private static String tituloMensaje = "";
    private static String cuerpoMensaje = "";

    public clsEnviarCorreo(String idInvoice, String emailTo, String[] fileAttach, String nameCustomer, String idCustomer, String nameSupplier, String idSupplier, String date, String dateTransmission, String total, String pagWeb, String ubicacionPrincial, String hostCorreo, String portCorreo, String usuarioCorreo, String passwordCorreo,String usuarioCorreoRemitente) {
        this.idInvoice = idInvoice;
        this.emailTo = emailTo;
        this.fileAttach = fileAttach;
        this.nameCustomer = nameCustomer;
        this.idCustomer = idCustomer;
        this.nameSupplier = nameSupplier;
        this.idSupplier = idSupplier;
        this.date = date;
        this.dateTransmission = dateTransmission;
        this.total = total;
        this.pagWeb = pagWeb;
        this.ubicacionPrincial = ubicacionPrincial;
        this.hostCorreo = hostCorreo;
        this.portCorreo = portCorreo;
        this.usuarioCorreo = usuarioCorreo;
        this.passwordCorreo = passwordCorreo;
        this.usuarioCorreoRemitente=usuarioCorreoRemitente;
    }

    public Boolean enviarCorreo() {
        Boolean rpta = false;
        try {
            System.out.println("... Crear correo  : " + idInvoice);
            construirMensaje();
            // Propiedades de la conexión
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", hostCorreo);
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", portCorreo);
            props.setProperty("mail.smtp.user", usuarioCorreo);
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");

            // Preparamos la sesion
            Session session = Session.getDefaultInstance(props);

            // Construimos el mensaje
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(usuarioCorreoRemitente, "FactuYa!"));
            //message.setReplyTo(new InternetAddress[] {new InternetAddress(respoder_a)});

            String[] destinatario = emailTo.split(",");
            for (int x = 0; x < destinatario.length; x++) {
                //System.out.println(destinatario[x]);
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario[x]));
            }

            // Se compone la parte del texto
            /*    BodyPart texto = new MimeBodyPart();
             texto.setText(text);*/
            MimeBodyPart texto = new MimeBodyPart();
            texto.setContent(cuerpoMensaje, "text/html; charset=utf-8");

            MimeBodyPart texto1 = new MimeBodyPart();
            // URL url = new URL(Main.PATH+"/IMAGEN/logo.jpeg");//this.getClass().getResource("/icon/logo.jpeg");
            // URLDataSource urlDatasource = new URLDataSource(url);
            DataSource fds = new FileDataSource(new File(ubicacionPrincial + "/IMAGEN/logo-email.jpeg"));

            texto1.setDataHandler(new DataHandler(fds));
            texto1.setHeader("Content-ID", "<image>");

            /*  MimeBodyPart textoMime = new MimeBodyPart();
             String charset = "utf-8";
             String contentType = "text/html";
             textoMime.setText(text, charset, contentType);
             texto = (BodyPart) textoMime;*/
            // Se compone el adjunto 
            BodyPart[] adjunto = new BodyPart[fileAttach.length];
            for (int j = 0; j < fileAttach.length; j++) {

                adjunto[j] = new MimeBodyPart();
                adjunto[j].setDataHandler(
                        new DataHandler(new FileDataSource(fileAttach[j])));

                String[] rutaArchivo = fileAttach[j].split("/");//separamos las palabras que forman la url y las                 ponemos en arreglo  de cadenas
                int nombre = rutaArchivo.length - 1;//del array buscamos la ultima posicion
                adjunto[j].setFileName(rutaArchivo[nombre]);//la ultima posicion debe tener el nombre del archivo

            }

            // Una MultiParte para agrupar texto e imagen.
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);
            multiParte.addBodyPart(texto1);

            for (BodyPart aux : adjunto) {
                multiParte.addBodyPart(aux);
            }

            message.setSubject(tituloMensaje);
            message.setContent(multiParte);

            // Lo enviamos.
            Transport t = session.getTransport("smtp");
            t.connect(usuarioCorreo, passwordCorreo);
            t.sendMessage(message, message.getAllRecipients());
            // Cierre.
            t.close();
            System.out.println("... Se ha enviado correo correctamente : " + idInvoice);
            rpta = true;
        } catch (Exception e) {
            rpta = false;
            System.out.println("... Error, vuelva a intentarlo : " + idInvoice + "    (" + e.getMessage() + ")");
            e.printStackTrace();
        } finally {
            return rpta;
        }

    }

    private void construirMensaje() {
        try {
            tituloMensaje = "INFORMACIÓN: Comprobante Electrónico Nro. " + idInvoice + " ACEPTADA POR SUNAT - " + nameSupplier;
            cuerpoMensaje
                    = "<html>"
                    + "<center>"
                    + "<table width='650' border='border-width: thin' cellspacing='0' cellpadding='0' style='height: 741px;'>"
                    + "<tbody>"
                    + "<tr style='height: 70px;'>"
                    + "<td  bgcolor='' height='70' style='height: 70px; padding-left: 30px; width: 651px; text-align: center;'><img src=\'cid:image\' width=\"400\" height=\"150\"></td>"
                    + "</tr>"
                    + "<tr style='height: 51px;'>"
                    + "<td style='height: 51px; text-align: justify; width: 651px;'>"
                    + "<p style='padding-left: 30px;'><br />Estimado(a),</p>"
                    + "<p style='padding-left: 30px;'><br /><strong>" + nameCustomer.toUpperCase() + "</strong></p>"
                    + "<p style='padding-left: 30px;'>Mediante la presente y por encargo del emisor:&nbsp;</p>"
                    + "<p style='padding-left: 30px;'><b>" + nameSupplier.toUpperCase() + "</b></p>"
                    + "<p style='padding-left: 30px;'>Es grato informar que el Comprobante Electr&oacute;nico Nro.:<span>&nbsp;</span><b>" + idInvoice + "</b>&nbsp;ha sido&nbsp;<strong>ACEPTADA POR SUNAT,&nbsp;</strong>con los siguientes datos:</p>"
                    + "<table width='650' style='height: 182px;'>"
                    + "<tbody>"
                    + "<tr style='height: 182px;'>"
                    + "<td style='width: 535px; height: 182px;'>"
                    + "<p style='padding-left: 30px;'><strong>* N&deg; RUC emisor: " + idSupplier + "</strong></p>"
                    + "<p style='padding-left: 30px;'><strong>* N&deg; RUC receptor: " + idCustomer + "</strong></p>"
                    + "<p style='padding-left: 30px;'><strong>* Fecha emision: " + date + "</strong></p>"
                    + "<p style='padding-left: 30px;'><strong>* Fecha</b><strong>&nbsp;transmisi&oacute;n: " + dateTransmission + "</strong></p>"
                    + "<p style='padding-left: 30px;'><strong>* Monto total del documento: " + total + "</strong></p>"
                    + "</td>"
                    + "<td> </td>"
                    + "<td style='width: 15px; height: 182px;'></td>"
                    + "</tr>"
                    + "</tbody>"
                    + "</table>"
                    + "<p style='padding-left: 30px;'>Se adjunta tambi&eacute;n la representaci&oacute;n impresa, el xml y cdr del comprobante electronico.</p>"
                    + "<p style='padding-left: 30px;'>Recuerde que los comprobantes tambi&eacute;n pueden ser consultados en el link: <a href='" + pagWeb + "' target='_blank' >" + pagWeb + "</a><strong>,</strong>&nbsp;mediante acceso an&oacute;nimo.</p>"
                    + "</td>"
                    + "</tr>"
                    + "<tr style='height: 141px;'>"
                    + "<td style='height: 141px; width: 651px;'>"
                    + "<p style='padding-left: 30px;'><strong>Atentamente.</strong></p>"
                    + "<p style='padding-left: 30px;'><b>Grupo Comet engineering services / FactuYA!&nbsp;</b></p>"
                    + "<p style='padding-left: 30px;'><strong>AGRADECEREMOS NO RESPONDER ESTE CORREO</strong></p>"
                    + "<p style='padding-left: 30px;'><b>Si deseas ser Emisor Electr&oacute;nico cont&aacute;ctanos<span>&nbsp;</span><a href='http://www.factuya.pe/' target='_blank' data-saferedirecturl='https://www.google.com/url?q=http://www.factuya.pe&amp;source=gmail&amp;ust=1540006844828000&amp;usg=AFQjCNGDWfq8u09bvaMfwhZUqx5laBN6HQ' rel='noopener'>www.factuya.pe</a>&nbsp;</b><b>o escr&iacute;benos al correo:<span>&nbsp;</span><a href='mailto:proyectos@open-comet.com' target='_blank' rel='noopener'>proyectos@open-comet.com</a>&nbsp;</b></p>"
                    + "</td>"
                    + "</tr>"
                    + "</tbody>"
                    + "</table>"
                    + "</center>"
                    + "</html>";

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

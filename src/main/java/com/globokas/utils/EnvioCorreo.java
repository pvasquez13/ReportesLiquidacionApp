package com.globokas.utils;

import com.globokas.bean.MailBean;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author pvasquez
 */
public class EnvioCorreo {

    private Address[] generarDireccionesCorreo(String direcciones) throws Exception {

        String[] arregloStr = direcciones.split(";");
        Address[] arregloAdd = new InternetAddress[arregloStr.length];

        for (int i = 0; i < arregloAdd.length; i++) {
            arregloAdd[i] = new InternetAddress(arregloStr[i]);
        }
        return arregloAdd;
    }

    public boolean enviarCorreo(MailBean mailBean) {
        boolean retornoExito = true;

        String emailTO = null;
        String emailCC = null;
        String emailBCC = null;
        String asunto = null;
        String contenido = null;
        List<File> archivosAttach = null;

        if (mailBean != null) {
            emailTO = mailBean.getEmailTO();
            emailCC = mailBean.getEmailCC();
            emailBCC = mailBean.getEmailBCC();
            asunto = mailBean.getAsunto();
            contenido = mailBean.getContenido();
            archivosAttach = mailBean.getArchivosAttach();
        }

        //1.2.- Usuarios Internos
        // echaina@globokas.com
        //2.- Obtenemos la sesión
        Session session;
        try {
            session = obtenerSession(mailBean);
            //3.- Formamos el mensaje
            MimeMessage m = new MimeMessage(session);
            m.setFrom(new InternetAddress(mailBean.getInternetAddress() == null ? "informacion@globokas.com" : mailBean.getInternetAddress()));

            //PARA
            if (emailTO != null && !emailTO.equals("")) {
                m.setRecipients(Message.RecipientType.TO, generarDireccionesCorreo(emailTO));
            }

            //CON COPIA PARA
            if (emailCC != null && !emailCC.equals("")) {
                m.setRecipients(Message.RecipientType.CC, generarDireccionesCorreo(emailCC));
            }

            //OCULTO
            if (emailBCC != null && !emailBCC.equals("")) {
                m.setRecipients(Message.RecipientType.BCC, generarDireccionesCorreo(emailBCC));
            }

            m.setSubject(asunto);
            //m.addHeader("Disposition-Notification-To", "echaina@globokas.com");
            m.setSentDate(new Date());

            String formato = "text/plain";

            if (mailBean != null && mailBean.isHTMLFormat()) {
                formato = "text/html";
            }

            Multipart mp = new MimeMultipart();

            if (archivosAttach != null) {

                for (File archivo : archivosAttach) {
                    MimeBodyPart mbp = new MimeBodyPart();

                    // attach the file to the message
                    FileDataSource fds = new FileDataSource(archivo);
                    mbp.setDataHandler(new DataHandler(fds));
                    mbp.setFileName(fds.getName());

                    mp.addBodyPart(mbp);
                }
            }

            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setContent(contenido, formato);

            mp.addBodyPart(mbp);

            m.setContent(mp);

            //m.setContent(cuerpo, formato);
            //4.- Enviamos el mensaje
            Transport.send(m);

        } catch (Exception e) {
            retornoExito = false;
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return retornoExito;
    }

    private Session obtenerSession(MailBean mailBean) throws Exception {
        Authenticator authenticator = null;

        if (mailBean != null && mailBean.getInternetAddress() != null && !mailBean.getInternetAddress().equals("")) {
            if (mailBean.getInternetAddressPassword() == null) {
                mailBean.setInternetAddressPassword("");
            }

            authenticator = new Authenticator(mailBean);
        } else {
            authenticator = new Authenticator();
        }

        Properties properties = new Properties();
        properties.setProperty("mail.smtp.submitter", authenticator.getPasswordAuthentication().getUserName());
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.starttls.required", "true");

        return Session.getInstance(properties, authenticator);
    }

    private class Authenticator extends javax.mail.Authenticator {

        private PasswordAuthentication authentication;

        public Authenticator() {
//            String username = "informacion@globokas.com";
            String username = ConfigApp.getValue("USUARIO_ENVIO_LIQUIDACION");
//            String password = ConfigApp.getValue("PASSWORD_ENVIO_LIQUIDACION");
            String password = "Informacion1";
            authentication = new PasswordAuthentication(username, password);
        }

        public Authenticator(MailBean mailBean) {
            String username = mailBean.getInternetAddress();
            String password = mailBean.getInternetAddressPassword();
            authentication = new PasswordAuthentication(username, password);
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return authentication;
        }
    }
}

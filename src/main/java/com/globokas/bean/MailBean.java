package com.globokas.bean;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author mmedina
 */
public class MailBean implements Serializable {

    private String emailTO;
    private String emailCC;
    private String emailBCC;
    private String asunto;
    private String contenido;
    private List<File> archivosAttach;

    private boolean HTMLFormat = false;

    private String internetAddress;
    private String internetAddressPassword;

//    public static final int ENVIO_CORREO_DE_EXCEPTIONS = 0;
//    public static final int ENVIO_CORREO_COMUNICADOS = 4;

    public static final String ENVIO_CORREO_DE_EXCEPTIONS_ = "EXCP";

    public MailBean() {
    }

//    public MailBean(int motivoEnvioCorreo) {
//        if (motivoEnvioCorreo == MailBean.ENVIO_CORREO_DE_EXCEPTIONS) {
//            emailTO = "echaina@globokas.com;mchuquillanqui@globokas.com";
//            asunto = "Globokas: Error en la creación de agentes";
//            emailBCC = "mmedina@globokas.com"; //new
//            HTMLFormat = false;
//        } else if (motivoEnvioCorreo == MailBean.ENVIO_CORREO_COMUNICADOS) {
//            asunto = "Globokas: Comunicado";
//            HTMLFormat = true;
//        }
//    }

    public String getEmailTO() {
        return emailTO;
    }

    public void setEmailTO(String emailTO) {
        this.emailTO = emailTO;
    }

    public String getEmailCC() {
        return emailCC;
    }

    public void setEmailCC(String emailCC) {
        this.emailCC = emailCC;
    }

    public String getEmailBCC() {
        return emailBCC;
    }

    public void setEmailBCC(String emailBCC) {
        this.emailBCC = emailBCC;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public List<File> getArchivosAttach() {
        return archivosAttach;
    }

    public void setArchivosAttach(List<File> archivosAttach) {
        this.archivosAttach = archivosAttach;
    }

    public boolean isHTMLFormat() {
        return HTMLFormat;
    }

    public void setHTMLFormat(boolean hTMLFormat) {
        HTMLFormat = hTMLFormat;
    }

    public String getInternetAddress() {
        return internetAddress;
    }

    public void setInternetAddress(String internetAddress) {
        this.internetAddress = internetAddress;
    }

    public String getInternetAddressPassword() {
        return internetAddressPassword;
    }

    public void setInternetAddressPassword(String internetAddressPassword) {
        this.internetAddressPassword = internetAddressPassword;
    }
}

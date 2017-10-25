import com.globokas.bean.LiquidacionBean;
import com.globokas.bean.MailBean;
import com.globokas.dao.sqlDao;
import com.globokas.utils.ConfigApp;
import com.globokas.utils.EnvioCorreo;
import com.globokas.utils.Excel;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pvasquez
 */
public class ReportesLiquidacionApp {

    public static void main(String[] args) {
        try {
            String cliente = args[0];
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, -1);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String fecha;

            String modo_automatico = ConfigApp.getValue("MODO_AUTOMATICO_LIQUIDACION");
            System.out.println("modo_automatico=" + modo_automatico);

            if (modo_automatico.equals("FALSE")) {
                fecha = ConfigApp.getValue("FECHA_OPERACION_LIQUIDACION");
            } else {//FECHA DE HOY
                fecha = df.format(c.getTime());
            }

            System.out.println("INICIO PROCESO FECHA=" + fecha);

            sqlDao sqlDao = new sqlDao();

            List<LiquidacionBean> liquidacionList = new ArrayList<>();
            String[] empresas = ConfigApp.getValue("EMPRESAS_"+cliente).split(",");
            for (String empresa : empresas) {
                List<LiquidacionBean> liquidacionEmpresaList = sqlDao.getLiquidacion(fecha, empresa);
                liquidacionList.addAll(liquidacionEmpresaList);
            }

            String ruta_descuadre = ConfigApp.getValue(cliente+".RUTA.REPORTE");
            String nombreArchivoDescuadres = "Liquidacion"+cliente+"_" + fecha + ".xls";
            String rutaServidor = ruta_descuadre + nombreArchivoDescuadres;
            Excel.crearExcelLiquidacion(liquidacionList, rutaServidor);
            //ENVIO DE CORREO
            String destinatarioTO = ConfigApp.getValue("CORREO_ENVIO");
            String destinatarioCC = ConfigApp.getValue("CORREO_ENVIO_CC");
            String asunto = "Globokas: Reporte Liquidacion "+cliente;
            String contenido = "Estimado Usuario,"
                    + "\r\n"
                    + "\r\n"
                    + "Se adjunta un reporte con el detalle de la liquidacion diaria con la empresa "+cliente+"\n"
                    + "\r\n"
                    + "\r\n"
                    + "* No responder a este buzon de correo ya que se encuentra desatendido.";
            envioCorreo(destinatarioTO, destinatarioCC, asunto, contenido, rutaServidor);
            System.out.println("ENVIO DE CORREO SATISFACTORIO");
        } catch (Exception ex) {
            Logger.getLogger(ReportesLiquidacionApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void envioCorreo(String destinatarioTO, String destinatarioCC, String asunto, String contenido, String rutaServidor) {

        EnvioCorreo envioCorreo = new EnvioCorreo();
        MailBean mailBean = new MailBean();
        mailBean.setAsunto(asunto);
        mailBean.setEmailTO(destinatarioTO);
        mailBean.setEmailCC(destinatarioCC);
        mailBean.setContenido(contenido);

        List<File> files = new ArrayList<>();
        File file = new File(rutaServidor);
        files.add(file);
        mailBean.setArchivosAttach(files);
        envioCorreo.enviarCorreo(mailBean);

    }

}

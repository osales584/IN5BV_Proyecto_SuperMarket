
package org.oliversales.report;

import java.io.InputStream;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.oliversales.db.Conexion;

public class GenerarReportes {
    public static void mostrarReportes(String nombreReporte, String titulo, Map parametro){
        InputStream  reporte = GenerarReportes.class.getResourceAsStream(nombreReporte);
        try{
            JasperReport reporteMaestro = (JasperReport)JRLoader.loadObject(reporte);
            JasperPrint reporteImpreso = JasperFillManager.fillReport(reporteMaestro, parametro, Conexion.getInstance().getConexion());
            JasperViewer visor = new JasperViewer(reporteImpreso, false);
            visor.setTitle(titulo);
            visor.setVisible(true);
            
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
/*
Esta es un Iterface Map
    HahsMap es uno de los objetos que implementa un conjunto de Key-valu.
    Tiene un constructor sin parametros new HashMap() y su finalidad suele 
    referirse para agrupar informacion en un unico objeto.

    Tiene cierta similitud con la coleccion de objetos ArrayList (porque ninugo de los dos conocemos la cantidad de 
    registros) pero con la diferencia de estos no tienen un orden
    se alamacema el registro de una direccion que es generada por una funcion se aplica a la llave
    del registro dentro de memoria fisica

    En java el HashMap posee un espacia en memoria y cuando se guarda un objeto en dicho espacio
    se determina su direccion, aplicando una funcion a la llave que le indiquemeo.
    Cada objeto se identifica mediante algun indentificador apropiado.
*/
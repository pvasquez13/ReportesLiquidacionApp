/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globokas.utils;

import com.globokas.bean.LiquidacionBean;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author pvasquez
 */
public class Excel {

    public static void createCell(Row row, int i, String value, CellStyle style) {
        Cell cell = row.createCell(i);
        cell.setCellValue(value);
        // si no hay estilo, no se aplica
        if (style != null) {
            cell.setCellStyle(style);
        }
    }

    public static void createCell(Row row, int i, Long value, CellStyle style) {
        Cell cell = row.createCell(i);
        cell.setCellValue(value);
        // si no hay estilo, no se aplica
        if (style != null) {
            cell.setCellStyle(style);
        }
    }

    public static void createCell(Row row, int i, Double value, CellStyle style) {
        Cell cell = row.createCell(i);
        cell.setCellValue(value);
        // si no hay estilo, no se aplica
        if (style != null) {
            cell.setCellStyle(style);
        }
    }

    public static void createCellFormula(Row row, int i, String formula, CellStyle style) {
        Cell cell = row.createCell(i);
        cell.setCellType(Cell.CELL_TYPE_FORMULA);
        cell.setCellFormula(formula);
        // si no hay estilo, no se aplica
        if (style != null) {
            cell.setCellStyle(style);
        }
    }

    public static void crearExcelLiquidacion(List<LiquidacionBean> liquidacionList,String filename)
            throws Exception {
        try {

            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String fechaActual = df.format(new Date());

            Workbook wb = new HSSFWorkbook();
            Sheet sheet = wb.createSheet("LIQUIDACION");
//            FileInputStream file = new FileInputStream(new File(filename));
//            Workbook wb = new HSSFWorkbook(file);
//            Sheet sheet = wb.getSheetAt(0);

            int filas = liquidacionList.size();

            CellStyle styleInd = wb.createCellStyle();
            Font fontInd = wb.createFont();
            fontInd.setFontHeightInPoints((short) 9);
            fontInd.setFontName("Arial");
            fontInd.setBoldweight(Font.BOLDWEIGHT_BOLD);
            fontInd.setColor(IndexedColors.BLACK.getIndex());
            styleInd.setFont(fontInd);

            CellStyle style = wb.createCellStyle();
            style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            style.setAlignment(CellStyle.ALIGN_CENTER);
            style.setBorderBottom(CellStyle.BORDER_DASHED);
            style.setBorderLeft(CellStyle.BORDER_DASHED);
            style.setBorderRight(CellStyle.BORDER_DASHED);
            style.setBorderTop(CellStyle.BORDER_DASHED);
            style.setWrapText(true);
            Font font = wb.createFont();
            font.setFontHeightInPoints((short) 9);
            font.setFontName("Arial");
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            font.setColor(IndexedColors.BLACK.getIndex());
            style.setFont(font);

            CellStyle styleCuerpo = wb.createCellStyle();
            Font fontCuerpo = wb.createFont();
            fontCuerpo.setFontHeightInPoints((short) 8);
            fontCuerpo.setFontName("Arial");
            styleCuerpo.setFont(fontCuerpo);

            CellStyle styleFooter = wb.createCellStyle();
            Font fontFooter = wb.createFont();
            fontFooter.setFontHeightInPoints((short) 8);
            fontFooter.setFontName("Arial");
            fontFooter.setBoldweight(Font.BOLDWEIGHT_BOLD);
            styleFooter.setFont(fontFooter);

            CellStyle styleFooterNumero = wb.createCellStyle();
            styleFooterNumero.setFont(fontFooter);
            styleFooterNumero.setDataFormat(wb.createDataFormat().getFormat("0.00"));

            /* INICIO HOJA 1*/
            Row cabecera = sheet.createRow((short) 1);
//            Row cabecera2 = sheet.createRow((short) 0);

            createCell(cabecera, 0, "FECHA", style);
            createCell(cabecera, 1, "CUENTA", style);
            createCell(cabecera, 2, "CANTIDAD", style);
            createCell(cabecera, 3, "IMPORTE", style);

//            createCell(cabecera2, 1, "Reporte Consolidado Telefónica", style);
            for (int i = 0; i < filas; i++) {
                LiquidacionBean log = (LiquidacionBean) liquidacionList.get(i);
                Row row = sheet.createRow((short) (i + 2));

                createCell(row, 0, log.getFecha(), styleCuerpo);
                createCell(row, 1, log.getCuenta(), styleCuerpo);
                createCell(row, 2, log.getCantidad() + "", styleCuerpo);
                createCell(row, 3, log.getImporte() + "", styleCuerpo);

            }

            Row rowFec = sheet.createRow((short) (filas + 4));
            createCell(rowFec, 1, "Fecha de Generación", styleInd);
            createCell(rowFec, 2, ": " + fechaActual, styleCuerpo);

            // Asignar automaticamente el tamaño de las celdas en funcion del contenido
            sheet.setColumnWidth(0, 4000);
            sheet.setColumnWidth(1, 8000);
            sheet.setColumnWidth(2, 4000);
            sheet.setColumnWidth(3, 4000);

            sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 6));

            /* FIN HOJA 1*/
            // Escribir el fichero.
            FileOutputStream fileOut = new FileOutputStream(filename);
            wb.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

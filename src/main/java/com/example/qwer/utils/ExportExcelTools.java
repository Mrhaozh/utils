package com.example.qwer.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExportExcelTools {
    public static void export(HttpServletResponse response) throws IOException {
        //创建工作薄对象
        HSSFWorkbook workbook = new HSSFWorkbook();//这里也可以设置sheet的Name
        HSSFSheet sheet = workbook.createSheet();//创建工作表对象
        HSSFRow row = sheet.createRow(0);//设置第一行，从零开始
        row.setHeightInPoints(24);
        createTitleCell(workbook, row, (short) 0, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, "用户名");
        createTitleCell(workbook, row, (short) 1, HSSFCellStyle.ALIGN_CENTER, HSSFCellStyle.VERTICAL_CENTER, "密码");

        for (int i = 1; i < 10; i++) {

        }
        workbook.setSheetName(0, "用户表");//设置sheet的Name
        //文档输出
        FileOutputStream out = new FileOutputStream("F:\\hzh\\excel\\" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()).toString() + ".xls");
        workbook.write(out);
        out.close();
        try {
            String downFileName = new String("user.xls");
            try {
                downFileName = URLEncoder.encode(downFileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String name = "aaaaaa" + ".xls";
            OutputStream output = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename =" + downFileName);
            response.setContentType("application/msexcel");
            workbook.write(output);
            output.close();
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 创建一个单元格并为其设定指定的对齐方式
     *
     * @param wb     工作簿
     * @param row    行
     * @param column 列
     * @param halign 水平方向对其方式
     * @param valign 垂直方向对其方式
     */
    private static void createTitleCell(Workbook wb, Row row, short column, short halign, short valign, String cellContent) {
        Cell cell = row.createCell(column);  // 创建单元格
        cell.setCellValue(new HSSFRichTextString(cellContent));  // 设置值
        CellStyle cellStyle = wb.createCellStyle(); // 创建单元格样式
        cellStyle.setAlignment(halign);  // 设置单元格水平方向对其方式
        cellStyle.setVerticalAlignment(valign); // 设置单元格垂直方向对其方式
        cell.setCellStyle(cellStyle); // 设置单元格样式　　　　}
        Font font = wb.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 10);//设置字体大小
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        cellStyle.setFont(font);
    }

    private static void createContentCell(Workbook wb, Row row, short column, short halign, short valign, String cellContent) {
        Cell cell = row.createCell(column);  // 创建单元格
        cell.setCellValue(new HSSFRichTextString(cellContent));  // 设置值
        CellStyle cellStyle = wb.createCellStyle(); // 创建单元格样式
        cellStyle.setAlignment(halign);  // 设置单元格水平方向对其方式
        cellStyle.setVerticalAlignment(valign); // 设置单元格垂直方向对其方式
        cell.setCellStyle(cellStyle); // 设置单元格样式　　　　}
        Font font = wb.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 10);//设置字体大小
        cellStyle.setFont(font);
    }
}

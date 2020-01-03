package com.itheima.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;



/**
 * @author cong
 */
public class PoiDemo {
    public static void main(String[] args) throws IOException {

        //readExcel();
        writeExcel();

    }

    /**
     *新建excel表格
     * @throws IOException
     */
    private static void writeExcel() throws IOException {
        //创建工作薄对象
        XSSFWorkbook sheets = new XSSFWorkbook();
        //创建表对象
        XSSFSheet sheet = sheets.createSheet("Hello");
        //创建行对象
        XSSFRow row = sheet.createRow(0);
        //创建单元格写入数据
        row.createCell(0).setCellValue("编号");
        row.createCell(1).setCellValue("姓名");
        row.createCell(2).setCellValue("年龄");

        XSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("1");
        row1.createCell(1).setCellValue("张三");
        row1.createCell(2).setCellValue("23");

        //创建输出流将sheets对象写入磁盘
        FileOutputStream outputStream = new FileOutputStream("E:\\itcast.xlsx");
        sheets.write(outputStream);
        outputStream.flush();
        outputStream.close();
        sheets.close();
    }

    /**
     * 读取excel表格数据
     * @throws IOException
     */
    private static void readExcel() throws IOException {
        //创建工作薄对象
        XSSFWorkbook worksheets = new XSSFWorkbook("E:\\Hello.xlsx");
        //窗机爱你工作表对象
        XSSFSheet sheetAt = worksheets.getSheetAt(0);
        //遍历工作表对象获取行对象
        for (Row row : sheetAt) {
            //遍历行对象获取单元格对象
            for (Cell cell : row) {
                //获取单元格中的值
                String stringCellValue = cell.getStringCellValue();
                System.out.println(stringCellValue);
            }
        }
        //释放工作流
        worksheets.close();
    }
}

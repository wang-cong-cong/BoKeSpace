package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.ReportService;
import com.itheima.service.SetMealService;
import com.itheima.utils.DateUtils;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aspectj.weaver.Lint;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author cong
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;
    @Reference
    private SetMealService setMealService;
    @Reference
    private ReportService reportService;


    /**
     * 查询日期段区间的会员数
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping("/getMemberScopeReport.do")
    public Result getMemberCount(String startDate,String endDate){
        try {
            //使用工具类根据日期区间获取日期列表
            List<String> monthBetween = DateUtils.getMonthBetween(startDate, endDate, "yyyy-MM");
            Map<String,Object> map = new HashMap<>();
            map.put("months",monthBetween);
            List<Integer> memberCountByMonth = memberService.findMemberCountByMonth(monthBetween);
            map.put("memberCount",memberCountByMonth);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        } catch (Exception e) {
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS);
        }

    }

    /**
     * 会员统计
     */
        @RequestMapping("/getMemberReport.do")
    public Result getMemberReport() {
        try {
                Calendar calendar = Calendar.getInstance();
                //获取当前日期往后12个月日期
                calendar.add(Calendar.MONTH, -12);

                LinkedList<String> linkedList = new LinkedList<>();

                for (int i = 0; i < 12; i++) {
                    calendar.add(Calendar.MONTH, 1);
                    linkedList.add(DateUtils.parseDate2String(calendar.getTime()));
                }

                Map<String, Object> map = new HashMap<>();
                map.put("months", linkedList);

                List<Integer> memberCount = memberService.findMemberCountByMonth(linkedList);

                map.put("memberCount", memberCount);

                return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    /**
     * 套餐统计
     */
    @RequestMapping("/getSetmealReport.do")
    public Result getSetmealReport() {
        try {
            List<Map<String, Object>> list = setMealService.findSetmealCount();

            Map<String, Object> map = new HashMap<>();
            map.put("setMealCount", list);

            LinkedList<String> linkedList = new LinkedList<>();
            for (Map<String, Object> stringObjectMap : list) {
                String name = (String) stringObjectMap.get("name");
                linkedList.add(name);
                map.put("setMealName",linkedList);
            }

            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }



    /**
     * 运营数据统计
     * */
    @RequestMapping("/getBusinessReportData.do")
    public Result getBusinessReportData(){
        try {
            Map<String,Object> map =  reportService.getBusinessReport();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 导出excel表格
     */
    @RequestMapping("/exportBusinessReport.do")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            //从数据库查询运营数据
            Map<String, Object> result = reportService.getBusinessReport();

            //今日到诊数
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            //当前日期
            String reportDate = (String) result.get("reportDate");
            //新增会员数
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            //本周到诊数
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            //本月新增会员数
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            //本周新增会员数
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            //总会员数
            Integer totalMember = (Integer) result.get("totalMember");
            //本月预约人数
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            //本月到诊人数
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            //今日预约数
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            //本周预约数
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            //热门套餐
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            //获取模板的绝对路径通过request对象
           String temlateRealPath =  request.getSession().getServletContext().getRealPath("template")+ File.separator+ "report_template.xlsx";
           //获取模板对象
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(temlateRealPath)));
            //获取第一个工作表
            XSSFSheet sheet = workbook.getSheetAt(0);

            //设置数据
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);
            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rowNum = 12;
            for(Map map : hotSetmeal){//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum ++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            //使用输出流进行表格下载,基于浏览器作为客户端下载
            OutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");//代表的是Excel文件类型
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");//指定以附件形式进行下载
            workbook.write(out);

            out.flush();
            out.close();
            workbook.close();
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 导出PDF
     */
    @RequestMapping("/exportBusinessReport4PDF.do")
    public Result exportBusinessReport4PDF(HttpServletResponse response,HttpServletRequest request){
        try {
            //获取运营数据
            Map<String, Object> result = reportService.getBusinessReport();
            //获取热门套餐数据
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            //获取jrxml模板的绝对磁盘路径
            String jrxmlPath = request.getSession().getServletContext().getRealPath("template")+File.separator+ "health_business3.jrxml";
            String jasperPath = request.getSession().getServletContext().getRealPath("template")+File.separator+ "health_business3.jasper";

            //编译模板
            JasperCompileManager.compileReportToFile(jrxmlPath,jasperPath);

            //填充数据
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath, result, new JRBeanCollectionDataSource(hotSetmeal));

            //获取输出流对象，设置头对象
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/pdf");//代表的是Excel文件类型
            response.setHeader("content-Disposition", "attachment;filename=report.pdf");//指定以附件形式进行下载

            //输出pdf
            JasperExportManager.exportReportToPdfStream(jasperPrint,outputStream);

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }
}

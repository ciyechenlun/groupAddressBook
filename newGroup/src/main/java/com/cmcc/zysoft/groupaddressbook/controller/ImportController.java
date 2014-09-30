// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.DateUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.cmcc.zysoft.excel.SmartExcelUtil;
import com.cmcc.zysoft.framework.utils.UUIDUtil;
import com.cmcc.zysoft.framework.utils.UploadUtil;
import com.cmcc.zysoft.framework.utils.XMLUtil;
import com.cmcc.zysoft.groupaddressbook.dto.UserCompanyDto;
import com.cmcc.zysoft.groupaddressbook.service.CompanyVersionService;
import com.cmcc.zysoft.groupaddressbook.service.DepartmentVersionService;
import com.cmcc.zysoft.groupaddressbook.service.DeptMagService;
import com.cmcc.zysoft.groupaddressbook.service.GroupVersionService;
import com.cmcc.zysoft.groupaddressbook.service.ImportService;
import com.cmcc.zysoft.groupaddressbook.service.PCCompanyService;
import com.cmcc.zysoft.groupaddressbook.service.PCEmployeeService;
import com.cmcc.zysoft.groupaddressbook.service.RightconfigService;
import com.cmcc.zysoft.groupaddressbook.service.UserCompanyService;
import com.cmcc.zysoft.groupaddressbook.service.UserDepartmentService;
import com.cmcc.zysoft.groupaddressbook.service.UserService;
import com.cmcc.zysoft.groupaddressbook.util.CacheFileUtil;
import com.cmcc.zysoft.groupaddressbook.util.ExportExcel;
import com.cmcc.zysoft.groupaddressbook.util.ThreadPool;
import com.cmcc.zysoft.groupaddressbook.util.concatPinyin;
import com.cmcc.zysoft.rule.Column;
import com.cmcc.zysoft.rule.RuleUtil;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.Department;
import com.cmcc.zysoft.sellmanager.model.Employee;
import com.cmcc.zysoft.sellmanager.model.SystemUser;
import com.cmcc.zysoft.sellmanager.model.UserCompany;
import com.cmcc.zysoft.sellmanager.model.UserDepartment;
import com.cmcc.zysoft.sellmanager.util.MD5Tools;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.cmcc.zysoft.sysmanage.service.SysEmployeeService;
import com.cmcc.zysoft.sysmanage.service.SystemUserPCService;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：importController
 * <br />版本:1.0.0
 * <br />日期： 2013-3-1 下午4:15:45
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Controller
@RequestMapping("pc/import")
@Scope("session")
public class ImportController extends BaseController{
	
	private static Logger logger = LoggerFactory.getLogger(ImportController.class);
	
	
	@Value("${upload.file.path}")
	private String path;
	
	@Resource
	private ImportService importService;
	
	@Resource
	private DeptMagService deptMagService;
	
	@Resource
	private SysEmployeeService employeeService;
	
	@Resource
	private SystemUserPCService systemUserPCService;
	
	@Resource
	private DepartmentVersionService departmentVersionService;
	
	@Resource
	private RightconfigService rightconfigService;
	
	@Resource
	private PCCompanyService pcCompanyService;
	
	@Resource
	private UserCompanyService userCompanyService;
	
	@Resource
	private PCEmployeeService pcEmployeeService;
	
	@Resource
	private GroupVersionService groupVersionService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private UserDepartmentService userDepartmentService;
	
	@Resource
	private CompanyVersionService companyVersionService;
	
	private int OPERATE_ROW;
	private int TOTAL_ROW;
    //@PostConstruct当bean加载完之后，就会执行init方法，并且将属性实例化；  
    @PostConstruct  
    @RequestMapping(value="/init.htm")
    @ResponseBody
    public void init(){  
    	OPERATE_ROW = 0;  
    	TOTAL_ROW =0;
    } 
	/**
	 * 定义允许上传的文件扩展名
	 */
	private static final HashMap<String, String> extMap = new HashMap<String, String>();
	static{
		extMap.put("image", "gif,jpg,jpeg,png,bmp");
		extMap.put("flash", "swf,flv");
		extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,mp4,asf,rm,rmvb");
		extMap.put("file", "doc,docx,pdf,xls,xlsx,ppt,pptx,htm,html,txt,zip,rar,gz,bz2");
		extMap.put("zip", "zip,rar,tar,tar.gz,war,jar,7z");
		extMap.put("excel", "xls,xlsx,txt,csv");
	}
	
	private static final String SUCCESS_KEY = "success";
	private static final String MESSAGE_KEY = "message";
	
	//运行上传的最大字节数
	private static final long MAX_UPLOAD_SIZE = 52428800;//50M
	
	/**
	 * 跳转到导入信息页面.
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping(value="/main.htm")
	public String main(ModelMap modelMap,String company_id,HttpServletRequest request){
		//查看当前用户是否是企业级管理员
		User user = SecurityContextUtil.getCurrentUser();
		String isManager = this.userService.isManager(user);
		modelMap.addAttribute("manager", isManager);
		
		if(request.getSession().getAttribute("selCompany")
				!=null)
		{
			Object companyObj = request.getSession().getAttribute("selCompany");
			Company comp = (Company)companyObj;
			modelMap.addAttribute("company",comp);
		}
		
/*		List<Map<String, Object>> list = this.pcCompanyService.list("0");
		List<Map<String, Object>> org_list = this.pcCompanyService.list("1");
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("org_list", org_list);
*/		return "web/import";
	}
	
	/**
	 * 智能导入第一步上传excel
	 * @date 2013-4-15 上午9:32:01
	 * @param request
	 * @return
	 */
	@RequestMapping("/smart/upload.htm")
	@ResponseBody
	public Map<String, Object> publish(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String,Object>();
		MultipartHttpServletRequest req = (MultipartHttpServletRequest)request;
		MultipartFile filedata = req.getFile("Filedata");
		String fileName = filedata.getOriginalFilename();
		logger.debug("#上传excel文件,文件名称={}",fileName);
		
		//文件保存路径
		String savePath = path;
		
		//检查扩展名
		String fileType = "excel";
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		if(!Arrays.<String>asList(extMap.get(fileType).split(",")).contains(fileExt)){
			logger.error("#文件类型错误，\n只允许" + extMap.get(fileType) + "格式。");
			map.put(SUCCESS_KEY, false);
			map.put(MESSAGE_KEY, "文件类型错误，\n只允许" + extMap.get(fileType) + "格式。");
			return map;
		}
		
		//检查文件大小
		long fileSize = filedata.getSize();
		if(fileSize > MAX_UPLOAD_SIZE){
			logger.error("#文件上传失败，文件大小超过50M");
			map.put(SUCCESS_KEY, false);
			map.put(MESSAGE_KEY, "文件上传失败，文件大小超过50M");
			return map;
		}
		
		//检查目录是否存在
		File uploadDir = new File(savePath);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
		
		//检查目录写权限
		uploadDir.setWritable(true);
		if(!uploadDir.canWrite()){
			logger.error("#文件上传失败,没有对目录{}的写权限",savePath);
			map.put(SUCCESS_KEY, false);
			map.put(MESSAGE_KEY, "文件上传失败,没有对目录"+savePath+"的写权限");
			return map;
		}
		
		//开始复制文件
		String saveName = "SmartExcel_" + new Date().getTime() + UploadUtil.getExtention(fileName);
		File destFile = new File(savePath + File.separator + saveName);
		logger.debug("#开始拷贝文件");
		try {
			//UploadUtil.copy(filedata, destFile);
			FileCopyUtils.copy(filedata.getBytes(),destFile);
			logger.debug("#开始拷贝完成");
		} catch (IOException e) {
			logger.error("#文件拷贝出现异常,{}",e.getMessage());
			e.printStackTrace();
			map.put(SUCCESS_KEY, false);
			map.put(MESSAGE_KEY, "文件上传失败,文件拷贝出现异常!");
			return map;
		}
		
		map.put(SUCCESS_KEY, true);
		map.put(MESSAGE_KEY, "文件上传成功");
		map.put("savename", saveName);
		logger.debug("#文件上传成功");
		return map;
	}
	
	/**
	 * 解析excel
	 * @param excelName
	 * @param request
	 * @return
	 */
	@RequestMapping("/smart/parse.htm")
	@ResponseBody
	public Map<String,Object> parseExcel(String excelName){
		String excelFilePath = path + File.separator + excelName;
		Map<Integer, List<Object>> excelValueMap = SmartExcelUtil.getExcelValues(excelFilePath);
		List<Column> columns = RuleUtil.parseColumns("cmcc.xml");
		Map<String,Object> map = SmartExcelUtil.autoMatches(excelValueMap, columns);
		return map;
	}
	 private String getRightStr(String sNum){
	        DecimalFormat decimalFormat = new DecimalFormat("#.000000");
	        String resultStr = decimalFormat.format(new Double(sNum));
	        if (resultStr.matches("^[-+]?\\d+\\.[0]+$"))
	        {
	            resultStr = resultStr.substring(0, resultStr.indexOf("."));
	        }
	        return resultStr;
	}
	private String getCellValue(org.apache.poi.ss.usermodel.Cell cell){
		String cellValue = "";
        if (cell == null)
        {
            return cellValue;
        }
        /** *//** 处理数字型的,自动去零 */
        if (org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC == cell.getCellType())
        {
            /** *//** 在excel里,日期也是数字,在此要进行判断 */
            if (HSSFDateUtil.isCellDateFormatted(cell))
            {
                cellValue = Double.toString(DateUtil.getExcelDate(cell.getDateCellValue()));
            }
            else
            {
                cellValue = getRightStr(cell.getNumericCellValue() + "");
            }
        }
        /** *//** 处理字符串型 */
        else if (org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING == cell.getCellType())
        {
            cellValue = cell.getStringCellValue();
        }
        /** *//** 处理布尔型 */
        else if (org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN == cell.getCellType())
        {
            cellValue = cell.getBooleanCellValue() + "";
        }
        /** *//** 其它的,非以上几种数据类型 */
        else
        {
            cellValue = cell.toString() + "";
        }
        return cellValue;
	}
	private void addCell(WritableSheet sheetW,org.apache.poi.ss.usermodel.Sheet sheetR,int index,String title) throws RowsExceededException, WriteException{
		int length = sheetR.getPhysicalNumberOfRows();
		if(title.equals("姓名")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(0, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}else if(title.equals("性别")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(1, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}else if(title.equals("主要号码")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(2, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}else if(title.equals("手机短号")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(3, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}else if(title.equals("办公固话")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(4, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}else if(title.equals("办公短号")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(5, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}else if(title.equals("单位")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(6, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}else if(title.equals("二级部门")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(7, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}else if(title.equals("三级部门")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(8, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}else if(title.equals("四级部门")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(9, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}else if(title.equals("五级部门")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(10, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}else if(title.equals("职位")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(11, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}else if(title.equals("办公地址")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(12, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}else if(title.equals("显示顺序")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(13, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}else if(title.equals("邮箱")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(14, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}else if(title.equals("QQ")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(15, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}else if(title.equals("微博账号")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(16, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}else if(title.equals("微信账号")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(17, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}else if(title.equals("学校")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(18, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}else if(title.equals("专业")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(19, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}else if(title.equals("年级")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(20, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}else if(title.equals("班级")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(21, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}else if(title.equals("学号")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(22, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}else if(title.equals("籍贯")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(23, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}else if(title.equals("家庭住址")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(24, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}else if(title.equals("家庭电话")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(25, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}else if(title.equals("生日")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(26, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}else if(title.equals("个性签名")){
			for (int i = 0; i < length; i++) {
				String cellTempValue = getCellValue(sheetR.getRow(i).getCell(index));
				Label lc = new Label(27, i,cellTempValue);
				sheetW.addCell(lc);
			}
		}
	}
	/**
	 * 构造模板导入的模板
	 * @param excelName
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/smart/writeTemplateWorksheet.htm")
	@ResponseBody
	public Map<String,Object> writeTemplateWorksheet(String excelName,String columns,HttpServletRequest request) throws InterruptedException{
		excelName = excelName.replace("csv", "xls").replace("txt", "xls");
		String excelFilePath = path + File.separator + excelName;
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			Document  document = XMLUtil.parseXML(columns);
			List<Node> columnNodes = (List<Node>) XMLUtil.selectNodesByXPath(document, "//column");
			File inputWorkbook = new File(excelFilePath);
			org.apache.poi.ss.usermodel.Workbook srcWorkbook =null;
			if (excelName.matches("^.+\\.(?i)(xlsx)$"))
	        {
				srcWorkbook =  new org.apache.poi.xssf.usermodel.XSSFWorkbook(new FileInputStream(inputWorkbook));
	        }else if (excelName.matches("^.+\\.(?i)(xls)$")){
	        	srcWorkbook =  new org.apache.poi.hssf.usermodel.HSSFWorkbook(new FileInputStream(inputWorkbook));
	        }
			File outputWorkbook = new File(path + File.separator+"temp" + excelName);
			WritableWorkbook workbook = Workbook.createWorkbook(outputWorkbook);
			WritableSheet  sheetW = workbook.createSheet("Sheet1", 0);
			org.apache.poi.ss.usermodel.Sheet  sheetR = srcWorkbook.getSheetAt(0);
			for(Node node : columnNodes){
				int index = Integer.parseInt(((Element)node).element("index").getTextTrim());
				String title = ((Element)node).element("title").getTextTrim();
				addCell(sheetW,sheetR,index,title);
			}
			workbook.write();
			workbook.close();
			String result = smartImport(path + File.separator+"temp" + excelName,request);
			if(!"OK".equals(result)){
				map.put("success", false);
				map.put("msg", result);
			}else{
				map.put("success", true);
				map.put("msg", "导入成功");
			}
			
		} catch (DocumentException e) {
			map.put("success", false);
			map.put("msg", "导入失败，请检查数据格式或者使用模板导入!");
			e.printStackTrace();
		}  catch (IOException e) {
			map.put("success", false);
			map.put("msg", "导入失败，请检查数据格式或者使用模板导入!");
			e.printStackTrace();
		} catch (WriteException e) {
			map.put("success", false);
			map.put("msg", "导入失败，请检查数据格式或者使用模板导入!");
			e.printStackTrace();
		}
		return map;
	}
	private String smartImport(String fileName,HttpServletRequest request) throws InterruptedException{
		Company company = this.pcCompanyService.getEntity(SecurityContextUtil.getCurrentUser().getCompanyId());
    	if(request.getSession().getAttribute("selCompany")!=null)
    	{
    		company = (Company)request.getSession().getAttribute("selCompany");
    	}
	 	try {
			Workbook excel = Workbook.getWorkbook(new File(fileName));
			Sheet sheet = null;
			try{
				sheet = excel.getSheet(0);
			}
			catch(Exception e)
			{
				return "无法读取工作表，建议新建一个excel，将内容复制进去后重新导入";
			}

			//int rows = sheet.getRows();
			int rows =getRightRows(sheet);
			//add by zhangjun 2013/11/21
			if(rows == 65536){
				return "无法读取工作表，建议新建一个excel，将内容复制进去后重新导入";
			}
			//add by zhangjun 2013/11/21
			if(rows>15000)
			{
				return "一次最多只能导入15000行，表格行数过多(" + rows + ")";
			}
			
			String[] checkText = checkUploadFileValidata(excel);
			if(StringUtils.hasText(checkText[0])){
				return checkText[0];
			}
			String companyId = company.getCompanyId();
			//部门最大顺序值
			int maxDisplayOrder = this.deptMagService.getMaxDisplayOrder(companyId);
			//员工最大顺序值
			int maxUserDisplayOrder = this.userCompanyService.getMaxDisplayOrder(companyId);
			/**mod by zhangjun 2013/11/18*/
			int poolSize = rows%300==0?rows/300:rows/300+1;
			ThreadPool threadPool = new ThreadPool(poolSize);
			/**mod by zhangjun 2013/11/18*/
			List<Cell[]> list = new ArrayList<Cell[]>();
			int threadId = 1;
			//拆分上传
			for(int i = 1;i<rows;i++)
			{
				list.add(sheet.getRow(i));
				//每300行生成一个新的线程
				if((i > 0 && i%300 == 0) || i == rows-1)
				{
					threadPool.execute(importTask(list, threadId, company,i,checkText[1],maxDisplayOrder,maxUserDisplayOrder));
					threadId += 1;
					list = new ArrayList<>();
					Thread.sleep(500);
				}
			}
			threadPool.waitFinish();
			threadPool.closePool();
			//importThread(sheet,company);
			//删除type=3的版本
			companyVersionService.delVsersion(company.getCompanyId());
			//导入完成后，如果这个企业存在缓存文件，则删除之
			CacheFileUtil.deleteCahceFiles(company.getCompanyId());
			return "OK";
			
		} catch (BiffException e) {
			e.printStackTrace();
			return "报表导入失败,请稍候重试!";
		} catch (IOException e) {
			e.printStackTrace();
			return "报表导入失败,请稍候重试!";
		}
	}
	/**
	 * 改成文件测试//线程池测试
	 * @return
	 * @throws InterruptedException
	 */
	@RequestMapping(value="/testThread.htm")
	@ResponseBody
	public String testThread() throws InterruptedException
	{
		File[] files = CacheFileUtil.getFilesByCompanyId("8a1896523c29d5ec013c29da5f0f0000");
		/*ThreadPool threadPool = new ThreadPool(3);
		Thread.sleep(500);
		for(int i = 0; i<5; i++)
		{
			threadPool.execute(createTask(i));
		}
		threadPool.waitFinish();
		threadPool.closePool();*/
		return "";
	}
	
	/**
	 * 多线程导入
	 * factRows实际的行号，用来做部门的排序
	 */
	private Runnable importTask(final List<Cell[]> cellList,final int i,final Company company,final int factRows,
			final String isDisplayOrderEmpty,final int maxDisplayOrder,final int maxUserDisplayOrder)
	{
		return new Runnable() {
			
			@Override
			public void run() {
				logger.debug("线程" + i + "==>" + cellList.size());
				importThread(cellList,company,i,factRows,isDisplayOrderEmpty,maxDisplayOrder,maxUserDisplayOrder);
			}
		};
	}

	/**
	 * 多线程 导入（重新原来的方法)
	 * @param cellList
	 * @param company
	 * @param threadId 线程编号
	 * @param factRows 实际行号
	 * @return
	 */
	private String importThread(List<Cell[]> cellList,Company company,int threadId,int factRows,
			String isDisplayOrderEmpty, int maxDisplayOrder, int maxUserDisplayOrder)
	{
		List<String> nList = new ArrayList<>();
		List<String> mList = new ArrayList<>();
		int rows = cellList.size();
		String companyId = company.getCompanyId();
		String companyName = company.getCompanyName();
		for (int r = 0; r < rows; r++){
			String parentDepartmentId="0";
			String depts = departmentEdit(companyName,cellList.get(r));
			String[] deptInfo = depts.split("[-]");
			//如果部门不存在，再向数据库进行查询
			if(mList.indexOf(depts) == -1){
				//保存部门
				mList.add(depts);
				//插入部门信息
				for(int i=1;i<deptInfo.length;i++){
					Department department = new Department();
					String dept_flag =this.deptMagService.checkDept(i, depts,companyId,parentDepartmentId); 
					//判断部门是否已存在.
					if(dept_flag == null){
						department.setDelFlag("0");
						department.setDepartmentFirstword(concatPinyin.
								changeToPinyin(deptInfo[i].substring(0, 1)));
						department.setParentDepartmentId(parentDepartmentId);
						department.setDepartmentName(deptInfo[i]);
						department.setCompany(company);
						department.setDepartmentLevel(i);
						
						//排序+10
						//int maxDisplayOrder = this.deptMagService.getMaxDisplayOrder(companyId);
						department.setDisplayOrder(maxDisplayOrder+(threadId-1)*3000+(r+1)*10+i);
						parentDepartmentId = this.deptMagService.insertEntity(department);
						this.departmentVersionService.save("0", parentDepartmentId);
					}else{
						//如果部门被删除，则还原之
						/*Department dept = this.deptMagService.getEntity(dept_flag);
						if(dept.getDelFlag().equals("1"))
						{
							dept.setDelFlag("0");
							department.setDisplayOrder(maxDisplayOrder+(threadId-1)*3000+(r+1)*10+i);
							this.deptMagService.updateEntity(dept);
							this.departmentVersionService.save("0", dept_flag);
						}*/
						//parentDepartmentId = this.deptMagService.
						//		checkDept(i, depts,companyId);
						parentDepartmentId = dept_flag;
						continue;
					}
				}
				//保存部门编号
				nList.add(parentDepartmentId);
			}
			else
			{
				parentDepartmentId = nList.get(mList.indexOf(depts)).toString();
			}
			//System.out.print("====>" + sheet.getCell(3, r).getContents());
			//插入人员信息
			//先判断名字是否重复(姓名,部门,手机号)
			List<Map<String,Object>> userlist = this.importService.getEmployeeIdByMobile(cellList.get(r)[2].getContents().replaceAll("\\s", ""));
			//String employeId = userlist.size()>0?userlist.get(0).get("employee_id").toString():"";
			//V网编号,如果为空  默认8888
			String VNumber ="8888";
			/*if(!StringUtils.hasText((VNumber))){
				VNumber = "8888";
			}*/
			//add by zhangjun 2013/11/21
			int displayOrder = 0;
			if(isDisplayOrderEmpty.equals("true")){
				displayOrder = maxUserDisplayOrder+(threadId-1)*3000+(r+1)*10;
			}else{
				displayOrder = Integer.parseInt(cellList.get(r)[13].getContents());
			}
			//add by zhangjun 2013/11/21
			//如果重复,则修改该条记录,不重复则当做新记录插入.
			if(userlist.size()>0){
				//更新
				updateEmployee(userlist,deptInfo,parentDepartmentId,cellList.get(r),VNumber,companyId,depts,displayOrder);
			}else{
				//新增
				addEmployee(cellList.get(r), companyId, deptInfo, VNumber, parentDepartmentId, depts, company,displayOrder);
			}
			OPERATE_ROW++;
		}
		return "导入成功";
	}
	/**
	 * 导入组织机构与联系人.
	 * @param importExcel 
	 * @param request 
	 * @return 
	 * 返回类型：String
	 * @throws InterruptedException 
	 */
	@RequestMapping(value="/excelImport.htm")
	@ResponseBody
	@Transactional
 	public String importExcel(@RequestParam("importExcel") MultipartFile importExcel,HttpServletRequest request) throws InterruptedException{
		String filename = importExcel.getOriginalFilename();
        String extName = filename.substring(filename.lastIndexOf(".")+1).toLowerCase();
        if(extName.matches("xls")){
			Company company = null;
        	if(request.getSession().getAttribute("selCompany")!=null)
        	{
        		company = (Company)request.getSession().getAttribute("selCompany");
        	}else{
        		company = this.pcCompanyService.getEntity(SecurityContextUtil.getCurrentUser().getCompanyId());
        	}
        	String lastFileName = System.currentTimeMillis()+extName;
        	String fileFullPath = path + File.separator + lastFileName;
        	File uploadfile = new File(fileFullPath);
        	try {
				FileCopyUtils.copy(importExcel.getBytes(),uploadfile);
			} catch (IOException e) {
				e.printStackTrace();
				return "报表导入失败,请稍候重试!" + e.getMessage();
			}
        	try {
				Workbook excel = Workbook.getWorkbook(uploadfile);
				Sheet sheet = null;
				try{
					sheet = excel.getSheet(0);
				}
				catch(Exception e)
				{
					return "无法读取工作表，建议新建一个excel，将内容复制进去后重新导入";
				}

				//int rows = sheet.getRows();
				int rows =getRightRows(sheet);
				OPERATE_ROW=0;
				TOTAL_ROW =rows-1;
				//add by zhangjun 2013/11/21
				if(rows == 65536){
					return "无法读取工作表，建议新建一个excel，将内容复制进去后重新导入";
				}
				//add by zhangjun 2013/11/21
				if(rows>15000)
				{
					return "一次最多只能导入15000行，表格行数过多(" + rows + ")";
				}
				
				String[] checkText = checkUploadFileValidata(excel);
				if(StringUtils.hasText(checkText[0])){
					return checkText[0];
				}
				String companyId = company.getCompanyId();
				//部门最大顺序值
				int maxDisplayOrder = this.deptMagService.getMaxDisplayOrder(companyId);
				//员工最大顺序值
				int maxUserDisplayOrder = this.userCompanyService.getMaxDisplayOrder(companyId);
				/**mod by zhangjun 2013/11/18*/
				int poolSize = rows%300==0?rows/300:rows/300+1;
				ThreadPool threadPool = new ThreadPool(poolSize);
				/**mod by zhangjun 2013/11/18*/
				List<Cell[]> list = new ArrayList<Cell[]>();
				int threadId = 1;
				//拆分上传
				for(int i = 1;i<rows;i++)
				{
					list.add(sheet.getRow(i));
					//每300行生成一个新的线程
					if((i > 0 && i%300 == 0) || i == rows-1)
					{
						threadPool.execute(importTask(list, threadId, company,i,checkText[1],maxDisplayOrder,maxUserDisplayOrder));
						threadId += 1;
						list = new ArrayList<>();
						Thread.sleep(500);
					}
				}
				threadPool.waitFinish();
				threadPool.closePool();
				//importThread(sheet,company);
				//删除type=3的版本
				companyVersionService.delVsersion(company.getCompanyId());
				//导入完成后，如果这个企业存在缓存文件，则删除之
				CacheFileUtil.deleteCahceFiles(company.getCompanyId());
				return "导入成功"+fileFullPath;
				
			} catch (BiffException e) {
				e.printStackTrace();
				return "报表导入失败,请稍候重试!";
			} catch (IOException e) {
				e.printStackTrace();
				return "报表导入失败,请稍候重试!";
			}
        }else{
        	return "请导入指定格式的文件!";
        }
	}
	

	//返回去掉空行的记录数
	private int getRightRows(Sheet sheet) {
		int rsCols = sheet.getColumns(); // 列数
		int rsRows = sheet.getRows(); // 行数
		int nullCellNum;
		int afterRows = rsRows;
		for (int i = 1; i < rsRows; i++) { // 统计行中为空的单元格数
			nullCellNum = 0;
			for (int j = 0; j < rsCols; j++) {
				String val = sheet.getCell(j, i).getContents().trim();
				if (!StringUtils.hasText(val)){
					nullCellNum++;
				}
			}
			if (nullCellNum >= rsCols) { // 如果nullCellNum大于或等于总的列数
				afterRows--; // 行数减一
			}
		}
		return afterRows;
	}
	

	// <editor-fold defaultstate="collapsed" desc="私有方法 ，导入用户时更新相关联表相信">
	/**
	 * 多线程导入时新增用户
	 * @param cells
	 * @param companyId
	 * @param deptInfo
	 * @param VNumber
	 * @param parentDepartmentId
	 * @param depts
	 * @param company
	 */
	private void addEmployee(Cell[] cells,String companyId,String[] deptInfo,String VNumber,String parentDepartmentId,String depts,Company company,int displayOrder)
	{
		//定义可能为空的字段
		String emailContent = "";
		String qqContent = "";
		String weiboContent = "";
		String wexinContent = "";
		String school = "";
		String major = "";
		String grade = "";
		String userClass = "";
		String studentId = "";
		String nativePlace = "";
		String homeAddress = "";
		String telephone = "";
		String birthday="";
		String mood = "";
		String headshipText = StringUtils.hasText(cells[11].getContents())?cells[11].getContents():" ";
		
		
		if(cells.length>14){//如果邮箱不为空
			emailContent = cells[14].getContents().trim();
		}
		
		if(cells.length>15) //qq
		{
			qqContent = cells[15].getContents().trim();
		}
		if(cells.length>16) //webo
		{
			weiboContent = cells[16].getContents().trim();
		}
		if(cells.length>17) //wexin
		{
			wexinContent = cells[17].getContents().trim();
		}
		if(cells.length>18) //school
		{
			school = cells[18].getContents().trim();
		}
		if(cells.length>19){major = cells[19].getContents().trim();}//major
		if(cells.length>20){grade = cells[20].getContents().trim();}//grade
		if(cells.length>21){userClass = cells[21].getContents().trim();}//userClass
		if(cells.length>22){studentId = cells[22].getContents().trim();}//studentId
		if(cells.length>23){nativePlace = cells[23].getContents().trim();} //nativePlace
		if(cells.length>24){homeAddress = cells[24].getContents().trim();}//homeAddress
		if(cells.length>25){telephone = cells[25].getContents().trim();}//telephone
		if(cells.length>26){birthday = cells[26].getContents().trim();}//birthday
		if(cells.length>27){mood = cells[27].getContents().trim();}//mood
		
		String headshipId = this.getHeadShipId(headshipText,companyId);
		Employee employee = new Employee();
		SystemUser systemUser = new SystemUser();
		employee.setEmployeeName(cells[0].getContents().replace(" ", ""));
		employee.setDepartmentName(deptInfo[1]);
		employee.setHeadshipId(headshipId);
		employee.setMobile(cells[2].getContents().replaceAll("\\s", ""));
		employee.setMobileShort(cells[3].getContents());
		employee.setTelShort(cells[5].getContents());
		employee.setTelephone2(cells[4].getContents());
		//String displayOrder = cells[13].getContents();
//		if("".equals(displayOrder)){
//			employee.setDisplayOrder(999999);
//		}else{
//			employee.setDisplayOrder(Integer.parseInt(displayOrder));
//		}
		employee.setDisplayOrder(displayOrder);
		employee.setGridNumber(VNumber);
		employee.setDelFlag("0");
		employee.setDepartmentId(parentDepartmentId);
		String employeeId = this.employeeService.insertEntity(employee);
		UserCompany userCompany = new UserCompany();
		userCompany.setEmployeeId(employeeId);
		userCompany.setCompanyId(companyId);
		userCompany.setEmployeeName(cells[0].getContents().replace(" ", "")); //组内成员名称
		userCompany.setMobile(cells[2].getContents().replaceAll("\\s", "")); //手机号码
		userCompany.setMobileShort(cells[3].getContents()); //手机短号
		userCompany.setTelephone2(cells[4].getContents()); // 办公固话
		userCompany.setTelShort(cells[5].getContents()); //办公短号
		userCompany.setUserCompany(cells[6].getContents()); //单位
		userCompany.setDepartmentName(depts); //部门
		userCompany.setHeadshipName(headshipText); //职位
		userCompany.setAddress(cells[12].getContents()); //办公地址
		userCompany.setEmail(emailContent); //邮箱
		userCompany.setQq(qqContent); //qq号码
		userCompany.setWeibo(weiboContent); //微博
		userCompany.setWeixin(wexinContent); //微信
		userCompany.setSchool(school); //学校
		userCompany.setUserMajor(major); //专业
		userCompany.setUserGrade(grade); //年级
		userCompany.setUserClass(userClass); //班级
		userCompany.setStudentId(studentId); //学号
		userCompany.setNativePlace(nativePlace); //籍贯
		userCompany.setHomeAddress(homeAddress); //家庭住址
		userCompany.setTelephone(telephone); //宅电
		userCompany.setMood(mood); //心情
		
		userCompany.setDelFlag("0");
		userCompany.setEmployeeFirstword(concatPinyin.firstwordOfName(cells[0].getContents().replace(" ", "")));
		userCompany.setEmployeeFullword(concatPinyin.changeToPinyin(cells[0].getContents().replace(" ", "")));
		//排序
		/*if (StringUtils.hasText(cells[13].getContents().trim()))
		{
			userCompany.setDisplayOrder(Integer.parseInt(cells[13].getContents().trim()));
		}*/
		userCompany.setDisplayOrder(displayOrder);
		String userCompanyId = this.userCompanyService.insertEntity(userCompany);
		//this.groupVersionService.addGroupVersion(userCompanyId, "0");
		String salt = UUIDUtil.generateUUID();
		systemUser.setCompany(company);
		systemUser.setEmployeeId(employeeId);
		systemUser.setDelFlag("0");
		systemUser.setCreateTime(new Date());
		systemUser.setPassSalt(salt);
		systemUser.setPassword(MD5Tools.encodePassword("111111", salt));
		systemUser.setRealName(cells[0].getContents().replace(" ", ""));
		systemUser.setUserName(cells[2].getContents().replaceAll("\\s", ""));
		systemUser.setMobile(cells[2].getContents().replaceAll("\\s", ""));
		this.systemUserPCService.insertEntity(systemUser);
		
		//新增用户部门的隶属关系
		addUserDept(userCompanyId, parentDepartmentId, depts, headshipId, userCompanyId,headshipText,employee.getDisplayOrder());
		this.groupVersionService.addGroupVersion(userCompanyId, "0");
	}
	
	/**
	 * 多线程更新用户
	 * @param employeId
	 * @param deptInfo
	 * @param parentDepartmentId
	 * @param cells
	 * @param VNumber
	 * @param companyId
	 * @param depts
	 */
	private void updateEmployee(List<Map<String,Object>> userlist,String[] deptInfo,String parentDepartmentId,Cell[] cells,String VNumber,String companyId,String depts,int displayOrder)
	{
		//定义可能为空的字段
		String emailContent = "";
		String qqContent = "";
		String weiboContent = "";
		String wexinContent = "";
		String school = "";
		String major = "";
		String grade = "";
		String userClass = "";
		String studentId = "";
		String nativePlace = "";
		String homeAddress = "";
		String telephone = "";
		String birthday="";
		String mood = "";
		String headshipText = StringUtils.hasText(cells[11].getContents())?cells[11].getContents():" ";
		
		String headshipId = this.getHeadShipId(headshipText,companyId);
		String employeId = "";
		boolean flag = false;//员工del_flag是否为0
		for (Map<String, Object> map : userlist) {
			employeId=map.get("employee_id").toString();
			if("0".equals(map.get("del_flag").toString())){
				flag = true;
				break;
			}
		}
		Employee employees = this.employeeService.getEntity(employeId);
		if(!flag){
			employees.setDelFlag("0");
		}
		employees.setEmployeeName(cells[0].getContents().replace(" ", ""));
		employees.setHeadshipId(headshipId);
		employees.setMobileShort(cells[3].getContents().trim());
		employees.setTelShort(cells[5].getContents().trim());
		employees.setTelephone2(cells[4].getContents().trim());
		if(cells.length>14){//如果邮箱不为空
				employees.setEmail(cells[14].getContents().trim());
		}
		
		if(cells.length>15) //qq
		{
			qqContent = cells[15].getContents().trim();
		}
		if(cells.length>16) //webo
		{
			weiboContent = cells[16].getContents().trim();
		}
		if(cells.length>17) //wexin
		{
			wexinContent = cells[17].getContents().trim();
		}
		if(cells.length>18) //school
		{
			school = cells[18].getContents().trim();
		}
		if(cells.length>19){major = cells[19].getContents().trim();}//major
		if(cells.length>20){grade = cells[20].getContents().trim();}//grade
		if(cells.length>21){userClass = cells[21].getContents().trim();}//userClass
		if(cells.length>22){studentId = cells[22].getContents().trim();}//studentId
		if(cells.length>23){nativePlace = cells[23].getContents().trim();} //nativePlace
		if(cells.length>24){homeAddress = cells[24].getContents().trim();}//homeAddress
		if(cells.length>25){telephone = cells[25].getContents().trim();}//telephone
		if(cells.length>26){birthday = cells[26].getContents().trim();}//birthday
		if(cells.length>27){mood = cells[27].getContents().trim();}//mood
		String oldVNumber = employees.getGridNumber();
		if (!StringUtils.hasText(oldVNumber))
		{
			employees.setGridNumber(VNumber);
		}
		if(true){
			this.employeeService.updateEntity(employees);
			List<UserCompany> ucList = this.userCompanyService.findByNamedParam(new String[]{"companyId","employeeId","delFlag"}, 
					new Object[]{companyId,employees.getEmployeeId(),"0"});
			if(ucList.size()>0){
				UserCompany userCompany = ucList.get(0);
				List<Map<String,Object>> udList = this.userDepartmentService.getUserDeptList(userCompany.getUserCompanyId(),headshipId, parentDepartmentId);
				if(udList.size() > 0){
					userCompany.setEmployeeName(cells[0].getContents().replace(" ", "")); //组内成员名称
					userCompany.setMobile(cells[2].getContents().replaceAll("\\s", "")); //手机号码
					userCompany.setMobileShort(cells[3].getContents()); //手机短号
					userCompany.setTelephone2(cells[4].getContents()); // 办公固话
					userCompany.setTelShort(cells[5].getContents()); //办公短号
					userCompany.setUserCompany(cells[6].getContents()); //单位
					userCompany.setDepartmentName(depts); //部门
					userCompany.setHeadshipName(headshipText); //职位
					userCompany.setAddress(cells[12].getContents()); //办公地址
					userCompany.setEmail(emailContent); //邮箱
					userCompany.setQq(qqContent); //qq号码
					userCompany.setWeibo(weiboContent); //微博
					userCompany.setWeixin(wexinContent); //微信
					userCompany.setSchool(school); //学校
					userCompany.setUserMajor(major); //专业
					userCompany.setUserGrade(grade); //年级
					userCompany.setUserClass(userClass); //班级
					userCompany.setStudentId(studentId); //学号
					userCompany.setNativePlace(nativePlace); //籍贯
					userCompany.setHomeAddress(homeAddress); //家庭住址
					userCompany.setTelephone(telephone); //宅电
					userCompany.setMood(mood); //心情
					//userCompany.setManageFlag("0");//默认为普通成员
					userCompany.setDelFlag("0");//设置为非删除状态
					userCompany.setEmployeeFirstword(concatPinyin.firstwordOfName(cells[0].getContents().replace(" ", "")));
					userCompany.setEmployeeFullword(concatPinyin.changeToPinyin(cells[0].getContents().replace(" ", "")));
					//排序
					/*if (StringUtils.hasText(cells[13].getContents().trim()))
					{
						userCompany.setDisplayOrder(Integer.parseInt(cells[13].getContents().trim()));
					}*/
					userCompany.setDisplayOrder(displayOrder);
					this.userCompanyService.updateEntity(userCompany);
					//this.groupVersionService.addGroupVersion(userCompany.getUserCompanyId(), "1");
					//新增用户、部门隶属关系
					this.updateUserDept(udList.get(0).get("user_department_id").toString(), parentDepartmentId, depts, headshipId, companyId,headshipText,userCompany.getDisplayOrder());
					this.groupVersionService.addGroupVersion(userCompany.getUserCompanyId(), "1");
				}else{
					//用户-公司信息不存在，则新增一条
					addUserCompany(cells, depts,headshipId,parentDepartmentId,companyId,employeId,displayOrder);
				}
			}
			else
			{
				//用户-公司信息不存在，则新增一条
				addUserCompany(cells, depts,headshipId,parentDepartmentId,companyId,employeId,displayOrder);
			}
			try{
			List<SystemUser> systemUsers = this.systemUserPCService.
					findByNamedParam("employeeId", employees.getEmployeeId());
				SystemUser systemUser2 = systemUsers.get(0);
				systemUser2.setUserName(cells[2].getContents().replaceAll("\\s", ""));
				systemUser2.setRealName(cells[0].getContents().replace(" ", ""));
				systemUser2.setModifyTime(new Date());
				systemUser2.setDelFlag("0");
				this.systemUserPCService.updateEntity(systemUser2);
			}
			catch(Exception e)
			{
				SystemUser systemUser2 = new SystemUser();
				systemUser2.setUserName(cells[2].getContents().replaceAll("\\s", ""));
				systemUser2.setRealName(cells[0].getContents().replace(" ", ""));
				systemUser2.setModifyTime(new Date());
				systemUser2.setDelFlag("0");
				this.systemUserPCService.insertEntity(systemUser2);
				logger.debug(e.getMessage());
			}
		}
	}
	/**
	 * 多线程：添加用户企业隶属关系
	 * @param cells
	 * @param depts
	 * @param headshipId
	 * @param parentDepartmentId
	 * @param companyId
	 * @param employeeId
	 */
	private void addUserCompany(Cell[] cells,String depts,String headshipId,String parentDepartmentId,String companyId,String employeeId,int displayOrder)
	{
		//定义可能为空的字段
		String emailContent = "";
		String qqContent = "";
		String weiboContent = "";
		String wexinContent = "";
		String school = "";
		String major = "";
		String grade = "";
		String userClass = "";
		String studentId = "";
		String nativePlace = "";
		String homeAddress = "";
		String telephone = "";
		String birthday="";
		String mood = "";
		String headshipText = StringUtils.hasText(cells[11].getContents())?cells[11].getContents():" ";
		
		
		if(cells.length>14){//如果邮箱不为空
			emailContent = cells[14].getContents().trim();
		}
		
		if(cells.length>15) //qq
		{
			qqContent = cells[15].getContents().trim();
		}
		if(cells.length>16) //webo
		{
			weiboContent = cells[16].getContents().trim();
		}
		if(cells.length>17) //wexin
		{
			wexinContent = cells[17].getContents().trim();
		}
		if(cells.length>18) //school
		{
			school = cells[18].getContents().trim();
		}
		if(cells.length>19){major = cells[19].getContents().trim();}//major
		if(cells.length>20){grade = cells[20].getContents().trim();}//grade
		if(cells.length>21){userClass = cells[21].getContents().trim();}//userClass
		if(cells.length>22){studentId = cells[22].getContents().trim();}//studentId
		if(cells.length>23){nativePlace = cells[23].getContents().trim();} //nativePlace
		if(cells.length>24){homeAddress = cells[24].getContents().trim();}//homeAddress
		if(cells.length>25){telephone = cells[25].getContents().trim();}//telephone
		if(cells.length>26){birthday = cells[26].getContents().trim();}//birthday
		if(cells.length>27){mood = cells[27].getContents().trim();}//mood
		
		UserCompany userCompany = new UserCompany();
		userCompany.setEmployeeId(employeeId);
		userCompany.setEmployeeName(cells[0].getContents().replace(" ", "")); //组内成员名称
		userCompany.setMobile(cells[2].getContents().trim()); //手机号码
		userCompany.setMobileShort(cells[3].getContents()); //手机短号
		userCompany.setTelephone2(cells[4].getContents()); // 办公固话
		userCompany.setTelShort(cells[5].getContents()); //办公短号
		userCompany.setCompanyId(companyId); //公司ID
		userCompany.setUserCompany(cells[6].getContents()); //单位
		userCompany.setDepartmentName(depts); //部门
		userCompany.setHeadshipName(headshipText); //职位
		userCompany.setAddress(cells[12].getContents()); //办公地址
		
		userCompany.setEmail(emailContent); //邮箱
		userCompany.setQq(qqContent); //qq号码
		userCompany.setWeibo(weiboContent); //微博
		userCompany.setWeixin(wexinContent); //微信
		userCompany.setSchool(school); //学校
		userCompany.setUserMajor(major); //专业
		userCompany.setUserGrade(grade); //年级
		userCompany.setUserClass(userClass); //班级
		userCompany.setStudentId(studentId); //学号
		userCompany.setNativePlace(nativePlace); //籍贯
		userCompany.setHomeAddress(homeAddress); //家庭住址
		userCompany.setTelephone(telephone); //宅电
		userCompany.setMood(mood); //心情
		
		userCompany.setManageFlag("0");//默认为普通成员
		userCompany.setDelFlag("0");//设置为非删除状态
		userCompany.setEmployeeFirstword(concatPinyin.firstwordOfName(cells[0].getContents().replace(" ", "")));
		userCompany.setEmployeeFullword(concatPinyin.changeToPinyin(cells[0].getContents().replace(" ", "")));
		//排序
		/*if (StringUtils.hasText(cells[13].getContents().trim()))
		{
			userCompany.setDisplayOrder(Integer.parseInt(cells[13].getContents().trim()));
		}*/
		userCompany.setDisplayOrder(displayOrder);
		//升级版本号
		String user_company_id = this.userCompanyService.insertEntity(userCompany);
		//this.groupVersionService.addGroupVersion(userCompany.getUserCompanyId(), "0");
		//添加用户部门信息
		addUserDept(user_company_id, parentDepartmentId, depts, headshipId, companyId,headshipText,userCompany.getDisplayOrder());
		this.groupVersionService.addGroupVersion(userCompany.getUserCompanyId(), "0");
	}
	
	/**
	 * 新增用户部门信息
	 * @param user_company_id
	 * @param parentDepartmentId
	 * @param depts
	 * @param headshipId
	 * @param companyId
	 */
	private void addUserDept(String user_company_id,String parentDepartmentId,String depts,String headshipId,String companyId,String headship_name,int displayOrder)
	{
		UserDepartment userDept = new UserDepartment();
		userDept.setDepartmentId(parentDepartmentId);
		userDept.setDepartmentPath(depts);
		userDept.setHeadshipId(headshipId);
		userDept.setTaxis(displayOrder);
		userDept.setUserCompanyId(user_company_id);
		userDept.setVisibleFlag("1");
		userDept.setHeadshipName(headship_name);
		this.userDepartmentService.insertEntity(userDept);
	}
	/**
	 * 更新用户部门信息
	 * @param user_company_id
	 * @param parentDepartmentId
	 * @param depts
	 * @param headshipId
	 * @param companyId
	 */
	private void updateUserDept(String user_department_id,String parentDepartmentId,String depts,String headshipId,String companyId,String headship_name,int displayOrder)
	{

		//更改排序
		UserDepartment userDept = this.userDepartmentService.getEntity(user_department_id);
		userDept.setTaxis(displayOrder);
		userDept.setHeadshipName(headship_name);
		userDept.setHeadshipId(headshipId);
		this.userDepartmentService.updateEntity(userDept);
		this.userDepartmentService.getHibernateBaseDao().flush();
		logger.debug("用户部门记录已经存在，更改排序");
	}
	// </editor-fold>
	
	/**
	 * 检测上传Excel的有效性
	 * @param excel
	 * @return
	 */
	private String[] checkUploadFileValidata(Workbook excel)
	{
		Sheet sheet = excel.getSheet(0);
		//int rows = sheet.getRows();
		int rows =getRightRows(sheet);
		String checkText = "";
		String isDisplayOrderEmpty = "";
		for (int r = 1; r < rows; r++){
			String checkName = sheet.getCell(0,r).getContents().trim();
			//姓名不能为空
			if(!StringUtils.hasText(checkName))
			{
				checkText += "第" + (r+1) + "行姓名为空，导入失败<br />";
			}
			//电话号码不能为空
			String checkMobile= sheet.getCell(2, r).getContents().trim();
			//电话号码必须为数字
			boolean b = Pattern.matches("^[0-9]*$", checkMobile);
			
			if(checkMobile.length()>12 || "".equals(checkMobile) || !b){
				checkText += "第"+(r+1)+"行电话号码有误,导入失败<br />";
			}
			//二级部门不能为空
			String department = sheet.getCell(7,r).getContents().trim();
			if(!StringUtils.hasText(department))
			{
				checkText += "第" + (r+1) + "行一级部门为空，导入失败<br />";
			}
			//职位不能为空
			/*if(!StringUtils.hasLength(sheet.getCell(11,r).getContents()))
			{
				checkText += "第" + (r+1)  + "行职位不能为空";
			}*/
			String displayOrder = sheet.getCell(13,r).getContents().trim();
			if(!StringUtils.hasText(displayOrder))
			{
				isDisplayOrderEmpty = "true";
			}
			//排序不能为空
//			if(!StringUtils.hasText(sheet.getCell(13, r).getContents()))
//			{
//				checkText += "第" + (r+1) + "行排序不能为空";
//			}
		}
		return new String[]{checkText,isDisplayOrderEmpty};
	}
	
	/**
	 * 部门处理。原模板用-分隔，再改成按单元格进行分割
	 * 现在的处理方法是把部门再拼成按[-]分隔，送回处理
	 * @param sheet excel的sheet对象
	 * @return 拼接好的部门字段
	 */
	private String departmentEdit(Sheet sheet,int rows)
	{
		String dept1 = sheet.getCell(6,rows).getContents().trim();
		String dept2 = sheet.getCell(7,rows).getContents().trim();
		String dept3 = sheet.getCell(8,rows).getContents().trim();
		String dept4 = sheet.getCell(9,rows).getContents().trim();
		String dept5 = sheet.getCell(10,rows).getContents().trim();
		String ret = dept1 + "-" + dept2;
		if(StringUtils.hasText(dept3))
		{
			ret += "-" + dept3;
		}
		if(StringUtils.hasText(dept4))
		{
			ret += "-" + dept4;
		}
		if(StringUtils.hasText(dept5))
		{
			ret += "-" + dept5;
		}
		return ret;
	}
	
	/**
	 * 部门处理，用-分隔
	 * @param cells
	 * @return
	 */
	private String departmentEdit(String companyName,Cell[] cells)
	{
		//String dept1 = cells[6].getContents().trim();
		String dept2 = cells[7].getContents().trim();
		String dept3 = cells[8].getContents().trim();
		String dept4 = cells[9].getContents().trim();
		String dept5 = cells[10].getContents().trim();
		String ret = companyName.replaceAll("-", "").replaceAll("\\s", "") + "-" + dept2.replaceAll("-", "").replaceAll("\\s", "");
		if(StringUtils.hasText(dept3))
		{
			ret += "-" + dept3.replaceAll("-", "").replaceAll("\\s", "");
		}
		if(StringUtils.hasText(dept4))
		{
			ret += "-" + dept4.replaceAll("-", "").replaceAll("\\s", "");
		}
		if(StringUtils.hasText(dept5))
		{
			ret += "-" + dept5.replaceAll("-", "").replaceAll("\\s", "");
		}
		return ret;
	}
	
	/**
	 * 判断员工名称是否已存在.
	 * @param employeeName 
	 * @param departmentId 
	 * @param mobile 
	 * @return 
	 * 返回类型：int
	 */
	public int checkEmployee(String employeeName, String departmentId, String mobile){
		List<Employee> employees = this.employeeService.findByNamedParam
				(new String[]{"employeeName","departmentId","mobile"}, new Object[]{employeeName,departmentId,mobile});
		if(employees.size()>0){
			return employees.size();
		}else{
			return 0;
		}
	}
	
	/**
	 * 通过员工职位名称获取职位编码.
	 * @param headShipName 职务名称
	 * @param companyId 所属公司
	 * @return 
	 * 返回类型：String
	 */
	public String getHeadShipId(String headShipName,String companyId){
		return this.importService.getHeadShipCodeByName(headShipName,companyId);
	}
	
	/**
	 * 导出人员信息.
	 * @param response 
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping(value="/export.htm")
	@ResponseBody
	public Map<String, Object> excelExport(HttpServletResponse response,HttpServletRequest request){
		//User user = SecurityContextUtil.getCurrentUser();
		//String employeeId = user.getEmployeeId();
		//if(StringUtils.hasText(employeeId)){
			//Employee employee = this.employeeService.getEntity(employeeId);
			//Department department = this.deptMagService.getEntity(employee.getDepartmentId());
			//department.getDepartmentLevel();
			//department.getDepartmentId();
			//this.rightconfigService.rights();
		//}
		String[] headers = {"姓名","性别","主要号码","手机短号","办公固话","办公短号","单位","二级部门","三级部门","四级部门","五级部门","职位","办公地址","显示顺序","邮箱","QQ","微博账号","微信账号","学校","专业","年级","班级","学号","籍贯","家庭住址","家庭电话","生日","个性签名"};
		String title="item";//报表名称
		String pattern = "yyyy-MM-dd hh:mm:ss";
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String companyId = "";
			if(request.getSession().getAttribute("selCompany")!=null)
			{
				companyId = ((Company)request.getSession().getAttribute("selCompany")).getCompanyId();
				System.out.println("$$$$"+companyId);
			}
			else
			{
				companyId = SecurityContextUtil.getCompanyId();
				System.out.println("####"+companyId);
			}
			OutputStream out = response.getOutputStream();
			response.reset();// 清空输出流  
			//// 设定输出文件头  
			response.setHeader("Content-disposition", "attachment; filename=" + title + ".xls");
			response.setContentType("application/ms-excel;charset=UTF-8");// 定义输出类型
			List<UserCompanyDto> list = this.importService.getUsersList(companyId);
			ExportExcel<UserCompanyDto> exportExcel = new ExportExcel<UserCompanyDto>();
			exportExcel.exportExcel("组织机构和联系人", headers, list, out, pattern);
			result.put("msg", "报表导出成功");
		} catch (IOException e) {
			e.printStackTrace();
			result.put("msg", "报表导出失败,请稍候重试!");
		}
		return result;
	}
	
	/**
	 * 辅助生成手机号码.
	 * @return 
	 * 返回类型：String
	 */
	public String moblie(){
		int number = (int)((Math.random()+1)*100000000);
		String code = ""+number;
		code = code.substring(1, 9);
		return "139" + code;
	}
	/**
	 * 返回当前进度.
	 * @return 
	 * 返回类型：double
	 */
	@RequestMapping(value="/getOperateRate.htm")
	@ResponseBody
	public double getOperateRate(){
		return TOTAL_ROW==0?0:OPERATE_ROW*100/TOTAL_ROW;
	}
}

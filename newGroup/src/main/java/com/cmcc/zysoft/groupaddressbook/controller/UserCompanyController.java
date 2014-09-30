// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.zysoft.HttpClientUtil.HttpClientUtil;
import com.cmcc.zysoft.groupaddressbook.service.PCCompanyService;
import com.cmcc.zysoft.groupaddressbook.service.PCEmployeeService;
import com.cmcc.zysoft.groupaddressbook.service.UserCompanyService;
import com.cmcc.zysoft.groupaddressbook.util.CacheFileUtil;
import com.cmcc.zysoft.groupaddressbook.util.ThreadPool;
import com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADCSoap.MobileShortICTClient;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.Employee;
import com.cmcc.zysoft.sellmanager.model.Headship;
import com.cmcc.zysoft.sellmanager.model.Role;
import com.cmcc.zysoft.sellmanager.model.UserCompany;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.cmcc.zysoft.sysmanage.service.HeadshipService;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：UserCompanyController
 * <br />版本:1.0.0
 * <br />日期： 2013-5-21 上午9:27:53
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Controller
@RequestMapping("/pc/userCompany")
public class UserCompanyController extends BaseController{
	
	@Resource
	private UserCompanyService userCompanyService;
	
	@Resource
	private PCEmployeeService pcEmployeeService;
	
	@Resource
	private HeadshipService headshipService;
	
	@Resource
	private PCCompanyService pcCompanyService;
	
	@Value("${upload.file.path}")
	private String path;
	private final  String URL = "http://10.152.89.207:8080/short/getMobileShort";
	private final  String URL1 = "http://10.152.89.207:8080/short/getMobileShorts";
	/**
	 * 导入个人通讯录.
	 * @param importExcel
	 * @param companyId
	 * @param request
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping(value="/import.htm")
	@Transactional
	@ResponseBody
	public String importUser(@RequestParam("importExcel2") MultipartFile importExcel,
			String companyId,HttpServletRequest request){
		User user = SecurityContextUtil.getCurrentUser();
		String employeeId = user.getEmployeeId();
		Employee employee = this.pcEmployeeService.getEntity(employeeId);
		boolean self_flag = false;// 用来判断excel里当前行的号码与登录人的号码是否一致的标志
		Date date = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = sd.format(date);
		String filename = importExcel.getOriginalFilename();
        String extName = filename.substring(filename.lastIndexOf(".")+1).toLowerCase();
        if(extName.matches("xls")){
        	String lastFileName = nowDate+ "_xjtxl_" +extName;
        	String fileFullPath = path + File.separator + lastFileName;
        	File uploadfile = new File(fileFullPath);
        	try {
				FileCopyUtils.copy(importExcel.getBytes(),uploadfile);
				Workbook excel;
				excel = Workbook.getWorkbook(uploadfile);
				Sheet sheet = excel.getSheet(0);
				int rows = sheet.getRows();
				for (int r = 1; r < rows; r++){
					String mobile = sheet.getCell(2, r).getContents();
					List<UserCompany> list = this.userCompanyService.
							findByNamedParam(new String[]{"companyId","mobile","delFlag"}, new Object[]{companyId,mobile,"0"});
					if(mobile.equals(employee.getMobile())){
						self_flag = true;//如果当前行的号码与登录人号码一致,则将标志位状态改变;导入完成后,若标志位为false则将当前登录人信息插入分组表.
					}
					if(list.size()>0){
						continue;//判断该分组里面是否已存在该手机号码,若存在,即为重复导入,跳过该条记录
					}
					UserCompany userCompany = new UserCompany();
					userCompany.setCompanyId(companyId);
					userCompany.setEmployeeName(sheet.getCell(0, r).getContents()); //组内成员名称
					userCompany.setMobile(sheet.getCell(2, r).getContents()); //手机号码
					userCompany.setMobileShort(sheet.getCell(3, r).getContents()); //手机短号
					userCompany.setTelephone2(sheet.getCell(4, r).getContents()); // 办公固话
					userCompany.setTelShort(sheet.getCell(5, r).getContents()); //办公短号
					userCompany.setUserCompany(sheet.getCell(7, r).getContents()); //单位
					userCompany.setDepartmentName(sheet.getCell(8, r).getContents()); //部门
					userCompany.setHeadshipName(sheet.getCell(9, r).getContents()); //职位
					userCompany.setAddress(sheet.getCell(10, r).getContents()); //办公地址
					userCompany.setEmail(sheet.getCell(12, r).getContents()); //邮箱
					userCompany.setQq(sheet.getCell(13, r).getContents()); //qq号码
					userCompany.setWeibo(sheet.getCell(14, r).getContents()); //微博
					userCompany.setWeixin(sheet.getCell(15, r).getContents()); //微信
					userCompany.setSchool(sheet.getCell(16, r).getContents()); //学校
					userCompany.setUserMajor(sheet.getCell(17, r).getContents()); //专业
					userCompany.setUserGrade(sheet.getCell(18, r).getContents()); //年级
					userCompany.setUserClass(sheet.getCell(19, r).getContents()); //班级
					userCompany.setStudentId(sheet.getCell(20, r).getContents()); //学号
					userCompany.setNativePlace(sheet.getCell(21, r).getContents()); //籍贯
					userCompany.setHomeAddress(sheet.getCell(23, r).getContents()); //家庭住址
					userCompany.setTelephone(sheet.getCell(24, r).getContents()); //宅电
					userCompany.setMood(sheet.getCell(26, r).getContents()); //心情
					userCompany.setDelFlag("0");
					if(self_flag){
						userCompany.setManageFlag("1");//如果当前行为导入者,将其设为管理员.
					}else{
						userCompany.setManageFlag("0");//默认为普通成员
					}
					this.userCompanyService.add(userCompany,"","","");
				}
				if(!self_flag){
					Headship headship = this.headshipService.getEntity(employee.getHeadshipId());
					String headshipName = "";
					if(headship != null){
						headshipName = headship.getHeadshipName();
					}
					this.userCompanyService.addSelf(employee, companyId,"",headshipName,"1");
				}
				return "导入成功";
			} catch (IOException e) {
				e.printStackTrace();
				return "报表导入失败,请稍候重试!";
			} catch (BiffException e) {
				e.printStackTrace();
				return "报表导入失败,请稍候重试!";
			}
        }else{
        	return "请导入指定格式的文件!";
        }
	}
	
	/**
	 * 新增或修改群组成员信息.
	 * @param userCompany
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping(value="/addOrUpdate.htm")
	@ResponseBody
	@Transactional
	public String addUserCompany(UserCompany userCompany,String departmentId,String headshipId,String headshipName,String gridNumber,String userCompanyIdAndDepartmentId){
		if(!StringUtils.hasText(String.valueOf(userCompany.getDisplayOrder())))
		{
			userCompany.setDisplayOrder(9999);
		}
		/*Map<String,String> map = MobileShortICTClient.getMobileShortAndVId(userCompany.getMobile());
		if(null != map && !map.isEmpty()){
			userCompany.setMobileShort(map.get("ShortNum"));
			gridNumber = map.get("GroupCode");
		}*/
		/*try {
			String result = HttpClientUtil.postForShort(URL,userCompany.getMobile());
			@SuppressWarnings("unchecked")
			Map<String,String> map = (Map<String,String>)JSONObject.parse(result);
			if(null != map && !map.isEmpty()){
				userCompany.setMobileShort(map.get("ShortNum"));
				gridNumber = map.get("GroupCode");
			}
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		CacheFileUtil.deleteCahceFiles(userCompany.getCompanyId());
		String idResult = userCompanyService.saveHeadship(userCompany.getCompanyId(), headshipName);
		userCompany.setMobile(userCompany.getMobile().trim());//防止用户输入首末存在空格
		if(StringUtils.hasText(userCompany.getUserCompanyId())){
			return this.userCompanyService.update(userCompany, departmentId, idResult,gridNumber,userCompanyIdAndDepartmentId);
		}else{
			return this.userCompanyService.add(userCompany, departmentId, idResult,gridNumber);
		}
	}
	/**
	 * 保存排序.
	 * @param 'firstMove' :二级部门移动元素,'secondMove' :三级部门移动元素,
	 *'thirdMove' :四级部门移动元素,'fourthMove' :五级部门移动元素,
	 *'lastOrder1' :二级部门最终排序,'lastOrder2' :三级部门最终排序,
	 *'lastOrder3' :四级部门最终排序,'lastOrder4' :五级部门最终排序
	 *				
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping(value="/saveOrder.htm")
	@ResponseBody
	@Transactional
	public String saveOrder(@RequestBody HashMap<String,Object> map,HttpServletRequest request){
		try {
			Company company = this.pcCompanyService.getEntity(SecurityContextUtil.getCurrentUser().getCompanyId());
	    	if(request.getSession().getAttribute("selCompany")!=null)
	    	{
	    		company = (Company)request.getSession().getAttribute("selCompany");
	    	}
	    	String companyName = company.getCompanyName();
			List<String> firstMove = (ArrayList<String>)map.get("firstMove");
			List<String> secondMove = (ArrayList<String>)map.get("secondMove");
			List<String> thirdMove = (ArrayList<String>)map.get("thirdMove");
			List<String> fourthMove = (ArrayList<String>)map.get("fourthMove");
			List<String> lastOrder1 = (ArrayList<String>)map.get("lastOrder1");
			List<String> lastOrder2 = (ArrayList<String>)map.get("lastOrder2");
			List<String> lastOrder3 = (ArrayList<String>)map.get("lastOrder3");
			List<String> lastOrder4 = (ArrayList<String>)map.get("lastOrder4");
			String firstDeptId = map.get("firstDept").toString();
			String secondDeptId = map.get("secondDept").toString();
			String thirdDeptId = map.get("thirdDept").toString();
			String fourthDeptId = map.get("fourthDept").toString();
			if(!firstMove.equals(lastOrder1)){
				for(int i=0;i<lastOrder1.size();i++){
					this.userCompanyService.updateDisplayOrderById(companyName,firstDeptId,lastOrder1.get(i), (i+1)*10);
				}
			}
			if(!secondMove.equals(lastOrder2)){
				for(int i=0;i<lastOrder2.size();i++){
					this.userCompanyService.updateDisplayOrderById(companyName,secondDeptId,lastOrder2.get(i), (i+1)*10);
				}
			}
			if(!thirdMove.equals(lastOrder3)){
				for(int i=0;i<lastOrder3.size();i++){
					this.userCompanyService.updateDisplayOrderById(companyName,thirdDeptId,lastOrder3.get(i), (i+1)*10);
				}
			}
			if(!fourthMove.equals(lastOrder4)){
				for(int i=0;i<lastOrder4.size();i++){
					this.userCompanyService.updateDisplayOrderById(companyName,fourthDeptId,lastOrder4.get(i), (i+1)*10);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "FAUILE";
		}
		
		return "SUCCESS";
	}
	/**
	 * 新增或修改前检查是否有权限,仅创建人和管理员能修改.
	 * @param companyId
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping(value="beforeAdd.htm")
	@ResponseBody
	public String beforeAdd(String companyId){
		User user = SecurityContextUtil.getCurrentUser();
		Company company = this.pcCompanyService.getEntity(companyId);
		if(user.getUsername().equals("s_admin"))
		{
			return "YES";
		}
		
		List<Role> roles = user.getRoles();
		for(Role role : roles)
		{
			if(role.getRoleId().equals("1"))
			{
				return "YES";
			}
		}
		
		List<UserCompany> list = this.userCompanyService.findByNamedParam(new String[]{"companyId","employeeId","delFlag"}, 
				new Object[]{companyId,user.getEmployeeId(),"0"});
		if(list.size() == 0){
			return "NO";
		}else{
			UserCompany userCompany = list.get(0);
			if("1".equals(userCompany.getManageFlag()) || "2".equals(userCompany.getManageFlag()) 
					|| "3".equals(userCompany.getManageFlag())){
				return "YES";
			}else{
				
				return "NO";
			}
		}
	}
	
	/**
	 * 删除个人通讯录的成员.
	 * @param userCompanyId
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping(value="delete.htm")
	@ResponseBody
	public String deleteUserCompany(String userCompanyId,String departmentId){
		UserCompany uc = this.userCompanyService.getEntity(userCompanyId);
		this.userCompanyService.deleteUserCompany(userCompanyId,departmentId);
		if(uc!=null){
			CacheFileUtil.deleteCahceFiles(uc.getCompanyId());
		}
		return "SUCCESS";
	}
	
	/**
	 * 企业管理员设置
	 * @param userCompanyId
	 * @return
	 */
	@RequestMapping(value="manageEdit.htm")
	@ResponseBody
	@Transactional
	public String manageEdit(String userCompanyId,String managerType,String managerDept)
	{
		return this.userCompanyService.manageEdit(userCompanyId,managerType,managerDept)?"SUCCESS":"ERROR";
	}
	

	/**
	 * 获取短号信息.
	 * @return map
	 * {"response":{"bizCode":"SI905","transID":"SI20131213000000000001","actionCode":2,"timeStamp":"201312131527530701","testFlag":0,"dealkind":1,"priority":1,"version":"V1.0","resultCode":"0","resultMsg":"success","svcCont":"<ShortNumResponse><OprResult><BizRegRsp><OrdSeq>20131213095050001</OrdSeq><Rslt>00</Rslt><RealInfo><PhoneNo>15156892727</PhoneNo><ShortNum>662727</ShortNum><GroupCode>5510000139</GroupCode></RealInfo></BizRegRsp></OprResult></ShortNumResponse>","siappID":"1"},"success":true}
	 * @throws HttpException 
	 */
	@RequestMapping("/getShort.htm")
	public void getShort(List<String> ucList) throws HttpException{
		logger.debug("##########  getShort  开始!  ##########");
		//List<String> mobileList =new ArrayList<String>();
		String mobiles="";
		int size = ucList.size();
		for(int i = 0 ;i <size ; i++){
			//mobileList.add(ucList.get(i));
			mobiles += ucList.get(i)+",";
	       	 if((i > 0 && (i+1)%5 == 0) || i == size-1){
	       		String flag = mobiles.substring(0, mobiles.length()-1);
				//List<Map<String,String>> list = MobileShortICTClient.getMobileShortAndVId(mobileList);
				String result = HttpClientUtil.postForShort(URL1,flag);
				JSONArray list = JSONObject.parseArray(result);
				if(null != list){
					for (int j=0;j<list.size();j++) {
						JSONObject obj =list.getJSONObject(j);
						if(null != obj && !obj.isEmpty()){
							this.userCompanyService.updateShortNumVIdByMobile(obj.get("PhoneNo").toString(),obj.get("ShortNum").toString(),obj.get("GroupCode").toString());
						}
					}
				}
				//mobileList = new ArrayList<String>();
				mobiles ="";
	       	 }
		}
		logger.debug("##########  getShort  结束!  ##########");
	}
	/**
	 * 多线程导入
	 * factRows实际的行号，用来做部门的排序
	 */
	private Runnable importTask(final List<String> mobileList)
	{
		return new Runnable() {
			
			@Override
			public void run() {
				try {
					getShort(mobileList);
				} catch (HttpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}
	@RequestMapping("/autoUpdateShortNum.htm")
	@ResponseBody
	public void autoUpdateShortNum(HttpServletRequest request,String path) {
		File uploadfile = new File(path);
		Workbook excel;
		try {
			excel = Workbook.getWorkbook(uploadfile);
			Sheet sheet = excel.getSheet(0);
			int rows = sheet.getRows();
			int poolSize = rows%100==0?rows/100:rows/100+1;
			ThreadPool threadPool = new ThreadPool(poolSize);
			List<String> list = new ArrayList<String>();
			//拆分上传
			for(int i = 1;i<rows;i++)
			{
				String phone = sheet.getRow(i)[2].getContents().trim();
				if (StringUtils.hasText(phone)){
					list.add(phone);//手机号
				}
				//每5行生成一个新的线程
				if((i > 0 && i%100 == 0) || i == rows-1)
				{
					threadPool.execute(importTask(list));
					list = new ArrayList<>();
				}
			}
			threadPool.waitFinish();
			threadPool.closePool();
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@RequestMapping("/getShortTest.htm")
	@ResponseBody
	public List<Map<String, String>> getShortTest(String[] mobile) {
		logger.debug("##########  getShort  开始!  ##########");
		List<String> mobileList = Arrays.asList(mobile);
		List<Map<String,String>> list = MobileShortICTClient.getMobileShortAndVId(mobileList);
		return list;
	}

}

/**
 * CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */

package com.cmcc.zysoft.groupaddressbook.mobile.controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MDepartmentReportService;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MEmployeeParamService;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MEmployeeRecordService;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MEmployeeReportService;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MEmployeeService;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MMedalService;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MovementService;
import com.cmcc.zysoft.groupaddressbook.mobile.service.RecordGpsService;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.EmployeeRecord;
import com.cmcc.zysoft.sellmanager.model.Movement;
import com.cmcc.zysoft.sellmanager.model.RecordGps;

/**
 * @author 袁凤建
 * @email yuan.fengjian@ustcinfo.com
 * @date 2013-6-8 下午12:58:44
 */

@Controller
@RequestMapping("mobile/pedometer")
public class MPedometerController extends BaseController {
	
	@Value("${upload.file.path}")
	private String path;
	
	@Resource
	private MEmployeeParamService mEmployeeParamService;
	
	@Resource
	private MMedalService mMedalService;
	
	@Resource
	private MEmployeeReportService mEmployeeReportService;
	
	@Resource 
	private MEmployeeRecordService mEmployeeRecordService;
	
	@Resource
	private MDepartmentReportService mDepartmentReportService;
	
	@Resource
	private MEmployeeService mEmployeeService;
	
	@Resource
	private RecordGpsService recordGpsService;
	
	@Resource
	private MovementService movementService;
	
	/**
	 * 获取用户的身高体重
	 * @param usercode
	 * @param token
	 * @return
	 */
	@RequestMapping("/getHeightAndWeight")
	@ResponseBody
	public Map<String, Object> getHeightAndWeight(String usercode, String token) {
		return this.mEmployeeParamService.getHeightAndWeight(usercode, token);
	}
	
	/**
	 * 用户身高、体重信息上传(身高、体重、步长必须有一个不为空)
	 * @param usercode：用户的employee_id
	 * @param token：令牌
	 * @param height：身高
	 * @param width：体重
	 * @param step：步长
	 * @return
	 */
	@RequestMapping("/updateUserParam")
	@ResponseBody
	public Map<String,Object> updateHeightAndWeight(String usercode,String token,String height,String weight,String step)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		if(!StringUtils.hasText("usercode") || 
				(!StringUtils.hasText(height) && !StringUtils.hasText(weight) && !StringUtils.hasText(step)))
		{
			map.put("code", "81");
		}
		else{
			if(this.mEmployeeParamService.updateUserHeightAndWeight(usercode, height, weight, step))
			{
				map.put("code", "0");
			}
			else
			{
				map.put("code", "-1");
			}
		}
		return map;
	}
	
	/**
	 * 获取单个用户的统计信息
	 * @param userCode
	 * @param token
	 * @return
	 */
	@RequestMapping("/getMyRecordReport")
	@ResponseBody
	public Map<String,Object> getMyRecordReport(String userCode,String token)
	{
		String movement_id = this.getMovementId(userCode);
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> list = this.mEmployeeReportService.getMyRecordReport(userCode,movement_id);
		if(list.size()>0)
		{
			map.put("code", "0");
			map.put("value", list.get(0));
		}
		else
		{
			map.put("code", "50");
		}
		return map;
	}
	
	/**
	 * 获取用户的勋章
	 * @param usercode
	 * @param token
	 * @param movement_id:活动id，可以为空
	 * @return
	 */
	@RequestMapping("/getMedals")
	@ResponseBody
	public Map<String, Object> getEmpMedals(String usercode, String token, String movement_id) {
		if(!StringUtils.hasText(movement_id))
		{
			movement_id = this.getMovementId(usercode);
		}
		Map<String,Object> map = this.mMedalService.getEmpMedals(usercode, token, movement_id);
		List<Map<String,Object>> list = this.mEmployeeReportService.getMyRecordReport(usercode,movement_id);
		map.put("record", list);
		return map;
	}
	
	
	/**
	 * 获取某个活动的勋章，以zip格式下载至客户端
	 * @param movement_id
	 * @return
	 */
	@RequestMapping("/getMedalsByMovement")
	@ResponseBody
	public Map<String,Object> getMedalByMovement(String movement_id)
	{
		List<Map<String,Object>> list = this.mMedalService.getMedalByMovement(movement_id);
		Map<String,Object> map = new Hashtable<String,Object>();
		map.put("code", "0");
		map.put("value", list);
		map.put("medalSys", this.mMedalService.getMedalSysByMovement(movement_id));
		return map;
	}
	
	
	
	/**
	 * 获取用户的排名
	 * @param usercode
	 * @param token
	 * @return
	 */
	@RequestMapping("/getMyOrder")
	@ResponseBody
	public Map<String, Object> getEmpOrder(String usercode, String token) {
		String movement_id = this.getMovementId(usercode);
		return this.mEmployeeReportService.getEmpOrder(usercode, token, movement_id);
	}
	
	/**
	 * 获取排名前十的用户列表
	 * @param usercode
	 * @param token
	 * @return
	 */
	@RequestMapping("/getTopTen")
	@ResponseBody
	public Map<String, Object> getTopTen(String usercode, String token) {
		String movement_id = this.getMovementId(usercode);
		//获取我的步数和排名
		Map<String,Object> map = this.mEmployeeReportService.getEmpOrder(usercode, token, movement_id);
		String order = map.get("order").toString();
		String steps = map.get("steps").toString();
		//获取与我排名相近的5个人
		//List<Map<String,Object>> list = this.mEmployeeReportService.getOrderList(Integer.parseInt(order), movement_id);
		//@zhouyu 2013.12.3，修改为获取与我相近的10个人
		List<Map<String,Object>> list = this.mEmployeeReportService.getOrderListNearestTen(Integer.parseInt(order), movement_id);
		//获取排名前10
		map = this.mEmployeeReportService.getTopTen(usercode, token, movement_id);
		map.put("order", order);
		map.put("steps", steps);
		map.put("list", list);
		return map;
	}
	
	/**
	 * 为了保持向下兼容，新建一个方法
	 * @param usercode
	 * @param token
	 * @param movement_id，可以为空
	 * @return
	 */
	@RequestMapping("/getTopTenV2")
	@ResponseBody
	public Map<String,Object> getTopTenV2(String usercode,String token,String movement_id)
	{
		if(!StringUtils.hasText(movement_id)){
			movement_id = this.getMovementId(usercode);
		}
		//获取我的步数和排名
		Map<String,Object> map = this.mEmployeeReportService.getEmpOrder(usercode, token, movement_id);
		String order = map.get("order").toString();
		String steps = map.get("steps").toString();
		//所有排名
		map = this.mEmployeeReportService.getTopTenV2(usercode, token, movement_id);
		map.put("order", order);
		map.put("steps", steps);
		return map;
	}
	
	/**
	 * 测试所有上传数据是否作弊（验证）
	 * @return
	 */
	@RequestMapping("/getAllSportRecord")
	@ResponseBody
	public Map<String,Object> getAllSportRecord()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		List<EmployeeRecord> list = this.mEmployeeRecordService.getAllSportRecord();
		map.put("value", list);
		return map;
	}
	
	/**
	 * 上传用户记录
	 * @param usercode 当前用户employee_id
	 * @param uploadStr 上传的json串，详见接口文档
	 * @param token 令牌
	 * @return
	 */
	@RequestMapping("/uploadUserRecord")
	@ResponseBody
	public Map<String,Object> uploadUserRecord(String usercode,String uploadStr,String token)
	{
		return this.uploadRecord(usercode, uploadStr, token);
	}
	
	/**
	 * 上传用户的运动记录
	 * @param usercode
	 * @param token
	 * @return
	 */
	//@Transactional
	public Map<String, Object> uploadRecord(String usercode,String uploadStr, String token) {
		final String movement_id = getMovementId(usercode);
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> medalList = new ArrayList<Map<String,Object>>();
		try{
			//添加记录至历史表中
			final List<EmployeeRecord> list = mEmployeeRecordService.uploadRecord(usercode, token, uploadStr, movement_id);
			if(list == null)
			{
				map.put("code", "-1");
				map.put("msg", "没有参加相应的健步活动，不能上报数据");
			}
			else{
				//更新统计表信息
				medalList = this.mEmployeeReportService.updateUserReport(usercode, list);
				//更新部门统计表
				//this.mDepartmentReportService.updateDepartmentReport(usercode, list);
				ArrayList<String> arr = new ArrayList<String>();
				//同步将用户记录recordId返回，供GPS上传时使用
				for(EmployeeRecord empRec : list)
				{
					arr.add(empRec.getEmployeeRecordId());
				}
				map.put("employeeRecordIds", arr);
				map.put("code", "0");
				//异步同步数据至赛季
				TimerTask timeTask = new TimerTask() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						updateParentMovement(list,movement_id);
					}
				};
				java.util.Timer timer = new Timer();
				timer.schedule(timeTask, Long.parseLong("5000"));
			}
		}
		catch(Exception e)
		{
			map.put("code", "-1");
			map.put("msg", e.getMessage());
		}
		if(medalList != null)
		{
			map.put("medals", medalList);
		}
		else
		{
			map.put("medals", "");
		}
		//返回
		return map;
	}
	
	/**
	 * 异步同步用户健步记录至赛季中
	 */
	private void updateParentMovement(List<EmployeeRecord> list,String movement_id)
	{
		List<EmployeeRecord> elist = new ArrayList<EmployeeRecord>();
		Movement movement = this.movementService.getEntity(movement_id);
		if(StringUtils.hasText(movement.getParentMovementId())){
			for(EmployeeRecord record : list)
			{
				record.setMovementId(movement.getParentMovementId());
				elist.add(record);
			}
			//更新统计报表
			this.mEmployeeReportService.updateUserReport(list.get(0).getEmployeeId(), elist);
		}
	}
	
	/**
	 * 健步GPS
	 * @param userCode
	 * @param uploadStr
	 * @return
	 */
	@RequestMapping("/uploadGpsData")
	@ResponseBody
	public Map<String,Object> uploadGpsData(String userCode,final String uploadStr)
	{
		Map<String,Object> map = new Hashtable<String,Object>();
		
		java.util.Timer timer = new Timer();
		//以deamon方式运行，程序结束timer也结束
		
		TimerTask timeTask = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				insertGPSDataToDB(uploadStr);
			}
		};
		
		timer.schedule(timeTask, Long.parseLong("5000"));
		
		map.put("code", "0");
		map.put("msg", "添加成功");
		return map;
	}
	
	/**
	 * 将gps数据入库
	 * @param uploadStr
	 */
	private void insertGPSDataToDB(String uploadStr)
	{
		System.out.print("开始将GPS信息入库");
		try
		{
			List<RecordGps> list = JSON.parseArray(uploadStr, RecordGps.class);
			for(RecordGps rg : list)
			{
				Date date = new Date();
				rg.setAddDate(date);
				this.recordGpsService.insertEntity(rg);
			}
		}
		catch(Exception e)
		{
			System.out.print("出现错误"+e.getMessage());
		}
	}
	
	/**
	 * 导入组织机构与联系人.
	 * @param importExcel 
	 * @param request 
	 * @return 
	 * 返回类型：String
	 * @throws ParseException 
	 */
	@RequestMapping(value="/excelImport.htm")
	//@Transactional
	@ResponseBody
	public String importExcel(@RequestParam("importExcel") MultipartFile importExcel,HttpServletRequest request) throws ParseException{
		String filename = importExcel.getOriginalFilename();
        String extName = filename.substring(filename.lastIndexOf(".")+1).toLowerCase();
        if(extName.matches("xls")){
        	String lastFileName = System.currentTimeMillis()+extName;
        	String fileFullPath = path + File.separator + lastFileName;
        	File uploadfile = new File(fileFullPath);
        	try {
				FileCopyUtils.copy(importExcel.getBytes(),uploadfile);
			} catch (IOException e) {
				e.printStackTrace();
				return "报表导入失败,请稍候重试!";
			}
        	try {
				Workbook excel = Workbook.getWorkbook(uploadfile);
				Sheet sheet = excel.getSheet(0);
				int rows = sheet.getRows();
				String checkText = checkUploadFileValidata(excel);
				if(StringUtils.hasText(checkText)){
					return checkText;
				}else{
					SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
					SimpleDateFormat format2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					for (int r = 1; r < rows; r++){
						String mobile = sheet.getCell(1, r).getContents().trim();
						//根据号码找到employee_id
						String usercode = this.mEmployeeService.getEmployeeIdByMobile(mobile);
						String employeeRecordModel = sheet.getCell(2,r).getContents().trim();
						Date sportDate = (Date)format.parseObject(sheet.getCell(3,r).getContents().trim());
						long longSportDate = sportDate.getTime();
						Date sportStartTime = (Date)format2.parseObject(format.format(sportDate) + " " + sheet.getCell(4,r).getContents().trim());
						long longSportStartTime = sportStartTime.getTime();
						Date sportEndTime = (Date)format2.parseObject(format.format(sportDate) + " " + sheet.getCell(5,r).getContents().trim());
						long longSportEndTime = sportEndTime.getTime();
						
						String sportElapseTime = sheet.getCell(6,r).getContents().trim();
						String sportStep = sheet.getCell(7,r).getContents().trim();
						String sportDistence = sheet.getCell(8,r).getContents().trim();
						String sportSpeed = sheet.getCell(9,r).getContents().trim();
						String sportCalories = sheet.getCell(10,r).getContents().trim();
						sheet.getCell(11,r).getContents().trim();
						sheet.getCell(12,r).getContents().trim();
						sheet.getCell(13,r).getContents().trim();
						sheet.getCell(14,r).getContents().trim();
						sheet.getCell(15,r).getContents().trim();
						sheet.getCell(16,r).getContents().trim();
						String uploadstr = "[{\"employeeRecordModel\":\""+employeeRecordModel+"\"," +
								"\"sportDate\":\""+longSportDate+"\"," +
								"\"sportStartTime\":\""+longSportStartTime+"\"," +
								"\"sportEndTime\":\""+longSportEndTime+"\"," +
								"\"sportElapseTime\":\""+sportElapseTime+"\"," +
								"\"sportStep\":"+sportStep+"," +
								"\"sportDistence\":"+sportDistence+"," +
								"\"sportSpeed\":"+sportSpeed+"," +
								"\"sportCalories\":\""+sportCalories+"\"," +
								"\"gpsStatus\":\"0\"," +
								"\"gpsDistence\":0,\"gpsSteps\":0,\"modeChangeTimes\":0," +
								"\"stepCalc\":\"0\",\"stepError\":0}]";
						this.uploadRecord(usercode, uploadstr, "123");
					}
					return "导入成功";
				}
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
	
	
	/**
	 * 获取我参加的活动接口(在当前时间段内的活动，不包括过期活动)
	 * @param cmd：命令字，保留字段，暂时不使用
	 * @param usercode：用户employee_id
	 * @param token：令牌，暂时未使用
	 * @param movement_id：如果不上传，则取用户当前参加的活动
	 * @return
	 */
	@RequestMapping(value="/getMyMovement")
	@ResponseBody
	public Map<String,Object> getMyMovement(String cmd,String usercode,String token,String movement_id)
	{
		if(!StringUtils.hasText(movement_id))
		{
			movement_id = this.getMovementId(usercode);
		}
		//我的排名
		List<Map<String,Object>> list= this.movementService.getMovementListByUser(usercode);
		Map<String,Object> map = this.mEmployeeReportService.getEmpOrder(usercode, token, movement_id);
		map.put("value", list);
		return map;
	}
	
	/**
	 * 获取某一活动的所有排名列表
	 * @param usercode
	 * @param movement_id
	 * @return
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/getOrderList")
	@ResponseBody
	public Map<String,Object> getOrderList(String usercode,String movement_id)
	{
		if(!StringUtils.hasText(movement_id))
		{
			movement_id = this.getMovementId(usercode);
		}
		List<Map<String,Object>> list = this.mEmployeeReportService.getOrderList(usercode, movement_id);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("code", "0");
		map.put("value", list);
		//map.put("integral", "0");	//积分，暂时没有计算
		return map;
	}
	
	/**
	 * 获取某个汇总类活动（赛季）的前100名
	 * @param usercode
	 * @param movement_id
	 * @return
	 */
	@RequestMapping("/getAllTop100")
	@ResponseBody
	public Map<String,Object> getAllTop100(String usercode,String movement_id)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		if(!StringUtils.hasText(movement_id))
		{
			movement_id = this.getMovementId(usercode);
		}
		Movement movement = this.movementService.getEntity(movement_id);
		if(movement!=null){
			if(StringUtils.hasText(movement.getParentMovementId())){
				List<Map<String,Object>> list = this.mEmployeeReportService.getAllTop100(movement.getParentMovementId());
				map.put("code", "0");
				map.put("value", list);
			}
			else
			{
				map.put("code", "-1");
				map.put("msg", "所在活动没有参加赛季");
			}
		}
		else
		{
			map.put("code", "-1");
			map.put("msg", "无活动隶属");
		}
		return map;
	}
	
	/**
	 * 获取当前用户所在活动编号（目前只支持一个活动）
	 * 全省混战区的活动编号为0000，如果用户不属于任何一个活动，则返回混战区活动排名
	 * @param usercode
	 * @return
	 */
	private String getMovementId(String usercode)
	{
		String movement_id = "";
		List<Map<String,Object>> mlist = this.movementService.getMovementListByUser(usercode);
		System.out.print(mlist.size());
		if(mlist.isEmpty())
		{
			movement_id = "0000";
		}
		else
		{
			movement_id = mlist.get(0).get("movement_id").toString();
		}
		return movement_id;
	}
	
	/**
	 * 检测上传Excel的有效性
	 * @param excel
	 * @return
	 */
	private String checkUploadFileValidata(Workbook excel)
	{
		Sheet sheet = excel.getSheet(0);
		int rows = sheet.getRows();
		String checkText = "";
		for (int r = 1; r < rows; r++){
			String checkName = sheet.getCell(0,r).getContents().trim();
			//姓名不能为空
			if(!StringUtils.hasText(checkName))
			{
				checkText += "第" + (r+1) + "行姓名为空，导入失败<br />";
			}
			//电话号码不能为空
			String checkMobile= sheet.getCell(1, r).getContents().trim();
			if(checkMobile.length()>11 || "".equals(checkMobile)){
				checkText += "第"+(r+1)+"行电话号码有误,导入失败<br />";
			}
			else
			{
				//判断用户是否存在
				if(!StringUtils.hasText(this.mEmployeeService.checkEmployee(checkMobile)))
				{
					checkText += "第" + (r+1) + "行电话号码在系统中不存在，导入失败<br />";
				}
			}
			//后面不为空就不做判断了
		}
		return checkText;
	}
}

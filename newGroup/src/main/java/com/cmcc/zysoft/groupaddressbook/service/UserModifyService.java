// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.dao.UserModifyDao;
import com.cmcc.zysoft.groupaddressbook.mobile.service.MEmployeeService;
import com.cmcc.zysoft.groupaddressbook.util.concatPinyin;
import com.cmcc.zysoft.sellmanager.model.Employee;
import com.cmcc.zysoft.sellmanager.model.UserCompany;
import com.cmcc.zysoft.sellmanager.model.UserModify;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
import com.starit.common.dao.support.Pagination;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：UserModifyService
 * <br />版本:1.0.0
 * <br />日期： 2013-5-29 上午11:47:26
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class UserModifyService extends BaseServiceImpl<UserModify, String>{
	
	@Resource
	private UserModifyDao userModifyDao;
	
	@Resource
	private UserCompanyService userCompanyService;
	
	@Resource
	private GroupVersionService groupVersionService;
	
	@Resource
	private MEmployeeService mEmployeeService;
	
	@Override
	public HibernateBaseDao<UserModify, String>getHibernateBaseDao(){
		return this.userModifyDao;
	}
	
	/**
	 * 审核列表.
	 * @param rows
	 * @param page
	 * @param companyId
	 * @return 
	 * 返回类型：Pagination<?>
	 */
	public Pagination<?> getModifyInfo(int rows, int page, String companyId){
		return this.userModifyDao.getModifyInfo(rows, page, companyId);
	}
	
	/**
	 * 审核详情.
	 * @param userModifyId
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> list(String userModifyId){
		return this.userModifyDao.list(userModifyId);
	}
	
	/**
	 * 审核通过.
	 * @param userModifyId
	 * @return 
	 * 返回类型：String
	 */
	public String saveModify(String userModifyId) {
		UserModify userModify = this.userModifyDao.get(userModifyId);
		String userCompanyId = userModify.getUserCompanyId();
		UserCompany userCompany = this.userCompanyService.getEntity(userCompanyId);
		if(userModify.getMobile().equals(userCompany.getMobile())){
			//主要号码未修改
			userCompany.setMobile(userModify.getMobile());
			userCompany.setEmployeeName(userModify.getEmployeeName());
			userCompany.setEmployeeFirstword(concatPinyin.firstwordOfName(userModify.getEmployeeName()));
			userCompany.setEmployeeFullword(concatPinyin.changeToPinyin(userModify.getEmployeeName()));
			userCompany.setUserCompany(userModify.getUserCompany());
			userCompany.setDepartmentName(userModify.getDepartmentName());
			userCompany.setHeadshipName(userModify.getHeadshipName());
			userCompany.setMobileShort(userModify.getMobileShort());
			userCompany.setTelephone2(userModify.getTelephone());
			userCompany.setTelephone(userModify.getHomeTelephone());
			userCompany.setTelShort(userModify.getTelShort());
			userCompany.setWeibo(userModify.getWeibo());
			userCompany.setWeixin(userModify.getWeixin());
			userCompany.setSchool(userModify.getSchool());
			userCompany.setUserMajor(userModify.getUserMajor());
			userCompany.setUserGrade(userModify.getUserGrade());
			userCompany.setUserClass(userModify.getUserClass());
			userCompany.setStudentId(userModify.getStudentId());
			userCompany.setNativePlace(userModify.getNativePlace());
			userCompany.setAddress(userModify.getAddress());
			userCompany.setHomeAddress(userModify.getHomeAddress());
			userCompany.setRemark(userModify.getRemark());
			userCompany.setEmail(userModify.getEmail());
			userCompany.setQq(userModify.getQq());
			userCompany.setMood(userModify.getMood());
			this.userCompanyService.updateEntity(userCompany);
			this.groupVersionService.addGroupVersion(userCompanyId, "1");
			userModify.setIsAudited("1");
			this.userModifyDao.update(userModify);
			return "SUCCESS";
		}else{
			List<Employee> empList = this.mEmployeeService.findByNamedParam("mobile", userModify.getMobile());
			if(empList.size()>0){
				return "MOBILE";
			}else{
				userCompany.setMobile(userModify.getMobile());
				userCompany.setEmployeeName(userModify.getEmployeeName());
				userCompany.setEmployeeFirstword(concatPinyin.firstwordOfName(userModify.getEmployeeName()));
				userCompany.setEmployeeFullword(concatPinyin.changeToPinyin(userModify.getEmployeeName()));
				userCompany.setUserCompany(userModify.getUserCompany());
				userCompany.setDepartmentName(userModify.getDepartmentName());
				userCompany.setHeadshipName(userModify.getHeadshipName());
				userCompany.setMobileShort(userModify.getMobileShort());
				userCompany.setTelephone2(userModify.getTelephone());
				userCompany.setTelephone(userModify.getHomeTelephone());
				userCompany.setTelShort(userModify.getTelShort());
				userCompany.setWeibo(userModify.getWeibo());
				userCompany.setWeixin(userModify.getWeixin());
				userCompany.setSchool(userModify.getSchool());
				userCompany.setUserMajor(userModify.getUserMajor());
				userCompany.setUserGrade(userModify.getUserGrade());
				userCompany.setUserClass(userModify.getUserClass());
				userCompany.setStudentId(userModify.getStudentId());
				userCompany.setNativePlace(userModify.getNativePlace());
				userCompany.setAddress(userModify.getAddress());
				userCompany.setHomeAddress(userModify.getHomeAddress());
				userCompany.setRemark(userModify.getRemark());
				userCompany.setEmail(userModify.getEmail());
				userCompany.setQq(userModify.getQq());
				userCompany.setMood(userModify.getMood());
				this.userCompanyService.updateEntity(userCompany);
				this.groupVersionService.addGroupVersion(userCompanyId, "1");
				this.mEmployeeService.updateEmpMobile(userCompany.getEmployeeId(), userCompany.getMobile());
				List<UserCompany> updateEmplist = this.userCompanyService.findByNamedParam("employeeId", userCompany.getEmployeeId());
				for(UserCompany us : updateEmplist){
					this.groupVersionService.addGroupVersion(us.getUserCompanyId(), "1");
				}
				userModify.setIsAudited("1");
				this.userModifyDao.update(userModify);
				return "SUCCESS";
			}
		}
	}
	
	/**
	 * 拒绝审核.
	 * @param userModifyId
	 * @return 
	 * 返回类型：String
	 */
	public String refuseModify(String userModifyId) {
		UserModify userModify = this.userModifyDao.get(userModifyId);
		userModify.setIsAudited("1");
		this.userModifyDao.update(userModify);
		return "SUCCESS";
	}

}

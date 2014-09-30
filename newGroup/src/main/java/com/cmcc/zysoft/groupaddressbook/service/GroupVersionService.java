// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.zysoft.groupaddressbook.dao.GroupVersionDao;
import com.cmcc.zysoft.sellmanager.model.GroupVersion;
import com.cmcc.zysoft.sellmanager.model.UserCompanyChanged;
import com.cmcc.zysoft.sellmanager.model.UserCompanyChangedId;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：GroupVersionService
 * <br />版本:1.0.0
 * <br />日期： 2013-5-24 下午4:52:08
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class GroupVersionService extends BaseServiceImpl<GroupVersion, Integer>{
	
	@Resource
	private UserCompanyChangedService userCompanyChangedService;
	
	@Resource
	private GroupVersionDao groupVersionDao;
	
	@Resource
	private UserCompanyService userCompanyService;
	
	@Override
	public HibernateBaseDao<GroupVersion, Integer>getHibernateBaseDao(){
		return this.groupVersionDao;
	}
	
	/**
	 * 人员分组信息版本表.
	 * @param userCompanyId
	 * @param type 
	 * 返回类型：void
	 */
	@Transactional
	public void addGroupVersion(String userCompanyId,String type){
		try{
		GroupVersion groupVersion = new GroupVersion();
		groupVersion.setUpdateDate(new Date());
		int groupVersionNum = this.groupVersionDao.save(groupVersion);
		UserCompanyChangedId userCompanyChangedId = new UserCompanyChangedId(userCompanyId, groupVersionNum);
		UserCompanyChanged usCompanyChanged = new UserCompanyChanged(userCompanyChangedId);
		usCompanyChanged.setUpdateType(type);
		//将最小职位级别显示在mark2上
		String headshipLevel = this.userCompanyService.getMinHeadshipLevel(userCompanyId);
		usCompanyChanged.setMark2(headshipLevel);
		
		this.userCompanyChangedService.insertEntity(usCompanyChanged);
		}
		catch(Exception e)
		{
			System.out.print(e.getMessage());
		}
	}
	
	/**
	 * 为了客户端更改用户号码不能更新的bug，客户端进行这个兼容性处理
	 * @param userCompanyId
	 * @param type
	 * @param old_mobile
	 */
	public void addGroupVersion(String userCompanyId,String type,String old_mobile)
	{
		try{
			GroupVersion groupVersion = new GroupVersion();
			groupVersion.setUpdateDate(new Date());
			int groupVersionNum = this.groupVersionDao.save(groupVersion);
			UserCompanyChangedId userCompanyChangedId = new UserCompanyChangedId(userCompanyId, groupVersionNum);
			UserCompanyChanged usCompanyChanged = new UserCompanyChanged(userCompanyChangedId);
			usCompanyChanged.setUpdateType(type);
			usCompanyChanged.setMark1(old_mobile);
			//将最小职位级别显示在mark2上
			String headshipLevel = this.userCompanyService.getMinHeadshipLevel(userCompanyId);
			usCompanyChanged.setMark2(headshipLevel);
			
			this.userCompanyChangedService.insertEntity(usCompanyChanged);
			}
			catch(Exception e)
			{
				System.out.print(e.getMessage());
			}
	}
	
	/**
	 * 删除企业时，需删除员工企业关联表
	 * @param companyId
	 * @param type 
	 * 返回类型：void
	 */
	public void addGroupVerByCompany(String companyId,String type){
		try{
			List<Map<String, Object>> list = this.userCompanyService.getUserCompanyByCompanyId(companyId);
			GroupVersion groupVersion = new GroupVersion();
			groupVersion.setUpdateDate(new Date());
			int groupVersionNum = this.groupVersionDao.save(groupVersion);
			for (Map<String, Object> map : list) {
				UserCompanyChangedId userCompanyChangedId = new UserCompanyChangedId(map.get("user_company_id").toString(), groupVersionNum);
				UserCompanyChanged usCompanyChanged = new UserCompanyChanged(userCompanyChangedId);
				usCompanyChanged.setUpdateType(type);
				this.userCompanyChangedService.insertEntity(usCompanyChanged);
			}
		
		}
		catch(Exception e)
		{
			System.out.print(e.getMessage());
		}
	}
	/**
	 * 职位级别变更时，所有职位相关的用户，都要添加版本号
	 * @param headshipId
	 * @param type 
	 * 返回类型：void
	 */
	public void addGroupVerByHeadship(String headshipId,String type){
		try{
			List<Map<String, Object>> list = this.userCompanyService.getUserAndHeadshipLevel(headshipId);
			GroupVersion groupVersion = new GroupVersion();
			groupVersion.setUpdateDate(new Date());
			int groupVersionNum = this.groupVersionDao.save(groupVersion);
			for (Map<String, Object> map : list) {
				UserCompanyChangedId userCompanyChangedId = new UserCompanyChangedId(map.get("user_company_id").toString(), groupVersionNum);
				UserCompanyChanged usCompanyChanged = new UserCompanyChanged(userCompanyChangedId);
				usCompanyChanged.setUpdateType(type);
				usCompanyChanged.setMark2(map.get("headship_level").toString());//mark2存储对应user_company_id的最小headshipLevel
				this.userCompanyChangedService.insertEntity(usCompanyChanged);
			}
		
		}
		catch(Exception e)
		{
			System.out.print(e.getMessage());
		}
	}

}

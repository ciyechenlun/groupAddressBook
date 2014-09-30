// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.dao.CompanyVersionDao;
import com.cmcc.zysoft.sellmanager.model.CompanyChanged;
import com.cmcc.zysoft.sellmanager.model.CompanyChangedId;
import com.cmcc.zysoft.sellmanager.model.CompanyVersion;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：CompanyVersionService
 * <br />版本:1.0.0
 * <br />日期： 2013-5-27 上午11:07:08
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class CompanyVersionService extends BaseServiceImpl<CompanyVersion, Integer>{
	
	@Resource
	private CompanyVersionDao companyVersionDao;
	
	@Resource
	private CompanyChangedService companyChangedService;
	
	@Override
	public HibernateBaseDao<CompanyVersion, Integer> getHibernateBaseDao(){
		return this.companyVersionDao;
	}
	
	/**
	 * 通讯录变更后增加版本记录.type为变更类型：0新增、1更新、2删除
	 * @param companyId
	 * @param type 
	 * 返回类型：void
	 */
	public void addCompanyVersion(String companyId,String type){
		CompanyVersion companyVersion = new CompanyVersion();
		companyVersion.setUpdateDate(new Date());
		int versionNum = this.companyVersionDao.save(companyVersion);
		CompanyChangedId companyChangedId = new CompanyChangedId(companyId, versionNum);
		CompanyChanged companyChanged = new CompanyChanged();
		companyChanged.setId(companyChangedId);
		companyChanged.setUpdateType(type);
		this.companyChangedService.insertEntity(companyChanged);
	}
	/**
	 * 通讯录被清空后，再被添加用户或部门时，需要删除type=3的版本
	 * @param companyId
	 * @param type 
	 * 返回类型：void
	 */
	public void delVsersion(String companyId){
		this.companyChangedService.delVersion(companyId);
	}
}

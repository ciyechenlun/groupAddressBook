// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.dao.CompanyChangedDao;
import com.cmcc.zysoft.sellmanager.model.CompanyChanged;
import com.cmcc.zysoft.sellmanager.model.CompanyChangedId;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：CompanyChangedService
 * <br />版本:1.0.0
 * <br />日期： 2013-5-27 上午11:05:17
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class CompanyChangedService extends BaseServiceImpl<CompanyChanged, CompanyChangedId>{
	
	@Resource
	private CompanyChangedDao companyChangedDao;
	
	@Override
	public HibernateBaseDao<CompanyChanged, CompanyChangedId> getHibernateBaseDao(){
		return this.companyChangedDao;
	}
	public void delVersion(String companyId){
		this.companyChangedDao.delVersion(companyId);
	}
}

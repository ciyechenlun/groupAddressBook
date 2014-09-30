// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.mobile.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.mobile.dao.MUserCompanyDao;
import com.cmcc.zysoft.sellmanager.model.UserCompany;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：MUserCompanyService
 * <br />版本:1.0.0
 * <br />日期： 2013-5-27 下午4:05:58
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class MUserCompanyService extends BaseServiceImpl<UserCompany, String>{
	
	@Resource
	private MUserCompanyDao mUserCompanyDao;
	
	@Override
	public HibernateBaseDao<UserCompany, String>getHibernateBaseDao(){
		return this.mUserCompanyDao;
	}
	
	/**
	 * 下载群组用户.若groupVersion不为空,则为更新数据.
	 * @param companyId
	 * @param groupVersion
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	public Map<String,Object> getGroupUsers(String companyId,String groupVersion){
		return this.mUserCompanyDao.getGroupUsers(companyId, groupVersion);
	}

}

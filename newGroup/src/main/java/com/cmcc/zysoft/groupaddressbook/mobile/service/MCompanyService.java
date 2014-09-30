// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.mobile.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.mobile.dao.MCompanyDao;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：MCompanyService
 * <br />版本:1.0.0
 * <br />日期： 2013-5-27 下午3:11:13
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class MCompanyService extends BaseServiceImpl<Company, String>{
	
	@Resource
	private MCompanyDao mCompanyDao;
	
	@Override
	public HibernateBaseDao<Company, String>getHibernateBaseDao(){
		return this.mCompanyDao;
	}
	
	/**
	 * 获取群组列表.
	 * @param userCode
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	public Map<String,Object> getMyGroups(String userCode){
		return this.mCompanyDao.getMyGroups(userCode);
	}
	
	/**
	 * 更新我的群组.
	 * @param userCode
	 * @param versionNum
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	public Map<String,Object> updateMyGroups(String userCode, String versionNum){
		return this.mCompanyDao.updateMyGroups(userCode, versionNum);
	}

}

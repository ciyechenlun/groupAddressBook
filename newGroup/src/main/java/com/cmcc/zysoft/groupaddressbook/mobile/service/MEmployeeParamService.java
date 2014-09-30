/**
 * CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */

package com.cmcc.zysoft.groupaddressbook.mobile.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.mobile.dao.MEmployeeParamDao;
import com.cmcc.zysoft.sellmanager.model.EmployeeParam;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 袁凤建
 * @email yuan.fengjian@ustcinfo.com
 * @date 2013-6-8 下午3:17:19
 */

@Service
public class MEmployeeParamService extends BaseServiceImpl<EmployeeParam, String> {

	@Resource
	private MEmployeeParamDao mEmployeeParamDao;
	
	@Override
	public HibernateBaseDao<EmployeeParam, String> getHibernateBaseDao() {
		return this.mEmployeeParamDao;
	}
	
	/**
	 * 获取用户的身高体重
	 * @param userCode
	 * @param token
	 * @return
	 */
	public Map<String, Object> getHeightAndWeight(String userCode, String token) {
		return this.mEmployeeParamDao.getHeightAndWeight(userCode, token);
	}
	
	/**
	 * 用户身高、体重、步长上传
	 * @param usercode
	 * @param height
	 * @param weight
	 * @param step
	 * @return
	 */
	public boolean updateUserHeightAndWeight(String usercode,String height,String weight,String step)
	{
		return this.mEmployeeParamDao.updateUserHeightAndWeight(usercode, height, weight, step);
	}
	
	/**
	 * 用户短信签名上传，使用mark1字段
	 * @param usercode
	 * @param sign
	 * @return
	 */
	public boolean updateUserSign(String usercode,String sign)
	{
		return this.mEmployeeParamDao.updateUserSign(usercode, sign);
	}
	
	/**
	 * 获取用户签名
	 * @param userCode
	 * @return
	 */
	public String getUserSign(String userCode)
	{
		return this.mEmployeeParamDao.getUserSign(userCode);
	}
}

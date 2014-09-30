// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.mobile.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.mobile.dao.MAdviseDao;
import com.cmcc.zysoft.sellmanager.model.Advise;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：MAdviseService
 * <br />版本:1.0.0
 * <br />日期： 2013-4-9 下午4:05:31
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class MAdviseService extends BaseServiceImpl<Advise, String>{
	
	@Resource
	private MAdviseDao mAdviseDao;
	
	@Override
	public HibernateBaseDao<Advise, String>getHibernateBaseDao(){
		return this.mAdviseDao;
	}
	
	/**
	 * 提交建议.
	 * @param employeeId 
	 * @param content 
	 * @param companyId 
	 * @return 
	 * 返回类型：Map<String,Object>
	 */
	public Map<String, Object> addAdvise(String employeeId, String content, String companyId){
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			Advise advise = new Advise();
			advise.setAdviseMan(employeeId);
			advise.setContent(content);
			advise.setCompanyId(companyId);
			advise.setAdviseDate(new Date());
			this.mAdviseDao.save(advise);
			map.put("success", "true");
		}catch (Exception e){
			map.put("success", "false");
		}
		return map;
	}
	

}

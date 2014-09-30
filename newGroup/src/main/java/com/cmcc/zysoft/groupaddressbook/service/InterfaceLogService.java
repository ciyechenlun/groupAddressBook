// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.dao.InterfaceLogDao;
import com.cmcc.zysoft.sellmanager.model.InterfaceLog;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
import com.starit.common.dao.support.Pagination;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：InterfaceLogService
 * <br />版本:1.0.0
 * <br />日期： 2013-4-12 下午3:34:02
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class InterfaceLogService extends BaseServiceImpl<InterfaceLog, Long>{
	
	@Resource
	private InterfaceLogDao interfaceLogDao;
	
	@Override
	public HibernateBaseDao<InterfaceLog, Long>getHibernateBaseDao(){
		return this.interfaceLogDao;
	}
	
	/**
	 * 接口日志列表,可根据时间查询.
	 * @param rows 
	 * @param page 
	 * @param companyId 
	 * @param key 
	 * @return 
	 * 返回类型：Pagination<?>
	 */
	public Pagination<?> logList(int rows, int page, String companyId, String key){
		return this.interfaceLogDao.logList(rows, page, companyId, key);
	}

}

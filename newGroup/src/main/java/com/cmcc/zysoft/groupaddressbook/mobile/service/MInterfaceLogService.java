// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.mobile.service;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.zysoft.groupaddressbook.mobile.dao.MInterfaceLogDao;
import com.cmcc.zysoft.sellmanager.model.InterfaceLog;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：MInterfaceLogService
 * <br />版本:1.0.0
 * <br />日期： 2013-4-11 下午5:22:08
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class MInterfaceLogService extends BaseServiceImpl<InterfaceLog, Long>{
	
	@Resource
	private MInterfaceLogDao mInterfaceLogDao;
	
	@Override
	public HibernateBaseDao<InterfaceLog, Long>getHibernateBaseDao(){
		return this.mInterfaceLogDao;
	}
	
	/**
	 * 接口调用日志.
	 * @param employeeId 调用人
	 * @param companyId 公司
	 * @param interfaceName 接口名称
	 * 返回类型：void
	 */
	@Transactional
	public void addLog(String employeeId,String companyId, String interfaceName){
		InterfaceLog interfaceLog = new InterfaceLog();
		interfaceLog.setCompanyId(companyId);
		interfaceLog.setInterfaceName(interfaceName);
		interfaceLog.setOperateMan(employeeId);
		interfaceLog.setOperateTime(new Date());
		this.mInterfaceLogDao.save(interfaceLog);
	}

}

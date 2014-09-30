// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.dao.TxlVersionDao;
import com.cmcc.zysoft.sellmanager.model.Employee;
import com.cmcc.zysoft.sellmanager.model.TxluserModifyusers;
import com.cmcc.zysoft.sellmanager.model.TxluserModifyusersId;
import com.cmcc.zysoft.sellmanager.model.TxluserVersion;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 杜纪亮
 * <br />邮箱：du.jiliang@ustcinfo.com
 * <br />描述：TxlVersionService
 * <br />版本:1.0.0
 * <br />日期： 2013-3-6 下午6:09:38
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class TxlVersionService extends BaseServiceImpl<TxluserVersion, Integer> {
	
	@Resource
	private TxluserModifyService txluserModifyService;
	
	@Resource
	private TxlVersionDao txlVersionDao;
	
	@Override
	public HibernateBaseDao<TxluserVersion, Integer> getHibernateBaseDao(){
		return this.txlVersionDao;
	}
	
	/**
	 * 对员工表操作时,更新员工版本记录表.
	 * @param type 更新类型
	 * @param employeeId  员工Id
	 * 返回类型：void
	 */
	public void saveAll(String type,String employeeId){
		TxluserVersion txluserVersion = new TxluserVersion();
		TxluserModifyusers txluserModifyusers = new TxluserModifyusers();
		TxluserModifyusersId txluserModifyusersId = new TxluserModifyusersId();
		Employee employee = new Employee(employeeId);
		txluserVersion.setUpdateDate(new Date());
		int versionNum = this.txlVersionDao.save(txluserVersion);
		txluserModifyusersId.setEmployeeId(employeeId);
		txluserModifyusersId.setTxluserVersionNum(versionNum);
		txluserModifyusers.setEmployee(employee);
		txluserModifyusers.setId(txluserModifyusersId);
		txluserModifyusers.setTxluserVersion(txluserVersion);
		txluserModifyusers.setUpdateType(type);
		this.txluserModifyService.insertEntity(txluserModifyusers);
	}

}

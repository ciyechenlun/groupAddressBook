// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.dao.DepartmentChangedDao;
import com.cmcc.zysoft.sellmanager.model.DepartmentChanged;
import com.cmcc.zysoft.sellmanager.model.DepartmentChangedId;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：DepartmentChangedService
 * <br />版本:1.0.0
 * <br />日期： 2013-3-7 下午1:10:17
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class DepartmentChangedService extends BaseServiceImpl<DepartmentChanged, DepartmentChangedId>{
	
	@Resource
	private DepartmentChangedDao departmentChangedDao;
	
	@Override
	public HibernateBaseDao<DepartmentChanged, DepartmentChangedId>getHibernateBaseDao(){
		return this.departmentChangedDao;
	}

}

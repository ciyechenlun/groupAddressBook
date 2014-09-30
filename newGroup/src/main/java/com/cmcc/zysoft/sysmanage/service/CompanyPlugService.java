package com.cmcc.zysoft.sysmanage.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.sellmanager.model.CompanyPlug;
import com.cmcc.zysoft.sysmanage.dao.CompanyPlugDao;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author liyuchen
 * <br />邮箱： li.yuchen@ustcinfo.com
 * <br />描述：
 * <br />@version:1.0.0
 * <br />日期： 2014-4-30 上午18:08:56
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class CompanyPlugService extends BaseServiceImpl<CompanyPlug, String> {
	
	@Resource
	private CompanyPlugDao companyPlugDao;

	@Override
	public HibernateBaseDao<CompanyPlug, String> getHibernateBaseDao() {
		// TODO Auto-generated method stub
		return this.companyPlugDao;
	}

}

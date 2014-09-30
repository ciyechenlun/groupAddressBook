package com.cmcc.zysoft.groupaddressbook.mobile.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.mobile.dao.RecordGpsDao;
import com.cmcc.zysoft.sellmanager.model.RecordGps;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
/**
 * @author 周瑜
 * <br />邮箱： zhouyusgs#ahmobile.com
 * <br />描述：用户GPS上传接口服务层
 * <br />版本:1.0.0
 * <br />日期： 2013-7-8 上午11:40:55
 * <br />
 */
@Service
public class RecordGpsService extends BaseServiceImpl<RecordGps, String> {

	@Resource
	private RecordGpsDao recordGpsDao;
	
	@Override
	public HibernateBaseDao<RecordGps, String> getHibernateBaseDao() {
		// TODO Auto-generated method stub
		return this.recordGpsDao;
	}

}

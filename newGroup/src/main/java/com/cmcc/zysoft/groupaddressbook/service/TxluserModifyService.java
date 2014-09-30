// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.dao.TxluserModifyDao;
import com.cmcc.zysoft.sellmanager.model.TxluserModifyusers;
import com.cmcc.zysoft.sellmanager.model.TxluserModifyusersId;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 杜纪亮
 * <br />邮箱：du.jiliang@ustcinfo.com
 * <br />描述：TxluserModifyService
 * <br />版本:1.0.0
 * <br />日期： 2013-3-6 下午6:19:14
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class TxluserModifyService extends BaseServiceImpl<TxluserModifyusers, TxluserModifyusersId>{
	
	@Resource
	private TxluserModifyDao txluserModifyDao;
	
	@Override
	public HibernateBaseDao<TxluserModifyusers, TxluserModifyusersId> getHibernateBaseDao(){
		return this.txluserModifyDao;
	}
}

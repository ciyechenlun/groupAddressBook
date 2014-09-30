// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.dao.ReplyDao;
import com.cmcc.zysoft.sellmanager.model.Advise;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

@Service
public class ReplyService extends BaseServiceImpl<Advise, String>{

	@Resource
	private ReplyDao replyDao;
	@Override
	public HibernateBaseDao<Advise, String> getHibernateBaseDao() {
		// TODO Auto-generated method stub
		return this.replyDao;
	}
	/**
	 * 根据反馈ID的获取反馈的信息
	 */
	public Map<String,Object> getAdviseInfo(String adviseId){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> maps = replyDao.getInfo(adviseId);
		if(!maps.isEmpty()){
			map = maps.get(0);
		}
		return map;
	}

}

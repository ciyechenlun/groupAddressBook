// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.dao.AdviseDao;
import com.cmcc.zysoft.sellmanager.model.Advise;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
import com.starit.common.dao.support.Pagination;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：AdviseService
 * <br />版本:1.0.0
 * <br />日期： 2013-4-10 上午10:22:40
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class AdviseService extends BaseServiceImpl<Advise, String>{
	
	@Resource
	private AdviseDao adviseDao;
	
	@Override
	public HibernateBaseDao<Advise, String>getHibernateBaseDao(){
		return this.adviseDao;
	}
	
	/**
	 * 分页获取反馈建议列表.
	 * @param rows 
	 * @param page 
	 * @param companyId 
	 * @return 
	 * 返回类型：Pagination<?>
	 */
	public Pagination<?> adviseList(int rows, int page, String companyId){
		return this.adviseDao.adviseList(rows, page, companyId);
	}

}

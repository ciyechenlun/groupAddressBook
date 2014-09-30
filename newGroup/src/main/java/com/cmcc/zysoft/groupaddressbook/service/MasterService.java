package com.cmcc.zysoft.groupaddressbook.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.dao.MasterDao;
import com.cmcc.zysoft.sellmanager.model.Master;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
import com.starit.common.dao.support.Pagination;

/**
 * @author 周瑜
 * <br />邮箱： zhouyusgs#ahmobile.com
 * <br />描述：MasterService
 * <br />版本:1.0.0
 * <br />日期： 2013-5-23 上午10:22:40
 * <br />CopyRight © China Mobile Anhui cmp Ltd.
 */
@Service
public class MasterService extends BaseServiceImpl<Master, String> {

	@Resource
	private MasterDao masterDao;
	
	@Override
	public HibernateBaseDao<Master, String> getHibernateBaseDao() {
		// TODO Auto-generated method stub
		return this.masterDao;
	}
	
	/**
	 * 分页获取反馈建议列表.
	 * @param rows 
	 * @param page 
	 * @param companyId 
	 * @return 
	 * 返回类型：Pagination<?>
	 */
	public Pagination<?> masterList(int rows, int page, String companyId,String key){
		return this.masterDao.masterList(rows, page, companyId,key);
	}
	
	/**
	 * 添加一个角色
	 * @param master_name 角色名称
	 * @param master_taxis 角色排序
	 * @param companyId 超级管理员可以维护其他公司的角色信息
	 */
	public void saveEntry(String master_name,String master_taxis,String companyId)
	{
		Master master = new Master();
		master.setCompanyId(companyId);
		master.setMasterName(master_name);
		master.setTaxis(Integer.parseInt(master_taxis));
		this.masterDao.save(master);
	}
	
	/**
	 * 删除角色
	 * @param master_id 角色编号
	 */
	public void master_delete(String master_id)
	{
		this.masterDao.master_delete(master_id);
	}

}

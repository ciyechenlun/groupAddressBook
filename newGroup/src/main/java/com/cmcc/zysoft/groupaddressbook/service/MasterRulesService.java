package com.cmcc.zysoft.groupaddressbook.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.dao.MasterRulesDao;
import com.cmcc.zysoft.sellmanager.model.MasterRules;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
import com.starit.common.dao.support.Pagination;

/**
 * @author 周瑜
 * <br />邮箱： zhouyusgs#ahmobile.com
 * <br />描述：MasterRulesService
 * <br />版本:1.0.0
 * <br />日期： 2013-5-24 上午10:22:40
 * <br />CopyRight © China Mobile Anhui cmp Ltd.
 */
@Service
public class MasterRulesService extends BaseServiceImpl<com.cmcc.zysoft.sellmanager.model.MasterRules, String> {

	@Resource
	private MasterRulesDao masterRulesDao;
	
	@Override
	public HibernateBaseDao<MasterRules, String> getHibernateBaseDao() {
		// TODO Auto-generated method stub
		return this.masterRulesDao;
	}
	
	/**
	 * 获取角色下的权限集合
	 * @param rows
	 * @param page
	 * @param masterId 角色Id
	 * @param key 关键词，暂未实现
	 * @return
	 */
	public Pagination<?> masterRulesList(int rows, int page, String masterId,String key)
	{
		return this.masterRulesDao.masterRulesList(rows, page, masterId, key);
	}
	
	/**
	 * 删除规则明细
	 * @param rules_id：规则明细编号
	 * @return 成功 or 失败
	 */
	public boolean deleteMasterRules(String rules_id)
	{
		this.masterRulesDao.deleteMasterRules(rules_id);
		return true;
	}

	/**
	 * 添加规则明细
	 * @param master_id：角色ID
	 * @param selReleation：与前一个条件的关系
	 * @param content：规则内容
	 */
	public void addMasterRules(String master_id,String selReleation,String content)
	{
		this.masterRulesDao.addMasterRules(master_id, selReleation, content);
	}
	/**
	 * 获取企业下的权限设置（唯一）
	 * @param companyId
	 * @return
	 */
	public Map<String,Object> getMasterRule(String companyId)
	{
		return this.masterRulesDao.getMasterRule(companyId);
	}
	/**
	 * 添加权限规则（精简）
	 * @param master_id
	 * @param rules_sql
	 */
	public void addMasterRule(String rule_id,String master_id,String rules_sql)
	{
		this.masterRulesDao.addMasterRule(rule_id,master_id, rules_sql);
	}

}

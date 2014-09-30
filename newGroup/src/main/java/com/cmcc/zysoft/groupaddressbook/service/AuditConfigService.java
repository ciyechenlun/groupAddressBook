// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.zysoft.groupaddressbook.dao.AuditConfigDao;
import com.cmcc.zysoft.sellmanager.model.AuditConfig;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：AuditConfigService
 * <br />版本:1.0.0
 * <br />日期： 2013-5-29 上午10:22:28
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class AuditConfigService extends BaseServiceImpl<AuditConfig, String>{
	
	@Resource
	private AuditConfigDao auditConfigDao;
	
	@Override
	public HibernateBaseDao<AuditConfig, String>getHibernateBaseDao(){
		return this.auditConfigDao;
	}
	
	/**
	 * 判断是否需要审核.
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> list(){
		return this.auditConfigDao.list();
	}
	
	/**
	 * 判断审核配置表是否有数据,若无,则插入一条数据.
	 *  
	 * 返回类型：void
	 */
	@Transactional
	public void checkList(){
		List<Map<String, Object>> list = this.auditConfigDao.checkList();
		if(list.size()==0){
			AuditConfig auditConfig = new AuditConfig();
			auditConfig.setAuditFlag("1");
			auditConfig.setModifyFlag("0");
			this.auditConfigDao.save(auditConfig);
		}
	}
	
	/**
	 * 修改审核配置表
	 * @param value 
	 * 返回类型：void
	 */
	public void updateAudit(String value){
		if("2".equals(value)){
			this.auditConfigDao.updateAudit("1", "0");
		}else{
			this.auditConfigDao.updateAudit("0", "1");
		}
	}

}

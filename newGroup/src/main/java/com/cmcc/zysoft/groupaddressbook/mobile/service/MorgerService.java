/**
 * @author 徐刚强
 */
package com.cmcc.zysoft.groupaddressbook.mobile.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.mobile.dao.MorgerDao;
import com.cmcc.zysoft.sellmanager.model.Employee;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
/**
 * @author gavin
 *
 */
@Service
public class MorgerService extends BaseServiceImpl<Employee, String>{
	
	@Resource
	private MorgerDao morgerDao;
	
	@Override
	public HibernateBaseDao<Employee, String> getHibernateBaseDao() {
		return this.morgerDao;
	}
	
	/**
	 * 下载所有部门
	 * @param cmd
	 * @param userCode
	 * @param deptCode
	 * @param deptList
	 * @param token
	 * @param company_id：可为空。如果为空则取当前用户所在的所有公司。指定公司 id则下载指定公司的部门
	 * @return
	 */
	public Map<String,Object> getAllorgerList(String cmd, String userCode, int deptCode, String deptList, String token, String company_id){
		//return this.morgerDao.getAllorgerList(cmd, userCode, zzjgversionCode, zzjg, token);
		Map<String, Object> map = new HashMap<String,Object>();
		map = this.morgerDao.getAllorgerList(cmd, userCode, deptCode, deptList, token, company_id);
		int versionnum = this.morgerDao.getdeptversion();
		map.put("deptversion", versionnum);
		map.put("cmd", cmd);
		int total = (int)map.get("total");
		if (total > 0)
			map.put("code", 0);
		else
			map.put("code", -1);
		return map;
	}
	
	/**
	 * 部门更新接口
	 * @param cmd 操作符，无具体含义
	 * @param userCode 用户编号（登录表的userId)
	 * @param deptCode 部门的版本号，服务器端根据此版本号返回更新的部门
	 * @param deptList 废除字段
	 * @param token 
	 * @return
	 */
	public Map<String,Object> updateorgerList(String cmd, String userCode, int deptCode, String deptList, String token,String filter_company){
		Map<String, Object> map = new HashMap<String,Object>();
		map = this.morgerDao.updateorgerList(cmd, userCode, deptCode, deptList, token, filter_company);
		int versionnum = this.morgerDao.getdeptversion();
		map.put("deptversion", versionnum);
		map.put("cmd", cmd);
		int total = (int)map.get("total");
		if (total > 0)
			map.put("code", 0);
		else
			map.put("code", -1);
		return map;
	}
	
	

}

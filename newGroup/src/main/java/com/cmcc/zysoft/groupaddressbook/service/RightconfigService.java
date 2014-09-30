// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.zysoft.groupaddressbook.dao.RightconfigDao;
import com.cmcc.zysoft.sellmanager.model.Rightconfig;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：RightconfigService
 * <br />版本:1.0.0
 * <br />日期： 2013-3-18 上午11:47:50
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class RightconfigService extends BaseServiceImpl<Rightconfig, String>{
	
	@Resource
	private RightconfigDao rightconfigDao;
	
	@Override
	public HibernateBaseDao<Rightconfig, String>getHibernateBaseDao(){
		return this.rightconfigDao;
	}
	
	/**
	 * 权限列表.
	 * @param type 权限类型.
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> rightConfig(String type){
		return this.rightconfigDao.rightConfig(type);
	}
	
	/**
	 * 保存选择的权限,选中的设为1,未选中的设为0.
	 * @param rightId 
	 * 返回类型：void
	 */
	public void check(String rightId){
		this.rightconfigDao.check(rightId);
	}
	
	/**
	 * 获取选择的权限,将其中具有重复权限的去除.例如权限中包括上两级和上一级,则取上两级.
	 * @return  返回的内容为处理后的权限,依次为上级_平级_下级;若存在对应权限,则对应级别有值,否则为空;
	 * 返回类型：String
	 */
	public String rights(){
		String up = this.rightconfigDao.rights("2");//上级标志位
		String down = this.rightconfigDao.rights("0");//下级标志位
		String self = this.rightconfigDao.rights("1");//平级标志位
		if("0".equals(self)){
			self = "1";
		}
		return up+","+self+","+down;
	}
	
	/**
	 * 获取当前登陆人所在公司的权限级别.
	 * @param companyId 
	 * @return 
	 * 返回类型：int
	 */
	public int rightconfigLevel(String companyId){
		return this.rightconfigDao.rightconfigLevel(companyId);
	}
	
	/**
	 * 判断当前公司是否有平级权限.
	 * @param companyId 
	 * @return 
	 * 返回类型：boolean
	 */
	public boolean checkSelf(String companyId){
		return this.rightconfigDao.checkSelf(companyId);
	}
	
	/**
	 * 判断权限级别和部门级别,使权限级别和部门一别一致.
	 * @param deptLevel 部门级别
	 * @param rightLevel 权限级别
	 * @param companyId  
	 * 返回类型：void
	 */
	@Transactional
	public void addRightconfig(int deptLevel, int rightLevel, String companyId){
		for(int i = rightLevel+1;i< deptLevel;i++){
			Rightconfig rightconfig_up = new Rightconfig();
			Rightconfig rightconfig_down = new Rightconfig();
			rightconfig_up.setCompanyId(companyId);
			rightconfig_up.setRightconfigName("上"+ i +"级");
			rightconfig_up.setRightconfigChecked("0");
			rightconfig_up.setRightconfigType("2");
			rightconfig_up.setLevel(i);
			this.rightconfigDao.save(rightconfig_up);
			rightconfig_down.setCompanyId(companyId);
			rightconfig_down.setRightconfigName("下"+ i +"级");
			rightconfig_down.setRightconfigChecked("0");
			rightconfig_down.setRightconfigType("0");
			rightconfig_down.setLevel(i);
			this.rightconfigDao.save(rightconfig_down);
		}
	}
	
	/**
	 * 判断数据库是否有平级权限,若无,则插入.
	 * @param companyId 
	 * 返回类型：void
	 */
	@Transactional
	public void addRightconfigSelf(String companyId){
		if(!this.checkSelf(companyId)){
			Rightconfig rightconfig_self = new Rightconfig();
			rightconfig_self.setCompanyId(companyId);
			rightconfig_self.setRightconfigName("平级");
			rightconfig_self.setRightconfigChecked("0");
			rightconfig_self.setRightconfigType("1");
			rightconfig_self.setLevel(0);
			this.rightconfigDao.save(rightconfig_self);
		}
	}

}

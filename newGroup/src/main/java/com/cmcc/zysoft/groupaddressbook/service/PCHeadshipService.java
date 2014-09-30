// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.groupaddressbook.dao.PCHeadshipDao;
import com.cmcc.zysoft.sellmanager.model.Headship;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
import com.starit.common.dao.support.Pagination;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：HeadshipService
 * <br />版本:1.0.0
 * <br />日期： 2013-5-16 上午10:59:57
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class PCHeadshipService extends BaseServiceImpl<Headship, String>{
	
	@Resource
	private PCHeadshipDao pcHeadshipDao;
	@Resource
	private GroupVersionService groupVersionService;
	
	@Override
	public HibernateBaseDao<Headship, String>getHibernateBaseDao(){
		return this.pcHeadshipDao;
	}
	
	/**
	 * 接口日志列表,可根据时间查询.
	 * @param rows 
	 * @param page 
	 * @param companyId 
	 * @param key 关键字
	 * @return 
	 * 返回类型：Pagination<?>
	 */
	public Pagination<?> list(int rows, int page, String companyId,String key){
		return this.pcHeadshipDao.list(rows, page, companyId,key);
	}
	
	/**
	 * 获取指定岗位Id的岗位信息.
	 * @param headshipId
	 * @return 
	 * 返回类型：List<Map<String,Object>>
	 */
	public List<Map<String, Object>> headship(String headshipId){
		return this.pcHeadshipDao.headship(headshipId);
	}
	
	/**
	 * 物理删除岗位信息.
	 * @param headshipId
	 * @return 
	 * 返回类型：String
	 */
	public String deleteHeadship(String headshipId){
		Long count = this.pcHeadshipDao.getUseHeadshipCount(headshipId);
		if(count>0){
			return "USE";
		}
		this.pcHeadshipDao.delete(headshipId);
		return "SUCCESS";
	}
	
	/**
	 * 新增或者修改岗位.
	 * @param companyId
	 * @param headship
	 * @return 
	 * 返回类型：String
	 */
	public String saveHeadship(String companyId, Headship headship){
		List<Headship> list = this.findByNamedParam(new String[]{"companyId","headshipName"}, new Object[]{companyId,headship.getHeadshipName()});
		if(StringUtils.hasText(headship.getHeadshipId())){
			Headship new_headship = this.pcHeadshipDao.get(headship.getHeadshipId());
			if(!new_headship.getHeadshipName().equals(headship.getHeadshipName()) && list.size()>0){
				return "NAME";
			}else{
				boolean flag=false;
				if(!new_headship.getHeadshipLevel().equals(headship.getHeadshipLevel())){
					flag=true;
				}
				new_headship.setHeadshipName(headship.getHeadshipName());
				new_headship.setHeadshipLevel(headship.getHeadshipLevel());
				new_headship.setDescription(headship.getDescription());
				this.pcHeadshipDao.update(new_headship);
				if(flag){
					this.groupVersionService.addGroupVerByHeadship(headship.getHeadshipId(), "1");
				}
				return "SUCCESS";
			}
		}else{
			if(list.size()>0){
				return "NAME";
			}else{
				headship.setCompanyId(companyId);
				headship.setDelFlag("0");
				String headshipId = this.pcHeadshipDao.save(headship);
				if(StringUtils.hasText(headshipId)){
					return "SUCCESS";
				}else{
					return "FALSE";
				}
			}
		}
	}
	
}

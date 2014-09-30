package com.cmcc.zysoft.sysmanage.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.sellmanager.model.Headship;
import com.cmcc.zysoft.sysmanage.dao.HeadshipDao;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
import com.starit.common.dao.support.Pagination;

/**
 * HeadshipService.java
 * @author zhangweihua
 * @email zhang.weihua@ustcinfo.com
 * @date 2012-12-2 下午6:06:41
 *
 */
@Service
public class HeadshipService extends BaseServiceImpl<Headship,String> {
	@Resource
	private HeadshipDao headshipDao;
	
	@Override
	public HibernateBaseDao<Headship, String> getHibernateBaseDao() {
		return headshipDao;
	}
	
	/**
	 * 获取所有岗位信息List
	 * 
	 * @return
	 */
	public List<Headship> getHeadshipList() {
		return headshipDao.getHeadshipList();
	}
	
	/**
	 * 获取所有岗位信息
	 * 
	 * @return
	 */
	public Pagination<Object> getAllHeadships(int page, int rows,String idIcon,String isAdmin) {
		return this.headshipDao.getAllHeadships(page,rows,idIcon,isAdmin);
	}
	
	/**
	 * 根据查询条件获取岗位信息
	 * 
	 * @param headshipName 岗位名称
	 * @param page
	 * @param rows
	 * @return
	 */
	public Pagination<Object> getHeadshipsByCondition(String headshipName,int page, int rows) {
		return this.headshipDao.getHeadshipsByCondition(headshipName,page,rows);
	}
	
    /**
     * 保存岗位信息
     * 
     * @param headship 岗位对象
     */
    public void saveHeadship(Headship headship){
    	this.headshipDao.saveHeadship(headship);
    }
    
    /**
     * 根据岗位id获取岗位信息
     * 
     * @param headshipId 岗位id
     * @return
     */
    public Headship getHeadship(String headshipId){
    	Headship headship =	this.headshipDao.getHeadship(headshipId);
    	return headship;
    }
    
    /**
     * 修改岗位信息
     * 
     * @param headship 岗位对象
     */
    public void updateHeadship(Headship headship){
    	this.headshipDao.updateHeadship(headship);
	}
    
    /**
     * 根据岗位id删除岗位信息
     * 
     * @param headshipId 岗位信息id
     */
    public void deleteHeadship(String headshipId){
    	this.headshipDao.deleteHeadship(headshipId);
    }
    
    /**
	 * 根据公司id获取下拉框
	 * @param companyId
	 * @return
	 */
	public List<Map<String, Object>> headshipCombo(String companyId) {
		return this.headshipDao.headshipCombo(companyId);
	}
}

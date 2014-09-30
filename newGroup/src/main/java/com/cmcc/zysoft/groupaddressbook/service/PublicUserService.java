// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.groupaddressbook.dao.PublicUserDao;
import com.cmcc.zysoft.sellmanager.model.PublicUser;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 张军
 * <br />邮箱： zhang.jun3@ustcinfo.com
 * <br />描述：PublicUserService
 * <br />版本:1.0.0
 * <br />日期： 2014-4-10 上午10:22:40
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class PublicUserService extends BaseServiceImpl<PublicUser, String>{
	
	@Resource
	private PublicUserDao publicUserDao;
	
	@Override
	public HibernateBaseDao<PublicUser, String> getHibernateBaseDao(){
		return this.publicUserDao;
	}
	/**
	 * 添加公告发送对象以及管理员信息
	 * @param publicId 公告Id
	 * @param target 发送对象（部门id或者人员ID）
	 * @param managerMember 管理员Id
	 * @param toRange 发送范围：0：整个企业，1：自选部门，2：自选成员
	 */
	
	public void addInfo(String publicId,String target,String managerMember,String toRange){
		PublicUser publicUser = new PublicUser();
		publicUser.setPublicId(publicId);
		
		if(managerMember!=null&&StringUtils.hasText(managerMember)){
			//添加管理员
			String[] managers = managerMember.split(",");
			//设置管理员
			publicUser.setRelateType("0");
			for(String manager:managers){
				publicUser.setUserCompanyId(manager);
				this.insertEntity(publicUser);
			}
		}
		publicUser = new PublicUser();
		publicUser.setPublicId(publicId);
		if(target!=null&&StringUtils.hasText(target)){
			String[] targets = target.split(",");
			//设置发送对象
			publicUser.setRelateType("1");
			if("1".equals(toRange)){
				//按部门发送
				for(String deptId:targets){
					publicUser.setDepartmentId(deptId);
					this.insertEntity(publicUser);
				}
			}else if("2".equals(toRange)){
				//按成员发送
				for(String userCompanyId:targets){
					publicUser.setUserCompanyId(userCompanyId);
					this.insertEntity(publicUser);
				}
			}
		}
	}
	/**
	 * 获取发送对象或者管理员信息
	 * @param publicId 公告号Id
	 * @param relateType 关联类型（0：管理员 1：发送对象）
	 * @param toRange 发送范围（0：全企业 1：自选部门 2：自选成员 ）
	 * @return
	 */
	public List<Map<String,Object>> getMemberInfo(String publicId,String relateType,String roRange){
		return this.publicUserDao.getMemberInfo(publicId, relateType, roRange);
	}


}

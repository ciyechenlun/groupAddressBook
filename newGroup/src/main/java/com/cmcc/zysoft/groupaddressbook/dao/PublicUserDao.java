// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.PublicUser;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;
import com.starit.common.dao.jdbc.NamedParameterJdbcTemplateExt;

/**
 * @author 张军
 * <br />邮箱： zhang.jun3@ustcinfo.com
 * <br />描述：PublicRoadDao
 * <br />版本:1.0.0
 * <br />日期： 2014-4-10 上午10:09:16
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Repository
public class PublicUserDao extends HibernateBaseDaoImpl<PublicUser, String>{
	
	@Autowired
	private NamedParameterJdbcTemplateExt namedParameterJdbcTemplateExt;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 获取发送对象或者管理员信息
	 * @param publicId 公告号Id
	 * @param relateType 关联类型（0：管理员 1：发送对象）
	 * @param toRange 发送范围（0：全企业 1：自选部门 2：自选成员 ）
	 * @return
	 */
	public List<Map<String,Object>> getMemberInfo(String publicId,String relateType,String toRange){
		String sql = "";
		if(relateType=="1"){//获取发送对象
			if("2".equals(toRange)){//自定义成员
				sql = "SELECT pu.user_company_id AS id,uc.employee_name AS name " +
						"FROM tb_c_public_user pu ,tb_c_user_company uc " +
						"WHERE pu.user_company_id = uc.user_company_id AND uc.del_flag='0' AND pu.relate_type='1' AND pu.public_id='"+publicId+"'";
			}else{//自定义部门
				sql = "SELECT pu.department_id AS id,dept.department_name AS name " +
						"FROM tb_c_public_user pu ,tb_c_department dept " +
						"WHERE pu.department_id = dept.department_id AND dept.del_flag='0' AND pu.relate_type='1' AND pu.public_id='"+publicId+"'";
			}
		}else{//获取管理员
			sql = "SELECT pu.user_company_id AS id,uc.employee_name AS name " +
					"FROM tb_c_public_user pu ,tb_c_user_company uc " +
					"WHERE pu.user_company_id = uc.user_company_id AND uc.del_flag='0' AND pu.relate_type='0' AND pu.public_id='"+publicId+"'";
		}
		return this.jdbcTemplate.queryForList(sql);
		
	}

}

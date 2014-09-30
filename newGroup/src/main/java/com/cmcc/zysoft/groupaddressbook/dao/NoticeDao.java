// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.dao;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.Notice;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;
import com.starit.common.dao.jdbc.NamedParameterJdbcTemplateExt;
import com.starit.common.dao.support.Pagination;

/**
 * @author 张军
 * <br />邮箱： zhang.jun3@ustcinfo.com
 * <br />描述：PublicRoadDao
 * <br />版本:1.0.0
 * <br />日期： 2014-4-10 上午10:09:16
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Repository
public class NoticeDao extends HibernateBaseDaoImpl<Notice, String>{
	
	@Autowired
	private NamedParameterJdbcTemplateExt namedParameterJdbcTemplateExt;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String,Object>> getPublicRoadList(String userId,String companyId){
		String sql="select uc.user_company_id,pr.public_id,pr.public_name,pr.picture from tb_c_system_user su,tb_c_user_company uc," +
				"tb_c_public_user pu,tb_c_public_road pr " +
				"where su.employee_id=uc.employee_id and uc.user_company_id=pu.user_company_id " +
				"and pu.public_id=pr.public_id and su.del_flag='0' and uc.del_flag='0' and pu.relate_type='0' " +
				"and pr.status='0' and su.user_id=? and uc.company_id=?";
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql, userId,companyId);
		return list;
	}
	/**
	 * 获取手机号集合
	 * @param publicId
	 * @return
	 */
	public List<Map<String,Object>> getMobileList(String publicId){
		String sql="select to_range,company_id from tb_c_public_road where public_id=?";
		Map<String,Object> map=  this.jdbcTemplate.queryForMap(sql, publicId);
		String toRange = map.get("to_range")==null?"":map.get("to_range").toString();
		String companyId = map.get("company_id")==null?"":map.get("company_id").toString();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		if("0".equals(toRange)){//全企业发送
			sql="select distinct mobile from tb_c_user_company where company_id=? and del_flag='0'";
			list=this.jdbcTemplate.queryForList(sql, companyId);
		}else if("1".equals(toRange)){//自选部门发送
			sql="select distinct mobile from tb_c_user_company uc,tb_c_user_department ud," +
			"tb_c_public_user pu where uc.user_company_id=ud.user_company_id and " +
			"ud.department_id=pu.department_id and uc.del_flag='0' and ud.visible_flag='1' " +
			"and pu.public_id=? and relate_type='1'";
			list=this.jdbcTemplate.queryForList(sql, publicId);
		}else if("2".equals(toRange)){//自选成员发送
			sql="select distinct mobile from tb_c_user_company uc," +
					"tb_c_public_user pu where uc.user_company_id=pu.user_company_id and uc.del_flag='0'" +
					" and pu.public_id=? and relate_type='1'";
			list=this.jdbcTemplate.queryForList(sql, publicId);
		}
		return list;
	}
	
	/**
	 * 获取公告
	 * @param rows
	 * @param page
	 * @param companyId
	 * @param status:0：新公告 1：草稿 2：历史
	 * @return
	 */
	public Pagination<?> getInfoList(int rows, int page,String companyId,String status,String userCompanyId ){
		Map<String, Object> map = new HashMap<String,Object>();
		String rowSql = "SELECT n.notice_id,pr.public_id, DATE_FORMAT(n.send_time,'%Y-%m-%d %H:%i') AS send_time,n.send_message_result," +
				"n.send_mms_result,uc.employee_name,n.notice_title,pr.public_name, DATE_FORMAT(n.save_time,'%Y-%m-%d %H:%i') AS save_time " +
				"FROM tb_c_notice n,tb_c_public_road pr,tb_c_user_company uc " +
				"WHERE pr.public_id=n.public_id AND n.user_company_id = uc.user_company_id AND n.status='"+status+"' " +
				"AND uc.del_flag='0' AND pr.company_id='"+companyId+"' AND n.user_company_id IN ("+userCompanyId+")"; 
		
		if("1".equals(status)){
			rowSql+="ORDER BY n.save_time DESC";
		}else{
			rowSql+="ORDER BY n.send_time DESC";
		}
		String countSql = "SELECT COUNT(*) " +
				"FROM tb_c_notice n,tb_c_public_road pr,tb_c_user_company uc " +
				"WHERE pr.public_id=n.public_id AND n.user_company_id = uc.user_company_id AND n.status='"+status+"' " +
				"AND uc.del_flag='0' AND pr.company_id='"+companyId+"' AND n.user_company_id IN ("+userCompanyId+")";
		int offset = (page - 1) * rows;
    	rowSql += " limit :offset, :limit";
		return this.namedParameterJdbcTemplateExt.queryPage(rowSql, countSql, offset, rows, map);
	}
	/**
	 * 删除公告消息
	 * @param noticeId
	 */
	public void deleteNotice(String noticeId){
		//删除与此公告相关联的信息
		String sql = "DELETE FROM tb_c_notice_note WHERE notice_id	='"+noticeId+"'";
		this.jdbcTemplate.update(sql);
		//删除公告
		sql = "DELETE FROM tb_c_notice WHERE notice_id	='"+noticeId+"'";
		this.jdbcTemplate.update(sql);
	}
	/**
	 * 按公告号删除
	 * @param publicId
	 */
	public void deleteByPublicId(String publicId){
		String sql = "DELETE FROM tb_c_notice WHERE public_id	='"+publicId+"'";
		this.jdbcTemplate.update(sql);
	}

}

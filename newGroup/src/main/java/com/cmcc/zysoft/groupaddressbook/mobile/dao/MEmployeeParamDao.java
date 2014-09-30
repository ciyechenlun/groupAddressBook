/**
 * CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */

package com.cmcc.zysoft.groupaddressbook.mobile.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.cmcc.zysoft.sellmanager.model.EmployeeParam;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author 袁凤建
 * @email yuan.fengjian@ustcinfo.com
 * @date 2013-6-8 下午3:17:31
 */

@Repository
public class MEmployeeParamDao extends HibernateBaseDaoImpl<EmployeeParam, String> {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 获取用户的身高体重
	 * @param userCode
	 * @param token
	 * @return
	 */
	public Map<String, Object> getHeightAndWeight(String userCode, String token) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "SELECT param.employee_height AS height, param.employee_weight AS weight" +
				" FROM tb_c_employee_param param" +
				" WHERE param.employee_id = ?";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, userCode);
		map.put("cmd", "getuserweightandheight");
		try {
			if(list.size()>0){
				map.put("code", "0");
				map.put("height", list.get(0).get("height").toString());
				map.put("weight", list.get(0).get("weight").toString());
			}
			else
			{
				map.put("code", "50");
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("code", "1");
		}
		return map;
	}
	
	/**
	 * 用户身高、体重、步长上传
	 * @param usercode
	 * @param height
	 * @param weight
	 * @param step
	 * @return
	 */
	public boolean updateUserHeightAndWeight(String usercode,String height,String weight,String step)
	{
		boolean hasHeight = false,hasWeight = false;
		String sql = "SELECT employee_id,employee_height,employee_weight,employyee_step,mark1,mark2 FROM tb_c_employee_param" +
				" WHERE employee_id=?";
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql, usercode);
		if(list.size()>0)
		{
			//update
			sql = "UPDATE tb_c_employee_param SET";
			if(StringUtils.hasText(height))
			{
				sql += " employee_height=" + height;
				hasHeight = true;
			}
			else{height = "175";}
			if(StringUtils.hasText(weight))
			{
				sql += hasHeight?",":"";
				sql += " employee_weight=" + weight;
				hasWeight = true;
			}
			else{weight = "65";}
			if(StringUtils.hasText(step))
			{
				sql += hasHeight || hasWeight ?",":"";
				sql += " employyee_step=" + step;
			}
			else{step = "60";}
			sql += " WHERE employee_id='"+usercode+"'";
			this.jdbcTemplate.execute(sql);
		}
		else
		{
			//insert
			sql = "INSERT INTO tb_c_employee_param(employee_id,employee_height,employee_weight,employyee_step) values " +
					"('" + usercode + "'," + height + "," + weight + "," + step + ")";
			this.jdbcTemplate.execute(sql);
		}
		return true;
	}
	
	/**
	 * 用户短信签名上传，使用mark1字段
	 * @param usercode
	 * @param sign
	 * @return
	 */
	public boolean updateUserSign(String usercode,String sign)
	{
		EmployeeParam ep = this.get(usercode);
		if(ep == null)
		{
			ep = new EmployeeParam();
			ep.setEmployeeId(usercode);
			ep.setMark1(sign);
			this.save(ep);
		}
		else
		{
			ep.setMark1(sign);
			this.update(ep);
		}
		return true;
	}
	
	/**
	 * 获取用户签名
	 * @param userCode
	 * @return
	 */
	public String getUserSign(String userCode)
	{
		EmployeeParam ep = this.get(userCode);
		return ep == null?"":ep.getMark1();
	}
}

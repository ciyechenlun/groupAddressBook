/**
 * @author AMCC
 * <br /> 邮箱:zhouyusgs@ahmobile.com
 * <br /> 描述:IntegrationDao.java
 * <br /> 版本：1.0.0
 * <br /> 日期：2013-12-17
 */
package com.cmcc.zysoft.groupaddressbook.mobile.dao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.Integration;
import com.cmcc.zysoft.sellmanager.util.UUIDUtil;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;
import com.starit.common.dao.jdbc.NamedParameterJdbcTemplateExt;
import com.starit.common.dao.support.Pagination;

/**
 * @author 周瑜
 *com.cmcc.zysoft.groupaddressbook.mobile.dao
 * 创建时间：2013-12-17
 */
@Repository
public class IntegrationDao extends HibernateBaseDaoImpl<Integration, String> {
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplateExt namedParameterJdbcTemplateExt;
	
	/**
	 * 积分列表
	 * @param rows
	 * @param page
	 * @return
	 */
	public Pagination<?> getIntegration(int rows,int page)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		String rowSql = "SELECT ig.*,emp.employee_name FROM tb_c_integration ig,tb_c_employee emp WHERE " +
				"ig.employee_id=emp.employee_id ORDER BY integration_value DESC";
		String countSql = "SELECT COUNT(integration_id) FROM tb_c_integration";
		int offset = (page - 1) * rows;
		rowSql += " limit :offset, :limit";
		return this.namedParameterJdbcTemplateExt.queryPage(rowSql,countSql,offset,rows,map);
	}
	
	/**
	 * 更新积分
	 * @return
	 */
	public String updateIntegration()
	{
		//上个月的积分未计算
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		//int day = cal.get(Calendar.DAY_OF_MONTH);
		int firstDayofMonth = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		int lastDayofMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		//计算上个月
		if(month==1)
		{
			month = 12;
			year = year - 1;
		}
		else
		{
			month = month - 1;
		}
		//上个月数据是否存在
		String pSql = "SELECT COUNT(integration_detail_id) AS count FROM tb_c_integration_detail WHERE " +
				"integration_detail_year="+year+" AND integration_detail_month=" + month;
		int count = this.jdbcTemplate.queryForInt(pSql);
		if(count>0)
		{
			return year + "年" + month + "月积分已计算";
		}
		String preMonthDate = year + "-" + month + "-" + firstDayofMonth;
		String lastDay = year + "-" + month + "-" + lastDayofMonth;
		String sql = "SELECT employee_report_id,employee_id,current_rank FROM tb_c_employee_report WHERE movement_id IN " +
				"(SELECT movement_id FROM tb_b_movement WHERE " +
				"movement_end_date<='"+lastDay+" 23:59:59' AND movement_start_date>='"+preMonthDate+"' AND " +
				" movement_flag='0')";
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql);
		for(Map<String,Object> map : list)
		{
			int integration = this.getUserIntegration(Integer.parseInt(map.get("current_rank").toString()));
			//加入积分明细
			String uuid = UUIDUtil.generateUUID();
			String insertSql = "INSERT INTO tb_c_integration_detail (integration_detail_id,integration_detail_year,integration_detail_month,employee_id,integration_detail_value)" +
					" VAlUES ('"+uuid+"','"+year+"','"+month+"','"+map.get("employee_id").toString()+"','"+integration+"')";
			this.jdbcTemplate.execute(insertSql);
			String updateSql = "UPDATE tb_c_employee_report SET sport_level="+integration+" WHERE " +
					"employee_report_id='"+map.get("employee_report_id").toString()+"'";
			//更新运动统计表
			this.jdbcTemplate.execute(updateSql);
			//更新积分表
			sql = "SELECT integration_id FROM tb_c_integration WHERE employee_id=?";
			List<Map<String,Object>> iList = this.jdbcTemplate.queryForList(sql, map.get("employee_id").toString());
			if(iList.size()>0)
			{
				sql="UPDATE tb_c_integration SET integration_value=integration_value + " + integration + " WHERE " +
						"employee_id='" + map.get("employee_id").toString() + "'";
				this.jdbcTemplate.execute(sql);
			}
			else
			{
				Integration ig = new Integration();
				ig.setEmployeeId(map.get("employee_id").toString());
				ig.setIntegrationValue(integration);
				this.saveOrUpdate(ig);
			}
		}
		return "SUCCESS";
	}
	
	/**
	 * 根据用户排名返回用户积分。没看到规律，先写死
	 * @param rank
	 * @return
	 */
	private int getUserIntegration(int rank)
	{
		int integration = 0;
		switch(rank)
		{
			case 1:
				integration = 500;
				break;
			case 2:
				integration = 375;
				break;
			case 3:
				integration = 300;
				break;
			case 4:
				integration = 275;
				break;
			case 5:
				integration = 250;
				break;
			case 6:
				integration = 225;
				break;
			case 7:
				integration = 200;
				break;
			case 8:
				integration = 175;
				break;
			case 9:
				integration = 150;
				break;
			case 10:
				integration = 125;
				break;
			case 11:
				integration = 116;
				break;
			case 12:
				integration = 107;
				break;
			case 13:
				integration = 99;
				break;
			case 14:
				integration = 91;
				break;
			case 15:
				integration = 83;
				break;
			case 16:
				integration = 75;
				break;
			case 17:
				integration = 73;
				break;
			case 18:
				integration = 71;
				break;
			case 19:
				integration = 69;
				break;
			case 20:
				integration = 67;
				break;
			case 21:
				integration = 65;
				break;
			case 22:
				integration = 63;
				break;
			case 23:
				integration = 61;
				break;
			case 24:
				integration = 58;
				break;
			case 25:
				integration = 57;
				break;
			case 26:
				integration = 55;
				break;
			case 27:
				integration = 53;
				break;
			case 28:
				integration = 51;
				break;
			case 29:
				integration = 49;
				break;
			case 30:
				integration = 47;
				break;
			case 31:
				integration = 45;
				break;
			case 32:
				integration = 43;
				break;
			case 33:
				integration = 41;
				break;
			case 34:
				integration = 39;
				break;
			case 35:
				integration = 37;
				break;
			case 36:
				integration = 35;
				break;
			case 37:
				integration = 33;
				break;
			case 38:
				integration = 31;
				break;
			case 39:
				integration = 29;
				break;
			case 40:
				integration = 27;
				break;
			case 41:
				integration = 25;
				break;
			case 42:
				integration = 23;
				break;
			case 43:
				integration = 22;
				break;
			case 44:
				integration = 21;
				break;
			case 45:
				integration = 20;
				break;
			case 46:
				integration = 19;
				break;
			case 47:
				integration = 18;
				break;
			case 48:
				integration = 17;
				break;
			case 49:
				integration = 16;
				break;
			case 50:
				integration = 15;
				break;
		}
		if(rank>50 && rank<=60)
		{
			integration = 13;
		}
		else if(rank>60 && rank<=80)
		{
			integration = 10;
		}
		else if(rank>80 && rank<=100)
		{
			integration = 7;
		}
		else if(rank>100)
		{
			integration = 5;
		}
		
		return integration;
	}
}

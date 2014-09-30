// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.InterfaceLog;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;
import com.starit.common.dao.jdbc.NamedParameterJdbcTemplateExt;
import com.starit.common.dao.support.Pagination;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：InterfaceLogDao
 * <br />版本:1.0.0
 * <br />日期： 2013-4-12 下午3:25:16
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Repository
public class InterfaceLogDao extends HibernateBaseDaoImpl<InterfaceLog, Long>{
	
	@Autowired
	private NamedParameterJdbcTemplateExt namedParameterJdbcTemplateExt;
	
	/**
	 * 接口日志列表,可根据时间查询.
	 * @param rows 
	 * @param page 
	 * @param companyId 
	 * @param key 关键字
	 * @return 
	 * 返回类型：Pagination<?>
	 */
	public Pagination<?> logList(int rows, int page, String companyId, String key){
		Map<String, Object> map = new HashMap<String,Object>();
		String rowSql = "SELECT " +
				"inter.*," +
				"DATE_FORMAT(inter.operate_time,'%Y-%m-%d %H:%i:%s') AS operateTime, " +
				"emp.employee_name " +
				"FROM tb_c_interface_log inter,tb_c_employee emp " +
				"WHERE emp.employee_id=inter.operate_man " +
				"AND inter.company_id='"+companyId+"' ";
		String countSql = "SELECT COUNT(*) " +
				"FROM tb_c_interface_log inter,tb_c_employee emp " +
				"WHERE emp.employee_id=inter.operate_man " +
				"AND inter.company_id='"+companyId+"' ";
		rowSql += "ORDER BY inter.operate_time desc ";
		int offset = (page - 1) * rows;
    	rowSql += " limit :offset, :limit";
		return this.namedParameterJdbcTemplateExt.queryPage(rowSql, countSql, offset, rows, map);
	}

}

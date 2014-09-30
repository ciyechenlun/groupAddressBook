// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.Advise;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;
import com.starit.common.dao.jdbc.NamedParameterJdbcTemplateExt;
import com.starit.common.dao.support.Pagination;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：AdviseDao
 * <br />版本:1.0.0
 * <br />日期： 2013-4-10 上午10:09:16
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Repository
public class AdviseDao extends HibernateBaseDaoImpl<Advise, String>{
	
	@Autowired
	private NamedParameterJdbcTemplateExt namedParameterJdbcTemplateExt;
	
	/**
	 * 分页获取反馈建议列表.
	 * @param rows 
	 * @param page 
	 * @param companyId 
	 * @return 
	 * 返回类型：Pagination<?>
	 */
	public Pagination<?> adviseList(int rows, int page, String companyId){
		Map<String, Object> map = new HashMap<String,Object>();
		String rowSql = "SELECT " +
				"emp.employee_name AS adviseMan," +
				"ad.advise_id AS adviseId," +
				"ad.content AS content," +
				"cmp.company_name as company_name," +
				"DATE_FORMAT(ad.advise_date,'%Y-%m-%d %H:%i:%s') AS adviseDate ," +
				"ad.remark as remark,"+
				"emp.mobile as mobile "+
				"FROM tb_c_advise ad,tb_c_employee emp,tb_c_company cmp " +
				"WHERE emp.employee_id=ad.advise_man AND cmp.company_id=ad.company_id " +
				"ORDER BY ad.advise_date desc ";
		String countSql = "SELECT COUNT(*) " +
				"FROM tb_c_advise ad,tb_c_employee emp " +
				"WHERE emp.employee_id=ad.advise_man";
		int offset = (page - 1) * rows;
    	rowSql += " limit :offset, :limit";
    	return this.namedParameterJdbcTemplateExt.queryPage(rowSql, countSql, offset, rows, map);
	}

}

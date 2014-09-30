/**
 * @author AMCC
 * <br /> 邮箱:zhouyusgs@ahmobile.com
 * <br /> 描述:MedalDao.java
 * <br /> 版本：1.0.0
 * <br /> 日期：2013-12-8
 */
package com.cmcc.zysoft.groupaddressbook.mobile.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.MedalSys;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author 周瑜
 *com.cmcc.zysoft.groupaddressbook.mobile.dao
 * 创建时间：2013-12-8
 */
@Repository
public class MedalSysDao extends HibernateBaseDaoImpl<MedalSys, String> {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
}

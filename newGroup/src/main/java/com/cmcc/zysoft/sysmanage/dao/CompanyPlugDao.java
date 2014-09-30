package com.cmcc.zysoft.sysmanage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.CompanyPlug;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author liyuchen
 * @date
 */
@Repository
public class CompanyPlugDao extends HibernateBaseDaoImpl<CompanyPlug, String>{

	@Autowired
	private JdbcTemplate jdbcTemplate;
}

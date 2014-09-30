// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.dao;

import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.MetaData;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;
import com.starit.common.dao.support.Pagination;

/**
 * @author yandou
 */
@Repository
public class MetaDataDao extends HibernateBaseDaoImpl<MetaData, String> {

	/**
	 * 元数据分页
	 * @param typeId
	 * @param page
	 * @param rows
	 * @return
	 */
	public Pagination<Object> getAllMetaDatas(String typeId, int page, int rows) {
		String rowSql = "from MetaData md where md.typeId = :typeId";
		String countSql = "select count(*) from MetaData md where md.typeId = :typeId";
		int offset = (page-1)*rows;
		return this.findPageByHQL(rowSql, countSql, offset, rows, "typeId", typeId);
	}
	
}

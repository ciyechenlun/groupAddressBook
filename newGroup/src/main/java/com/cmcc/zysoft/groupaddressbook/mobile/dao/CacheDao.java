/**
 * @author AMCC
 * <br /> 邮箱:zhouyusgs@ahmobile.com
 * <br /> 描述:CacheDao.java
 * <br /> 版本：1.0.0
 * <br /> 日期：2013-10-22
 */
package com.cmcc.zysoft.groupaddressbook.mobile.dao;

import org.springframework.stereotype.Repository;

import com.cmcc.zysoft.sellmanager.model.UCache;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;

/**
 * @author 周瑜
 *com.cmcc.zysoft.groupaddressbook.mobile.dao
 * 创建时间：2013-10-22
 */

@Repository
public class CacheDao extends HibernateBaseDaoImpl<UCache, String> {

}

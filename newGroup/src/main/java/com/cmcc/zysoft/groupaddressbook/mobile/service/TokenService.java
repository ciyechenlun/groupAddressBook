// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.mobile.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmcc.zysoft.groupaddressbook.mobile.dao.TokenDao;
import com.cmcc.zysoft.sellmanager.model.Token;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 李梦华
 * <br />邮箱： li.menghua@ustcinfo.com
 * <br />描述：TokenService
 * <br />版本:1.0.0
 * <br />日期： 2013-3-6 上午9:57:13
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class TokenService extends BaseServiceImpl<Token, String>{
	
	@Resource
	private TokenDao tokenDao;
	
	@Override
	public HibernateBaseDao<Token, String>getHibernateBaseDao(){
		return this.tokenDao;
	}
	
	
	/**
	 * 登陆用户生成令牌记录.
	 * @param empId 员工Id
	 * @param tokenValue 令牌值
	 * @param tokenSalt 令牌盐
	 * @param createTime 生成时间
	 * @throws ParseException 
	 * 返回类型：void
	 */
	@Transactional
	public void add(String empId,String tokenValue,String tokenSalt,String createTime) throws ParseException{
		//生成新令牌之前删掉以前的旧令牌
		this.tokenDao.deleteToken(empId);
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date cearte_time = sd.parse(createTime);
		long value_date = cearte_time.getTime() + 3600000;
		Token token = new Token();
		token.setEmployeeId(empId);
		token.setTokenValue(tokenValue);
		token.setPassSalt(tokenSalt);
		token.setAddDate(cearte_time);
		Date tokenDate = new Date(value_date);
		token.setTokenDate(tokenDate);
		this.tokenDao.save(token);
	}
	
	/**
	 * 通过员工Id,获取登陆令牌.
	 * @param empId 
	 * @return 
	 * 返回类型：Token
	 */
	public Token getToken(String empId){
		List<Token> list = this.findByNamedParam("employeeId", empId);
		return list.get(0);
	}

}

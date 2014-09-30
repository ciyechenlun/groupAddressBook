// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.dao.NoticeDao;
import com.cmcc.zysoft.sellmanager.model.Notice;
import com.cmcc.zysoft.sellmanager.model.UserCompany;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
import com.starit.common.dao.support.Pagination;

/**
 * @author 张军
 * <br />邮箱： zhang.jun3@ustcinfo.com
 * <br />描述：NoticeService
 * <br />版本:1.0.0
 * <br />日期： 2014-4-10 上午10:22:40
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class NoticeService extends BaseServiceImpl<Notice, String>{
	
	@Resource
	private NoticeDao noticeDao;
	
	@Override
	public HibernateBaseDao<Notice, String> getHibernateBaseDao(){
		return this.noticeDao;
	}
	public List<Map<String,Object>> getPublicRoadList(String userId,String companyId){
		return this.noticeDao.getPublicRoadList(userId, companyId);
	}
	/**
	 * 获取公告历史
	 * @param rows
	 * @param page
	 * @param companyId
	 * @return
	 */
	public Pagination<?> getHistoryList(int rows,int page,String companyId,List<UserCompany> userCompanyList){
		String userCompanyId = this.userCompanyIdStr(userCompanyList);
		return this.noticeDao.getInfoList(rows, page, companyId,"2",userCompanyId);
	}
	/**
	 * 删除公告
	 * @param noticeId
	 */
	public void deleteNoticeInfo(String noticeId){
		this.noticeDao.deleteNotice(noticeId);
	}
	/**
	 * 获取草稿箱列表
	 * @param rows
	 * @param page
	 * @param companyId
	 * @return
	 */
	public Pagination<?> getDraftboxList(int rows,int page,String companyId,List<UserCompany> userCompanyList){
		String userCompanyId = this.userCompanyIdStr(userCompanyList);
		return this.noticeDao.getInfoList(rows, page, companyId,"1",userCompanyId);
	}
	
	public List<Map<String,Object>> getMobileList(String publicId){
		return this.noticeDao.getMobileList(publicId);
	}
	/**
	 * 获取userCompanyId字符串
	 * @param list
	 * @return
	 */
	private String userCompanyIdStr(List<UserCompany> list){
		String result = "";
		if(list!=null&&!list.isEmpty()){
			for(UserCompany userCompany :list){
				result +="'"+userCompany.getUserCompanyId()+ "',";
			}
			result = result.substring(0, result.length()-1);
		}
		return result;
	}


}

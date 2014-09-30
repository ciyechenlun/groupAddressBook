// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.cmcc.zysoft.groupaddressbook.dao.NoticeNoteDao;
import com.cmcc.zysoft.sellmanager.model.NoticeNote;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author 张军
 * <br />邮箱： zhang.jun3@ustcinfo.com
 * <br />描述：NoticeNoteService
 * <br />版本:1.0.0
 * <br />日期： 2014-4-10 上午10:22:40
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class NoticeNoteService extends BaseServiceImpl<NoticeNote, String>{
	
	@Resource
	private NoticeNoteDao noticeNoteDao;
	
	@Override
	public HibernateBaseDao<NoticeNote, String> getHibernateBaseDao(){
		return this.noticeNoteDao;
	}
	public void batchAddNote(List<Map<String,Object>> list,String noticeId){
		 this.noticeNoteDao.batchAddNote(list,noticeId);
	}
	
	public void updateByMobile(String noticeId,String[] tels,String sendType,String serialNumber,String result){
		 this.noticeNoteDao.updateByMobile(noticeId,tels,sendType,serialNumber,result);
	}
	public void updateImResult(final String noticeId,final JSONArray ims,final String sendType,final String result){
		 this.noticeNoteDao.updateImResult(noticeId,ims,sendType,result);
	}
	/**
	 * 获取发送失败的手机号
	 * @param noticeId 消息id
	 * @return 手机号列表
	 */
	public List<Map<String,Object>> getFaileMobile(){
		return this.noticeNoteDao.getFaileMobile();
	}
}

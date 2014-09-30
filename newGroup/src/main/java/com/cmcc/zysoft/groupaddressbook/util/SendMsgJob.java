package com.cmcc.zysoft.groupaddressbook.util;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.HttpException;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.zysoft.HttpClientUtil.HttpClientUtil;
import com.cmcc.zysoft.groupaddressbook.service.NoticeNoteService;
import com.cmcc.zysoft.groupaddressbook.service.NoticeService;
import com.cmcc.zysoft.sellmanager.model.Notice;
/**
 * 定时发送信息
 * 	对发送公告消息未成功的且发送次数小于三次的重新发送公告
 * 发送时间：8:00-22:00
 * @author liyuchen
 *
 */
public class SendMsgJob {
	
	@Resource
	private NoticeService noticeService;
	@Resource
	private NoticeNoteService noticeNoteService;
	
	private final static String URL = "http://120.209.139.133:8080/notice.jsp";
	
	
	
	public void sendMsgByTime(){
		
		List<Map<String,Object>> list = this.noticeNoteService.getFaileMobile();
		for(Map<String,Object> map:list){
			String noticeId = String.valueOf(map.get("notice_id"));
			String mobiles = String.valueOf(map.get("mobile"));
			if(StringUtils.hasText(mobiles)&&StringUtils.hasText(noticeId)){
				String[] mobileList = mobiles.split(",");
				autoSendMessage(noticeId,mobileList);
			}
			
		}
	}
	
	public void autoSendMessage(String noticeId,String[] list) {
		final Notice notice = this.noticeService.getEntity(noticeId);
		String desPath = notice.getUrl();
		System.out.println("#####"+desPath+"####");
		if(desPath!=null&&StringUtils.hasText(desPath)){
			int poolSize = 1;
			ThreadPool threadPool = new ThreadPool(poolSize);
			threadPool.execute(runTask(list,notice,desPath));
			threadPool.waitFinish();
			threadPool.closePool();
		}
		
	}
	/**
	 * 调用接口发送消息
	 * @param list
	 * @param notice
	 * @return
	 */
	private Runnable runTask(final String[] list,final Notice notice,final String url)
	{
		return new Runnable() {
			
			@Override
			public void run() {
				send(list,notice,url);
			}
		};
	}
	private void send( String[] list, Notice notice,String url){
		JSONObject obj = new JSONObject();
		obj.put("title", notice.getNoticeTitle());
		obj.put("body", notice.getNoticeContent());
		obj.put("group", notice.getPublicId());
		obj.put("url", url);
		int size=list.length;
		String mobiles="";
		for(int i=1;i<=size;i++){
			mobiles += list[i-1]+",";
			if((i > 0 && i%100 == 0) || i == size)
			{
				String flag = mobiles.substring(0, mobiles.length()-1);
				try {
					String result =HttpClientUtil.post(URL,flag,obj.toJSONString(),"1");
					JSONObject json = JSONObject.parseObject(result);
//					JSONObject mms = json.getJSONObject("mms");
					JSONArray mms = json.getJSONArray("mms");
					if(null != mms){
//						String mmssn = mms.getString("sn");
//						String mmstel = mms.getString("tel");
//						String[] mmstels = mmstel.split(",");
//						this.noticeNoteService.updateByMobile(notice.getNoticeId(),mmstels, "1", mmssn, "0");
						this.noticeNoteService.updateImResult(notice.getNoticeId(),mms,"1","0");
					}
					JSONArray ims = json.getJSONArray("im");
					if(null !=ims){
						this.noticeNoteService.updateImResult(notice.getNoticeId(),ims,"0","0");
					}
					String faile = json.getString("faile");
					if(null != faile){
						String[] failtels = faile.split(",");
						this.noticeNoteService.updateByMobile(notice.getNoticeId(),failtels, "", "", "1");
					}
					mobiles ="";
				} catch (HttpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}

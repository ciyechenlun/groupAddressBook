package com.cmcc.zysoft.groupaddressbook.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.zysoft.HttpClientUtil.HttpClientUtil;
import com.cmcc.zysoft.groupaddressbook.service.UserCompanyService;
import com.cmcc.zysoft.groupaddressbook.webservice.SIInterfaceForADCSoap.MobileShortICTClient;

public class Job {
	@Resource
	private UserCompanyService userCompanyService;
	/**
	 * 日志
	 */
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	private final  String URL1 = "http://10.152.89.207:8080/short/getMobileShorts";
	/**
	 * 执行次数
	 */
	private int number=0;
    /**
     *  Function:更新索引内容
     *  @author JLC
     */
    //@Scheduled(fixedDelay = 5000)  // 5秒更新一次
   // @Scheduled(cron = "0 0/1 * * * ?")  //一分钟更新一次
    public void rehreshShortNum(){
    	logger.debug("###### start ###### "+number);
    	if(number<10){
	        List<Map<String,Object>> alllist = userCompanyService.getUserCompanyNoShort(number);
	        List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();
	        int size = alllist.size();
	        int poolSize = size%500==0?size/500:size/500+1;
			 ThreadPool threadPool = new ThreadPool(poolSize);
	        for(int i = 0 ;i <size ; i++){
	       	 list.add(alllist.get(i));
	       	 if((i > 0 && i%500 == 0) || i == size-1)
					{
						threadPool.execute(importTask(list));
						list = new ArrayList<Map<String,Object>>();
					}
	        }
			threadPool.waitFinish();
			threadPool.closePool();
			number++;
    	}
		logger.debug("###### end ######");
   }
	/**
	 * 获取短号信息.
	 * @return map
	 * {"response":{"bizCode":"SI905","transID":"SI20131213000000000001","actionCode":2,"timeStamp":"201312131527530701","testFlag":0,"dealkind":1,"priority":1,"version":"V1.0","resultCode":"0","resultMsg":"success","svcCont":"<ShortNumResponse><OprResult><BizRegRsp><OrdSeq>20131213095050001</OrdSeq><Rslt>00</Rslt><RealInfo><PhoneNo>15156892727</PhoneNo><ShortNum>662727</ShortNum><GroupCode>5510000139</GroupCode></RealInfo></BizRegRsp></OprResult></ShortNumResponse>","siappID":"1"},"success":true}
	 * @throws HttpException 
	 */
	public void getShort(List<Map<String,Object>> ucList) throws HttpException {
		logger.debug("##########  getShort  开始!  ##########");
		String mobiles="";
		int size = ucList.size();
		for(int i = 0 ;i <size ; i++){
			mobiles += ucList.get(i)+",";
	       	 if((i > 0 && (i+1)%5 == 0) || i == size-1){
	       		String flag = mobiles.substring(0, mobiles.length()-1);
				String result = HttpClientUtil.postForShort(URL1,flag);
				JSONArray list = JSONObject.parseArray(result);
				if(null != list){
					for (int j=0;j<list.size();j++) {
						JSONObject obj =list.getJSONObject(j);
						if(null != obj && !obj.isEmpty()){
							this.userCompanyService.updateShortNumVIdByMobile(obj.get("PhoneNo").toString(),obj.get("ShortNum").toString(),obj.get("GroupCode").toString());
						}
					}
				}
				mobiles ="";
	       	 }
		}
		
		logger.debug("##########  getShort  结束!  ##########");
	}
	/**
	 * 多线程导入
	 * factRows实际的行号，用来做部门的排序
	 */
	private Runnable importTask(final List<Map<String,Object>> ucList)
	{
		return new Runnable() {
			
			@Override
			public void run() {
				try {
					getShort(ucList);
				} catch (HttpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}
}

// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.zysoft.HttpClientUtil.HttpClientUtil;
import com.cmcc.zysoft.groupaddressbook.service.NoticeNoteService;
import com.cmcc.zysoft.groupaddressbook.service.NoticeService;
import com.cmcc.zysoft.groupaddressbook.service.PCCompanyService;
import com.cmcc.zysoft.groupaddressbook.service.PublicRoadService;
import com.cmcc.zysoft.groupaddressbook.util.ThreadPool;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.Notice;
import com.cmcc.zysoft.sellmanager.model.PublicRoad;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author 张军
 * <br />邮箱： zhang.jun3@ustcinfo.com
 * <br />描述：AdviseController
 * <br />版本:1.0.0
 * <br />日期： 2014-4-10 上午10:25:32
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Controller
@RequestMapping("/pc/notice")
public class NoticeController extends BaseController{
	private static Logger _logger = LoggerFactory.getLogger(NoticeController.class); 
	@Resource
	private NoticeService noticeService;
	@Resource
	private NoticeNoteService noticeNoteService;
	@Resource
	private PCCompanyService pcCompanyService;
	@Resource
	private PublicRoadService publicRoadService;
	
	@Value("${upload.file.path}")
	private String path;
	
	private final static String URL = "http://120.209.139.133:8080/notice.jsp";
	
	/**
	 * 跳转到查看建议反馈页面.
	 * @param modelMap 
	 * @param search 
	 * @return 
	 * 返回类型：String
	 */
	@RequestMapping(value="/main.htm")
	public String getAdvise(ModelMap modelMap,HttpServletRequest request,String publicId,String noticeId){
		User user = SecurityContextUtil.getCurrentUser();
		Company company = null;
    	if(request.getSession().getAttribute("selCompany")!=null)
    	{
    		company = (Company)request.getSession().getAttribute("selCompany");
    	}else{
    		company = this.pcCompanyService.getEntity(user.getCompanyId());
    	}
		List<Map<String,Object>> list= this.noticeService.getPublicRoadList(user.getUserId(), company.getCompanyId());
		
		modelMap.put("publicRoads", list);
		modelMap.put("publicId", publicId);
		if(StringUtils.hasText(noticeId)){
			Notice notice = noticeService.getEntity(noticeId);
			modelMap.put("noticeContent", notice.getNoticeContent());
			modelMap.put("noticeTitle", notice.getNoticeTitle());
			DateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
			String reTime = format.format(new Date());
			modelMap.put("sendTime", reTime);
			modelMap.put("noticeId", noticeId);
			modelMap.put("publicId", notice.getPublicId());
			modelMap.put("userCompanyId", notice.getUserCompanyId());
		}
		return "web/noticeSec";
	}
	/**
	 * 显示公告号图片
	 * @param fileCode
	 * @param suffix
	 * @param request
	 * @param response
	 */
	@RequestMapping("/images/{fileCode}.{suffix}")
	public void showPublicImage(@PathVariable String fileCode, @PathVariable String suffix, HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setHeader("Content-Type", "application/octet-stream");
			File file = null;
			//如果没有上传附件，默认处理方式，分图片和文件两种方式
			// 获取文件
			file = new File(path +File.separator+ fileCode +"."+ suffix);
			if(file.canRead()) {
				FileUtils.copyFile(file, response.getOutputStream());
			} else {
				file = new File(request.getSession().getServletContext().getRealPath("")+"/resources/images/img1.jpg");
				FileUtils.copyFile(file, response.getOutputStream());
				System.out.println(file);
			}
		} catch (IOException e) {
			_logger.error(e.getMessage());
		}
	}
	
	
	/**
	 * 检查信息：每个公告号每天默认发送上限为10，发送频率为30时钟
	 * @param publicId
	 * @return
	 */
	@RequestMapping("/checkInfo.htm")
	@ResponseBody
	public String checkInfo(String publicId){
		String result = "00";
		List<PublicRoad> publicRoadList = publicRoadService.findByNamedParam("publicId", publicId);
		//获取公告号
		PublicRoad publicRoad = publicRoadList.get(0);
		List<Notice> noticeList = noticeService.findByNamedParam(new String[]{"publicId","status"}, new Object[]{publicId,"2"});
		//检查是否超过设定的发送次数
		if(noticeList.size()>=publicRoad.getCaps()){
			result = "01";
		}else{
			//检查时候超过设定的发送频率
			for(Notice notice:noticeList){
				Date date = notice.getSendTime();
				date.setTime(date.getTime() + publicRoad.getFrequency()*60*1000);
				if(date.after(new Date())){
					result = "02";
					break;
				}
			}
		}
		
		return result;
	}
	
	
	
	
	
	@RequestMapping(value="/newNoticeMain.htm")
	public String newNoticeMain(ModelMap modelMap,String publicId,String userCompanyId,String noticeId,HttpServletRequest request){
		
		if(noticeId==null||!StringUtils.hasText(noticeId)){
			modelMap.put("publicId", publicId);
			modelMap.put("userCompanyId", userCompanyId);
			modelMap.put("noticeTitle", "请输入标题");
		}else{
			Notice notice = noticeService.getEntity(noticeId);
			modelMap.put("noticeContent", notice.getNoticeContent());
			modelMap.put("noticeTitle", notice.getNoticeTitle());
			modelMap.put("noticeId", noticeId);
			modelMap.put("publicId", notice.getPublicId());
			modelMap.put("userCompanyId", notice.getUserCompanyId());
		}
		
		return "web/noticeSec";
	}
	@RequestMapping(value="/imgUpload.htm")
	@ResponseBody
 	public Map<String,Object> imgUpload(@RequestParam("imgFile") MultipartFile imgFile,HttpServletRequest request){
		String filename = imgFile.getOriginalFilename();
        String extName = filename.substring(filename.lastIndexOf(".")+1).toLowerCase();
        Map<String,Object> result=new HashMap<String,Object>();
        if(extName.matches("gif|jpg|jpeg|png|bmp")){
        	String lastFileName = System.currentTimeMillis()+"."+extName;
        	String fileFullPath = path + File.separator + lastFileName;
        	File uploadfile = new File(fileFullPath);
        	try {
				FileCopyUtils.copy(imgFile.getBytes(),uploadfile);
				result.put("error", 0);
				result.put("url", fileFullPath);
			} catch (IOException e) {
				e.printStackTrace();
				result.put("error", 1);
				result.put("message", e.getMessage());
			}
        }else{
        	result.put("error", 1);
			result.put("message", "导入格式错误");
        }
        return result;
	}
	@RequestMapping(value="/saveDraft.htm")
	@ResponseBody
	public String saveDraft(ModelMap modelMap,Notice notice,HttpServletRequest request){
		String noticeId = notice.getNoticeId();
		if(noticeId!=null&&StringUtils.hasText(noticeId)){
			notice.setStatus("1");
			notice.setSaveTime(new Date());
			Notice notice1 = noticeService.getEntity(noticeId);
			if("2".equals(notice1.getStatus())){//历史公告
				noticeId = this.noticeService.insertEntity(notice);
			}else{
				noticeService.updateEntity(notice);
			}
		}else{
			notice.setStatus("1");	
			notice.setSaveTime(new Date());
			noticeId = this.noticeService.insertEntity(notice);
		}
		return noticeId;
	}
	@RequestMapping(value="/sendMsg.htm")
	@ResponseBody
	public String sendMsg(ModelMap modelMap,Notice notice,HttpServletRequest request){
		notice.setStatus("2");
		notice.setSendTime(new Date());
		//生成彩信文件 获得路径
		String desPath = makeHtml(notice,request);
		notice.setUrl(desPath);
		String noticeId = notice.getNoticeId();
		if(!StringUtils.hasText(noticeId)){
			noticeId = this.noticeService.insertEntity(notice);
		}else{
			Notice notice1 = noticeService.getEntity(noticeId);
			if("2".equals(notice1.getStatus())){//历史公告
				noticeId = this.noticeService.insertEntity(notice);
			}else{
				noticeService.updateEntity(notice);
			}
		}
		List<Map<String,Object>> list = this.noticeService.getMobileList(notice.getPublicId());
		this.noticeNoteService.batchAddNote(list, noticeId);
		return noticeId;
	}
	public String makeHtml(Notice notice,HttpServletRequest request){
		String realPath=request.getSession().getServletContext().getRealPath("");
		String temPath=realPath+"/resources/template/";
		String desPath =realPath+ "/attached/html/";
		String desFile = System.currentTimeMillis()+".html";
		String desIntactPath= desPath+desFile;
		File file = new File (desIntactPath);
		if(!file.exists())
		{
		   try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		String reqUrl = request.getScheme()+"://"+request.getHeader("host")+"/attached/html/"+desFile;
		 Configuration cfg = new Configuration();
		 Writer out =null;
		try {
			cfg.setDirectoryForTemplateLoading(new File(temPath));
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			Template newsTemplate = cfg.getTemplate("template.html");
			newsTemplate.setEncoding("UTF-8");
			Map<String, Notice> root = new HashMap<String, Notice>();
			 DateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
			 String reTime = format.format(notice.getSendTime());
			 notice.setMark1(reTime);
			root.put("notice", notice);
			out = new OutputStreamWriter(new FileOutputStream(desIntactPath));
			newsTemplate.process(root, out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			 try {
				out.flush();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return reqUrl;
	} 
	@RequestMapping(value="/autoSendMessage.htm")
	public void autoSendMessage(String noticeId,HttpServletRequest request) {
		Notice notice = this.noticeService.getEntity(noticeId);
		List<Map<String,Object>> list = this.noticeService.getMobileList(notice.getPublicId());
		//this.noticeNoteService.batchAddNote(list, noticeId);
//		String desPath = makeHtml(notice,request);
		String desPath = notice.getUrl();
		System.out.println("#####"+desPath+"####");
		int poolSize = 1;
		ThreadPool threadPool = new ThreadPool(poolSize);
		threadPool.execute(runTask(list,notice,desPath));
		threadPool.waitFinish();
		threadPool.closePool();
	}
	/**
	 * 调用接口发送消息
	 * @param list
	 * @param notice
	 * @return
	 */
	private Runnable runTask(final List<Map<String,Object>> list,final Notice notice,final String url)
	{
		return new Runnable() {
			
			@Override
			public void run() {
				send(list,notice,url);
			}
		};
	}
	private void send( List<Map<String,Object>> list, Notice notice,String url){
		JSONObject obj = new JSONObject();
		obj.put("title", notice.getNoticeTitle());
		obj.put("body", notice.getNoticeContent());
		obj.put("group", notice.getPublicId());
		obj.put("url", url);
		int size=list.size();
		String mobiles="";
		for(int i=1;i<=size;i++){
			mobiles += list.get(i-1).get("mobile").toString()+",";
			if((i > 0 && i%100 == 0) || i == size)
			{
				String flag = mobiles.substring(0, mobiles.length()-1);
				try {
					String result =HttpClientUtil.post(URL,flag,obj.toJSONString(),"0");
					_logger.info("####"+result);
					JSONObject json = JSONObject.parseObject(result);
					JSONArray mms = json.getJSONArray("mms");
					if(null != mms){
						//String mmssn = mms.getString("sn");
						//String mmstel = mms.getString("tel");
						//String[] mmstels = mmstel.split(",");
						//this.noticeNoteService.updateByMobile(notice.getNoticeId(),mmstels, "1", mmssn, "0");
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

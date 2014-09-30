// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cmcc.zysoft.groupaddressbook.dao.NoticeDao;
import com.cmcc.zysoft.groupaddressbook.dao.NoticeNoteDao;
import com.cmcc.zysoft.groupaddressbook.dao.PublicRoadDao;
import com.cmcc.zysoft.sellmanager.model.Notice;
import com.cmcc.zysoft.sellmanager.model.PublicRoad;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
import com.starit.common.dao.support.Pagination;

/**
 * @author 张军
 * <br />邮箱： zhang.jun3@ustcinfo.com
 * <br />描述：PublicRoadService
 * <br />版本:1.0.0
 * <br />日期： 2014-4-10 上午10:22:40
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Service
public class PublicRoadService extends BaseServiceImpl<PublicRoad, String>{
	
	@Resource
	private PublicRoadDao publicRoadDao;
	
	@Resource
	private NoticeNoteDao noticeNoteDao;
	@Resource
	private NoticeDao noticeDao;
	
	@Resource
	private NoticeService noticeService;
	
	@Value("${upload.file.path}")
	private String path;
	
	@Override
	public HibernateBaseDao<PublicRoad, String>getHibernateBaseDao(){
		return this.publicRoadDao;
	}
	public List<Map<String, Object>> deptUserTree(String parentDeptId,String companyId){
		return this.publicRoadDao.deptUserTree(parentDeptId, companyId);
	}
	/**
	 * 获取企业公告
	 * @param rows 每页行数
	 * @param page  页数
	 * @param companyId 企业ID
	 * @return 
	 */
	public Pagination<?> list(int rows, int page, String companyId,String manager,String userId){
		return this.publicRoadDao.getManagerList(rows, page, companyId,manager,userId);
	}
	/**
	 *变更公告状态
	 * @param publicId 公告Id
	 * @param status 状态
	 */
	public void changeStatus(String publicId,String status){
		publicRoadDao.updateStatus(publicId, status);
	}
	/**
	 * 删除公告
	 * @param flag:true:公告号以及其关联信息全部删除（删除操作）；false：删除public_user的信息（编辑操作）
	 * @param publicId 公告Id
	 */
	public void deleteRoad(boolean flag,String publicId){
		if(flag){
			//删除公告号下的消息以及关联信息
			List<Notice> notices = this.noticeService.findByNamedParam("publicId", publicId);
			String noticeIdStr = this.noticeIdStr(notices);
			if(StringUtils.hasText(noticeIdStr)){
				noticeNoteDao.deleteByNoticeIds(noticeIdStr);
				noticeDao.deleteByPublicId(publicId);
			}
		}
		publicRoadDao.deleteRoad(flag,publicId);
	}
	
	/**
	 * 上传头像
	 * @param logo
	 * @param request
	 * @return
	 */
	public String uploadPhoto(MultipartFile logo,HttpServletRequest request){
		//上传文件名
		String fileName = logo.getOriginalFilename();
		//文件名处理
		String lastName = System.currentTimeMillis()+"_"+fileName;
		//保存路径
		String fileFullPath = path+File.separator+lastName;
		//将头像复制到工程下面，手机端直接查看
		String realPath=request.getSession().getServletContext().getRealPath("");
		String desPath =realPath+ "/attached/image/"+lastName;
		try {
			//保存文件
			FileCopyUtils.copy(logo.getBytes(),new File(fileFullPath));
			//将头像复制到工程下面，手机端直接查看
			FileCopyUtils.copy(logo.getBytes(),new File(desPath));
			return lastName;
		} catch (IOException e) {
			e.printStackTrace();
			return "ERROR";
			
		} 
	}
	
	/**
	 * 获取NoticeId字符串
	 * @param list
	 * @return
	 */
	private String noticeIdStr(List<Notice> list){
		String result = "";
		if(list!=null&&!list.isEmpty()){
			for(Notice notice :list){
				result +="'"+notice.getNoticeId()+ "',";
			}
			result = result.substring(0, result.length()-1);
		}
		return result;
	}
	
}

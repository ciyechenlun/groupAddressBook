package com.cmcc.zysoft.groupaddressbook.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.groupaddressbook.model.Search;
import com.cmcc.zysoft.groupaddressbook.service.NoticeService;
import com.cmcc.zysoft.groupaddressbook.service.PublicRoadService;
import com.cmcc.zysoft.groupaddressbook.service.UserCompanyService;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.UserCompany;
import com.cmcc.zysoft.spring.security.model.User;
import com.cmcc.zysoft.spring.security.util.SecurityContextUtil;
import com.starit.common.dao.support.Pagination;
/**
 * 
 * @author liyuchen
 *
 */
@Controller
@RequestMapping("/pc/publicRoadManager")
public class PublicRoadManagerController extends BaseController{
	@Resource
	private PublicRoadService publicRoadService;
	@Resource
	private UserCompanyService userCompanyService;
	
	@Resource
	private	NoticeService noticeService;
	/**
	 * 跳转至公告关联页面
	 * @param modelMap
	 * @param search
	 * @param request
	 * @return
	 */
	@RequestMapping("/main.htm")
	public String toManager(ModelMap modelMap,Search search,String manager,HttpServletRequest request){
		String companyId = SecurityContextUtil.getCompanyId();
		User user = SecurityContextUtil.getCurrentUser();
		if(request.getSession().getAttribute("selCompany")!=null)
		{
			Company company = (Company)request.getSession().getAttribute("selCompany");
			companyId = company.getCompanyId();
		}
		Pagination<?> pagination = publicRoadService.list(8, search.getPageNo(), companyId,manager,user.getUserId());
		modelMap.put("pagination", pagination);
		modelMap.put("manager", manager);
		return  "web/publicNumManage";
	}
	/**
	 * 公告状态变更
	 * @param publicId 公告ID
	 * @param status 状态
	 * @return
	 */
	@RequestMapping("/changeStatus.htm")
	@ResponseBody
	public boolean changeStatus(String publicId,String status){
		Boolean result = false;
		try{
			//变更
			publicRoadService.changeStatus(publicId, status);
			result = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 删除公告
	 * @param publicId 公告Id
	 * @return
	 */
	@RequestMapping("/delete.htm")
	@ResponseBody
	public boolean deleteRoad(String publicId){
		Boolean result = false;
		try{
			publicRoadService.deleteRoad(true,publicId);
			result = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 跳转至公告历史页面
	 * @param modelMap
	 * @param search
	 * @param request
	 * @return
	 */
	@RequestMapping("/toHistory.htm")
	public String toHistory(ModelMap modelMap,Search search,HttpServletRequest request){
		String companyId = SecurityContextUtil.getCompanyId();
		User user = SecurityContextUtil.getCurrentUser();
		if(request.getSession().getAttribute("selCompany")!=null)
		{
			Company company = (Company)request.getSession().getAttribute("selCompany");
			companyId = company.getCompanyId();
		}
		List<UserCompany> userCompany = userCompanyService.findByNamedParam(new String[]{"companyId","employeeId","delFlag"}, new Object[]{companyId,user.getEmployeeId(),"0"});
		Pagination<?> pagination = noticeService.getHistoryList(8, search.getPageNo(), companyId,userCompany);
		modelMap.put("pagination", pagination);
		
		return  "web/publicRoadHistory";
	}
	/**
	 * 删除历史公告
	 * @param noticeId 
	 * @return
	 */
	@RequestMapping("/deleteHistory.htm")
	@ResponseBody
	public Boolean deleteHistory(String noticeId){
		if(StringUtils.hasText(noticeId)){
			try {
				this.noticeService.deleteNoticeInfo(noticeId);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}else{
			return false;
		}
		
	}
	
	/**
	 * 跳转至草稿箱
	 * @param modelMap
	 * @param search
	 * @param request
	 * @return
	 */
	@RequestMapping("/toDraftbox.htm")
	public String toDraftbox(ModelMap modelMap,Search search,HttpServletRequest request){
		String companyId = SecurityContextUtil.getCompanyId();
		User user = SecurityContextUtil.getCurrentUser();
		if(request.getSession().getAttribute("selCompany")!=null)
		{
			Company company = (Company)request.getSession().getAttribute("selCompany");
			companyId = company.getCompanyId();
		}
		List<UserCompany> userCompany = userCompanyService.findByNamedParam(new String[]{"companyId","employeeId","delFlag"}, new Object[]{companyId,user.getEmployeeId(),"0"});
		Pagination<?> pagination = noticeService.getDraftboxList(8, search.getPageNo(), companyId,userCompany);
		modelMap.put("pagination", pagination);
		
		return  "web/roadDraftbox";
	}
	/**
	 * 删除草稿箱
	 * @param noticeId 
	 * @return
	 */
	@RequestMapping("/deleteDraft.htm")
	@ResponseBody
	public Boolean deleteDraft(String noticeId){
		if(StringUtils.hasText(noticeId)){
			try {
				this.noticeService.deleteNoticeInfo(noticeId);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}else{
			return false;
		}
		
	}

}

// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.sysmanage.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cmcc.zysoft.groupaddressbook.util.UnZip;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sellmanager.model.CompanyPlug;
import com.cmcc.zysoft.sellmanager.model.Department;
import com.cmcc.zysoft.sellmanager.model.Employee;
import com.cmcc.zysoft.sellmanager.model.Headship;
import com.cmcc.zysoft.sellmanager.model.Role;
import com.cmcc.zysoft.sellmanager.model.SystemUser;
import com.cmcc.zysoft.sellmanager.model.UserCompany;
import com.cmcc.zysoft.sellmanager.model.UserDepartment;
import com.cmcc.zysoft.sellmanager.model.UserRole;
import com.cmcc.zysoft.sysmanage.dao.CompanyDao;
import com.cmcc.zysoft.sysmanage.dao.DepartmentDao;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;

/**
 * @author li.menghua
 * @date 2012-11-28 下午3:54:21
 */
@Service
public class CompanyService extends BaseServiceImpl<Company, String>{
	
	/**
	 * 属性名称：_logger 类型：Logger
	 */
	private static Logger _logger = LoggerFactory.getLogger(CompanyService.class);
	
	/**
	 * 属性名称：companyDao 类型：CompanyDao
	 */
	@Resource
	private CompanyDao companyDao;
	
	/**
	 * 属性名称：departmentDao 类型：DepartmentDao
	 */
	@Resource
	private DepartmentDao departmentDao;
	
	@Resource
	private com.cmcc.zysoft.groupaddressbook.dao.UserDao userDao;
	
	@Resource
	private com.cmcc.zysoft.groupaddressbook.dao.LookGroupDao lookGroupDao;
	
	@Resource
	private com.cmcc.zysoft.groupaddressbook.dao.UserCompanyDao userCompanyDao;
	
	@Resource
	private com.cmcc.zysoft.groupaddressbook.dao.UserDepartmentDao userDepartmentDao;
	
	@Resource
	private com.cmcc.zysoft.sysmanage.dao.RoleDao roleDao;
	
	@Resource
	private com.cmcc.zysoft.sysmanage.dao.UserRoleDao userRoleDao;
	
	@Resource
	private com.cmcc.zysoft.groupaddressbook.service.GroupVersionService groupVersionService;
	
	@Resource
	private com.cmcc.zysoft.groupaddressbook.service.PCHeadshipService pcHeadshipService;
	
	@Resource
	private com.cmcc.zysoft.groupaddressbook.service.DepartmentVersionService departmentVersionService;
	
	@Resource
	private CompanyPlugService companyPlugService;
	
	@Value("${upload.file.path}")
	private String path;

	@Value("${project.skin.path}")
	private String skinPath;
	
	@Override
	public HibernateBaseDao<Company, String>getHibernateBaseDao(){
		return this.companyDao;
	}
	
	/**
	 * 获取父企业列表
	 * @return 
	 */
	public List<Map<String, Object>> getCompanyTree(String companyId){
		List<Map<String, Object>> companyList = this.companyDao.getCompanyTree("0",companyId);
		return getSubComp(companyList,companyId);
	}
	
	/**
	 * 递归获取子企业列表
	 * @return 
	 */
	public List<Map<String, Object>> getSubComp(List<Map<String, Object>> companyList,String companyId){
		for(Map<String, Object> map : companyList){
			if (!map.isEmpty()){
				try{
					String haveChildCompany = map.get("have_child_company").toString();
					if("Y".equals(haveChildCompany)){
						String parentCompanyId = map.get("company_id").toString();
						List<Map<String, Object>> ChildList = this.companyDao.getCompanyTree(parentCompanyId,companyId);
						getSubComp(ChildList,companyId);
						map.put("children", ChildList);
						map.put("state", "closed");
					}else{
						map.put("state", "open");
					}
				}
				catch(Exception ex)
				{
					logger.debug(ex.getMessage());
				}
			}
		}
		return companyList;
	}
	
	/**
	 * 通过公司Id获取公司实体
	 * @param companyId
	 * @return 
	 */
	public Company update(String companyId){
		return companyDao.get(companyId);
	}
	
	/**
	 * 增加企业
	 * @param  comp
	 * @return companyId
	 */
	public String addCompany(Company comp,HttpServletRequest request,MultipartFile photoFile,String pedometer) {
//		if(!"0".equals(comp.getParentCompanyId())){
//			Company father_comp = companyDao.get(comp.getParentCompanyId());
//			father_comp.setHaveChildCompany("Y");
//			companyDao.update(father_comp);
//		}
		if(photoFile.getSize() == 0){
			comp.setIndexLogo("");
			comp.setIndexPictrue("");
		} else {
			String logo = this.uploadPhoto(request, photoFile);
			if(logo=="ERROR PHOTO"){
				return logo;
			}else{
				comp.setIndexPictrue(this.uploadPhoto(request, photoFile));
				comp.setIndexLogo("m_logo.png");
			}
		}
		if(this.checkCompanyCode(comp.getCompanyCode())>0){
			return "companyCode error";
		}
		comp.setHaveChildCompany("N");
		comp.setDelFlag("0");
		comp.setParentCompanyId("0");
		comp.setVitrueFlag("0");
		String companyId = this.companyDao.save(comp);
		if(StringUtils.hasText(companyId)){
			if(pedometer!=null&&StringUtils.hasText(pedometer)){
				if("1".equals(pedometer)){//打开计步器
						CompanyPlug companyPlug = new CompanyPlug();
						companyPlug.setCompanyId(companyId);
						//plugId=1 为计步器插件
						companyPlug.setPlugId("1");
						companyPlug.setDisplayOrder(1);
						//添加记录
						this.companyPlugService.insertEntity(companyPlug);
					}
			}
			//将企业联系人添加为企业管理员
			addCompanyManager(companyId, comp.getContactMan(), comp.getTelephone());
			_logger.debug("添加企业成功");
			return companyId;
		}else{
			_logger.debug("添加企业失败");
			return "ERROR";
		}
	}
	
	/**
	 * 企业用户管理员
	 * @param company_id
	 * @param user_name
	 * @param mobile
	 */
	private void addCompanyManager(String company_id,String user_name,String mobile)
	{
		Company company = this.companyDao.get(company_id);
		//employee
		Map<String,Object> map = this.lookGroupDao.getEmployeeByMobile(mobile);
		//在当前企业下添加部门
		Department dept = new Department();
		
		dept.setCompany(company);
		dept.setDelFlag("0");
		dept.setDepartmentFirstword("y");
		dept.setDepartmentLevel(1);
		dept.setDepartmentName("移动客服");
		dept.setDisplayOrder(10000);
		dept.setParentDepartmentId("0");
		String department_id = this.departmentDao.addDepartment(dept);
		//增加部门版本号
		this.departmentVersionService.save("0", department_id);
		
		//在当前企业下添加职位
		Headship headship = new Headship();
		headship.setCompanyId(company_id);
		headship.setDelFlag("0");
		headship.setHeadshipLevel("2");
		headship.setHeadshipName("移动客服");
		String headship_id = this.pcHeadshipService.insertEntity(headship);
		
		String employee_id = "";
		if(map!=null)
		{
			employee_id = map.get("employee_id").toString();
		}
		else
		{
			//2.添加员工
			Employee emp = new Employee();
			emp.setEmployeeName(user_name);
			emp.setDepartmentId(department_id);
			emp.setDepartmentName("移动客服");
			emp.setDisplayOrder(10000);
			emp.setHeadshipId(headship_id);
			emp.setMobile(mobile);
			employee_id = this.lookGroupDao.save(emp);
		}
		//system_user
		List<SystemUser> listSysUser = this.userDao.getSystemUserByMobile(mobile);
		SystemUser sysUser = null;
		if (listSysUser.size()>0)
		{
			sysUser = listSysUser.get(0);
		}
		else
		{
			sysUser = new SystemUser();
			sysUser.setPassSalt("477b9ab6a4894641910f4ef75140246d");
			sysUser.setPassword("3d9d9db43eb6910bade44c6d129d31f1");
			sysUser.setCompany(company);
			sysUser.setUserName(mobile);
			sysUser.setDelFlag("0");
			sysUser.setEmployeeId(employee_id);
			this.userDao.save(sysUser);
		}
		//tb_user_company
		UserCompany userComp = new UserCompany();
		userComp.setCompanyId(company_id);
		userComp.setDelFlag("0");
		userComp.setDisplayOrder(10000);
		userComp.setEmployeeId(employee_id);
		userComp.setEmployeeName(user_name);
		userComp.setMobile(mobile);
		userComp.setHeadshipName("移动客服");
		userComp.setDepartmentName("移动客服");
		userComp.setManageFlag("1");

		String user_company_id = this.userCompanyDao.save(userComp);
		addUserDept(department_id,user_company_id,headship_id);
		
		this.groupVersionService.addGroupVersion(userComp.getUserCompanyId(), "0");
		
		//加为管理员
		Role role = this.roleDao.get("1");
		UserRole ur = new UserRole();
		ur.setSystemUser(sysUser);
		ur.setRole(role);
		this.userRoleDao.save(ur);
	}
	/**
	 * 添加管理员时，同步添加部门隶属
	 * @param department_id
	 * @param user_company_id
	 */
	private void addUserDept(String department_id,String user_company_id,String headship_id)
	{
		UserDepartment userDept = new UserDepartment();
		userDept.setDepartmentId(department_id);
		userDept.setDepartmentPath("移动客服");
		userDept.setHeadshipId(headship_id);
		userDept.setHeadshipName("移动客服");
		userDept.setUserCompanyId(user_company_id);
		userDept.setVisibleFlag("1");
		userDept.setTaxis(9999);
		this.userDepartmentDao.save(userDept);
	}
	
	/**
	 * 修改企业信息
	 * @param  comp
	 * @return 
	 */
	public String updateCompany(Company comp,HttpServletRequest request,MultipartFile photoFile,String pedometer){
//		if(!"0".equals(comp.getParentCompanyId())){
//			Company father_comp = companyDao.get(comp.getParentCompanyId());
//			father_comp.setHaveChildCompany("Y");
//			companyDao.update(father_comp); //修改企业时，若选择的上级公司不为"无",则将所选择的上级公司是否有子公司状态改为"Y"
//		}
		Company new_comp=companyDao.get(comp.getCompanyId());
//		if(!new_comp.getParentCompanyId().equals(comp.getParentCompanyId()) && !"0".equals(new_comp.getParentCompanyId())){
//			List<Company> list = this.findByNamedParam(new String[]{"parentCompanyId","delFlag"}, new Object[]{new_comp.getParentCompanyId(), "0"});
//			if(list.size()==1){
//				Company father_comp_new = companyDao.get(new_comp.getParentCompanyId());
//				father_comp_new.setHaveChildCompany("N");
//				companyDao.update(father_comp_new); //修改企业时,如果修改了上级公司,且之前的父公司下只有这一个子公司，则将之前的父公司的是否有子公司状态改为"N"
//			}
//		}
		if(photoFile.getSize() == 0){
			new_comp.setIndexLogo(comp.getIndexLogo());
		} else {
			String logo = this.uploadPhoto(request, photoFile);
			if(logo=="ERROR PHOTO"){
				return logo;
			}else{
				new_comp.setIndexLogo("m_logo.png");
				new_comp.setIndexPictrue(this.uploadPhoto(request, photoFile));
			}
		}
		new_comp.setUsersLimit(comp.getUsersLimit());
		new_comp.setCompanyUsersLimit(comp.getCompanyUsersLimit());
		new_comp.setOtherNetFlag(comp.getOtherNetFlag());
		new_comp.setCompanyName(comp.getCompanyName());
		//new_comp.setCompanyAddress(comp.getCompanyAddress());
		new_comp.setCity(comp.getCity());
		new_comp.setContactMan(comp.getContactMan());
		new_comp.setTelephone(comp.getTelephone());
		new_comp.setFax(comp.getFax());
		new_comp.setLoginUrl(comp.getLoginUrl());
		companyDao.update(new_comp);
		//更新计步器开关
		if(pedometer!=null&&StringUtils.hasText(pedometer)){
			//plugId="1" 表示计步器
			List<CompanyPlug> companyPlugList = companyPlugService.findByNamedParam(new String[]{"companyId","plugId"}, new Object[]{comp.getCompanyId(),"1"});
			if("1".equals(pedometer)){//打开计步器
				if(companyPlugList==null||companyPlugList.size()==0){
					CompanyPlug companyPlug = new CompanyPlug();
					companyPlug.setCompanyId(comp.getCompanyId());
					companyPlug.setPlugId("1");
					companyPlug.setDisplayOrder(1);
					//添加记录
					this.companyPlugService.insertEntity(companyPlug);
				}
			}else{//关闭计步器
				if(companyPlugList!=null&&companyPlugList.size()!=0){
					//删除记录
					this.companyPlugService.deleteEntity(companyPlugList.get(0).getCompanyPlugId());
				}
			}
		}
		
		
		_logger.debug("修改企业信息成功");
		return "SUCCESS";
	}
	
	/**
	 * 删除企业,若删除该企业后，上级企业下面已无任何子企业,则将上级企业改为无子企业状态
	 * @param companyId
	 */
	public void deleteCompany(String companyId){
		Company comp = companyDao.get(companyId);
		comp.setDelFlag("1");
		this.companyDao.update(comp);
		if(!comp.getParentCompanyId().equals("0")){
			List<Company> list = this.findByNamedParam(new String[]{"parentCompanyId","delFlag"}, new Object[]{comp.getParentCompanyId(), "0"});
			if(list.size()==0){
				Company father_comp_new = this.companyDao.get(comp.getParentCompanyId());
				father_comp_new.setHaveChildCompany("N");
				this.companyDao.update(father_comp_new);
			}
		}
	}
	
	/**
	 * combortree 获取公司树
	 * @author yandou
	 * @param companyId 
	 * @return
	 */
	public List<Map<String, Object>> companyTree(String companyId) {
		// companyId 只能对该公司以下公司或部门的人员设置权限
		List<Map<String, Object>> list = this.companyDao.companyTree(companyId);
		return getChildComp(list);
	}
	
	/**
	 * 获取子公司，递归，
	 * @author yandou
	 * @param list
	 * @return
	 */
	public List<Map<String, Object>> getChildComp(List<Map<String, Object>> list){
		for(Map<String,Object> map : list){
			String hasChildCom = map.get("haveChildCom").toString();
			map.put("iconCls", "company");//添加图标
			if("Y".equals(hasChildCom)){//存在子公司
				String companyId = map.get("id").toString();
				List<Map<String, Object>> childList = this.companyDao.companyTree(companyId);
				getChildComp(childList);
				map.put("state", "closed");
				map.put("children", childList);
			}else{
				map.put("state", "open");
			}
		}
		return list;
	}
	
	/**
	 * 上传图片
	 * @author yandou
	 * @return
	 */
	public String uploadPhoto(HttpServletRequest request,MultipartFile photoFile) {
		//在这里就可以对file进行处理了，可以根据自己的需求把它存到数据库或者服务器的某个文件夹  
        String filename = photoFile.getOriginalFilename();
        String extName = filename.substring(filename.lastIndexOf(".")).toLowerCase(); 
        try {
        	if(extName.trim().equals(".zip")){
    	        String lastFileName = System.currentTimeMillis()+extName;
//    	        String path = "/resources/image/upload/companyLogo/";
//    	        String fileFullPath = request.getServletContext().getRealPath("")+path+lastFileName;
    	        //图片存储的相对路径   
    	        String fileFullPath = path+File.separator+"company_"+lastFileName;
    			FileCopyUtils.copy(photoFile.getBytes(),new File(fileFullPath));
    			String skinFullPath = skinPath +  File.separator + "company_"+lastFileName;//放到skin目录下
    			FileCopyUtils.copy(photoFile.getBytes(),new File(skinFullPath));
    			UnZip.unzipFile(skinFullPath, skinFullPath.replace(".zip",""));
    			return "company_"+lastFileName;
            }else{
            	return "ERROR PHOTO";
            }
		} catch (Exception e) {
			return "ERROR";
		}
	}

	/**
	 * 立即定位树显示
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> comsForImmeLocat(String id) {
		List<Map<String, Object>> coms =  this.companyDao.companyTree(id);
		for(Map<String,Object> map : coms){
			String companyId = map.get("id").toString();
			map.put("id", "c_"+companyId);
			map.put("iconCls", "company");
			List<Map<String, Object>> deptList = this.departmentDao.departmentTree("0", companyId);
			if("Y".equals(map.get("haveChildCom").toString())){
				map.put("state", "closed");
			}else{
				if(deptList.isEmpty()){
					map.put("state", "open");
				}else{
					map.put("state", "closed");
				}
			}
		}
		return coms;
	}
	
	/**
	 * 查询公司
	 * @param deptName
	 * @return
	 */
	public List<Map<String, Object>> searchComp(String companyName){
		Map<String, Object> map = null;
		List<Map<String, Object>> list = this.companyDao.searchComp(companyName);
		List<Map<String, Object>> parentList = new ArrayList<Map<String, Object>>();
		if(null != list && list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				map = list.get(i);
				if(!"0".equals(map.get("parent_company_id").toString())){
					parentList.addAll(searchParent(map.get("parent_company_id").toString()));
				}
			}
			list.addAll(parentList);
		}
		return list;
	}
	
	/**
	 * 查询时，获取查询到的公司的上级公司
	 * @param parentCompanyId
	 * @return
	 */
	public List<Map<String, Object>> searchParent(String parentCompanyId){
		List<Map<String, Object>> parentList = this.companyDao.searchCompByCompId(parentCompanyId);
		Map<String,Object> parentMap = parentList.get(0);
		if(!"0".equals(parentMap.get("parent_company_id").toString())){
			parentList.addAll(searchParent(parentMap.get("parent_company_id").toString()));
		}
		return parentList;
	}
	
	/**
	 * 查找相同企业编码的数量
	 * @param companyCode
	 * @return
	 */
	public int checkCompanyCode(String companyCode){
		List<Company> list = this.findByNamedParam("companyCode",companyCode);
		return list.size();
	}
	
	/**
	 * 根据登录地址获取公司
	 * @param loginUrl 登录地址pattern 
	 * <p>e.g:http://localhost:8181/cmcc,响应的pattern是cmcc</p>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Company getCompanyByUrl(String loginUrl){
		String hql = "from Company as c where c.loginUrl = ? and c.delFlag='0'";
		List<Company> companies = this.companyDao.findByHQL(hql, loginUrl);
		return companies.isEmpty()?null:companies.get(0);
	}
	
	/**
	 * 公司部门树(员工管理,基础数据模块,销售管理模块)
	 * 先找到公司
	 * @param companyId
	 * @return
	 */
	public List<Map<String, Object>> getCompanyTreeHql(String companyId){
		return this.companyDao.getCompanyTreeHql(companyId);
	}
	
	/**
	 * 根据公司编码获取公司信息
	 * @param companyCode
	 * @return 返回类型：Company
	 */
	public Company getCompanyByCode(String companyCode){
		List<Company> companies = this.companyDao.findByNamedParam("companyCode", companyCode);
		if(!companies.isEmpty()){
			return companies.get(0);
		}
		return null;
	}
	
	/**
	 * 根据手机号码返回公司列表
	 * @param mobile
	 * @return
	 */
	public List<Map<String,Object>> getCompanyByMobile(String mobile)
	{
		return this.companyDao.getCompanyByMobile(mobile);
	}
	
	/**
	 * 添加公司
	 * @param company
	 * @return 返回类型：String
	 */
	public String addCompany(Company company){
		return this.companyDao.addCompany(company);
	}
	
	public void test(String company_id,String company_name)
	{
		this.companyDao.test(company_id, company_name);
	}
	/**
	 * 根据手机号码，返回用户皮肤资源文件地址
	 * @param mobile
	 * @return
	 */
	public List<Map<String, Object>> getCompanySkin(String mobile)
	{
		return this.companyDao.getCompanySkin(mobile);
	}
	
	/**
	 * 获取所有企业信息列表---用于同步企业信息.
	 * add by yuan.fengjian@ustcinfo.com
	 * @return list
	 */
	public List<Company> getAllCompanys() {
		return this.companyDao.getAllCompanys();
	}
	
	/**
	 * 根据企业逻辑删除企业下的部门，用户，关联表数据
	 * add by 张军 2013/11/20
	 */
	public void delGroupByCompany(String companyId) {
		this.companyDao.delGroupByCompany(companyId);
	}
}

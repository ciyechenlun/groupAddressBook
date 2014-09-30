/**
 * CopyRight © 2012 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */

package com.cmcc.zysoft.groupaddressbook.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmcc.zysoft.groupaddressbook.webservice.client.GrayMembersClient;
import com.cmcc.zysoft.groupaddressbook.webservice.client.IctServiceRegOrChangeClient;
import com.cmcc.zysoft.groupaddressbook.webservice.client.MemberShipICTClient;
import com.cmcc.zysoft.sellmanager.common.BaseController;
import com.cmcc.zysoft.sellmanager.model.Company;
import com.cmcc.zysoft.sysmanage.service.CompanyService;
import com.cmcc.zysoft.sysmanage.service.SysEmployeeService;

/**
 * 同步所有集团信息和成员信息、以及灰度用户列表同步控制器.
 * @author 袁凤建
 * <br />邮箱: yuan.fengjian@ustcinfo.com
 * <br />描述: SyncAllCompanyAndEmployee.java
 * <br />版本: 1.0.0
 * <br />日期: 2013-7-8 上午11:23:27
 * <br />CopyRight © 2012 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */

@Controller
@RequestMapping("pc/sync")
public class SyncAllCompanyAndEmployee extends BaseController {

	/**
	 * 日志.
	 */
	private static final Logger logger = LoggerFactory.getLogger(SyncAllCompanyAndEmployee.class);
	
	@Resource
	private CompanyService companyService;
	
	@Resource
	private SysEmployeeService sysEmployeeService;
	
	/**
	 * 同步企业信息.
	 * @return map
	 */
	@RequestMapping("/allCompany.htm")
	@ResponseBody
	public Map<String, Object> syncAllCompany(String prdcode,String servicename,String accessNo,Company cmp) {
		logger.debug("##########  同步企业信息  开始!  ##########");
		//测试标记(0-非测试交易, 1-测试交易)
		int testFlag = 1;
		//操作类型(0-开通，1-修改)
		String OptType = "0";
		//产品编号-BOSS分配的产品编号
		String PrdCode = "ICT0001";
		//业务编码-识别此业务的唯一标识(服务编码)
		String ServiceCode = "ICT0001";
		if(StringUtils.hasText(prdcode))
		{
			PrdCode = prdcode;
			ServiceCode = prdcode;
		}
		//集团产品号码-标识集团订购关系
		String PrdOrdNum = "000000001";
		//业务名称
		String ServiceName = StringUtils.hasText(servicename)?servicename:"集团通讯录";
		if(StringUtils.hasText(servicename))
		{
			ServiceName = servicename;
		}
		//业务基本接入号-每个业务都有一个基本接入号(短号)
		String AccessNo = "10657112033757";
		//EC接入端口号-每个EC订购都分配一个端口号,作为BOSS计费号码
		String ECAccessPort = "000000001";
		//生效时间-该业务的生效日期。业务生效日期到达之前，暂时不向企业提供服务 格式:YYYY-MM-DD HH:MM:SS
		String StartEfft = "2013-01-01 00:00:00";
		//失效时间-格式:YYYY-MM-DD HH:MM:SS
		String EndEfft = "9999-12-31 23:59:59";
		//EC管理员帐户名-开通时需要传一个EC管理员帐户（登陆名，在EC范围内唯一），此管理员可以登录进行业务管理分配成员使用SI业务
		String AdminUser = "admin@5512012441";
		//EC管理员帐户标识-EC管理员的标识，唯一标识这个用户
		String AdminUFID = "142989";
		//优先级-(保留字段)现在用来传送Admin用户手机号码
		String Priority = "15156892727";
		//是否存在试用期(0：无；1：有)
		String TrailFlag = "1";
		//服务号码-BOSS根据产品自动生成：企业编号+001
		String BizID = "服务号码";
		//成员许可数上限-取值范围：1~99999，表示可开通帐号数量，用于控制成员数量
		String MemberNumLimit = "20000";
		//业务平台类型(00500-模式1 00501-模式2)
		String PlatType = "00500";
		//应用系统版本(A-标准版，B-增强版)
		String SoftVersion = "A";
		//外勤管家集成费(元)
		String ICTCost = "100.99";
		//归属地市-A:合肥,B:芜湖,C:蚌埠,D:淮南,E:马鞍山,F:淮北,G:铜陵,H:安庆,J:黄山,K:阜阳,L:宿州,M:滁州,N:六安,P:宣城,Q:巢湖,R:贵池,S:亳州
		String AreaCode = "A";
		//业务变更原因---添加、修改时均可为空
		String OPTNOTE = "";
		//获取所有企业列表
		Company company = null;
		if(cmp!=null)
		{
			company = cmp;
		}
		else{
			company =this.companyService.getCompanyByCode("AH_CMCC");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		//for(Company company : companyList) {
			map = IctServiceRegOrChangeClient.syncIctServiceRegOrChange(testFlag, OptType, company, PrdCode, ServiceCode, PrdOrdNum, 
					ServiceName, AccessNo, ECAccessPort, StartEfft, EndEfft, AdminUser, AdminUFID, Priority, TrailFlag, BizID, 
					MemberNumLimit, PlatType, SoftVersion, ICTCost, AreaCode, OPTNOTE);
		//}
		logger.debug("##########  同步企业信息  结束!  ##########");
		return map;
	}
	
	/**
	 * 同步成员信息
	 * @param eccode：企业代码
	 * @param ecname：企业名称
	 * @param prdornum：集团产品代码，标识订购的唯一关系
	 * @param servicecode：服务唯一标识，可以与集团产品编码一致
	 * @param servicename：服务名称（产品名称）
	 * @param mobile：推送指定的手机号码至云平台，为空推送全部
	 * @param opttype：操作类型:03－定购  04－取消定购  05-业务信息变更（本次增加）
	 * @return
	 */
	@RequestMapping("/allEmployees.htm")
	@ResponseBody
	public Map<String, Object> syncAllEmployee(String eccode,String ecname,String prdornum,String servicecode,String servicename,String mobile,String opttype) {
		logger.debug("##########  同步成员信息  开始!  ##########");
		//测试标记(0-非测试交易, 1-测试交易)
		int testFlag = 1;
		//EC企业代码: 要修改成员的业务的企业计费代码
		String ECCode = "AH_CMCC";
		if (StringUtils.hasText(eccode))
		{
			ECCode = eccode;
		}
		//EC企业名称: 该业务的企业名称
		String ECName = "安徽移动";
		if (StringUtils.hasText(ecname))
		{
			ECName = ecname;
		}
		//集团产品代码: 订购关系的唯一标示
		String PrdOrdNum = "ICT0001";
		if (StringUtils.hasText(prdornum))
		{
			PrdOrdNum = prdornum;
		}
		//服务标识: 服务的唯一标识
		String ServiseCode = "ICT0001";
		if (StringUtils.hasText(servicecode))
		{
			ServiseCode = servicecode;
		}
		//服务名称
		String ServiseName = "集团通讯录";
		if (StringUtils.hasText(servicename))
		{
			ServiseName = servicename;
		}
		//属性名称
		String ItemName = "";
		//属性值
		String ItemValue = "";
		//过滤条件
		String where = "";
		if (StringUtils.hasText(mobile))
		{
			where = " mobile='"+mobile+"'";
		}
		//获取所有成员列表
		List<Map<String,Object>> empList = this.sysEmployeeService.getAllEmployeeWithIMSISQL(where);
		//操作类型:01－加入名单 02－退出名单  03－定购  04－取消定购  05-业务信息变更（本次增加）
		String OptType = "03";
		if (StringUtils.hasText(opttype))
		{
			OptType = opttype;
		}
		//登录鉴权类型:0-普通鉴权 1-手机号码+IMSI绑定鉴权
		String AuthType = "1";
		Map<String, Object> map = new HashMap<String, Object>();
		map = MemberShipICTClient.syncMemberShip(testFlag, ECCode, ECName, PrdOrdNum, ServiseCode, ServiseName, ItemName,
				ItemValue, empList, OptType, AuthType);
		logger.debug("##########  同步成员信息  结束!  ##########");
		return map;
	}
	
	/**
	 * 同步灰度用户列表信息.
	 * @return map
	 */
	@RequestMapping("/grayMembers.htm")
	@ResponseBody
	public Map<String, Object> syncGrayMembers() {
		logger.debug("##########  同步灰度用户列表信息  开始!  ##########");
		//测试标记:发起方填写，0：非测试交易，1：测试交易；测试必须是业务级别，即在同一个业务流水中所有交易必须具有相同的测试标记(默认为0即可)
		int testFlag = 1;
		//由BOSS统一分配的产品标识,如果不走BOSS进行集团订购的产品，则由云平台分配 如集团通讯录标准版:ICT0006
		String ProductCode = "ICT0006";
		//产品名称
		String ProductName = "集团通讯录标准版";
		//版本号:如：1.2.3。该版本必需在云平台上已存在且为体验版。根据版本号可以查询该版本的功能描述。
		String Version = "1.2.1";
		//描述信息:描述本次发布的目的。如：网达定制版、ICT中心内部测试版等等。 具体内容由业务平台提供，用于管理员审核的依据之一。
		String Desc = "测试test";
		//发布范围:对于灰度发布范围的描述。对应业务平台的用户筛选条件。
		String Scope = "测试test";
		//操作说明:01：添加体验用户 02：删除体验用户
		String Operate = "01";
		//需要进行同步的用户列表---根据实际需要进行获取,如List<Employee> empList = this.sysEmployeeService.getAllEmployee();-同步全部用户
		List<Map<String, Object>> empList = null;
		Map<String, Object> map = new HashMap<String, Object>();
		map = GrayMembersClient.syncGrayMembers(testFlag, ProductCode, ProductName, Version, Desc, Scope, Operate, empList);
		logger.debug("##########  同步灰度用户列表信息  结束!  ##########");
		return map;
	}
}
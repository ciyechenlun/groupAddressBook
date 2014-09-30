/**
 * CopyRight © 2012 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */

package com.cmcc.zysoft.groupaddressbook.util;

/**
 * 存储固定XML报文格式的常量类.
 * @author 袁凤建
 * <br />邮箱：yuan.fengjian@ustcinfo.com
 * <br />描述：XMLConstant.java
 * <br />版本: 1.0.0
 * <br />日期：2013-7-2 上午9:08:34
 * <br />CopyRight © 2012 USTC SINOVATE SOFTWARE CO.LTD All Rights Reserved.
 */

public final class XMLConstant {

	/**
	 * ICT服务开通和修改业务的请求报文.
	 */
	public static final String PROVISIONG_ICT_SERVICE_REQUEST = 
			"<ADCProvisioningICTRequest>\n" +
			"	<BODY>\n" +
			"		<EcFrom>%s</EcFrom>\n" +
			"		<SICode>%s</SICode>\n" +
			"		<SIName>%s</SIName>\n" +
			"		<OptType>%s</OptType>\n" +
			"		<ECCode>%s</ECCode>\n" +
			"		<ECName>%s</ECName>\n" +
			"		<PrdCode>%s</PrdCode>\n" +
			"		<ServiceCode>%s</ServiceCode>\n" +
			"		<PrdOrdNum>%s</PrdOrdNum>\n" +
			"		<ServiceName>%s</ServiceName>\n" +
			"		<AccessNo>%s</AccessNo>\n" +
			"		<ECAccessPort>%s</ECAccessPort>\n" +
			"		<StartEfft>%s</StartEfft>\n" +
			"		<EndEfft>%s</EndEfft>\n" +
			"		<AdminUser>%s</AdminUser>\n" +
			"		<AdminUFID>%s</AdminUFID>\n" +
			"		<Priority>%s</Priority>\n" +
			"		<TrialInfo>\n" +
			"			<TrailFlag>%s</TrailFlag>\n" +
			"			<ToBusinessTime>%s</ToBusinessTime>\n" +
			"			<BusiLimit>%s</BusiLimit>\n" +
			"		</TrialInfo>\n" +
			"		<PARAMMAP>\n" +
			"			<PARAMNAME>%s</PARAMNAME>\n" +
			"			<PARAMVALUE>%s</PARAMVALUE>\n" +
			"		</PARAMMAP>\n" +
			"		<ORDERPOINTMAP>\n" +
			"			<POINTNAME>%s</POINTNAME>\n" +
			"			<POINTVALUE>%s</POINTVALUE>\n" +
			"		</ORDERPOINTMAP>\n" +
			"		<LICENSE>%s</LICENSE>\n" +
			"		<IctParam>\n" +
			"			<BizID>%s</BizID>\n" +
			"			<MemberNumLimit>%s</MemberNumLimit>\n" +
			"			<PlatType>%s</PlatType>\n" +
			"			<SoftVersion>%s</SoftVersion>\n" +
			"			<ICTCost>%s</ICTCost>\n" +
			"			<AreaCode>%s</AreaCode>\n" +
			"		</IctParam>\n" +
			"		<OPTNOTE>%s</OPTNOTE>\n" +
			"	</BODY>\n" + 
			"</ADCProvisioningICTRequest>\n";
	
	/**
	 * ICT服务开通和修改业务的返回报文.
	 */
	public static final String ADC_PROVISIONING_ICT_RESPONSE = 
			"<ADCProvisioningICTResponse >\n" +
			"	<BODY>\n" + 
			"		<URL>%s</URL>\n" + 
			"	</BODY>\n" + 
			"</ADCProvisioningICTResponse >\n" ;

	/**
	 * ICT客户成员订购管理业务的请求报文.
	 */
	public static final String ICT_CUSTOMER_MEMBERS_ORDER_REQUEST = 
			"<MemberShipICTRequest>\n" +
			"	<BODY>\n" +
			"		<UserFrom>%s</UserFrom>\n" +
			"		<ECCode>%s</ECCode>\n" +
			"		<ECName>%s</ECName>\n" +
			"		<PrdOrdNum>%s</PrdOrdNum>\n" +
			"		<ServiseCode>%s</ServiseCode>\n" +
			"		<ServiseName>%s</ServiseName>\n" +
			"		<Members>%s</Members>\n" +
			"		<Extitem>\n" +
			"			<ItemName>%s</ItemName>\n" +
			"			<ItemValue>%s</ItemValue>\n" +
			"		</Extitem>\n" +
			"	</BODY>\n" + 
			"</MemberShipICTRequest>\n";
	
	/**
	 * ICT客户成员订购管理业务-成员详细信息(请求)报文.
	 */
	public static final String ICT_CUSTOMER_MEMBERS_DETAIL = 
			"<Member>\n" +
			"	<OptType>%s</OptType>\n" +
			"	<MemberMobile>%s</MemberMobile>\n" +
			"	<MemberName>%s</MemberName>\n" +
			"	<MemberAccount>%s</MemberAccount>\n" +
			"	<UFID>%s</UFID>\n" +
			"	<BizID>%s</BizID>\n" +
			"	<PlatType>%s</PlatType>\n" +
			"	<AuthType>%s</AuthType>\n" +
			"	<ICTFee>%s</ICTFee>\n" +
			"	<ICTFeePayer>%s</ICTFeePayer>\n" +
			"	<EditDate>%s</EditDate>\n" +
			"	<USERINFOMAP>\n" +
			"		<ItemName>%s</ItemName>\n" +
			"		<ItemValue>%s</ItemValue>\n" +
			"	</USERINFOMAP>\n" +
			"	<Imsi>%s</Imsi>\n" +
			"</Member>\n";
	
	/**
	 * ICT客户成员订购管理业务的返回报文.
	 */
	public static final String MEMBERSHIP_ICT_RESPONSE = 
			"<MemberShipICTResponse>\n" +
			"	<BODY>\n" + 
			"		<EntCode>%s</EntCode>\n" + 
			"    	<Members>%s</Members>\n" + 
			"  </BODY>\n" + 
			"</MemberShipICTResponse>" ;
	
	/**
	 * 替换MEMBERSHIP_ICT_RESPONSE中Members节点内容，可多个.
	 */
	public static final String MEMBERSHIP_RESPONSE_BODY_MEMBER = 
			"<MemberShipResponseBODYMember>\n" +
			"	<OptType>%s</OptType>\n" + 
			"   <MemberMobile>%s</MemberMobile>\n" + 
			"   <MemberAccount>%s</MemberAccount>\n" + 
			"   <RESULTCODE>%s</RESULTCODE>\n" + 
			"   <RESULTMSG>%s</RESULTMSG>\n" + 
			"</MemberShipResponseBODYMember>";
	
	/**
	 * 灰度发布体验用户列表同步管理的请求报文.
	 */
	public static final String GRAY_MEMBERS_REQUEST = 
			"<GrayMembersRequest>\n" +
			"	<BODY>\n" +
			"		<ProductCode>%s</ProductCode>\n" +
			"		<ProductName>%s</ProductName>\n" +
			"		<Version>%s</Version>\n" +
			"		<Desc>%s</Desc>\n" +
			"		<Scope>%s</Scope>\n" +
			"		<Operate>%s</Operate>\n" +
			"		<Members>%s</Members>\n" +
			"	</BODY>\n" +
			"</GrayMembersRequest>\n";
	
	/**
	 * 灰度发布体验用户列表同步管理---用户详细信息(请求)报文.
	 */
	public static final String GRAY_MEMBERS_DETAIL = 
			"<Member>\n" +
			"	<Mobile>%s</Mobile>\n" +
			"</Member>\n";
	/**
	 * 短号获取的报文头.
	 */
	public static final String MOBILE_SHORT_REQUEST_HEAD = 
			"<ShortNumRequest>\n" +
            "<BizOrder>%s</BizOrder>\n" + 
            "</ShortNumRequest>\n";
	/**
	 * 短号获取的报文.
	 */
	public static final String MOBILE_SHORT_REQUEST = 
            "<BizRegReq>\n"+
            "<OrdSeq>%s</OrdSeq>\n"+
            "<PhoneNo>%s</PhoneNo>\n"+
            "</BizRegReq>\n";
}
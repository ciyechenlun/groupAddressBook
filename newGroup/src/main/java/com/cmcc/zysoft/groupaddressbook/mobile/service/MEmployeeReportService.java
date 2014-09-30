/**
 * CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */

package com.cmcc.zysoft.groupaddressbook.mobile.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.dto.UserRecordDto;
import com.cmcc.zysoft.groupaddressbook.mobile.dao.MEmployeeReportDao;
import com.cmcc.zysoft.sellmanager.model.EmployeeRecord;
import com.cmcc.zysoft.sellmanager.model.EmployeeReport;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
import com.starit.common.dao.support.Pagination;

/**
 * @author 袁凤建
 * @email yuan.fengjian@ustcinfo.com
 * @date 2013-6-8 下午4:28:50
 */

@Service
public class MEmployeeReportService extends BaseServiceImpl<EmployeeReport, String> {

	@Resource
	private MEmployeeReportDao mEmployeeReportDao;
	
	@Override
	public HibernateBaseDao<EmployeeReport, String> getHibernateBaseDao() {
		return this.mEmployeeReportDao;
	}
	
	/**
	 * 获取用户的排名
	 * @param userCode
	 * @param token
	 * @param movement_id
	 * @return
	 */
	public Map<String, Object> getEmpOrder(String userCode, String token, String movement_id) {
		return this.mEmployeeReportDao.getEmpOrder(userCode, token, movement_id);
	}
	
	/**
	 * 获取某一活动所有排名
	 * @param usercode
	 * @param movement_id
	 * @return
	 */
	public List<Map<String,Object>> getOrderList(String usercode,String movement_id)
	{
		return this.mEmployeeReportDao.getOrderList(usercode, movement_id);
	}
	
	/**
	 * 获取排名前十的用户列表
	 * @param userCode
	 * @param token
	 * @return
	 */
	public Map<String, Object> getTopTen(String userCode, String token, String movement_id) {
		return this.mEmployeeReportDao.getTopTen(userCode, token, movement_id);
	}
	
	/**
	 * 获取排名前十的用户列表(保持向下兼容，返回所有)
	 * @param userCode
	 * @param token
	 * @return
	 */
	public Map<String, Object> getTopTenV2(String userCode, String token, String movement_id) {
		return this.mEmployeeReportDao.getTopTenV2(userCode, token, movement_id);
	}
	
	/**
	 * 返回某个活动的前100名（疯了，早知道写个共通了）
	 * @param movement_id
	 * @return
	 */
	public List<Map<String,Object>> getAllTop100(String movement_id)
	{
		return this.mEmployeeReportDao.getAllTop100(movement_id);
	}
	
	/**
	 * 与我排名相近的5个排名
	 * @param myOrder
	 * @param movement_id 活动编号
	 * @return
	 */
	public List<Map<String,Object>> getOrderList(int myOrder, String movement_id){
		return this.mEmployeeReportDao.getOrderList(myOrder, movement_id);
	}
	
	/**
	 * 与我排名相近的10个排名
	 * @param myOrder 当前登录用户的排名
	 * @param movement_id 活动编号
	 * @return
	 */
	public List<Map<String,Object>> getOrderListNearestTen(int myOrder, String movement_id)
	{
		return this.mEmployeeReportDao.getOrderListNearestTen(myOrder, movement_id);
	}
	
	/**
	 * 更新用户统计表信息
	 * @param userCode：当前登录用户编号
	 * @param empRec：上传的计步明细数据
	 * @return
	 */
	public List<Map<String,Object>> updateUserReport(String userCode,List<EmployeeRecord> empRecList)
	{
		List<Map<String,Object>> list = this.mEmployeeReportDao.updateUserReport(userCode, empRecList);
		return list;
	}
	
	/**
	 * 获得个人统计信息（gps除外）
	 * @param usercode
	 * @return
	 */
	public List<Map<String,Object>> getMyRecordReport(String usercode,String movement_id)
	{
		return this.mEmployeeReportDao.getMyRecordReport(usercode,movement_id);
	}
	
	/**
	 * 上传用户的运动记录
	 * @param userCode
	 * @param token
	 * @param value
	 * @return
	 */
	public Map<String, Object> uploadRecord(String userCode, String token, String value) {
		return this.mEmployeeReportDao.uploadRecord(userCode, token, value);
	}
	
	/**
	 * 统计部门下员工的健步记录
	 * @param departmentId：部门编号
	 * @param startDate:开始日期，可以为空
	 * @param endDate:结束日期，可以为空（为空时，如果startDate不为空，则为当前日期）
	 * @param type:统计字段
	 * @param movement_id:活动编号
	 * @return
	 */
	public List<Map<String,Object>> getReportByDepartmentId(String departmentId,String startDate,String endDate,String type,String movement_id)
	{
		return this.mEmployeeReportDao.getReportByDepartmentId(departmentId,startDate,endDate,type,movement_id);
	}
	
	/**
	 * 根据部门获取用户列表
	 * @param departmentId：如果为空，则返回当前公司所有用户
	 * @param company_id
	 * @param movement_id
	 * @return
	 */
	public Pagination<?> getUserListByDepartment(int rows, int page,String departmentId,String company_id, String movement_id)
	{
		return this.mEmployeeReportDao.getUserListByDepartment(rows, page,departmentId, company_id, movement_id);
	}
	
	/**
	 * 导出数据源
	 * @param movement_id
	 *            :活动编号
	 * @return
	 */
	public List<UserRecordDto> getUserRecordExportSource(String movement_id) {
		return this.mEmployeeReportDao.getUserRecordExportSource(movement_id);
	}
	
	/**
	 * 获得某个用户的详细信息
	 * @param employee_id
	 * @return
	 */
	public Map<String,Object> getUserDetail(String employee_id)
	{
		return this.mEmployeeReportDao.getUserDetail(employee_id);
	}
}

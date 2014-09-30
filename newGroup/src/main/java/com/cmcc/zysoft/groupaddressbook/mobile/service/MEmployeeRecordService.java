/**
 * CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */

package com.cmcc.zysoft.groupaddressbook.mobile.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmcc.zysoft.groupaddressbook.dto.WalkRecordDto;
import com.cmcc.zysoft.groupaddressbook.mobile.dao.MEmployeeRecordDao;
import com.cmcc.zysoft.groupaddressbook.mobile.dao.MovementDao;
import com.cmcc.zysoft.sellmanager.model.EmployeeRecord;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
import com.starit.common.dao.support.Pagination;

/**
 * @author 周瑜
 * @email zhouyusgs#ahmobile.com
 * @date 2013-6-11 上午11:38:29
 */

@Service
public class MEmployeeRecordService extends BaseServiceImpl<EmployeeRecord, String> {

	@Resource
	private MEmployeeRecordDao mEmployeeRecordDao;
	
	@Resource
	private MovementDao movementDao;
	
	@Override
	public HibernateBaseDao<EmployeeRecord, String> getHibernateBaseDao() {
		return this.mEmployeeRecordDao;
	}
	
	/**
	 * 上传用户的运动记录
	 * @param userCode
	 * @param token
	 * @param value
	 * @return
	 * @throws ParseException 
	 */
	public List<EmployeeRecord> uploadRecord(String userCode, String token, String value, String movementId) throws ParseException {
		//判断当前用户是否有活动归属
		return this.mEmployeeRecordDao.uploadRecord(userCode, token, value,movementId);
	}
	
	/**
	 * 健步记录列表
	 * @param rows:行数
	 * @param page：页码
	 * @param company_id：公司id，在部门id为空时，取当前公司所有人的健步记录
	 * @param department_id：按部门id进行检索
	 * @param usercode：按employee_id进行检索
	 * @param startDate：开始日期
	 * @param endDate：结束日期
	 * @param movment_id：活动编号
	 * @return
	 */
	public Pagination<?> getRecordList(int rows,int page,String company_id,String department_id,String usercode,String user_name,
			String startDate,String endDate,String movement_id)
	{
		return this.mEmployeeRecordDao.getRecordList(rows, page, company_id, department_id, usercode, user_name, startDate, endDate, movement_id);
	}
	
	/**
	 * 代替单元测试，打印所有已上传的作弊未作弊清单
	 */
	public List<EmployeeRecord> getAllSportRecord()
	{
		return this.mEmployeeRecordDao.getAllSportRecord();
	}
	
	/**
	 * 获取某次运动的轨迹
	 * @param employee_record_id
	 * @return
	 */
	public List<Map<String,Object>> getGpsByEmployeeRecordId(String employee_record_id)
	{
		return this.mEmployeeRecordDao.getGpsByEmployeeRecordId(employee_record_id);
	}
	
	/**
	 * 健步记录列表（供导出
	 * @param company_id：公司id，在部门id为空时，取当前公司所有人的健步记录
	 * @param department_id：按部门id进行检索
	 * @param usercode：按employee_id进行检索
	 * @param startDate：开始日期
	 * @param endDate：结束日期
	 * @param movement_id: 活动编号
	 * @return
	 */
	public List<WalkRecordDto> getRecordExportSource(String company_id,String department_id,String usercode,String user_name,String startDate,String endDate,String movement_id)
	{
		return this.mEmployeeRecordDao.getRecordExportSource(company_id, department_id, usercode, user_name, startDate, endDate, movement_id);
	}
}

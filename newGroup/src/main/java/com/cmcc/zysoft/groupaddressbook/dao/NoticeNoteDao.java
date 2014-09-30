// ~ CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
package com.cmcc.zysoft.groupaddressbook.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.cmcc.zysoft.sellmanager.model.NoticeNote;
import com.starit.common.dao.hibernate.HibernateBaseDaoImpl;
import com.starit.common.dao.jdbc.NamedParameterJdbcTemplateExt;

/**
 * @author 张军
 * <br />邮箱： zhang.jun3@ustcinfo.com
 * <br />描述：NoticeNoteDao
 * <br />版本:1.0.0
 * <br />日期： 2014-4-10 上午10:09:16
 * <br />CopyRight © 2012 USTC SINOVATE  SOFTWARE CO.LTD All Rights Reserved.
 */
@Repository
public class NoticeNoteDao extends HibernateBaseDaoImpl<NoticeNote, String>{
	
	@Autowired
	private NamedParameterJdbcTemplateExt namedParameterJdbcTemplateExt;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void batchAddNote(final List<Map<String,Object>> list,final String noticeId){
		String sql = "insert into tb_c_notice_note(note_id,notice_id,user_mobile)" +
				"values(replace(uuid(),'-',''),?,?)";
		BatchPreparedStatementSetter set = new BatchPreparedStatementSetter(){

			public int getBatchSize() {

				return list.size();

			}

			public void setValues(PreparedStatement ps, int i) throws SQLException {

				Map<String,Object> map = list.get(i);
	
					ps.setString(1, noticeId);
					ps.setString(2, map.get("mobile").toString());
	
			}
		};
		this.jdbcTemplate.batchUpdate(sql, set);
	}
	public void updateByMobile(final String noticeId,final String[] tels,final String sendType,final String serialNumber,final String result){
		String sql="update tb_c_notice_note set send_type=?,serial_number=?,send_result=?,send_time=sysdate(),send_number = send_number+1 where user_mobile =? and notice_id=?";
		BatchPreparedStatementSetter set = new BatchPreparedStatementSetter(){

			public int getBatchSize() {

				return tels.length;

			}

			public void setValues(PreparedStatement ps, int i) throws SQLException {

					ps.setString(1, sendType);
					ps.setString(2, serialNumber);
					ps.setString(3, result);
					ps.setString(4, tels[i]);
					ps.setString(5, noticeId);
	
			}
		};
		this.jdbcTemplate.batchUpdate(sql, set);
	}
	public void updateImResult(final String noticeId,final JSONArray ims,final String sendType,final String result){
		String sql="update tb_c_notice_note set send_type=?,serial_number=?,send_result=?,send_time=sysdate(),send_number = send_number+1 where user_mobile =? and notice_id=?";
		BatchPreparedStatementSetter set = new BatchPreparedStatementSetter(){

			public int getBatchSize() {

				return ims.size();

			}

			public void setValues(PreparedStatement ps, int i) throws SQLException {

					ps.setString(1, sendType);
					ps.setString(2, ims.getJSONObject(i).getString("sn"));
					ps.setString(3, result);
					ps.setString(4, ims.getJSONObject(i).getString("tel"));
					ps.setString(5, noticeId);
	
			}
		};
		this.jdbcTemplate.batchUpdate(sql, set);
	}
	
	/**
	 * 删除
	 * @param noticeIds
	 */
	public void deleteByNoticeIds(String noticeIds){
		String sql = "DELETE FROM tb_c_notice_note WHERE notice_id IN("+noticeIds+")";
		this.jdbcTemplate.update(sql);
	}
	
	/**
	 * 获取发送失败的手机号
	 * @param noticeId 消息号
	 * @return mobile
	 */
	public List<Map<String,Object>> getFaileMobile(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		String sql = "SELECT GROUP_CONCAT(nn.user_mobile) AS mobile," +
					 "n.notice_id " +
					 "FROM tb_c_notice_note nn,tb_c_notice n " +
				     "WHERE nn.notice_id=n.notice_id " +
					 "AND nn.user_get_time is null " +
					 "AND nn.send_number<3 " +
					 "AND n.send_time >= DATE_ADD(NOW(),INTERVAL -1 DAY) " +
					 "AND n.send_time <= DATE_ADD(NOW(),INTERVAL -30 MINUTE) " +
					 "AND nn.send_type='1' " +
					 "GROUP BY nn.notice_id";
		list = this.jdbcTemplate.queryForList(sql);
		return list;
	}
}

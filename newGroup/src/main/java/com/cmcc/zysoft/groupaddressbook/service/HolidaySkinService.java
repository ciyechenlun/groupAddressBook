package com.cmcc.zysoft.groupaddressbook.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cmcc.zysoft.framework.utils.UploadUtil;
import com.cmcc.zysoft.groupaddressbook.dao.HolidaySkinDao;
import com.cmcc.zysoft.sellmanager.model.Holidayskin;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
import com.starit.common.dao.support.Pagination;

/**
 * @author 周瑜
 * <br />邮箱： zhouyusgs#ahmobile.com
 * <br />描述：HolidaySkinService
 * <br />版本:1.0.0
 * <br />日期： 2013-7-23 上午8:49:16
 * <br />CopyRight © Chinamobile Anhui Cmp Ltd.
 */

@Service
public class HolidaySkinService extends BaseServiceImpl<Holidayskin, String> {

	@Value("${upload.file.path}")
	private String path;
	
	@Value("${project.mobile.img.path}")
	private String projectPath;
	
	@Resource
	private HolidaySkinDao holidaySkinDao; 
	
	/**
	 * 日志.
	 */
	private static final Logger logger = LoggerFactory.getLogger(HolidaySkinService.class);
	
	@Override
	public HibernateBaseDao<Holidayskin, String> getHibernateBaseDao() {
		return this.holidaySkinDao;
	}
	
	/**
	 * 获取节假日皮肤分页列表.
	 * @param rows
	 * @param page
	 * @return pagination
	 */
	public Pagination<?> getSkinList(int rows, int page) {
		return this.holidaySkinDao.getSkinList(rows, page);
	}
	
	/**
	 * 判断同一时间节假日皮肤的唯一性-有:true,无:false.
	 * @param skinId
	 * @param startTime
	 * @param endTime
	 * @return Boolean
	 */
	public Boolean isSkinUseExist(String skinId, Date startTime, Date endTime) {
		List<Holidayskin> list = this.holidaySkinDao.getHolidaySkin(skinId);
		for(Holidayskin skin : list) {
			//Date1.after(Date2),当Date1大于Date2时;返回TRUE,当小于等于时,返回false.
			//Date1.before(Date2),当Date1小于Date2时,返回TRUE;当大于等于时,返回false.
			if(skin.getHolidayskinEndDate().after(startTime) && skin.getHolidayskinStartDate().before(endTime)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 通过皮肤ID查找节假日皮肤.
	 * @param skinId
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getSkinById(String skinId) {
		return this.holidaySkinDao.getSkinById(skinId);
	}
	
	/**
	 * 获取节日皮肤. 
	 * @return map
	 * code说明:0-成功,1-复制皮肤包至工程目录下失败,2-皮肤包不存在或已删除,3-当前时间不存在可用皮肤包
	 */
	public Map<String, Object> getHolidaySkin() {
		Date date = new Date();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Holidayskin> skinList = this.holidaySkinDao.getHolidaySkin(null);
		for(Holidayskin skin : skinList) {
			//当前时间存在可用皮肤包
			if(date.after(skin.getHolidayskinStartDate()) && date.before(skin.getHolidayskinEndDate())) {
				File oldFile = new File(path + File.separator + skin.getHolidayskinPath());
				//检查工程目录是否存在
				File projectDir = new File(projectPath);
				if (!projectDir.exists()) {
					projectDir.mkdirs();
				}
				//设置工程目录写权限
				projectDir.setWritable(true);
				File destFile = new File(projectDir + File.separator + skin.getHolidayskinPath());
				//检查工程目录下是否存在该皮肤包-存在则返回,不存在进原皮肤包复制至工程目录下
				if(!destFile.exists()) { //工程目录下不存在皮肤包-复制
					if(oldFile.exists()) {
						try {
							UploadUtil.copy(oldFile, destFile);
							map.put("code", "0");
							logger.debug("##########  复制皮肤包至工程目录下成功!  ##########");
						} catch (IOException e) {
							e.printStackTrace();
							map.put("code", "1");
							logger.debug("##########  复制皮肤包至工程目录下失败!  ##########");
						}
					} else {
						map.put("code", "2");
						logger.debug("##########  皮肤包不存在或已删除!  ##########");
					}
				} else { //工程目录下存在皮肤包-直接使用
					map.put("code", "0");
					logger.debug("##########  工程目录下已存在皮肤包-直接使用!  ##########");
				}
				map.put("holidayskin", skin);
				break;
			} else {
				map.put("code", "3");
				map.put("holidayskin", "");
				logger.debug("##########  当前时间不存在可用皮肤包!  ##########");
			}
		}
		return map;
	}
}
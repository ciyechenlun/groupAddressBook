package com.cmcc.zysoft.groupaddressbook.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cmcc.zysoft.groupaddressbook.dao.CompanyMapDao;
import com.cmcc.zysoft.sellmanager.model.CompanyMap;
import com.starit.common.dao.hibernate.HibernateBaseDao;
import com.starit.common.dao.service.BaseServiceImpl;
import com.starit.common.dao.support.Pagination;


@Service
public class CompanyMapService extends BaseServiceImpl<CompanyMap, String>{
	
	@Autowired
	private CompanyMapDao companyMapDao;
	
	private String mapPath = "/root/ict/tomcat/apache-tomcat-6.0.35/webapps/ROOT";
	
//	private String mapPath = "D:/map";

	@Override
	public HibernateBaseDao<CompanyMap, String> getHibernateBaseDao() {
		// TODO Auto-generated method stub
		return this.companyMapDao;
	}
	
	/**
	 * 获取map信息
	 * @param rows
	 * @param page
	 * @return
	 */
	public Pagination<?> getCompanyMapInfo(int rows, int page){
		return this.companyMapDao.getMapInfo(rows, page);
	}
	
	
	/**
	 * 添加地图信息
	 * @param mapName
	 * @param companyIds
	 */
	public void setCompanyMap(String mapName,String companyIds,HttpServletRequest request,String companyName){
		//支持多个企业共用地图
		if(StringUtils.hasText(companyIds)){
			String[] companyIdList = companyIds.split(",");
			for(String companyId:companyIdList){
				String mapId = this.companyMapDao.getMayByCompany(companyId);
				//获取最大版本号
				int version = this.companyMapDao.getMaxNum();
				if(StringUtils.hasText(mapId)){
					//已经上传过地图，更新地图
					CompanyMap companyMap = this.getEntity(mapId);
					String oldPath = companyMap.getPath();
					//版本号+1
					companyMap.setVersion(version+1);
					companyMap.setUpdateTime(new Date());
					//更新最新的地图路径
					companyMap.setPath(mapName);
					//文件名
					companyMap.setMark1(companyName);
					this.updateEntity(companyMap);
					//删除旧地图
					String desPath =mapPath+oldPath;
					File oldFile = new File(desPath);
					if(oldFile.exists()){
						oldFile.delete();
					}
				}else{
					//未上传过地图，首次添加
					CompanyMap companyMap = new CompanyMap();
					companyMap.setCompanyId(companyId);
					//版本号+1
					companyMap.setVersion(version+1);
					companyMap.setUpdateTime(new Date());
					companyMap.setPath(mapName);
					//文件名
					companyMap.setMark1(companyName);
					this.insertEntity(companyMap);
				}
			}
		}
	}
	
	/**
	 * 上传文件
	 * @param mapZip
	 * @param request
	 * @return
	 */
	public String uploadPhoto(MultipartFile mapZip,HttpServletRequest request){
		//文件名处理
		String lastName = System.currentTimeMillis()+".zip";
//		//保存路径
//		String fileFullPath = path+File.separator+lastName;
		//将头像复制到工程下面，手机端直接查看
		String desPath =mapPath+ "/resources/map/"+lastName;
		try {
			//保存文件
//			FileCopyUtils.copy(logo.getBytes(),new File(fileFullPath));
			//将头像复制到工程下面，手机端直接查看
			FileCopyUtils.copy(mapZip.getBytes(),new File(desPath));
			return "/resources/map/"+lastName;
		} catch (IOException e) {
			e.printStackTrace();
			return "ERROR";
			
		} 
	}

}

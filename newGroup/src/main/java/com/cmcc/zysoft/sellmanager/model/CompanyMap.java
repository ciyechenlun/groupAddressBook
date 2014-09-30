package com.cmcc.zysoft.sellmanager.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name="tb_c_map")
public class CompanyMap implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 244190864344864752L;
	
	private String mapId;
	private String companyId;
	private Date updateTime;
	private int version;
	private String path;
	private String mark1;
	private String mark2;
	
	public CompanyMap(){
		
	}
	
	public CompanyMap(String mapId,String companyId,Date updateTime,int version,String path,String mark1,String mark2){
		this.mapId = mapId;
		this.companyId = companyId;
		this.updateTime = updateTime;
		this.version = version;
		this.path = path;
		this.mark1 = mark1;
		this.mark2 = mark2;
	}

    @GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
    @Column(name="map_id", unique=true, nullable=false, length=32)
	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	@Column(name="company_id", length=32)
	public String getCompanyId() {
		return companyId;
	}

	
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_time", length=19)
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@Column(name="version")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(name="path", length=256)
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Column(name="mark1", length=256)
	public String getMark1() {
		return mark1;
	}

	public void setMark1(String mark1) {
		this.mark1 = mark1;
	}

	@Column(name="mark2", length=256)
	public String getMark2() {
		return mark2;
	}

	public void setMark2(String mark2) {
		this.mark2 = mark2;
	}
	
	
	

}

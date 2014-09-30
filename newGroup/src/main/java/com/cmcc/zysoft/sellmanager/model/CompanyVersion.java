package com.cmcc.zysoft.sellmanager.model;
// Generated 2013-5-27 10:55:59 by Hibernate Tools 3.2.2.GA


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * CompanyVersion generated by hbm2java
 */
@Entity
@Table(name="tb_c_company_version")
public class CompanyVersion  implements java.io.Serializable {


     /**
	 * 属性名称：serialVersionUID <br/>
	 * 类型：long
	 */
	 private static final long serialVersionUID = -2455878521540242952L;
	 private int companyVersion;
     private Date updateDate;
     private String mark1;
     private String mark2;

    public CompanyVersion() {
    }

	
    public CompanyVersion(int companyVersion) {
        this.companyVersion = companyVersion;
    }
    public CompanyVersion(int companyVersion, Date updateDate, String mark1, String mark2) {
       this.companyVersion = companyVersion;
       this.updateDate = updateDate;
       this.mark1 = mark1;
       this.mark2 = mark2;
    }
   
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="company_version", unique=true, nullable=false)
    public int getCompanyVersion() {
        return this.companyVersion;
    }
    
    public void setCompanyVersion(int companyVersion) {
        this.companyVersion = companyVersion;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="update_date", length=19)
    public Date getUpdateDate() {
        return this.updateDate;
    }
    
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    
    @Column(name="mark1", length=200)
    public String getMark1() {
        return this.mark1;
    }
    
    public void setMark1(String mark1) {
        this.mark1 = mark1;
    }
    
    @Column(name="mark2", length=200)
    public String getMark2() {
        return this.mark2;
    }
    
    public void setMark2(String mark2) {
        this.mark2 = mark2;
    }




}


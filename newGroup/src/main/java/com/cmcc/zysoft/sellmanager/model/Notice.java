package com.cmcc.zysoft.sellmanager.model;
// Generated 2013-2-28 14:16:39 by Hibernate Tools 3.2.2.GA


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

/**
 * Notice generated by hbm2java
 */
@Entity
@Table(name="tb_c_notice")
public class Notice  implements java.io.Serializable {


	 private static final long serialVersionUID = 5760625193810142824L;
	 private String noticeId;
     private String publicId;
     private String status;
     private Date saveTime;
     private String noticeTitle;
     private String noticeContent;
     private Date sendTime;
     private String userCompanyId;
     private String sendMessageResult;
     private String sendMmsResult;
     private String url;
     private String mark1;


   
    public Notice() {
	}

	public Notice(String noticeId, String publicId, String status,
			Date saveTime, String noticeTitle, String noticeContent,
			Date sendTime, String userCompanyId, String sendMessageResult,
			String sendMmsResult, String url,String mark1) {
		this.noticeId = noticeId;
		this.publicId = publicId;
		this.status = status;
		this.saveTime = saveTime;
		this.noticeTitle = noticeTitle;
		this.noticeContent = noticeContent;
		this.sendTime = sendTime;
		this.userCompanyId = userCompanyId;
		this.sendMessageResult = sendMessageResult;
		this.sendMmsResult = sendMmsResult;
		this.url = url;
		this.mark1 = mark1;
		
	}

	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
    @Column(name="notice_id", unique=true, nullable=false, length=32)
    public String getNoticeId() {
        return this.noticeId;
    }
    
    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }
    
    @Column(name="public_id",length=32)
    public String getPublicId() {
        return publicId;
    }
    
    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }
    
    @Column(name="status", length=1)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="save_time")
	public Date getSaveTime() {
		return saveTime;
	}

	public void setSaveTime(Date saveTime) {
		this.saveTime = saveTime;
	}
	 @Column(name="notice_title", length=100)
	public String getNoticeTitle() {
		return noticeTitle;
	}

	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}
	 @Column(name="notice_content")
	public String getNoticeContent() {
		return noticeContent;
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}
	 @Temporal(TemporalType.TIMESTAMP)
	 @Column(name="send_time")
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
    @Column(name="user_company_id",length=32)
	public String getUserCompanyId() {
		return userCompanyId;
	}

	public void setUserCompanyId(String userCompanyId) {
		this.userCompanyId = userCompanyId;
	}
	@Column(name="send_message_result",length=100)
	public String getSendMessageResult() {
		return sendMessageResult;
	}

	public void setSendMessageResult(String sendMessageResult) {
		this.sendMessageResult = sendMessageResult;
	}
	@Column(name="send_mms_result",length=100)
	public String getSendMmsResult() {
		return sendMmsResult;
	}

	public void setSendMmsResult(String sendMmsResult) {
		this.sendMmsResult = sendMmsResult;
	}
	@Column(name="url",length=100)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	@Column(name="mark1",length=50)
	public String getMark1() {
		return mark1;
	}

	public void setMark1(String mark1) {
		this.mark1 = mark1;
	}
    
    
    
    
}


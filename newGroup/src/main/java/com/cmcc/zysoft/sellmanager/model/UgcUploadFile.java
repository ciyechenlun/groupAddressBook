package com.cmcc.zysoft.sellmanager.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

import com.cmcc.zysoft.groupaddressbook.mobile.util.BaseObject;

/**
 * UGC上传文件bean
 * 
 * @author Administrator
 * 
 */
@XmlRootElement
public class UgcUploadFile extends BaseObject implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String fileLocalName;
	private String fileLocalPropertiesName;
	private Long startOffset;
	private Long endOffset;
	private String fileMd5;
	private String programTitle;
	private String programDesc;
	private String fileType;
	private Long fileSize;
	private String fileName;
	private String fileCreatetime;
	private Long segmentAmount;
	private String createTime;// 创建时间

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileLocalName() {
		return fileLocalName;
	}

	public void setFileLocalName(String fileLocalName) {
		this.fileLocalName = fileLocalName;
	}

	public String getFileLocalPropertiesName() {
		return fileLocalPropertiesName;
	}

	public void setFileLocalPropertiesName(String fileLocalPropertiesName) {
		this.fileLocalPropertiesName = fileLocalPropertiesName;
	}

	public Long getStartOffset() {
		return startOffset;
	}

	public void setStartOffset(Long startOffset) {
		this.startOffset = startOffset;
	}

	public Long getEndOffset() {
		return endOffset;
	}

	public void setEndOffset(Long endOffset) {
		this.endOffset = endOffset;
	}

	public String getFileMd5() {
		return fileMd5;
	}

	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}

	public String getProgramTitle() {
		return programTitle;
	}

	public void setProgramTitle(String programTitle) {
		this.programTitle = programTitle;
	}

	public String getProgramDesc() {
		return programDesc;
	}

	public void setProgramDesc(String programDesc) {
		this.programDesc = programDesc;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileCreatetime() {
		return fileCreatetime;
	}

	public void setFileCreatetime(String fileCreatetime) {
		this.fileCreatetime = fileCreatetime;
	}

	public Long getSegmentAmount() {
		return this.segmentAmount;
	}

	public void setSegmentAmount(Long segmentAmount) {
		this.segmentAmount = segmentAmount;
	}

	@Column(name = "CREATE_TIME")
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

}

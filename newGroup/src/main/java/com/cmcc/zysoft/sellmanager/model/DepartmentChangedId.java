package com.cmcc.zysoft.sellmanager.model;
// Generated 2013-2-28 14:16:39 by Hibernate Tools 3.2.2.GA


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * DepartmentChangedId generated by hbm2java
 */
@Embeddable
public class DepartmentChangedId  implements java.io.Serializable {


	 private static final long serialVersionUID = -5679875183163384610L;
	 private String departmentId;
     private int departmentVersionNum;

    public DepartmentChangedId() {
    }

    public DepartmentChangedId(String departmentId, int departmentVersionNum) {
       this.departmentId = departmentId;
       this.departmentVersionNum = departmentVersionNum;
    }
   

    @Column(name="department_id", nullable=false, length=32)
    public String getDepartmentId() {
        return this.departmentId;
    }
    
    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    @Column(name="department_version_num", nullable=false)
    public int getDepartmentVersionNum() {
        return this.departmentVersionNum;
    }
    
    public void setDepartmentVersionNum(int departmentVersionNum) {
        this.departmentVersionNum = departmentVersionNum;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof DepartmentChangedId) ) return false;
		 DepartmentChangedId castOther = ( DepartmentChangedId ) other; 
         
		 return ( (this.getDepartmentId()==castOther.getDepartmentId()) || ( this.getDepartmentId()!=null && castOther.getDepartmentId()!=null && this.getDepartmentId().equals(castOther.getDepartmentId()) ) )
 && (this.getDepartmentVersionNum()==castOther.getDepartmentVersionNum());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getDepartmentId() == null ? 0 : this.getDepartmentId().hashCode() );
         result = 37 * result + this.getDepartmentVersionNum();
         return result;
   }   


}


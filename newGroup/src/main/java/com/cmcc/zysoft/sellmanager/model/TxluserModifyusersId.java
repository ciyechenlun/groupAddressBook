package com.cmcc.zysoft.sellmanager.model;
// Generated 2013-2-28 14:16:39 by Hibernate Tools 3.2.2.GA


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TxluserModifyusersId generated by hbm2java
 */
@Embeddable
public class TxluserModifyusersId  implements java.io.Serializable {


	 private static final long serialVersionUID = -8630636328641508567L;
	 private String employeeId;
     private int txluserVersionNum;

    public TxluserModifyusersId() {
    }

    public TxluserModifyusersId(String employeeId, int txluserVersionNum) {
       this.employeeId = employeeId;
       this.txluserVersionNum = txluserVersionNum;
    }
   

    @Column(name="employee_id", nullable=false, length=32)
    public String getEmployeeId() {
        return this.employeeId;
    }
    
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    @Column(name="txluser_version_num", nullable=false)
    public int getTxluserVersionNum() {
        return this.txluserVersionNum;
    }
    
    public void setTxluserVersionNum(int txluserVersionNum) {
        this.txluserVersionNum = txluserVersionNum;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof TxluserModifyusersId) ) return false;
		 TxluserModifyusersId castOther = ( TxluserModifyusersId ) other; 
         
		 return ( (this.getEmployeeId()==castOther.getEmployeeId()) || ( this.getEmployeeId()!=null && castOther.getEmployeeId()!=null && this.getEmployeeId().equals(castOther.getEmployeeId()) ) )
 && (this.getTxluserVersionNum()==castOther.getTxluserVersionNum());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getEmployeeId() == null ? 0 : this.getEmployeeId().hashCode() );
         result = 37 * result + this.getTxluserVersionNum();
         return result;
   }   


}


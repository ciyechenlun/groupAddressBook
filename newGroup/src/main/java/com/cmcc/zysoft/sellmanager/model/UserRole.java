package com.cmcc.zysoft.sellmanager.model;
// Generated 2013-2-28 14:16:39 by Hibernate Tools 3.2.2.GA


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * UserRole generated by hbm2java
 */
@Entity
@Table(name="tb_c_user_role")
public class UserRole  implements java.io.Serializable {


	 private static final long serialVersionUID = -5158379294594460225L;
	 private String id;
     private SystemUser systemUser;
     private Role role;

    public UserRole() {
    }

	
    public UserRole(String id) {
        this.id = id;
    }
    public UserRole(String id, SystemUser systemUser, Role role) {
       this.id = id;
       this.systemUser = systemUser;
       this.role = role;
    }
   
    @GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
    @Column(name="id", unique=true, nullable=false, length=32)
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    public SystemUser getSystemUser() {
        return this.systemUser;
    }
    
    public void setSystemUser(SystemUser systemUser) {
        this.systemUser = systemUser;
    }
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="role_id")
    public Role getRole() {
        return this.role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }




}


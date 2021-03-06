package com.cmcc.zysoft.sellmanager.model;
// Generated 2013-8-16 19:28:35 by Hibernate Tools 3.2.2.GA


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
 * MovementUsers generated by hbm2java
 */
@Entity
@Table(name="tb_b_movement_users")
public class MovementUsers  implements java.io.Serializable {


     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String movementUsersId;
     private Movement movement;
     private String employeeId;
     private String mark1;
     private String mark2;

    public MovementUsers() {
    }

	
    public MovementUsers(String movementUsersId) {
        this.movementUsersId = movementUsersId;
    }
    public MovementUsers(String movementUsersId, Movement movement, String employeeId, String mark1, String mark2) {
       this.movementUsersId = movementUsersId;
       this.movement = movement;
       this.employeeId = employeeId;
       this.mark1 = mark1;
       this.mark2 = mark2;
    }
   
    @GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
    @Column(name="movement_users_id", unique=true, nullable=false, length=32)
    public String getMovementUsersId() {
        return this.movementUsersId;
    }
    
    public void setMovementUsersId(String movementUsersId) {
        this.movementUsersId = movementUsersId;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="movement_id")
    public Movement getMovement() {
        return this.movement;
    }
    
    public void setMovement(Movement movement) {
        this.movement = movement;
    }
    
    @Column(name="employee_id", length=32)
    public String getEmployeeId() {
        return this.employeeId;
    }
    
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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



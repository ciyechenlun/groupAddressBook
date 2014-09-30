package com.cmcc.zysoft.sellmanager.model;
// Generated 2013-2-28 14:16:39 by Hibernate Tools 3.2.2.GA


import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.hibernate.annotations.GenericGenerator;


/**
 * Menu generated by hbm2java
 */
@Entity
@Table(name="tb_c_menu")
public class Menu  implements java.io.Serializable {


	 private static final long serialVersionUID = 7904430349211193644L;
	 private String menuId;
	 @JsonBackReference
     private Company company;
     private String parentId;
     private String metaId;
     private String menuName;
     private String path;
     private String isLeaf;
     private Integer displayOrder;
     private String icon;
     private String status;
     private String comment;
     private Set<MenuElement> menuElements = new HashSet<MenuElement>(0);

    public Menu() {
    }

	
    public Menu(String menuId) {
        this.menuId = menuId;
    }
    public Menu(String menuId, Company company, String parentId, String metaId, String menuName, String path, String isLeaf, Integer displayOrder, String icon, String status, String comment, Set<MenuElement> menuElements) {
       this.menuId = menuId;
       this.company = company;
       this.parentId = parentId;
       this.metaId = metaId;
       this.menuName = menuName;
       this.path = path;
       this.isLeaf = isLeaf;
       this.displayOrder = displayOrder;
       this.icon = icon;
       this.status = status;
       this.comment = comment;
       this.menuElements = menuElements;
    }
   
    @GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
    @Column(name="menu_id", unique=true, nullable=false, length=32)
    public String getMenuId() {
        return this.menuId;
    }
    
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="company_id")
    public Company getCompany() {
        return this.company;
    }
    
    public void setCompany(Company company) {
        this.company = company;
    }
    
    @Column(name="parent_id", length=32)
    public String getParentId() {
        return this.parentId;
    }
    
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    
    @Column(name="meta_id", length=32)
    public String getMetaId() {
        return this.metaId;
    }
    
    public void setMetaId(String metaId) {
        this.metaId = metaId;
    }
    
    @Column(name="menu_name", length=64)
    public String getMenuName() {
        return this.menuName;
    }
    
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
    
    @Column(name="path", length=256)
    public String getPath() {
        return this.path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    @Column(name="is_leaf", length=1)
    public String getIsLeaf() {
        return this.isLeaf;
    }
    
    public void setIsLeaf(String isLeaf) {
        this.isLeaf = isLeaf;
    }
    
    @Column(name="display_order")
    public Integer getDisplayOrder() {
        return this.displayOrder;
    }
    
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    @Column(name="icon", length=256)
    public String getIcon() {
        return this.icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    @Column(name="status", length=1)
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Column(name="comment", length=256)
    public String getComment() {
        return this.comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    public Set<MenuElement> getMenuElements() {
        return this.menuElements;
    }
    
    public void setMenuElements(Set<MenuElement> menuElements) {
        this.menuElements = menuElements;
    }




}


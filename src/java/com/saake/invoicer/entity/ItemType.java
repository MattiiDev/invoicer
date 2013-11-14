/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jn
 */
@Entity
@Table(name = "item_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ItemType.findAll", query = "SELECT i FROM ItemType i"),
    @NamedQuery(name = "ItemType.findByItemTypeId", query = "SELECT i FROM ItemType i WHERE i.itemTypeId = :itemTypeId"),
    @NamedQuery(name = "ItemType.findByCode", query = "SELECT i FROM ItemType i WHERE i.code = :code"),
    @NamedQuery(name = "ItemType.findByName", query = "SELECT i FROM ItemType i WHERE i.name = :name"),
    @NamedQuery(name = "ItemType.findByParentItemTypeId", query = "SELECT i FROM ItemType i WHERE i.parentItemTypeId = :parentItemTypeId")})
public class ItemType implements Serializable {
    @Size(max = 20)
    @Column(name = "ITEM_CATEGORY")
    private String itemCategory;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATE_TS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTs;
    @Size(max = 20)
    @Column(name = "CREATED_BY")
    private String createdBy;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ITEM_TYPE_ID")
    private Integer itemTypeId;
    @Size(max = 25)
    @Column(name = "CODE")
    private String code;
    @Size(max = 50)
    @Column(name = "NAME")
    private String name;
    @Column(name = "PARENT_ITEM_TYPE_ID")
    private Integer parentItemTypeId;
//    @OneToMany(mappedBy = "itemTypeId")
//    private Collection<Item> itemCollection;

    public ItemType() {
    }

    public ItemType(Integer itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public Integer getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(Integer itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentItemTypeId() {
        return parentItemTypeId;
    }

    public void setParentItemTypeId(Integer parentItemTypeId) {
        this.parentItemTypeId = parentItemTypeId;
    }

//    @XmlTransient
//    public Collection<Item> getItemCollection() {
//        return itemCollection;
//    }
//
//    public void setItemCollection(Collection<Item> itemCollection) {
//        this.itemCollection = itemCollection;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (itemTypeId != null ? itemTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemType)) {
            return false;
        }
        ItemType other = (ItemType) object;
        if ((this.itemTypeId == null && other.itemTypeId != null) || (this.itemTypeId != null && !this.itemTypeId.equals(other.itemTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saake.invoicer.entity.ItemType[ itemTypeId=" + itemTypeId + " ]";
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public Date getCreateTs() {
        return createTs;
    }

    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
}

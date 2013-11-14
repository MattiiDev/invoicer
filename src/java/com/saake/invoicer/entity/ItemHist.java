/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jn
 */
@Entity
@Table(name = "item_hist")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ItemHist.findAll", query = "SELECT i FROM ItemHist i"),
    @NamedQuery(name = "ItemHist.findByItemHistId", query = "SELECT i FROM ItemHist i WHERE i.itemHistId = :itemHistId"),
    @NamedQuery(name = "ItemHist.findByName", query = "SELECT i FROM ItemHist i WHERE i.name = :name"),
    @NamedQuery(name = "ItemHist.findByDescription", query = "SELECT i FROM ItemHist i WHERE i.description = :description"),
    @NamedQuery(name = "ItemHist.findByUnitCost", query = "SELECT i FROM ItemHist i WHERE i.unitCost = :unitCost"),
    @NamedQuery(name = "ItemHist.findByCreateTs", query = "SELECT i FROM ItemHist i WHERE i.createTs = :createTs"),
    @NamedQuery(name = "ItemHist.findByCreatedBy", query = "SELECT i FROM ItemHist i WHERE i.createdBy = :createdBy")})
public class ItemHist implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ITEM_HIST_ID")
    private Integer itemHistId;
    @Size(max = 50)
    @Column(name = "NAME")
    private String name;
    @Size(max = 250)
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "UNIT_COST")
    private Integer unitCost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATE_TS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTs;
    @Size(max = 20)
    @Column(name = "CREATED_BY")
    private String createdBy;
    @JoinColumn(name = "ITEM_ID", referencedColumnName = "ITEM_ID")
    @ManyToOne(optional = false)
    private Item itemId;

    public ItemHist() {
    }

    public ItemHist(Integer itemHistId) {
        this.itemHistId = itemHistId;
    }

    public ItemHist(Integer itemHistId, Date createTs) {
        this.itemHistId = itemHistId;
        this.createTs = createTs;
    }

    public Integer getItemHistId() {
        return itemHistId;
    }

    public void setItemHistId(Integer itemHistId) {
        this.itemHistId = itemHistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(Integer unitCost) {
        this.unitCost = unitCost;
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

    public Item getItemId() {
        return itemId;
    }

    public void setItemId(Item itemId) {
        this.itemId = itemId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (itemHistId != null ? itemHistId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemHist)) {
            return false;
        }
        ItemHist other = (ItemHist) object;
        if ((this.itemHistId == null && other.itemHistId != null) || (this.itemHistId != null && !this.itemHistId.equals(other.itemHistId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saake.invoicer.entity.ItemHist[ itemHistId=" + itemHistId + " ]";
    }
    
}

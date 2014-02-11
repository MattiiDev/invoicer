/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.entity;

import com.saake.invoicer.util.Utils;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jn
 */
@Entity
@Table(name = "work_order_items")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WorkOrderItems.findAll", query = "SELECT w FROM WorkOrderItems w"),
    @NamedQuery(name = "WorkOrderItems.findByWorkOrderItemsId", query = "SELECT w FROM WorkOrderItems w WHERE w.workOrderItemsId = :workOrderItemsId")})
public class WorkOrderItems implements Serializable,Comparable {
    private static final long serialVersionUID = 1L;
    
    static WorkOrderItems copy(WorkOrderItems current, WorkOrderItems that) {
        that.workOrderItemsId = current.workOrderItemsId;
        that.quantity = current.quantity;
        that.description = current.description;
        that.unitPrice = current.unitPrice;
        that.discount = current.discount;
        that.amount = current.amount;
        that.item = current.item;
        that.createTs = current.createTs;
        that.updateTs = current.updateTs;
        that.createdBy = current.createdBy;
        that.updatedBy = current.updatedBy;
        that.workOrderId = current.workOrderId;
        
        return that;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WORK_ORDER_ITEMS_ID")
    private Integer workOrderItemsId;
    
    @Column(name = "QUANTITY")
    private Integer quantity;
    
    @Column(name = "DESCRIPTION")
    private String description;
    
    @Column(name = "UNIT_PRICE")
    private Double unitPrice;
    
    @Column(name = "DISCOUNT")
    private Double discount;
    
    @Column(name = "AMOUNT")
    private Double amount;
    
    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;
    
    @Column(name = "CREATE_TS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTs;
    
    @Column(name = "UPDATE_TS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTs;
    
    @Column(name = "CREATED_BY")
    private String createdBy;
    
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    
    @JoinColumn(name = "WORK_ORDER_ID", referencedColumnName = "WORK_ORDER_ID")
    @ManyToOne
    private WorkOrder workOrderId;

    @Transient
    private boolean addItem = false;
    
    public WorkOrderItems() {
    }

    public WorkOrderItems(Integer workOrderItemsId) {
        this.workOrderItemsId = workOrderItemsId;
    }

    public WorkOrderItems(Integer workOrderItemsId, Date createTs, Date updateTs) {
        this.workOrderItemsId = workOrderItemsId;
        this.createTs = createTs;
        this.updateTs = updateTs;
    }

    public Integer getWorkOrderItemsId() {
        return workOrderItemsId;
    }

    public void setWorkOrderItemsId(Integer workOrderItemsId) {
        this.workOrderItemsId = workOrderItemsId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }


    public Date getCreateTs() {
        return createTs;
    }

    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    public Date getUpdateTs() {
        return updateTs;
    }

    public void setUpdateTs(Date updateTs) {
        this.updateTs = updateTs;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public WorkOrder getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(WorkOrder workOrderId) {
        this.workOrderId = workOrderId;
    }

   @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.workOrderItemsId);
        hash = 83 * hash + Objects.hashCode(this.quantity);
        hash = 83 * hash + Objects.hashCode(this.description);
        hash = 83 * hash + Objects.hashCode(this.unitPrice);
        hash = 83 * hash + Objects.hashCode(this.discount);
        hash = 83 * hash + Objects.hashCode(this.amount);
        hash = 83 * hash + Objects.hashCode(this.workOrderId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
        final WorkOrderItems other = (WorkOrderItems) obj;
        if (!Objects.equals(this.workOrderItemsId, other.workOrderItemsId)) {
            return false;
        }
        if (!Objects.equals(this.quantity, other.getQuantity())) {
            return false;
        }
        if (!Objects.equals(this.description, other.getDescription())) {
            return false;
        }
        if (!Objects.equals(this.unitPrice, other.getUnitPrice())) {
            return false;
        }
        if (!Objects.equals(this.discount, other.getDiscount())) {
            return false;
        }
        if (!Objects.equals(this.amount, other.getAmount())) {
            return false;
        }
        if (!Objects.equals(this.workOrderId, other.workOrderId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saake.invoicer.entity.WorkOrderItems[ workOrderItemsId=" + workOrderItemsId + " ]";
    }    
    
     public boolean isEmptyForUse() {
        return (amount == null || amount == 0.0)
                //                && (discount == null || discount == 0.0) 
                //                && invoiceItemId == null 
                && Utils.isBlank(description)
                //                && (item == null || item.getItemId() == null ) && 
                //(quantity == null || quantity == 0)  
                && (unitPrice == null || unitPrice == 0.0);
    }
     
    public boolean isEmpty() {
        return (amount == null || amount == 0.0) && workOrderItemsId == null && Utils.isBlank(description) && 
                (quantity == null || quantity == 0)  &&
                 (unitPrice == null || unitPrice == 0.0);
    }

        public boolean isAddItem() {
        return addItem;
    }

    public void setAddItem(boolean addItem) {
        this.addItem = addItem;
    }

    public void reset(){
        this.setDescription(null);
        this.setUnitPrice(0.0);
        this.setAmount(0.0);
        this.setQuantity(1);
        this.setItem(null);
        this.setAddItem(false);
    }
    
    @Override
    public int compareTo(Object o) {
        int val = 0;

        if (o instanceof WorkOrderItems) {
            WorkOrderItems that = (WorkOrderItems)o;
            
            if(this.workOrderItemsId != null && that.workOrderItemsId != null ){
                val = that.workOrderItemsId.compareTo(workOrderItemsId);
            }
            else if(this.amount != null && that.amount != null ){
                val = that.amount.compareTo(amount);
            }
            else if(this.description != null && that.description != null ){
                val = that.description.compareTo(description);
            }
            else if(this.discount != null && that.amount != null ){
                val = that.amount.compareTo(amount);
            }
            else if(this.quantity != null && that.quantity != null ){
                val = that.quantity.compareTo(quantity);
            }
            else if(this.unitPrice != null && that.unitPrice != null ){
                val = that.unitPrice.compareTo(unitPrice);
            }
            else if(this.workOrderId != null && this.workOrderId.getWorkOrderId()!= null && that.workOrderId != null && that.workOrderId.getWorkOrderId() != null){
                val = that.workOrderId.getWorkOrderId().compareTo(workOrderId.getWorkOrderId());
            }
        }
        
        return val;
    }
    
}

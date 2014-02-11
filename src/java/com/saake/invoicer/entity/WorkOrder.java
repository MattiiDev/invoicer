/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.entity;

import com.google.common.base.Objects;
import com.saake.invoicer.util.Utils;
import com.saake.invoicer.util.WorkOrderStatusEnum;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "work_order")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WorkOrder.findAll", query = "SELECT w FROM WorkOrder w"),
    @NamedQuery(name = "WorkOrder.findByWorkOrderId", query = "SELECT w FROM WorkOrder w WHERE w.workOrderId = :workOrderId"),
    @NamedQuery(name = "WorkOrder.findByWorkOrderNum", query = "SELECT w FROM WorkOrder w WHERE w.workOrderNum = :workOrderNum")})
public class WorkOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    public static WorkOrder copy(WorkOrder that, WorkOrder current) {
        
        current.workOrderId = that.workOrderId;
        current.workOrderNum = that.workOrderNum;
        current.notes = that.notes;
        current.workOrderDate = that.workOrderDate;
        current.discount = that.discount;
        current.amount = that.amount;
        current.customerId = that.customerId;
        current.assignedUser = that.assignedUser;
        current.vehicle = that.vehicle;
        current.status = that.status;
        current.isInvoiced = that.isInvoiced;
        current.createTs = that.createTs;
        current.updateTs = that.updateTs;
        current.invoicedTs = that.invoicedTs;
        current.createdBy = that.createdBy;
        current.updatedBy = that.updatedBy;
        
        if(that.workOrderItems != null){
            current.workOrderItems = new ArrayList<>();
            for(WorkOrderItems woItems : that.workOrderItems){
                WorkOrderItems thatItem = WorkOrderItems.copy(woItems, new WorkOrderItems());
                thatItem.setWorkOrderItemsId(null);
                thatItem.setAddItem(false);
                thatItem.setCreateTs(new Date());
                thatItem.setUpdateTs(null);
                thatItem.setUpdatedBy(null);
                current.workOrderItems.add(thatItem) ;                
            }
        }
        
        return current;        
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WORK_ORDER_ID")
    private Integer workOrderId;
    
    @Column(name = "WORK_ORDER_NUM")
    private String workOrderNum;
    
    @Column(name = "NOTES")
    private String notes;
    
    @Column(name = "WORK_ORDER_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date workOrderDate;
    
    @Column(name = "DISCOUNT")
    private Integer discount;
    
    @Column(name = "AMOUNT")
    private Double amount;
    
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID")
    @ManyToOne
    private Customer customerId;

    @JoinColumn(name = "ASSIGNED_USER_ID", referencedColumnName = "user_id")
    @OneToOne
    private User assignedUser;

    @JoinColumn(name = "vehicle_id", referencedColumnName = "vehicle_id")
    @ManyToOne
    private Vehicle vehicle;
    
    @Column(name = "STATUS")
    private String status;

    @Column(name = "IS_INVOICED")
    private String isInvoiced;

    @Column(name = "CREATE_TS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTs;
    
    @Column(name = "UPDATE_TS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTs;
    
    @Column(name = "INVOICED_TS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date invoicedTs;
    
    @Column(name = "CREATED_BY")
    private String createdBy;
    
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    
    @OneToMany(mappedBy = "workOrderId", cascade = CascadeType.ALL)
    private List<WorkOrderItems> workOrderItems;

    public WorkOrder() {
    }

    public WorkOrder(Integer workOrderId) {
        this.workOrderId = workOrderId;
    }

    public WorkOrder(Integer workOrderId, Date workOrderDate, Date createTs, Date updateTs) {
        this.workOrderId = workOrderId;
        this.workOrderDate = workOrderDate;
        this.createTs = createTs;
        this.updateTs = updateTs;
    }

    public Integer getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Integer workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getWorkOrderNum() {
        return workOrderNum;
    }

    public void setWorkOrderNum(String workOrderNum) {
        this.workOrderNum = workOrderNum;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getWorkOrderDate() {
        return workOrderDate;
    }

    public void setWorkOrderDate(Date workOrderDate) {
        this.workOrderDate = workOrderDate;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
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

    public String getIsInvoiced() {
        return isInvoiced;
    }

    public void setIsInvoiced(String isInvoiced) {
        this.isInvoiced = isInvoiced;
    }

    public Date getInvoicedTs() {
        return invoicedTs;
    }

    public void setInvoicedTs(Date invoicedTs) {
        this.invoicedTs = invoicedTs;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (workOrderId != null ? workOrderId.hashCode() : 0);
        return hash;
    }

    @XmlTransient
    public List<WorkOrderItems> getWorkOrderItems() {
        return workOrderItems;
    }

    public void setWorkOrderItems(List<WorkOrderItems> workOrderItems) {
        this.workOrderItems = workOrderItems;
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }       

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WorkOrder)) {
            return false;
        }
        WorkOrder other = (WorkOrder) object;
        if ((this.workOrderId == null && other.workOrderId != null) || (this.workOrderId != null && !this.workOrderId.equals(other.workOrderId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saake.invoicer.entity.WorkOrder[ workOrderId=" + workOrderId + " ]";
    }
    
     public boolean isEmpty() {
        return this.customerId == null && this.vehicle == null && (this.amount == null || this.amount == 0) && this.workOrderDate == null 
                && Utils.isBlank(this.notes) && this.workOrderId == null && Utils.isEmpty(this.workOrderItems) 
                && Utils.isBlank(this.status);
    }
     
     public Double getItemTotalAmount() {
        Double tot = 0.0;
        if(getWorkOrderItems() != null ){
            for (WorkOrderItems oItm : getWorkOrderItems()) {
                if (oItm.getAmount() != null) {
                    tot = tot + oItm.getAmount();
                }
            
            }
        }

        return tot;
    }
     
    public boolean isInvoiced(){
        return Utils.convertYorNToBoolean(isInvoiced);
    }
    
    public boolean isAssigned(){
        return assignedUser != null;
    }
    
    public boolean isAccepted(){
        return status.equals(WorkOrderStatusEnum.ACCEPTED.name());
    }
    
    public boolean isDraft(){
        return Objects.equal(status,WorkOrderStatusEnum.DRAFT.name());
    }
     
    public String getStatusDisplay() {
        return Utils.notBlank(status)? WorkOrderStatusEnum.valueOf(status).getValue() : "";
    }

    public String getStatusColor(){
        return Utils.notBlank(status)? 
                status.equals(WorkOrderStatusEnum.DRAFT.name())? "navy":
                status.equals(WorkOrderStatusEnum.DELETED.name())? "gray":
                status.equals(WorkOrderStatusEnum.SENT.name())? "limegreen":
                status.equals(WorkOrderStatusEnum.CLOSED.name())? "gray":
                status.equals(WorkOrderStatusEnum.ACCEPTED.name())? "limegreen": "blue":"";
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.entity;

import com.google.common.base.Objects;
import com.saake.invoicer.util.InvoiceStatusEnum;
import com.saake.invoicer.util.TransTypeEnum;
import com.saake.invoicer.util.Utils;
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
import javax.persistence.FetchType;
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
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author jn
 */
@Entity
@Table(name = "invoice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Invoice.findAll", query = "SELECT i FROM Invoice i"),
    @NamedQuery(name = "Invoice.findByInvoiceId", query = "SELECT i FROM Invoice i WHERE i.invoiceId = :invoiceId"),
    @NamedQuery(name = "Invoice.findByInvoiceNum", query = "SELECT i FROM Invoice i WHERE i.invoiceNum = :invoiceNum"),
    @NamedQuery(name = "Invoice.findByInvoiceDetails", query = "SELECT i FROM Invoice i WHERE i.invoiceDetails = :invoiceDetails"),
    @NamedQuery(name = "Invoice.findByInvoiceDate", query = "SELECT i FROM Invoice i WHERE i.invoiceDate = :invoiceDate")})
public class Invoice implements Serializable, SelectableDataModel<Invoice> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INVOICE_ID")
    private Integer invoiceId;
    
    @Column(name = "INVOICE_NUM")
    private String invoiceNum;
    
    @Column(name = "INVOICE_DETAILS")
    private String invoiceDetails;
    
    @Column(name = "STATUS")
    private String status;
    
    @Column(name = "INVOICE_DATE")
    @Temporal(TemporalType.TIMESTAMP)       
    private Date invoiceDate;
    
    @Column(name = "DISCOUNT")    
    private Double discount;
    
    @Column(name = "AMOUNT")
    private Double amount;
    
    @Column(name = "ADVANCE_AMT")
    private Double advanceAmount;
    
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
    
    @OneToMany(mappedBy = "invoiceId", cascade = CascadeType.ALL)
    private Collection<Transaction> transactions;
    
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private Collection<InvoiceItems> invoiceItems;
    
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID")
    @ManyToOne
    private Customer customerId;
    
    @JoinColumn(name = "vehicle_id", referencedColumnName = "vehicle_id")
    @ManyToOne
    private Vehicle vehicle;
    
    @JoinColumn(name = "WORK_ORDER_ID", referencedColumnName = "WORK_ORDER_ID")
    @OneToOne
    private WorkOrder workOrder;

    public Invoice() {
    }

    public Invoice(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Invoice(Integer invoiceId, Date invoiceDate) {
        this.invoiceId = invoiceId;
        this.invoiceDate = invoiceDate;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getInvoiceDetails() {
        return invoiceDetails;
    }

    public void setInvoiceDetails(String invoiceDetails) {
        this.invoiceDetails = invoiceDetails;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Collection<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Collection<Transaction> transactions) {
        this.transactions = transactions;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
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

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public Collection<InvoiceItems> getInvoiceItems() {
        return invoiceItems;
    }

    public List<InvoiceItems> getInvoiceItemsAsList() {
        return new ArrayList<InvoiceItems>(invoiceItems);
    }

    public void setInvoiceItems(Collection<InvoiceItems> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    public String getStatusDisplay() {
        return Utils.notBlank(status)? InvoiceStatusEnum.valueOf(status).getValue() : "";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Double getAdvanceAmount() {
        return advanceAmount;
    }

    public void setAdvanceAmount(Double advanceAmount) {
        this.advanceAmount = advanceAmount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (invoiceId != null ? invoiceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Invoice)) {
            return false;
        }
        Invoice other = (Invoice) object;
        if ((this.invoiceId == null && other.invoiceId != null) || (this.invoiceId != null && !this.invoiceId.equals(other.invoiceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saake.invoicer.entity.Invoice[ invoiceId=" + invoiceId + " ]";
    }

    public Double getItemTotalAmount() {
        Double tot = 0.0;
        for (InvoiceItems oItm : getInvoiceItems()) {
            if (oItm.getAmount() != null) {
                tot = tot + oItm.getAmount();
            }
        }

        return tot;
    }

    public Double getTransactionAmount() {
        Double tot = 0.0;
        if (getTransactions() != null) {
            for (Transaction trans : getTransactions()) {
                if (trans.getTransType().equals(TransTypeEnum.PAYMENT.getValue())) {
                    if (trans.getAmount() != null) {
                        tot = tot + trans.getAmount();
                    }
                }
                if (trans.getTransType().equals(TransTypeEnum.REFUND.getValue())) {
                    tot = tot - trans.getAmount();
                }
            }
        }

        return tot;
    }

    @Override
    public Object getRowKey(Invoice t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Invoice getRowData(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isEmpty() {
        return this.customerId == null && this.vehicle == null && (this.amount == null || this.amount == 0) && this.invoiceDate == null && (this.discount == null || this.discount == 0)
                && Utils.isBlank(this.invoiceDetails) && this.invoiceId == null && Utils.isEmpty(this.invoiceItems) && this.invoiceNum == null
                && Utils.isBlank(this.status);
    }
    
        
    public boolean isPaid(){
        return Objects.equal(status,InvoiceStatusEnum.PAID.name());
    }
    
    public boolean isDraft(){
        return Objects.equal(status,InvoiceStatusEnum.DRAFT.name());
    }
    
    public String getStatusColor(){
        return Utils.notBlank(status)? 
                status.equals(InvoiceStatusEnum.DRAFT.name())? "navy":
                status.equals(InvoiceStatusEnum.DELETED.name())? "gray":
                status.equals(InvoiceStatusEnum.CLOSED.name())? "gray":
                status.equals(InvoiceStatusEnum.OVERPAY.name())? "red":
                status.equals(InvoiceStatusEnum.PARTIALPAY.name())? "orange":
                status.equals(InvoiceStatusEnum.PAID.name())? "limegreen": "blue":"";
    }
}

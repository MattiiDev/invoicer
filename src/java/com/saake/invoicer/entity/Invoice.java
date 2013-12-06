/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.entity;

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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author jn
 */
@Entity
@Table(name = "simple_invoice")
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
//    @Basic(optional = false)
//    @NotNull
    @Column(name = "INVOICE_ID")
    private Integer invoiceId;
    
    @Column(name = "INVOICE_NUM")
    private String invoiceNum;
    
//    @Size(max = 250)
    @Column(name = "INVOICE_DETAILS")
    private String invoiceDetails;
    
    @Column(name = "STATUS")
    private String status;
    
//    @Basic(optional = false)
//    @NotNull
    @Column(name = "INVOICE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date invoiceDate;
    
    @Column(name = "DISCOUNT")
    private Double discount;
    
    @Column(name = "AMOUNT")
    private Double amount;
    
//    @OneToMany(mappedBy = "invoiceId")
//    private Collection<Transactions> transactions;
//    
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private Collection<InvoiceItems> invoiceItems;
    
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID")
    @ManyToOne
    private Customer customerId;
    
//    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ORDER_ID")
//    @ManyToOne
//    private Orders order;

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

    //    @XmlTransient
    //    public Collection<Transactions> getTransactionCollection() {
    //        return transactions;
    //    }
    //
    //    public void setTransactionCollection(Collection<Transactions> transactions) {
    //        this.transactions = transactions;
    //    }
    //    public Orders getOrder() {
    //        return order;
    //    }
    //
    //    public void setOrder(Orders order) {
    //        this.order = order;
    //    }
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
    
    public Double getItemTotalAmount(){
        Double tot = 0.0;
         for (InvoiceItems oItm : getInvoiceItems()) {
            if (oItm.getAmount()!= null) {
                tot = tot + oItm.getAmount();
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
        return  this.customerId == null && (this.amount == null || this.amount == 0) && this.invoiceDate == null && (this.discount == null ||  this.discount == 0)
                && Utils.isBlank(this.invoiceDetails) && this.invoiceId == null && Utils.isEmpty(this.invoiceItems) && this.invoiceNum == null &&
                Utils.isBlank(this.status);
    }
}

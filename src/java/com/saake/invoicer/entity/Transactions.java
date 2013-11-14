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
import javax.persistence.OneToOne;
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
@Table(name = "transactions")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transactions.findAll", query = "SELECT t FROM Transactions t"),
    @NamedQuery(name = "Transactions.findByTranId", query = "SELECT t FROM Transactions t WHERE t.tranId = :tranId"),
    @NamedQuery(name = "Transactions.findByTranTs", query = "SELECT t FROM Transactions t WHERE t.tranTs = :tranTs"),
    @NamedQuery(name = "Transactions.findByAmount", query = "SELECT t FROM Transactions t WHERE t.amount = :amount"),
    @NamedQuery(name = "Transactions.findByComments", query = "SELECT t FROM Transactions t WHERE t.comments = :comments")})
public class Transactions implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
//    @Basic(optional = false)
//    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRAN_ID")
    private Integer tranId;
    
//    @Basic(optional = false)
//    @NotNull
    @Column(name = "TRAN_TS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tranTs;
    
    @Column(name = "AMOUNT")
    private Integer amount;
    
//    @Size(max = 1000)
    @Column(name = "COMMENTS")
    private String comments;
    
    @JoinColumn(name = "INVOICE_ID", referencedColumnName = "INVOICE_ID")
    @OneToOne
    private Invoice invoiceId;
    
//    @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ACCOUNT_ID")
//    @ManyToOne
//    private Account accountId;

    @JoinColumn(name = "TRAN_TYPE_ID", referencedColumnName = "TRAN_TYPE_ID")
    @ManyToOne
    private TransactionType tranTypeId;

    public Transactions() {
    }

    public Transactions(Integer tranId) {
        this.tranId = tranId;
    }

    public Transactions(Integer tranId, Date tranTs) {
        this.tranId = tranId;
        this.tranTs = tranTs;
    }

    public Integer getTranId() {
        return tranId;
    }

    public void setTranId(Integer tranId) {
        this.tranId = tranId;
    }

    public Date getTranTs() {
        return tranTs;
    }

    public void setTranTs(Date tranTs) {
        this.tranTs = tranTs;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Invoice getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Invoice invoiceId) {
        this.invoiceId = invoiceId;
    }

    //    public Account getAccountId() {
    //        return accountId;
    //    }
    //
    //    public void setAccountId(Account accountId) {
    //        this.accountId = accountId;
    //    }
    
    public TransactionType getTranTypeId() {
        return tranTypeId;
    }

    public void setTranTypeId(TransactionType tranTypeId) {
        this.tranTypeId = tranTypeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tranId != null ? tranId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transactions)) {
            return false;
        }
        Transactions other = (Transactions) object;
        if ((this.tranId == null && other.tranId != null) || (this.tranId != null && !this.tranId.equals(other.tranId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saake.invoicer.entity.Transactions[ tranId=" + tranId + " ]";
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.entity;

import java.io.Serializable;
import java.util.Objects;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jn
 */
@Entity
@Table(name = "invoice_items")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvoiceItems.findAll", query = "SELECT i FROM InvoiceItems i"),
    @NamedQuery(name = "InvoiceItems.findByInvoiceItemId", query = "SELECT i FROM InvoiceItems i WHERE i.invoiceItemId = :invoiceItemId"),
    @NamedQuery(name = "InvoiceItems.findByQuantity", query = "SELECT i FROM InvoiceItems i WHERE i.quantity = :quantity"),
    @NamedQuery(name = "InvoiceItems.findByDescription", query = "SELECT i FROM InvoiceItems i WHERE i.description = :description"),
    @NamedQuery(name = "InvoiceItems.findByUnitPrice", query = "SELECT i FROM InvoiceItems i WHERE i.unitPrice = :unitPrice"),
    @NamedQuery(name = "InvoiceItems.findByDiscount", query = "SELECT i FROM InvoiceItems i WHERE i.discount = :discount"),
    @NamedQuery(name = "InvoiceItems.findByAmount", query = "SELECT i FROM InvoiceItems i WHERE i.amount = :amount")})
public class InvoiceItems implements Serializable,Comparable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INVOICE_ITEM_ID")
    private Integer invoiceItemId;
    
    @Column(name = "QUANTITY")
    private Integer quantity;
    
    @Column(name = "DESCRIPTION")
    private String description;
    
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ITEM_ID")
    private Item item;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "UNIT_PRICE")
    private Double unitPrice;
    
    @Column(name = "DISCOUNT")
    private Double discount;
    
    @Column(name = "AMOUNT")
    private Double amount;
    
    @JoinColumn(name = "INVOICE_ID", referencedColumnName = "INVOICE_ID")
    @ManyToOne
    private Invoice invoice;

    public InvoiceItems() {
    }

    public InvoiceItems(Integer invoiceItemId) {
        this.invoiceItemId = invoiceItemId;
    }

    public Integer getInvoiceItemId() {
        return invoiceItemId;
    }

    public void setInvoiceItemId(Integer invoiceItemId) {
        this.invoiceItemId = invoiceItemId;
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

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.invoiceItemId);
        hash = 83 * hash + Objects.hashCode(this.quantity);
        hash = 83 * hash + Objects.hashCode(this.description);
        hash = 83 * hash + Objects.hashCode(this.unitPrice);
        hash = 83 * hash + Objects.hashCode(this.discount);
        hash = 83 * hash + Objects.hashCode(this.amount);
        hash = 83 * hash + Objects.hashCode(this.invoice);
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
        
        final InvoiceItems other = (InvoiceItems) obj;
        if (!Objects.equals(this.invoiceItemId, other.invoiceItemId)) {
            return false;
        }
        if (!Objects.equals(this.quantity, other.quantity)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.unitPrice, other.unitPrice)) {
            return false;
        }
        if (!Objects.equals(this.discount, other.discount)) {
            return false;
        }
        if (!Objects.equals(this.amount, other.amount)) {
            return false;
        }
        if (!Objects.equals(this.invoice, other.invoice)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "InvoiceItems{" + "invoiceItemId=" + invoiceItemId + ", quantity=" + quantity + ", description=" + description + ", unitPrice=" + unitPrice + ", discount=" + discount + ", amount=" + amount + '}';
    }

    @Override
    public int compareTo(Object o) {
        int val = 0;

        if (o instanceof InvoiceItems) {
            InvoiceItems that = (InvoiceItems)o;
            
            if(this.invoiceItemId != null && that.invoiceItemId != null ){
                val = that.invoiceItemId.compareTo(invoiceItemId);
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
            else if(this.invoice != null && this.invoice.getInvoiceId() != null && that.invoice != null && that.invoice.getInvoiceId() != null){
                val = that.invoice.getInvoiceId().compareTo(invoice.getInvoiceId());
            }
        }
        
        return val;
    }
        
   
}

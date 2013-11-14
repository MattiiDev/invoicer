/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.model;

import com.saake.invoicer.entity.Customer;
import com.saake.invoicer.util.Utils;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author jn
 */
public class SearchInvoiceVO implements Serializable{
    private Date fromDate;
    private Date toDate;
    private Integer invoiceId;
    private String invoiceNum;
    private String status;
    private String invoicePeriod;
    private Customer customer ;
    private Double fromAmount;
    private Double toAmount;

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public Double getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(Double fromAmount) {
        this.fromAmount = fromAmount;
    }

    public Double getToAmount() {
        return toAmount;
    }

    public void setToAmount(Double toAmount) {
        this.toAmount = toAmount;
    }       

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInvoicePeriod() {
        return invoicePeriod;
    }

    public void setInvoicePeriod(String invoicePeriod) {
        this.invoicePeriod = invoicePeriod;
    }
    
    public boolean empty() {
        return (this.customer == null || this.customer.empty()) && this.fromAmount == null && this.toAmount == null
                && this.fromDate == null && this.toDate == null && this.invoiceId == null && Utils.isBlank(this.invoiceNum) 
                && Utils.isBlank(this.status) && Utils.isBlank(this.invoicePeriod);
                
    } 
       
}

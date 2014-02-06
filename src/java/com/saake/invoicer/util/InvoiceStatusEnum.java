/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.util;

/**
 *
 * @author jn
 */
public enum InvoiceStatusEnum {
    
    DRAFT ("Draft" ),
    PAID ("Paid" ),
    PARTIALPAY ("Partial Paid"),
    OVERPAY ("Over Paid"),
    DELETED ("Deleted"),
    CLOSED ("Closed");
    
    private String value;

    private InvoiceStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}

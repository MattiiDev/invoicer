/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.util;

/**
 *
 * @author jn
 */
public enum TransTypeEnum {
    
    PAYMENT ("Payment"),
    REFUND ("Refund");
    
    private String value;

    private TransTypeEnum(String status) {
        this.value = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}

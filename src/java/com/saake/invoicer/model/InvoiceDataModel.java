/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.model;

import com.saake.invoicer.entity.Invoice;
import java.io.Serializable;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author jn
 */
public class InvoiceDataModel extends ListDataModel<Invoice> implements Serializable,SelectableDataModel<Invoice> { 
    
    public InvoiceDataModel() {
    }
    
    
    public InvoiceDataModel(List<Invoice> data) {
        super(data);
    }

    @Override
    public Object getRowKey(Invoice t) {
        return t.getInvoiceId();
    }

    @Override
    public Invoice getRowData(String rowKey) {
        List<Invoice> list = (List<Invoice>) getWrappedData();  
          
        for(Invoice inv : list) {  
            if(inv.getInvoiceId().equals(rowKey))  
                return inv;  
        }  
          
        return null; 
    }        
}

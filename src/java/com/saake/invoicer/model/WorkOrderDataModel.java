/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.model;

import com.saake.invoicer.entity.WorkOrder;
import java.io.Serializable;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author jn
 */
public class WorkOrderDataModel extends ListDataModel<WorkOrder> implements Serializable,SelectableDataModel<WorkOrder> { 
    
    public WorkOrderDataModel() {
    }
    
    
    public WorkOrderDataModel(List<WorkOrder> data) {
        super(data);
    }

    @Override
    public Object getRowKey(WorkOrder t) {
        return t.getWorkOrderId();
    }

    @Override
    public WorkOrder getRowData(String rowKey) {
        List<WorkOrder> list = (List<WorkOrder>) getWrappedData();  
          
        for(WorkOrder inv : list) {  
            if(inv.getWorkOrderId().equals(rowKey))  
                return inv;  
        }  
          
        return null; 
    }        
}

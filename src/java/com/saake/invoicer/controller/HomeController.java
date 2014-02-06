/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.controller;

import com.saake.invoicer.util.JsfUtil;
import com.saake.invoicer.util.Utils;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

/**
 *
 * @author jn
 */
@ManagedBean(name = "homeCtrl")
@ViewScoped
public class HomeController implements Serializable {

    private static final Log log = LogFactory.getLog(HomeController.class);
    
    private DashboardModel model;
    
    public HomeController() {
        log.info("Inside HomeController!!!");
    }
        
    @PostConstruct
    private void init() {
        model = new DefaultDashboardModel();  
        DashboardColumn column1 = new DefaultDashboardColumn();  
        DashboardColumn column2 = new DefaultDashboardColumn();  
        DashboardColumn column3 = new DefaultDashboardColumn();  
          
        column2.addWidget("invoice");  
        column1.addWidget("workorder");  
          
//        column2.addWidget("lifestyle");  
//        column2.addWidget("weather");  
//          
//        column3.addWidget("politics");  
  
        model.addColumn(column1);  
        model.addColumn(column2);  
//        model.addColumn(column3);  
    }

    public DashboardModel getModel() {
        return model;
    }

    public void setModel(DashboardModel model) {
        this.model = model;
    }   
    
}

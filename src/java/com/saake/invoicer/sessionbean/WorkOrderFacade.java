/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.sessionbean;

import com.saake.invoicer.entity.Invoice;
import com.saake.invoicer.entity.InvoiceItems;
import com.saake.invoicer.entity.Item;
import com.saake.invoicer.entity.WorkOrder;
import com.saake.invoicer.entity.WorkOrderItems;
import com.saake.invoicer.util.InvoiceStatusEnum;
import com.saake.invoicer.util.Utils;
import com.saake.invoicer.util.WorkOrderStatusEnum;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jn
 */
@Stateless
public class WorkOrderFacade extends AbstractFacade<WorkOrder> {

    private static final Log log = LogFactory.getLog(WorkOrderFacade.class);
    @PersistenceContext(unitName = "invoicerPU")
    private EntityManager em;

    @EJB
    ItemFacade itemSvc;
    
    @EJB
    CustomerFacade custSvc;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public List<WorkOrder> findAll() {

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();

        Root<WorkOrder> root = cq.from(WorkOrder.class);
        cq.select(root);

        ParameterExpression<String> status = cb.parameter(String.class);
//        cq.where(cb.notEqual(invRoot.get("status"), status));
        cq.orderBy(cb.desc(root.get("workOrderId")));
        Query query = getEntityManager().createQuery(cq);
//        query.setParameter(status, InvoiceStatusEnum.DELETE.getValue());

        return (List<WorkOrder>) query.getResultList();
    }

    public WorkOrderFacade() {
        super(WorkOrder.class);
    }

    public WorkOrder createWorkOrder(WorkOrder wo) {
        
        preSaveActions(wo);
        
        wo.setStatus(WorkOrderStatusEnum.DRAFT.name());

        List<WorkOrderItems> items = wo.getWorkOrderItems();
        wo.setWorkOrderItems(null);
        
        create(wo);
        em.flush();
        
        for(WorkOrderItems woi: items){
            woi.setWorkOrderId(wo);
            em.persist(woi);
            em.flush();
        }
        
        wo.setWorkOrderItems(items);
        
        em.merge(wo);
        //em.flush();
        
        return wo;

    }

    public WorkOrder updateWorkOrder(WorkOrder wo) {
        preSaveActions(wo);

        //Reset this
        wo.setIsInvoiced("N");
        wo.setInvoicedTs(null);

        wo = edit(wo);

        return wo;
    }
    
    private WorkOrder preSaveActions(WorkOrder wo) {        
        //Save new vehicles.
        wo.getVehicle().setCustomerId(wo.getCustomerId());
        wo.setVehicle(custSvc.saveCustomerVehicle(wo.getVehicle()));
            
        //Add new items, if needed.
        if (Utils.notEmpty(wo.getWorkOrderItems())) {                      
            wo = addNewItemsIfNeeded(wo);                        
        }
        
        return wo;
    }
    
    private WorkOrder addNewItemsIfNeeded(WorkOrder wo) {
        List<WorkOrderItems> newItemsList = new ArrayList<>();

        for (WorkOrderItems items : wo.getWorkOrderItems()) {
            if (items.isAddItem()) {
                newItemsList.add(items);
            }
        }

        if (Utils.notEmpty(newItemsList)) {
            for (WorkOrderItems invItem : newItemsList) {
                invItem.setItem(itemSvc.saveItem(new Item(invItem.getDescription(), invItem.getUnitPrice(), "product")));
            }
        }

        return wo;
    }
    
    public void softDelete(WorkOrder current) {
        current.setStatus(InvoiceStatusEnum.DELETED.name());

        em.merge(current);
    }

    public WorkOrder getWorkOrder(int id) {
        WorkOrder wo = em.find(WorkOrder.class,id);
        
        return wo;
    }

    public Invoice convertToInvoice(WorkOrder workOrder) {
        Invoice invoice = null;
        if(workOrder != null){
            invoice = new Invoice();
            
            invoice.setAmount(workOrder.getAmount());
            invoice.setCustomerId(workOrder.getCustomerId());
            invoice.setInvoiceDate(new Date());
            invoice.setInvoiceDetails(workOrder.getNotes());            
            invoice.setVehicle(workOrder.getVehicle());
            invoice.setWorkOrder(workOrder);
            invoice.setCreateTs(new Date());
            invoice.setStatus(InvoiceStatusEnum.DRAFT.name());
            invoice.setInvoiceItems(new ArrayList<InvoiceItems>());
            for(WorkOrderItems woItem : workOrder.getWorkOrderItems()){
                InvoiceItems invItem = new InvoiceItems();
                invItem.setAmount(woItem.getAmount());
                invItem.setCreateTs(new Date());
                invItem.setDescription(woItem.getDescription());
                invItem.setItem(woItem.getItem());
                invItem.setQuantity(woItem.getQuantity());
                invItem.setUnitPrice(woItem.getUnitPrice());
                invItem.setInvoice(invoice);
                
                invoice.getInvoiceItems().add(invItem);
            }
            
            em.persist(invoice);
            
            workOrder.setUpdateTs(new Date());           
            workOrder.setStatus(WorkOrderStatusEnum.ACCEPTED.name());
            workOrder.setIsInvoiced("Y");
            workOrder.setInvoicedTs(new Date());
            
            em.merge(workOrder);            
        }
        
        return invoice;

    }

    public WorkOrder assignWorkOrder(WorkOrder current) {
        return em.merge(current);
    }

}

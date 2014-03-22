/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.sessionbean;

import com.saake.invoicer.entity.Invoice;
import com.saake.invoicer.entity.InvoiceItems;
import com.saake.invoicer.entity.Item;
import com.saake.invoicer.entity.Transaction;
import com.saake.invoicer.entity.WorkOrderItems;
import com.saake.invoicer.reports.ReportHelper;
import com.saake.invoicer.util.InvoiceStatusEnum;
import com.saake.invoicer.util.Utils;
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
public class InvoiceFacade extends AbstractFacade<Invoice> {

    private static final Log log = LogFactory.getLog(InvoiceFacade.class);
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

    
    public InvoiceFacade() {
        super(Invoice.class);
    }

    @Override
    public List<Invoice> findAll() {

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();

        Root<Invoice> invRoot = cq.from(Invoice.class);
        cq.select(invRoot);

        ParameterExpression<String> status = cb.parameter(String.class);
//        cq.where(cb.notEqual(invRoot.get("status"), status));
        cq.orderBy(cb.desc(invRoot.get("invoiceId")));
        Query query = getEntityManager().createQuery(cq);
//        query.setParameter(status, InvoiceStatusEnum.DELETE.getValue());

        return (List<Invoice>) query.getResultList();
    }

    public Invoice createInvoice(Invoice invoice){    
        preSaveActions(invoice);
        
        invoice.setStatus(InvoiceStatusEnum.DRAFT.name());
        //create(invoice);   
        
        List<InvoiceItems> items = invoice.getInvoiceItemsAsList();
        invoice.setInvoiceItems(null);
        
        create(invoice);
        em.flush();
        
        for(InvoiceItems woi: items){
            woi.setInvoice(invoice);
            em.persist(woi);
            em.flush();
        }
        
        invoice.setInvoiceItems(items);
        
        em.merge(invoice);
        
        return invoice;
        
    }
    
    
    public Invoice updateInvoice(Invoice invoice) {
        preSaveActions(invoice);
        
        invoice = edit(invoice);            
        
        return invoice;
    }
    
    public void softDelete(Invoice current) {
        current.setStatus(InvoiceStatusEnum.DELETED.name());

        em.merge(current);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Transaction addTransaction(Transaction trans) {
        if (trans != null) {
            if (trans.getTranId() == null) {
                trans.setCreateTs(new Date());
                em.persist(trans);
            } else {
                trans.setUpdateTs(new Date());
                trans = em.merge(trans);
            }
            
            Invoice invoice = em.find(Invoice.class,trans.getInvoiceId().getInvoiceId());
            
            if(invoice.getAmount() == invoice.getTransactionAmount()){
                invoice.setStatus(InvoiceStatusEnum.PAID.name());
            }
            else if(invoice.getAmount() > invoice.getTransactionAmount()){
                invoice.setStatus(InvoiceStatusEnum.PARTIALPAY.name());
            }
            else if(invoice.getAmount() < invoice.getTransactionAmount()){
                invoice.setStatus(InvoiceStatusEnum.OVERPAY.name());
            }            
            
            em.merge(invoice);
            
        }
        return trans;
    }
    
    public Invoice postTransaction(Transaction trans) {
        Invoice invoice = null;

        if (trans != null) {
            invoice = trans.getInvoiceId();

            if (trans.getTranId() == null) {
                trans.setCreateTs(new Date());
            } else {
                trans.setUpdateTs(new Date());
            }
            
            
            if(invoice.getAmount().equals(invoice.getTransactionAmount())){
                invoice.setStatus(InvoiceStatusEnum.PAID.name());
            }
            else if(invoice.getAmount() > invoice.getTransactionAmount()){
                invoice.setStatus(InvoiceStatusEnum.PARTIALPAY.name());
            }
            else if(invoice.getAmount() < invoice.getTransactionAmount()){
                invoice.setStatus(InvoiceStatusEnum.OVERPAY.name());
            }            
            
            em.merge(invoice);
            em.flush();
            
        }
        return invoice;
    }

    public Invoice getInvoice(int invoiceId) {
        Invoice invoice = em.find(Invoice.class,invoiceId);
//        invoice.getTransactions();
        
        return invoice;
    }

    private Invoice addNewItemsIfNeeded(Invoice invoice) {
        List<InvoiceItems> newItemsList = new ArrayList<>();

        for (InvoiceItems items : invoice.getInvoiceItems()) {
            if (items.isAddItem()) {
                newItemsList.add(items);
            }
        }

        if (Utils.notEmpty(newItemsList)) {
            for (InvoiceItems invItem : newItemsList) {
                invItem.setItem(itemSvc.saveItem(new Item(invItem.getDescription(), invItem.getUnitPrice(), "product")));
            }
        }

        return invoice;
    }

    private Invoice preSaveActions(Invoice invoice) {        
        //Save new vehicles.
        invoice.getVehicle().setCustomerId(invoice.getCustomerId());
        invoice.setVehicle(custSvc.saveCustomerVehicle(invoice.getVehicle()));
            
        //Add new items, if needed.
        if (Utils.notEmpty(invoice.getInvoiceItems())) {                      
            invoice = addNewItemsIfNeeded(invoice);                        
        }
        
        return invoice;
    }

}

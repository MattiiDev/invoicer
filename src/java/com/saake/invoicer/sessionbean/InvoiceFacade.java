/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.sessionbean;

import com.saake.invoicer.entity.Invoice;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

/**
 *
 * @author jn
 */

@Stateless
public class InvoiceFacade extends AbstractFacade<Invoice> {
    @PersistenceContext(unitName = "invoicerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @Override
     public List<Invoice> findAll() {
        
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
        Root<Invoice> invRoot = cq.from(Invoice.class);
        cq.select(invRoot);
        cq.orderBy(cb.desc(invRoot.get("invoiceId")));
        return (List<Invoice>)getEntityManager().createQuery(cq).getResultList();
    }

    public InvoiceFacade() {
        super(Invoice.class);
    }
    
}

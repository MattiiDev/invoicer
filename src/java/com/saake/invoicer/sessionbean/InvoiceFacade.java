/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.sessionbean;

import com.saake.invoicer.entity.Invoice;
import com.saake.invoicer.reports.ReportHelper;
import com.saake.invoicer.util.InvoiceStatusEnum;
import java.util.List;
import javax.ejb.Stateless;
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
    private static final Log log = LogFactory.getLog(ReportHelper.class);

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
        
        ParameterExpression<String> status = cb.parameter(String.class);
//        cq.where(cb.notEqual(invRoot.get("status"), status));
        cq.orderBy(cb.desc(invRoot.get("invoiceId")));
        Query query = getEntityManager().createQuery(cq);
//        query.setParameter(status, InvoiceStatusEnum.DELETE.getValue());
        
        return (List<Invoice>)query.getResultList();
    }

    public InvoiceFacade() {
        super(Invoice.class);
    }

    public void softDelete(Invoice current) {
        current.setStatus(InvoiceStatusEnum.DELETE.name());

        em.merge(current);
    }
    
}

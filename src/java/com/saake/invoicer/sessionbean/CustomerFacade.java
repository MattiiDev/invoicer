/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.sessionbean;

import com.saake.invoicer.entity.Customer;
import com.saake.invoicer.entity.Vehicle;
import com.saake.invoicer.entity.Item;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jn
 */
@Stateless
public class CustomerFacade extends AbstractFacade<Customer> {
    @PersistenceContext(unitName = "invoicerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CustomerFacade() {
        super(Customer.class);
    }

    public Vehicle saveCustomerVehicle(Vehicle custVehicle) {
        if(custVehicle.getCustVehicleId() == null){
            em.persist(custVehicle);
        }
        else{
            if(!custVehicle.equals(em.find(custVehicle.getClass(),custVehicle.getCustVehicleId()))){
                custVehicle = em.merge(custVehicle);
            }
        }
        
        em.flush();
        
        return custVehicle;
    }
           
    @Override
    public List<Customer> findAll() {
//
//        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
//        javax.persistence.criteria.CriteriaQuery cq = cb.createQuery();
//
//        Root<Item> root = cq.from(Item.class);
//        cq.select(root);
//
//        ParameterExpression<String> status = cb.parameter(String.class);
////        cq.where(cb.notEqual(invRoot.get("status"), status));
//        cq.orderBy(cb.desc(root.get("itemId")));
//        Query query = getEntityManager().createQuery(cq);
////        query.setParameter(status, InvoiceStatusEnum.DELETE.getValue());

        return (List<Customer>) em.createQuery("select t from Customer t where t.status is null or t.status = 'INACTIVE' order by t.customerId desc", Customer.class).getResultList();
    }

    public void softDelete(Customer current) {
        current.setStatus("DELETED");

        em.merge(current);
    }

    public Vehicle getVehicle(Integer id) {
        return (Vehicle) em.createNamedQuery("Vehicle.findByCustVehicleId").setParameter("vehicleId", id).getResultList().get(0);
    }
    
}

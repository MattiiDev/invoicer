/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.entity;

import com.saake.invoicer.util.Utils;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jn
 */
@Entity
@Table(name = "customer_vehicle")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CustomerVehicle.findAll", query = "SELECT c FROM CustomerVehicle c"),
    @NamedQuery(name = "CustomerVehicle.findByCustVehicleId", query = "SELECT c FROM CustomerVehicle c WHERE c.custVehicleId = :custVehicleId"),
    @NamedQuery(name = "CustomerVehicle.findByMake", query = "SELECT c FROM CustomerVehicle c WHERE c.make = :make"),
    @NamedQuery(name = "CustomerVehicle.findByModel", query = "SELECT c FROM CustomerVehicle c WHERE c.model = :model"),
    @NamedQuery(name = "CustomerVehicle.findByVin", query = "SELECT c FROM CustomerVehicle c WHERE c.vin = :vin"),
    @NamedQuery(name = "CustomerVehicle.findByCreateTs", query = "SELECT c FROM CustomerVehicle c WHERE c.createTs = :createTs"),
    @NamedQuery(name = "CustomerVehicle.findByUpdateTs", query = "SELECT c FROM CustomerVehicle c WHERE c.updateTs = :updateTs"),
    @NamedQuery(name = "CustomerVehicle.findByCreatedBy", query = "SELECT c FROM CustomerVehicle c WHERE c.createdBy = :createdBy"),
    @NamedQuery(name = "CustomerVehicle.findByUpdatedBy", query = "SELECT c FROM CustomerVehicle c WHERE c.updatedBy = :updatedBy")})
public class CustomerVehicle implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cust_vehicle_id")
    private Integer custVehicleId;
    
    @Column(name = "year")
    private String year;

    @Column(name = "make")
    private String make;

    @Column(name = "model")
    private String model;

    @Column(name = "mileage")
    private String mileage;

    @Column(name = "vin")
    private String vin;

    @Column(name = "create_ts")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTs;

    @Column(name = "update_ts")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTs;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;
    
    @JoinColumn(name = "customer_id", referencedColumnName = "CUSTOMER_ID")
    @ManyToOne
    private Customer customerId;

    public CustomerVehicle() {
    }

    public CustomerVehicle(Integer custVehicleId) {
        this.custVehicleId = custVehicleId;
    }

    public CustomerVehicle(Integer custVehicleId, Date createTs, Date updateTs) {
        this.custVehicleId = custVehicleId;
        this.createTs = createTs;
        this.updateTs = updateTs;
    }

    public Integer getCustVehicleId() {
        return custVehicleId;
    }

    public void setCustVehicleId(Integer custVehicleId) {
        this.custVehicleId = custVehicleId;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Date getCreateTs() {
        return createTs;
    }

    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    public Date getUpdateTs() {
        return updateTs;
    }

    public void setUpdateTs(Date updateTs) {
        this.updateTs = updateTs;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (custVehicleId != null ? custVehicleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CustomerVehicle)) {
            return false;
        }
        CustomerVehicle other = (CustomerVehicle) object;
        if ((this.custVehicleId == null && other.custVehicleId != null) || (this.custVehicleId != null && !this.custVehicleId.equals(other.custVehicleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saake.invoicer.entity.CustomerVehicle[ custVehicleId=" + custVehicleId + " ]";
    }    

    public boolean isEmpty() {
        return Utils.isBlank(vin) && Utils.isBlank(make) && Utils.isBlank(model) && Utils.isBlank(year) && Utils.isBlank(mileage); 
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.entity;

import com.google.common.base.Objects;
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
@Table(name = "vehicle")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Vehicle.findAll", query = "SELECT c FROM Vehicle c"),
    @NamedQuery(name = "Vehicle.findByCustVehicleId", query = "SELECT c FROM Vehicle c WHERE c.vehicleId = :vehicleId"),
    @NamedQuery(name = "Vehicle.findByMake", query = "SELECT c FROM Vehicle c WHERE c.make = :make"),
    @NamedQuery(name = "Vehicle.findByModel", query = "SELECT c FROM Vehicle c WHERE c.model = :model"),
    @NamedQuery(name = "Vehicle.findByVin", query = "SELECT c FROM Vehicle c WHERE c.vin = :vin"),
    @NamedQuery(name = "Vehicle.findByCreateTs", query = "SELECT c FROM Vehicle c WHERE c.createTs = :createTs"),
    @NamedQuery(name = "Vehicle.findByUpdateTs", query = "SELECT c FROM Vehicle c WHERE c.updateTs = :updateTs"),
    @NamedQuery(name = "Vehicle.findByCreatedBy", query = "SELECT c FROM Vehicle c WHERE c.createdBy = :createdBy"),
    @NamedQuery(name = "Vehicle.findByUpdatedBy", query = "SELECT c FROM Vehicle c WHERE c.updatedBy = :updatedBy")})
public class Vehicle implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private Integer vehicleId;
    
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

    public Vehicle() {
    }

    public Vehicle(Integer custVehicleId) {
        this.vehicleId = custVehicleId;
    }

    public Vehicle(Integer custVehicleId, Date createTs, Date updateTs) {
        this.vehicleId = custVehicleId;
        this.createTs = createTs;
        this.updateTs = updateTs;
    }

    public Integer getCustVehicleId() {
        return vehicleId;
    }

    public void setCustVehicleId(Integer custVehicleId) {
        this.vehicleId = custVehicleId;
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
        int hash = 7;
        hash = 31 * hash + java.util.Objects.hashCode(this.vehicleId);
        hash = 31 * hash + java.util.Objects.hashCode(this.year);
        hash = 31 * hash + java.util.Objects.hashCode(this.make);
        hash = 31 * hash + java.util.Objects.hashCode(this.model);
        hash = 31 * hash + java.util.Objects.hashCode(this.mileage);
        hash = 31 * hash + java.util.Objects.hashCode(this.vin);
        hash = 31 * hash + java.util.Objects.hashCode(this.customerId);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vehicle)) {
            return false;
        }
        Vehicle other = (Vehicle) object;
        if ((this.vehicleId == null && other.vehicleId != null) || (this.vehicleId != null && !this.vehicleId.equals(other.vehicleId))) {
            return false;
        }
        else{
            if(!Objects.equal(this.make, other.make) || !Objects.equal(this.model, other.model) || !Objects.equal(this.year, other.year) ||
                    !!Objects.equal(this.vin, other.vin) || !!Objects.equal(this.mileage, other.mileage)){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saake.invoicer.entity.CustomerVehicle[ custVehicleId=" + vehicleId + " ]";
    }    

    public boolean isEmpty() {
        return 
//                Utils.isBlank(vin) || 
//                Utils.isBlank(make) || 
                Utils.isBlank(model) && Utils.isBlank(year) 
//                || Utils.isBlank(mileage)
                ; 
    }
    
    public boolean getIsEmpty() {
        return isEmpty();
    }
}

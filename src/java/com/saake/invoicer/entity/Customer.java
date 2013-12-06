/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saake.invoicer.entity;

import com.saake.invoicer.util.Utils;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jn
 */
@Entity
@Table(name = "customer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Customer.findAll", query = "SELECT c FROM Customer c"),
    @NamedQuery(name = "Customer.findByCustomerId", query = "SELECT c FROM Customer c WHERE c.customerId = :customerId"),
    @NamedQuery(name = "Customer.findByCreateTs", query = "SELECT c FROM Customer c WHERE c.createTs = :createTs"),
    @NamedQuery(name = "Customer.findByFirstName", query = "SELECT c FROM Customer c WHERE c.firstName = :firstName"),
    @NamedQuery(name = "Customer.findByMiddleName", query = "SELECT c FROM Customer c WHERE c.middleName = :middleName"),
    @NamedQuery(name = "Customer.findByLastName", query = "SELECT c FROM Customer c WHERE c.lastName = :lastName"),
    @NamedQuery(name = "Customer.findByGivenName", query = "SELECT c FROM Customer c WHERE c.givenName = :givenName"),
    @NamedQuery(name = "Customer.findByGender", query = "SELECT c FROM Customer c WHERE c.gender = :gender"),
    @NamedQuery(name = "Customer.findByEmail", query = "SELECT c FROM Customer c WHERE c.email = :email"),
    @NamedQuery(name = "Customer.findByAddressLine1", query = "SELECT c FROM Customer c WHERE c.addressLine1 = :addressLine1"),
    @NamedQuery(name = "Customer.findByAddressLine2", query = "SELECT c FROM Customer c WHERE c.addressLine2 = :addressLine2"),
    @NamedQuery(name = "Customer.findByCity", query = "SELECT c FROM Customer c WHERE c.city = :city"),
    @NamedQuery(name = "Customer.findByStateProvince", query = "SELECT c FROM Customer c WHERE c.stateProvince = :stateProvince"),
    @NamedQuery(name = "Customer.findByMobileNum", query = "SELECT c FROM Customer c WHERE c.mobileNum = :mobileNum")})
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOMER_ID")
    private Integer customerId;
    
    @Column(name = "CREATE_TS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTs;
    
    @Column(name = "COMPANY_NAME")
    private String companyName;
    
    @Column(name = "FIRST_NAME")
    private String firstName;
    
    @Column(name = "MIDDLE_NAME")
    private String middleName;
    
    @Column(name = "LAST_NAME")
    private String lastName;
    
    @Column(name = "GIVEN_NAME")
    private String givenName;

    @Column(name = "GENDER")
    private String gender;
    
//    @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Column(name = "EMAIL")
    private String email;
    
    @Column(name = "ADDRESS_LINE_1")
    private String addressLine1;
    
    @Column(name = "ADDRESS_LINE_2")
    private String addressLine2;
    
    @Column(name = "CITY")
    private String city;
    
    @Column(name = "STATE_PROVINCE")
    private String stateProvince;
    
    @Column(name = "COUNTRY")
    private String country;
    
    @Column(name = "MOBILE_NUM")
    private Integer mobileNum;
    
    @Column(name = "OFFICE_PHONE_NUM")
    private Integer officePhoneNum;
    
    @Column(name = "NOTES")
    private String notes;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerId")
    private Collection<Account> accountCollection;

    @Column(name = "ZIP_CODE")
    private String zipCode;

    @OneToMany(mappedBy = "customerId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CustomerVehicle> customerVehicles;
    
    @OneToMany(mappedBy = "customerId")
    private Collection<Orders> order1Collection;
    
    public Customer() {
    }

    public Customer(Integer customerId) {
        this.customerId = customerId;
    }

    public Customer(Integer customerId, Date createTs) {
        this.customerId = customerId;
        this.createTs = createTs;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Date getCreateTs() {
        return createTs;
    }

    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public Integer getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(Integer mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getOfficePhoneNum() {
        return officePhoneNum;
    }

    public void setOfficePhoneNum(Integer officePhoneNum) {
        this.officePhoneNum = officePhoneNum;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @XmlTransient
    public Collection<Account> getAccountCollection() {
        return accountCollection;
    }

    public void setAccountCollection(Collection<Account> accountCollection) {
        this.accountCollection = accountCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.saake.invoicer.entity.Customer[ customerId=" + customerId + " ]";
    }

    @XmlTransient
    public Collection<Orders> getOrder1Collection() {
        return order1Collection;
    }

    public void setOrder1Collection(Collection<Orders> order1Collection) {
        this.order1Collection = order1Collection;
    }

    public boolean empty() {
        return this.customerId == null && Utils.isBlank(this.companyName) && Utils.isBlank(this.firstName);
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @XmlTransient
    public List<CustomerVehicle> getCustomerVehicles() {
        return customerVehicles;
    }

    public void setCustomerVehicles(List<CustomerVehicle> customerVehicleCollection) {
        this.customerVehicles = customerVehicleCollection;
    }
    
}

package org.jarvis.phmart.model.entity;

import org.jarvis.orm.hibernate.domain.base.RecyclableEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created: kim chheng
 * Date: 20-Jan-2019 Sun
 * Time: 11:13 AM
 */
@Entity
@Table(name = "ph_supplier")
public class Supplier extends RecyclableEntity<Integer> {

    private static final long serialVersionUID = 233388527983881232L;
    private String companyName;
    private String companyNameKh;
    private String firstName;
    private String lastName;
    private String email;
    private String phone1;
    private String phone2;
    private String phone3;
    private String address;
    //private Gender gender;
    //private Address address;
    private Set<Product> productSet = new HashSet<>();

    public Supplier() {
    }

    public Supplier(String companyName) {
        this.companyName = companyName;
    }

    @Id
    @SequenceGenerator(name = "supplierSeq", sequenceName = "ph_supplier_sup_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "supplierSeq", strategy = GenerationType.SEQUENCE)
    @Column(name = "sup_id", nullable = false, unique = true)
    @Override
    public Integer getId() {
        return id;
    }

    /*@Convert(converter = GenderConverter.class)
    @Column(name = "gen_value", length = 50)
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }*/

    @Column(name = "sup_address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "sup_company_name", length = 100)
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Column(name = "sup_company_name_kh")
    public String getCompanyNameKh() {
        return companyNameKh;
    }

    public void setCompanyNameKh(String companyNameKh) {
        this.companyNameKh = companyNameKh;
    }

    @Column(name = "sup_first_name", length = 100)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "sup_last_name", length = 100)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "sup_email", length = 100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "sup_phone1", length = 20)
    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    @Column(name = "sup_phone2", length = 20)
    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    @Column(name = "sup_phone3", length = 20)
    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    @OneToMany(mappedBy = "supplier", fetch = FetchType.EAGER)
    public Set<Product> getProductSet() {
        return productSet;
    }

    public void setProductSet(Set<Product> productSet) {
        this.productSet = productSet;
    }

    /*@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "add_id")
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }*/

    @Override
    public String toString() {
        return companyName == null || companyName.isEmpty() ? "N/A" : companyName;
    }

    @Override
    public boolean equals(Object that) {
        return equals(that, "companyName");
    }
}

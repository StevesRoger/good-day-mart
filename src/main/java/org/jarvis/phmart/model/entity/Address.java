package org.jarvis.phmart.model.entity;

import org.jarvis.orm.hibernate.domain.base.RecyclableEntity;

import javax.persistence.*;

/**
 * Created: kim chheng
 * Date: 20-Jan-2019 Sun
 * Time: 3:26 PM
 */
//@Entity
//@Table(name = "ph_address")
public class Address extends RecyclableEntity<Integer> {

    private static final long serialVersionUID = -2351924590800969008L;

    private String houseNo;
    private String streetNo;
    private String village;
    private String commune;
    private String district;
    private String province;
    private String country;
    private String postcode;
    private Supplier supplier;

    @Id
    @SequenceGenerator(name = "addressSeq", sequenceName = "ph_address_add_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "addressSeq", strategy = GenerationType.SEQUENCE)
    @Column(name = "add_id", nullable = false, unique = true)
    @Override
    public Integer getId() {
        return id;
    }

    @Column(name = "add_house_no", length = 100)
    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    @Column(name = "add_street_no", length = 100)
    public String getStreetNo() {
        return streetNo;
    }

    public void setStreetNo(String streetNo) {
        this.streetNo = streetNo;
    }

    @Column(name = "add_village", length = 100)
    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    @Column(name = "add_commune", length = 100)
    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    @Column(name = "add_district", length = 100)
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Column(name = "add_province", length = 100)
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Column(name = "add_country", length = 100)
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Column(name = "add_postcode", length = 100)
    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @OneToOne(mappedBy = "address", fetch = FetchType.LAZY)
    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}

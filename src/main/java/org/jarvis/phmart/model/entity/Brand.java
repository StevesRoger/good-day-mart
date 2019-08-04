package org.jarvis.phmart.model.entity;

import org.jarvis.orm.hibernate.domain.base.NamingEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created: kim chheng
 * Date: 19-Jan-2019 Sat
 * Time: 3:01 PM
 */
//@Entity
//@Table(name = "ph_brand")
public class Brand extends NamingEntity<Integer> {

    private static final long serialVersionUID = -6492401781634957012L;

    private Float price;
    private Float discount;
    private String description;
    private Set<Product> productSet = new HashSet<>();

    @Id
    @SequenceGenerator(name = "brandSeq", sequenceName = "ph_brand_bra_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "brandSeq", strategy = GenerationType.SEQUENCE)
    @Column(name = "bra_id", nullable = false, unique = true)
    @Override
    public Integer getId() {
        return id;
    }

    @Column(name = "bar_price")
    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    @Column(name = "bar_discount")
    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    @Column(name = "bar_description", length = 150)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToMany(mappedBy = "brandSet")
    public Set<Product> getProductSet() {
        return productSet;
    }

    public void setProductSet(Set<Product> productSet) {
        this.productSet = productSet;
    }

    @Override
    public String toString() {
        return nameEn;
    }
}

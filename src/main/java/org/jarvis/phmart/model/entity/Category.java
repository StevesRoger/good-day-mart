package org.jarvis.phmart.model.entity;

import org.jarvis.orm.hibernate.domain.base.NamingEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created: kim chheng
 * Date: 13-Jan-2019 Sun
 * Time: 2:08 PM
 */
@Entity
@Table(name = "ph_category")
public class Category extends NamingEntity<Integer> {

    private static final long serialVersionUID = 1360065204733394994L;

    private String label;
    private String description;
    private Set<Product> productSet = new HashSet<>();

    public Category() {
    }

    public Category(String label, String description) {
        this.label = label;
        this.description = description;
        this.nameEn=label;
    }

    @Id
    @SequenceGenerator(name = "categorySeq", sequenceName = "ph_category_cat_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "categorySeq", strategy = GenerationType.SEQUENCE)
    @Column(name = "cat_id", nullable = false, unique = true)
    @Override
    public Integer getId() {
        return id;
    }

    @Column(name = "cat_label", length = 100)
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Column(name = "cat_description", length = 100)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
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

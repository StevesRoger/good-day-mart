package org.jarvis.phmart.model.entity.reference;

import org.jarvis.orm.hibernate.domain.base.Reference;

import javax.persistence.*;

/**
 * Created: kim chheng
 * Date: 13-Jan-2019 Sun
 * Time: 2:36 PM
 */
@Entity
@Table(name = "ph_form")
public class Form extends Reference<String> {

    private static final long serialVersionUID = -965013165791725218L;

    private String valueKh;

    public Form() {
    }

    public Form(String value, String description) {
        super(value, description);
    }

    @Id
    @SequenceGenerator(name = "formSeq", sequenceName = "ph_form_for_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "formSeq", strategy = GenerationType.SEQUENCE)
    @Column(name = "for_id", nullable = false, unique = true)
    @Override
    public Integer getId() {
        return id;
    }

    @Column(name = "for_value", nullable = false, length = 50)
    @Override
    public String getValue() {
        return value;
    }

    @Column(name = "for_value_kh")
    public String getValueKh() {
        return valueKh;
    }

    public void setValueKh(String valueKh) {
        this.valueKh = valueKh;
    }

    @Column(name = "for_description", length = 150)
    @Override
    public String getDescription() {
        return description;
    }
}

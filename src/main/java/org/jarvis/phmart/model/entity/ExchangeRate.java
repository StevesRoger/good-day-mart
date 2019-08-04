package org.jarvis.phmart.model.entity;

import org.hibernate.envers.Audited;
import org.jarvis.orm.hibernate.domain.base.Auditable;

import javax.persistence.*;

/**
 * Created: kim chheng
 * Date: 14-May-2019 Tue
 * Time: 11:51 AM
 */
@Audited
@Table(name = "ph_exchange_rate")
@Entity
public class ExchangeRate extends Auditable<Integer> {

    private static final long serialVersionUID = 2136406213733957164L;

    private Integer value;
    private String fromCurrency;
    private String toCurrency;
    private String desc;

    @Id
    @SequenceGenerator(name = "exchangeSeq", sequenceName = "ph_exchange_rate_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "exchangeSeq", strategy = GenerationType.SEQUENCE)
    @Column(name = "rate_id", nullable = false, unique = true)
    @Override
    public Integer getId() {
        return id;
    }

    @Column(name = "rate", length = 100)
    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Column(name = "from_currency", length = 10)
    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    @Column(name = "to_currency", length = 10)
    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    @Column(name = "label", length = 50)
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc;
    }
}

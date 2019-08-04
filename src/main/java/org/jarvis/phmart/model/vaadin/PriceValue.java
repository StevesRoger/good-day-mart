package org.jarvis.phmart.model.vaadin;

import org.jarvis.common.model.AnyObject;
import org.jarvis.orm.hibernate.domain.reference.Currency;


/**
 * Created: kim chheng
 * Date: 25-Apr-2019 Thu
 * Time: 3:40 PM
 */
public class PriceValue extends AnyObject {

    private static final long serialVersionUID = -5865801449216113221L;

    private Float price;
    private Currency currency;

    public PriceValue(Float price, Currency currency) {
        this.price = price;
        this.currency = currency;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}

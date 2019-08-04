package org.jarvis.phmart.model.vaadin;

import org.jarvis.common.model.AnyObject;
import org.jarvis.orm.hibernate.domain.reference.Currency;

/**
 * Created: kim chheng
 * Date: 11-May-2019 Sat
 * Time: 11:26 PM
 */
public class AmountValue extends AnyObject {

    private static final long serialVersionUID = -5865801449216113221L;

    private int qty;
    private float discount;
    private float amount;
    private PriceValue priceValue;

    public AmountValue(int qty, float discount, PriceValue priceValue) {
        this.qty = qty;
        this.discount = discount;
        this.priceValue = priceValue;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public Float getPrice() {
        return priceValue.getPrice();
    }

    public void setPrice(Float price) {
        this.priceValue.setPrice(price);
    }

    public Currency getCurrency() {
        return priceValue.getCurrency();
    }

    public void setCurrency(Currency currency) {
        this.priceValue.setCurrency(currency);
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}

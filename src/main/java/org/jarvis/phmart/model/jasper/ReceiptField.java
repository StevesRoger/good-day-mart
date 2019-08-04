package org.jarvis.phmart.model.jasper;

import org.jarvis.common.model.AnyObject;

/**
 * Created: kim chheng
 * Date: 18-Jun-2019 Tue
 * Time: 9:07 PM
 */
public class ReceiptField extends AnyObject {

    private String name;
    private String qty;
    private String price;
    private String disc;
    private String total;

    public ReceiptField() {
    }

    public ReceiptField(String name, String qty, String price, String disc, String total) {
        this.name = name;
        this.qty = qty;
        this.price = price;
        this.disc = disc;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "ReceiptField{" +
                "name='" + name + '\'' +
                ", qty='" + qty + '\'' +
                ", price='" + price + '\'' +
                ", disc='" + disc + '\'' +
                ", total='" + total + '\'' +
                '}';
    }
}

package org.jarvis.phmart.model.vaadin;

import org.jarvis.common.model.AnyObject;
import org.jarvis.common.util.DateUtil;
import org.jarvis.core.CoreConstant;
import org.jarvis.orm.hibernate.domain.reference.Currency;
import org.jarvis.phmart.model.entity.Image;
import org.jarvis.phmart.model.entity.Product;
import org.jarvis.sercurity.util.SecurityUtil;
import org.jarvis.vaadin.ui.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.Objects;


/**
 * Created: kim chheng
 * Date: 13-May-2019 Mon
 * Time: 2:19 PM
 */
public class ProductSale extends AnyObject {

    private static final long serialVersionUID = -8571648321147572420L;

    private static final Logger log = LoggerFactory.getLogger(ProductSale.class);

    private long id;
    private int qty;
    private int discount;
    private float price;
    private float amount;
    private String amountFormat;
    private transient Product product;
    private transient ImageView imageView;

    public ProductSale(Product product, int qty) {
        this(product, qty, true);
    }

    public ProductSale(Product product, int qty, boolean calculate) {
        this.product = product;
        this.id = product.getId();
        this.qty = qty;
        this.price = product.getSalePrice();
        this.discount = product.getDiscount();
        if (calculate)
            this.calculate();
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public long getId() {
        return id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public int getDiscount() {
        return discount;
    }

    public String getNameEn() {
        return product.getNameEn();
    }

    public String getNameKh() {
        return product.getNameKh();
    }

    public String getLabel() {
        return product.getLabel();
    }

    public Image getProfile() {
        return product.getProfile();
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public PriceValue getPriceValue() {
        return new PriceValue(price, Currency.USD);
    }

    public float getAmount() {
        return amount;
    }

    public String getAmountFormat() {
        return amountFormat;
    }

    public String getPriceFormat() {
        return String.format("$%.2f", getPrice());
    }

    public String getDiscountFormat() {
        return discount + "%";
    }

    public void calculate() {
        float discount = getPrice() / 100 * getDiscount();
        float price = getPrice() - discount;
        amount = (qty * price);
        amountFormat = String.format("$%.2f", amount);
        log.info("=========#######==========");
        log.info("user:{}", SecurityUtil.getUsername());
        log.info("date:{}", DateUtil.format(new Date()));
        log.info("product's name:{}", getNameEn());
        log.info("original price:{}$", getPrice());
        log.info("original discount:{}%", getDiscount());
        log.info("discount:{}%", discount);
        log.info("price:{}$", price);
        log.info("qty:{}", qty);
        log.info("total amount:{}$", amount);
        log.info("=========#######==========");
    }

    public boolean hasEnoughQty() {
        return qty < product.getQuantity();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object that) {
        return equals(that, "id");
    }

    @Override
    public String toString() {
        String json = CoreConstant.GSON.toJson(this);
        Map<String, Object> map = CoreConstant.GSON.fromJson(json, Map.class);
        map.put("name_en", getNameEn());
        map.put("name_kh", getNameKh());
        return CoreConstant.GSON.toJson(map);
    }
}

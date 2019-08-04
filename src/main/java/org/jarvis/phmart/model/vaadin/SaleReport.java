package org.jarvis.phmart.model.vaadin;

import org.jarvis.common.model.AnyObject;
import org.jarvis.phmart.model.entity.SaleHistory;

public class SaleReport extends AnyObject {

    private String productNameEn;
    private String productNameKh;
    private String productLabel;
    private String productForm;
    private String productCategory;
    private Integer qty;
    private Float total;

    public SaleReport(SaleHistory saleHistory) {
        this.productNameEn = saleHistory.getProductNameEn();
        this.productNameKh = saleHistory.getProductNameKh();
        this.productLabel = saleHistory.getProductLabel();
        this.productForm = saleHistory.getProductForm();
        this.productCategory = saleHistory.getProductCategory();
        this.qty = 0;
        this.total = 0f;
    }

    public String getProductNameEn() {
        return productNameEn;
    }

    public void setProductNameEn(String productNameEn) {
        this.productNameEn = productNameEn;
    }

    public String getProductNameKh() {
        return productNameKh;
    }

    public void setProductNameKh(String productNameKh) {
        this.productNameKh = productNameKh;
    }

    public String getProductLabel() {
        return productLabel;
    }

    public void setProductLabel(String productLabel) {
        this.productLabel = productLabel;
    }

    public String getProductForm() {
        return productForm;
    }

    public void setProductForm(String productForm) {
        this.productForm = productForm;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public void add(SaleHistory saleHistory) {
        if (saleHistory.getProductPrice() != null)
            total += saleHistory.getProductPrice();
        if (saleHistory.getQty() != null)
            qty += saleHistory.getQty();
    }
}

package org.jarvis.phmart.model.entity;

import org.jarvis.orm.hibernate.domain.base.AnyEntity;
import org.jarvis.phmart.model.vaadin.ProductSale;
import org.jarvis.sercurity.domain.entity.JarvisUser;
import org.jarvis.sercurity.util.SecurityUtil;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Created: kim chheng
 * Date: 20-Jan-2019 Sun
 * Time: 4:03 PM
 */
@Entity
@Table(name = "ph_sale_history")
public class SaleHistory extends AnyEntity<Long> {

    private static final long serialVersionUID = -5296030237901058109L;
    private Long productId;
    private Date createdDate;
    private String userName;
    private String userAccount;
    private String role;
    private String productNameEn;
    private String productNameKh;
    private String productLabel;
    private Float productPrice;
    private Integer qty;
    private Integer productDiscount;
    private String productBranch;
    private String productImage;
    private String productForm;
    private String productCategory;
    private String productBarCode;
    private Date expiredDate;
    private Date publishDate;

    public SaleHistory() {
    }

    public SaleHistory(ProductSale productSale) {
        Product product = productSale.getProduct();
        this.productId = productSale.getId();
        this.createdDate = new Date();
        this.userName = SecurityUtil.getUsername();
        this.userAccount = SecurityUtil.getUsername();
        this.role = roles();
        this.productNameEn = productSale.getNameEn();
        this.productNameKh = productSale.getNameKh();
        this.productLabel = productSale.getLabel();
        this.productPrice = productSale.getPrice();
        this.qty = productSale.getQty();
        this.productDiscount = productSale.getDiscount();
        this.productForm = product.getForm() == null ? null : product.getForm().getValue();
        this.productCategory = product.getCategory() == null ? null : product.getCategory().toString();
        this.productBarCode = product.getBarcode();
        this.expiredDate = product.getExpireDate();
        this.publishDate = product.getPublishDate();
    }

    @Id
    @SequenceGenerator(name = "saleHistorySeq", sequenceName = "ph_sale_history_sal_history_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "saleHistorySeq", strategy = GenerationType.SEQUENCE)
    @Column(name = "sal_history_id", nullable = false, unique = true)
    @Override
    public Long getId() {
        return id;
    }

    @Column(name = "pro_id")
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Column(name = "user_name", length = 100)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "user_account", length = 100)
    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    @Column(name = "role", length = 50)
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Column(name = "pro_name_en", length = 100)
    public String getProductNameEn() {
        return productNameEn;
    }

    public void setProductNameEn(String productNameEn) {
        this.productNameEn = productNameEn;
    }

    @Column(name = "pro_name_kh", length = 100)
    public String getProductNameKh() {
        return productNameKh;
    }

    public void setProductNameKh(String productNameKh) {
        this.productNameKh = productNameKh;
    }

    @Column(name = "pro_label", length = 100)
    public String getProductLabel() {
        return productLabel;
    }

    public void setProductLabel(String productLabel) {
        this.productLabel = productLabel;
    }

    @Column(name = "pro_price")
    public Float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Float productPrice) {
        this.productPrice = productPrice;
    }

    @Column(name = "pro_qty")
    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    @Column(name = "pro_discount")
    public Integer getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(Integer productDiscount) {
        this.productDiscount = productDiscount;
    }

    @Column(name = "pro_branch", length = 100)
    public String getProductBranch() {
        return productBranch;
    }

    public void setProductBranch(String productBranch) {
        this.productBranch = productBranch;
    }

    @Column(name = "pro_image")
    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    @Column(name = "pro_form", length = 50)
    public String getProductForm() {
        return productForm;
    }

    public void setProductForm(String productForm) {
        this.productForm = productForm;
    }

    @Column(name = "pro_category", length = 100)
    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    @Column(name = "pro_barcode")
    public String getProductBarCode() {
        return productBarCode;
    }

    public void setProductBarCode(String productBarCode) {
        this.productBarCode = productBarCode;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "pro_expired_date")
    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "pro_publish_date")
    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    @Transient
    public String roles() {
        JarvisUser user = (JarvisUser) SecurityUtil.getUser();
        if (user != null && user.getProfile() != null)
            return user.getProfile().getAuthority();
        return "unknown_role";
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(productId);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj, "productId");
    }
}

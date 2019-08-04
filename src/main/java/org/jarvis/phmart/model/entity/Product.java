package org.jarvis.phmart.model.entity;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.jarvis.orm.hibernate.domain.base.NamingEntity;
import org.jarvis.orm.hibernate.domain.reference.Currency;
import org.jarvis.phmart.model.entity.converter.FormConverter;
import org.jarvis.phmart.model.entity.reference.Form;
import org.jarvis.phmart.model.vaadin.PriceValue;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created: kim chheng
 * Date: 06-Jan-2019 Sun
 * Time: 5:00 PM
 */
@Entity
@Table(name = "ph_product")
@Audited
public class Product extends NamingEntity<Long> {

    private static final long serialVersionUID = -4992277004490023273L;

    private Integer quantity;
    private Float salePrice;
    private Float originalPrice;
    private Integer discount;
    private String label;
    private String description;
    private String brand;
    private String barcode;
    private Form form;
    //private Currency currency;
    private Category category;
    private Date expireDate;
    private Date publishDate;
    private Supplier supplier;
    private Set<Image> imageSet = new LinkedHashSet<>();

    @Id
    @SequenceGenerator(name = "productSeq", sequenceName = "ph_product_pro_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "productSeq", strategy = GenerationType.SEQUENCE)
    @Column(name = "pro_id", nullable = false, unique = true)
    @Override
    public Long getId() {
        return id;
    }

    @Column(name = "pro_quantity", nullable = false)
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Column(name = "pro_price", nullable = false)
    public Float getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Float salePrice) {
        this.salePrice = salePrice;
    }

    @Column(name = "pro_discount")
    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    @Column(name = "pro_label", length = 100)
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Column(name = "pro_description", length = 150)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "pro_barcode")
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @Convert(converter = FormConverter.class)
    @Column(name = "for_value", length = 50)
    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    /*@Convert(converter = CurrencyConverter.class)
    @Column(name = "cur_value", length = 50, nullable = false)
    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }*/

    @Column(name = "pro_original_price")
    public Float getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Float originalPrice) {
        this.originalPrice = originalPrice;
    }

    @Column(name = "pro_brand")
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_id")
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @NotAudited
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Set<Image> getImageSet() {
        return imageSet;
    }

    public void setImageSet(Set<Image> imageSet) {
        this.imageSet = imageSet;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "pro_expired_date")
    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "pro_publish_date")
    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sup_id")
    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    @Transient
    public PriceValue getPriceValue() {
        return new PriceValue(salePrice, Currency.USD);
    }

    @Transient
    public Image getProfile() {
        for (Image image : imageSet)
            if (image.isProfile())
                return image;
        return imageSet.isEmpty() ? null : imageSet.stream().findFirst().get();
    }

    public void updateQTYStock(int qty) {
        this.setQuantity(getQuantity() - qty);
    }

    public void addImage(Image image) {
        image.setProduct(this);
        imageSet.add(image);
    }

    public void addImage(String name, String path, String mimeType) {
        addImage(name, path, mimeType, new byte[0]);
    }

    public void addImage(String name, String path, String mimeType, byte[] bytes) {
        addImage(new Image(name, path, mimeType, bytes));
    }

    public void addImage(String name, String mimeType, byte[] bytes, boolean isProfile) {
        addImage(new Image(name, null, mimeType, bytes, isProfile));
    }
}

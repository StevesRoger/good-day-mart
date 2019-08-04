package org.jarvis.phmart.vaadin.dialog;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import org.jarvis.common.util.DateUtil;
import org.jarvis.core.util.ContextUtil;
import org.jarvis.phmart.helper.ImageHelper;
import org.jarvis.phmart.model.entity.Product;
import org.jarvis.vaadin.factory.ComponentReadOnlyFactory;
import org.jarvis.vaadin.ui.TextBox;
import org.jarvis.vaadin.ui.layout.GridLayout;
import org.jarvis.vaadin.ui.layout.HorizontalScrollLayout;

/**
 * Created: kim chheng
 * Date: 20-May-2019 Mon
 * Time: 10:02 AM
 */
public class ProductDetailDialog extends Dialog implements LocaleChangeObserver {

    private TextBox txtNameEn;
    private TextBox txtNameKh;
    private TextBox txtQty;
    private TextBox txtPrice;
    private TextBox txtDiscount;
    private TextBox txtBarcode;
    private TextBox txtLabel;
    private TextBox txtForm;
    private TextBox txtCategory;
    private TextBox txtSupplier;
    private TextBox txtPublishDate;
    private TextBox txtExpiredDate;
    private TextBox txtCreatedAt;
    private TextBox txtUpdatedAt;
    private TextBox txtCreatedBy;
    private TextBox txtUpdatedBy;
    private TextBox txtOriginalPrice;
    private TextBox txtBrand;
    private TextBox txtDesc;
    // private Griddy<Import> grid;
    private HorizontalScrollLayout imageScrollLayout;

    private GridLayout gridLayout;
    private final Product product;

    public ProductDetailDialog(Product product) {
        this.product = product;
        init();
        readBean();
        open();
    }

    private void init() {
        gridLayout = new GridLayout();
        imageScrollLayout = new HorizontalScrollLayout();
        imageScrollLayout.setClassName("img-view");
        imageScrollLayout.setHeight("180px");
        imageScrollLayout.setWidth("610px");

        txtNameEn = ComponentReadOnlyFactory.createTextBox(getTranslation("product.name.en"));
        txtNameKh = ComponentReadOnlyFactory.createTextBox(getTranslation("product.name.kh"));
        txtQty = ComponentReadOnlyFactory.createTextBox(getTranslation("product.qty"));

        txtPrice = ComponentReadOnlyFactory.createTextBox(getTranslation("product.sale.price"));
        txtPrice.setSuffixComponent(new Span(ContextUtil.getProperty("usd_currency")));
        txtPrice.getElement().setAttribute("theme", "align-right");
        txtDiscount = ComponentReadOnlyFactory.createTextBox(getTranslation("product.discount"));
        txtDiscount.setSuffixComponent(new Span("%"));
        txtDiscount.getElement().setAttribute("theme", "align-right");
        txtBarcode = ComponentReadOnlyFactory.createTextBox(getTranslation("product.barcode"));

        txtLabel = ComponentReadOnlyFactory.createTextBox(getTranslation("product.label"));
        txtForm = ComponentReadOnlyFactory.createTextBox(getTranslation("product.form"));
        txtCategory = ComponentReadOnlyFactory.createTextBox(getTranslation("product.category"));

        txtCreatedAt = ComponentReadOnlyFactory.createTextBox(getTranslation("product.created.at"));
        txtUpdatedAt = ComponentReadOnlyFactory.createTextBox(getTranslation("product.update.at"));
        txtCreatedBy = ComponentReadOnlyFactory.createTextBox(getTranslation("product.created.by"));
        txtUpdatedBy = ComponentReadOnlyFactory.createTextBox(getTranslation("product.update.by"));

        txtSupplier = ComponentReadOnlyFactory.createTextBox(getTranslation("product.supplier"));
        txtPublishDate = ComponentReadOnlyFactory.createTextBox(getTranslation("product.publish.date"));
        txtExpiredDate = ComponentReadOnlyFactory.createTextBox(getTranslation("product.expire.date"));

        txtOriginalPrice = ComponentReadOnlyFactory.createTextBox(getTranslation("product.original.price"));
        txtOriginalPrice.setSuffixComponent(new Span(ContextUtil.getProperty("usd_currency")));
        txtOriginalPrice.getElement().setAttribute("theme", "align-right");
        txtDesc = ComponentReadOnlyFactory.createTextBox(getTranslation("product.description"));
        txtBrand =  ComponentReadOnlyFactory.createTextBox(getTranslation("product.brand"), "LUX");

        gridLayout.add(0, txtNameEn, txtNameKh, txtQty);
        gridLayout.add(1, txtPrice, txtDiscount, txtBarcode);
        gridLayout.add(2, txtLabel, txtForm, txtCategory);
        gridLayout.add(3, txtOriginalPrice, txtBrand, txtDesc);
        gridLayout.add(4, txtSupplier, txtPublishDate, txtExpiredDate);
        gridLayout.add(5, txtCreatedAt, txtUpdatedAt, txtUpdatedBy);

        /*grid = new Griddy<>();
        grid.setHeight("200px");
        grid.addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        Grid.Column<Import> columnImportDate = grid.addColumn(new DateRenderer<>(Import::getCreatedDate, "dd/MM/yyyy H:mm:ss"));
        columnImportDate.setHeader("Created Date").setKey("created_date").setResizable(true);
        columnImportDate.setComparator(Comparator.comparing(Import::getCreatedDate));
        Grid.Column<Import> columnSupplier = grid.addColumn(Import::getSupplier).setHeader("Supplier").setKey("supplier");
        Grid.Column<Import> columnPrice = grid.addColumn(new PriceRenderer<>(Import::getPriceValue)).setHeader("Price").setKey("price");
        columnPrice.setComparator(Comparator.comparing(Import::getSalePrice));
        Grid.Column<Import> columnQty = grid.addColumn(Import::getQuantity).setHeader("Quantity").setKey("quantity");
        columnQty.setComparator(Comparator.comparing(Import::getQuantity));
        Grid.Column<Import> columnDiscount = grid.addColumn(Import::getDiscount).setHeader("Discount").setKey("discount");
        columnDiscount.setComparator(Comparator.comparing(Import::getDiscount));
        VaadinHelper.importComponentRenderer(grid);*/

        add(new H3(getTranslation("title.detail")));
        add(gridLayout);
        add(new H5(getTranslation("image")));
        add(imageScrollLayout);
        //add(new H5(getTranslation("import")));
        //add(grid);
    }

    private void readBean() {
        txtNameEn.setValue(product.getNameEn());
        txtNameKh.setValue(product.getNameKh() == null ? "" : product.getNameKh());
        txtQty.setValue(String.format("%d", product.getQuantity()));
        txtPrice.setValue(String.format("%.2f", product.getSalePrice()));
        txtDiscount.setValue(String.format("%d", product.getDiscount()));
        txtBarcode.setValue(product.getBarcode() == null ? "" : product.getBarcode());
        txtLabel.setValue(product.getLabel() == null ? "" : product.getLabel());
        txtForm.setValue(product.getForm() == null ? "" : product.getForm().getValue());
        txtCategory.setValue(product.getCategory() == null ? "" : product.getCategory().getNameEn());
        txtCreatedAt.setValue(DateUtil.format(product.getCreatedDate(), "dd/MM/yyyy H:mm:ss"));
        txtUpdatedAt.setValue(DateUtil.format(product.getLastModifiedDate(), "dd/MM/yyyy H:mm:ss"));
        txtUpdatedBy.setValue(product.getLastModifiedBy());
        txtSupplier.setValue(product.getSupplier() == null ? "" : product.getSupplier().getCompanyName());
        txtPublishDate.setValue(DateUtil.format(product.getPublishDate(), "dd/MM/yyyy", ""));
        txtExpiredDate.setValue(DateUtil.format(product.getExpireDate(), "dd/MM/yyyy", ""));
        txtOriginalPrice.setValue(product.getOriginalPrice() == null ? "0.00" : String.format("%.2f", product.getOriginalPrice()));
        txtDesc.setValue(product.getDescription() == null ? "" : product.getDescription());
        txtBrand.setValue(product.getBrand() == null ? "" : product.getBrand());
        //txtCreatedBy.setValue(product.getCreatedBy());
        product.getImageSet().forEach(image -> imageScrollLayout.add(ImageHelper.createViewImage(image.getStreamResource())));
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }
}

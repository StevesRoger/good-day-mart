package org.jarvis.phmart.vaadin.dialog;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyDownEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import org.jarvis.core.util.ContextUtil;
import org.jarvis.phmart.model.vaadin.ProductSale;
import org.jarvis.vaadin.factory.ComponentReadOnlyFactory;
import org.jarvis.vaadin.ui.*;
import org.jarvis.vaadin.ui.layout.GridLayout;

/**
 * Created: kim chheng
 * Date: 07-May-2019 Tue
 * Time: 9:13 PM
 */
public class SaleEditDialog extends Dialog {

    private TextBox txtNameEn;
    private TextBox txtNameKh;
    private TextBox txtLabel;
    private NumberField<Integer> txtDiscount;
    private NumberField<Integer> txtQTY;
    private NumberField<Float> txtPrice;

    private Click btnOk;
    private Click btnCancel;

    private GridLayout gridLayout;
    private ProductSale productSale;
    private Griddy<ProductSale> grid;

    private TextBox txtBarcode;

    public SaleEditDialog(ProductSale productSale, Griddy<ProductSale> grid, TextBox txtBarcode) {
        this.productSale = productSale;
        this.grid = grid;
        this.txtBarcode = txtBarcode;
        init();
        readBean();
        open();
    }

    private void init() {
        txtNameEn = ComponentReadOnlyFactory.createTextBox(getTranslation("product.name.en"));
        txtNameKh = ComponentReadOnlyFactory.createTextBox(getTranslation("product.name.kh"));
        txtLabel = ComponentReadOnlyFactory.createTextBox(getTranslation("product.label"));

        txtQTY = new NumberField<>(getTranslation("product.qty"), Integer.class);
        txtQTY.setRequired(true);
        txtQTY.setFormat("%d");
        txtQTY.addKeyDownListener(Key.ENTER, this::onKeyEnter);
        txtDiscount = new NumberField<>(getTranslation("product.discount"), Integer.class);
        txtDiscount.setRequired(true);
        txtDiscount.setFormat("%d");
        txtDiscount.setSuffixComponent(new Span("%"));
        txtDiscount.addKeyDownListener(Key.ENTER, this::onKeyEnter);
        txtDiscount.getElement().setAttribute("theme", "align-right");
        txtPrice = new NumberField<>(getTranslation("product.sale.price"), Float.class);
        txtPrice.setRequired(true);
        txtPrice.setFormat("%.2f");
        txtPrice.setSuffixComponent(new Span(ContextUtil.getProperty("usd_currency")));
        txtPrice.getElement().setAttribute("theme", "align-right");
        txtPrice.addKeyDownListener(Key.ENTER, this::onKeyEnter);

        btnOk = new Click(getTranslation("button.ok"), this::onOk);
        btnOk.getElement().getThemeList().add("primary");
        btnCancel = new Click(getTranslation("button.cancel"), this::onClose);
        btnCancel.getElement().getThemeList().add("error");

        ImageView image = new ImageView("frontend/image/bg-beach.jpg", "bg-beach.jpg");
        image.setWidth("260px");
        image.setHeight("170px");

        gridLayout = new GridLayout();

        add(new H3((getTranslation("button.product"))));
        add(gridLayout);

        gridLayout.add(0, txtNameEn, txtNameKh, txtLabel);
        gridLayout.add(1, txtQTY, txtDiscount, txtPrice);
        gridLayout.add(2, btnOk, btnCancel);
        gridLayout.getRow(0, 2).getStyle().set("margin-top", "5px");
    }

    private void readBean() {
        txtNameEn.setValue(productSale.getNameEn());
        txtNameKh.setValue(productSale.getNameKh());
        txtLabel.setValue(productSale.getLabel());
        txtQTY.setNumber(productSale.getQty());
        txtDiscount.setNumber(productSale.getDiscount());
        txtPrice.setNumber(productSale.getPrice());
    }

    private void onOk(ClickEvent<Button> event) {
        productSale.setQty(txtQTY.getNumber());
        productSale.setDiscount(txtDiscount.getNumber());
        productSale.setPrice(txtPrice.getNumber());
        productSale.calculate();
        grid.refreshItem(productSale);
        close();
    }

    private void onKeyEnter(KeyDownEvent event) {
        btnOk.click();
    }

    private void onClose(ClickEvent<Button> event) {
        this.close();
    }

    @Override
    public void close() {
        super.close();
        txtBarcode.setValue("");
        txtBarcode.focus();
    }
}

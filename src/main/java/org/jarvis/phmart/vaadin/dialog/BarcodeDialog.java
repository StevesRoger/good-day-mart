package org.jarvis.phmart.vaadin.dialog;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.jarvis.phmart.model.entity.Product;
import org.jarvis.phmart.model.vaadin.ProductSale;
import org.jarvis.phmart.vaadin.SaleDataProvider;
import org.jarvis.phmart.view.sale.SalePresenter;
import org.jarvis.vaadin.ui.NumberField;
import org.jarvis.vaadin.ui.TextView;
import org.jarvis.vaadin.ui.layout.HorizontalScrollLayout;

/**
 * Created: kim chheng
 * Date: 17-Jun-2019 Mon
 * Time: 9:36 AM
 */
public class BarcodeDialog extends Dialog {

    public BarcodeDialog(Product product, SaleDataProvider dataProvider, HorizontalScrollLayout imageLayout, SalePresenter presenter) {
        ProductSale productSale = new ProductSale(product, 1, false);
        HorizontalLayout layout = new HorizontalLayout();
        layout.add(new TextView("Name:" + product.getNameEn()));
        layout.add(new TextView("Price:" + productSale.getPriceFormat()));
        layout.add(new TextView("Discount:" + productSale.getDiscountFormat()));
        layout.add(new TextView("Quantity:" + productSale.getProduct().getQuantity()));
        NumberField<Integer> txtQty = new NumberField<>(getTranslation("product.qty"), Integer.class);
        txtQty.setWidth("100%");
        txtQty.setNumber(1);
        txtQty.focus();
        txtQty.addKeyDownListener(Key.ENTER, event -> {
            presenter.createOrder(product, txtQty.getNumber(), dataProvider, imageLayout);
            close();
        });
        addDialogCloseActionListener(event -> {
            close();
            presenter.getView().clear(false);
        });
        add(new Div(layout, txtQty));
        open();
    }
}

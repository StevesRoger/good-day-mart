package org.jarvis.phmart.view.sale;

import com.vaadin.flow.component.KeyDownEvent;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.jarvis.phmart.helper.ImageHelper;
import org.jarvis.phmart.helper.MessageDialogHelper;
import org.jarvis.phmart.model.entity.Image;
import org.jarvis.phmart.model.entity.Product;
import org.jarvis.phmart.model.vaadin.ProductSale;
import org.jarvis.phmart.vaadin.SaleDataProvider;
import org.jarvis.phmart.vaadin.dialog.SaleDialog;
import org.jarvis.phmart.view.base.AnyPresenter;
import org.jarvis.vaadin.ui.ImageView;
import org.jarvis.vaadin.ui.TextBox;
import org.jarvis.vaadin.ui.layout.HorizontalScrollLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

/**
 * Created: kim chheng
 * Date: 05-May-2019 Sun
 * Time: 9:30 AM
 */
@SpringComponent
@SessionScope
public class SalePresenter extends AnyPresenter<SaleView, SaleInteractor> implements SaleView.SalveViewListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SalePresenter.class);

    public SalePresenter() {
        LOGGER.info(this.toString());
    }

    @Override
    public void onBarcodeInput(TextBox txtBarcode, SaleDataProvider dataProvider, HorizontalScrollLayout imageLayout) {
        String barcode = txtBarcode.getValue();
        txtBarcode.setValue("");
        Product product = repository.getEntityByProperty(Product.class, "barcode", barcode);
        if (product != null)
            createOrder(product, 1, dataProvider, imageLayout);
            //new BarcodeDialog(product, dataProvider, imageLayout, this);
        else
            MessageDialogHelper.showMessageI18NDialog("product.not.found");
    }

    @Override
    public void createOrder(Product product, int qty, SaleDataProvider dataProvider, HorizontalScrollLayout imageLayout) {
        if (product != null) {
            if (product.getQuantity() == 0 || product.getQuantity() < qty || !dataProvider.hasEnoughQty(product)) {
                MessageDialogHelper.showMessageI18NDialog("message.not.enough.qty");
                return;
            }
            ProductSale productSale = new ProductSale(product, qty);
            Image profile = productSale.getProfile();
            ImageView imageView = null;
            if (profile != null && profile.getBytes().length > 0)
                imageView = ImageHelper.createViewImage(profile.getStreamResource());
            if (dataProvider.addItem(productSale) && imageView != null) {
                productSale.setImageView(imageView);
                imageLayout.add(imageView);
            }
            view.clear(false);
        } else {
            MessageDialogHelper.showMessageI18NDialog("message.no.product");
            view.clear(false);
        }
    }

    @Override
    public void executeOrder(SaleDataProvider dataProvider) {
        if (!dataProvider.isEmpty())
            new SaleDialog(dataProvider, this);
        else
            MessageDialogHelper.showMessageI18NDialog("no.order.product");
    }
}

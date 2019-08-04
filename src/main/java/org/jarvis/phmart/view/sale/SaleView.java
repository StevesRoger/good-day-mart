package org.jarvis.phmart.view.sale;

import com.vaadin.flow.component.KeyDownEvent;
import org.jarvis.phmart.model.entity.Product;
import org.jarvis.phmart.vaadin.SaleDataProvider;
import org.jarvis.phmart.view.base.AnyView;
import org.jarvis.vaadin.ui.TextBox;
import org.jarvis.vaadin.ui.layout.HorizontalScrollLayout;

/**
 * Created: kim chheng
 * Date: 24-Feb-2019 Sun
 * Time: 10:54 AM
 */
public interface SaleView extends AnyView {

    void clear(boolean clearAll);

    interface SalveViewListener {

        void onBarcodeInput(TextBox txtBarcode, SaleDataProvider dataProvider, HorizontalScrollLayout imageLayout);

        void createOrder(Product product, int qty, SaleDataProvider dataProvider, HorizontalScrollLayout imageLayout);

        void executeOrder(SaleDataProvider dataProvider);
    }
}

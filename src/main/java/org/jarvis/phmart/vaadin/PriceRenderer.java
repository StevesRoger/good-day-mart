package org.jarvis.phmart.vaadin;

import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.function.ValueProvider;
import org.jarvis.core.util.ContextUtil;
import org.jarvis.phmart.model.vaadin.PriceValue;

/**
 * Created: kim chheng
 * Date: 30-Mar-2019 Sat
 * Time: 10:37 PM
 */
public class PriceRenderer<SOURCE> extends BasicRenderer<SOURCE, PriceValue> {

    public PriceRenderer(ValueProvider<SOURCE, PriceValue> valueProvider) {
        super(valueProvider);
    }

    @Override
    protected String getFormattedValue(PriceValue priceValue) {
        String currency = priceValue.getCurrency().getValue();
        Float price = priceValue.getPrice();
        if ("USD".equals(currency))
            return String.format("%.2f$", price);
        else if ("KHR".equals(currency))
            return String.format("%.0f" + ContextUtil.getProperty("kh_currency"), price);
        throw new IllegalStateException(
                String.format("Unable to format the given value: [currency: %s, price: %s]", currency, price));
    }

}

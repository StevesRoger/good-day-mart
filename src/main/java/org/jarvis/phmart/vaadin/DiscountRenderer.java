package org.jarvis.phmart.vaadin;

import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.function.ValueProvider;

/**
 * Created: kim chheng
 * Date: 04-May-2019 Sat
 * Time: 2:32 PM
 */
public class DiscountRenderer<SOURCE> extends BasicRenderer<SOURCE, Integer> {

    public DiscountRenderer(ValueProvider<SOURCE, Integer> valueProvider) {
        super(valueProvider);
    }

    @Override
    protected String getFormattedValue(Integer discount) {
        if (discount == null || 0 == discount)
            return "0%";
        return discount + "%";
    }
}

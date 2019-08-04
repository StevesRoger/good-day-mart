package org.jarvis.phmart.vaadin;

import com.vaadin.flow.component.html.Span;
import org.jarvis.core.util.ContextUtil;
import org.jarvis.vaadin.ui.TextBox;

/**
 * Created: kim chheng
 * Date: 18-Jun-2019 Tue
 * Time: 10:28 PM
 */
public class ExchangeRateLabel extends Span {

    private TextBox txtExchangeRate;

    public ExchangeRateLabel(String text) {
        super(text);
        txtExchangeRate = new TextBox();
        txtExchangeRate.setReadOnly(true);
        txtExchangeRate.getStyle().set("margin-left", "5px").set("width", "103px");
        add(txtExchangeRate);
        getElement().getStyle().set("margin-left", "auto");
    }

    public void setRate(int rate) {
        txtExchangeRate.setValue("1$=" + rate + ContextUtil.getProperty("kh_currency"));
    }
}

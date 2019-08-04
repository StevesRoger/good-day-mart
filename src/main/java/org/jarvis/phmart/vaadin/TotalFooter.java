package org.jarvis.phmart.vaadin;

import com.vaadin.flow.component.html.Span;
import org.jarvis.core.util.ContextUtil;
import org.jarvis.phmart.helper.MoneyHelper;
import org.jarvis.vaadin.ui.NumberField;

/**
 * Created: kim chheng
 * Date: 14-May-2019 Tue
 * Time: 1:43 PM
 */
public class TotalFooter extends Span {

    private NumberField<Float> txtUSD;
    private NumberField<Long> txtKHR;

    public TotalFooter(String text) {
        super(text);
        init();
    }

    private void init() {
        txtUSD = new NumberField<>(Float.class);
        txtUSD.setFormat("%.2f");
        txtUSD.setNumber(0f);
        txtUSD.getStyle().set("padding-left", "5px").set("width", "100px");
        txtUSD.addClassName("color-red");
        txtUSD.setReadOnly(true);
        txtUSD.setSuffixComponent(new Span(ContextUtil.getProperty("usd_currency")));
        txtUSD.getElement().setAttribute("theme", "align-right");

        txtKHR = new NumberField<>(Long.class);
        txtKHR.setFormat("%d");
        txtKHR.setNumber(0l);
        txtKHR.getStyle().set("padding-left", "5px").set("width", "100px");
        txtKHR.addClassName("color-red");
        txtKHR.setReadOnly(true);
        txtKHR.setSuffixComponent(new Span(ContextUtil.getProperty("kh_currency")));
        txtKHR.getElement().setAttribute("theme", "align-right");

        add(txtUSD, txtKHR);
    }

    public void setValue(float value) {
        txtUSD.setNumber(value);
        long exchange = MoneyHelper.exchangeToKH(txtUSD.getNumber());
        txtKHR.setNumber(exchange);
    }

    public NumberField<Float> getTxtUSD() {
        return txtUSD;
    }

    public NumberField<Long> getTxtKHR() {
        return txtKHR;
    }
}

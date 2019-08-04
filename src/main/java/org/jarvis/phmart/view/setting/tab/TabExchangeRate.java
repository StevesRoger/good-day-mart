package org.jarvis.phmart.view.setting.tab;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.jarvis.core.util.ContextUtil;
import org.jarvis.orm.hibernate.util.EntityUtil;
import org.jarvis.phmart.helper.MoneyHelper;
import org.jarvis.phmart.model.entity.ExchangeRate;
import org.jarvis.phmart.view.setting.SettingPresenter;
import org.jarvis.vaadin.ui.Click;
import org.jarvis.vaadin.ui.NumberField;
import org.jarvis.vaadin.ui.layout.GridLayout;

/**
 * Created: kim chheng
 * Date: 22-Jun-2019 Sat
 * Time: 10:57 AM
 */
public class TabExchangeRate extends Div {

    private GridLayout layout;
    private NumberField<Integer> txtWhichCurrency;
    private NumberField<Integer> txtKHRRate;
    private Click btnSave;
    private SettingPresenter presenter;
    private VerticalLayout container;

    public TabExchangeRate(SettingPresenter presenter) {
        this.presenter = presenter;
        init();
        getRate();
    }

    private void init() {
        container = new VerticalLayout();
        layout = new GridLayout();

        txtWhichCurrency = new NumberField<>(Integer.class);
        txtWhichCurrency.setFormat("%d");
        txtWhichCurrency.setReadOnly(true);
        txtWhichCurrency.setNumber(1);
        txtWhichCurrency.getStyle().set("padding-left", "5px").set("width", "100px");
        txtWhichCurrency.setSuffixComponent(new Span(ContextUtil.getProperty("usd_currency")));
        txtWhichCurrency.getElement().setAttribute("theme", "align-right");

        txtKHRRate = new NumberField<>(Integer.class);
        txtKHRRate.focus();
        txtKHRRate.setAutoselect(true);
        txtKHRRate.setFormat("%d");
        txtKHRRate.setNumber(MoneyHelper.getRate());
        txtKHRRate.getStyle().set("padding-left", "5px").set("width", "100px");
        txtKHRRate.setSuffixComponent(new Span(ContextUtil.getProperty("kh_currency")));
        txtKHRRate.getElement().setAttribute("theme", "align-right");
        txtKHRRate.addKeyDownListener(Key.ENTER, event -> btnSave.click());

        Span span = new Span("Exchange Rate");
        span.add(txtWhichCurrency, txtKHRRate);

        btnSave = new Click(getTranslation("button.save"), this::saveExchangeRate);
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        layout.add(0, span, btnSave);


        /*grid = new Griddy<>();
        Grid.Column<ExchangeRateLabel> columnFrom = grid.addColumn(ExchangeRateLabel::getFromCurrency).setHeader(getTranslation("from.currency"));
        Grid.Column<ExchangeRateLabel> columnTo = grid.addColumn(ExchangeRateLabel::getToCurrency).setHeader(getTranslation("to.currency"));
        Grid.Column<ExchangeRateLabel> columnRate = grid.addColumn(ExchangeRateLabel::getValue).setHeader(getTranslation("rate"));
        Grid.Column<ExchangeRateLabel> columnDesc = grid.addColumn(ExchangeRateLabel::getDesc).setHeader(getTranslation("desc"));
        Grid.Column<ExchangeRateLabel> columnPrice = grid.addColumn(new PriceRenderer<>(ExchangeRateLabel::getPriceValue)).setHeader(getTranslation("product.price")).setWidth("5px");
        Grid.Column<ExchangeRateLabel> columnDiscount = grid.addColumn(ExchangeRateLabel::getDiscountFormat).setHeader(getTranslation("product.discount")).setWidth("5px");
        Grid.Column<ExchangeRateLabel> columnAmount = grid.addColumn(ExchangeRateLabel::getAmountFormat).setHeader(getTranslation("sale.amount")).setWidth("5px");
        Grid.Column<ExchangeRateLabel> columnAction = grid.addColumn(TemplateRenderer.<ExchangeRateLabel>of(
                "<button on-click='handleUpdate'>Edit</button>&nbsp;<button on-click='handleRemove'>Remove</button>")
                .withEventHandler("handleUpdate", product -> new SaleEditDialog(product, grid, txtBarcode))
                .withEventHandler("handleRemove", product -> onRemove(product)))
                .setHeader("Actions").setWidth("150px");
        columnAction.setFooter(totalFooter);*/
        container.add(layout);
        add(container);
    }

    private void getRate() {
        ExchangeRate exchangeRate = MoneyHelper.getExchangeRate();
        if (exchangeRate != null)
            txtKHRRate.setNumber(exchangeRate.getValue());
    }

    private void saveExchangeRate(ClickEvent<Button> event) {
        ExchangeRate exchangeRate = MoneyHelper.getExchangeRate();
        if (exchangeRate != null) {
            exchangeRate.setValue(txtKHRRate.getNumber());
            exchangeRate.setDesc("1$=" + txtKHRRate.getNumber() + ContextUtil.getProperty("kh_currency"));
            EntityUtil.modified(exchangeRate);
            presenter.getView().updateExchangeRateLabel(txtKHRRate.getNumber());
            presenter.getRepository().saveOrUpdate(exchangeRate);
        }
    }
}

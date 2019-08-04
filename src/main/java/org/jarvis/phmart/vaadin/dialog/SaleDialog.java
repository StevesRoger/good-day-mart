package org.jarvis.phmart.vaadin.dialog;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.FocusNotifier;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.jarvis.core.util.ContextUtil;
import org.jarvis.phmart.exception.SaleException;
import org.jarvis.phmart.helper.MessageDialogHelper;
import org.jarvis.phmart.helper.MoneyHelper;
import org.jarvis.phmart.model.entity.Product;
import org.jarvis.phmart.model.entity.SaleHistory;
import org.jarvis.phmart.model.jasper.ReceiptField;
import org.jarvis.phmart.model.vaadin.ProductSale;
import org.jarvis.phmart.vaadin.SaleDataProvider;
import org.jarvis.phmart.view.sale.SalePresenter;
import org.jarvis.sercurity.util.SecurityUtil;
import org.jarvis.vaadin.ui.CheckBox;
import org.jarvis.vaadin.ui.Click;
import org.jarvis.vaadin.ui.NumberField;
import org.jarvis.vaadin.ui.layout.GridLayout;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Created: kim chheng
 * Date: 17-Jun-2019 Mon
 * Time: 1:31 PM
 */
public class SaleDialog extends Dialog {

    private SaleDataProvider dataProvider;
    private NumberField<Float> txtUSD;
    private NumberField<Long> txtKHR;
    private NumberField<Float> totalUSD;
    private NumberField<Long> totalKHR;
    private NumberField<Float> changeUSD;
    private NumberField<Long> changeKHR;
    private CheckBox isPrint;
    private Click btnOk;
    private Click btnPreview;
    private Click btnCancel;
    private GridLayout layout;
    private SalePresenter presenter;
    private CompletableFuture<JasperReport> compileReportTask;

    private static final Logger LOGGER = LoggerFactory.getLogger(SaleDialog.class);

    public SaleDialog(SaleDataProvider dataProvider, SalePresenter presenter) {
        this.dataProvider = dataProvider;
        this.presenter = presenter;
        this.compileReport();
        init();
        open();
    }

    private void compileReport() {
        compileReportTask = CompletableFuture.supplyAsync(() -> {
            try {
                LOGGER.info("start compile receipt");
                LOGGER.info("jasper file:receipt/good-day-mart.jrxml");
                Resource resource = new ClassPathResource("receipt/good-day-mart.jrxml");
                JasperReport jasperReport = JasperCompileManager.compileReport(resource.getInputStream());
                LOGGER.info("compile receipt success");
                return jasperReport;
            } catch (Exception ex) {
                ex.printStackTrace();
                LOGGER.info("compile receipt error:{}", ex.getMessage());
                return null;
            }
        });
    }

    private void init() {
        txtUSD = new NumberField<>(createLabel(getTranslation("cash.receive"),
                ContextUtil.getProperty("usd_currency")), Float.class);
        txtUSD.setFormat("%.2f");
        txtUSD.setAutoselect(true);
        txtUSD.focus();
        txtUSD.setNumber(0f);
        txtUSD.setSuffixComponent(new Span(ContextUtil.getProperty("usd_currency")));
        txtUSD.getElement().setAttribute("theme", "align-right");
        txtUSD.addValueChangeListener(this::onInputReceiveCash);
        txtUSD.addKeyDownListener(Key.ENTER, event -> btnOk.click());
        txtUSD.addFocusListener(this::onFocusInput);

        txtKHR = new NumberField<>(createLabel(getTranslation("cash.receive"),
                ContextUtil.getProperty("kh_currency")), Long.class);
        txtKHR.setFormat("%d");
        txtKHR.setAutoselect(true);
        txtKHR.setNumber(0l);
        txtKHR.setSuffixComponent(new Span(ContextUtil.getProperty("kh_currency")));
        txtKHR.getElement().setAttribute("theme", "align-right");
        txtKHR.addValueChangeListener(this::onInputReceiveCash);
        txtKHR.addKeyDownListener(Key.ENTER, event -> btnOk.click());
        txtKHR.addFocusListener(this::onFocusInput);

        changeUSD = new NumberField<>(createLabel(getTranslation("change.money"),
                ContextUtil.getProperty("usd_currency")), Float.class);
        changeUSD.setFormat("%.2f");
        changeUSD.setNumber(0f);
        changeUSD.setReadOnly(true);
        changeUSD.setSuffixComponent(new Span(ContextUtil.getProperty("usd_currency")));
        changeUSD.getElement().setAttribute("theme", "align-right");

        changeKHR = new NumberField<>(createLabel(getTranslation("change.money"),
                ContextUtil.getProperty("kh_currency")), Long.class);
        changeKHR.setFormat("%d");
        changeKHR.setNumber(0l);
        changeKHR.setReadOnly(true);
        changeKHR.setSuffixComponent(new Span(ContextUtil.getProperty("kh_currency")));
        changeKHR.getElement().setAttribute("theme", "align-right");

        totalUSD = new NumberField<>(createLabel(getTranslation("total"),
                ContextUtil.getProperty("usd_currency")), Float.class);
        totalUSD.setFormat("%.2f");
        totalUSD.setNumber(dataProvider.getUSD());
        totalUSD.addClassName("color-red");
        totalUSD.setReadOnly(true);
        totalUSD.setSuffixComponent(new Span(ContextUtil.getProperty("usd_currency")));
        totalUSD.getElement().setAttribute("theme", "align-right");

        totalKHR = new NumberField<>(createLabel(getTranslation("total"),
                ContextUtil.getProperty("kh_currency")), Long.class);
        totalKHR.setFormat("%d");
        totalKHR.setNumber(dataProvider.getKHR());
        totalKHR.addClassName("color-red");
        totalKHR.setReadOnly(true);
        totalKHR.setSuffixComponent(new Span(ContextUtil.getProperty("kh_currency")));
        totalKHR.getElement().setAttribute("theme", "align-right");

        isPrint = new CheckBox(getTranslation("print.receipt"), true);

        btnOk = new Click(getTranslation("button.ok"), this::updateStock);
        btnOk.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnPreview = new Click(getTranslation("button.preview"));
        btnCancel = new Click(getTranslation("button.cancel"), event -> close());
        btnCancel.addThemeVariants(ButtonVariant.LUMO_ERROR);

        layout = new GridLayout();
        layout.add(0, txtUSD, txtKHR);
        layout.add(1, changeUSD, changeKHR);
        layout.add(2, totalUSD, totalKHR);
        layout.add(3, isPrint);
        layout.add(4, btnOk, btnCancel);
        layout.getRow(0, 3).getStyle().set("margin-top", "5px");

        add(layout);
    }

    private void updateStock(ClickEvent<Button> event) {
        try {
            JasperReport jasperReport = compileReportTask.get();
            if (jasperReport == null)
                throw new SaleException("Error compile receipt!");

            if (txtUSD.getNumber() == 0 && txtKHR.getNumber() == 0)
                throw new SaleException("please.input.cash.receive");

            if (txtUSD.getNumber() < totalUSD.getNumber() && txtKHR.getNumber() < totalKHR.getNumber())
                throw new SaleException("cash.receive.not.enough");

            LOGGER.info("prepare receipt");
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(getReceiptField());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, getParameter(), dataSource);
            LOGGER.info("prepare receipt successful");

            LOGGER.info("========= Start update stock =============");
            List<ProductSale> list = new ArrayList<>(dataProvider.getItems());
            LOGGER.info("Item ordered:{}", list.size());
            LOGGER.info("=========#######=======");
            for (ProductSale productSale : list) {
                Product product = productSale.getProduct();
                product.updateQTYStock(productSale.getQty());
                SaleHistory saleHistory = new SaleHistory(productSale);
                presenter.getRepository().saveOrUpdate(product);
                presenter.getRepository().saveOrUpdate(saleHistory);
                LOGGER.info(productSale.toString());
            }
            LOGGER.info("=========#######=======");
            LOGGER.info("total USD:{}", totalUSD.getNumber());
            LOGGER.info("total KHR:{}", totalKHR.getNumber());
            LOGGER.info("======== End update stock ===============");
            LOGGER.info("Is print receipt:{}", isPrint.getValue());

            if (isPrint.getValue()) {
                JasperPrintManager.printReport(jasperPrint, false);
                LOGGER.info("print receipt successful");
            }
            close();
            presenter.getView().clear(true);
        } catch (SaleException ex) {
            MessageDialogHelper.showMessageI18NDialog(ex.getKey(), () -> {
                if (txtKHR.getNumber() > 0) {
                    txtKHR.focus();
                    txtKHR.setAutoselect(true);
                } else if (txtUSD.getNumber() > 0) {
                    txtUSD.focus();
                    txtUSD.setAutoselect(true);
                }
            });
            changeKHR.setNumber(0l);
            changeUSD.setNumber(0f);
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageDialogHelper.showMessageDialog(ex.getMessage());
            //TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        }
    }

    private List<ReceiptField> getReceiptField() {
        LOGGER.info("prepare receipt field");
        List<ReceiptField> fieldList = new ArrayList<>();
        List<ProductSale> datasource = new ArrayList<>(dataProvider.getItems());
        for (ProductSale productSale : datasource) {
            ReceiptField receiptField = new ReceiptField();
            receiptField.setName(productSale.getNameEn());
            receiptField.setQty(String.valueOf(productSale.getQty()));
            receiptField.setPrice(productSale.getPriceFormat());
            receiptField.setDisc(productSale.getDiscountFormat());
            receiptField.setTotal(productSale.getAmountFormat());
            fieldList.add(receiptField);
        }
        LOGGER.info(new JSONArray(fieldList).toString(2));
        LOGGER.info("prepare receipt field successful");
        return fieldList;
    }

    private Map<String, Object> getParameter() {
        LOGGER.info("prepare receipt parameter");
        Map<String, Object> param = new HashMap<>();
        param.put("cashier", SecurityUtil.getUsername());
        param.put("exchangeRate", "$1=" + MoneyHelper.getRate());
        param.put("customer", "General");
        param.put("date", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        param.put("grandQty", String.valueOf(dataProvider.size()));
        param.put("grandTotal", String.format("$%.2f", totalUSD.getNumber()));
        param.put("cashReceiveUSD", String.format("$%.2f", txtUSD.getNumber()));
        param.put("cashReceiveKH", String.valueOf(txtKHR.getNumber()) + "៛");
        param.put("changeKH", String.valueOf(changeKHR.getNumber()) + "៛");
        param.put("changeUSD", String.format("$%.2f", changeUSD.getNumber()));
        LOGGER.info(new JSONObject(param).toString(2));
        LOGGER.info("prepare receipt parameter successful");
        return param;
    }

    private void onInputReceiveCash(AbstractField.ComponentValueChangeEvent<TextField, String> event) {
        if (event.getSource().equals(txtKHR)) {
            if (txtKHR.getNumber().equals(totalKHR.getNumber())) {
                changeKHR.setNumber(0l);
                changeUSD.setNumber(0f);
            } else if (txtKHR.getNumber() > totalKHR.getNumber()) {
                changeKHR.setNumber(txtKHR.getNumber() - totalKHR.getNumber());
                changeUSD.setNumber(MoneyHelper.exchangeToUSD(changeKHR.getNumber()));
            } else if (txtKHR.getNumber() == null || txtKHR.getNumber() <= 0) {
                changeKHR.setNumber(0l);
                changeUSD.setNumber(0f);
            }
        } else {
            if (txtUSD.getNumber().equals(totalUSD.getNumber())) {
                changeUSD.setNumber(0f);
                changeKHR.setNumber(0l);
            } else if (txtUSD.getNumber() > totalUSD.getNumber()) {
                changeUSD.setNumber(txtUSD.getNumber() - totalUSD.getNumber());
                changeKHR.setNumber(MoneyHelper.exchangeToKH(changeUSD.getNumber()));
            } else if (txtUSD.getNumber() == null || txtUSD.getNumber() <= 0) {
                changeKHR.setNumber(0l);
                changeUSD.setNumber(0f);
            }
        }
    }

    private void onFocusInput(FocusNotifier.FocusEvent<TextField> event) {
        if (event.getSource().equals(txtUSD)) {
            txtKHR.setNumber(0l);
            changeKHR.setNumber(0l);
        } else {
            txtUSD.setNumber(0f);
            changeUSD.setNumber(0f);
        }
    }

    private String createLabel(String label, String currency) {
        return new StringBuilder(label).append(" (").append(currency).append(")").toString();
    }
}

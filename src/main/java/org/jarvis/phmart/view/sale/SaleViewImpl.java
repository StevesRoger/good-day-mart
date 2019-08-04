package org.jarvis.phmart.view.sale;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.FocusNotifier;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jarvis.phmart.helper.IConstants;
import org.jarvis.phmart.helper.VaadinHelper;
import org.jarvis.phmart.model.entity.Product;
import org.jarvis.phmart.model.vaadin.ProductSale;
import org.jarvis.phmart.vaadin.PriceRenderer;
import org.jarvis.phmart.vaadin.SaleDataProvider;
import org.jarvis.phmart.vaadin.TotalFooter;
import org.jarvis.phmart.vaadin.dialog.ProductDetailDialog;
import org.jarvis.phmart.vaadin.dialog.SaleEditDialog;
import org.jarvis.phmart.view.main.MainViewImpl;
import org.jarvis.vaadin.ui.*;
import org.jarvis.vaadin.ui.layout.HorizontalScrollLayout;
import org.jarvis.vaadin.util.JavaScriptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created: kim chheng
 * Date: 01-Feb-2019 Fri
 * Time: 10:57 PM
 */
//@UIScope
//@SpringView(name = SaleViewImpl.VIEW_NAME)// The same instance will be used by all views of the UI
@Route(value = "sale", layout = MainViewImpl.class)
@PageTitle("Sale")
public class SaleViewImpl extends VerticalLayout implements SaleView, BeforeLeaveObserver {

    private TextBox txtBarcode;
    private DropDown<Product> cbProductName;
    private DropDown<Product> cbProductNameKh;
    private DropDown<Product> cbProductLabel;
    private NumberField<Integer> txtQTY;

    private Click btnAdd;
    private Click btnReady;
    private Click btnClear;
    private HorizontalScrollLayout imageScrollLayout;
    private Griddy<ProductSale> grid;
    private TotalFooter totalFooter;
    private transient Product product;

    private transient Logger log = LoggerFactory.getLogger(SaleViewImpl.class);

    private transient final SalePresenter presenter;

    public SaleViewImpl(@Autowired SalePresenter presenter) {
        this.presenter = presenter;
        this.presenter.onAttach(this);
        init();
        getStyle().set("padding-top", "0px");
        log.info(this.toString());
    }

    @Override
    public void init() {
        totalFooter = new TotalFooter(getTranslation("sale.total"));

        txtBarcode = new TextBox(getTranslation("product.barcode"));
        txtBarcode.setId("barcode");
        txtBarcode.focus();
        txtBarcode.addKeyDownListener(Key.ENTER, event -> presenter.onBarcodeInput(txtBarcode, grid.getExactDataProvider(), imageScrollLayout));

        cbProductName = new DropDown<>(getTranslation("product.name.en"), Product.class, presenter::listDropDownItem);
        cbProductName.setItemLabelGenerator(Product::getNameEn);
        cbProductName.filterItem(product -> product.getNameEn() != null && !product.getNameEn().isEmpty());
        cbProductName.addFocusListener(this::onFocus);
        cbProductName.addValueChangeListener(this::inputQty);

        cbProductNameKh = new DropDown<>(getTranslation("product.name.kh"), Product.class, presenter::listDropDownItem);
        cbProductNameKh.setItemLabelGenerator(Product::getNameKh);
        cbProductNameKh.filterItem(product -> product.getNameKh() != null && !product.getNameKh().isEmpty());
        cbProductNameKh.addFocusListener(this::onFocus);
        cbProductNameKh.addValueChangeListener(this::inputQty);

        cbProductLabel = new DropDown<>(getTranslation("product.label"), Product.class, presenter::listDropDownItem);
        cbProductLabel.setItemLabelGenerator(Product::getLabel);
        cbProductLabel.filterItem(product -> product.getLabel() != null && !product.getLabel().isEmpty());
        cbProductLabel.addFocusListener(this::onFocus);
        cbProductLabel.addValueChangeListener(this::inputQty);

        txtQTY = new NumberField<>(getTranslation("product.qty"), Integer.class);
        txtQTY.setAutoselect(true);
        txtQTY.setNumber(1);
        txtQTY.setRequired(true);
        txtQTY.setWidth("100px");
        txtQTY.addKeyDownListener(Key.ENTER, event -> btnAdd.click());

        btnAdd = new Click(getTranslation("button.add"),
                event -> presenter.createOrder(product, txtQTY.getNumber(), grid.getExactDataProvider(), imageScrollLayout));
        btnAdd.getElement().getThemeList().add("primary");

        btnReady = new Click(getTranslation("button.pay"), event -> presenter.executeOrder(grid.getExactDataProvider()));
        btnReady.getElement().getThemeList().add("primary");

        btnClear = new Click(getTranslation("button.clear"), this::onClickClear);
        btnClear.addThemeVariants(ButtonVariant.LUMO_ERROR);

        imageScrollLayout = new HorizontalScrollLayout(IConstants.LIST_IMAGE_VIEW);
        imageScrollLayout.setClassName("img-view");
        imageScrollLayout.setHeight("180px");

        grid = new Griddy<>();
        grid.setDataProvider(new SaleDataProvider(IConstants.MAP_PRODUCT, totalFooter));
        grid.addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        Grid.Column<ProductSale> columnName = grid.addColumn(ProductSale::getNameEn).setHeader(getTranslation("product.name.en")).setWidth("20px");
        Grid.Column<ProductSale> columnNameKh = grid.addColumn(ProductSale::getNameKh).setHeader(getTranslation("product.name.kh")).setWidth("20px");
        Grid.Column<ProductSale> columnLabel = grid.addColumn(ProductSale::getLabel).setHeader(getTranslation("product.label")).setWidth("20px");
        Grid.Column<ProductSale> columnQuantity = grid.addColumn(ProductSale::getQty).setHeader(getTranslation("product.qty")).setWidth("5px");
        Grid.Column<ProductSale> columnPrice = grid.addColumn(new PriceRenderer<>(ProductSale::getPriceValue)).setHeader(getTranslation("product.sale.price")).setWidth("5px");
        Grid.Column<ProductSale> columnDiscount = grid.addColumn(ProductSale::getDiscountFormat).setHeader(getTranslation("product.discount")).setWidth("5px");
        Grid.Column<ProductSale> columnAmount = grid.addColumn(ProductSale::getAmountFormat).setHeader(getTranslation("sale.amount")).setWidth("5px");
        Grid.Column<ProductSale> columnAction = grid.addColumn(VaadinHelper.gridActionTemplate(
                product -> new SaleEditDialog(product, grid, txtBarcode),
                product -> onRemove(product))).setHeader("Actions").setWidth("150px");
        columnAction.setFooter(totalFooter);
        grid.addItemDoubleClickListener(event -> new ProductDetailDialog(event.getItem().getProduct()));

        HorizontalLayout actionLayout = new HorizontalLayout();
        actionLayout.add(txtBarcode, cbProductName, cbProductNameKh, cbProductLabel, txtQTY, btnAdd, btnReady, btnClear);
        actionLayout.setDefaultVerticalComponentAlignment(Alignment.START);
        actionLayout.setVerticalComponentAlignment(Alignment.END, btnAdd);
        actionLayout.setVerticalComponentAlignment(Alignment.END, btnClear);
        actionLayout.setVerticalComponentAlignment(Alignment.END, btnReady);
        actionLayout.getStyle().set("padding-top", "0px");
        actionLayout.getStyle().set("margin-top", "0px");
        add(actionLayout, imageScrollLayout, grid);
    }

    private void inputQty(AbstractField.ComponentValueChangeEvent<ComboBox<Product>, Product> event) {
        if (event.getValue() != null) {
            txtQTY.focus();
            this.product = event.getValue();
        }
    }

    private void onRemove(ProductSale product) {
        SaleDataProvider saleDataProvider = grid.getExactDataProvider();
        ConfirmDialog dialog = new ConfirmDialog("Confirm remove",
                "Are you sure you want to remove " + "\"" + product.getNameEn() + "\"?",
                "Yes", e -> {
            saleDataProvider.removeItem(product);
            if (product.getImageView() != null)
                imageScrollLayout.remove(product.getImageView());
            clear(false);
            JavaScriptUtil.execute("removeVaadinLicense()");
        }, "No", e -> {
            JavaScriptUtil.execute("removeVaadinLicense()");
        });
        dialog.setConfirmButtonTheme("error primary");
        dialog.setCancelButtonTheme("primary");
        dialog.open();
    }

    private void onFocus(FocusNotifier.FocusEvent<ComboBox<Product>> event) {
        if (!cbProductName.equals(event.getSource()))
            cbProductName.setValue(null);
        if (!cbProductNameKh.equals(event.getSource()))
            cbProductNameKh.setValue(null);
        if (!cbProductLabel.equals(event.getSource()))
            cbProductLabel.setValue(null);
    }


    private void onClickClear(ClickEvent<Button> event) {
        if (!IConstants.MAP_PRODUCT.isEmpty()) {
            ConfirmDialog dialog = new ConfirmDialog("Confirm clear",
                    "Are you sure?",
                    "Yes", e -> {
                clear(true);
                JavaScriptUtil.execute("removeVaadinLicense()");
            }, "No", e -> {
                JavaScriptUtil.execute("removeVaadinLicense()");
            });
            dialog.setConfirmButtonTheme("error primary");
            dialog.setCancelButtonTheme("primary");
            dialog.open();
        }
    }

    @Override
    public void clear(boolean clearAll) {
        txtQTY.setNumber(1);
        txtBarcode.focus();
        cbProductName.setValue(null);
        cbProductNameKh.setValue(null);
        cbProductLabel.setValue(null);
        txtBarcode.setValue("");
        if (clearAll) {
            SaleDataProvider saleDataProvider = grid.getExactDataProvider();
            saleDataProvider.clearAll();
            imageScrollLayout.removeAll();
        }
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
        btnAdd.setText(getTranslation("button.add"));
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        //BeforeLeaveEvent.ContinueNavigationAction action = event.postpone();
        //txtQTY.setData(null);
        //action.proceed();
    }
}

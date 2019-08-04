package org.jarvis.phmart.view.product;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.converter.LocalDateToDateConverter;
import com.vaadin.flow.data.converter.StringToFloatConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.jarvis.core.util.ContextUtil;
import org.jarvis.phmart.helper.IConstants;
import org.jarvis.phmart.helper.VaadinHelper;
import org.jarvis.phmart.model.entity.Category;
import org.jarvis.phmart.model.entity.Product;
import org.jarvis.phmart.model.entity.Supplier;
import org.jarvis.phmart.model.entity.reference.Form;
import org.jarvis.phmart.model.vaadin.ProductFilter;
import org.jarvis.phmart.vaadin.DiscountRenderer;
import org.jarvis.phmart.vaadin.LimitFooter;
import org.jarvis.phmart.vaadin.PageFooter;
import org.jarvis.phmart.vaadin.PriceRenderer;
import org.jarvis.phmart.vaadin.dialog.ProductDetailDialog;
import org.jarvis.phmart.view.main.MainViewImpl;
import org.jarvis.vaadin.data.binder.EntityBinder;
import org.jarvis.vaadin.recevier.MultiFileMemory;
import org.jarvis.vaadin.ui.*;
import org.jarvis.vaadin.ui.layout.GridLayout;
import org.jarvis.vaadin.util.JavaScriptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZoneId;

/**
 * Created: kim chheng
 * Date: 02-Feb-2019 Sat
 * Time: 4:47 PM
 */
@Route(value = "product", layout = MainViewImpl.class)
@PageTitle("Product")
public class ProductViewImpl extends VerticalLayout implements ProductView, BeforeLeaveObserver {

    private static final long serialVersionUID = -4587235680584881430L;

    private TextBox txtNameEn;
    private TextBox txtNameKh;
    private NumberField<Integer> txtQTY;
    private NumberField<Float> txtSalePrice;
    private NumberField<Float> txtOriginalPrice;
    private NumberField<Integer> txtDiscount;
    private TextBox txtLabel;
    private TextBox txtBarcode;
    private TextBox txtDesc;
    private TextBox txtBrand;
    private DropDown<Category> cbCategory;
    private DropDown<Form> cbForm;
    //private NoClearDropDown<Currency> cbCurrency;
    private DateField expireDate;
    private DateField publishDate;
    private DropDown<Supplier> cbSupplier;
    private ImageView imageView;
    private ImageUpload imageUpload;
    private Click btnSave;
    private Click btnUpload;
    private Click btnClear;
    private Griddy<Product> grid;
    private EntityBinder<Product> productBinder;
    private VerticalLayout gallery;
    private LimitFooter limitFooter;
    private PageFooter pageFooter;

    private transient final ProductPresenter presenter;

    private transient Logger log = LoggerFactory.getLogger(ProductViewImpl.class);

    public ProductViewImpl(@Autowired ProductPresenter presenter) {
        this.productBinder = new EntityBinder<>(Product.class, product -> product != null && product.getId() != null);
        this.presenter = presenter;
        this.presenter.onAttach(this);
        this.init();
        this.getStyle().set("padding-top", "0px");
        log.info(this.toString());
    }

    @Override
    public void init() {
        add(initForm(), initGrid());
        initBinding();
       /* this.addListener(KeyDownEvent.class, event -> {
            if (event.getCode().isPresent() && event.getKey() != null) {
                List<String> codes = event.getCode().get().getKeys();
                List<String> keys = event.getKey().getKeys();
                if (!codes.isEmpty() && !keys.isEmpty()) {
                    if (event.getLocation() != null && "STANDARD".equals(event.getLocation().name())) {
                        //barcode += keys.get(0);
                    }
                }
            }
        });
       /* UI.getCurrent().getElement().addEventListener("keydown",domEvent -> {
            System.out.println(domEvent.getEventData().toString());
        });*/

    }

    private GridLayout initForm() {

        GridLayout gridLayout = new GridLayout();

        txtNameEn = new TextBox(getTranslation("product.name.en"), "Tea");
        txtNameEn.setRequired(true);
        txtNameKh = new TextBox(getTranslation("product.name.kh"), ContextUtil.getProperty("tea"));
        txtQTY = new NumberField<>(getTranslation("product.qty"), Integer.class);
        txtQTY.setAutoselect(true);
        txtQTY.setNumber(1);
        txtQTY.setRequired(true);
        txtQTY.setFormat("%d");
        gridLayout.add(0, txtNameEn, txtNameKh, txtQTY);

        txtSalePrice = new NumberField<>(getTranslation("product.sale.price"), Float.class);
        txtSalePrice.setNumber(1f);
        txtSalePrice.setRequired(true);
        txtSalePrice.setAutoselect(true);
        txtSalePrice.setFormat("%.2f");
        txtSalePrice.setPlaceholder("0.00");
        txtSalePrice.setSuffixComponent(new Span(ContextUtil.getProperty("usd_currency")));
        txtSalePrice.getElement().setAttribute("theme", "align-right");
        txtDiscount = new NumberField<>(getTranslation("product.discount"), Integer.class);
        txtDiscount.setAutoselect(true);
        txtDiscount.setFormat("%d");
        txtDiscount.setNumber(0);
        txtDiscount.setSuffixComponent(new Span("%"));
        txtDiscount.getElement().setAttribute("theme", "align-right");
        txtBarcode = new TextBox(getTranslation("product.barcode"), "2434545DFG353");
        txtBarcode.setId("barcode");
        //cbCurrency = new NoClearDropDown<>(getTranslation("product.currency"), Currency.class, presenter::listDropDownItem);
        //cbCurrency.setDefaultSelect("USD");
        gridLayout.add(1, txtSalePrice, txtDiscount, txtBarcode);

        txtOriginalPrice = new NumberField<>(getTranslation("product.original.price"), Float.class);
        txtOriginalPrice.setNumber(0f);
        txtOriginalPrice.setAutoselect(true);
        txtOriginalPrice.setFormat("%.2f");
        txtOriginalPrice.setPlaceholder("0.00");
        txtOriginalPrice.setSuffixComponent(new Span(ContextUtil.getProperty("usd_currency")));
        txtOriginalPrice.getElement().setAttribute("theme", "align-right");
        txtLabel = new TextBox(getTranslation("product.label"), "label");
        txtBrand = new TextBox(getTranslation("product.brand"), "LUX");
        gridLayout.add(2, txtOriginalPrice, txtBrand, txtLabel);

        cbForm = new DropDown<>(getTranslation("product.form"), Form.class, presenter::listDropDownItem);
        txtDesc = new TextBox(getTranslation("product.description"));
        cbCategory = new DropDown<>(getTranslation("product.category"), Category.class, presenter::listDropDownItem);
        gridLayout.add(3, txtDesc, cbForm, cbCategory);

        publishDate = new DateField(getTranslation("product.publish.date"));
        publishDate.setLocale(IConstants.LOCALE_KH);
        publishDate.setValue(null);
        expireDate = new DateField((getTranslation("product.expire.date")));
        expireDate.setLocale(IConstants.LOCALE_KH);
        expireDate.setValue(null);
        cbSupplier = new DropDown<>(getTranslation("product.supplier"), Supplier.class, presenter::listDropDownItem);
        gridLayout.add(4, cbSupplier, publishDate, expireDate);

        btnSave = new Click(getTranslation("button.save"), event -> presenter.clickSaveEvent(imageUpload.getFileReceiver(), productBinder));
        btnUpload = new Click(getTranslation("button.image"));
        btnUpload.setId("btnUpload");
        btnClear = new Click(getTranslation("button.clear"), event -> clearForm());
        btnClear.getElement().getThemeList().add("primary");
        btnSave.getElement().getThemeList().add("primary");
        btnUpload.getElement().getThemeList().add("primary");

        gallery = new VerticalLayout();
        gallery.setId("gallery");
        gallery.setPadding(false);
        gallery.setMargin(false);
        gallery.setSpacing(false);
        gallery.setWidth("100px");

        //block imageView upload
        imageUpload = new ImageUpload(new MultiFileMemory());
        imageUpload.addClassName("jarvis-upload");
        imageUpload.setDropAllowed(false);
        imageUpload.setUploadButton(btnUpload);
        imageUpload.setMaxFiles(3);
        imageUpload.addSucceededListener(event -> presenter.uploadImageSucceedEvent(event, imageUpload, imageView));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(btnSave, imageUpload, btnClear);
        buttonLayout.getStyle().set("margin-bottom", "0px");
        gridLayout.add(5, buttonLayout);
        gridLayout.getRow(0, 5).getStyle().set("margin-top", "5px");

        imageView = new ImageView();
        imageView.setWidth("344px");
        imageView.setHeight("365px");
        imageView.setClassName("img-view");
        gridLayout.add(0, 1, imageView, gallery);
        gridLayout.getColumn(1).setMargin(true);
        gridLayout.getColumn(1).setSpacing(true);
        gridLayout.getColumn(1).getStyle().set("margin-top", "5px");

        return gridLayout;
    }

    private Grid<Product> initGrid() {

        pageFooter = new PageFooter(getTranslation("page"), presenter);
        limitFooter = new LimitFooter(presenter, pageFooter);
        ProductFilter productFilter = presenter.getProductFilter();

        grid = new Griddy<>();
        grid.setDataProvider(productFilter.getDataProvider());
        grid.addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        //Grid.Column<Product> columnNo=grid.addColumn(TemplateRenderer.of("[[index]]")).setHeader("No").setWidth("5px");
        Grid.Column<Product> columnId = grid.addColumn(Product::getId).setHeader("Id");
        columnId.setVisible(false);
        Grid.Column<Product> columnName = grid.addColumn(Product::getNameEn).setHeader(getTranslation("product.name.en"));
        columnName.setSortProperty("nameEn").setWidth("70px");
        Grid.Column<Product> columnNameKh = grid.addColumn(Product::getNameKh).setHeader(getTranslation("product.name.kh"));
        columnNameKh.setSortProperty("nameKh").setWidth("70px");
        Grid.Column<Product> columnQuantity = grid.addColumn(Product::getQuantity).setHeader(getTranslation("product.qty"));
        columnQuantity.setSortProperty("quantity").setWidth("30px");
        Grid.Column<Product> columnPrice = grid.addColumn(new PriceRenderer<>(Product::getPriceValue)).setHeader(getTranslation("product.sale.price"));
        columnPrice.setSortProperty("price").setWidth("30px");
        Grid.Column<Product> columnDiscount = grid.addColumn(new DiscountRenderer<>(Product::getDiscount)).setHeader(getTranslation("product.discount"));
        columnDiscount.setWidth("30px");
        Grid.Column<Product> columnLabel = grid.addColumn(Product::getLabel).setHeader(getTranslation("product.label"));
        columnLabel.setSortProperty("label").setWidth("60px");
        Grid.Column<Product> columnForm = grid.addColumn(Product::getForm).setHeader(getTranslation("product.form"));
        Grid.Column<Product> columnCategory = grid.addColumn(Product::getCategory).setHeader(getTranslation("product.category"));
        Grid.Column<Product> columnAction = grid.addColumn(VaadinHelper.gridActionTemplate(
                product -> presenter.gridClickEditEvent(grid, product, imageUpload.getFileReceiver(), productBinder),
                product -> presenter.gridClickRemoveEvent(product, grid))).setHeader("Actions");
        grid.addItemDoubleClickListener(event -> new ProductDetailDialog(event.getItem()));
        productFilter.setHeaderRow(grid.prependHeaderRow());
        productFilter.addFieldFilter(columnName, new TextBox().setProperty("nameEn").width("130px"));
        productFilter.addFieldFilter(columnNameKh, new TextBox().setProperty("nameKh").width("130px"));
        productFilter.addFieldFilter(columnQuantity, new NumberField<>(Integer.class).setProperty("quantity").width("85px"));
        productFilter.addFieldFilter(columnPrice, new NumberField<>(Float.class).setProperty("price").width("85px"));
        productFilter.addFieldFilter(columnLabel, new TextBox().setProperty("label").width("130px"));
        productFilter.addFieldFilter(columnDiscount, new NumberField<>(Integer.class).setProperty("discount").width("85px"));
        productFilter.addFieldFilter(columnForm, new DropDown<>(Form.class, presenter::listDropDownItem).setProperty("form"));
        productFilter.addFieldFilter(columnCategory, new DropDown<>(Category.class, presenter::listDropDownItem).setProperty("category"));
        productFilter.addFieldFilter(columnAction, new TextBox().setProperty("barcode").width("130px"));
        columnName.setFooter(limitFooter);
        columnAction.setFooter(pageFooter);
        return grid;
    }

    private void initBinding() {
        productBinder.forField(txtBarcode).bind(Product::getBarcode, Product::setBarcode);
        productBinder.forField(txtNameEn).asRequired(getTranslation("message.not.empty"))
                .bind(Product::getNameEn, Product::setNameEn);
        productBinder.forField(txtNameKh).bind(Product::getNameKh, Product::setNameKh);
        productBinder.forField(txtQTY).asRequired(getTranslation("message.not.empty"))
                .withConverter(new StringToIntegerConverter(0, getTranslation("message.not.empty")))
                .bind(Product::getQuantity, Product::setQuantity);
        productBinder.forField(txtSalePrice).asRequired(getTranslation("message.not.empty"))
                .withConverter(new StringToFloatConverter(0f, getTranslation("message.not.empty")))
                .bind(Product::getSalePrice, Product::setSalePrice);
        productBinder.forField(txtLabel).bind(Product::getLabel, Product::setLabel);
        productBinder.forField(txtOriginalPrice).withConverter(new StringToFloatConverter(0f, "message.error.number.format"))
                .bind(Product::getOriginalPrice, Product::setOriginalPrice);
        productBinder.forField(txtBrand).bind(Product::getBrand, Product::setBrand);
        productBinder.forField(txtDesc).bind(Product::getDescription, Product::setDescription);
        //productBinder.forField(cbCurrency).asRequired(getTranslation("message.not.empty")).bind(Product::getCurrency, Product::setCurrency);
        productBinder.forField(txtDiscount).withConverter(new StringToIntegerConverter(getTranslation("message.error.number.format")))
                .bind(Product::getDiscount, Product::setDiscount);
        productBinder.forField(cbForm).bind(Product::getForm, Product::setForm);
        productBinder.forField(cbCategory).bind(Product::getCategory, Product::setCategory);

        productBinder.forField(expireDate).withConverter(new LocalDateToDateConverter(ZoneId.systemDefault()))
                .bind(Product::getExpireDate, Product::setExpireDate);
        productBinder.forField(publishDate).withConverter(new LocalDateToDateConverter(ZoneId.systemDefault()))
                .bind(Product::getPublishDate, Product::setPublishDate);
        productBinder.forField(cbSupplier).bind(Product::getSupplier, Product::setSupplier);
    }

    @Override
    public void clearForm() {
        productBinder.clear();
        txtQTY.setNumber(1);
        txtSalePrice.setNumber(1f);
        txtOriginalPrice.setNumber(0f);
        txtDiscount.setNumber(0);
        publishDate.setValue(null);
        expireDate.setValue(null);
        gallery.removeAll();
        MultiFileMemory multiFileMemory = imageUpload.getFileReceiver();
        multiFileMemory.clear();
        imageUpload.getElement().setProperty("files", "[]");
        JavaScriptUtil.execute("imageView($0)", "");
        //JavaScriptUtil.execute("enableUpload($0)", 4);
        JavaScriptUtil.execute("$('#upload').prop('files',[])");
        //  imageUpload.getElement().setPropertyJson("files", Json.createArray());
        grid.getSelectionModel().deselectAll();
        log.info("=====>>> clear form!");
    }

    @Override
    public void createGallery(Integer id, String mimeType, StreamResource streamResource) {
        ImageView image = new ImageView(streamResource);
        image.setMimeType(mimeType);
        image.setClassName("img-gallery");
        JavaScriptUtil.execute("galleryClick($('.img-gallery'))");
        ImageView close = new ImageView("frontend/image/icon/cross_16.png", "cross_16.png");
        close.setClassName("close");
        close.addClickListener(event -> presenter.galleryImageRemoveEvent(imageUpload.getFileReceiver(), image.getName(), id));
        Block block = new Block(image, close);
        block.setId(image.getName());
        block.setClassName("img-container");
        gallery.addComponentAsFirst(block);
        JavaScriptUtil.execute("imageView($0)", image.getSrc());
        //if (4 <= gallery.getChildren().count())
        //JavaScriptUtil.execute("$('#btnUpload').prop('disabled', 'true')");
    }

    @Override
    public void girdRefreshItem(Product product) {
        grid.refreshItem(product);
    }

    @Override
    public void gridRefreshAll() {
        grid.refreshAll();
        limitFooter.totalRecord();
        pageFooter.setTotalPage(limitFooter.pages());
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {
        txtBarcode.setLabel(getTranslation("product.barcode"));
        txtNameEn.setLabel(getTranslation("product.name.en"));
        txtNameKh.setLabel(getTranslation("product.name.kh"));
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent leaveEvent) {
        BeforeLeaveEvent.ContinueNavigationAction action = leaveEvent.postpone();
        if (productBinder.isUpdate() && (productBinder.isChanged())) {
            VerticalLayout message = VaadinHelper.createDiscardMessage(productBinder);
            ConfirmDialog dialog = new ConfirmDialog();
            dialog.setHeader("Unsaved changes " + productBinder.getEntity().getNameEn());
            message.addComponentAsFirst(new Label("Do you want to save or discard your changes before leaving this page?"));
            dialog.setText(message);
            dialog.setConfirmButton("Save", event -> {
                presenter.clickSaveEvent(imageUpload.getFileReceiver(), productBinder);
                action.proceed();
                JavaScriptUtil.execute("removeVaadinLicense()");
            });
            dialog.setCancelButton("Discard", event -> {
                action.proceed();
                JavaScriptUtil.execute("removeVaadinLicense()");
            }, "error primary");
            dialog.open();
        } else
            action.proceed();
    }
}

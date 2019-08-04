package org.jarvis.phmart.view.report;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jarvis.core.util.ContextUtil;
import org.jarvis.orm.hibernate.criterion.BaseCriteria;
import org.jarvis.orm.hibernate.criterion.Restriction;
import org.jarvis.phmart.helper.IConstants;
import org.jarvis.phmart.model.entity.Product;
import org.jarvis.phmart.model.entity.SaleHistory;
import org.jarvis.phmart.model.vaadin.SaleReport;
import org.jarvis.phmart.view.main.MainViewImpl;
import org.jarvis.vaadin.ui.*;
import org.jarvis.vaadin.ui.layout.GridLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created: kim chheng
 * Date: 26-Apr-2019 Fri
 * Time: 2:42 PM
 */
@Route(value = "report", layout = MainViewImpl.class)
@PageTitle("Report")
public class ReportViewImpl extends VerticalLayout implements ReportView {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportViewImpl.class);
    private DateField startDate;
    private DateField endDate;
    private DropDown<Product> cbProductName;
    private DropDown<Product> cbProductNameKh;
    private DropDown<Product> cbProductLabel;
    private Griddy<SaleReport> grid;
    private Click btnSearch;
    private Click btnClear;
    private NumberField txtTotal;

    private transient final ReportPresenter presenter;

    public ReportViewImpl(@Autowired ReportPresenter presenter) {
        this.presenter = presenter;
        init();
    }

    @Override
    public void init() {
        GridLayout gridLayout = new GridLayout();
        startDate = new DateField(getTranslation("start.date"));
        startDate.setLocale(IConstants.LOCALE_KH);
        startDate.setValue(null);
        endDate = new DateField((getTranslation("end.date")));
        endDate.setLocale(IConstants.LOCALE_KH);
        endDate.setValue(null);
        gridLayout.add(0, startDate, endDate);

        cbProductName = new DropDown<>(getTranslation("product.name.en"), Product.class, presenter::listDropDownItem);
        cbProductName.setItemLabelGenerator(Product::getNameEn);
        cbProductName.filterItem(product -> product.getNameEn() != null && !product.getNameEn().isEmpty());

        cbProductNameKh = new DropDown<>(getTranslation("product.name.kh"), Product.class, presenter::listDropDownItem);
        cbProductNameKh.setItemLabelGenerator(Product::getNameKh);
        cbProductNameKh.filterItem(product -> product.getNameKh() != null && !product.getNameKh().isEmpty());

        cbProductLabel = new DropDown<>(getTranslation("product.label"), Product.class, presenter::listDropDownItem);
        cbProductLabel.setItemLabelGenerator(Product::getLabel);
        cbProductLabel.filterItem(product -> product.getLabel() != null && !product.getLabel().isEmpty());

        gridLayout.add(1, cbProductName, cbProductNameKh, cbProductLabel);

        btnSearch = new Click(getTranslation("button.search"), this::onSearch);
        btnSearch.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnClear = new Click(getTranslation("button.clear"), this::onClear);
        gridLayout.add(2, btnSearch, btnClear);

        grid = new Griddy<>();
        grid.setDataProvider(new ListDataProvider<>(new ArrayList<>()));
        grid.addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        Grid.Column<SaleReport> columnName = grid.addColumn(SaleReport::getProductNameEn).setHeader(getTranslation("product.name.en"));
        columnName.setSortProperty("productNameEn").setWidth("70px");
        Grid.Column<SaleReport> columnNameKh = grid.addColumn(SaleReport::getProductNameKh).setHeader(getTranslation("product.name.kh"));
        columnNameKh.setSortProperty("productNameKh").setWidth("70px");
        Grid.Column<SaleReport> columnLabel = grid.addColumn(SaleReport::getProductLabel).setHeader(getTranslation("product.label"));
        columnLabel.setSortProperty("label").setWidth("60px");
        Grid.Column<SaleReport> columnForm = grid.addColumn(SaleReport::getProductForm).setHeader(getTranslation("product.form"));
        Grid.Column<SaleReport> columnCategory = grid.addColumn(SaleReport::getProductCategory).setHeader(getTranslation("product.category"));
        Grid.Column<SaleReport> columnQuantity = grid.addColumn(SaleReport::getQty).setHeader(getTranslation("product.qty"));
        columnQuantity.setSortProperty("qty").setWidth("30px");
        Grid.Column<SaleReport> columnTotal = grid.addColumn(new NumberRenderer<>(SaleReport::getTotal,
                "%(,.2f$", Locale.US, "$ 0.00")).setHeader(getTranslation("sale.total"));
        columnTotal.setSortProperty("total").setWidth("30px");

        txtTotal = new NumberField<>(Float.class);
        txtTotal.setFormat("%.2f");
        txtTotal.setNumber(0f);
        txtTotal.getStyle().set("padding-left", "5px").set("width", "100px");
        txtTotal.addClassName("color-red");
        txtTotal.setReadOnly(true);
        txtTotal.setSuffixComponent(new Span(ContextUtil.getProperty("usd_currency")));
        txtTotal.getElement().setAttribute("theme", "align-right");

        Span span = new Span(getTranslation("sale.total"));
        span.add(txtTotal);
        columnTotal.setFooter(span);
        add(gridLayout, grid);
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }

    public void onSearch(ClickEvent<Button> event) {
        float total = 0f;
        BaseCriteria<SaleHistory> criteria = new BaseCriteria<>(SaleHistory.class);
        criteria.orderByASC("productId");
        if (startDate.getValue() != null)
            criteria.condition(Restriction.greatThanOrEqual("createdDate", startDate.getDate()));
        if (endDate.getValue() != null)
            criteria.condition(Restriction.lessThan("createdDate", endDate.getDate()));
        if (cbProductName.getValue() != null && cbProductName.getValue().getNameEn() != null)
            criteria.condition(Restriction.equal("productNameEn", cbProductName.getValue().getNameEn()));
        if (cbProductNameKh.getValue() != null && cbProductNameKh.getValue().getNameKh() != null)
            criteria.condition(Restriction.equal("productNameKh", cbProductNameKh.getValue().getNameKh()));
        if (cbProductLabel.getValue() != null && cbProductLabel.getValue().getLabel() != null)
            criteria.condition(Restriction.equal("productLabel", cbProductLabel.getValue().getLabel()));
        if (criteria.hasCondition()) {
            List<SaleHistory> list = presenter.getRepository().list(criteria);
            Set<SaleHistory> set = new HashSet<>(list);
            List<SaleReport> reportList = new ArrayList<>();
            for (SaleHistory unique : set) {
                SaleReport saleReport = new SaleReport(unique);
                reportList.add(saleReport);
                for (SaleHistory saleHistory : list)
                    if (saleHistory.equals(unique)) {
                        saleReport.add(saleHistory);
                    }
                total += saleReport.getTotal();
            }
            grid.setListDataSource(reportList);
        } else
            grid.setListDataSource(new ArrayList<>());
        grid.refreshAll();
        txtTotal.setNumber(total);
    }

    public void onClear(ClickEvent<Button> event) {
        startDate.setValue(null);
        endDate.setValue(null);
        cbProductName.setValue(null);
        cbProductNameKh.setValue(null);
        cbProductLabel.setValue(null);
        grid.setListDataSource(new ArrayList<>());
        grid.refreshAll();
        txtTotal.setNumber(0f);
    }
}

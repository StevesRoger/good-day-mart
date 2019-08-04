package org.jarvis.phmart.model.vaadin;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.jarvis.phmart.model.entity.Product;
import org.jarvis.vaadin.ui.DropDown;
import org.jarvis.vaadin.ui.TextBox;
import org.jarvis.vaadin.ui.base.AnyComponent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created: kim chheng
 * Date: 10-Mar-2019 Sun
 * Time: 1:54 PM
 */
public class ProductFilter {

    private Integer limit = 10;
    private Integer page = 1;
    private Object search;
    private Map<String, AnyComponent> fieldFilterMap;
    private HeaderRow headerRow;
    private ConfigurableFilterDataProvider<Product, Void, ProductFilter> dataProvider;
    private List<Product> list;

    public ProductFilter(CallbackDataProvider<Product, ProductFilter> dataProvider) {
        this.dataProvider = dataProvider.withConfigurableFilter();
        this.dataProvider.setFilter(this);
        this.fieldFilterMap = new HashMap<>();
        this.list = new ArrayList<>();
    }

    public Integer getLimit() {
        return limit;
    }

    public ProductFilter setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public Integer getPage() {
        return page;
    }

    public ProductFilter setPage(Integer page) {
        this.page = page;
        return this;
    }

    public List<Product> getList() {
        return list;
    }

    public void setList(List<Product> list) {
        this.list = list;
    }

    public HeaderRow getHeaderRow() {
        return headerRow;
    }

    public void setHeaderRow(HeaderRow headerRow) {
        this.headerRow = headerRow;
    }

    public ConfigurableFilterDataProvider<Product, Void, ProductFilter> getDataProvider() {
        return dataProvider;
    }

    public <T> T getSearch() {
        return (T) search;
    }

    public void refreshAll() {
        this.dataProvider.refreshAll();
    }

    public void refreshItem(Product product) {
        this.dataProvider.refreshItem(product);
    }

    public void addFieldFilter(Grid.Column<Product> column, TextBox textField) {
        if (column == null || textField == null)
            throw new IllegalArgumentException("Column or TextField can not be null");
        fieldFilterMap.put(textField.getProperty(), textField);
        headerRow.getCell(column).setComponent(textField);
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setPlaceholder("Search");
        textField.addValueChangeListener(event -> dataProvider.refreshAll());
    }

    public <T extends Serializable> void addFieldFilter(Grid.Column<Product> column, DropDown<T> dropDown) {
        if (column == null || dropDown == null)
            throw new IllegalArgumentException("Column or DateField can not be null");
        if ("discount".equals(dropDown.getProperty()))
            dropDown.getStyle().set("width", "100px");
        else
            dropDown.getStyle().set("width", "150px");
        fieldFilterMap.put(dropDown.getProperty(), dropDown);
        headerRow.getCell(column).setComponent(dropDown);
        dropDown.setPlaceholder("Search");
        dropDown.addValueChangeListener(event -> dataProvider.refreshAll());
    }

    public boolean isSearch(String property) {
        AnyComponent anyComponent = getComponent(property);
        if (anyComponent instanceof TextBox) {
            search = ((TextBox) anyComponent).getValue();
            return search != null && !"".equals(search);
        } else if (anyComponent instanceof DropDown) {
            search = ((DropDown<Serializable>) anyComponent).getValue();
            return search != null;
        }
        return false;
    }

    public AnyComponent getComponent(String property) {
        return fieldFilterMap.get(property);
    }
}

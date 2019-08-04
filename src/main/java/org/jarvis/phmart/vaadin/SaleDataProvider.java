package org.jarvis.phmart.vaadin;

import org.jarvis.phmart.helper.MoneyHelper;
import org.jarvis.phmart.model.entity.Product;
import org.jarvis.phmart.model.vaadin.ProductSale;
import org.jarvis.vaadin.data.JarvisListDataProvider;

import java.util.Map;

/**
 * Created: kim chheng
 * Date: 14-May-2019 Tue
 * Time: 9:39 AM
 */
public class SaleDataProvider extends JarvisListDataProvider<ProductSale> {

    private TotalFooter totalFooter;
    private Map<Long, ProductSale> mapItem;

    public SaleDataProvider(Map<Long, ProductSale> items, TotalFooter totalFooter) {
        super(items.values());
        this.totalFooter = totalFooter;
        this.mapItem = items;
        init();
    }

    private void init() {
        if (mapItem != null)
            mapItem.values().parallelStream().forEach(item -> item.calculate());
        calculateTotal();
    }

    @Override
    public boolean addItem(ProductSale item) {
        ProductSale productSale = mapItem.get(item.getId());
        if (productSale != null) {
            productSale.setQty(productSale.getQty() + item.getQty());
            productSale.calculate();
            refreshAll();
            return false;
        } else {
            mapItem.put(item.getId(), item);
            refreshAll();
            return true;
        }
    }

    @Override
    public void refreshItem(ProductSale item) {
        calculateTotal();
        super.refreshItem(item);
    }

    @Override
    public void refreshAll() {
        calculateTotal();
        super.refreshAll();
    }

    @Override
    public void clearAll() {
        mapItem.clear();
        super.refreshAll();
    }

    public boolean contain(Long key) {
        return mapItem.containsKey(key);
    }

    public boolean hasEnoughQty(Product product) {
        return hasEnoughQty(product.getId());
    }

    public boolean hasEnoughQty(long id) {
        ProductSale productSale = mapItem.get(id);
        if (productSale != null)
            return productSale.hasEnoughQty();
        return true;
    }

    public boolean isEmpty() {
        return getItems().isEmpty();
    }

    public int size() {
        return getItems().size();
    }

    public Long getKHR() {
        return totalFooter.getTxtKHR().getNumber();
    }

    public Float getUSD() {
        return totalFooter.getTxtUSD().getNumber();
    }

    private void calculateTotal() {
        float total = 0;
        for (ProductSale productSale : getItems())
            total += productSale.getAmount();
        totalFooter.setValue(MoneyHelper.roundHalfUp(total));
    }
}

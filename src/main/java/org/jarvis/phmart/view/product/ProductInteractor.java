package org.jarvis.phmart.view.product;

import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import org.hibernate.criterion.MatchMode;
import org.jarvis.orm.hibernate.criterion.BaseCriteria;
import org.jarvis.orm.hibernate.criterion.Restriction;
import org.jarvis.orm.hibernate.domain.reference.Status;
import org.jarvis.phmart.model.vaadin.ProductFilter;
import org.jarvis.phmart.model.entity.Product;
import org.jarvis.phmart.view.base.AnyInteractor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created: kim chheng
 * Date: 17-Mar-2019 Sun
 * Time: 3:53 PM
 */
public class ProductInteractor extends AnyInteractor<ProductPresenter> {

    public ProductInteractor(ProductPresenter presenter) {
        super(presenter);
    }

    public int count(Query<Product, ProductFilter> query) {
        if (query.getFilter().isPresent()) {
            ProductFilter filter = query.getFilter().get();
            BaseCriteria<Product> criteria = new BaseCriteria<>(Product.class);
            criteria.condition(Restriction.equal("status", Status.ACTIVE));
            //criteria.desc("createdDate");
            criteria.orderByASC("id");
            criteria.limit(filter.getLimit());
            criteria.page(filter.getPage());
            if (filter.isSearch("nameEn"))
                criteria.condition(Restriction.ilike("nameEn", filter.getSearch(), MatchMode.ANYWHERE));
            if (filter.isSearch("nameKh"))
                criteria.condition(Restriction.ilike("nameKh", filter.getSearch(), MatchMode.ANYWHERE));
            if (filter.isSearch("quantity"))
                criteria.condition(Restriction.equal("quantity", Integer.valueOf(filter.getSearch())));
            if (filter.isSearch("price"))
                criteria.condition(Restriction.equal("price", Float.valueOf(filter.getSearch())));
            if (filter.isSearch("label"))
                criteria.condition(Restriction.ilike("label", filter.getSearch(), MatchMode.ANYWHERE));
            if (filter.isSearch("form"))
                criteria.condition(Restriction.equal("form", filter.getSearch()));
            if (filter.isSearch("category"))
                criteria.condition(Restriction.equal("category", filter.getSearch()));
            if (filter.isSearch("discount"))
                criteria.condition(Restriction.equal("discount", Integer.valueOf(filter.getSearch())));
            if (filter.isSearch("barcode"))
                criteria.condition(Restriction.equal("barcode", filter.getSearch()));
            filter.setList(presenter.getRepository().list(criteria));
            return filter.getList().size();
        }
        return 0;
    }

    public Stream<Product> fetch(Query<Product, ProductFilter> query) {
        List<Product> list = query.getFilter().get().getList();
        if (!query.getSortOrders().isEmpty()) {
            QuerySortOrder sortOrder = query.getSortOrders().get(0);
            String property = sortOrder.getSorted();
            SortDirection direction = sortOrder.getDirection();
            return list.stream().sorted((a, b) -> compare(a, b, property, direction));
        }
        return list.stream();
    }

    private int compare(Product a, Product b, String property, SortDirection direction) {
        try {
            Field field = ReflectionUtils.findField(Product.class, property);
            field.setAccessible(true);
            Object valueA = ReflectionUtils.getField(field, a);
            Object valueB = ReflectionUtils.getField(field, b);
            if (valueA == null || valueB == null)
                return 0;
            if (valueA instanceof Integer)
                return SortDirection.ASCENDING == direction ?
                        ((Integer) valueA).compareTo((Integer) valueB) :
                        ((Integer) valueB).compareTo((Integer) valueA);
            else if (valueA instanceof Float)
                return SortDirection.ASCENDING == direction ?
                        ((Float) valueA).compareTo((Float) valueB) :
                        ((Float) valueB).compareTo((Float) valueA);
            else
                return SortDirection.ASCENDING == direction ?
                        valueA.toString().compareTo(valueB.toString()) :
                        valueB.toString().compareTo(valueA.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }
}

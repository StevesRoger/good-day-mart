package org.jarvis.phmart.helper;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.dom.Element;
import org.jarvis.phmart.model.entity.Product;
import org.jarvis.vaadin.data.binder.EntityBinder;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created: kim chheng
 * Date: 05-Jun-2019 Wed
 * Time: 10:51 PM
 */
public final class VaadinHelper {

    public static <T> TemplateRenderer<T> gridActionTemplate(Consumer<T> edit, Consumer<T> delete) {
        return TemplateRenderer.<T>of(
                "<button on-click='handleUpdate' class='btn btn-xs btn-primary'>Edit</button>&nbsp;" +
                        "<button on-click='handleRemove' class='btn btn-xs btn-danger'>Delete</button>")
                .withEventHandler("handleUpdate", bean -> edit.accept(bean))
                .withEventHandler("handleRemove", bean -> delete.accept(bean));
    }

    public static HorizontalLayout displayRemain(Product product) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.add(new H5("Quantity: " + product.getQuantity()));
        layout.add(new H5("Price: " + product.getSalePrice()));
        layout.add(new H5("Discount: " + product.getDiscount()));
        return layout;
    }

    public static VerticalLayout createDiscardMessage(EntityBinder<Product> productBinder) {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setMargin(false);
        verticalLayout.setSpacing(false);
        verticalLayout.setPadding(false);
        Set<String> set = new HashSet<>(productBinder.getChangedMessages());
        set.parallelStream().forEach(msg -> verticalLayout.add(new Label(msg)));
        return verticalLayout;
    }

    public static void setResizableColumn(Grid.Column column) {
        column.setResizable(true);
        Element parent = column.getElement().getParent();
        while (parent != null
                && "vaadin-grid-column-group".equals(parent.getTag())) {
            parent.setProperty("resizable", "true");
            parent = parent.getParent();
        }
    }
}

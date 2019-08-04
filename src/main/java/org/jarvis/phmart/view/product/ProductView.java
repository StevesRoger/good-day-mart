package org.jarvis.phmart.view.product;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.server.StreamResource;
import org.jarvis.phmart.model.entity.Product;
import org.jarvis.phmart.view.base.AnyView;
import org.jarvis.vaadin.data.binder.EntityBinder;
import org.jarvis.vaadin.recevier.MultiFileMemory;
import org.jarvis.vaadin.ui.Griddy;
import org.jarvis.vaadin.ui.ImageUpload;
import org.jarvis.vaadin.ui.ImageView;

/**
 * Created: kim chheng
 * Date: 17-Feb-2019 Sun
 * Time: 9:44 AM
 */
public interface ProductView extends AnyView {

    void createGallery(Integer id, String mimeType, StreamResource streamResource);

    void gridRefreshAll();

    void girdRefreshItem(Product product);

    void clearForm();

    interface ProductViewListener {

        void clickSaveEvent(MultiFileMemory multiFileMemory, EntityBinder<Product> productBinder);

        void gridClickEditEvent(Griddy<Product> grid, Product product, MultiFileMemory multiFileMemory, EntityBinder<Product> productBinder);

        void gridClickRemoveEvent(Product product, Grid<Product> grid);

        void uploadImageSucceedEvent(SucceededEvent event, ImageUpload upload, ImageView imageView);

        void galleryImageRemoveEvent(MultiFileMemory multiFileMemory, String filename, Integer id);
    }
}

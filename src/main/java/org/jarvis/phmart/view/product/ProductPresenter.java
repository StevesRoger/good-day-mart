package org.jarvis.phmart.view.product;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.jarvis.common.function.Runner;
import org.jarvis.common.util.FileUtil;
import org.jarvis.common.util.ImageUtil;
import org.jarvis.orm.hibernate.domain.reference.Status;
import org.jarvis.orm.hibernate.util.EntityUtil;
import org.jarvis.phmart.helper.ImageHelper;
import org.jarvis.phmart.helper.MessageDialogHelper;
import org.jarvis.phmart.helper.VaadinHelper;
import org.jarvis.phmart.model.entity.Product;
import org.jarvis.phmart.model.vaadin.ProductFilter;
import org.jarvis.phmart.view.base.AnyPresenter;
import org.jarvis.sercurity.util.SecurityUtil;
import org.jarvis.vaadin.data.binder.EntityBinder;
import org.jarvis.vaadin.recevier.MultiFileMemory;
import org.jarvis.vaadin.ui.Griddy;
import org.jarvis.vaadin.ui.ImageUpload;
import org.jarvis.vaadin.ui.ImageView;
import org.jarvis.vaadin.util.JavaScriptUtil;
import org.jarvis.vaadin.util.StreamResourceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.annotation.SessionScope;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * Created: kim chheng
 * Date: 09-Mar-2019 Sat
 * Time: 11:30 AM
 */
@SpringComponent
@SessionScope
public class ProductPresenter extends AnyPresenter<ProductView, ProductInteractor> implements ProductView.ProductViewListener {

    private final ProductFilter productFilter;

    private static Logger log = LoggerFactory.getLogger(ProductPresenter.class);

    public ProductPresenter() {
        this.interactor = new ProductInteractor(this);
        this.productFilter = new ProductFilter(DataProvider.fromFilteringCallbacks(interactor::fetch, interactor::count));
        log.info(this.toString());
    }

    public ProductFilter getProductFilter() {
        return productFilter;
    }

    @Override
    public void clickSaveEvent(MultiFileMemory multiFileMemory, EntityBinder<Product> productBinder) {
        if (productBinder.validate().isOk()) {
            if (productBinder.isUpdate()) {
                Product product = productBinder.getEntity();
                productBinder.writeBeanIfValid(product);
                EntityUtil.modified(SecurityUtil.getUsername(), product);
                ImageHelper.updateImage(multiFileMemory, product);
                repository.update(product);
                view.girdRefreshItem(product);
                MessageDialogHelper.showNotification("Update product success!");
                log.info("=====>>> user update product!");
            } else {
                Product product = new Product();
                productBinder.writeBeanIfValid(product);
                ImageHelper.createImage(multiFileMemory, product);
                repository.save(product);
                view.gridRefreshAll();
                MessageDialogHelper.showNotification("Add new product success!");
                log.info("=====>>> user insert new product");
            }
            view.clearForm();
        }
    }

    @Override
    public void gridClickEditEvent(Griddy<Product> grid, Product product, MultiFileMemory multiFileMemory, EntityBinder<Product> productBinder) {
        JavaScriptUtil.execute("scrollTop()");
        Runner runner = () -> {
            if (!product.equals(productBinder.getEntity())) {
                view.clearForm();
                productBinder.readBean(product);
                product.getImageSet().forEach(image ->
                        view.createGallery(image.getId(), image.getMimeType(),
                                StreamResourceUtil.toStreamResource(image.getName(), image.getBytes())));
                grid.getSelectionModel().select(product);
            }
        };
        if (productBinder.isUpdate() && !product.equals(productBinder.getEntity()) && (productBinder.isChanged())) {
            VerticalLayout message = VaadinHelper.createDiscardMessage(productBinder);
            ConfirmDialog dialog = new ConfirmDialog();
            dialog.setHeader("Unsaved changes " + productBinder.getEntity().getNameEn());
            message.addComponentAsFirst(new Label("Do you want to save or discard your changed?"));
            dialog.setText(message);
            dialog.setConfirmButton("Save", event -> {
                clickSaveEvent(multiFileMemory, productBinder);
                runner.run();
                JavaScriptUtil.execute("removeVaadinLicense()");
            });
            dialog.setCancelButton("Discard", event -> {
                runner.run();
                JavaScriptUtil.execute("removeVaadinLicense()");
            });
            dialog.setCancelButtonTheme("error primary");
            dialog.open();
        } else
            runner.run();
    }

    @Override
    public void uploadImageSucceedEvent(SucceededEvent event, ImageUpload upload, ImageView imageView) {
        try {
            MultiFileMemory multiFileMemory = upload.getFileReceiver();
            BufferedImage bufferedImage = ImageIO.read(multiFileMemory.getInputStream(event.getFileName()));
            if (bufferedImage.getWidth() > 350 || bufferedImage.getHeight() > 370) {
                ByteArrayOutputStream outputStream = ImageUtil.scaleSmooth(bufferedImage,
                        FileUtil.getExtension(event.getFileName()), 344, 365);
                StreamResource streamResource = StreamResourceUtil.toStreamResource(event.getFileName(), outputStream.toByteArray());
                multiFileMemory.setStreamResource(event.getFileName(), streamResource);
                multiFileMemory.setBytes(event.getFileName(), outputStream.toByteArray());
            }
            view.createGallery(null, event.getMIMEType(), multiFileMemory.getStreamResource(event.getFileName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gridClickRemoveEvent(Product product, Grid<Product> grid) {
        grid.getSelectionModel().select(product);
        MessageDialogHelper.showConfirmDialog("Confirm delete",
                "Are you sure you want to delete \"" + product.getNameEn() + "\"?",
                () -> {
                    product.setStatus(Status.INACTIVE);
                    repository.saveOrUpdate(product);
                    view.clearForm();
                    view.gridRefreshAll();
                    MessageDialogHelper.showNotification("Delete product success!");
                    JavaScriptUtil.execute("removeVaadinLicense()");
                }, () -> {
                    grid.getSelectionModel().deselectAll();
                    JavaScriptUtil.execute("removeVaadinLicense()");
                });
        /*ConfirmDialog dialog = new ConfirmDialog("Confirm delete",
                "Are you sure you want to delete \"" + product.getNameEn() + "\"?",
                "Yes", event -> {
            product.setStatus(Status.INACTIVE);
            repository.saveOrUpdate(product);
            view.clearForm();
            view.gridRefreshAll();
            Notification.show("Delete product succeeded!", 5000, Notification.Position.TOP_END);
            JavaScriptUtil.execute("removeVaadinLicense()");
        }, "No", event -> {
            grid.getSelectionModel().deselectAll();
            JavaScriptUtil.execute("removeVaadinLicense()");
        });
        dialog.setConfirmButtonTheme("error primary");
        dialog.setCancelButtonTheme("primary");
        dialog.open();*/
    }

    @Override
    public void galleryImageRemoveEvent(MultiFileMemory multiFileMemory, String fileName, Integer id) {
        multiFileMemory.addRemovedId(id);
        multiFileMemory.remove(fileName);
        JavaScriptUtil.execute("removeImage($0)", fileName);
    }

}

package org.jarvis.phmart.helper;

import com.vaadin.flow.server.StreamResource;
import org.jarvis.core.util.ContextUtil;
import org.jarvis.orm.hibernate.repository.EntityRepository;
import org.jarvis.phmart.model.entity.Image;
import org.jarvis.phmart.model.entity.Product;
import org.jarvis.vaadin.recevier.MultiFileMemory;
import org.jarvis.vaadin.ui.ImageView;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created: kim chheng
 * Date: 13-Apr-2019 Sat
 * Time: 4:15 PM
 */
public final class ImageHelper {

    public static void createImage(MultiFileMemory multiFileMemory, Product product) {
        multiFileMemory.getAllFileResource().parallelStream().forEach(file ->
                product.addImage(file.getFileName(), file.getMimeType(), file.getBytes(), false));
    }

    public static void updateImage(MultiFileMemory multiFileMemory, Product product) {
        Set<Image> imageSet = new LinkedHashSet<>();
        multiFileMemory.getListIdRemoved().forEach(id -> {
            ContextUtil.getBean(EntityRepository.class).deleteById(id, Image.class);
            product.getImageSet().parallelStream().forEach(image -> {
                if (!image.getId().equals(id))
                    imageSet.add(image);
            });
        });
        product.setImageSet(imageSet);
        createImage(multiFileMemory, product);
    }

    public static ImageView createViewImage(StreamResource streamResource) {
        ImageView imageView = new ImageView(streamResource, streamResource.getName());
        imageView.setWidth("180px");
        imageView.setHeight("170px");
        return imageView;
    }

}

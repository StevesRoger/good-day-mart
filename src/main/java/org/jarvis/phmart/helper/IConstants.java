package org.jarvis.phmart.helper;

import com.vaadin.flow.component.Component;
import org.jarvis.phmart.model.vaadin.ProductSale;

import java.time.LocalDate;
import java.util.*;

/**
 * Created by KimChheng on 7/7/2019.
 */
public interface IConstants {
    Locale LOCALE_EN = new Locale("en", "GB");
    Locale LOCALE_KH = new Locale("kh", "KH");
    List<Component> LIST_IMAGE_VIEW = new ArrayList<>();
    Map<Long, ProductSale> MAP_PRODUCT = new HashMap<>();

}

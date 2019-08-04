package org.jarvis.phmart.model.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.jarvis.common.model.AnyObject;
import org.jarvis.phmart.config.TranslationProvider;
import org.jarvis.phmart.helper.IConstants;
import org.jarvis.vaadin.ui.ImageView;
import org.jarvis.vaadin.ui.TextView;

import java.util.Locale;
import java.util.Objects;

/**
 * Created: kim chheng
 * Date: 18-May-2019 Sat
 * Time: 4:28 PM
 */
public class Language extends AnyObject {

    private static final long serialVersionUID = -2222320821139162874L;

    public static final Language KH = new Language(IConstants.LOCALE_KH,
            "frontend/image/icon/khmer-flag-x24.png", "khmer-flag-x24", "KH");
    public static final Language USA = new Language(IConstants.LOCALE_EN,
            "frontend/image/icon/english-flag-x24.png", "usa-flag-x24", "EN");

    private Locale locale;
    private String srcFlag;
    private String label;
    private String name;

    public Language() {
    }

    public Language(Locale locale, String srcFlag, String name, String label) {
        this.locale = locale;
        this.srcFlag = srcFlag;
        this.label = label;
        this.name = name;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getSrcFlag() {
        return srcFlag;
    }

    public void setSrcFlag(String srcFlag) {
        this.srcFlag = srcFlag;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Component render() {
        HorizontalLayout layout = new HorizontalLayout();
        ImageView image = new ImageView(this.getSrcFlag(), this.getName());
        TextView label = new TextView(getLabel());
        label.getStyle().set("margin-left", "4px");
        layout.add(image, label);
        //layout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER,image);
        //layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        return layout;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object that) {
        return equals(that, "name");
    }
}

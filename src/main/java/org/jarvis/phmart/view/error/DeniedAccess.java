package org.jarvis.phmart.view.error;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jarvis.phmart.view.base.AnyView;

/**
 * Created: kim chheng
 * Date: 18-May-2019 Sat
 * Time: 2:26 PM
 */
@Route(value = "accessDenied")
@PageTitle("Access Denied")
public class DeniedAccess extends HorizontalLayout implements AnyView {

    public DeniedAccess() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(new H2("Access denied!"));
        add(formLayout);
        setVerticalComponentAlignment(Alignment.CENTER, formLayout);
    }

    @Override
    public void init() {
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }
}

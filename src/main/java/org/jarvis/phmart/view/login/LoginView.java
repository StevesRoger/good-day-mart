package org.jarvis.phmart.view.login;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.jarvis.phmart.view.base.AnyInteractor;
import org.jarvis.phmart.view.base.AnyPresenter;
import org.jarvis.phmart.view.base.AnyView;
import org.jarvis.phmart.view.sale.SaleViewImpl;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created: kim chheng
 * Date: 18-May-2019 Sat
 * Time: 10:06 PM
 */
@Tag("sa-login-view")
@Route(value = "login")
@PageTitle("Login")
public class LoginView extends VerticalLayout implements AnyView, AfterNavigationObserver, BeforeEnterObserver {

    private LoginOverlay login = new LoginOverlay();

    public LoginView() {
        login.setAction("login");
        login.setOpened(true);
        //login.setTitle((Component) null);
        login.setTitle("Good Day Mart");
        login.setDescription("Login into system");
        login.setForgotPasswordButtonVisible(false);
        getElement().appendChild(login.getElement());
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
       // login.setI18n(createTranslatedI18N());
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        login.setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            //String js="window.location.replace(window.location.href.replace('sale', 'login'))";
            //UI.getCurrent().getPage().executeJavaScript(js);
            beforeEnterEvent.rerouteTo(SaleViewImpl.class);
        }
    }

    private LoginI18n createTranslatedI18N() {
        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.setForm(new LoginI18n.Form());

        /*i18n.getHeader().setTitle(getTranslation(MY_APP_NAME.getKey());
        //i18n.getHeader().setDescription("");
        i18n.getForm().setSubmit(getTranslation(LOGIN.getKey()));
        i18n.getForm().setTitle(getTranslation(LOGIN.getKey()));
        i18n.getForm().setUsername(getTranslation(USERNAME.getKey()));
        i18n.getForm().setPassword(getTranslation(PASSWORD.getKey()));
        i18n.getErrorMessage().setTitle(getTranslation(LOGIN_ERROR_TITLE.getKey()));
        i18n.getErrorMessage().setMessage(getTranslation(LOGIN_ERROR.getKey()));
        i18n.setAdditionalInformation(getTranslation(LOGIN_INFO.getKey()));*/
        return i18n;
    }

    @Override
    public void init() {

    }

}


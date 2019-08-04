package org.jarvis.phmart.view.main;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.router.*;
import org.jarvis.phmart.helper.MoneyHelper;
import org.jarvis.phmart.model.vaadin.Language;
import org.jarvis.phmart.vaadin.ExchangeRateLabel;
import org.jarvis.phmart.view.base.AnyView;
import org.jarvis.phmart.view.product.ProductViewImpl;
import org.jarvis.phmart.view.report.ReportViewImpl;
import org.jarvis.phmart.view.sale.SaleViewImpl;
import org.jarvis.phmart.view.setting.SettingViewImpl;
import org.jarvis.sercurity.domain.entity.JarvisUser;
import org.jarvis.vaadin.ui.ImageView;
import org.jarvis.vaadin.ui.TextView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

/**
 * Created: kim chheng
 * Date: 01-Feb-2019 Fri
 * Time: 4:23 PM
 */
@Route(value = "")
//@Push
@PageTitle("home")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@HtmlImport("frontend://style/custom-style.html")
@HtmlImport("frontend://style/no-clear-button.html")
@HtmlImport("frontend://style/griddy-theme.html")
//@StyleSheet("frontend://css/bootstrap-3.4.0.min.css")
@JavaScript("frontend://js/jquery-3.4.1.min.js")
//@JavaScript("frontend://js/bootstrap-3.4.0.min.js")
//@JavaScript("frontend://js/jquery.scannerdetection.js")
//@JavaScript("frontend://js/barcode-listener.js")
@JavaScript("frontend://js/main.js")
public class MainViewImpl extends Div implements RouterLayout, MainView {

    private static final long serialVersionUID = 6209389632515972510L;

    @Autowired
    private transient MainPresenter mainPresenter;

    private UI currentUi;
    private ExchangeRateLabel exchangeRate;
    private Select<Language> language;
    private JarvisUser user;

    public MainViewImpl() {
        init();
    }

    @Override
    public void init() {
        user = (JarvisUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HorizontalLayout navigationBar = new HorizontalLayout();
        navigationBar.setWidth("100%");
        navigationBar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        navigationBar.add(createNavigationButton("Product", ProductViewImpl.class,
                VaadinIcon.STOCK, user.hasAnyRoles("ROLE_ADMIN")));
        navigationBar.add(createNavigationButton("Sale", SaleViewImpl.class,
                VaadinIcon.CART, user.hasAnyRoles("ROLE_ADMIN", "ROLE_USER")));
        navigationBar.add(createNavigationButton("Report", ReportViewImpl.class,
                VaadinIcon.NEWSPAPER, user.hasAnyRoles("ROLE_ADMIN")));
        navigationBar.add(createNavigationButton("Setting", SettingViewImpl.class
                , VaadinIcon.COGS, user.hasAnyRoles("ROLE_ADMIN")));

        exchangeRate = new ExchangeRateLabel(getTranslation("exchange.rate"));
        setExchangeRate(MoneyHelper.getRate());
        language = new Select<>(Language.KH, Language.USA);
        language.addClassName("lang");
        language.setWidth("100px");
        language.setEmptySelectionAllowed(false);
        language.setValue(Language.USA);
        language.setRenderer(new ComponentRenderer<>(Language::render));
        //language.getElement().getStyle().set("margin-left", "auto");
        //language.addValueChangeListener(event -> VaadinSession.getCurrent().setLocale(event.getValue().getLocale()));

        ImageView iconUserx64 = new ImageView("frontend/image/icon/user-x64.png", "user-x64.png");
        iconUserx64.getStyle().set("width", "40px").set("height", "40px");
        iconUserx64.addClassName("user-login");
        ImageView iconUser32 = new ImageView("frontend/image/icon/user-x64.png", "user-x64.png");
        iconUser32.getStyle().set("width", "32px").set("height", "32px");
        TextView username = new TextView(user.getUsername());
        username.getStyle().set("font-weight", "500")
                .set("margin-left", "4px");

        ContextMenu contextMenu = new ContextMenu(iconUserx64);
        contextMenu.setOpenOnClick(true);
        contextMenu.add(new HorizontalLayout(iconUser32, username));
        contextMenu.add(new Hr(), contextMenu.addItem("Logout", this::logout));

        navigationBar.add(exchangeRate, language, iconUserx64);
        add(new VerticalLayout(navigationBar));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        mainPresenter.onAttach(this);
        currentUi = attachEvent.getUI();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        mainPresenter.onDetach();
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent) {

    }

    @Override
    public void showRouterLayoutContent(HasElement content) {
        if (content != null) {
            if (AnyView.class.isAssignableFrom(content.getClass()))
                ((AnyView) content).onMainViewNavigate(this);
            getElement().appendChild(Objects.requireNonNull(content.getElement()));
        }
    }

    @Override
    public void setExchangeRate(int rate) {
        exchangeRate.setRate(rate);
    }

    private void logout(ClickEvent<MenuItem> event) {
        SecurityContextHolder.clearContext();
        UI.getCurrent().getSession().close();
        UI.getCurrent().getPage().reload();
    }

    private RouterLink createNavigationButton(String caption, Class<? extends Component> view, VaadinIcon icon, boolean role) {
        RouterLink routerLink = new RouterLink(null, view);
        routerLink.add(new Icon(icon), new Label(caption));
        routerLink.setVisible(role);
        //reviews.addClassName("route-link");
        routerLink.addClassName("main-layout__nav-item");
        routerLink.setHighlightCondition(HighlightConditions.sameLocation());
        return routerLink;
    }

}

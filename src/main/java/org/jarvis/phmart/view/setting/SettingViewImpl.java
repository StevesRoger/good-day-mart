package org.jarvis.phmart.view.setting;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.jarvis.phmart.view.main.MainView;
import org.jarvis.phmart.view.main.MainViewImpl;
import org.jarvis.phmart.view.setting.tab.*;
import org.jarvis.vaadin.ui.PageTabs;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created: kim chheng
 * Date: 17-Feb-2019 Sun
 * Time: 4:52 PM
 */
@Route(value = "setting", layout = MainViewImpl.class)
@PageTitle("Setting")
public class SettingViewImpl extends VerticalLayout implements SettingView {

    private MainView mainView;
    private PageTabs pageTabs;
    private TabUser tabUser;
    private TabExchangeRate tabExchangeRate;
    private TabCategory tabCategory;
    private TabForm tabForm;
    private TabSupplier tabSupplier;
    private final SettingPresenter presenter;

    public SettingViewImpl(@Autowired SettingPresenter presenter) {
        this.presenter = presenter;
        this.presenter.onAttach(this);
        init();
    }

    @Override
    public void init() {
        pageTabs = new PageTabs();
        tabUser = new TabUser();
        pageTabs.addTab(tabUser, getTranslation("user"));
        tabExchangeRate = new TabExchangeRate(presenter);
        pageTabs.addTab(tabExchangeRate, getTranslation("exchange.rate"));
        tabCategory = new TabCategory();
        pageTabs.addTab(tabCategory, getTranslation("product.category"));
        tabForm = new TabForm();
        pageTabs.addTab(tabForm, getTranslation("product.form"));
        tabSupplier = new TabSupplier();
        pageTabs.addTab(tabSupplier, getTranslation("product.supplier"));
        add(pageTabs.getTabs());
        add(pageTabs.getPages());
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {

    }

    @Override
    public void updateExchangeRateLabel(int rate) {
        mainView.setExchangeRate(rate);
    }

    @Override
    public void onMainViewNavigate(MainView main) {
        this.mainView = main;
    }
}

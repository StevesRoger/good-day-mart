package org.jarvis.phmart.view.setting.tab;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.jarvis.core.util.ContextUtil;
import org.jarvis.orm.hibernate.domain.base.Identity;
import org.jarvis.orm.hibernate.repository.EntityRepository;
import org.jarvis.phmart.helper.MessageDialogHelper;
import org.jarvis.phmart.helper.VaadinHelper;
import org.jarvis.vaadin.factory.ComponentFactory;
import org.jarvis.vaadin.ui.Click;
import org.jarvis.vaadin.ui.Griddy;
import org.jarvis.vaadin.ui.TextBox;
import org.jarvis.vaadin.ui.layout.GridLayout;
import org.jarvis.vaadin.util.JavaScriptUtil;

import java.util.List;

/**
 * Created by KimChheng on 7/10/2019.
 */
public abstract class BaseTab<T extends Identity> extends Div {

    protected TextBox txtNameEn;
    protected TextBox txtNameKh;
    protected Click btnSave;
    protected Click btnClear;
    protected Griddy<T> grid;
    protected GridLayout layout;
    protected List<T> items;
    protected T bean;
    protected VerticalLayout container;

    protected EntityRepository repository;

    public BaseTab() {
        repository = ContextUtil.getBean(EntityRepository.class);
        init();
    }

    public void init() {
        container = new VerticalLayout();
        txtNameEn = ComponentFactory.createTextBox(getTranslation("name.en"));
        txtNameEn.setRequired(true);
        txtNameKh = ComponentFactory.createTextBox(getTranslation("name.kh"));

        btnSave = new Click(getTranslation("button.save"), this::onClick);
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnClear = new Click(getTranslation("button.clear"), this::onClick);
        btnClear.addThemeVariants(ButtonVariant.LUMO_ERROR);

        items = getListItems();
        grid = new Griddy<>();
        grid.setDataProvider(new ListDataProvider<>(items));
        grid.addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);

        layout = new GridLayout();
        layout.add(0, txtNameEn, txtNameKh);
        layout.add(1, btnSave, btnClear);

        container.add(layout);
        container.add(grid);
        add(container);
    }

    protected void addColumnAction() {
        grid.addColumn(VaadinHelper.gridActionTemplate(
                bean -> {
                    clear();
                    onGridEdit(bean);
                    JavaScriptUtil.execute("scrollTop()");
                },
                bean -> onGridDelete(bean))).setHeader("Actions");
    }

    protected abstract void onGridDelete(T bean);

    protected abstract void onGridEdit(T bean);

    protected void clear() {
        txtNameEn.setValue("");
        txtNameKh.setValue("");
        bean = null;
        grid.refreshAll();
    }

    private void onClick(ClickEvent<Button> event) {
        if (event.getSource().equals(btnClear)) {
            clear();
            onClickClear(event);
        } else if (event.getSource().equals(btnSave)) {
            if (txtNameEn.getValue() == null || txtNameEn.isEmpty())
                MessageDialogHelper.showMessageDialog(getTranslation("please.input.name"));
            else
                onClickSave(event);
        }
    }

    protected abstract List<T> getListItems();

    protected abstract void onClickSave(ClickEvent<Button> event);

    protected void onClickClear(ClickEvent<Button> event) {
        clear();
    }
}

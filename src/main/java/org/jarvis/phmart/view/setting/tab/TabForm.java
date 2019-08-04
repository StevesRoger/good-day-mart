package org.jarvis.phmart.view.setting.tab;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import org.jarvis.core.util.ContextUtil;
import org.jarvis.orm.hibernate.criterion.BaseCriteria;
import org.jarvis.phmart.helper.MessageDialogHelper;
import org.jarvis.phmart.helper.VaadinHelper;
import org.jarvis.phmart.model.entity.reference.Form;
import org.jarvis.vaadin.util.JavaScriptUtil;

import java.util.List;

/**
 * Created by KimChheng on 7/10/2019.
 */
public class TabForm extends BaseTab<Form> {

    @Override
    public void init() {
        super.init();
        txtNameEn.setPlaceholder("Bottle");
        txtNameKh.setPlaceholder(ContextUtil.getProperty("bottle"));
        Grid.Column<Form> columnName = grid.addColumn(Form::getValue).setHeader(getTranslation("name.en"));
        columnName.setSortProperty("nameEn").setWidth("70px");
        VaadinHelper.setResizableColumn(columnName);
        Grid.Column<Form> columnNameKh = grid.addColumn(Form::getValueKh).setHeader(getTranslation("name.kh"));
        columnNameKh.setSortProperty("nameKh").setWidth("70px");
        VaadinHelper.setResizableColumn(columnNameKh);
        addColumnAction();
    }

    @Override
    protected void onGridDelete(Form form) {
        MessageDialogHelper.showConfirmDialog("Confirm delete",
                "Are you sure you want to delete \"" + form.getValue() + "\"?",
                () -> {
                    repository.delete(form);
                    items.remove(form);
                    clear();
                    MessageDialogHelper.showNotification("Delete form success!");
                    JavaScriptUtil.execute("removeVaadinLicense()");
                }, () -> {
                    grid.getSelectionModel().deselectAll();
                    JavaScriptUtil.execute("removeVaadinLicense()");
                });
    }

    @Override
    protected void onGridEdit(Form form) {
        grid.getSelectionModel().select(form);
        bean = form;
        txtNameEn.setValue(form.getValue());
        txtNameKh.setValue(form.getValueKh());
    }

    @Override
    protected void onClickSave(ClickEvent<Button> event) {
        if (bean == null || bean.getId() == null) {
            bean = new Form();
            items.add(bean);
        }
        bean.setValue(txtNameEn.getValue());
        bean.setValueKh(txtNameKh.getValue());
        bean.setDescription(txtNameEn.getValue());
        repository.saveOrUpdate(bean);
        clear();
        MessageDialogHelper.showNotification("Success!");
    }

    @Override
    protected List<Form> getListItems() {
        BaseCriteria<Form> criteria = new BaseCriteria<>(Form.class);
        //criteria.desc("createdDate");
        criteria.orderByASC("id");
        return repository.list(criteria);
    }
}

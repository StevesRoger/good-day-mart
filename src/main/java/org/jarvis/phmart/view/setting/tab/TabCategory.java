package org.jarvis.phmart.view.setting.tab;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import org.jarvis.core.util.ContextUtil;
import org.jarvis.orm.hibernate.criterion.BaseCriteria;
import org.jarvis.orm.hibernate.criterion.Restriction;
import org.jarvis.orm.hibernate.domain.reference.Status;
import org.jarvis.phmart.helper.MessageDialogHelper;
import org.jarvis.phmart.helper.VaadinHelper;
import org.jarvis.phmart.model.entity.Category;
import org.jarvis.vaadin.util.JavaScriptUtil;

import java.util.List;

/**
 * Created by KimChheng on 7/10/2019.
 */
public class TabCategory extends BaseTab<Category> {


    @Override
    public void init() {
        super.init();
        txtNameEn.setPlaceholder("Drink");
        txtNameKh.setPlaceholder(ContextUtil.getProperty("drink"));
        Grid.Column<Category> columnName = grid.addColumn(Category::getNameEn).setHeader(getTranslation("name.en"));
        columnName.setSortProperty("nameEn").setWidth("70px");
        VaadinHelper.setResizableColumn(columnName);
        Grid.Column<Category> columnNameKh = grid.addColumn(Category::getNameKh).setHeader(getTranslation("name.kh"));
        columnNameKh.setSortProperty("nameKh").setWidth("70px");
        VaadinHelper.setResizableColumn(columnNameKh);
        addColumnAction();
    }

    @Override
    protected void onGridEdit(Category category) {
        grid.getSelectionModel().select(category);
        bean = category;
        txtNameEn.setValue(category.getNameEn());
        txtNameKh.setValue(category.getNameKh());
    }

    @Override
    protected void onGridDelete(Category category) {
        MessageDialogHelper.showConfirmDialog("Confirm delete",
                "Are you sure you want to delete \"" + category.getNameEn() + "\"?",
                () -> {
                    category.setStatus(Status.INACTIVE);
                    repository.saveOrUpdate(category);
                    items.remove(category);
                    clear();
                    MessageDialogHelper.showNotification("Delete category success!");
                    JavaScriptUtil.execute("removeVaadinLicense()");
                }, () -> {
                    grid.getSelectionModel().deselectAll();
                    JavaScriptUtil.execute("removeVaadinLicense()");
                });
    }

    @Override
    protected List<Category> getListItems() {
        BaseCriteria<Category> criteria = new BaseCriteria<>(Category.class);
        criteria.condition(Restriction.equal("status", Status.ACTIVE));
        //criteria.desc("createdDate");
        criteria.orderByASC("id");
        return repository.list(criteria);
    }

    @Override
    protected void onClickSave(ClickEvent<Button> event) {
        if (bean == null || bean.getId() == null) {
            bean = new Category();
            items.add(bean);
        }
        bean.setNameEn(txtNameEn.getValue());
        bean.setNameKh(txtNameKh.getValue());
        repository.saveOrUpdate(bean);
        clear();
        MessageDialogHelper.showNotification("Success!");
    }
}

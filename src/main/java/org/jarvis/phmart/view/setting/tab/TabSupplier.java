package org.jarvis.phmart.view.setting.tab;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import org.jarvis.core.util.ContextUtil;
import org.jarvis.orm.hibernate.criterion.BaseCriteria;
import org.jarvis.orm.hibernate.criterion.Restriction;
import org.jarvis.orm.hibernate.domain.reference.Status;
import org.jarvis.phmart.helper.MessageDialogHelper;
import org.jarvis.phmart.helper.VaadinHelper;
import org.jarvis.phmart.model.entity.Supplier;
import org.jarvis.vaadin.factory.ComponentFactory;
import org.jarvis.vaadin.ui.EmailBox;
import org.jarvis.vaadin.ui.NumberField;
import org.jarvis.vaadin.ui.TextBox;
import org.jarvis.vaadin.util.JavaScriptUtil;

import java.util.List;

/**
 * Created by KimChheng on 7/10/2019.
 */
public class TabSupplier extends BaseTab<Supplier> {

    private EmailBox txtEmail;
    private TextBox txtAddress;
    private NumberField<Integer> txtPhone1;
    private NumberField<Integer> txtPhone2;

    @Override
    public void init() {
        super.init();
        txtNameEn.setPlaceholder("Tommy");
        txtNameKh.setPlaceholder(ContextUtil.getProperty("tommy"));
        txtEmail = new EmailBox("Email", "tony@gmail.com");
        txtAddress = ComponentFactory.createTextBox(getTranslation("address"), "St 293,No 34, Teklaor, SenSok, Phnom Penh");
        txtPhone1 = new NumberField<>(getTranslation("phone1"));
        txtPhone2 = new NumberField<>(getTranslation("phone2"));
        Grid.Column<Supplier> columnName = grid.addColumn(Supplier::getCompanyName).setHeader(getTranslation("name.en"));
        columnName.setSortProperty("nameEn").setWidth("70px");
        VaadinHelper.setResizableColumn(columnName);
        Grid.Column<Supplier> columnNameKh = grid.addColumn(Supplier::getCompanyNameKh).setHeader(getTranslation("name.kh"));
        columnNameKh.setSortProperty("nameKh").setWidth("70px");
        VaadinHelper.setResizableColumn(columnNameKh);
        Grid.Column<Supplier> columnEmail = grid.addColumn(Supplier::getEmail).setHeader("Email");
        Grid.Column<Supplier> columnAddress = grid.addColumn(Supplier::getAddress).setHeader(getTranslation("address"));
        columnNameKh.setSortProperty("address").setWidth("70px");
        VaadinHelper.setResizableColumn(columnAddress);
        Grid.Column<Supplier> columnPhone1 = grid.addColumn(Supplier::getPhone1).setHeader(getTranslation("phone1"));
        Grid.Column<Supplier> columnPhone2 = grid.addColumn(Supplier::getPhone2).setHeader(getTranslation("phone2"));
        addColumnAction();
        layout.removeRow(1);
        layout.add(0, txtEmail);
        layout.add(1, txtAddress, txtPhone1, txtPhone2);
        layout.add(2, btnSave, btnClear);
    }

    @Override
    protected void onGridEdit(Supplier supplier) {
        grid.getSelectionModel().select(supplier);
        bean = supplier;
        txtNameEn.setValue(supplier.getCompanyName());
        txtNameKh.setValue(supplier.getCompanyNameKh());
        txtEmail.setValue(supplier.getEmail());
        txtAddress.setValue(supplier.getAddress());
        txtPhone1.setValue(supplier.getPhone1());
        txtPhone2.setValue(supplier.getPhone2());
    }

    @Override
    protected void onGridDelete(Supplier supplier) {
        MessageDialogHelper.showConfirmDialog("Confirm delete",
                "Are you sure you want to delete \"" + supplier.getCompanyName() + "\"?",
                () -> {
                    supplier.setStatus(Status.INACTIVE);
                    repository.saveOrUpdate(supplier);
                    items.remove(supplier);
                    clear();
                    MessageDialogHelper.showNotification("Delete form success!");
                    JavaScriptUtil.execute("removeVaadinLicense()");
                }, () -> {
                    grid.getSelectionModel().deselectAll();
                    JavaScriptUtil.execute("removeVaadinLicense()");
                });
    }

    @Override
    protected void clear() {
        super.clear();
        txtEmail.setValue("");
        txtAddress.setValue("");
        txtPhone1.setNumber(0);
        txtPhone2.setNumber(0);
    }

    @Override
    protected void onClickSave(ClickEvent<Button> event) {
        if (bean == null || bean.getId() == null) {
            bean = new Supplier();
            items.add(bean);
        }
        bean.setCompanyName(txtNameEn.getValue());
        bean.setCompanyNameKh(txtNameKh.getValue());
        bean.setEmail(txtEmail.getValue());
        bean.setAddress(txtAddress.getValue());
        bean.setPhone1(txtPhone1.getValue());
        bean.setPhone2(txtPhone2.getValue());
        repository.saveOrUpdate(bean);
        clear();
        MessageDialogHelper.showNotification("Success!");
    }

    @Override
    protected List<Supplier> getListItems() {
        BaseCriteria<Supplier> criteria = new BaseCriteria<>(Supplier.class);
        criteria.condition(Restriction.equal("status", Status.ACTIVE));
        //criteria.desc("createdDate");
        criteria.orderByASC("id");
        return repository.list(criteria);
    }
}

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
import org.jarvis.sercurity.domain.entity.JarvisProfile;
import org.jarvis.sercurity.domain.entity.JarvisUser;
import org.jarvis.vaadin.factory.ComponentFactory;
import org.jarvis.vaadin.ui.*;
import org.jarvis.vaadin.util.JavaScriptUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;
import java.util.List;

/**
 * Created: kim chheng
 * Date: 13-Jul-2019 Sat
 * Time: 10:42 AM
 */
public class TabUser extends BaseTab<JarvisUser> {

    private TextBox txtUsername;
    private PasswordBox txtPassword;
    private EmailBox txtEmail;
    private NumberField<Integer> txtPhone;
    private DropDown<JarvisProfile> cbProfile;

    @Override
    public void init() {
        super.init();
        txtNameEn.setLabel(getTranslation("first.name"));
        txtNameEn.setPlaceholder("Steve");
        txtNameKh.setLabel(getTranslation("last.name"));
        txtNameKh.setPlaceholder("Roger");
        txtUsername = ComponentFactory.createTextBox(getTranslation("user.name"), "admin");
        txtPassword = new PasswordBox(getTranslation("password"));
        txtPassword.setAutoselect(true);
        txtPassword.setPlaceholder("PassW0rd!@2019");
        txtEmail = new EmailBox("Email", "tony@gmail.com");
        txtPhone = new NumberField<>(getTranslation("phone"));
        txtPhone.setNumber(0);
        cbProfile = new DropDown<>(getTranslation("profile"), JarvisProfile.class, this::listProfile);
        cbProfile.setRequired(true);

        Grid.Column<JarvisUser> columnFirstName = grid.addColumn(JarvisUser::getFirstName).setHeader(getTranslation("first.name"));
        columnFirstName.setSortProperty("firstName").setWidth("70px");
        VaadinHelper.setResizableColumn(columnFirstName);
        Grid.Column<JarvisUser> columnLastName = grid.addColumn(JarvisUser::getLastName).setHeader(getTranslation("last.name"));
        columnLastName.setSortProperty("lastName").setWidth("70px");
        VaadinHelper.setResizableColumn(columnLastName);
        Grid.Column<JarvisUser> columnUsername = grid.addColumn(JarvisUser::getUsername).setHeader(getTranslation("user.name"));
        columnUsername.setSortProperty("username").setWidth("70px");
        VaadinHelper.setResizableColumn(columnUsername);
        Grid.Column<JarvisUser> columnEmail = grid.addColumn(JarvisUser::getEmail).setHeader("Email");
        Grid.Column<JarvisUser> columnPhone1 = grid.addColumn(JarvisUser::getPhone1).setHeader(getTranslation("phone"));
        Grid.Column<JarvisUser> columnProfile = grid.addColumn(JarvisUser::getProfile).setHeader(getTranslation("profile"));
        addColumnAction();
        layout.removeRow(1);
        layout.add(0, txtUsername);
        layout.add(1, txtPassword, txtEmail, txtPhone);
        layout.add(2, cbProfile);
        layout.add(3, btnSave, btnClear);
    }

    private <T extends Serializable> List<T> listProfile(Class<T> clazz, DropDown<T> comboBox) {
        return repository.list(clazz);
    }

    @Override
    protected void onGridDelete(JarvisUser user) {
        MessageDialogHelper.showConfirmDialog("Confirm delete",
                "Are you sure you want to delete \"" + user.getUsername() + "\"?",
                () -> {
                    user.setStatus(Status.INACTIVE);
                    repository.saveOrUpdate(user);
                    items.remove(user);
                    clear();
                    MessageDialogHelper.showNotification("Delete user success!");
                    JavaScriptUtil.execute("removeVaadinLicense()");
                }, () -> {
                    grid.getSelectionModel().deselectAll();
                    JavaScriptUtil.execute("removeVaadinLicense()");
                });
    }

    @Override
    protected void onGridEdit(JarvisUser user) {
        grid.getSelectionModel().select(user);
        bean = user;
        txtNameEn.setValue(user.getFirstName());
        txtNameKh.setValue(user.getLastName());
        txtEmail.setValue(user.getEmail());
        txtUsername.setValue(user.getUsername());
        txtPhone.setValue(user.getPhone1());
        cbProfile.setValue(user.getProfile());
    }

    @Override
    protected List<JarvisUser> getListItems() {
        BaseCriteria<JarvisUser> criteria = new BaseCriteria<>(JarvisUser.class);
        criteria.condition(Restriction.equal("status", Status.ACTIVE));
        criteria.orderByASC("id");
        return repository.list(criteria);
    }

    @Override
    protected void onClickSave(ClickEvent<Button> event) {
        if (cbProfile.getValue() == null) {
            MessageDialogHelper.showMessageDialog("Please select profile");
            return;
        }
        if (bean == null || bean.getId() == null) {
            bean = new JarvisUser();
            items.add(bean);
        }
        if (bean.getPassword() == null || bean.getPassword().isEmpty()) {
            MessageDialogHelper.showMessageDialog("Please input password");
            return;
        }
        bean.setFirstName(txtNameEn.getValue());
        bean.setLastName(txtNameKh.getValue());
        bean.setEmail(txtEmail.getValue());
        bean.setUsername(txtUsername.getValue());
        if (txtPassword.getValue() != null && !txtPassword.isEmpty())
            bean.setPassword(ContextUtil.getBean(PasswordEncoder.class).encode(txtPassword.getValue()));
        bean.setPhone1(txtPhone.getValue());
        bean.getProfiles().clear();
        bean.addProfile(cbProfile.getValue());
        repository.executeUpdate("DELETE FROM ph_user_profile WHERE use_id = " + bean.getId());
        repository.saveOrUpdate(bean);
        clear();
        MessageDialogHelper.showNotification("Success!");
    }

    @Override
    protected void clear() {
        super.clear();
        txtEmail.setValue("");
        txtUsername.setValue("");
        txtPassword.setValue("");
        txtPhone.setNumber(0);
        cbProfile.setValue(null);
    }
}

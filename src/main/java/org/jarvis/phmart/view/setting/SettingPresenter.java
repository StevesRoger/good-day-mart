package org.jarvis.phmart.view.setting;

import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.jarvis.phmart.view.base.AnyPresenter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

/**
 * Created: kim chheng
 * Date: 18-Jun-2019 Tue
 * Time: 9:38 AM
 */
@SpringComponent
@SessionScope
public class SettingPresenter extends AnyPresenter<SettingView, SettingInteractor> {
}

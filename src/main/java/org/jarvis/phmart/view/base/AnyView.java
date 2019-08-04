package org.jarvis.phmart.view.base;

import com.vaadin.flow.i18n.LocaleChangeObserver;
import org.jarvis.phmart.view.main.MainView;

/**
 * Created: kim chheng
 * Date: 09-Mar-2019 Sat
 * Time: 2:46 PM
 */
public interface AnyView extends LocaleChangeObserver {
    void init();

    default void onMainViewNavigate(MainView main) {

    }
}

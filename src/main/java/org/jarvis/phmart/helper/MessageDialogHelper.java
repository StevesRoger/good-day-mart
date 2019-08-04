package org.jarvis.phmart.helper;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.dom.DomListenerRegistration;
import org.jarvis.common.function.Runner;

/**
 * Created by KimChheng on 6/27/2019.
 */
public final class MessageDialogHelper {

    public static void showConfirmDialog(String header, String text, Runner yes, Runner no) {
        ConfirmDialog dialog = new ConfirmDialog(header, text, "Yes", event -> yes.run(),
                "No", event -> no.run());
        dialog.setConfirmButtonTheme("error primary");
        dialog.setCancelButtonTheme("primary");
        dialog.open();
    }

    public static void showMessageI18NDialog(String message) {
        Dialog dialog = createDialog(null);
        dialog.add(new Label(dialog.getTranslation(message)));
        dialog.open();
    }

    public static void showMessageDialog(String message) {
        Dialog dialog = createDialog(null);
        dialog.add(new Label(message));
        dialog.open();
    }

    public static void showMessageI18NDialog(String message, Runner callback) {
        Dialog dialog = createDialog(callback);
        dialog.add(new Label(dialog.getTranslation(message)));
        dialog.open();
    }

    private static Dialog createDialog(Runner callback) {
        Dialog dialog = new Dialog();
        dialog.setWidth("250px");
        dialog.setHeight("20px");
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);
        dialog.getElement().getNode().runWhenAttached(ui -> {
            DomListenerRegistration keydown = ui.getElement().addEventListener("keydown", event -> dialog.close());
            dialog.addDetachListener(event -> {
                keydown.remove();
                if (callback != null)
                    callback.run();
            });
        });
        return dialog;
    }

    public static Notification showNotification(String message) {
        return showNotification(message, 3000);
    }

    public static Notification showNotification(String message, int duration) {
        return showNotification(message, duration, Notification.Position.BOTTOM_START);
    }

    public static Notification showNotification(String message, int duration, Notification.Position position) {
        Notification notification = new Notification(message, duration, position);
        notification.open();
        return notification;
    }
}

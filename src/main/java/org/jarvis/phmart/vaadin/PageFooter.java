package org.jarvis.phmart.vaadin;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import org.jarvis.phmart.view.product.ProductPresenter;
import org.jarvis.vaadin.ui.Ikon;
import org.jarvis.vaadin.ui.NumberField;
import org.jarvis.vaadin.ui.TextView;

/**
 * Created: kim chheng
 * Date: 15-May-2019 Wed
 * Time: 2:17 PM
 */
public class PageFooter extends Span {

    private TextView lblTotalPage;
    private NumberField<Integer> txtPage;
    private Ikon backward;
    private Ikon forward;
    private ProductPresenter presenter;
    private int totalPage;

    public PageFooter(String text, ProductPresenter presenter) {
        super(text);
        this.presenter = presenter;
        init();
    }

    private void init() {

        lblTotalPage = new TextView(" / 5");
        lblTotalPage.getStyle().set("font-weight", "500");
        lblTotalPage.getStyle().set("padding-left", "5px");

        txtPage = new NumberField<>(null, Integer.class);
        txtPage.setFormat("%d");
        txtPage.setNumber(1);
        txtPage.setAllowEmpty(false);
        txtPage.getStyle()
                .set("width", "50px")
                .set("font-size", "14px");
        txtPage.addValueChangeListener(this::pageChange);

        backward = new Ikon(VaadinIcon.ANGLE_LEFT, this::backward);
        backward.setClassName("ikon");
        forward = new Ikon(VaadinIcon.ANGLE_RIGHT, this::forward);
        forward.setClassName("ikon");

        add(backward, txtPage, lblTotalPage, forward);
    }

    public void setTotalPage(int page) {
        this.lblTotalPage.setText(new StringBuilder(" / ").append(page).toString());
        this.totalPage = page;
    }

    private void backward(ClickEvent<Icon> event) {
        int current = txtPage.getNumber();
        if (current > 1) {
            txtPage.setNumber(current - 1);
            presenter.getProductFilter().setPage(txtPage.getNumber()).refreshAll();
        }
    }

    private void forward(ClickEvent<Icon> event) {
        if (txtPage.getNumber() < totalPage) {
            txtPage.setNumber(txtPage.getNumber() + 1);
            presenter.getProductFilter().setPage(txtPage.getNumber()).refreshAll();
        }
    }

    private void pageChange(AbstractField.ComponentValueChangeEvent<TextField, String> event) {
        if (event.getSource().getValue() != null && !event.getSource().isEmpty()) {
            Integer input = Integer.valueOf(event.getSource().getValue());
            if (!presenter.getProductFilter().getPage().equals(input) && input > 0) {
                presenter.getProductFilter().setPage(input).refreshAll();
            }
        }
    }
}

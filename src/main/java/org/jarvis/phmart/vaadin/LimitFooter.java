package org.jarvis.phmart.vaadin;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import org.jarvis.phmart.view.product.ProductPresenter;
import org.jarvis.vaadin.ui.NoClearDropDown;
import org.jarvis.vaadin.ui.TextView;

import java.util.Arrays;

/**
 * Created: kim chheng
 * Date: 15-May-2019 Wed
 * Time: 1:52 PM
 */
public class LimitFooter extends Span {

    private TextView totalRecord;
    private NoClearDropDown<Integer> cbLimitRecord;
    private ProductPresenter presenter;
    private PageFooter pageFooter;

    public LimitFooter(ProductPresenter presenter, PageFooter pageFooter) {
        super();
        this.pageFooter = pageFooter;
        this.presenter = presenter;
        init();
    }

    private void init() {
        totalRecord = new TextView("Total:0");
        totalRecord.getStyle().set("font-weight", "bold");
        // .set("margin-left","100px");
        cbLimitRecord = new NoClearDropDown<>(null, Arrays.asList(5, 10, 20, 50));
        cbLimitRecord.setDefaultSelect("10");
        cbLimitRecord.getStyle().set("width", "75px");
        cbLimitRecord.getStyle().set("padding-left", "5px");
        cbLimitRecord.addValueChangeListener(this::valueChange);
        add(totalRecord, cbLimitRecord);
        this.totalRecord();
        this.pageFooter.setTotalPage(pages());
    }

    public void totalRecord() {
        int total = count();
        this.totalRecord.setText("Total:" + total);
    }

    public void setValue(Integer value) {
        this.cbLimitRecord.setValue(value);
    }

    public Integer getValue() {
        return this.cbLimitRecord.getValue();
    }

    public int pages() {
        int limit = getValue();
        return (count() + limit - 1) / limit;
    }

    private void valueChange(AbstractField.ComponentValueChangeEvent<ComboBox<Integer>, Integer> event) {
        int limit = getValue();
        pageFooter.setTotalPage(pages());
        presenter.getProductFilter().setLimit(limit).refreshAll();
    }

    private int count() {
        Object object = presenter.getRepository().getSingleResult("SELECT count(*) FROM ph_product WHERE status='active'");
        return object != null ? Integer.valueOf(object.toString()) : 0;
    }

}

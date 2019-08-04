package org.jarvis.phmart.vaadin;

import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.function.ValueProvider;
import org.jarvis.common.util.DateUtil;

import java.util.Date;

/**
 * Created: kim chheng
 * Date: 22-May-2019 Wed
 * Time: 10:04 PM
 */
public class DateRenderer<SOURCE> extends BasicRenderer<SOURCE, Date> {

    private String format;

    public DateRenderer(ValueProvider<SOURCE, Date> valueProvider) {
        this(valueProvider, "dd/MM/yyyy");
    }

    public DateRenderer(ValueProvider<SOURCE, Date> valueProvider, String format) {
        super(valueProvider);
        this.format = format;
    }

    @Override
    protected String getFormattedValue(Date date) {
        if (date == null)
            return "";
        return DateUtil.format(date, format);
    }
}

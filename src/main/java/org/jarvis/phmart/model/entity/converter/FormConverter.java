package org.jarvis.phmart.model.entity.converter;

import org.jarvis.orm.hibernate.domain.base.AnyConverter;
import org.jarvis.phmart.model.entity.reference.Form;

import javax.persistence.Converter;

/**
 * Created: kim chheng
 * Date: 13-Jan-2019 Sun
 * Time: 3:32 PM
 */
@Converter
public class FormConverter extends AnyConverter<Form, String> {

    @Override
    public Form convertToEntityAttribute(String value) {
        return value == null || value.isEmpty() ? null : new Form(value, value);
    }
}

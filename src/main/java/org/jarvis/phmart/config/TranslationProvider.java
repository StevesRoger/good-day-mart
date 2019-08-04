package org.jarvis.phmart.config;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.vaadin.flow.i18n.I18NProvider;
import org.jarvis.phmart.helper.IConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created: kim chheng
 * Date: 28-Apr-2019 Sun
 * Time: 9:45 AM
 */
@Component
@PropertySource(value = "classpath:/i18n/message_kh.properties", encoding = "UTF-8")
public class TranslationProvider implements I18NProvider {

    public static final String BUNDLE_PREFIX = "i18n.translate";

    private Logger log = LoggerFactory.getLogger(TranslationProvider.class);

    @Override
    public List<Locale> getProvidedLocales() {
        return Collections.unmodifiableList(Arrays.asList(IConstants.LOCALE_EN, IConstants.LOCALE_KH));
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        if (key == null) {
            log.warn("Got lang request for key with null value!");
            return "";
        }
        ResourceBundle bundle = bundleCache.getUnchecked(locale);
        String value;
        try {
            value = bundle.getString(key);
            value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
        } catch (MissingResourceException e) {
            log.warn("Missing resource", e);
            return "!" + locale.getLanguage() + ": " + key;
        } catch (UnsupportedEncodingException ex) {
            log.warn("Failed to parse encoding of value", ex);
            return "!" + locale.getLanguage() + ": " + key;
        }
        if (params.length > 0)
            value = MessageFormat.format(value, params);
        return value;
    }

    private final LoadingCache<Locale, ResourceBundle> bundleCache = CacheBuilder
            .newBuilder().expireAfterWrite(1, TimeUnit.DAYS)
            .build(new CacheLoader<Locale, ResourceBundle>() {
                @Override
                public ResourceBundle load(final Locale locale) throws Exception {
                    return readProperties(locale);
                }
            });


    protected ResourceBundle readProperties(final Locale locale) {
        try {
            return ResourceBundle.getBundle(BUNDLE_PREFIX, locale, TranslationProvider.class.getClassLoader());
        } catch (MissingResourceException ex) {
            log.warn("Missing resource", ex);
            return null;
        }
    }

}

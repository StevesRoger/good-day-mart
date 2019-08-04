package org.jarvis.phmart.helper;

import org.jarvis.core.util.ContextUtil;
import org.jarvis.orm.hibernate.repository.EntityRepository;
import org.jarvis.phmart.model.entity.ExchangeRate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created: kim chheng
 * Date: 14-May-2019 Tue
 * Time: 11:57 AM
 */
public final class MoneyHelper {

    public static float roundHalfUp(float amount) {
        BigDecimal decimal = new BigDecimal(amount);
        decimal.setScale(2, RoundingMode.HALF_UP);
        return decimal.floatValue();
    }

    public static long exchangeToKH(float amount) {
        if (amount < 0)
            return 0;
        BigDecimal decimal = new BigDecimal(amount * getRate());
        decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        return decimal.longValue();
    }

    public static float exchangeToUSD(long amount) {
        if (amount < 0)
            return 0;
        BigDecimal decimal = new BigDecimal(amount / getRate());
        decimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        return decimal.floatValue();
    }

    public static long exchangeToKHRoundUp(float amount) {
        if (amount < 0)
            return 0;
        return Math.round((amount * getRate()) / 100) * 100;
    }

    public static float exchangeToUSDRoundUp(long amount) {
        if (amount < 0)
            return 0;
        return Math.round(amount / getRate());
    }

    public static int getRate() {
        ExchangeRate exchangeRate = getExchangeRate();
        return exchangeRate == null ? 4000 : exchangeRate.getValue();
    }

    public static ExchangeRate getExchangeRate() {
        ExchangeRate exchangeRate = ContextUtil.getBean(EntityRepository.class).
                getEntityByProperties(ExchangeRate.class, new String[]{"fromCurrency", "toCurrency"},
                        new Serializable[]{"USD", "KHR"});
        return exchangeRate;
    }
}

package org.jarvis.phmart.exception;

/**
 * Created by KimChheng on 6/23/2019.
 */
public class SaleException extends Exception {

    private String key;

    public SaleException(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}

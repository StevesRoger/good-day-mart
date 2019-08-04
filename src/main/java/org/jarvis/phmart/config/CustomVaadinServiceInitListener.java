package org.jarvis.phmart.config;

import com.vaadin.flow.server.*;
import org.jarvis.phmart.helper.IConstants;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


/**
 * Created: kim chheng
 * Date: 19-May-2019 Sun
 * Time: 2:03 PM
 */
//@SpringComponent
public class CustomVaadinServiceInitListener implements VaadinServiceInitListener, SessionInitListener, BootstrapListener {

    /**
     * serial VUID
     */
    private static final long serialVersionUID = 7782078275956323697L;

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.addBootstrapListener(this);
        event.getSource().addSessionInitListener(this);
    }


    @Override
    public void sessionInit(SessionInitEvent event) throws ServiceException {
        event.getSession().setLocale(IConstants.LOCALE_KH);
    }

    @Override
    public void modifyBootstrapPage(BootstrapPageResponse response) {
        Document document = response.getDocument();
        Element head = document.head();
        Element meta = head.appendElement("meta");
        meta.attr("name", "keywords");
        meta.attr("content", "Flow, Spring, Polymer");
    }
}
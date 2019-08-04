package org.jarvis.phmart.view.report;

import com.vaadin.flow.spring.annotation.SpringComponent;
import org.jarvis.phmart.view.base.AnyPresenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.annotation.SessionScope;

@SpringComponent
@SessionScope
public class ReportPresenter extends AnyPresenter<ReportView, ReportInteractor> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportPresenter.class);

}

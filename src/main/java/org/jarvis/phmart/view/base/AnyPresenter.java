package org.jarvis.phmart.view.base;

import org.jarvis.core.util.ContextUtil;
import org.jarvis.orm.hibernate.repository.EntityRepository;
import org.jarvis.vaadin.ui.DropDown;

import java.io.Serializable;
import java.util.List;

/**
 * Created: kim chheng
 * Date: 09-Mar-2019 Sat
 * Time: 2:45 PM
 */
public abstract class AnyPresenter<V extends AnyView, I> {

    protected V view;

    protected I interactor;

    protected EntityRepository repository;

    public AnyPresenter() {
        repository = ContextUtil.getBean(EntityRepository.class);
    }

    public AnyPresenter(I interactor) {
        this();
        this.interactor = interactor;
    }

    public void onAttach(V view) {
        this.view = view;
    }

    public void onDetach() {
        this.view = null;
    }

    public EntityRepository getRepository() {
        return repository;
    }

    public V getView() {
        return view;
    }

    public <I extends Serializable> List<I> listDropDownItem(Class<I> clazz, DropDown<I> dropDown) {
        return repository.list(clazz);
    }
}

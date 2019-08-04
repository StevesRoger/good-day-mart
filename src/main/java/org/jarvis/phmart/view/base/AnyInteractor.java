package org.jarvis.phmart.view.base;

import org.jarvis.core.util.ContextUtil;
import org.jarvis.orm.hibernate.repository.EntityRepository;

/**
 * Created: kim chheng
 * Date: 17-Mar-2019 Sun
 * Time: 4:01 PM
 */
public abstract class AnyInteractor<T extends AnyPresenter> {

    protected final EntityRepository repository = ContextUtil.getBean(EntityRepository.class);

    protected T presenter;

    public AnyInteractor() {
    }

    public AnyInteractor(T presenter) {
        this.presenter = presenter;
    }

    public T getPresenter() {
        return presenter;
    }

    public void setPresenter(T presenter) {
        this.presenter = presenter;
    }

    public EntityRepository getRepository() {
        return repository;
    }
}

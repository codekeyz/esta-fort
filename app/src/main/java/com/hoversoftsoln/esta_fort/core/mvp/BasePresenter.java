package com.hoversoftsoln.esta_fort.core.mvp;

public abstract class BasePresenter<V extends BaseView> {

    private V view;

    public V getView() {
        return view;
    }

    public void setView(V view) {
        this.view = view;
    }

    public boolean isViewAttached() {
        return this.view != null;
    }

}

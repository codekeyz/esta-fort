package com.hoversoftsoln.esta_fort.core.mvp

abstract class BasePresenter<V : BaseView> {

    var view: V? = null

    val isViewAttached: Boolean
        get() = this.view != null

}

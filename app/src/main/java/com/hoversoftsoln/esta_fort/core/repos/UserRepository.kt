package com.hoversoftsoln.esta_fort.core.repos

import com.hoversoftsoln.esta_fort.core.data.User
import com.hoversoftsoln.esta_fort.core.mvp.BasePresenter
import com.hoversoftsoln.esta_fort.core.mvp.BaseView

internal interface UserContracts {
    fun getMyAccount()
}

class UserRepository : BasePresenter<UserRepository.View>(), UserContracts {

    override fun getMyAccount() {

    }

    interface View : BaseView
}

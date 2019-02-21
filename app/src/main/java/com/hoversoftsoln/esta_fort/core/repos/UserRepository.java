package com.hoversoftsoln.esta_fort.core.repos;

import com.hoversoftsoln.esta_fort.core.mvp.BasePresenter;
import com.hoversoftsoln.esta_fort.core.mvp.BaseView;

interface UserContracts {
    void getMyAccount();
}

public class UserRepository extends BasePresenter<UserRepository.View> implements UserContracts {

    public UserRepository() {
    }

    @Override
    public void getMyAccount() {

    }

    public interface View extends BaseView {

    }
}

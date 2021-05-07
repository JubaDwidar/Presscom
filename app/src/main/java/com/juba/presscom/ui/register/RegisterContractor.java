package com.juba.presscom.ui.register;

import io.reactivex.Completable;

public interface RegisterContractor {

    interface RegisterView{}
      Completable signUpUser(String email, String password);
}

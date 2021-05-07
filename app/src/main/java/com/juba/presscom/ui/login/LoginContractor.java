package com.juba.presscom.ui.login;

import io.reactivex.Completable;

public interface LoginContractor {

      Completable SignInUser(String email, String Password);

      interface LoginView{}
}

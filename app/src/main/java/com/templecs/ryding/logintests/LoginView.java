package com.templecs.ryding.logintests;

/**
 * Created by rafaellima on 12/8/15.
 */
public interface LoginView {
    String getPinNumber();

    void showPinNumberError(int resId);

    void startDriverActivity();

    void showLoginError(int resId);
}
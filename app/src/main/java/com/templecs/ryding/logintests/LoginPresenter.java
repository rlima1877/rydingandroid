package com.templecs.ryding.logintests;

import com.templecs.ryding.R;

/**
 * Created by rafaellima on 12/8/15.
 */
public class LoginPresenter {
    private LoginView view;
    private LoginService service;

    public LoginPresenter(LoginView view, LoginService service) {
        this.view = view;
        this.service = service;
    }

    public void onLoginClicked() {


        String pin = view.getPinNumber();

        if (pin.isEmpty()) {
            view.showPinNumberError(R.string.prompt_pin_empty);
            return;
        }
        else if (!isDriverIDValid(pin)) {
            view.showPinNumberError(R.string.number_required);
            return;
        }

        boolean loginSucceeded = service.login(pin);
        if (loginSucceeded) {
            view.startDriverActivity();
            return;
        }
        view.showLoginError(R.string.login_failed);
    }

    private boolean isDriverIDValid(String driverID) {
        //add your own logic
        String regex = "[0-9]+";

        return driverID.matches(regex);

    }
}
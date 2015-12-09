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


    private boolean isDriverIDValid(String driverID) {
        //add your own logic
        String regex = "[0-9]+";
        return driverID.matches(regex);

    }

    public boolean checkPinFieldIsEmpty(LoginView view){

        String pin = view.getPinNumber();

        if (pin.isEmpty()) {
            view.showLoginError(R.string.prompt_pin_empty);
            return true;
        }

        return false;
    }

    public boolean checkIfPinFieldContainsAnIntegerNumber(LoginView view){

        String pin = view.getPinNumber();

        if(!isDriverIDValid(pin)) {
            view.showLoginError(R.string.number_required);
            return false;
        }

        return true;
    }
    public boolean checkIfPinIsCorrect(LoginView view){
        String pin = view.getPinNumber();


        boolean loginSucceeded = service.login(pin);
        if (loginSucceeded) {
            return true;
        }
        else {
            view.showLoginError(R.string.invalid_pin);
            return false;
        }
    }
    public boolean checkIfPinIsCorrect(String pin){

        boolean loginSucceeded = service.login(pin);
        if (loginSucceeded) {
            return true;
        }else{
            view.showLoginError(R.string.invalid_pin);
            return false;
        }
    }

}
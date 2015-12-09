package com.templecs.ryding.logintests;

/**
 * Created by rafaellima on 12/8/15.
 *
 * This class mocks a pin verification service class.
 */
public class LoginService {

    public boolean login(String pin) {
        return "1234".equals(pin);
    }
}

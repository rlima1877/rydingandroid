package com.templecs.ryding.logintests;

import com.templecs.ryding.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by rafaellima on 12/8/15.
 */

@RunWith(MockitoJUnitRunner.class)
public class LoginPresenterTest {
    @Mock
    private LoginView view;
    @Mock
    private LoginService service;
    private LoginPresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new LoginPresenter(view, service);
    }

    @Test
    public void shouldShowErrorMessageWhenPinNumberIsEmpty() throws Exception {
        when(view.getPinNumber()).thenReturn("");
        presenter.onLoginClicked();

        verify(view).showPinNumberError(R.string.prompt_pin_empty);
    }

    @Test
    public void shouldShowErrorMessageWhenPinNumberIsNotAnInteger() throws Exception {
        when(view.getPinNumber()).thenReturn("this is not a number");
        presenter.onLoginClicked();

        verify(view).showPinNumberError(R.string.number_required);
    }


    @Test
    public void shouldStartMainActivityWhenPinNumberIsCorrect() throws Exception {
        when(view.getPinNumber()).thenReturn("1234");
        when(service.login("1234")).thenReturn(true);
        presenter.onLoginClicked();

        verify(view).startDriverActivity();
    }

}
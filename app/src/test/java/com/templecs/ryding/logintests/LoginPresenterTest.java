package com.templecs.ryding.logintests;

import com.templecs.ryding.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.templecs.ryding.activities.LoginActivity;

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
        presenter.checkPinFieldIsEmpty(view);
        verify(view).showLoginError(R.string.prompt_pin_empty);
    }

    @Test
    public void shouldShowErrorMessageWhenPinNumberIsAString() throws Exception {
        when(view.getPinNumber()).thenReturn("this is not a number");
        presenter.checkIfPinFieldContainsAnIntegerNumber(view);
        verify(view).showLoginError(R.string.number_required);
    }

    @Test
    public void shouldShowErrorMessageWhenPinNumberIsADouble() throws Exception {
        when(view.getPinNumber()).thenReturn("9.9");
        presenter.checkIfPinFieldContainsAnIntegerNumber(view);
        verify(view).showLoginError(R.string.number_required);
    }

    @Test
    public void shouldShowErrorMessageWhenPinNumberIsIncorrect() throws Exception {
        when(view.getPinNumber()).thenReturn("1654");
        presenter.checkIfPinIsCorrect(view);
        verify(view).showLoginError(R.string.invalid_pin);
    }
}
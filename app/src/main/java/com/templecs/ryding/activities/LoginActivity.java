package com.templecs.ryding.activities;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.templecs.ryding.R;
import com.templecs.ryding.model.Bus;
import com.templecs.ryding.logintests.LoginPresenter;
import com.templecs.ryding.logintests.LoginService;
import com.templecs.ryding.logintests.LoginView;

import java.util.ArrayList;
import java.util.List;


/**
 * Android login screen Activity
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, LoginView{

    private static final String DUMMY_PIN = "1234";

    private UserLoginTask userLoginTask = null;
    private View loginFormView;
    private View progressView;
    private AutoCompleteTextView driverPinTextView;
    private TextView signUpTextView;
    public ArrayList<Bus> busList;
    LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        presenter = new LoginPresenter(this, new LoginService());


        Bundle bundle = getIntent().getBundleExtra("BusList");
        if(bundle != null){
            busList = bundle.getParcelableArrayList("BusList");
        }

        if(savedInstanceState != null) {
            busList = savedInstanceState.getParcelableArrayList("bus_data");
        }

        driverPinTextView = (AutoCompleteTextView) findViewById(R.id.email);
        loadAutoComplete();


        Button loginButton = (Button) findViewById(R.id.email_sign_in_button);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                initLogin();
                presenter.onLoginClicked();


            }
        });

        loginFormView = findViewById(R.id.login_form);
        progressView = findViewById(R.id.login_progress);

        //adding underline and link to signup textview
        signUpTextView = (TextView) findViewById(R.id.signUpTextView);
        signUpTextView.setPaintFlags(signUpTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Linkify.addLinks(signUpTextView, Linkify.ALL);

        signUpTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("LoginActivity", "Sign Up Activity activated.");
                // this is where you should start the signup Activity
                // LoginActivity.this.startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    private void loadAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Validate Login form and authenticate.
     */
    public void initLogin() {
        if (userLoginTask != null) {
            return;
        }

        driverPinTextView.setError(null);

        String driverPin = driverPinTextView.getText().toString();

        Log.d("DRIVERPIN","Driver pin: " + driverPin);

        boolean cancelLogin = false;
        View focusView = null;


        if (TextUtils.isEmpty(driverPin)) {
            driverPinTextView.setError(getString(R.string.field_required));
            focusView = driverPinTextView;
            cancelLogin = true;
        } else if (!isDriverIDValid(driverPin)) {
            driverPinTextView.setError(getString(R.string.invalid_driver_pin));
            focusView = driverPinTextView;
            cancelLogin = true;
        }
        else if(!driverPin.equals(DUMMY_PIN)){
            driverPinTextView.setError(getString(R.string.invalid_driver_pin));
            focusView = driverPinTextView;
            cancelLogin = true;
        }

        if (cancelLogin) {
            // error in login
            focusView.requestFocus();
        } else {
            // show progress spinner, and start background task to login
            showProgress(true);
            userLoginTask = new UserLoginTask(driverPin,getApplicationContext());
            userLoginTask.execute((Void) null);
        }
    }

    private boolean isDriverIDValid(String driverID) {
        //add your own logic
        String regex = "[0-9]+";

        return driverID.matches(regex);

    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            loginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> driverIDs = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            driverIDs.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Async Login Task to authenticate
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String driverPin;
        Context context;

        UserLoginTask(String driverPin, Context context) {
            this.driverPin = driverPin;
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //this is where you should write your authentication code
            // or call external service
            // following try-catch just simulates network access
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            //using a local dummy credentials store to authenticate
            String dummyID = DUMMY_PIN;
            if (dummyID.equals(driverPin)) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            userLoginTask = null;
            //stop the progress spinner
            showProgress(false);

            if (success) {
                //clear textfield before moving on
                driverPinTextView.getText().clear();

                //  login success and move to driver activity.
                startActivity(new Intent(context, DriverActivity.class));

            } else {
                // login failure
                driverPinTextView.setError(getString(R.string.invalid_driver_pin));
                driverPinTextView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            userLoginTask = null;
            showProgress(false);
        }
    }


    @Override
    public String getPinNumber() {
        return driverPinTextView.getText().toString();
    }

    @Override
    public void showPinNumberError(int resId) {
        driverPinTextView.setError(getString(resId));
    }

    @Override
    public void startDriverActivity() {
        startActivity(new Intent(this, DriverActivity.class));
    }

    @Override
    public void showLoginError(int resId) {

    }



}
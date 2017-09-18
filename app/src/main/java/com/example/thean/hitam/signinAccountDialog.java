package com.example.thean.hitam;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by thean on 17/09/2017.
 */

public class signinAccountDialog extends DialogPreference implements DialogInterface.OnClickListener {

    EditText email, password;
    AlertDialog alertDialog;

    public signinAccountDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPersistent(false);
        setDialogLayoutResource(R.layout.activity_login);
        setPositiveButtonText("login");
    }

    @Override
    public void onBindDialogView(View view) {
        email = (EditText)view.findViewById(R.id.auth_email);
        password = (EditText)view.findViewById(R.id.auth_password);


        super.onBindDialogView(view);
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);

        alertDialog = (AlertDialog) getDialog();
        alertDialog.setCanceledOnTouchOutside(false);
        Button affirmative = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        affirmative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passText = String.valueOf(password.getText());
                String emailText = String.valueOf(email.getText());

                if(isValid(emailText, passText)) {
                    authParameters parameters = new authParameters(emailText, passText);
                    authenticTasks signInTime = new authenticTasks();
                    signInTime.execute(parameters);
                }
            }
        });
    }

    public boolean isValid(String emailText, String passText) {
        boolean valid = true;


        if(TextUtils.isEmpty(emailText) || !Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.setError((CharSequence) "Invalid email");
            valid = false;
        }

        if(passText.length() < 6) {
            password.setError((CharSequence) "Must be 6 characters");
            valid = false;
        }

        return valid;
    }

    private static class authParameters {
        String email;
        String password;

        authParameters(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    private class authenticTasks extends AsyncTask<authParameters, Void, String> {

        @Override
        protected String doInBackground(authParameters... params) {
            Preferences.hitamFB.authenticSignIn(params[0].email, params[0].password);
            return "finsihed";
        }

        @Override
        protected void onPostExecute(String result) {
            alertDialog.dismiss();
        }
    }
}


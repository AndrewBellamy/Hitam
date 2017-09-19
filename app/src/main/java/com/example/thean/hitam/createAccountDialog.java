package com.example.thean.hitam;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.app.AlertDialog;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Andrew Bellamy on 16/09/2017.
 * For Hitam | Assignment 2 SIT207
 * Student ID: 215240036
 */

public class createAccountDialog extends DialogPreference implements DialogInterface.OnClickListener {

    //Controls
    EditText email, password;
    AlertDialog alertDialog;

    /**
     * Initialises the dialog preference
     * @param context
     * @param attrs
     */
    public createAccountDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPersistent(false);
        setDialogLayoutResource(R.layout.activity_login);
        setPositiveButtonText("create");
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
                    //Call Firebase interface to create account
                    Home.hitamFB.authenticCreateUser(emailText, passText);
                    alertDialog.dismiss();
                }
            }
        });
    }

    /**
     * Handles validation for email and password, setting error UI value states when conditions are
     * not met.
     * @param emailText
     * @param passText
     * @return
     */
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
}

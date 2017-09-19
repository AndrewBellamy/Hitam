package com.example.thean.hitam;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Andrew Bellamy on 15/09/2017.
 * For Hitam | Assignment 2 SIT207
 * Student ID: 215240036
 */

public class FBControl {

    Context firebaseContext;
    //Firebase interfaces
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    /**
     * Initialises the interface. Called in the topmost activity.
     * @param context
     */
    public FBControl(Context context) {
        firebaseContext = context;
        mAuth = FirebaseAuth.getInstance();
    }

    //Authentication services

    /**
     * Initialises the Authentication listener.
     */
    public void authenticService() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Toast.makeText(firebaseContext, R.string.auth_login_success, Toast.LENGTH_SHORT).show();
                } else {
                    // User is signed out
                    Toast.makeText(firebaseContext, R.string.auth_logout_success, Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    /**
     * Starts the Authentication listener
     */
    public void authenticServiceStart() {
        mAuth.addAuthStateListener(mAuthListener);
    }

    /**
     * Stops the Authentication listener
     */
    public void authenticServiceStop() {
        mAuth.removeAuthStateListener(mAuthListener);
    }

    /**
     * Convenience for checking if user is currently set. Required for Firebase DB functions.
     * @return
     */
    public boolean authenticUserSet() {
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Creates a Firebase Hitam Account, using an Email and Password. Errors are logged in the Firebase
     * crash report log.
     * @param email
     * @param password
     */
    public void authenticCreateUser(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener((Activity) firebaseContext, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(firebaseContext, task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                        FirebaseCrash.log(task.getException().getMessage());
                    } else {
                        Toast.makeText(firebaseContext, R.string.auth_create_success, Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }

    /**
     * Sign in method for Firebase Authentication. Errors are logged in the Firebase crash report log.
     * Cross-Activity method calls are used to Handle preference async UI behaviour.
     * @param email
     * @param password
     */
    public void authenticSignIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
           .addOnCompleteListener((Activity) firebaseContext, new OnCompleteListener<AuthResult>() {
                @Override
               public void onComplete(@NonNull Task<AuthResult> task) {

                    if (!task.isSuccessful()) {
                        Toast.makeText(firebaseContext, task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                        /*Log error for query*/
                        FirebaseCrash.log(task.getException().getMessage());
                    } else {
                        Toast.makeText(firebaseContext, R.string.auth_login_success,
                                Toast.LENGTH_SHORT).show();
                    }
                    signinAccountDialog.progress.dismiss();
                    Preferences.PreferencesFragment.setAuthPreferences(Preferences.PreferencesFragment.signInPref, Preferences.PreferencesFragment.signOutPref);
               }
           });
    }

    /**
     * Handler for Signing out of Firebase
     */
    public void authenticSignOut() {
        mAuth.signOut();
    }

    //Firebase noSQL database

    /**
     * Writes entries in te Firebase DB, using the user's UID as reference.
     * @param balance
     * @param date
     */
    public void writeNewEntry(Double balance, Long date) {
        try {
            database.getReference().child(mAuth.getCurrentUser().getUid()).child(String.valueOf(date)).setValue(balance);
            Toast.makeText(firebaseContext, R.string.firebase_write_success,
                    Toast.LENGTH_SHORT).show();
        } catch(Exception e) {
            FirebaseCrash.report(e);
        }


    }
}

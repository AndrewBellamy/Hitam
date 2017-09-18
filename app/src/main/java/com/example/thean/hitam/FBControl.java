package com.example.thean.hitam;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by thean on 15/09/2017.
 */

public class FBControl {

    Context firebaseContext;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static final String TAG = "firebase auth";

    public FBControl(Context context) {
        firebaseContext = context;
        mAuth = FirebaseAuth.getInstance();
    }

    //Authentication services
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

    public void authenticServiceStart() {
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void authenticServiceStop() {
        mAuth.removeAuthStateListener(mAuthListener);
    }

    public boolean authenticUserSet() {
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null) {
            return true;
        } else {
            return false;
        }
    }

    public void authenticCreateUser(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener((Activity) firebaseContext, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(firebaseContext, R.string.auth_error,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(firebaseContext, R.string.auth_create_success, Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }

    public void authenticSignIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
           .addOnCompleteListener((Activity) firebaseContext, new OnCompleteListener<AuthResult>() {
                @Override
               public void onComplete(@NonNull Task<AuthResult> task) {

                    if (!task.isSuccessful()) {
                        Toast.makeText(firebaseContext, R.string.auth_error,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(firebaseContext, R.string.auth_login_success,
                                Toast.LENGTH_SHORT).show();
                    }

               }
           });
    }

    public void authenticSignOut() {
        mAuth.signOut();
    }

    //Firebase noSQL database

    public void writeNewEntry(Double balance, Long date) {

        database.getReference().child(mAuth.getCurrentUser().getUid()).child(String.valueOf(date)).setValue(balance);
        Toast.makeText(firebaseContext, R.string.firebase_write_success,
                Toast.LENGTH_SHORT).show();
    }
}

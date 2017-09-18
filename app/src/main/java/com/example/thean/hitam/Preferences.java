package com.example.thean.hitam;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by thean on 15/09/2017.
 */

public class Preferences extends PreferenceActivity {

    //constants
    public static final String SIGN_IN_KEY = "auth_login_preference";
    public static final String SIGN_OUT_KEY = "auth_sign_out";
    public static final String CREATE_ACC_KEY = "auth_create_preference";

    static FBControl hitamFB;
    Context preferenceContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferencesFragment()).commit();
        hitamFB = new FBControl(this);
        hitamFB.authenticService();
        preferenceContext = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        hitamFB.authenticServiceStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hitamFB.authenticServiceStop();
    }

    public static class PreferencesFragment extends PreferenceFragment {

        DialogPreference signInPref;
        Preference signOutPref;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            PreferenceManager manager = getPreferenceManager();
            manager.setSharedPreferencesName("HitamPreferences");
            manager.setSharedPreferencesMode(MODE_PRIVATE);

            addPreferencesFromResource(R.xml.preferences);

            signOutPref = (Preference) findPreference(SIGN_OUT_KEY);
            signOutPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                @Override
                public boolean onPreferenceClick(Preference p) {
                    Preferences.hitamFB.authenticSignOut();
                    setAuthPreferences();
                    return false;
                }
            });
            signInPref = (DialogPreference) findPreference(SIGN_IN_KEY);

            setAuthPreferences();
        }

        @Override
        public void onResume() {
            super.onResume();
            setAuthPreferences();
        }

        public void setAuthPreferences() {
            signOutPref.setEnabled(Preferences.hitamFB.authenticUserSet());
            signInPref.setEnabled(!Preferences.hitamFB.authenticUserSet());
        }

    }
}

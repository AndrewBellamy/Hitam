package com.example.thean.hitam;

import android.content.Context;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * Created by Andrew Bellamy on 15/09/2017.
 * For Hitam | Assignment 2 SIT207
 * Student ID: 215240036
 */

public class Preferences extends PreferenceActivity {

    //constants
    public static final String SIGN_IN_KEY = "auth_login_preference";
    public static final String SIGN_OUT_KEY = "auth_sign_out";

    //Context
    Context preferenceContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferencesFragment()).commit();
        Home.hitamFB.authenticService();
        preferenceContext = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Home.hitamFB.authenticServiceStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Home.hitamFB.authenticServiceStop();
    }

    /**
     * Preference fragment, used to handle behaviour of preference UI
     */
    public static class PreferencesFragment extends PreferenceFragment {

        static DialogPreference signInPref;
        static Preference signOutPref;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            PreferenceManager manager = getPreferenceManager();
            manager.setSharedPreferencesName("HitamPreferences");
            manager.setSharedPreferencesMode(MODE_PRIVATE);

            addPreferencesFromResource(R.xml.preferences);
            signInPref = (DialogPreference) findPreference(SIGN_IN_KEY);
            signOutPref = (Preference) findPreference(SIGN_OUT_KEY);
            signOutPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                @Override
                public boolean onPreferenceClick(Preference p) {
                    Home.hitamFB.authenticSignOut();
                    setAuthPreferences(signInPref, signOutPref);
                    return false;
                }
            });


            setAuthPreferences(signInPref, signOutPref);
        }

        @Override
        public void onResume() {
            super.onResume();
            setAuthPreferences(signInPref, signOutPref);
        }

        /**
         * Sets control enabled property, based on user check in Firebase interface.
         * @param a
         * @param b
         */
        public static void setAuthPreferences(DialogPreference a, Preference b) {
            b.setEnabled(Home.hitamFB.authenticUserSet());
            a.setEnabled(!Home.hitamFB.authenticUserSet());
        }

    }
}

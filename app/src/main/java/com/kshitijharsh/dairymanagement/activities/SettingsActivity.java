package com.kshitijharsh.dairymanagement.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.kshitijharsh.dairymanagement.R;
import com.kshitijharsh.dairymanagement.utils.AppCompatPreferenceActivity;

public class SettingsActivity extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // load settings fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
    }

    public static class MainPreferenceFragment extends PreferenceFragment {

        CheckBoxPreference calculatePref, facilityPref;
        public static String CALCULATE_PREF = "prefs";

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_main);

            SharedPreferences pref = getActivity().getSharedPreferences("SNFPref", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = pref.edit();

            facilityPref = (CheckBoxPreference) findPreference("snf_facility");
            calculatePref = (CheckBoxPreference) findPreference("snf_calculate");

            facilityPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if (newValue.toString().equals("false")) {
                        editor.putString(CALCULATE_PREF, "none");
                        editor.apply();
//                        Toast.makeText(getActivity(), "Restarted your app to see the changes.", Toast.LENGTH_LONG).show();
                    }
                    if (newValue.toString().equals("true")) {
                        if (calculatePref.isChecked()) {
                            editor.putString(CALCULATE_PREF, "true");
                            editor.apply();
                        } else {
                            editor.putString(CALCULATE_PREF, "false");
                            editor.apply();
//                            Toast.makeText(getActivity(), "Restarted your app to apply the changes.", Toast.LENGTH_LONG).show();
                        }
                    }
//                    Intent i = getActivity().getPackageManager()
//                            .getLaunchIntentForPackage(getActivity().getPackageName());
//                    assert i != null;
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(i);
                    return true;
                }
            });

            calculatePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    if (facilityPref.isChecked()) {
                        if (o.toString().equals("true")) {
                            editor.putString(CALCULATE_PREF, "true");
                            editor.apply();
//                            Toast.makeText(getActivity(), "Restarted your app to apply the changes.", Toast.LENGTH_LONG).show();
                        }
                        if (o.toString().equals("false")) {
                            editor.putString(CALCULATE_PREF, "false");
                            editor.apply();
//                            Toast.makeText(getActivity(), "Restarted your app to apply the changes.", Toast.LENGTH_LONG).show();
                        }
//                        Intent i = getActivity().getPackageManager()
//                                .getLaunchIntentForPackage(getActivity().getPackageName());
//                        assert i != null;
//                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(i);
                    }
                    return true;
                }
            });
        }

        @Override
        public void onStop() {
            super.onStop();
//            facilityPref.setChecked(false);
//            calculatePref.setChecked(false);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }
}

package com.kshitijharsh.dairymanagement;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.MenuItem;
import android.widget.Toast;

public class SettingsActivity extends AppCompatPreferenceActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // load settings fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
    }

    public static class MainPreferenceFragment extends PreferenceFragment {

        CheckBoxPreference calculatePref, facilityPref;
        public static String CALCULATE_PREF = "none";

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_main);

            facilityPref = (CheckBoxPreference) findPreference("snf_facility");
            calculatePref = (CheckBoxPreference) findPreference("snf_calculate");

            facilityPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference preference, Object newValue) {
//                    Toast.makeText(getActivity(), "Pref " + preference.getKey() + " changed to " + newValue.toString(), Toast.LENGTH_SHORT).show();
                    if (newValue.toString().equals("false")) {
                        CALCULATE_PREF = "none";
                        Toast.makeText(getActivity(), "Please restart your app to see the changes.", Toast.LENGTH_LONG).show();
                    }
                    if (newValue.toString().equals("true")) {
                        if (calculatePref.isChecked())
                            CALCULATE_PREF = "true";
                        else
                            CALCULATE_PREF = "false";
                        Toast.makeText(getActivity(), "Please restart your app to see the changes.", Toast.LENGTH_LONG).show();
                    }
                    Intent i = getActivity().getPackageManager()
                            .getLaunchIntentForPackage(getActivity().getPackageName());
                    assert i != null;
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    return true;
                }
            });

            calculatePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
//                    Toast.makeText(getActivity(), "Pref " + preference.getKey() + " changed to " + o.toString(), Toast.LENGTH_SHORT).show();
                    if (facilityPref.isChecked()) {
                        if (o.toString().equals("true")) {
                            CALCULATE_PREF = "true";
                            Toast.makeText(getActivity(), "Please restart your app to see the changes.", Toast.LENGTH_LONG).show();
                        }
                        if (o.toString().equals("false")) {
                            CALCULATE_PREF = "false";
                            Toast.makeText(getActivity(), "Please restart your app to see the changes.", Toast.LENGTH_LONG).show();
                        }
                        Intent i = getActivity().getPackageManager()
                                .getLaunchIntentForPackage(getActivity().getPackageName());
                        assert i != null;
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}

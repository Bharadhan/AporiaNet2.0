package christos.voutselas.aporianet;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;


public class SettingsActivity extends AppCompatActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class SchoolFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //addPreferencesFromResource(R.xml.settings_main);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);
                System.out.print(stringValue);
            }
            return true;
        }
    }
}

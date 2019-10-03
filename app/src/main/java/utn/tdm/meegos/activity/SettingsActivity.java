package utn.tdm.meegos.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import utn.tdm.meegos.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            switch(bundle.getInt("fragment")) {
                case 1:
                    getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings, new SettingsContactFragment())
                        .commit();
                    break;
                case 2:
                    getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.settings, new SettingsHistoryFragment())
                        .commit();

            }
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsContactFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.contact_preferences, rootKey);
        }
    }

    public static class SettingsHistoryFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.history_contact_preferences, rootKey);
        }
    }
}
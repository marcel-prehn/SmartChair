package uni.marcel.smartchair;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class Settings extends Activity {

    private SharedPreferences preferences;
    private Spinner spinner;
    private Button btClose;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        index = preferences.getInt("timerIntervalIndex", 1);

        Log.i("timer", "loaded index " + index);

        spinner = (Spinner) findViewById(R.id.spinner);
        btClose = (Button) findViewById(R.id.btSettingsSave);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, (R.array.settingsTimerInterval), R.layout.settings_interval_item);
        adapter.setDropDownViewResource(R.layout.settings_interval_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(index);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                index = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("timerIntervalIndex", index);
                editor.commit();
                Log.i("timer", "saved index " + index);
                finish();
            }
        });
    }
}

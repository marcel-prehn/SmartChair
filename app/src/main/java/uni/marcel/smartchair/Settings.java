package uni.marcel.smartchair;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Settings extends Activity {

    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.settingsTimerInterval, R.layout.settings_interval_item);
        adapter.setDropDownViewResource(R.layout.settings_interval_item);
        spinner.setAdapter(adapter);
    }
}

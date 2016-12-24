package uni.marcel.smartchair;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;

public class SelectDevice extends Activity {

    ListView lvDevices;
    ArrayList<String> list;
    BtAdapter btadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_device);

        lvDevices = (ListView) findViewById(R.id.listSelectDevice);
        btadapter = new BtAdapter();

        if (btadapter.getBlueAdapter() != null) {
            if (!btadapter.getBlueAdapter().isEnabled()) {
                Intent intentBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(intentBluetooth);
            }

            list = new ArrayList<>();
            final Set<BluetoothDevice> devices = btadapter.getBondedDevices();

            if (devices != null) {
                for (BluetoothDevice b : devices) {
                    list.add(b.getName());
                    Log.d("info", "bonded device: " + b.getName());
                }
            }
            lvDevices.setAdapter(new ArrayAdapter<>(this, R.layout.select_device_item, list));
            lvDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intentOverview = new Intent(SelectDevice.this, Overview.class);
                    intentOverview.putExtra("deviceName", list.get(i));
                    startActivity(intentOverview);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_fragment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuTutorial:
                startActivity(new Intent(SelectDevice.this, Tutorial.class));
                return true;
            case R.id.menuReconnect:
                return true;
            case R.id.menuAbout:
                startActivity(new Intent(SelectDevice.this, About.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            boolean isFirstStart = pref.getBoolean(getString(R.string.firstStart), false);
            if(!isFirstStart) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean(getString(R.string.firstStart), Boolean.TRUE);
                editor.commit();
                ShowTutorial();
            }
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    private void ShowTutorial() {
        startActivity(new Intent(SelectDevice.this, Tutorial.class));
    }
}

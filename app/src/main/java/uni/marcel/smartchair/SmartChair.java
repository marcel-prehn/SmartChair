package uni.marcel.smartchair;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class SmartChair {

    private String deviceName;
    private Sensor[] sensors;
    private int THRESHOLD = 50;
    private BtAdapter btAdapter;
    private BtThread btThread;

    public SmartChair(String deviceName) {
        this.deviceName = deviceName;
    }

    public Sensor[] getSensors() {
        return this.sensors;
    }

    public void setSensors(Sensor[] sensors) {
        if(sensors != null) {
            this.sensors = sensors;
        }
    }

    public void addSensor(Sensor s) {
        if(s != null) {
            Sensor[] temp = new Sensor[sensors.length + 1];
            for(int i = 0; i < sensors.length; i++) {
                temp[i] = sensors[i];
            }
            temp[sensors.length] = s;
            this.sensors = temp;
        }
    }

    public Sensor getSensor(int id) {
        if(id >= 0) {
            for(Sensor s : sensors) {
                if(s.getId() == id) {
                    return s;
                }
            }
            return null;
        }
        else {
            return null;
        }
    }

    private void Connect() throws IOException {
        try {
            if (btAdapter != null) {
                btAdapter.connect(deviceName);
            }
        }
        catch (Exception ex) {
            Log.e("smartchair", "connect:" + ex.toString());
        }
    }

    //TODO messung der last in seat klasse
    public Sensor measureLoad() {
        Sensor sensor = null;



        return sensor;
    }
}

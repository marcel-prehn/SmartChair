package uni.marcel.smartchair;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class SmartChair {

    private Sensor[] sensors;
    private String device;
    private BtAdapter bt;

    public SmartChair(String deviceName) {
        bt = new BtAdapter();
        this.device = deviceName;
    }

    public void connect() {
        try {
            bt.connect(device);
        }
        catch (Exception ex) {
            Log.e("error", "smartchair read " + ex.toString());
        }
    }

    public Sensor[] getSensors() {
        this.sensors = bt.read();
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

    public void measureLoad() {

    }
}

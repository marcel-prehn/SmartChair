package uni.marcel.smartchair;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.ParcelUuid;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

public class BtAdapter {
    private BluetoothAdapter blueAdapter;
    private Set<BluetoothDevice> bondedDevices;
    private OutputStream outputStream;
    private InputStream inStream;
    private BluetoothDevice device;
    private BluetoothSocket socket;

    public BtAdapter() {
        blueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (blueAdapter != null) {
            if (blueAdapter.isEnabled()) {
                bondedDevices = blueAdapter.getBondedDevices();
            }
        }
    }

    public BluetoothAdapter getBlueAdapter() {
        return this.blueAdapter;
    }

    public Set<BluetoothDevice> getBondedDevices() {
        return this.bondedDevices;
    }

    public void connect(String deviceName) throws IOException {
        if (bondedDevices.size() > 0) {
            for (BluetoothDevice b : bondedDevices) {
                if (deviceName.equals(b.getName())) {
                    device = b;
                }
            }
            //Object[] devices = (Object []) bondedDevices.toArray();
            //BluetoothDevice device = (BluetoothDevice) devices[position];

            Log.d("info", "device name: " + deviceName);

            ParcelUuid[] uuids = device.getUuids();
            Log.d("info", "device uuid: " + uuids[0]);

            //socket = device.createInsecureRfcommSocketToServiceRecord(uuids[0].getUuid());
            blueAdapter.cancelDiscovery();
            BtThread btThread = new BtThread(device);
            btThread.start();
            socket = btThread.getSocket();
        }
    }

    public Sensor[] read() {
        try {
            inStream = socket.getInputStream();
            if (inStream.available() > 0) {
                int size = inStream.available();
                int pos = 0;
                char delimiter = '\n';

                byte[] str = new byte[size];
                byte[] buffer = new byte[1024];

                inStream.read(str);

                for (int i = 0; i < size; i++) {
                    byte b = str[i];
                    if (b == delimiter) {
                        byte[] encodedBytes = new byte[pos];
                        System.arraycopy(buffer, 0, encodedBytes, 0, encodedBytes.length);
                        final String data = new String(encodedBytes, "US-ASCII").replaceAll("\\p{C}", ""); // Remove non-printable chars
                        pos = 0;
                        return StreamToSensor(data);
                    }
                    else {
                        buffer[pos++] = b;
                    }
                }
                return null;
            }
            else {
                return null;
            }
        }
        catch (IOException ex) {
            Log.d("error", ex.toString());
            return null;
        }
    }

    public boolean isConnected() {
        if (socket != null && socket.isConnected()) {
            return true;
        }
        return false;
    }

    public void close() {
        try {
            if (socket != null && socket.isConnected()) {
                socket.close();
                socket = null;
            }
        } catch (IOException ex) {
            Log.d("error", ex.toString());
        }
    }

    private Sensor[] StreamToSensor(String stream) {
        if(stream != null && stream != "" && stream.contains(";")) {
            String[] values = stream.split(";");
            Sensor[] sensors = new Sensor[values.length];
            for(int i = 0; i < values.length; i++) {
                if(values[i].equals("")) {
                    sensors[i] = new Sensor(i, 0);
                }
                else {
                    sensors[i] = new Sensor(i, (int)Integer.valueOf(values[i]));
                }
            }
            return sensors;
        }
        else {
            return null;
        }
    }
}

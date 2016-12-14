package uni.marcel.smartchair;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BtThread extends Thread {

    private BtAdapter adapter;
    private BluetoothSocket socket;
    private BluetoothDevice device;
    private UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private InputStream input;
    private OutputStream output;


    public BtThread(BluetoothDevice device) {
        this.device = device;
        try {
            socket = this.device.createInsecureRfcommSocketToServiceRecord(uuid);
        } catch (IOException ex) {
            Log.e("error, thread", ex.toString());
        }
    }

    public void run() {
        try {
            socket.connect();
            input = socket.getInputStream();
            output = socket.getOutputStream();
        } catch (IOException connectException) {
            Log.e("error, thread run", connectException.toString());
            try {
                socket.close();
            } catch (IOException closeException) {
                Log.e("error, thread run close", closeException.toString());
            }
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException closeException) {
            Log.e("error, thread close", closeException.toString());
        }
    }

    public BluetoothSocket getSocket() {
        return this.socket;
    }
}

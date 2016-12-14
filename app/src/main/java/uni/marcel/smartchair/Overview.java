package uni.marcel.smartchair;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InterruptedIOException;

public class Overview extends Activity {

    private BtAdapter bt;
    private String deviceName;
    private Thread threadRead;

    TextView tvDeviceValue;
    TextView tvStatusValue;
    TextView tvLoad0;
    TextView tvLoad1;
    TextView tvLoad2;
    TextView tvLoad3;
    TextView tvLoad4;
    TextView tvLoad5;
    TextView tvLoad6;
    TextView tvLoad7;
    TextView[] textViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        try {
            Initialize();
            SetDeviceName();
        }
        catch (Exception ex) {
            Log.e("oncreate", ex.getMessage());
        }
    }

    private void Initialize() {
        bt = new BtAdapter();
        textViews = new TextView[8];
        tvDeviceValue = (TextView)findViewById(R.id.tvDeviceValue);
        tvStatusValue = (TextView)findViewById(R.id.tvStatusValue);
        tvLoad0 = (TextView)findViewById(R.id.tvLoad0);
        tvLoad1 = (TextView)findViewById(R.id.tvLoad1);
        tvLoad2 = (TextView)findViewById(R.id.tvLoad2);
        tvLoad3 = (TextView)findViewById(R.id.tvLoad3);
        tvLoad4 = (TextView)findViewById(R.id.tvLoad4);
        tvLoad5 = (TextView)findViewById(R.id.tvLoad5);
        tvLoad6 = (TextView)findViewById(R.id.tvLoad6);
        tvLoad7 = (TextView)findViewById(R.id.tvLoad7);
        textViews[0] = tvLoad0;
        textViews[1] = tvLoad1;
        textViews[2] = tvLoad2;
        textViews[3] = tvLoad3;
        textViews[4] = tvLoad4;
        textViews[5] = tvLoad5;
        textViews[6] = tvLoad6;
        textViews[7] = tvLoad7;
    }

    private void SetDeviceName() {
        Intent intentSelectDevice = getIntent();
        String deviceName = intentSelectDevice.getStringExtra("deviceName");
        if(deviceName != "") {
            this.deviceName = deviceName;
            tvDeviceValue.setText(deviceName);
        }
        else {
            tvDeviceValue.setText("No Device Selected");
        }
    }

    private void Connect() throws IOException {
        try {
            if (bt != null) {
                Runnable conn = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            bt.connect(deviceName);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Overview.this, "connecting...", Toast.LENGTH_SHORT).show();
                                    tvStatusValue.setText("connecting...");
                                }
                            });
                        }
                        catch (IOException ex) {
                            Log.e("io error, overview read", ex.toString());
                        }
                    }
                };
                conn.run();
                tvStatusValue.setText("connected");
            }
        } catch (Exception ex) {
            Log.e("io error, overview read", ex.toString());
        }
    }

    private void Read() {
        threadRead = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(threadRead.isInterrupted() == false) {
                        Thread.sleep(500);
                        final Sensor[] sensors = bt.read();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (sensors != null && sensors.length == 8) {
                                    UpdateData(sensors);
                                }
                            }
                        });
                    }
                }
                catch (InterruptedException ex) {
                    Log.e("error, overview read", ex.toString());
                }
            }
        });
        threadRead.start();
    }

    private void UpdateData(Sensor[] sensors) {
        for(int i = 0; i < textViews.length; i++) {
            textViews[i].setText(sensors[i].getId() + ": " + sensors[i].getValue());
        }
        UpdateImage(sensors);
    }

    private void UpdateImage(Sensor[] sensors) {
        ImageView imgOverview = (ImageView)findViewById(R.id.imgOverview);
        if(sensors != null && sensors.length == 8) {
            //TODO Threshold values
            int left = sensors[0].getValue() + sensors[3].getValue() + sensors[5].getValue();
            int right = sensors[2].getValue() + sensors[4].getValue() + sensors[7].getValue();
            int front = sensors[0].getValue() + sensors[1].getValue() + sensors[2].getValue();
            int back = sensors[5].getValue() + sensors[6].getValue() + sensors[7].getValue();
            int sensor0 = sensors[0].getValue();
            int sensor1 = sensors[1].getValue();
            int sensor2 = sensors[2].getValue();
            int sensor3 = sensors[3].getValue();
            int sensor4 = sensors[4].getValue();
            int sensor5 = sensors[5].getValue();
            int sensor6 = sensors[6].getValue();
            int sensor7 = sensors[7].getValue();

            if(left == right && front == back) {
                imgOverview.setImageResource(R.drawable.smartchair_overview2);
            }
            if(sensor0 > sensor2 && sensor3 > sensor4 && sensor5 > sensor7) {
                imgOverview.setImageResource(R.drawable.smartchair_overview_left);
            }
            if(sensor0 < sensor2 && sensor3 < sensor4 && sensor5 < sensor7) {
                imgOverview.setImageResource(R.drawable.smartchair_overview_right);
            }
            if(sensor0 > sensor5 && sensor1 > sensor6 && sensor2 > sensor7) {
                imgOverview.setImageResource(R.drawable.smartchair_overview_front);
            }
            if(sensor0 < sensor5 && sensor1 < sensor6 && sensor2 < sensor7) {
                imgOverview.setImageResource(R.drawable.smartchair_overview_back);
            }
            if(sensor0 > sensor1 && sensor0 > sensor2) {
                imgOverview.setImageResource(R.drawable.smartchair_overview_0);
            }
            if(sensor2 > sensor0 && sensor2 > sensor1) {
                imgOverview.setImageResource(R.drawable.smartchair_overview_2);
            }
            if(sensor5 > sensor7 && sensor5 > sensor6) {
                imgOverview.setImageResource(R.drawable.smartchair_overview_5);
            }
            if(sensor7 > sensor5 && sensor7 > sensor6) {
                imgOverview.setImageResource(R.drawable.smartchair_overview_7);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(bt != null && bt.isConnected()) {
            bt.close();
            bt = null;
        }
        if(threadRead != null && threadRead.isAlive()) {
            threadRead.interrupt();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Initialize();
            Connect();
            Read();
        }
        catch (IOException ex) {
            Log.e("onresume", ex.toString());
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(bt != null && bt.isConnected()) {
            bt.close();
            bt = null;
        }
        if(threadRead != null && threadRead.isAlive()) {
            threadRead.interrupt();
        }
    }
}

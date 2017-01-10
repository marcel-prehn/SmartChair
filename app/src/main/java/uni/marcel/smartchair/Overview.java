package uni.marcel.smartchair;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Overview extends Activity {

    private BtAdapter bt;
    private String deviceName;
    private Thread threadRead;
    private SmartChair chair;
    private CountDownTimer timer;
    private SharedPreferences preferences;

    TextView tvLoad0;
    TextView tvLoad1;
    TextView tvLoad2;
    TextView tvLoad3;
    TextView tvLoad4;
    TextView tvLoad5;
    TextView tvLoad6;
    TextView tvLoad7;
    TextView[] textViews;
    TextView tvAdvice;
    TextView tvTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        try {
            SetDeviceName();
            Initialize();
            StartTimer();
        }
        catch (Exception ex) {
            Log.e("oncreate", ex.getMessage());
        }
    }

    private void Initialize() {
        bt = new BtAdapter();
        //chair = new SmartChair(deviceName);
        tvAdvice = (TextView) findViewById(R.id.tvAdviceText);
        textViews = new TextView[8];
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
        tvTimer = (TextView) findViewById(R.id.tvTimer);
    }

    private void SetDeviceName() {
        Intent intentSelectDevice = getIntent();
        String deviceName = intentSelectDevice.getStringExtra("deviceName");
        if(deviceName != "") {
            this.deviceName = deviceName;
        }
    }

    private void StartTimer() {
        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final int index = preferences.getInt("timerIntervalIndex", 3);
        final String[] timerValues = getResources().getStringArray(R.array.settingsTimerInterval);

        Log.i("timer", "index: " + index);

        final int INTERVAL = (Integer.parseInt(timerValues[index])) * 1000 * 60;

        try {
            timer = new CountDownTimer(INTERVAL, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvTimer.setText(String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60));
                }

                @Override
                public void onFinish() {
                    tvTimer.setText(getText(R.string.timerElapsed));
                    tvTimer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            StartTimer();
                        }
                    });
                }
            }.start();
        }
        catch (Exception ex) {
            Log.e("timer", ex.getMessage());
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
                            //chair.connect();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Overview.this, "connecting...", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        //catch (IOException ex) {
                        catch (Exception ex) {
                            Log.e("io error, overview read", ex.toString());
                        }
                    }
                };
                conn.run();
            }
        }
        catch (Exception ex) {
            Log.e("io error, overview read", ex.toString());
        }
        finally {
            Toast.makeText(Overview.this, "connected", Toast.LENGTH_SHORT).show();
        }
    }

    private void Read() {
        threadRead = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(threadRead.isInterrupted() == false) {
                        Thread.sleep(500);
                        //final Sensor[] sensors = chair.getSensors();
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

        if(sensors != null && sensors.length == 8) {
            //TODO Threshold values
            final float THRESHOLD = 1.5f;
            int left = sensors[0].getValue() + sensors[3].getValue() + sensors[5].getValue();
            int right = sensors[2].getValue() + sensors[4].getValue() + sensors[7].getValue();
            int front = sensors[0].getValue() + sensors[1].getValue() + sensors[2].getValue();
            int back = sensors[5].getValue() + sensors[6].getValue() + sensors[7].getValue();
            int sensor0 = sensors[0].getValue();
            int sensor2 = sensors[2].getValue();
            int sensor5 = sensors[5].getValue();
            int sensor7 = sensors[7].getValue();

            if(front*THRESHOLD > back) {
                if(sensor0 > sensor2) {
                    Highlight(Sensors.SENSOR0);
                }
                else if(sensor2 > sensor0) {
                    Highlight(Sensors.SENSOR2);
                }
                else {
                    Highlight(Sensors.FRONT);
                }
            }
            else if(back > front*THRESHOLD) {
                if(sensor5 > sensor7) {
                    Highlight(Sensors.SENSOR5);
                }
                else if(sensor7 > sensor5) {
                    Highlight(Sensors.SENSOR7);
                }
                else {
                    Highlight(Sensors.BACK);
                }
            }
            else if(left > right) {
                if(sensor0 > sensor5) {
                    Highlight(Sensors.SENSOR0);
                }
                else if(sensor5 > sensor0) {
                    Highlight(Sensors.SENSOR5);
                }
                else {
                    Highlight(Sensors.LEFT);
                }
            }
            else if(right > left) {
                if(sensor2 > sensor7) {
                    Highlight(Sensors.SENSOR2);
                }
                else if(sensor7 > sensor2) {
                    Highlight(Sensors.SENSOR7);
                }
                else {
                    Highlight(Sensors.RIGHT);
                }
            }
            else {
                Highlight(Sensors.NONE);
            }

        }
    }

    private void Highlight(Sensors sensor) {
        ImageView imgOverview = (ImageView)findViewById(R.id.imgOverview);

        switch (sensor) {
            case SENSOR0:
                imgOverview.setImageResource(R.drawable.smartchair_overview_0);
                tvAdvice.setText(R.string.overviewAdviceText8);
                break;
            case SENSOR2:
                imgOverview.setImageResource(R.drawable.smartchair_overview_2);
                tvAdvice.setText(R.string.overviewAdviceText3);
                break;
            case SENSOR5:
                imgOverview.setImageResource(R.drawable.smartchair_overview_5);
                tvAdvice.setText(R.string.overviewAdviceText5);
                break;
            case SENSOR7:
                imgOverview.setImageResource(R.drawable.smartchair_overview_7);
                tvAdvice.setText(R.string.overviewAdviceText6);
                break;
            case FRONT:
                imgOverview.setImageResource(R.drawable.smartchair_overview_front);
                tvAdvice.setText(R.string.overviewAdviceText4);
                break;
            case BACK:
                imgOverview.setImageResource(R.drawable.smartchair_overview_back);
                tvAdvice.setText(R.string.overviewAdviceText7);
                break;
            case LEFT:
                imgOverview.setImageResource(R.drawable.smartchair_overview_left);
                tvAdvice.setText(R.string.overviewAdviceText10);
                break;
            case RIGHT:
                imgOverview.setImageResource(R.drawable.smartchair_overview_right);
                tvAdvice.setText(R.string.overviewAdviceText13);
                break;
            case NONE:
                imgOverview.setImageResource(R.drawable.smartchair_overview2);
                tvAdvice.setText(R.string.overviewAdviceText1);
                break;
            default:
                imgOverview.setImageResource(R.drawable.smartchair_overview2);
                tvAdvice.setText(R.string.overviewAdviceText1);
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

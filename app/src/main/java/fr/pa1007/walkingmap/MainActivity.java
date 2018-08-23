package fr.pa1007.walkingmap;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import fr.pa1007.walkingmap.gps.listener.GPSListener;
import fr.pa1007.walkingmap.notifiaction.StartWalkingNotification;
import fr.pa1007.walkingmap.sensor.listener.StepSensorListener;

public class MainActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor        mSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean stepInit = false;
        super.onCreate(savedInstanceState);
        final Context context = this;
        try {
            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            mSensor.getName();
            stepInit = true;
        }
        catch (NullPointerException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("No sensor detected")
                    .setMessage("The application will use the GPS instead")
                    .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }
        setContentView(R.layout.activity_main);
        Button startButton = findViewById(R.id.button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartWalkingNotification.notify(context, "exc", 0, 1, "test", "dqqzd");
            }
        });
        if (stepInit) {
            mSensorManager.registerListener(new StepSensorListener(), mSensor, 1);
        }
        else {
            LocationManager  locationManager  = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new GPSListener(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, locationListener);
        }
    }
}

package mike.androidapps;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MikeAppActivity1 extends Activity {

	private SensorManager mySensorManager;
	private Sensor accelerometer, proximity, lightMeter, orientation;
	private TextView textX, textY, textZ, textMaxAcc, textMaxAccG, textXG, textYG, textZG, textProx, textLight, textAzimuth, textPitch, textRoll;
	private double x, y, z, maxAcc = 0.00;
	private int prox, light, azimuth, pitch, roll = 0;

	/**
	 *  @Override
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.main);

    	textX = (TextView) findViewById(R.id.textX);
		textY = (TextView) findViewById(R.id.textY);
		textZ = (TextView) findViewById(R.id.textZ);
		textXG = (TextView) findViewById(R.id.textXG);
		textYG = (TextView) findViewById(R.id.textYG);
		textZG = (TextView) findViewById(R.id.textZG);
		textMaxAcc = (TextView) findViewById(R.id.textMaxAcc);
		textMaxAccG = (TextView) findViewById(R.id.textMaxAccG);
		textProx = (TextView) findViewById(R.id.textProx);
		textLight = (TextView) findViewById(R.id.textLight);
		textAzimuth = (TextView) findViewById(R.id.textAzimuth);
		textPitch = (TextView) findViewById(R.id.textPitch);
		textRoll = (TextView) findViewById(R.id.textRoll);
		
		final Button exitButton = (Button) findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	System.exit(0);
            }
        });

		mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		accelerometer = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		proximity = mySensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		lightMeter = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
		orientation = mySensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		mySensorManager.registerListener(mySensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
		mySensorManager.registerListener(mySensorEventListener, proximity, SensorManager.SENSOR_DELAY_UI);
		mySensorManager.registerListener(mySensorEventListener, lightMeter, SensorManager.SENSOR_DELAY_UI);
		mySensorManager.registerListener(mySensorEventListener, orientation, SensorManager.SENSOR_DELAY_UI);
	}
	
	public void onDestroy() {
		mySensorManager.unregisterListener(mySensorEventListener, accelerometer);
		mySensorManager.unregisterListener(mySensorEventListener, proximity);
		mySensorManager.unregisterListener(mySensorEventListener, lightMeter);
		mySensorManager.unregisterListener(mySensorEventListener, orientation);
	}

	/** Accelerometer Listener*/	
	private SensorEventListener mySensorEventListener = new SensorEventListener() {
		
	    public void onAccuracyChanged(Sensor sensor, int accuracy) {
	    }

	    public void onSensorChanged(SensorEvent event) {
	    	switch (event.sensor.getType()) {
	            case Sensor.TYPE_ACCELEROMETER:
	            	int dp = 2;		
	            	double gravity = 9.80665;
			    	x = decimalPlaces(event.values[0], dp);
			    	y = decimalPlaces(event.values[1], dp);
			    	z = decimalPlaces(event.values[2], dp);
			    	
			    	double temp = Math.max(Math.max(Math.abs(x), Math.abs(y)), Math.abs(z));
			    	if(temp > maxAcc)
			    		maxAcc = temp;
					
			    	textX.setText(String.valueOf(x));
					textY.setText(String.valueOf(y));	
					textZ.setText(String.valueOf(z));
					textXG.setText(String.valueOf(decimalPlaces(x/gravity, 2)));
					textYG.setText(String.valueOf(decimalPlaces(y/gravity, 2)));	
					textZG.setText(String.valueOf(decimalPlaces(z/gravity, 2)));
					textMaxAcc.setText(String.valueOf(maxAcc));
					textMaxAccG.setText(String.valueOf(decimalPlaces(maxAcc/gravity, dp)));					
			    	break;
			    	
	            case Sensor.TYPE_PROXIMITY:  
			    	prox = (int)event.values[0];
			    	if(prox == 1)
			    		textProx.setText("Far");
			    	else
			    		textProx.setText("Near");
					break;
					
	            case Sensor.TYPE_LIGHT:  
			    	light = (int)event.values[0];
			    	textLight.setText(String.valueOf(light));
					break;
					
	            case Sensor.TYPE_ORIENTATION:  
	            	azimuth = (int)event.values[0];
			    	pitch = (int)event.values[1];
			    	roll = (int)event.values[2];
					
			    	if(azimuth > 337.5 || azimuth <= 22.5)
			    		textAzimuth.setText("N");
			    	else if(azimuth > 22.5 && azimuth <= 67.5)
			    		textAzimuth.setText("NE");
			    	else if(azimuth > 67.5 && azimuth <= 112.5)
			    		textAzimuth.setText("E");
			    	else if(azimuth > 112.5 && azimuth <= 157.5)
			    		textAzimuth.setText("SE");
			    	else if(azimuth > 157.5 && azimuth <= 202.5)
			    		textAzimuth.setText("S");
			    	else if(azimuth > 202.5 && azimuth <= 247.5)
			    		textAzimuth.setText("SW");
			    	else if(azimuth > 247.5 && azimuth <= 292.5)
			    		textAzimuth.setText("W");
			    	else if(azimuth > 292.5 && azimuth <= 337.5)
			    		textAzimuth.setText("NW");
					textPitch.setText(String.valueOf(pitch));	
					textRoll.setText(String.valueOf(roll));
					break;
	    	}
	    }
	};
	
	public double decimalPlaces(double num, int x) {
		return ((double)(Math.round(num*Math.pow(10, x)))) / Math.pow(10, x);
	}
}
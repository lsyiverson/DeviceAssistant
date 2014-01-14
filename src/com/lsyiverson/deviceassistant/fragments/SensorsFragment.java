package com.lsyiverson.deviceassistant.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.lsyiverson.deviceassistant.R;
import com.lsyiverson.deviceassistant.utils.BatteryUtils;
import com.lsyiverson.deviceassistant.utils.Constants;
import com.lsyiverson.deviceassistant.utils.LogUtils;

public class SensorsFragment extends Fragment implements SensorEventListener {

    private Context mContext;

    private SimpleAdapter mAdapter;

    private SensorManager mSensorManager;

    private List<Sensor> mSensorList;

    private List<Map<String, Object>> mSensorItemList = new ArrayList<Map<String,Object>>();

    private HashMap<String, Integer> mSensorMap = new HashMap<String, Integer>();

    private HashMap<String, Integer> mSensorBackup = new HashMap<String, Integer>();

    public SensorsFragment() {
    }

    @Override
    public void onPause() {
        for (Sensor sensor : mSensorList) {
            mSensorManager.unregisterListener(this, sensor);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        for (Sensor sensor : mSensorList) {
            mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        mContext = activity;
        mSensorManager = (SensorManager)mContext.getSystemService(Context.SENSOR_SERVICE);

        mSensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        for (int i=0;i<mSensorList.size();i++) {
            Sensor sensor = mSensorList.get(i);
            if (!mSensorMap.containsKey(sensor.getName())) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put(Constants.LIST_NAME, sensor.getName() + " ("+sensor.getVendor()+")");
                map.put(Constants.LIST_VALUE, "");
                mSensorItemList.add(map);
                mSensorMap.put(sensor.getName(), mSensorItemList.size() - 1);

                LogUtils.d(SensorsFragment.class, "sensor name : " + sensor.getName() + " ("+sensor.getVendor()+")");
                LogUtils.d(SensorsFragment.class, "sensor power : " + sensor.getPower());
            } else {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put(Constants.LIST_NAME, sensor.getName() + " ("+sensor.getVendor()+")");
                map.put(Constants.LIST_VALUE, "");
                mSensorItemList.add(map);
                mSensorBackup.put(sensor.getName(), mSensorItemList.size() - 1);
            }
        }
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sensors, container, false);
        ListView sensor_list = (ListView)rootView.findViewById(R.id.sensor_list);
        mAdapter = new SimpleAdapter(mContext, mSensorItemList, R.layout.sensor_info_item, new String[] {Constants.LIST_NAME, Constants.LIST_VALUE},
                new int[] { R.id.sensor_name, R.id.sensor_value});
        sensor_list.setAdapter(mAdapter);
        return rootView;
    }

    private void notifyDataSetChanged() {
        synchronized (mAdapter) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        int index = mSensorMap.get(sensor.getName());
        Map<String, Object> map = mSensorItemList.get(index);
        StringBuilder builder = new StringBuilder();
        switch (sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                float x_accelerometer = event.values[0];
                float y_accelerometer = event.values[1];
                float z_accelerometer = event.values[2];
                builder.append(mContext.getString(R.string.sensor_xyz_acceleration, x_accelerometer, y_accelerometer, z_accelerometer));
                builder.append("\n");
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                boolean use_celsius = BatteryUtils.getUseCelsius(mContext);
                float ambient_temperature = event.values[0];
                if (use_celsius) {
                    builder.append(mContext.getString(R.string.sensor_temperature_celsius, ambient_temperature));
                } else {
                    ambient_temperature = (float)(ambient_temperature * 1.8 + 32);
                    builder.append(mContext.getString(R.string.sensor_temperature_fahrenheit, ambient_temperature));
                }
                builder.append("\n");
                break;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                float x_game_rotation = event.values[0];
                float y_game_rotation = event.values[1];
                float z_game_rotation = event.values[2];
                builder.append(mContext.getString(R.string.sensor_xyz, String.format("%.5f", x_game_rotation), String.format("%.5f", y_game_rotation), String.format("%.5f", z_game_rotation)));
                builder.append("\n");
                break;
            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                float x_geomagnetic_rotation = event.values[0];
                float y_geomagnetic_rotation = event.values[1];
                float z_geomagnetic_rotation = event.values[2];
                builder.append(mContext.getString(R.string.sensor_xyz, String.format("%.5f", x_geomagnetic_rotation), String.format("%.5f", y_geomagnetic_rotation), String.format("%.5f", z_geomagnetic_rotation)));
                builder.append("\n");
                break;
            case Sensor.TYPE_GRAVITY:
                float x_gravity = event.values[0];
                float y_gravity = event.values[1];
                float z_gravity = event.values[2];
                builder.append(mContext.getString(R.string.sensor_xyz_acceleration, x_gravity, y_gravity, z_gravity));
                builder.append("\n");
                break;
            case Sensor.TYPE_GYROSCOPE:
                float x_gyroscore = event.values[0];
                float y_gyroscore = event.values[1];
                float z_gyroscore = event.values[2];
                builder.append(mContext.getString(R.string.sensor_xyz_gyroscore, x_gyroscore, y_gyroscore, z_gyroscore));
                builder.append("\n");
                break;
            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                if (mSensorBackup.containsKey(sensor.getName())) {
                    map = mSensorItemList.get(mSensorBackup.get(sensor.getName()));
                }
                float x_gyroscore_uncalibrated = event.values[0];
                float y_gyroscore_uncalibrated = event.values[1];
                float z_gyroscore_uncalibrated = event.values[2];

                float x_estimated_drift = event.values[3];
                float y_estimated_drift = event.values[4];
                float z_estimated_drift = event.values[5];
                builder.append(mContext.getString(R.string.sensor_xyz_gyroscore, x_gyroscore_uncalibrated, y_gyroscore_uncalibrated, z_gyroscore_uncalibrated));
                builder.append("\n");
                builder.append(mContext.getString(R.string.sensor_xyz_gyroscore_estimated_drift, x_estimated_drift, y_estimated_drift, z_estimated_drift));
                builder.append("\n");
                break;
            case Sensor.TYPE_LIGHT:
                float light = event.values[0];
                builder.append(mContext.getString(R.string.sensor_light, light));
                builder.append("\n");
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                float x_linear_accelerometer = event.values[0];
                float y_linear_accelerometer = event.values[1];
                float z_linear_accelerometer = event.values[2];
                builder.append(mContext.getString(R.string.sensor_xyz_acceleration, x_linear_accelerometer, y_linear_accelerometer, z_linear_accelerometer));
                builder.append("\n");
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                float x_magnetic_field = event.values[0];
                float y_magnetic_field = event.values[1];
                float z_magnetic_field = event.values[2];
                builder.append(mContext.getString(R.string.sensor_xyz_magnetic_field, x_magnetic_field, y_magnetic_field, z_magnetic_field));
                builder.append("\n");
                break;
            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                if (mSensorBackup.containsKey(sensor.getName())) {
                    map = mSensorItemList.get(mSensorBackup.get(sensor.getName()));
                }
                float x_uncali = event.values[0];
                float y_uncali = event.values[1];
                float z_uncali = event.values[2];

                float x_bias = event.values[3];
                float y_bias = event.values[4];
                float z_bias = event.values[5];
                builder.append(mContext.getString(R.string.sensor_xyz_magnetic_field, x_uncali, y_uncali, z_uncali));
                builder.append("\n");
                builder.append(mContext.getString(R.string.sensor_xyz_magnetic_field_bias, x_bias, y_bias, z_bias));
                builder.append("\n");
                break;
            case Sensor.TYPE_ORIENTATION:
                float azimuth = event.values[0];
                float pitch = event.values[1];
                float roll = event.values[2];
                builder.append(mContext.getString(R.string.sensor_orientation, azimuth, pitch, roll));
                builder.append("\n");
                break;
            case Sensor.TYPE_PRESSURE:
                float pressure = event.values[0];
                builder.append(mContext.getString(R.string.sensor_pressure, pressure));
                builder.append("\n");
                break;
            case Sensor.TYPE_PROXIMITY:
                float distance = event.values[0];
                builder.append(mContext.getString(R.string.sensor_proximity, distance));
                builder.append("\n");
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                float relative_humidity = event.values[0];
                builder.append(mContext.getString(R.string.sensor_relative_humidity, relative_humidity));
                builder.append("\n");
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                float x_rotation = event.values[0];
                float y_rotation = event.values[1];
                float z_rotation = event.values[2];
                builder.append(mContext.getString(R.string.sensor_xyz, String.format("%.5f", x_rotation), String.format("%.5f", y_rotation), String.format("%.5f", z_rotation)));
                builder.append("\n");
                break;
            case Sensor.TYPE_SIGNIFICANT_MOTION:
                break;
            case Sensor.TYPE_STEP_COUNTER:
                float step_offset = event.values[0];
                builder.append(mContext.getString(R.string.sensor_step, step_offset));
                builder.append("\n");
                break;
            case Sensor.TYPE_STEP_DETECTOR:
                float step_detector = event.values[0];
                builder.append(mContext.getString(R.string.sensor_step, step_detector));
                builder.append("\n");
                break;
            case Sensor.TYPE_TEMPERATURE:
                boolean is_use_celsius = BatteryUtils.getUseCelsius(mContext);
                float temperature = event.values[0];
                if (is_use_celsius) {
                    builder.append(mContext.getString(R.string.sensor_temperature_celsius, temperature));
                } else {
                    temperature = (float)(temperature * 1.8 + 32);
                    builder.append(mContext.getString(R.string.sensor_temperature_fahrenheit, temperature));
                }
                builder.append("\n");
                break;
        }
        builder.append(mContext.getString(R.string.sensor_current_consumption, sensor.getPower()));
        map.put(Constants.LIST_VALUE, builder.toString());
        notifyDataSetChanged();
    };
}

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
import com.lsyiverson.deviceassistant.utils.Constants;
import com.lsyiverson.deviceassistant.utils.LogUtils;

public class SensorsFragment extends Fragment implements SensorEventListener {

    protected List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();

    private SimpleAdapter mAdapter;

    private SensorManager mSensorManager;

    private List<Sensor> mSensorList;

    private ArrayList<HashMap<String, String>> mSensorItemList = new ArrayList<HashMap<String,String>>();

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
        mSensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);

        mSensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        for (Sensor sensor : mSensorList) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(Constants.LIST_NAME, sensor.getName());
            map.put(Constants.LIST_VALUE, "");
            mSensorItemList.add(map);
            LogUtils.d(SensorsFragment.class, "sensor name : " + sensor.getName() + " ("+sensor.getVendor()+")");
            LogUtils.d(SensorsFragment.class, "sensor power : " + sensor.getPower());
        }
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sensors, container, false);
        ListView sensor_list = (ListView)rootView.findViewById(R.id.sensor_list);
        getData();
        mAdapter = new SimpleAdapter(getActivity(), mList, R.layout.sensor_info_item, new String[] {Constants.LIST_NAME, Constants.LIST_VALUE},
                new int[] { R.id.sensor_name, R.id.sensor_value});
        sensor_list.setAdapter(mAdapter);
        return rootView;
    }

    private void notifyDataSetChanged() {
        synchronized (mAdapter) {
            mAdapter.notifyDataSetChanged();
        }
    }

    protected List<Map<String, Object>> getData() {
        return null;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
    };
}

package com.lsyiverson.deviceassistant.fragments;

import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.lsyiverson.deviceassistant.utils.BatteryUtils;

public class BatteryFragment extends BaseInfoFragment {

    private BatteryReceiver mBatteryReceiver;
    private Intent mBatteryIntent;

    @Override
    protected List<Map<String, Object>> getData() {
        mList.clear();
        if (mBatteryIntent != null) {
            mList.addAll(BatteryUtils.getBatteryInfo(getActivity(), mBatteryIntent));
        }
        return mList;
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(mBatteryReceiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        mBatteryReceiver = new BatteryReceiver();
        IntentFilter batteryFilter = new IntentFilter();
        batteryFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        getActivity().registerReceiver(mBatteryReceiver, batteryFilter);
        super.onResume();
    }

    Handler hander = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            mBatteryIntent = (Intent)msg.obj;
            getData();
            notifyDataSetChanged();
        }

    };

    private class BatteryReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Message msg = new Message();
            msg.obj = intent;
            hander.sendMessage(msg);
        }
    }

}

package com.lsyiverson.deviceassistant.fragments;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.os.Message;

import com.lsyiverson.deviceassistant.utils.SystemUtils;

public class SystemFragment extends BaseInfoFragment {

    @Override
    protected List<Map<String, Object>> getData() {
        mList.clear();
        mList.addAll(SystemUtils.getSystemInfo(getActivity()));
        return mList;
    }

    Handler hander = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            getData();
            notifyDataSetChanged();
        }

    };

    Timer mTimer;

    @Override
    public void onPause() {
        mTimer.cancel();
        mTimer.purge();
        super.onPause();
    }

    @Override
    public void onResume() {
        mTimer = new Timer();
        mTimer.schedule(new RealTimeRefreshTask(), 3000, 3000);
        super.onResume();
    }

    private class RealTimeRefreshTask extends TimerTask {

        @Override
        public void run() {
            hander.sendEmptyMessage(0);
        }
    }
}

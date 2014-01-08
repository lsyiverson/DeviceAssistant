package com.lsyiverson.deviceassistant.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.os.Message;

import com.lsyiverson.deviceassistant.utils.CpuUtils;
import com.lsyiverson.deviceassistant.utils.GpuUtils;

public class SocFragment extends BaseInfoFragment {

    @Override
    protected List<Map<String, Object>> getData() {
        mList.clear();
        ArrayList<HashMap<String, Object>> cpuInfos = CpuUtils.getCpuInfo(getActivity());
        // Add the cpu informations
        mList.addAll(cpuInfos);
        // Add the gpu informations
        mList.addAll(GpuUtils.getInstance(getActivity()).getGpuInfo());
        return mList;
    }

    Handler hander = new Handler(){

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
        mTimer.schedule(new RealTimeRefreshTask(), 1000, 1000);
        super.onResume();
    }

    private class RealTimeRefreshTask extends TimerTask {

        @Override
        public void run() {
            hander.sendEmptyMessage(0);
        }
    }

}

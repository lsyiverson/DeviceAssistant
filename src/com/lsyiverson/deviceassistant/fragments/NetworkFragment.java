package com.lsyiverson.deviceassistant.fragments;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.lsyiverson.deviceassistant.R;
import com.lsyiverson.deviceassistant.utils.Constants;
import com.lsyiverson.deviceassistant.utils.NetworkUtils;

public class NetworkFragment extends BaseInfoFragment {

    private Context mContext;
    private ConnectivityReceiver mReceiver = new ConnectivityReceiver();

    @Override
    public void onAttach(Activity activity) {
        mContext = activity;
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(mReceiver, filter);
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        mContext.unregisterReceiver(mReceiver);
        super.onDetach();
    }

    @Override
    protected List<Map<String, Object>> getData() {
        mList.clear();

        HashMap<String, Object> opName = new HashMap<String, Object>();
        opName.put(Constants.LIST_NAME, mContext.getString(R.string.network_operator_name));
        opName.put(Constants.LIST_VALUE, NetworkUtils.getOperatorName(mContext));
        mList.add(opName);

        HashMap<String, Object> accessType = new HashMap<String, Object>();
        accessType.put(Constants.LIST_NAME, mContext.getString(R.string.network_type));
        accessType.put(Constants.LIST_VALUE, NetworkUtils.getNetworkTypeName(mContext));
        mList.add(accessType);

        HashMap<String, Object> apn = new HashMap<String, Object>();
        apn.put(Constants.LIST_NAME, mContext.getString(R.string.network_access_point));
        apn.put(Constants.LIST_VALUE, NetworkUtils.getApnType(mContext));
        mList.add(apn);

        HashMap<String, Object> mac_address = new HashMap<String, Object>();
        mac_address.put(Constants.LIST_NAME, mContext.getString(R.string.network_mac_address));
        mac_address.put(Constants.LIST_VALUE, NetworkUtils.getMacAddress(mContext));
        mList.add(mac_address);

        String[] ids = NetworkUtils.getDeviceString(mContext);

        HashMap<String, Object> phone_number = new HashMap<String, Object>();
        phone_number.put(Constants.LIST_NAME, mContext.getString(R.string.network_phone_number));
        phone_number.put(Constants.LIST_VALUE, ids[0]);
        mList.add(phone_number);

        HashMap<String, Object> imei = new HashMap<String, Object>();
        imei.put(Constants.LIST_NAME, mContext.getString(R.string.network_imei));
        imei.put(Constants.LIST_VALUE, ids[1]);
        mList.add(imei);

        HashMap<String, Object> imsi = new HashMap<String, Object>();
        imsi.put(Constants.LIST_NAME, mContext.getString(R.string.network_imsi));
        imsi.put(Constants.LIST_VALUE, ids[2]);
        mList.add(imsi);

        HashMap<String, Object> sim_number = new HashMap<String, Object>();
        sim_number.put(Constants.LIST_NAME, mContext.getString(R.string.network_sim_card_number));
        sim_number.put(Constants.LIST_VALUE, ids[3]);
        mList.add(sim_number);

        return mList;
    }

    class ConnectivityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            getData();
            notifyDataSetChanged();
        }

    }
}

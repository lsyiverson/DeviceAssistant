package com.lsyiverson.deviceassistant.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.lsyiverson.deviceassistant.R;
import com.lsyiverson.deviceassistant.utils.Constants;

public abstract class BaseInfoFragment extends Fragment {
    protected List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();;
    private SimpleAdapter mAdapter;

    public BaseInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_base, container, false);
        ListView info_list = (ListView)rootView.findViewById(R.id.info);
        getData();
        mAdapter = new SimpleAdapter(getActivity(), mList, R.layout.simple_info_item, new String[] {Constants.LIST_NAME, Constants.LIST_VALUE},
                new int[] { R.id.info_name, R.id.info_value});
        info_list.setAdapter(mAdapter);
        return rootView;
    }

    public void notifyDataSetChanged() {
        synchronized (mAdapter) {
            mAdapter.notifyDataSetChanged();
        }
    }

    protected abstract List<Map<String, Object>> getData();
}

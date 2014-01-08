package com.lsyiverson.deviceassistant.fragments;

import java.util.List;
import java.util.Map;

import com.lsyiverson.deviceassistant.utils.SystemUtils;

public class SystemFragment extends BaseInfoFragment {

    @Override
    protected List<Map<String, Object>> getData() {
        mList.clear();
        mList.addAll(SystemUtils.getSystemInfo(getActivity()));
        return mList;
    }

}

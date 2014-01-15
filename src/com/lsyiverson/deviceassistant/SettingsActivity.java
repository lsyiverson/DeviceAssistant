
package com.lsyiverson.deviceassistant;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.lsyiverson.deviceassistant.fragments.SensorsFragment;
import com.lsyiverson.deviceassistant.utils.BatteryUtils;
import com.umeng.analytics.MobclickAgent;

public class SettingsActivity extends ActionBarActivity {
    private RadioGroup mTemperatureUnit;

    private CheckBox mDisplayCurrentCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTemperatureUnit = (RadioGroup)findViewById(R.id.temperature_group);
        mTemperatureUnit.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.celsius:
                        BatteryUtils.setUseCelsius(SettingsActivity.this, true);
                        break;
                    case R.id.fahrenheit:
                        BatteryUtils.setUseCelsius(SettingsActivity.this, false);
                        break;
                }
            }
        });
        mDisplayCurrentCheckBox = (CheckBox)findViewById(R.id.current_check);
        mDisplayCurrentCheckBox
        .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SensorsFragment.setDisplayCurrent(SettingsActivity.this, isChecked);
            }
        });
    }

    @Override
    protected void onResume() {
        MobclickAgent.onResume(this);
        if (BatteryUtils.getUseCelsius(this)) {
            mTemperatureUnit.check(R.id.celsius);
        } else {
            mTemperatureUnit.check(R.id.fahrenheit);
        }
        mDisplayCurrentCheckBox.setChecked(SensorsFragment.getDisplayCurrent(this));
        super.onResume();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPause(this);
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

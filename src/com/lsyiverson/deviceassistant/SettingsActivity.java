
package com.lsyiverson.deviceassistant;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.lsyiverson.deviceassistant.utils.BatteryUtils;

public class SettingsActivity extends ActionBarActivity {
    private RadioGroup mTemperatureUnit;

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
    }

    @Override
    protected void onResume() {
        if (BatteryUtils.getUseCelsius(this)) {
            mTemperatureUnit.check(R.id.celsius);
        } else {
            mTemperatureUnit.check(R.id.fahrenheit);
        }
        super.onResume();
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

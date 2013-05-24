package com.icechen1.lifeclock;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import net.simonvt.widget.DatePicker;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.actionbarsherlock.app.SherlockActivity;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends SherlockActivity {
	
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    static String TAG = "LifeClock";
	@SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pref_layout);
        
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        datePicker.setMaxDate(System.currentTimeMillis());

        boolean showCalendar = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            showCalendar = getResources().getConfiguration().smallestScreenWidthDp >= 600;
        } else {
            showCalendar =
                    (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
        }

        datePicker.setCalendarViewShown(showCalendar);
    }
	public void okBtn(View v){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = settings.edit();
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);


        datePicker.getYear();
        edit.putInt("day", datePicker.getDayOfMonth());
        edit.putInt("month",datePicker.getMonth());
        edit.putInt("year", datePicker.getYear());
        edit.commit();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
        	mAppWidgetId = extras.getInt(
        			AppWidgetManager.EXTRA_APPWIDGET_ID, 
        			AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RemoteViews views = new RemoteViews(this.getPackageName(),
        		R.layout.widget_layout);
        
		DateTime time_now = new DateTime ();
		
		DateTime time_born = new DateTime (datePicker.getYear(), (datePicker.getMonth() + 1), datePicker.getDayOfMonth(),0, 0,0,0);
		
		Interval intv = new Interval(time_born, time_now);
		
	    Log.d(TAG, ""+intv.toDuration().getStandardDays());
		
        views.setTextViewText(R.id.text, "" + intv.toDuration().getStandardDays());
        appWidgetManager.updateAppWidget(mAppWidgetId, views);
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
	}
}

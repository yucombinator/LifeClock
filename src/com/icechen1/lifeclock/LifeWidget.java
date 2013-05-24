package com.icechen1.lifeclock;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.MutableInterval;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

public class LifeWidget extends AppWidgetProvider {

	  @Override
	  public void onUpdate(Context context, AppWidgetManager appWidgetManager,
	      int[] appWidgetIds) {
		  
		DateTime time_now = new DateTime ();
		
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        Integer year = settings.getInt("year", 2010);
        Integer day = settings.getInt("day", 1);
        Integer month = settings.getInt("month", 1);
		DateTime time_born = new DateTime (year, (month + 1), day,0, 0,0,0);
		Interval intv = new Interval(time_born, time_now);
		
	    // Get all ids
	    ComponentName thisWidget = new ComponentName(context,
	    		LifeWidget.class);
	    int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
	    for (int widgetId : allWidgetIds) {
	      // Create some random data

	      RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
	          R.layout.widget_layout);
		    Log.d(SettingsActivity.TAG, ""+intv.toDuration().getStandardDays());
	      // Set the text
	      remoteViews.setTextViewText(R.id.text, "" + intv.toDuration().getStandardDays());

	      // Register an onClickListener
	      Intent intent = new Intent(context, LifeWidget.class);

	      intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
	      intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

	      PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
	          0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	      remoteViews.setOnClickPendingIntent(R.id.text, pendingIntent);
	      appWidgetManager.updateAppWidget(widgetId, remoteViews);
	    }
	  }
	  
}

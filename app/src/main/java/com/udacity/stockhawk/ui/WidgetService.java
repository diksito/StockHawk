package com.udacity.stockhawk.ui;


import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by m_abo on 3/27/2017.
 */

public class WidgetService extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new WidgetAdapter(this.getApplicationContext(),intent));
    }
}

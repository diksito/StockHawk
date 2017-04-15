package com.udacity.stockhawk.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;

public class NewAppWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
       for(int i=0;i<appWidgetIds.length;i++)
       {
           Intent intent=new Intent(context,WidgetService.class);
           intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetIds[i]);
           intent.setData(Uri.parse(intent.toUri(intent.URI_INTENT_SCHEME)));
           RemoteViews views=new RemoteViews(context.getPackageName(),R.layout.app_widget);
          Intent clickedIntent=new Intent(context,MainActivity.class);
           PendingIntent pendingIntent=PendingIntent.getActivity(context,0,clickedIntent,0);
           views.setOnClickPendingIntent(R.id.widget_bar,pendingIntent);
           views.setRemoteAdapter(appWidgetIds[i],R.id.widget_listView,intent);
           views.setEmptyView(R.id.widget_listView,R.id.widget_empty_view);
           appWidgetManager.updateAppWidget(appWidgetIds[i],views);
       }
       super.onUpdate(context,appWidgetManager,appWidgetIds);
    }

}


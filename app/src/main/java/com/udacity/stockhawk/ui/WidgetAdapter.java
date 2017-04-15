package com.udacity.stockhawk.ui;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.PrefUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by m_abo on 3/27/2017.
 */

public class WidgetAdapter implements RemoteViewsService.RemoteViewsFactory {
    Context context;
    Cursor cursor;
    int appWidgetId;
    private final DecimalFormat dollarFormatWithPlus;
    private final DecimalFormat dollarFormat;
    private final DecimalFormat percentageFormat;

    public WidgetAdapter(Context context, Intent intent) {
        this.context = context;
        appWidgetId=intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus.setPositivePrefix("+$");
        percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
        percentageFormat.setMaximumFractionDigits(2);
        percentageFormat.setMinimumFractionDigits(2);
        percentageFormat.setPositivePrefix("+");
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        if(cursor!=null)
        {
            cursor.close();
        }
        cursor=context.getContentResolver().query(
                Contract.Quote.URI,
                new String[]{Contract.Quote.COLUMN_SYMBOL,Contract.Quote.COLUMN_PRICE
                        ,Contract.Quote.COLUMN_ABSOLUTE_CHANGE,Contract.Quote.COLUMN_PERCENTAGE_CHANGE},
                null,
                null,
                Contract.Quote.COLUMN_PRICE+" DESC"
                );
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        int count = 0;
        if (cursor != null) {
            count = cursor.getCount();
        }
        return count;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        String symbol="";
        float price =0;
        float rawAbsoluteChange=0;
        float percentageChange=0;
        if(cursor.moveToPosition(position))
        {
            symbol=cursor.getString(cursor.getColumnIndex(Contract.Quote.COLUMN_SYMBOL));
            price=cursor.getFloat(cursor.getColumnIndex(Contract.Quote.COLUMN_PRICE));
            rawAbsoluteChange = cursor.getFloat(cursor.getColumnIndex(Contract.Quote.COLUMN_ABSOLUTE_CHANGE));
            percentageChange = cursor.getFloat(cursor.getColumnIndex(Contract.Quote.COLUMN_PERCENTAGE_CHANGE));
        }
        RemoteViews views=new RemoteViews(context.getPackageName(), R.layout.list_item_quote);
        views.setTextViewText(R.id.symbol,symbol);
        views.setTextViewText(R.id.price,dollarFormat.format(price));
        if (rawAbsoluteChange > 0) {
           views.setInt(R.id.change,"setBackgroundResource",R.drawable.percent_change_pill_green);
        } else {
            views.setInt(R.id.change,"setBackgroundResource",R.drawable.percent_change_pill_red);
        }
        String changee=dollarFormatWithPlus.format(rawAbsoluteChange);
        String percentage=dollarFormat.format(percentageChange);
        if (PrefUtils.getDisplayMode(context)
                .equals(context.getString(R.string.pref_display_mode_absolute_key))) {
            views.setTextViewText(R.id.change,changee+"");
        } else {
            views.setTextViewText(R.id.change,percentage+"");
        }
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
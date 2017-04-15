package com.udacity.stockhawk.ui;


import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.udacity.stockhawk.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class StockGraph extends AppCompatActivity {
    @BindView(R.id.chart)
    LineChart chart;
    String symbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_graph);
        ButterKnife.bind(this);
        chart = new LineChart(this);
        symbol = getIntent().getExtras().getString(getString(R.string.symbol_key));

        new AsyncTask<Void, Void, Void>() {
            ArrayList<Float> valueX = new ArrayList<Float>();
            ArrayList<Float> valueY = new ArrayList<Float>();

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Calendar from = Calendar.getInstance();
                    Calendar to = Calendar.getInstance();
                    from.add(Calendar.YEAR, -2);
                    Stock stock = YahooFinance.get(symbol);
                    List<HistoricalQuote> history = stock.getHistory(from, to, Interval.WEEKLY);
                    for (HistoricalQuote it : history) {
                        valueX.add(Float.valueOf(it.getDate().getTimeInMillis()));
                        valueY.add(Float.parseFloat(String.valueOf(it.getClose())));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                List<Entry> entries = new ArrayList<Entry>();
                for (int i = 0; i < valueX.size(); i++) {
                    entries.add(new Entry(valueX.get(i),valueY.get(i)));
                }
                LineDataSet dataSet=new LineDataSet(entries,symbol);
                dataSet.setColor(Color.BLUE);
                LineData lineData=new LineData(dataSet);
                chart.setData(lineData);
                chart.setVisibility(View.VISIBLE);
                chart.invalidate();
            }
        }.execute();
    }
}
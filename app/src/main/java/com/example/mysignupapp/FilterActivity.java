package com.example.mysignupapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.Locale;

public class FilterActivity extends AppCompatActivity
{
    String[] criteria ={"User rank", "Increasing price", "Decreasing price", "Item trade off", "Price Only", "Trade off only"};

    AutoCompleteTextView choices;

    ArrayAdapter<String> adapter_items;

    RangeSlider price_range_slider;

    String[] prices_range ={"0-20", "20-50", "50-100", "100-500", "500+"};

    AutoCompleteTextView prices;

    ArrayAdapter<String> adapter_prices;

    TextView multiple_selections;
    boolean[] selectedDay;

    ArrayList<Integer> daylist = new ArrayList<>();
    String[] dayArray = {"Collectors", "Vehicles", "Books", "Men Clothing", "Women Clothing", "Music", "Sports"};


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        choices  = findViewById(R.id.autocomplete_text);

        adapter_items = new ArrayAdapter<String>(this, R.layout.sort_collection, criteria);

        choices.setAdapter(adapter_items);

        choices.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String item = parent.getItemAtPosition(position).toString();
            }
        });

        prices  = findViewById(R.id.autocomplete_text2);

        adapter_prices = new ArrayAdapter<String>(this, R.layout.sort_collection, prices_range);

        prices.setAdapter(adapter_prices);

        prices.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String item = parent.getItemAtPosition(position).toString();
            }
        });


        price_range_slider = findViewById(R.id.price_slider);
        price_range_slider.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
                currencyFormat.setCurrency(Currency.getInstance(Locale.GERMANY));
                return currencyFormat.format(value);
            }
        });

        multiple_selections = findViewById(R.id.category_multiple_selector);
        selectedDay = new boolean[dayArray.length];

        multiple_selections.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(FilterActivity.this);
                builder.setTitle("Category");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(dayArray, selectedDay, new DialogInterface.OnMultiChoiceClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked)
                    {
                        if(isChecked)
                        {
                            daylist.add(which);
                            Collections.sort(daylist);
                        }
                        else
                        {
                            daylist.remove(Integer.valueOf(which));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        StringBuilder stringBuilder = new StringBuilder();

                        for(int j = 0; j < daylist.size(); j++)
                        {
                            stringBuilder.append(dayArray[daylist.get(j)]);

                            if(j != daylist.size() -1)
                            {
                                stringBuilder.append(", ");
                            }
                        }

                        multiple_selections.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });

                builder.setNeutralButton("Clear all", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        for(int j = 0; j <selectedDay.length; j++)
                        {
                            selectedDay[j] = false;
                            daylist.clear();
                            multiple_selections.setText("");
                        }

                    }
                });

                builder.show();
            }
        });

    }
}
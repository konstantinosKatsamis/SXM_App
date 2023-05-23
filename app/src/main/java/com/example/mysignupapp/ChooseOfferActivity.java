package com.example.mysignupapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class ChooseOfferActivity extends Activity
{
    ImageView cancel_button;
    ImageButton money_offer;
    ImageButton ad_offer;

    boolean i_offer_money;
    boolean i_offer_ad;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_offer);

        cancel_button = (ImageView) findViewById(R.id.cancel);
        money_offer = (ImageButton) findViewById(R.id.money_offer_button);
        ad_offer = (ImageButton) findViewById(R.id.ad_offer_button);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * 0.9), (int)(height * 0.6));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        cancel_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        money_offer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(ChooseOfferActivity.this, "Money offer selected", Toast.LENGTH_SHORT).show();
                i_offer_money = true;
                i_offer_ad = false;
                Intent to_offer = new Intent(ChooseOfferActivity.this, MakeOffer.class);
                to_offer.putExtra("OFFER_MONEY", i_offer_money);
                to_offer.putExtra("OFFER_AD", i_offer_ad);
                startActivity(to_offer);
            }
        });

        ad_offer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(ChooseOfferActivity.this, "Ad offer selected", Toast.LENGTH_SHORT).show();
                i_offer_money = false;
                i_offer_ad = true;
                Intent to_offer = new Intent(ChooseOfferActivity.this, MakeOffer.class);
                to_offer.putExtra("OFFER_MONEY", i_offer_money);
                to_offer.putExtra("OFFER_AD", i_offer_ad);
                startActivity(to_offer);
            }
        });
    }
}
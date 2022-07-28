package com.damavandit.apps.chair.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.fragments.ShoppingCartFragment;

import java.util.Random;

public class ShoppingCartActivity extends AppCompatActivity {

    private TextView textChair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        textChair = findViewById(R.id.chair_text);
        Typeface face= Typeface.createFromAsset(getAssets(),"Font/Sans.ttf");
        textChair.setTypeface(face);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.basket_container, ShoppingCartFragment.newInstance());
        transaction.commit();
    }
}

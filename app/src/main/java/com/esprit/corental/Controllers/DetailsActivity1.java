package com.esprit.corental.Controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;


import com.esprit.corental.R;
import com.esprit.corental.Utils.database.AppDataBase;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class DetailsActivity1 extends AppCompatActivity {
    Button info ;
    private AppDataBase database;
    private Context mContext;

    private SharedPreferences mPreference;
    public static final String sharedPrefFile = "com.example.coretal";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collapse);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();
        mPreference = getSharedPreferences(sharedPrefFile,MODE_PRIVATE);



        Button info = findViewById(R.id.button7);
        TextView homeN = findViewById(R.id.HomeName);
        TextView awayN = findViewById(R.id.AwayName);
        ImageView homeI = findViewById(R.id.HomeImg);
       // ImageView awayI = findViewById(R.id.AwayImg);

//        homeN.setText(mPreference.getString("home",""));
      //  awayN.setText(mPreference.getString("away",""));
      //  homeI.setImageResource(mPreference.getInt("homeI",0));
      //  awayI.setImageResource(mPreference.getInt("awayI",0));


        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Fragment fragg = new ProfilePub();
                ((AppCompatActivity) v.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.details, fragg).commit();




            }
        });

        try {
            Glide.with(this).load(R.drawable.bg).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }


}

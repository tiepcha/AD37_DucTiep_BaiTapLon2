package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.myapplication.Fragment.VideoCategoryAdapter;
import com.example.myapplication.Fragment.VideoCategoryItem;
import com.example.myapplication.Fragment.VideoHot;
import com.example.myapplication.data.Main2Activity;
import com.google.android.material.tabs.TabLayout;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {



    String getCategory = "https://demo5639557.mockable.io/getCategory";

    String getItemCategory = "https://demo5639557.mockable.io/getItemCategory";

    private List<Fragment> fragmentList = new ArrayList<>();

    List<Category> categoryList = new ArrayList<>();
    FlowingDrawer mDrawer;
    static String result="";

    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        tv = findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Main2Activity.class);
                startActivity(intent);
            }
        });

        new getDataCategory(getCategory).execute();


        mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
                if (newState == ElasticDrawer.STATE_CLOSED) {
                    Log.i("MainActivity", "Drawer STATE_CLOSED");
                }
            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
                Log.i("MainActivity", "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
            }
        });
    }

    private void getFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    class getDataCategory extends AsyncTask< Void, Void, Void> {

        String urlApi;
        String result = "";

        public getDataCategory(String urlApi) {
            this.urlApi = urlApi;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(urlApi);
                URLConnection connection = url.openConnection();
                InputStream inputStream = connection.getInputStream();
                int byteChar;

                while ((byteChar = inputStream.read()) != -1) {
                    result += (char) byteChar;

                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getCategory(result);
            ViewPager viewPager = findViewById(R.id.view_pager);
            viewPager.setOffscreenPageLimit(4);
            viewPager.setPageTransformer(true, new DepthPageTransformer());
            PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());


            pagerAdapter.add(VideoHot.newInstance(), "HOT VIDEOS");


            for ( int i = 0 ; i<categoryList.size();i++){
//                VideoCategoryItem videoCategoryItem = new VideoCategoryItem();
//                pagerAdapter.add(videoCategoryItem ,categoryList.get(i).getCategory());
//                pagerAdapter.add(VideoCategoryItem.newInstance() ,categoryList.get(i).getCategory());
                pagerAdapter.add(new VideoCategoryItem(categoryList.get(i)) ,categoryList.get(i).getCategory());



            }




            viewPager.setAdapter(pagerAdapter);
            TabLayout tabLayout = findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);



        }
    }

    void getCategory(String string) {


        try {
            JSONArray jsonArray = new JSONArray(string);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Category category = new Category();

                category.setCategory(jsonObject.getString("title"));
                category.setThumb(jsonObject.getString("thumb"));
                categoryList.add(category);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}

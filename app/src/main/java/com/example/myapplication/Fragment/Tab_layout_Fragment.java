package com.example.myapplication.Fragment;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Category;
import com.example.myapplication.DepthPageTransformer;
import com.example.myapplication.Interface.IonClickCategory;
import com.example.myapplication.PagerAdapter;
import com.example.myapplication.R;
import com.example.myapplication.VideoItem;
import com.example.myapplication.data.Favorites_Video;
import com.example.myapplication.databinding.TabLayoutMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class Tab_layout_Fragment extends Fragment {


    String getCategory = "https://demo5639557.mockable.io/getCategory";

    String getItemCategory = "https://demo5639557.mockable.io/getItemCategory";
    String getweather = "http://api.openweathermap.org/data/2.5/weather?q=hanoi&appid=91722eb7c3eeec798193574b674ff46a&units=metric";


    private List<Fragment> fragmentList = new ArrayList<>();

    List<Category> categoryListList = new ArrayList<>();
    FlowingDrawer mDrawer;
    List<VideoItem> list_favorite = new ArrayList<>();
    List<VideoItem> list_recent = new ArrayList<>();
    Favorites_Video favorites_video;
    VideoAdapter videoAdapterRecent, videoAdapterFavorite;
    SharedPreferences sharedPreferences;

    TabLayoutMainBinding binding;
    ViewPager viewPager;

    public static Tab_layout_Fragment newInstance() {

        Bundle args = new Bundle();

        Tab_layout_Fragment fragment = new Tab_layout_Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    public Tab_layout_Fragment() {
    }

    public Tab_layout_Fragment(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.tab_layout_main, container, false);


        new getDataCategory(getCategory).execute();




        return binding.getRoot();


    }



    class getDataCategory extends AsyncTask<Void, Void, Void> {

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
            final PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager());
            pagerAdapter.setIonClickCategory(new IonClickCategory() {
                @Override
                public void onClickItem(Category category) {
                    pagerAdapter.open(new CTGR_Fragment(category,getContext()), category.getCategory());
                }
            });


            pagerAdapter.add(VideoHot.newInstance(), "HOT VIDEOS");

            pagerAdapter.add(new Category_list(categoryListList), "CATEGORY");



            binding.viewPager.setAdapter(pagerAdapter);
            binding.viewPager.setPageTransformer(true, new DepthPageTransformer());

            binding.tabs.setupWithViewPager(binding.viewPager);
            binding.tabs.setTabMode(TabLayout.MODE_FIXED);
            binding.tabs.setTabGravity(TabLayout.GRAVITY_FILL);


        }


        void getCategory(String string) {
            categoryListList.clear();

            try {
                JSONArray jsonArray = new JSONArray(string);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Category categoryList = new Category();

                    categoryList.setCategory(jsonObject.getString("title"));
                    categoryList.setThumb(jsonObject.getString("thumb"));
                    categoryListList.add(categoryList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }




}
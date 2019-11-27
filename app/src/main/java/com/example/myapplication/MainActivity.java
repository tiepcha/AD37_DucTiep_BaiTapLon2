package com.example.myapplication;


import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Fragment.CTGR_Fragment;
import com.example.myapplication.Fragment.Category_list;
import com.example.myapplication.Fragment.Tab_layout_Fragment;
import com.example.myapplication.Fragment.VideoAdapter;
import com.example.myapplication.Fragment.VideoHot;
import com.example.myapplication.Fragment.VideoHotAdapter;
import com.example.myapplication.Interface.IonClickCategory;
import com.example.myapplication.Interface.IonClickItem;
import com.example.myapplication.data.Favorites_Video;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import static com.example.myapplication.R.color.black60;
import static com.example.myapplication.R.color.brown;
import static com.example.myapplication.R.color.white;
import static com.example.myapplication.R.color.white30;

public class MainActivity extends AppCompatActivity implements Serializable {


    private static final String TAG = "tag";
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
    String weather, icon;
    int tempmain, temp_max, temp_min;
    private ViewPager viewpager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

//      getFragment(Tab_layout_Fragment.newInstance(),viewpager);
        getFragment(new Tab_layout_Fragment(viewpager));
//
        sharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        checkLanguage();

//        new getDataCategory(getCategory).execute();

        new getWeather(getweather, binding.tvweather, binding.tvtemp, binding.tvtempmax, binding.tvtempmin, binding.imgWeather, binding.imgEmpty).execute();


        final TranslateAnimation animate_show = new TranslateAnimation(-500, 0, 0, 0);
        animate_show.setDuration(500);
        animate_show.setFillAfter(false);

        final TranslateAnimation animate_hide = new TranslateAnimation(0, -500, 0, 0);
        animate_hide.setDuration(500);
        animate_hide.setFillAfter(false);


        mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
                favorites_video.checkList(list_recent, list_favorite);


                videoAdapterFavorite.notifyDataSetChanged();
                videoAdapterRecent.notifyDataSetChanged();
                if (newState == ElasticDrawer.STATE_CLOSED) {
                    Log.i("MainActivity", "Drawer STATE_CLOSED");

                }
            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {

                Log.i("MainActivity", "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
            }
        });

        int RED = 0xffFF0000;
        int BLUE = 0xff0000FF;
        ValueAnimator colorAnim = ObjectAnimator.ofInt(binding.appname, "textColor", RED, BLUE);
        colorAnim.setDuration(2000);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(ValueAnimator.INFINITE);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();

        favorites_video = new Favorites_Video(getBaseContext());
        favorites_video.checkList(list_recent, list_favorite);

//tắt cuộn chồng nhau       ---------------------------------------------------------------------------------------------------
        binding.rcvFavoriteVideos.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                view.getParent().requestDisallowInterceptTouchEvent(true);

                return false;
            }
        });

        binding.rcvRecentVideos.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                view.getParent().requestDisallowInterceptTouchEvent(true);

                return false;
            }
        });


//tạo list yêu thích
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false);
        videoAdapterFavorite = new VideoAdapter(list_favorite, getBaseContext());
        videoAdapterFavorite.setIonClickItem(new IonClickItem() {
            @Override
            public void onClickName(String name, int video_position, int position, View v) {
                Intent intent = new Intent(v.getContext(), Playing.class);
                intent.putExtra("mp4", name);
                intent.putExtra("list", (Serializable) list_favorite);
                intent.putExtra("position", position);
                intent.putExtra("video_position", video_position);
                startActivity(intent);
            }

            @Override
            public void onClickAva() {

            }

            @Override
            public void onPosition(VideoHotAdapter adapter, int position, int i) {

            }
        });
        binding.rcvFavoriteVideos.setLayoutManager(layoutManager);
        binding.rcvFavoriteVideos.setAdapter(videoAdapterFavorite);

//tạo list gần đây
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false);
        videoAdapterRecent = new VideoAdapter(list_recent, getBaseContext());
        videoAdapterRecent.setIonClickItem(new IonClickItem() {
            @Override
            public void onClickName(String name, int video_position, int position, View v) {
                Intent intent = new Intent(v.getContext(), Playing.class);
                intent.putExtra("mp4", name);
                intent.putExtra("list", (Serializable) list_recent);
                intent.putExtra("position", position);
                intent.putExtra("video_position", video_position);
                startActivity(intent);
            }

            @Override
            public void onClickAva() {

            }

            @Override
            public void onPosition(VideoHotAdapter adapter, int position, int i) {

            }
        });
        binding.rcvRecentVideos.setLayoutManager(layoutManager2);
        binding.rcvRecentVideos.setAdapter(videoAdapterRecent);


//khi ấn đăng nhập/đăng kí---------------------------------------------------------------------------------------------------
        binding.tvSignin.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                binding.rlSignup.setVisibility(View.GONE);
                if (binding.rlSignin.getVisibility() == View.GONE) {
                    binding.rlSignin.setVisibility(View.VISIBLE);
                    binding.rlSignin.startAnimation(animate_show);
                    binding.tvSignin.setTextColor(getResources().getColor(white));
                    binding.tvSignup.setTextColor(getResources().getColor(white30));


                } else {
                    binding.rlSignin.startAnimation(animate_hide);
                    binding.rlSignin.setVisibility(View.GONE);
                    binding.tvSignin.setTextColor(getResources().getColor(white30));//ll,l,ld,lds
                }
            }
        });

        binding.tvSignup.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                binding.rlSignin.setVisibility(View.GONE);
                if (binding.rlSignup.getVisibility() == View.GONE) {

                    binding.rlSignup.startAnimation(animate_show);
                    binding.rlSignup.setVisibility(View.VISIBLE);
                    binding.tvSignup.setTextColor(getResources().getColor(white));
                    binding.tvSignin.setTextColor(getResources().getColor(white30));
                } else {
                    binding.rlSignup.startAnimation(animate_hide);
                    binding.rlSignup.setVisibility(View.GONE);
                    binding.tvSignup.setTextColor(getResources().getColor(white30));
                }
            }
        });

        //mở list yêu thích        ---------------------------------------------------------------------------------------------------
        binding.tvfavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list_favorite.isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.novideos, Toast.LENGTH_SHORT).show();
                } else {
                    if (binding.rcvFavoriteVideos.getVisibility() == View.GONE) {
                        binding.idContainerMenu.setBackgroundColor(ContextCompat.getColor(getBaseContext(), brown));
                        binding.rcvFavoriteVideos.startAnimation(animate_show);

                        binding.rcvFavoriteVideos.setVisibility(View.VISIBLE);

                    } else {
                        binding.idContainerMenu.setBackgroundColor(ContextCompat.getColor(getBaseContext(), black60));
                        binding.rcvFavoriteVideos.startAnimation(animate_hide);
                        binding.rcvFavoriteVideos.setVisibility(View.GONE);
                    }
                }
            }
        });
        binding.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list_favorite.isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.novideos, Toast.LENGTH_SHORT).show();
                } else {
                    if (binding.rcvFavoriteVideos.getVisibility() == View.GONE) {
                        binding.idContainerMenu.setBackgroundColor(ContextCompat.getColor(getBaseContext(), brown));
                        binding.rcvFavoriteVideos.startAnimation(animate_show);

                        binding.rcvFavoriteVideos.setVisibility(View.VISIBLE);

                    } else {
                        binding.idContainerMenu.setBackgroundColor(ContextCompat.getColor(getBaseContext(), black60));
                        binding.rcvFavoriteVideos.startAnimation(animate_hide);
                        binding.rcvFavoriteVideos.setVisibility(View.GONE);
                    }
                }

            }
        });

//mở list gần đây       ---------------------------------------------------------------------------------------------------
        binding.tvrecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list_recent.isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.novideos, Toast.LENGTH_SHORT).show();
                } else {
                    if (binding.rcvRecentVideos.getVisibility() == View.GONE) {
                        binding.idContainerMenu2.setBackgroundColor(ContextCompat.getColor(getBaseContext(), brown));
                        binding.rcvRecentVideos.startAnimation(animate_show);
                        binding.rcvRecentVideos.setVisibility(View.VISIBLE);


                    } else {
                        binding.idContainerMenu2.setBackgroundColor(ContextCompat.getColor(getBaseContext(), black60));

                        binding.rcvRecentVideos.startAnimation(animate_hide);
                        binding.rcvRecentVideos.setVisibility(View.GONE);
                    }
                }

            }
        });
        binding.imgRecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list_recent.isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.novideos, Toast.LENGTH_SHORT).show();
                } else {
                    if (binding.rcvRecentVideos.getVisibility() == View.GONE) {
                        binding.idContainerMenu2.setBackgroundColor(ContextCompat.getColor(getBaseContext(), brown));
                        binding.rcvRecentVideos.startAnimation(animate_show);
                        binding.rcvRecentVideos.setVisibility(View.VISIBLE);


                    } else {
                        binding.idContainerMenu2.setBackgroundColor(ContextCompat.getColor(getBaseContext(), black60));

                        binding.rcvRecentVideos.startAnimation(animate_hide);
                        binding.rcvRecentVideos.setVisibility(View.GONE);
                    }
                }

            }
        });


//mở setting       ---------------------------------------------------------------------------------------------------
        binding.tvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.rlSettings.getVisibility() == View.GONE) {
                    binding.settings.setBackgroundColor(ContextCompat.getColor(getBaseContext(), brown));

                    binding.rlSettings.setVisibility(View.VISIBLE);

                    binding.rlSettings.startAnimation(animate_show);
                } else {
                    binding.settings.setBackgroundColor(ContextCompat.getColor(getBaseContext(), black60));
                    binding.rlSettings.startAnimation(animate_hide);
                    binding.rlSettings.setVisibility(View.GONE);
                }
            }
        });


        binding.imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.rlSettings.getVisibility() == View.GONE) {
                    binding.settings.setBackgroundColor(ContextCompat.getColor(getBaseContext(), brown));
                    binding.rlSettings.setVisibility(View.VISIBLE);
                    binding.rlSettings.startAnimation(animate_show);
                } else {
                    binding.settings.setBackgroundColor(ContextCompat.getColor(getBaseContext(), black60));
                    binding.rlSettings.startAnimation(animate_hide);
                    binding.rlSettings.setVisibility(View.GONE);
                }
            }
        });

        binding.imgAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.aboutInfo.getVisibility() == View.GONE) {
                    binding.rlAbout.setBackgroundColor(ContextCompat.getColor(getBaseContext(), brown));
                    binding.aboutInfo.setVisibility(View.VISIBLE);
                    binding.aboutInfo.startAnimation(animate_show);
                } else {
                    binding.rlAbout.setBackgroundColor(ContextCompat.getColor(getBaseContext(), black60));
                    binding.aboutInfo.startAnimation(animate_hide);
                    binding.aboutInfo.setVisibility(View.GONE);
                }

            }
        });
        binding.tvabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.aboutInfo.getVisibility() == View.GONE) {
                    binding.rlAbout.setBackgroundColor(ContextCompat.getColor(getBaseContext(), brown));
                    binding.aboutInfo.setVisibility(View.VISIBLE);
                    binding.aboutInfo.startAnimation(animate_show);
                } else {
                    binding.rlAbout.setBackgroundColor(ContextCompat.getColor(getBaseContext(), black60));
                    binding.aboutInfo.startAnimation(animate_hide);
                    binding.aboutInfo.setVisibility(View.GONE);
                }

            }
        });


        binding.language2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(getBaseContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.language_menu, popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        sharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        String l = sharedPreferences.getString("language", "english");
                        switch (menuItem.getItemId()) {
                            case R.id.english:
                                if (l != "english") {
                                    Locale locale = new Locale("en");
                                    Locale.setDefault(locale);
                                    Configuration config = new Configuration();
                                    config.locale = locale;
                                    getBaseContext().getResources()
                                            .updateConfiguration(
                                                    config,
                                                    getBaseContext().getResources()
                                                            .getDisplayMetrics());
                                    Toast.makeText(getBaseContext(), "Chuyển sang Tiếng Anh",
                                            Toast.LENGTH_LONG).show();
                                    editor.putString("language", "english");
                                    editor.apply();
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(new Intent(getBaseContext(),
                                            MainActivity.class));
                                }
                                break;

                            case R.id.vietnam:
                                if (l != "vietnam") {
                                    Locale locale2 = new Locale("vi");
                                    Locale.setDefault(locale2);
                                    Configuration config2 = new Configuration();
                                    config2.locale = locale2;
                                    getBaseContext().getResources()
                                            .updateConfiguration(
                                                    config2,
                                                    getBaseContext().getResources()
                                                            .getDisplayMetrics());
                                    Toast.makeText(getBaseContext(), "Switch to Vietnamese",
                                            Toast.LENGTH_LONG).show();
                                    binding.language2.setText("Tiếng Việt");
                                    editor.putString("language", "vietnam");
                                    editor.apply();

                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(new Intent(getBaseContext(),
                                            MainActivity.class));
                                }
                                break;

                        }
                        return false;
                    }
                });
            }
        });
    }


    private void checkLanguage() {
        String l = sharedPreferences.getString("language", "english");
        if (l.equals("vietnam")) {
            Locale.setDefault(new Locale("vi"));
        }
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

            final ViewPager viewPager = findViewById(R.id.view_pager);
//            viewPager.setOffscreenPageLimit(8);
            viewPager.setPageTransformer(true, new DepthPageTransformer());
            final PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
            pagerAdapter.setIonClickCategory(new IonClickCategory() {
                @Override
                public void onClickItem(Category category) {
                    pagerAdapter.open(new CTGR_Fragment(category,getBaseContext()), category.getCategory());
                }
            });


            pagerAdapter.add(VideoHot.newInstance(), "HOT VIDEOS");


            pagerAdapter.add(new Category_list(categoryListList), "CATEGORY");


            viewPager.setAdapter(pagerAdapter);


            final TabLayout tabLayout = findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        }
    }

    void getCategory(String string) {


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


    class getWeather extends AsyncTask<Void, Void, Void> {

        String urlApi;
        String result = "";
        TextView tv_weather, tv_temp, tv_temp_max, tv_temp_min;
        ImageView img_weather;
        RelativeLayout img_empty;

        public getWeather(String urlApi, TextView tv_weather, TextView tv_temp, TextView tv_temp_max, TextView tv_temp_min, ImageView img_weather, RelativeLayout img_empty) {
            this.urlApi = urlApi;
            this.tv_weather = tv_weather;
            this.tv_temp = tv_temp;
            this.tv_temp_max = tv_temp_max;
            this.tv_temp_min = tv_temp_min;
            this.img_weather = img_weather;
            this.img_empty = img_empty;
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
            getHanoiweather(result, tv_weather, tv_temp, tv_temp_max, tv_temp_min, img_weather, img_empty);

        }
    }

    void getHanoiweather(String string, TextView tvweather, TextView tvtemp, TextView tempmax, TextView tempmin, ImageView img_weather, RelativeLayout imgEmpty) {


        try {
            JSONObject jsonObject = new JSONObject(string);

            JSONArray w = jsonObject.getJSONArray("weather");
            JSONObject w2 = w.getJSONObject(0);
            weather = w2.getString("main");
            icon = w2.getString("icon");


            JSONObject temp = jsonObject.getJSONObject("main");
            tempmain = temp.getInt("temp");
            temp_max = temp.getInt("temp_max");
            temp_min = temp.getInt("temp_min");


            tvweather.setText(" " + weather);
            tvtemp.setText(String.valueOf(tempmain) + "°C");
            tempmax.setText(String.valueOf(temp_max) + "°C");
            tempmin.setText(String.valueOf(temp_min) + "°C");
            Picasso.get().load("http://openweathermap.org/img/wn/" + icon + "@2x.png").into(img_weather);

            switch (weather) {
                case "Clouds":
                    imgEmpty.setBackgroundResource(R.drawable.cloud);
                    tvweather.setText(R.string.cloud);
                    break;
                case "Clear":
                    imgEmpty.setBackgroundResource(R.drawable.clear);
                    tvweather.setText(R.string.clear);
                    break;
                case "Rain":
                    imgEmpty.setBackgroundResource(R.drawable.rain);
                    tvweather.setText(R.string.rain);
                    break;
                case "Drizzle":
                    imgEmpty.setBackgroundResource(R.drawable.drizzle);
                    tvweather.setText(R.string.drizzle);
                    break;
                case "Thunderstorm":
                    imgEmpty.setBackgroundResource(R.drawable.thunder);
                    tvweather.setText(R.string.thunder);
                    break;


            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getFragment(Fragment fragment) {
        try {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment,"tabs")
                    .addToBackStack("tabs")
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getFragment: " + e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {

//        if (getSupportFragmentManager().getFragments().get(0).isMenuVisible()) {

//
//        } else if (getSupportFragmentManager().getFragments().get(1).isMenuVisible()){
//            Toast.makeText(this, "back", Toast.LENGTH_SHORT).show();
//        }


//
//       Tab_layout_Fragment tab = (Tab_layout_Fragment) getSupportFragmentManager().findFragmentByTag("tabs");
//        if (tab != null && tab.isVisible()) {
//            // add your code here
//            Toast.makeText(this, "tab", Toast.LENGTH_SHORT).show();

//        }else {
//            Toast.makeText(this, "listitem", Toast.LENGTH_SHORT).show();
//            super.onBackPressed();
//        }

        // Create the AlertDialog object and return it
//        super.onBackPressed();
        // This will pop the Activity from the stack.
        int count = getSupportFragmentManager().getBackStackEntryCount();

//
        if (count == 1 || count == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.out)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
//                            finishAndRemoveTask();

                            System.exit(0);
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    }).create().show();

            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }




    }

    public Tab_layout_Fragment getActiveFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return (Tab_layout_Fragment) getSupportFragmentManager().findFragmentByTag(tag);
    }
}








package com.example.myapplication.data;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;

import com.example.myapplication.R;

public class Main2Activity extends AppCompatActivity {

    TextView tv,tv2,tv3;
    MediaController mc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        tv = findViewById(R.id.tv);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tv2.getVisibility()==View.GONE){
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
            }
                else {
                    tv2.setVisibility(View.GONE);
                    tv3.setVisibility(View.GONE);
                }

                }
        });



    }
}

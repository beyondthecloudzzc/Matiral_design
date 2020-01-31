package com.example.matiral_design;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class check extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        Button now = (Button) findViewById(R.id.current);
        Button ALL = (Button)findViewById(R.id.all_count);
        now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //当前签到查询
            }
        });
        ALL .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //全部签到查询
            }
        });
    }
}

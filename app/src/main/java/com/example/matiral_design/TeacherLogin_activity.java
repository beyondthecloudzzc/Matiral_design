package com.example.matiral_design;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class TeacherLogin_activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login_activity);
        Button button = findViewById(R.id.zhuce);//跳转注册按钮
        Button button1 = findViewById(R.id.denglu);//登录按钮
        button.setOnClickListener(new View.OnClickListener() {//跳转注册界面
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TeacherLogin_activity.this,Teacher_res.class);
                startActivity(intent);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//在此更改判断登录的语句
                Intent intent = new Intent(TeacherLogin_activity.this,TeacherMainActivity.class);
                startActivity(intent);
            }
        });
    }


}

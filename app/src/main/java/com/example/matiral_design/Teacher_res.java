package com.example.matiral_design;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Teacher_res extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_res);
        Button button3=findViewById(R.id.zhuce1);//注册按钮
        Button button4=findViewById(R.id.fanhuidl);//返回登录界面按钮
        //还需添加对再次确认密码才能完成注册的相关语句
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //在此添加把用户数据添加到数据库的操作
                Toast.makeText(Teacher_res.this,"注册成功",Toast.LENGTH_SHORT).show();
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Teacher_res.this, TeacherLogin_activity.class);
                startActivity(intent);
            }
        });
    }
}

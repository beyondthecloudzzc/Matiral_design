package com.example.matiral_design;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class check extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        Button now = (Button) findViewById(R.id.current);
        Button ALL = (Button)findViewById(R.id.all_count);

        String check="1";

        CheckRequest(check);
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


    public   void CheckRequest(final String check) {
        //请求地址
        String url = "http://271643ug95.wicp.vip/MyFirstWebApp/CheckServlet";    //注①
        String tag = "Check";    //注②


        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");  //注③
                            String result = jsonObject.getString("Result");  //注④
                            String student = jsonObject.getString("Student");


                            TextView tv = findViewById(R.id.stu_not_here);

                            tv.setText(student);


                            System.out.println(student);

                            if (result.equals("success")) {  //注⑤
                                //做自己的注册成功操作，如页面跳转
                                Toast.makeText(check.this, "你好!正在查询未到的学生"+student, Toast.LENGTH_LONG).show();

                            } else {
                                //做自己的登录失败操作，如Toast提示
                                Toast.makeText(check.this, "查询失败", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(check.this, "查询失败，无网络连接", Toast.LENGTH_LONG).show();
                            //Toast.makeText(this,"网络异常",Toast.LENGTH_LONG).show();
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(check.this, "稍后再试", Toast.LENGTH_LONG).show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Check", check);  //注⑥

                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
}

package com.example.matiral_design;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
public class Login_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        Button login = (Button) findViewById(R.id.login);
        Button register = (Button) findViewById(R.id.button2);
        //登录按钮触发事件
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et_userName=(EditText) findViewById(R.id.username);
                EditText et_passWord=(EditText) findViewById(R.id.password);
                String useName=et_userName.getText().toString();
                String passWord=et_passWord.getText().toString();
                LoginRequest(useName,passWord);//请求登录
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_activity.this,res.class);
                startActivity(intent);
            }
        });
    }
//客户端请求代码
    public  void LoginRequest(final String accountNumber, final String password) {
        //请求地址
        String url = "http://271643ug95.wicp.vip/MyFirstWebApp/LoginServlet";
        String tag = "Login";

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
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                            String result = jsonObject.getString("Result");
                            if (result.equals("success")) {
                                Intent intent = new Intent(Login_activity.this,MainActivity.class);
                                //提示成功之后进行跳转
                                Toast.makeText(Login_activity.this, "你好!登录成功", Toast.LENGTH_LONG).show();
                                startActivity(intent);
                            } else {
                                //登录失败操作Toast提示，不跳转
                                Toast.makeText(Login_activity.this, "登录失败", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            //请求异常Toast提示（“无网络连接”等）
                            Toast.makeText(Login_activity.this, "无网络连接", Toast.LENGTH_LONG).show();
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //响应错误操作Toast提示（“请稍后重试”等）
                Toast.makeText(Login_activity.this, "稍后再试", Toast.LENGTH_LONG).show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("AccountNumber", accountNumber);
                params.put("Password", password);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
}

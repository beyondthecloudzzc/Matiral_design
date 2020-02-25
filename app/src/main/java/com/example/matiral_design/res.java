package com.example.matiral_design;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class res extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res);
        Button res = (Button)findViewById(R.id.res);
        Button re  = (Button) findViewById(R.id.back);
        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注册按钮点击逻辑，大家可以在这里执行数据库写入操作
                EditText et_userName=(EditText)findViewById(R.id.editText);
                EditText et_passWord=(EditText)findViewById(R.id.editText2);
                String userName=et_userName.getText().toString();
                String passWord=et_passWord.getText().toString();
                RegisterRequest(userName,passWord);
            }
        });
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(res.this,Login_activity.class);
                startActivity(intent);
            }
        });
    }

//注册的servlet
    public  void RegisterRequest(final String accountNumber, final String password) {
        //请求地址
        String url = "http://271643ug95.wicp.vip/MyFirstWebApp/RegisterServlet";
        String tag = "Register";

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
                                //注册成功操作，如页面跳转
                                Toast.makeText(res.this, "你好!已经注册", Toast.LENGTH_LONG).show();
                            } else {
                                //注册失败操作，如Toast提示
                                Toast.makeText(res.this, "注册失败", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            //请求异常操作Toast提示（“无网络连接”等）
                            Toast.makeText(res.this, "无网络连接", Toast.LENGTH_LONG).show();
                            //Toast.makeText(this,"网络异常",Toast.LENGTH_LONG).show();
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(res.this, "稍后再试", Toast.LENGTH_LONG).show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("AccountNumber", accountNumber);  //注⑥
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

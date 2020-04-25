package com.example.matiral_design;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

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
import java.util.Set;

public class Edit_message extends AppCompatActivity {
    //提交测试
    /*
    @张纯
    2020.2.25
    为了获得要上传的数据

     */
    String college,grade,major,class_num,name,stu_num;//学院年级专业班级姓名学号

    static String stupic_str;//识别码
    static String stupic_name;
    static String str[] = new String[3];//该数组存储固定格式四位为年级+专业号+班级，如17 0820 09

    static final Map<String,String> hmap= new HashMap<String, String>(){//打表存储信息，后续的可以在这里添加
        {
            put("2016","16");
            put("2017","17");
            put("2018","18");
            put("2019","19");
            put("计算机科学与技术","0810");
            put("电子信息科学与技术","0820");
            put("信息安全","0830");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_message);

        final Spinner sp_college = findViewById(R.id.academy);
        final  Spinner sp_grade = findViewById(R.id.grade);
        final Spinner sp_major = findViewById(R.id.prof);
        final Spinner sp_class_num = findViewById(R.id.class_num);
        final EditText et_name = findViewById(R.id.name);
        final EditText et_stu_num = findViewById(R.id.stu_num);
        Button save = findViewById(R.id.save);



        //String college,grade,major,class_num,name,stu_num;//学院年级专业班级姓名学号

        //2020.2.25@张纯

        //spinner全员监听器
        sp_college.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                college = (String) sp_college.getSelectedItem();
                //  stupic_path = "//"+college;//存个学院，这里不能+=不然路径前面就多了个空null
                //这里暂时不做要求

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_grade.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                grade = (String) sp_grade.getSelectedItem();
                str[0] = stupic_path_to_num(grade);//年级放在一号位

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_major.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                major = (String)sp_major.getSelectedItem();
                str[1] = stupic_path_to_num(major);//得到该专业对应的数字标记,放在二号位

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_class_num.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                class_num = (String)sp_class_num.getSelectedItem();
                str[2] = class_num;//班级不用转换数字标记，直接放在三号位

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_name.getText().toString();//获取姓名和学号
                et_name.setText(name);
                stu_num = et_stu_num.getText().toString();//没办法这玩意不弄监听器了，上传前取一下就行
                et_stu_num.setText(stu_num);

                stupic_str = str[0]+str[1]+str[2]; //合成一个识别码

                System.out.println(stupic_str);
                // StuRequest(stu_num,name,college,grade,major,class_num);

                stupic_name = stu_num+name;//存一下这个学号+姓名作为图片名字

                System.out.println(stupic_name);
                System.out.println("this is a test_out from Edit_message");
                Toast.makeText(Edit_message.this,"保存成功",Toast.LENGTH_SHORT).show();
            }
        });et_name.setText(name);et_stu_num.setText(stu_num);


    }

    public  String stupic_path_to_num(String path) //这个函数用来把下啦文本框选择的选项转换为数字
    {
        String num = hmap.get(path);
        return num;
    }

    //上传学生信息的servlet
  /*  public   void StuRequest(final String stu_num, final String name, final String college ,final String grade, final String major ,final String class_num ) {
        //请求地址
        String url = "http://271643ug95.wicp.vip/MyFirstWebApp/StuServlet";
        String tag = "StuRequest";

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
                                //上传成功的提示
                                Toast.makeText(Edit_message.this, "你好!已经上传信息", Toast.LENGTH_LONG).show();
                            } else {
                                //上传失败
                                Toast.makeText(Edit_message.this, "上传失败", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            //请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(Edit_message.this, "上传失败，无网络连接", Toast.LENGTH_LONG).show();
                            //Toast.makeText(this,"网络异常",Toast.LENGTH_LONG).show();
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(Edit_message.this, "稍后再试", Toast.LENGTH_LONG).show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Name", name);  //注⑥
                params.put("Stu_num", stu_num);
                params.put("College",college);
                params.put("Grade",grade);
                params.put("Major",major);
                params.put("Class_num",class_num);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }
    */
}

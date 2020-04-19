package com.example.matiral_design;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;

public class activity_teacher_check extends AppCompatActivity {
    String college,grade,major,class_num;
    static String stupic_path;
    static String stupic_name;
    private Spinner insspinner=null;
    private Spinner graspinner=null;
    private Spinner prospinner=null;
    private Spinner claspinner=null;
    private Button up_btn=null;
    ArrayAdapter<String> insAdapter = null;
    ArrayAdapter<String> graAdapter = null;
    ArrayAdapter<String> proAdapter = null;
    ArrayAdapter<String> claAdapter=null;
    static int insPosition = 0;
    static int graPosition=0;
    static int proPosition=0;
    static int claPosition=0;
    private String upload_str;
    HashMap<String, String> hm = new HashMap<String, String>();



    private String[] ins = new String[] {"计算机科学与技术学院"};
    private String[][] gra=new String[][]{
            {"16","17","18","19"}
    };
    private String[][][] prof=new String[][][]{
            {{"计算机科学与技术","电子信息科学与技术","信息安全","网络工程"},
                    {"计算机科学与技术","电子信息科学与技术","信息安全"},
                    {"计算机科学与技术","电子信息科学与技术","信息安全","数据科学与大数据技术专业"},
                    {"计算机类"}}
    };
    private String[][][][] cla=new String[][][][]{
            {
                    {{"01","02","03","04","05","06","07"}, {"01","02","03","04"},{"01","02","03","04"},{"01"}},
                    {{"01","02","03","04","05","06"}, {"01","02","03","04"},{"01","02","03","04"}},
                    {{"01","02","03","04"}, {"01","02","03","04"},{"01","02","03","04"},{"01","02"}},
                    {{"01","02","03","04","05","06","07","08","09","10","11","12","13"}}
            }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_check);
        hm.put("计算机类", "0801");
        hm.put("计算机科学与技术", "0810");
        hm.put("电子信息科学与技术", "0820");
        hm.put("网络工程", "0830");
        hm.put("信息安全", "0840");
        hm.put("数据科学与大数据技术专业", "0850");
        up_btn=findViewById(R.id.up_btn);
        up_btn.setOnClickListener(new Buttonlistener());
        setSpinner();
    }
    private class Buttonlistener implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.up_btn:
                    //***************************************************************************
                    //这里写上传的代码,上传图片与识别码，识别码就是upload_str,它的构成为GGPPPPCC,GG年级,PPPP专业号,CC班级号，比如 17081004 代表17级计算机科学与技术4班

                    Toast.makeText(activity_teacher_check.this,upload_str,Toast.LENGTH_SHORT ).show();
                    break;


                case R.id.check_btn:
                //反馈结果的，显示在下面的textview里
                    break;
                default:
                    break;
            }
        }
    }
    private void setSpinner(){
        insspinner=findViewById(R.id.spin_institue);
        graspinner=findViewById(R.id.spin_grade);
        prospinner=findViewById(R.id.spin_prof);
        claspinner=findViewById(R.id.spin_class);

        insAdapter = new ArrayAdapter<String>(activity_teacher_check.this, android.R.layout.simple_spinner_item, ins);
        insspinner.setAdapter(insAdapter);
        insspinner.setSelection(0,true);

        graAdapter = new ArrayAdapter<String>(activity_teacher_check.this, android.R.layout.simple_spinner_item, gra[0]);
        graspinner.setAdapter(graAdapter);
        graspinner.setSelection(0,true);

        proAdapter = new ArrayAdapter<String>(activity_teacher_check.this, android.R.layout.simple_spinner_item, prof[0][0]);
        prospinner.setAdapter(proAdapter);
        prospinner.setSelection(0,true);

        claAdapter = new ArrayAdapter<String>(activity_teacher_check.this, android.R.layout.simple_spinner_item, cla[0][0][0]);
        claspinner.setAdapter(claAdapter);
        claspinner.setSelection(0,true);

        insspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                //position为当前省级选中的值的序号

                //将地级适配器的值改变为city[position]中的值
                graAdapter = new ArrayAdapter<String>(
                        activity_teacher_check.this, android.R.layout.simple_spinner_item, gra[position]);
                // 设置二级下拉列表的选项内容适配器
                graspinner.setAdapter(graAdapter);
                insPosition = position;    //记录当前省级序号，留给下面修改县级适配器时用
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }});


        //地级下拉监听
        graspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3)
            {
                proAdapter = new ArrayAdapter<String>(activity_teacher_check.this,
                        android.R.layout.simple_spinner_item,prof[insPosition][position]);
                prospinner.setAdapter(proAdapter);
                graPosition=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }});

        prospinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3)
            {
                claAdapter = new ArrayAdapter<String>(activity_teacher_check.this,
                        android.R.layout.simple_spinner_item, cla[insPosition][graPosition][position]);
                claspinner.setAdapter(claAdapter);
                proPosition=position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }});

        claspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3)
            {
                claPosition=position;
                String str=ins[insPosition]+gra[insPosition][graPosition]+prof[insPosition][graPosition][proPosition]+cla[insPosition][graPosition][proPosition][claPosition];
                upload_str=gra[insPosition][graPosition]+hm.get(prof[insPosition][graPosition][proPosition])+cla[insPosition][graPosition][proPosition][claPosition];
                Toast.makeText(activity_teacher_check.this,str,Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }});
    }
}

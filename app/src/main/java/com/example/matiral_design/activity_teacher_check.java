package com.example.matiral_design;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

    private GridView gridView1;              //网格显示缩略图
    private Button buttonPublish;            //发布按钮
    private final int IMAGE_OPEN = 1;        //打开图片标记
    private final int PHOTO_REQUEST_GALLERY=2;
    private final int  PHOTO_REQUEST_CAREMA=3;
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private File tempFile;
    private String pathImage;                //选择图片路径
    private Bitmap bmp;                      //导入临时图片
    private Bitmap upload_bp;
    private String upload_bp_str;
    private String upload_name;
    private ArrayList<HashMap<String, Bitmap>> imageItem;
    private SimpleAdapter simpleAdapter;     //适配器
    public String sdCardDir = Environment.getExternalStorageDirectory() + "/DCIM/Camara/";

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

        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_teacher_check);
        gridView1 = (GridView) findViewById(R.id.gridView1);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.add);
        imageItem = new ArrayList<HashMap<String, Bitmap>>();
        HashMap<String, Bitmap> map = new HashMap<String, Bitmap>();
        map.put("itemImage", bmp);
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(this,
                imageItem, R.layout.griditem_addpic,
                new String[] { "itemImage"}, new int[] { R.id.imageView1});
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                // TODO Auto-generated method stub
                if(view instanceof ImageView && data instanceof Bitmap){
                    ImageView i = (ImageView)view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        gridView1.setAdapter(simpleAdapter);
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                if( imageItem.size() == 4) { //第一张为默认图片
                    Toast.makeText(activity_teacher_check.this, "图片数3张已满，可以上传了", Toast.LENGTH_SHORT).show();
                }
                else if(position == 0) { //点击图片位置为+ 0对应0张图片
                    Toast.makeText(activity_teacher_check.this, "添加图片", Toast.LENGTH_SHORT).show();
                    addpic();
                }
                else {
                    dialog(position);
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                Uri uri = data.getData();
                Log.e("图片路径？？", data.getData() + "");
                if (!TextUtils.isEmpty(uri.getAuthority())) {
                    //查询选择图片
                    Cursor cursor = getContentResolver().query(
                            uri,
                            new String[] { MediaStore.Images.Media.DATA },
                            null,
                            null,
                            null);
                    //返回 没找到选择图片
                    if (null == cursor) {
                        return;
                    }
                    //光标移动至开头 获取图片路径
                    cursor.moveToFirst();
                    pathImage = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                }
            }

        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            Bundle bm = data.getExtras();
            Bitmap bitmap = (Bitmap) bm.get("data");
            saveBitmap(bitmap);
            Toast.makeText(activity_teacher_check.this, "可以！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity_teacher_check.this, "未找到存储卡，无法存储照片！",
                    Toast.LENGTH_SHORT).show();
        }
        try {
            if (tempFile != null && tempFile.exists())
                tempFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(pathImage)){
            Bitmap addbmp=BitmapFactory.decodeFile(pathImage);
            HashMap<String, Bitmap> map = new HashMap<String, Bitmap>();
            map.put("itemImage", addbmp);
            imageItem.add(map);
            simpleAdapter = new SimpleAdapter(this,
                    imageItem, R.layout.griditem_addpic,
                    new String[] { "itemImage"}, new int[] { R.id.imageView1});
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data,
                                            String textRepresentation) {
                    // TODO Auto-generated method stub
                    if(view instanceof ImageView && data instanceof Bitmap){
                        ImageView i = (ImageView)view;
                        i.setImageBitmap((Bitmap) data);
                        return true;
                    }
                    return false;
                }
            });
            gridView1.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
            //刷新后释放防止手机休眠后自动添加
            pathImage = null;
        }
    }

    private class Buttonlistener implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.up_btn:
                    if(imageItem.size()<4){
                        Toast.makeText(activity_teacher_check.this,"图片不足3张，请多拍几张",Toast.LENGTH_SHORT ).show();
                    }else{
                        //***************************************************************************
                        //这里写上传的代码,上传图片与识别码，识别码就是upload_str,它的构成为GGPPPPCC,GG年级,PPPP专业号,CC班级号，比如 17081004 代表17级计算机科学与技术4班
                        int cnt=1;
                        Iterator<HashMap<String, Bitmap>> lis = imageItem.iterator();
                        lis.next();
                        for (; lis.hasNext();) {
                            upload_bp=lis.next().get("itemImage");
                            upload_bp_str=encodeImagetoString(upload_bp);
                            upload_name="test"+String.valueOf(cnt);
                            //TeaRequest();//要上传就取消这个的注释符号
                            cnt++;
                            Toast.makeText(activity_teacher_check.this,"上传"+upload_name,Toast.LENGTH_SHORT ).show();
                        }
                        upload_bp=null;
                        upload_bp_str=null;
                        upload_name=null;
                    }
                    break;
                case R.id.check_btn://这个等后端代码
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
    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity_teacher_check.this);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                simpleAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void addpic() {
        final CharSequence[] items = { "相册（像素高，推荐）", "拍照(像素低，不推荐)" };
        AlertDialog dlg = new AlertDialog.Builder(activity_teacher_check.this)
                .setTitle("选择图片")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // 这里item是根据选择的方式，
                        if (item == 0) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent,PHOTO_REQUEST_GALLERY);
                        } else {/*
                           */
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePictureIntent, PHOTO_REQUEST_CAREMA);
                        }
                    }
                }).create();
        dlg.show();
    }

    private void saveBitmap(Bitmap bitmap) {
        //String sdCardDir="/miui/gallery/cloud/owner/app";
        //String sdCardDir="/storage/emulated/0/DCIM/Camara/";
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        final String save_pic_name = "dis"+simpleDateFormat.format(date);
        File file;
        try {
            File dirFile = new File(sdCardDir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
                Toast.makeText(this, "no folder!", Toast.LENGTH_LONG).show();
            }
            Toast.makeText(this, sdCardDir, Toast.LENGTH_LONG).show();

            file = new File(sdCardDir, save_pic_name + ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        file = new File(sdCardDir, save_pic_name + ".jpg");
        Toast.makeText(this, "OK!", Toast.LENGTH_LONG).show();
        try {
            MediaStore.Images.Media.insertImage(this.getContentResolver(), file.getAbsolutePath(),save_pic_name, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // 通知图库更新
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + "/sdcard/namecard/")));
        pathImage=file.getAbsolutePath();

    }

    public   void TeaRequest() {
        //请求地址
        String url = "http://271643ug95.wicp.vip/MyFirstWebApp/TeaServlet";
        String tag = "TeaRequest";

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
                                Toast.makeText(activity_teacher_check.this, "你好!已经上传信息", Toast.LENGTH_LONG).show();
                            } else {
                                //上传失败
                                Toast.makeText(activity_teacher_check.this, "上传失败", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            //请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(activity_teacher_check.this, "上传失败，无网络连接", Toast.LENGTH_LONG).show();
                            //Toast.makeText(this,"网络异常",Toast.LENGTH_LONG).show();
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(activity_teacher_check.this, "稍后再试", Toast.LENGTH_LONG).show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("image", upload_bp_str);  //注⑥
                params.put("pic_name", upload_name);
                params.put("upload_str",upload_str);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

    public String encodeImagetoString(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // 压缩图片
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
        byte[] byte_arr = stream.toByteArray();
        // Base64图片转码为String
        String encodedString = Base64.encodeToString(byte_arr, 0);
        return encodedString;

    }
}

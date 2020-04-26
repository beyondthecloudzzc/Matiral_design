package com.example.matiral_design;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.util.*;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.matiral_design.utils.Base64Coder;
import com.example.matiral_design.utils.ZoomBitmap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;



public class MainActivity extends AppCompatActivity
{
    public static final int TAKE_POTHO=1;
    // private static final String HOST = "http://271643ug95.wicp.vip/MyFirstWebApp/UpServlet_tec";
    private static final String HOST = "http://271643ug95.wicp.vip/MyFirstWebApp/UpServer";//上传的servlet的链接
    private ImageView imageView;
    private Button button;
    private Uri uri;
    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MyDataBaseHelper dbHelper;

    private Bitmap upbitmap;//2020/4/18添加
    private Handler myHandler;
    private ProgressDialog myDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navview = (NavigationView) findViewById(R.id.nav_view);

        ActionBar actionBar = getSupportActionBar();

        imageView = (ImageView) findViewById(R.id.picture);


        myHandler=new MyHandler();//2020/4/18

        // dbHelper.getWritableDatabase();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.category);
        }
        navview.setCheckedItem(R.id.nav_call);
        navview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    //选择部分
                    case R.id.nav_call:
                        Intent intent = new Intent(MainActivity.this,Edit_message.class);
                        startActivity(intent);
                        break;
                    case  R.id.nav_freinds:
                        if(upbitmap == null)//如果没拍照要提醒
                        {
                            Toast.makeText(getApplicationContext(), "请先拍照", Toast.LENGTH_SHORT).show();
                            break;
                        }

                        // String stupic_name = Edit_message.stupic_name;
                        //String stupic_str = Edit_message.stupic_str;

                        System.out.println("测试取到图片识别码" +Edit_message.stupic_str);
                        System.out.println("测试取到图片名称" +Edit_message.stupic_name);

                        new Thread(new Runnable() {
                            public void run() {
                                Message msg = new Message();
                                try {
                                    upload(Edit_message.stupic_str,Edit_message.stupic_name);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    msg.what = 2;
                                }
                                // upload(stupic_path,stupic_name);//重写了upload函数以适应需求2020/4/19

                                msg.what = 1;
                                msg.obj = "上传完成";
                                myHandler.sendMessage(msg);
                            }
                        }).start();


                        Toast.makeText(MainActivity.this,"打卡成功",Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.nav_location:
                        Intent intent3 = new Intent(MainActivity.this,check.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_mail:
                        Intent intent4 = new Intent(MainActivity.this,feedback.class);
                        startActivity(intent4);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outImage=new File(getExternalCacheDir(),"output_image.jpg");
                try{
                    if(outImage.exists())
                    {
                        outImage.delete();
                    }
                    outImage.createNewFile();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                if(Build.VERSION.SDK_INT>=24)
                {
                    uri= FileProvider.getUriForFile(MainActivity.this,"com.example.gdzc.cameraalbumtest.fileprovider",outImage);
                }
                else
                {
                    uri=Uri.fromFile(outImage);
                }
                Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                startActivityForResult(intent,TAKE_POTHO);
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFruits();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.action_settings:
                Toast.makeText(MainActivity.this,"谢谢支持",Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void refreshFruits() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //我写的下拉刷新部分，这里也可以注入一部分操作
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requsetCode, int resultCode, Intent data) {
        switch (requsetCode)
        {
            case TAKE_POTHO:
                if(resultCode==RESULT_OK)
                {
                    try{
                        Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        float wight=bitmap.getWidth();
                        float height=bitmap.getHeight();
                        imageView.setImageBitmap(bitmap);
                        System.out.println("shizaizheliaaaa");
                        //upbitmap= ZoomBitmap.zoomImage(bitmap, wight/1.1, height/1.1);//这句压缩
                        upbitmap= ZoomBitmap.zoomImage(bitmap, wight/1.5, height/1.5);//这句压缩，多次实验1.1还是太大了，成功率有点低提到1.5
                    }catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    //upload再次更新，一行代码或许可以抛弃数据库2020/4/19
//upload核心2020/4/18
    public void upload(String pic_str,String pic_name) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        upbitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);

        byte[] b = stream.toByteArray();
        // 将图片流以字符串形式存储下来

        String file = new String(Base64Coder.encodeLines(b));

      final  HttpClient client = new DefaultHttpClient();
        // 设置上传参数
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("file", file));

        /*下面就是这个,传图片的的时候把人的学号名字一并传上去，
        同理图片在服务器端的路径也可以一并完成，
        这样就可以减轻服务器端做大量的数据库查表操作

         */
        formparams.add(new BasicNameValuePair("path",pic_str));
        formparams.add(new BasicNameValuePair("name", pic_name));
      final   HttpPost post = new HttpPost(HOST);
        UrlEncodedFormEntity entity;


        try {
            entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            post.addHeader("Accept",
                    "text/javascript, text/html, application/xml, text/xml");
            post.addHeader("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
            post.addHeader("Accept-Encoding", "gzip,deflate,sdch");
            post.addHeader("Connection", "Keep-Alive");
            post.addHeader("Cache-Control", "no-cache");
            post.addHeader("Content-Type", "application/x-www-form-urlencoded");


            post.setEntity(entity);

            System.out.println("可以到这里没有问题");
            HttpResponse response = null;
            System.out.println("可以到这里好像也没有问题");
            System.out.println("可能是下面这句的问题");

            try {

                response = client.execute(post);//放线程里放线程里放线程里！！！！！！
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("可以到这里好像也啊啊啊啊啊啊啊啊没有问题");
            System.out.println(response.getStatusLine().getStatusCode());

            HttpEntity e = response.getEntity();

            System.out.println(EntityUtils.toString(e));

            if (200 == response.getStatusLine().getStatusCode()) {
                System.out.println("上传完成");
            } else {
                System.out.println("上传失败");
            }
            client.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1)
            {
                String resp = (String) msg.obj;
                Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_SHORT).show();
            }
           // myDialog.dismiss();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}

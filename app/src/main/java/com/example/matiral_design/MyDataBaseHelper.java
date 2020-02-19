package com.example.matiral_design;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDataBaseHelper extends SQLiteOpenHelper {
   public  static final String Major_Create="create table Major ("
           +"prof_num integer primary key,"
           +"prof_name text,"
           +"class_number integer,"
           +"academy_number integer,"
           +"name text,"
           +"grade integer)";
    public  static final String Academy_Create = "create table Major ("
            +"academy_num integer primary key,"
            +"academy_name text)";

   private  Context mContext;
   public  MyDataBaseHelper(Context context, String name,SQLiteDatabase.CursorFactory factory, int Version)
   {
       super(context, name, factory, Version);
       mContext = context;
   }
   @Override
   public void onCreate(SQLiteDatabase db)
   {
       db.execSQL(Major_Create);
       db.execSQL(Academy_Create);
       Toast.makeText(mContext,"Create succeeded",Toast.LENGTH_SHORT).show();
   }
   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersions, int newVersion)
   {

   }

}

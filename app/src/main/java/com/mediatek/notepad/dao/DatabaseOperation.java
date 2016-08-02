package com.mediatek.notepad.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.mediatek.notepad.R;
import com.mediatek.notepad.model.NoteBean;
import com.mediatek.notepad.util.MyToast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brook on 2016/7/20.
 */
public class DatabaseOperation
{
    private SQLiteDatabase db;
    private Context context;

    public DatabaseOperation(Context context, SQLiteDatabase db)
    {
        this.db = db;
        this.context = context;
    }

    //数据库的打开或创建
    public void create_db()
    {
        //创建或打开数据库
        db = context.openOrCreateDatabase("notepad.db", Context.MODE_PRIVATE, null);

        if (db == null)
        {
            MyToast.showLong(context, R.string.error_create_database);
        }

        //创建表,关于boolean型的定义采用interger,0代表false，1代表ture. 比如：is_stick
        db.execSQL("create table if not exists notes(_id integer primary key autoincrement," +
                "title text," +
                "context text," +
                "update_time varchar(20)," +
                "create_time varchar(20)," +
                "groupId integer," +
                "is_stick integer DEFAULT 0," +
                "is_have_img integer DEFAULT 0," +
                "img_path text )"
        );

    }

    public void insert_db(String title, String text, String update_time)
    {
        db.execSQL("insert into notes(title,context,update_time) values('" + title + "','" + text + "','" + update_time + "');");
    }

    public void update_db(String title, String text, String time, int item_ID)
    {
        db.execSQL("update notes set context='" + text + "',title='" + title + "',time='" + time + "'where _id='" + item_ID + "'");
    }

    public List<NoteBean> query_all_notes()
    {
        List<NoteBean> notesList = new ArrayList<NoteBean>();
        long beforeTime = System.currentTimeMillis();
        Log.d("QUERY BEFORE",":"+beforeTime);

        Cursor cursor = db.rawQuery("select * from notes", null);

        long afterTime = System.currentTimeMillis();
        Log.d("QUERY AFTER",":"+afterTime);
        Log.d("QUERY SHIJIAN CHA",":"+(afterTime - beforeTime)/1000.0);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
        {
            int _id = cursor.getColumnIndex("_id");
            int title = cursor.getColumnIndex("title");
            int context = cursor.getColumnIndex("context");
            int update_time = cursor.getColumnIndex("update_time");
            int create_time = cursor.getColumnIndex("create_time");
            int groupId = cursor.getColumnIndex("groupId");
            int is_stick = cursor.getColumnIndex("is_stick");
            int is_have_img = cursor.getColumnIndex("is_have_img");
            int img_path = cursor.getColumnIndex("img_path");

            NoteBean note = new NoteBean();
            note.setNoteId(cursor.getInt(_id));
            note.setNoteContext(cursor.getString(context));
            note.setNoteUpdateTime(cursor.getString(update_time));
            note.setGroupId(cursor.getInt(groupId));
            note.setStick(cursor.getInt(is_stick) == 1 ? true:false);
            note.setImgPath(cursor.getString(img_path));
            notesList.add(note);
        }
        return notesList;
    }

    public Cursor query_db(int item_ID)
    {
        Cursor cursor = db.rawQuery("select * from notes where _id='" + item_ID + "';", null);
        return cursor;

    }

    public void delete_db(int item_ID)
    {
        db.execSQL("delete from notes where _id='" + item_ID + "'");
        //Toast.makeText(context, "删除成功", Toast.LENGTH_LONG).show();
    }

    //关闭数据库
    public void close_db()
    {
        db.close();
    }
}

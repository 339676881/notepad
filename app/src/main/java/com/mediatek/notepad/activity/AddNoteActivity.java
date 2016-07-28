package com.mediatek.notepad.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.mediatek.notepad.R;
import com.mediatek.notepad.constant.Constant;
import com.mediatek.notepad.dao.DatabaseOperation;
import com.mediatek.notepad.model.NoteBean;
import com.mediatek.notepad.util.MyToast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity
{
    private EditText mEditText;
    private boolean is_edit;
    private NoteBean noteBean;
    private int noteID;

    private DatabaseOperation mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        initToolbar();

        mEditText = (EditText) findViewById(R.id.et_note_content);

        initData();


    }

    private void initData()
    {
        is_edit = getIntent().getBooleanExtra(Constant.EDIT_STATUS, false);

        if (is_edit){
            noteBean = (NoteBean) getIntent().getSerializableExtra(Constant.NOTE);
            noteID =  noteBean.getNoteId();
            mEditText.setText(noteBean.getNoteContext());
        }
    }

    private void initToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.default_category);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_add_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()){
            case R.id.action_save_note:
                String noteContent = mEditText.getText().toString();
                if(noteContent.isEmpty()){
                    MyToast.showLong(AddNoteActivity.this, R.string.error_note_content_null);
                }else{
                    //取得当前时间
                    SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy-MM-dd HH:mm");
                    Date curDate   =   new Date(System.currentTimeMillis());//获取当前时间
                    String   time   =   formatter.format(curDate);
                    //打开数据库
                    mDatabase = new DatabaseOperation(this,null);
                    mDatabase.create_db();
                    //判断是更新还是新增记事
                    if(!is_edit){
                        mDatabase.insert_db("",noteContent,time);
                    }else{
                        mDatabase.update_db("",noteContent,time,noteID);
                    }
                    mDatabase.close_db();
                    finish();
                }

        }
        return super.onOptionsItemSelected(item);
    }
}

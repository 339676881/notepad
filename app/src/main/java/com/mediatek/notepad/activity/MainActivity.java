package com.mediatek.notepad.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.mediatek.notepad.R;
import com.mediatek.notepad.adapter.NotelistAdapter;
import com.mediatek.notepad.constant.Constant;
import com.mediatek.notepad.dao.DatabaseOperation;
import com.mediatek.notepad.model.NoteBean;
import com.mediatek.notepad.wheel.DividerLineNotelist;

import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private RecyclerView mRvNotelist;
    private NotelistAdapter mNotelistAdapter;
    private List<NoteBean> mNotelist;
    private DatabaseOperation mDop;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initToolbar();

        mDop = new DatabaseOperation(this, null);
        mDop.create_db();
        mNotelist =  mDop.query_all_notes();

        mRvNotelist = (RecyclerView) findViewById(R.id.rv_note_list);
        mNotelistAdapter = new NotelistAdapter(this, mNotelist);
        mNotelistAdapter.setOnItemClickLitener(new NotelistAdapter.OnItemClickLitener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                intent.putExtra(Constant.EDIT_STATUS, true);
                intent.putExtra(Constant.NOTE, mNotelist.get(position));
                startActivity(intent);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvNotelist.setLayoutManager(linearLayoutManager);
        mRvNotelist.setAdapter(mNotelistAdapter);
        mRvNotelist.addItemDecoration(new DividerLineNotelist(MainActivity.this,
                DividerLineNotelist.VERTICAL_LIST));

        initDrawerlayout();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        mNotelist.clear();
        mNotelist.addAll(mDop.query_all_notes());
        mNotelistAdapter.notifyDataSetChanged();
    }

    private void initToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.default_category);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initDrawerlayout()
    {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
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
        if (drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        int id = item.getItemId();
        if (id == R.id.action_edit)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}

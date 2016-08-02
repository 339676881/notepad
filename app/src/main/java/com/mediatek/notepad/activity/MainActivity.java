package com.mediatek.notepad.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.mediatek.notepad.R;
import com.mediatek.notepad.adapter.NotelistAdapter;
import com.mediatek.notepad.constant.Constant;
import com.mediatek.notepad.dao.DatabaseOperation;
import com.mediatek.notepad.model.NoteBean;
import com.mediatek.notepad.wheel.DividerLineNotelist;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ActionMode.Callback
{
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private RecyclerView mRvNotelist;
    private NotelistAdapter mNotelistAdapter;
    private List<NoteBean> mNotelist;
    private DatabaseOperation mDop;

    private ActionMode mActionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();

        mDop = new DatabaseOperation(this, null);
        mDop.create_db();
        mNotelist =  mDop.query_all_notes();

        initRecyclerView();

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

    private void initRecyclerView()
    {
        mRvNotelist = (RecyclerView) findViewById(R.id.rv_note_list);
        mNotelistAdapter = new NotelistAdapter(this, mNotelist);
        mNotelistAdapter.setOnItemClickLitener(new NotelistAdapter.OnItemClickLitener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                if (mActionMode != null)
                {
                    if (!mNotelistAdapter.getCheckedList().contains((Integer) position)){
                        mNotelistAdapter.getCheckedList().add(new Integer(position));
                    }
                    else
                    {
                        mNotelistAdapter.getCheckedList().remove(new Integer(position));
                    }
                    mNotelistAdapter.notifyDataSetChanged();
                    mActionMode.setTitle(mNotelistAdapter.getCheckedList().size() + " 已选择");
                }
                else
                {
                    Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                    intent.putExtra(Constant.EDIT_STATUS, true);
                    intent.putExtra(Constant.NOTE, mNotelist.get(position));
                    startActivity(intent);
                }

            }

            @Override
            public void onItemLongClick(View view, int position)
            {
                if (mActionMode == null) {
                    mActionMode = startSupportActionMode(MainActivity.this);
                    mNotelistAdapter.isSelectMode = true;
                    mNotelistAdapter.notifyDataSetChanged();
                }
            }
        });
        mNotelistAdapter.setOnCheckboxClickLitener(new NotelistAdapter.OnCheckboxClickListener()
        {
            @Override
            public void onCheckboxClick()
            {
                if (mActionMode != null){
                    mActionMode.setTitle(mNotelistAdapter.getCheckedList().size() + " 已选择");
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvNotelist.setLayoutManager(linearLayoutManager);
        mRvNotelist.setAdapter(mNotelistAdapter);
        mRvNotelist.addItemDecoration(new DividerLineNotelist(MainActivity.this,
                DividerLineNotelist.VERTICAL_LIST));
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

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu)
    {
        if (mActionMode == null) {
            mActionMode = mode;
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_main_activity_delete, menu);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu)
    {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_delete:
                for (Integer integer: mNotelistAdapter.getCheckedList())
                {
                    int noteId = mNotelist.get(integer).getNoteId();
                    mDop.delete_db(noteId);
                }
                mNotelistAdapter.getCheckedList().clear();
                mNotelist.clear();
                mNotelist.addAll(mDop.query_all_notes());
                mNotelistAdapter.notifyDataSetChanged();
                mode.finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode)
    {
        mActionMode = null;
        mNotelistAdapter.getCheckedList().clear();
        mNotelistAdapter.isSelectMode = false;
        mNotelistAdapter.notifyDataSetChanged();
    }
}

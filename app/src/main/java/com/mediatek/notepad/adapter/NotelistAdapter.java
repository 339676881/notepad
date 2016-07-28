package com.mediatek.notepad.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mediatek.notepad.R;
import com.mediatek.notepad.model.NoteBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brook on 2016/7/21.
 */
public class NotelistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private LayoutInflater mLayoutInflater;
    private Context context;
    private List<NoteBean> mListNotes;
    private List<Integer> mCheckedList;

    //建立枚举 2个item 类型
    public enum ITEM_TYPE
    {
        ONLY_STICK,
        IMG_AND_STICK
    }

    public NotelistAdapter(Context context, List<NoteBean> notelist)
    {
        this.mListNotes = notelist;
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        mCheckedList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        //加载Item View的时候根据不同TYPE加载不同的布局
        if (viewType == ITEM_TYPE.ONLY_STICK.ordinal())
        {
            return new OnlyStickViewHolder(mLayoutInflater.inflate(R.layout.item_notelist_only_stick, parent, false));
        }
        else
        {
            return new ImgAndStickViewHolder(mLayoutInflater.inflate(R.layout.item_notelist_img_and_stick, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position)
    {
        if (holder instanceof OnlyStickViewHolder)
        {
            ((OnlyStickViewHolder) holder).noteTime.setText(mListNotes.get(position).getNoteUpdateTime());
            ((OnlyStickViewHolder) holder).noteContext.setText(mListNotes.get(position).getNoteContext());
            if (mCheckedList != null)
            {
                ((OnlyStickViewHolder)holder).checkBox.setChecked((mCheckedList.contains(new Integer(position)) ? true : false));
            }
            else
            {
                ((OnlyStickViewHolder) holder).checkBox.setChecked(false);
            }
        }
        else if (holder instanceof ImgAndStickViewHolder)
        {
            ((ImgAndStickViewHolder) holder).noteTime.setText(mListNotes.get(position).getNoteUpdateTime());
            ((ImgAndStickViewHolder) holder).noteContext.setText(mListNotes.get(position).getNoteContext());
            if (mCheckedList != null)
            {
                ((ImgAndStickViewHolder)holder).checkBox.setChecked((mCheckedList.contains(new Integer(position)) ? true : false));
            }
            else
            {
                ((ImgAndStickViewHolder) holder).checkBox.setChecked(false);
            }
        }

        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });
        }
        onChecked(holder, position);
    }

    //设置ITEM类型，可以自由发挥，这里设置item position单数显示item1 偶数显示item2
    @Override
    public int getItemViewType(int position)
    {
        //Enum类提供了一个ordinal()方法，返回枚举类型的序数，这里ITEM_TYPE.ONLY_STICK.ordinal()代表0， ITEM_TYPE.IMG_AND_STICK.ordinal()代表1
        if (!mListNotes.get(position).isHaveImg())
        {
            return ITEM_TYPE.ONLY_STICK.ordinal();
        }
        else
        {
            return ITEM_TYPE.IMG_AND_STICK.ordinal();
        }
    }


    @Override
    public int getItemCount()
    {
        return mListNotes == null ? 0 : mListNotes.size();
    }

    //item1 的ViewHolder
    public static class OnlyStickViewHolder extends RecyclerView.ViewHolder
    {
        TextView noteTime;
        TextView noteContext;
        ImageView noteStick;
        CheckBox checkBox;

        public OnlyStickViewHolder(View itemView)
        {
            super(itemView);
            noteTime = (TextView) itemView.findViewById(R.id.tv_note_time);
            noteContext = (TextView) itemView.findViewById(R.id.tv_note_context);
            noteStick = (ImageView) itemView.findViewById(R.id.iv_stick_icon);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        }
    }

    //item2 的ViewHolder
    public static class ImgAndStickViewHolder extends RecyclerView.ViewHolder
    {
        TextView noteTime;
        TextView noteContext;
        ImageView noteImage;
        ImageView noteStick;
        CheckBox checkBox;

        public ImgAndStickViewHolder(View itemView)
        {
            super(itemView);
            noteTime = (TextView) itemView.findViewById(R.id.tv_note_time);
            noteContext = (TextView) itemView.findViewById(R.id.tv_note_context);
            noteImage = (ImageView) itemView.findViewById(R.id.iv_note_image);
            noteStick = (ImageView) itemView.findViewById(R.id.iv_stick_icon);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        }
    }

    public void setData(List<NoteBean> mData)
    {
        this.mListNotes = mData;
        notifyDataSetChanged();
    }

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);

    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private void onChecked(final RecyclerView.ViewHolder viewHolder, final int position) {


        if (viewHolder instanceof OnlyStickViewHolder)
        {
            ((OnlyStickViewHolder)viewHolder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if (isChecked)
                    {
                        mCheckedList.add(new Integer(position));
                    }
                    else
                    {
                        mCheckedList.remove(new Integer(position));
                    }
                }
            });
        }
        else if (viewHolder instanceof ImgAndStickViewHolder)
        {
            ((ImgAndStickViewHolder)viewHolder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if (isChecked)
                    {
                        mCheckedList.add(new Integer(position));
                    }
                    else
                    {
                        mCheckedList.remove(new Integer(position));
                    }
                }
            });
        }

    }

}


package com.mediatek.notepad.model;

import java.io.Serializable;

/**
 * Created by Brook on 2016/7/21.
 */
public class NoteBean implements Serializable
{
    private static final long serialVersionUID = 0L;

    private int noteId;
    private String noteTitle;
    private String noteContext;
    private String noteCreateTime;
    private String noteUpdateTime;
    private int groupId;            //组ID
    private boolean isStick;       //是否置顶
    private boolean isHaveImg;       //是否有图片
    private String imgPath;


    public int getNoteId()
    {
        return noteId;
    }

    public void setNoteId(int noteId)
    {
        this.noteId = noteId;
    }

    public String getNoteTitle()
    {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle)
    {
        this.noteTitle = noteTitle;
    }

    public String getNoteContext()
    {
        return noteContext;
    }

    public void setNoteContext(String noteContext)
    {
        this.noteContext = noteContext;
    }

    public String getNoteCreateTime()
    {
        return noteCreateTime;
    }

    public void setNoteCreateTime(String noteCreateTime)
    {
        this.noteCreateTime = noteCreateTime;
    }

    public String getNoteUpdateTime()
    {
        return noteUpdateTime;
    }

    public void setNoteUpdateTime(String noteUpdateTime)
    {
        this.noteUpdateTime = noteUpdateTime;
    }

    public int getGroupId()
    {
        return groupId;
    }

    public void setGroupId(int groupId)
    {
        this.groupId = groupId;
    }

    public boolean isStick()
    {
        return isStick;
    }

    public void setStick(boolean stick)
    {
        isStick = stick;
    }

    public String getImgPath()
    {
        return imgPath;
    }

    public void setImgPath(String imgPath)
    {
        this.imgPath = imgPath;
    }

    public boolean isHaveImg()
    {
        return isHaveImg;
    }

    public void setHaveImg(boolean haveImg)
    {
        isHaveImg = haveImg;
    }
}

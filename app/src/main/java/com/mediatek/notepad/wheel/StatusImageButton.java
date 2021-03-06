/*
 * Copyright 2016 Matthew Lee
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.mediatek.notepad.wheel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

import com.mediatek.notepad.R;


public class StatusImageButton extends AppCompatImageButton
{
    private Drawable activated;
    private Drawable defauls;
    private boolean isActivated;

    public StatusImageButton(Context context) {
        super(context);
        init(null);
    }

    public StatusImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public StatusImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.StatusImageButton);
        activated = array.getDrawable(R.styleable.StatusImageButton_activated);
        defauls = array.getDrawable(R.styleable.StatusImageButton_defauls);
        isActivated = array.getBoolean(R.styleable.StatusImageButton_status, false);
        array.recycle();

        setActivated(isActivated);
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean isActivated) {
        this.isActivated = isActivated;
        setImageDrawable(isActivated ? activated : defauls);
    }
}

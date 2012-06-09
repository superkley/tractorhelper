/*  Copyright (c) 2010 Xiaoyun Zhu
 * 
 *  Permission is hereby granted, free of charge, to any person obtaining a copy  
 *  of this software and associated documentation files (the "Software"), to deal  
 *  in the Software without restriction, including without limitation the rights  
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell  
 *  copies of the Software, and to permit persons to whom the Software is  
 *  furnished to do so, subject to the following conditions:
 *  
 *  The above copyright notice and this permission notice shall be included in  
 *  all copies or substantial portions of the Software.
 *  
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR  
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,  
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE  
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER  
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,  
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN  
 *  THE SOFTWARE.  
 */
package cn.kk.tractorhelper.datatype;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.text.Html;
import android.text.Spanned;
import android.view.Display;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ToggleButton;
import cn.kk.tractorhelper.R;
import cn.kk.tractorhelper.game.Card;
import cn.kk.tractorhelper.game.Suit;
import cn.kk.tractorhelper.helper.AndroidHelper;

public class ValuedButton extends ToggleButton {
    private int value;
    protected int width;
    protected int height;
    private static int displayHeight = -1;
    protected float textSize;

    public ValuedButton(Context context, int rows, int value, String html, OnClickListener setsClickListener) {
        super(context);
        setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_card));
        setValue(value);
        Spanned text = Html.fromHtml(html);
        setText(text);
        setTextOn(text);
        setTextOff(text);
        setOnClickListener(setsClickListener);

        if (displayHeight == -1) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            displayHeight = display.getHeight();
        }
        int w;
        int h;
        switch (rows) {
        case 1:
        case 2:
            h = (int) (displayHeight / 4.0);
            w = (int) (h / 1.3);
            break;
        default:
            h = (int) (displayHeight / (rows + 2.0));
            w = (int) (h / 1.3);
        }
        this.width = w;
        this.height = h;            
        this.textSize = this.height * 0.15f;
        
        setTextSize(this.textSize);
        setLayoutParams(new GridView.LayoutParams(this.width, this.height));

    }

    public ValuedButton(Context context, int rows, int value, String html, OnClickListener setsClickListener, float relativeFontSize) {
        this(context, rows, value, html, setsClickListener);
        setTextSize(this.textSize * relativeFontSize);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}

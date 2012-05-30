package cn.kk.tractorhelper.datatype;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.text.Html;
import android.text.Spanned;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ToggleButton;
import cn.kk.tractorhelper.R;
import cn.kk.tractorhelper.helper.AndroidHelper;

public class ValuedButton extends ToggleButton {
	private int value;


    public ValuedButton(Context context, int widthDip, int value, String html, OnClickListener setsClickListener) {
        super(context);
        setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_card));
        setValue(value);
        Spanned text = Html.fromHtml(html);
        setText(text);
        setTextOn(text);
        setTextOff(text);
        setOnClickListener(setsClickListener);
        final int w = (int) AndroidHelper.convertDpToPixel(widthDip, context);
        setLayoutParams(new GridView.LayoutParams(w, (int) (w * 1.3)));
    }


	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}

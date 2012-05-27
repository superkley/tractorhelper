package cn.kk.tractorhelper.datatype;

import android.content.Context;
import android.widget.Button;

public class ValuedButton extends Button {
    private int value;

    public ValuedButton(Context context) {
        super(context);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}

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
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import cn.kk.tractorhelper.game.Card;
import cn.kk.tractorhelper.game.Suit;
import cn.kk.tractorhelper.helper.AndroidHelper;
import cn.kk.tractorhelper.helper.GameHelper;

public class ScoredValuedButton extends ValuedButton {
    private static final TextPaint amountPaint;
    private static final TextPaint handPaint;
    private static float amountX;
    private static float handX;
    private static float fontHeight;

    static {
        amountPaint = new TextPaint();
        amountPaint.setARGB(225, 200, 10, 20);
        amountPaint.setStrokeWidth(2);
        amountPaint.setStyle(Style.FILL);

        handPaint = new TextPaint();
        handPaint.setARGB(225, 180, 180, 180);
        handPaint.setStrokeWidth(2);
        handPaint.setStyle(Style.FILL);
    }

    private int amount;
    private int hand;

    @Override
    public void setChecked(boolean checked) {
        if (amount > 0) {
            amount--;
            hand++;
            super.setChecked(true);
        } else {
            amount = hand;
            hand = 0;
            super.setChecked(false);
        }
    }

    public int clearChecked() {
        if (hand == 0) {
            return 0;
        } else {
            int tmp = hand;
            hand = 0;
            super.setChecked(false);
            return tmp;
        }
    }

    public ScoredValuedButton(Context context, int rows, Card c, String html, OnClickListener setsClickListener,
            int amount) {
        super(context, rows, c.idx, html, setsClickListener);
        setPadding(2, 0, 2, 0);
        this.amount = amount;
        if (amountX == 0) {
            // Log.i("amount", "" + amountX);
            // Log.i("hand", "" + handX);
            float amountSize = this.textSize * 0.9f;
            amountPaint.setTextSize(amountSize);
            handPaint.setTextSize(amountSize);
            FontMetrics metrics = amountPaint.getFontMetrics();
            fontHeight = -metrics.top + metrics.bottom;
            amountX = fontHeight * 0.6f + 2;
            handX = this.width - fontHeight;            
        }
        
        if (c == Card.from(Suit.JOKER, Card.BLACK_JOKER_NAME) || c == Card.from(Suit.JOKER, Card.COLOR_JOKER_NAME)) {
            setTextSize(this.textSize * 0.7f);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (amount > 0) {
            canvas.drawText(String.valueOf(this.amount), amountX, fontHeight, amountPaint);
        }
        if (hand > 0) {
            canvas.drawText(String.valueOf(this.hand), handX, fontHeight, handPaint);
        }
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getHand() {
        return hand;
    }

    public void setHand(int hand) {
        this.hand = hand;
    }
}

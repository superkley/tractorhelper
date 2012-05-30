package cn.kk.tractorhelper.datatype;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import cn.kk.tractorhelper.helper.AndroidHelper;

public class ScoredValuedButton extends ValuedButton {
	private static final TextPaint amountPaint;
	private static final TextPaint handPaint;
	private static float height;
	private static float amountX;
	private static float handX;

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

	public ScoredValuedButton(Context context, int widthDip, int value,
			String html, OnClickListener setsClickListener, float sizeText,
			int amount) {
		super(context, widthDip, value, html, setsClickListener);
		setTextSize(sizeText);
		setPadding(2, 0, 2, 0);
		this.amount = amount;
		if (amountX == 0) {
			amountX = AndroidHelper.convertDpToPixel(5, context);
			handX = AndroidHelper.convertDpToPixel(21, context);
			amountPaint.setTextSize(AndroidHelper.convertDpToPixel(8, context));
			handPaint.setTextSize(AndroidHelper.convertDpToPixel(8, context));
			FontMetrics metrics = amountPaint.getFontMetrics();
			height = -metrics.top + metrics.bottom;
			// Log.i("amount", "" + amountX);
			// Log.i("hand", "" + handX);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (amount > 0) {
			canvas.drawText(String.valueOf(amount), amountX, height,
					amountPaint);
		}
		if (hand > 0) {
			canvas.drawText(String.valueOf(hand), handX, height, handPaint);
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

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
package cn.kk.tractorhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import cn.kk.tractorhelper.datatype.MyIntents;
import cn.kk.tractorhelper.datatype.ValuedButton;
import cn.kk.tractorhelper.game.Card;
import cn.kk.tractorhelper.helper.AndroidHelper;

public class ChooseLevelActivity extends Activity {
	private boolean loadedFromIntent;
	private int sets;
	private TextView textSettingSets;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.choose_level);

		final Intent intent = getIntent();
		this.sets = intent.getIntExtra(MyIntents.SETS, -1);
		if (this.sets != -1) {
			this.loadedFromIntent = true;
		}

		GridView grid = (GridView) findViewById(R.id.grid_levels);
		grid.setAdapter(new LevelsAdapter());

		this.textSettingSets = (TextView) findViewById(R.id.text_setting_sets);
	}

	private OnClickListener levelClickListener = new OnClickListener() {
		public void onClick(View v) {
			nextStep(v);
		}
	};

	public class LevelsAdapter extends BaseAdapter {
		public View getView(int position, View convertView, ViewGroup parent) {
			final int cardName;
			if (position < 12) {
				cardName = position + 2;
			} else if (position == 12) {
				cardName = 1;
			} else {
				cardName = Card.LEVEL_JOKER;
			}

			return new ValuedButton(ChooseLevelActivity.this, 2, cardName,
					"<b>" + Card.toString(cardName) + "</b>",
					levelClickListener);
		}

		public final int getCount() {
			return 14;
		}

		public final Object getItem(int position) {
			return Integer.valueOf(position);
		}

		public final long getItemId(int position) {
			return position;
		}
	}

	public void previousStep(final View view) {
		Toast.makeText(this, getResources().getString(R.string.text_cfg_reset),
				Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this, GameStartActivity.class);
		intent.putExtra(MyIntents.SETS, this.sets);
		startActivity(intent);
	}

	public void nextStep(final View view) {
		if (view instanceof ValuedButton) {
			final int level = ((ValuedButton) view).getValue();
			final String name = Card.toString(level);
			Toast.makeText(
					this,
					Html.fromHtml(getResources().getString(R.string.text_cfg)
							+ getResources().getString(R.string.text_cfg_level)
							+ "<b>" + name + "</b>"), Toast.LENGTH_SHORT)
					.show();
			nextStep(level);
		}
	}

	private void nextStep(final int level) {
		Intent intent = new Intent(this, SelectTrumpActivity.class);
		intent.putExtra(MyIntents.SETS, this.sets);
		intent.putExtra(MyIntents.LEVEL, level);
		startActivity(intent);
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPreferences(Activity.MODE_PRIVATE).edit().putInt(MyIntents.SETS, 2)
				.commit();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!this.loadedFromIntent) {
			this.sets = getPreferences(Activity.MODE_PRIVATE).getInt(
					MyIntents.SETS, 2);
		}
		StringBuffer settingsText = new StringBuffer();
		settingsText.append(Card.getSetsName(this.sets)).append(
				getResources().getString(R.string.text_set_unit));
		this.textSettingSets.setText(settingsText.toString());
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			previousStep(null);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}

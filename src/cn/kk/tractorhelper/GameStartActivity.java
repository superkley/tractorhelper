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
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import cn.kk.tractorhelper.helper.AndroidHelper;

public class GameStartActivity extends Activity {
	private static final int[] TEXTS = { R.string.text_sets_1,
			R.string.text_sets_2, R.string.text_sets_3, R.string.text_sets_4,
			R.string.text_sets_5, R.string.text_sets_6 };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.game_start);

		final Intent intent = getIntent();
		final int sets = intent.getIntExtra(MyIntents.SETS, -1);
		if (sets != -1) {
			// returning from other activities
			getPreferences(Activity.MODE_PRIVATE).edit().clear().commit();
		}

		final TextView textTitle = (TextView) findViewById(R.id.text_title);

		GridView grid = (GridView) findViewById(R.id.grid_sets);
		grid.setAdapter(new SetsAdapter());
	}

	private OnClickListener setsClickListener = new OnClickListener() {
		public void onClick(View v) {
			nextStep(v);
		}
	};

	public class SetsAdapter extends BaseAdapter {
		public View getView(int position, View convertView, ViewGroup parent) {
			return new ValuedButton(GameStartActivity.this, 2, position + 1,
					getResources().getString(TEXTS[position]),
					setsClickListener, 0.6f);
		}

		public final int getCount() {
			return TEXTS.length;
		}

		public final Object getItem(int position) {
			return Integer.valueOf(position);
		}

		public final long getItemId(int position) {
			return position;
		}
	}

	public void nextStep(final View view) {
		if (view instanceof ValuedButton) {
			final int sets = ((ValuedButton) view).getValue();
			getPreferences(Activity.MODE_PRIVATE).edit()
					.putInt(MyIntents.SETS, sets).commit();
			Toast.makeText(
					this,
					getResources().getString(R.string.text_cfg)
							+ String.valueOf(sets)
							+ getResources().getString(R.string.text_cfg_sets),
					Toast.LENGTH_SHORT).show();
			nextStep(sets);
		}
	}

	private void nextStep(final int sets) {
		Intent intent = new Intent(this, ChooseLevelActivity.class);
		intent.putExtra(MyIntents.SETS, sets);
		startActivity(intent);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		final int sets = getPreferences(Activity.MODE_PRIVATE).getInt(
				MyIntents.SETS, -1);
		if (sets != -1) {
			nextStep(sets);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(true);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}
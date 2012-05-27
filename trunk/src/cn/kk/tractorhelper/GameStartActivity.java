package cn.kk.tractorhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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
    private static final int[] TEXTS = { R.string.text_sets_1, R.string.text_sets_2, R.string.text_sets_3,
            R.string.text_sets_4, R.string.text_sets_5, R.string.text_sets_6 };

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
            ValuedButton btn = new ValuedButton(GameStartActivity.this);
            btn.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
            btn.setValue(position + 1);
            btn.setText(Html.fromHtml(getResources().getString(TEXTS[position])));
            btn.setOnClickListener(setsClickListener);
            final int w = (int) AndroidHelper.convertDpToPixel(64, GameStartActivity.this);
            btn.setLayoutParams(new GridView.LayoutParams(w, (int) (w * 1.3)));
            return btn;
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
            Toast.makeText(
                    this,
                    getResources().getString(R.string.text_cfg) + String.valueOf(sets)
                            + getResources().getString(R.string.text_cfg_sets), Toast.LENGTH_SHORT).show();
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
        getPreferences(Activity.MODE_PRIVATE).edit().putInt(MyIntents.SETS, 2).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        final int sets = getPreferences(Activity.MODE_PRIVATE).getInt(MyIntents.SETS, -1);
        if (sets != -1) {
            nextStep(sets);
        }
    }
}
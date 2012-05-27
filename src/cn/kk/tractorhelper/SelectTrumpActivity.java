package cn.kk.tractorhelper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
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
import cn.kk.tractorhelper.game.Card;
import cn.kk.tractorhelper.game.Suit;
import cn.kk.tractorhelper.helper.AndroidHelper;
import cn.kk.tractorhelper.helper.GameHelper;

public class SelectTrumpActivity extends Activity {
    private boolean loadedFromIntent;
    private int sets;
    private int level;
    private TextView textSettingSets;
    private TextView textSettingLevel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_trump);

        final Intent intent = getIntent();
        this.sets = intent.getIntExtra(MyIntents.SETS, -1);
        if (this.sets != -1) {
            loadedFromIntent = true;
            this.level = intent.getIntExtra(MyIntents.LEVEL, 2);
        }

        GridView grid = (GridView) findViewById(R.id.grid_levels);
        grid.setAdapter(new TrumpsAdapter());

        this.textSettingSets = (TextView) findViewById(R.id.text_setting_sets);
        this.textSettingLevel = (TextView) findViewById(R.id.text_setting_level);
    }

    private OnClickListener trumpClickListener = new OnClickListener() {
        public void onClick(View v) {
            nextStep(v);
        }
    };

    public class TrumpsAdapter extends BaseAdapter {
        public View getView(int position, View convertView, ViewGroup parent) {
            final Suit trump;
            if (position < 4) {
                trump = Suit.fromId(position);
            } else {
                trump = Suit.JOKER;
            }
            final String name = GameHelper.getTrumpName(trump, SelectTrumpActivity.this.level);
            ValuedButton btn = new ValuedButton(SelectTrumpActivity.this);
            btn.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
            btn.setValue(trump.id);
            String color = GameHelper.getColorString(trump.color.color);
            btn.setText(Html
                    .fromHtml("<b>" + name + "<br/><font color='" + color + "'>" + trump.symbol + "</font></b>"));
            btn.setOnClickListener(trumpClickListener);
            final int w = (int) AndroidHelper.convertDpToPixel(64, SelectTrumpActivity.this);
            btn.setLayoutParams(new GridView.LayoutParams(w, (int) (w * 1.3)));
            return btn;
        }

        public final int getCount() {
            return 5;
        }

        public final Object getItem(int position) {
            return Integer.valueOf(position);
        }

        public final long getItemId(int position) {
            return position;
        }
    }

    public void previousStep(final View view) {
        Toast.makeText(this, R.string.text_new, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ChooseLevelActivity.class);
        intent.putExtra(MyIntents.SETS, this.sets);
        startActivity(intent);
    }

    public void nextStep(final View view) {
        if (view instanceof ValuedButton) {
            final Suit trump = Suit.fromId(((ValuedButton) view).getValue());
            String color = GameHelper.getColorString(trump.color.color);
            final String name = GameHelper.getTrumpName(trump, this.level);
            Toast.makeText(
                    this,
                    Html.fromHtml(getResources().getString(R.string.text_cfg)
                            + getResources().getString(R.string.text_cfg_trump) + "<b>" + name + "<font color='"
                            + color + "'>" + trump.symbol + "</font></b>"), Toast.LENGTH_SHORT).show();
            nextStep(trump.id);
        }
    }

    private void nextStep(final int trump) {

        Intent intent = new Intent(this, PlayGameActivity.class);
        intent.putExtra(MyIntents.SETS, this.sets);
        intent.putExtra(MyIntents.LEVEL, this.level);
        intent.putExtra(MyIntents.TRUMP, trump);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Editor editor = getPreferences(Activity.MODE_PRIVATE).edit();
        editor.putInt(MyIntents.SETS, this.sets).putInt(MyIntents.LEVEL, this.level).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!loadedFromIntent) {
            final SharedPreferences preferences = getPreferences(Activity.MODE_PRIVATE);
            this.sets = preferences.getInt(MyIntents.SETS, 2);
            this.level = preferences.getInt(MyIntents.LEVEL, 2);
        }
        if (this.level == Card.LEVEL_JOKER) {
            nextStep(Suit.JOKER.id);
        }
        final String name = GameHelper.getLevelName(this.level);
        StringBuffer settingsText = new StringBuffer();
        final Resources resources = getResources();
        settingsText.append(Card.getSetsName(this.sets)).append(resources.getString(R.string.text_set_unit));
        this.textSettingSets.setText(settingsText.toString());

        settingsText = new StringBuffer();
        settingsText.append(resources.getString(R.string.text_level_unit)).append(name);
        this.textSettingLevel.setText(settingsText.toString());

    }
}
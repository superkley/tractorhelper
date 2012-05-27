package cn.kk.tractorhelper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.kk.tractorhelper.datatype.MyIntents;
import cn.kk.tractorhelper.game.Card;
import cn.kk.tractorhelper.game.Game;
import cn.kk.tractorhelper.game.Suit;
import cn.kk.tractorhelper.helper.AndroidHelper;
import cn.kk.tractorhelper.helper.GameHelper;

public class PlayGameActivity extends Activity {

    private static final Suit[] SUITS = new Suit[] { Suit.JOKER, Suit.SPADE, Suit.HEART, Suit.CLUB, Suit.DIAMOND };
    private boolean loadedFromIntent;
    private int sets;
    private int level;
    private Suit trump;
    private Game game;
    private TextView textSettingSets;
    private TextView textSettingLevel;
    private TextView textSettingTrump;
    private TextView textInfoCardsLeft;
    private TextView textInfoPointsLeft;
    private TextView textInfoPointsGained;
    private LinearLayout lytBoard;
    private HorizontalScrollView hsvTrump;
    private HorizontalScrollView hsvSpade;
    private HorizontalScrollView hsvHeart;
    private HorizontalScrollView hsvClub;
    private HorizontalScrollView hsvDiamond;
    private LinearLayout lytTrump;
    private LinearLayout lytSpade;
    private LinearLayout lytHeart;
    private LinearLayout lytClub;
    private LinearLayout lytDiamond;
    private LayoutParams lytParamsCard;
    private float sizeText;

    // AsyncTask, MotionEvent

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.play_game);
        // GridView grid = (GridView) findViewById(R.id.grid_trump);
        // grid.setAdapter(new AppsAdapter());

        final Intent intent = getIntent();
        this.sets = intent.getIntExtra(MyIntents.SETS, -1);
        if (this.sets != -1) {
            this.loadedFromIntent = true;
            this.level = intent.getIntExtra(MyIntents.LEVEL, 2);
            this.trump = Suit.fromId(intent.getIntExtra(MyIntents.TRUMP, -1));
        }

        this.textSettingSets = (TextView) findViewById(R.id.text_setting_sets);
        this.textSettingLevel = (TextView) findViewById(R.id.text_setting_level);
        this.textSettingTrump = (TextView) findViewById(R.id.text_setting_trump);
        this.textInfoCardsLeft = (TextView) findViewById(R.id.text_info_cardsleft);
        this.textInfoPointsLeft = (TextView) findViewById(R.id.text_info_pointsleft);
        this.textInfoPointsGained = (TextView) findViewById(R.id.text_info_pointsgained);
        this.lytBoard = (LinearLayout) findViewById(R.id.lyt_board);

        this.hsvTrump = (HorizontalScrollView) findViewById(R.id.hsv_trump);
        this.lytTrump = (LinearLayout) findViewById(R.id.lyt_trump);

        this.hsvSpade = (HorizontalScrollView) findViewById(R.id.hsv_spade);
        this.lytSpade = (LinearLayout) findViewById(R.id.lyt_spade);

        this.hsvHeart = (HorizontalScrollView) findViewById(R.id.hsv_heart);
        this.lytHeart = (LinearLayout) findViewById(R.id.lyt_heart);

        this.hsvClub = (HorizontalScrollView) findViewById(R.id.hsv_club);
        this.lytClub = (LinearLayout) findViewById(R.id.lyt_club);

        this.hsvDiamond = (HorizontalScrollView) findViewById(R.id.hsv_diamond);
        this.lytDiamond = (LinearLayout) findViewById(R.id.lyt_diamond);

    }

    public void previousStep(final View view) {
        if (this.game.hasHistory()) {
            updateBoard();
        } else {
            Toast.makeText(this, getResources().getString(R.string.text_new), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ChooseLevelActivity.class);
            intent.putExtra(MyIntents.SETS, this.sets);
            startActivity(intent);
        }
    }

    private void updateBoard() {
        for (Suit s : SUITS) {
            HorizontalScrollView hsv = getCardsHorizontalScrollView(s);
            Card[] cards = this.game.getCards(s);
            if (cards.length > 0) {
                LinearLayout lyt = getCardsLayout(s);
                lyt.removeAllViews();
                for (Card c : cards) {
                    lyt.addView(createCardButton(c));
                }
                hsv.setVisibility(View.VISIBLE);
            } else {
                hsv.setVisibility(View.GONE);
            }
        }
    }

    private HorizontalScrollView getCardsHorizontalScrollView(Suit s) {
        switch (s) {
        case JOKER:
            return hsvTrump;
        case SPADE:
            return hsvSpade;
        case HEART:
            return hsvHeart;
        case CLUB:
            return hsvClub;
        case DIAMOND:
            return hsvDiamond;
        default:
            return hsvTrump;
        }
    }

    private LinearLayout getCardsLayout(Suit s) {
        switch (s) {
        case JOKER:
            return lytTrump;
        case SPADE:
            return lytSpade;
        case HEART:
            return lytHeart;
        case CLUB:
            return lytClub;
        case DIAMOND:
            return lytDiamond;
        default:
            return lytTrump;
        }
    }

    private View createCardButton(Card c) {
        Button btn = new Button(this);
        btn.setPadding(2, 1, 2, 1);
        btn.setTextSize(sizeText);
        btn.setLayoutParams(this.lytParamsCard);
        String color;
        if (c == Card.from(Suit.JOKER, Card.BLACK_JOKER_NAME)) {
            color = GameHelper.getColorString(0x303030);
        } else if (c == Card.from(Suit.JOKER, Card.COLOR_JOKER_NAME)) {
            color = GameHelper.getColorString(0xff6a30);
        } else {
            color = GameHelper.getColorString(c.suit.color.color);
        }
        String name = Card.toString(c.name);
        char symbol = c.suit.symbol;
        StringBuffer btnText = new StringBuffer();
        btnText.append(name).append("<br />").append("<font color='").append(color).append("'>").append(symbol)
                .append("</font>");
        btn.setText(Html.fromHtml(btnText.toString()));
        return btn;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Editor editor = getPreferences(Activity.MODE_PRIVATE).edit();
        editor.putInt(MyIntents.SETS, this.sets).putInt(MyIntents.LEVEL, this.level)
                .putInt(MyIntents.TRUMP, this.trump.id);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!loadedFromIntent) {
            final SharedPreferences preferences = getPreferences(Activity.MODE_PRIVATE);
            this.sets = preferences.getInt(MyIntents.SETS, 2);
            this.level = preferences.getInt(MyIntents.LEVEL, 2);
            this.trump = Suit.fromId(preferences.getInt(MyIntents.LEVEL, -1));
        }

        this.sizeText = AndroidHelper.convertDpToPixel(8, this);
        final float w = AndroidHelper.convertDpToPixel(32, this);
        this.lytParamsCard = new LayoutParams((int) w, (int) (w * 1.3));

        this.game = new Game(this.sets, this.level, this.trump);

        final String levelName = GameHelper.getLevelName(this.level);
        StringBuffer settingsText = new StringBuffer();
        final Resources resources = getResources();
        settingsText.append(Card.getSetsName(this.sets)).append(resources.getString(R.string.text_set_unit));
        this.textSettingSets.setText(settingsText.toString());

        settingsText = new StringBuffer();
        settingsText.append(resources.getString(R.string.text_level_unit)).append(levelName);
        this.textSettingLevel.setText(settingsText.toString());

        final String trumpName = GameHelper.getTrumpName(trump, this.level);
        final String color = GameHelper.getColorString(trump.color.color);
        settingsText = new StringBuffer();

        settingsText.append(trumpName).append("<font color='").append(color).append("'>").append(trump.symbol)
                .append("</font>");
        this.textSettingTrump.setText(Html.fromHtml(settingsText.toString()));

        updateBoard();

        updateInfo();
    }

    private void updateInfo() {
        final Resources resources = getResources();
        StringBuffer infoText = new StringBuffer();
        infoText.append(String.valueOf(this.game.getCardsLeft())).append(resources.getString(R.string.text_card_unit));
        this.textInfoCardsLeft.setText(infoText.toString());

        infoText = new StringBuffer();
        infoText.append(String.valueOf(this.game.getPointsLeft()))
                .append(resources.getString(R.string.text_point_unit));
        this.textInfoPointsLeft.setText(infoText.toString());

        infoText = new StringBuffer();
        infoText.append(String.valueOf(this.game.getPointsGained())).append(
                resources.getString(R.string.text_point_unit));
        this.textInfoPointsGained.setText(infoText.toString());
    }
}
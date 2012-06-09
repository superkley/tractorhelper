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

import java.util.BitSet;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.kk.tractorhelper.datatype.MyIntents;
import cn.kk.tractorhelper.datatype.ScoredValuedButton;
import cn.kk.tractorhelper.game.Card;
import cn.kk.tractorhelper.game.Game;
import cn.kk.tractorhelper.game.Suit;
import cn.kk.tractorhelper.helper.AndroidHelper;
import cn.kk.tractorhelper.helper.GameHelper;

public class PlayGameActivity extends Activity {
	private Timer timer = new Timer();
	private static final Suit[] SUITS = new Suit[] { Suit.JOKER, Suit.SPADE,
			Suit.HEART, Suit.CLUB, Suit.DIAMOND };
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
	private ImageButton btnPrev;

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

		this.btnPrev = (ImageButton) findViewById(R.id.btn_prev);

		this.btnPrev.setOnLongClickListener(clearHistoryClickListener);
	}

	private Runnable checkCards = new Runnable() {
		public void run() {
			final LinearLayout[] lyts = { lytTrump, lytClub, lytDiamond,
					lytHeart, lytSpade };
			final HorizontalScrollView[] horizontalScrollViews = { hsvTrump,
					hsvClub, hsvDiamond, hsvHeart, hsvSpade };
			LinkedList<Card> droppedCards = new LinkedList<Card>();
			for (int l = 0; l < lyts.length; l++) {
				LinearLayout lyt = lyts[l];
				if (lyt.getVisibility() != Button.GONE) {
					boolean svChanged = false;
					final int count = lyt.getChildCount();
					for (int i = 0; i < count; i++) {
						ScoredValuedButton btn = (ScoredValuedButton) lyt
								.getChildAt(i);
						final int checked = btn.clearChecked();
						if (checked > 0) {
							Card c = Card.from(btn.getValue());
							for (int j = 0; j < checked; j++) {
								droppedCards.add(c);
							}
							if (btn.getAmount() == 0) {
								btn.setVisibility(Button.GONE);
								svChanged = true;
							}
						}
					}
					if (svChanged) {
						boolean empty = true;
						for (int i = 0; i < count; i++) {
							if (lyt.getChildAt(i).getVisibility() != Button.GONE) {
								empty = false;
								break;
							}
						}
						if (empty) {
							horizontalScrollViews[l].setVisibility(Button.GONE);
						}
					}
				}
			}
			if (!droppedCards.isEmpty()) {
				game.drop(droppedCards.toArray(new Card[droppedCards.size()]));
				if (game.hasHistory()) {
					btnPrev.setImageDrawable(getResources().getDrawable(
							R.drawable.step_revert));
				}
				updateInfo();
			}
			if (game.getCardsLeft() == 0) {
				askForNewGame();
			}
		}
	};

	private TimerTask checkCardsTask;

	private OnLongClickListener clearHistoryClickListener = new OnLongClickListener() {
		public boolean onLongClick(View paramView) {
			askForNewGame();
			return true;
		}
	};

	private OnClickListener playGameClickListener = new OnClickListener() {
		public void onClick(View v) {
			if (checkCardsTask != null) {
				checkCardsTask.cancel();
			}
			checkCardsTask = new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(checkCards);
				}
			};
			timer.schedule(checkCardsTask, 1000);
		}
	};

	public void previousStep(final View view) {
		if (this.game.hasHistory()) {
			game.restore();
			updateBoard();
			if (!game.hasHistory()) {
				btnPrev.setImageDrawable(getResources().getDrawable(
						R.drawable.step_back));
			}
		} else {
			askForNewGame();
		}
	}

	private void askForNewGame() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提示")
				.setMessage("开始新游戏吗？")
				.setCancelable(false)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Toast.makeText(PlayGameActivity.this,
								getResources().getString(R.string.text_new),
								Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(PlayGameActivity.this,
								ChooseLevelActivity.class);
						intent.putExtra(MyIntents.SETS,
								PlayGameActivity.this.sets);
						startActivity(intent);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		builder.create().show();
	}

	private void updateBoard() {
		for (Suit s : SUITS) {
			if (this.trump != Suit.JOKER && s == this.trump) {
				final HorizontalScrollView hsv = getCardsHorizontalScrollView(s);
				hsv.setVisibility(View.GONE);
				continue;
			} else {
				final HorizontalScrollView hsv = getCardsHorizontalScrollView(s);
				final Card[] cards = this.game.getCards(s);
				if (cards.length > 0) {
					LinearLayout lyt = getCardsLayout(s);
					if (cards.length != getCardsLength(lyt)) {
						hsv.setVisibility(View.INVISIBLE);

						lyt.removeAllViews();

						int amount = 0;
						Card card = null;
						for (Card c : cards) {
							if (card != c) {
								if (card != null) {
									lyt.addView(createCardButton(card, amount));
								}
								card = c;
								amount = 1;
							} else {
								amount++;
							}
						}
						if (amount > 0 && card != null) {
							lyt.addView(createCardButton(card, amount));
						}
						hsv.setVisibility(View.VISIBLE);
					}
				} else {
					hsv.setVisibility(View.GONE);
				}
			}
		}
	}

	private final int getCardsLength(LinearLayout lyt) {
		int sum = 0;
		final int count = lyt.getChildCount();
		for (int i = 0; i < count; i++) {
			ScoredValuedButton btn = (ScoredValuedButton) lyt.getChildAt(i);
			if (btn.getVisibility() != Button.GONE) {
				sum += btn.getAmount();
			}
		}
		return sum;
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

	private ScoredValuedButton createCardButton(Card c, int amount) {
		final String color;
		if (c == Card.from(Suit.JOKER, Card.BLACK_JOKER_NAME)) {
			color = GameHelper.getColorString(0x606060);
		} else if (c == Card.from(Suit.JOKER, Card.COLOR_JOKER_NAME)) {
			color = GameHelper.getColorString(0x609ac0);
		} else {
			color = GameHelper.getColorString(c.suit.color.color);
		}
		final String name = Card.toString(c.name);
		final char symbol = c.suit.symbol;
		final StringBuffer btnText = new StringBuffer();
		if (c == Card.from(Suit.JOKER, Card.BLACK_JOKER_NAME)
				|| c == Card.from(Suit.JOKER, Card.COLOR_JOKER_NAME)) {
			btnText.append("<font color='").append(color).append("'>")
					.append(name).append("</font>");
		} else {
			btnText.append(name).append("<br />").append("<font color='")
					.append(color).append("'>").append(symbol)
					.append("</font>");
		}
		ScoredValuedButton btn = new ScoredValuedButton(PlayGameActivity.this,
				5, c, btnText.toString(), null, amount);
		btn.setOnClickListener(playGameClickListener);
		return btn;
	}

	@Override
	protected void onPause() {
		super.onPause();
		Editor editor = getPreferences(Activity.MODE_PRIVATE).edit();
		editor.putInt(MyIntents.SETS, this.sets)
				.putInt(MyIntents.LEVEL, this.level)
				.putInt(MyIntents.TRUMP, this.trump.id);
		editor.putString(MyIntents.CARDS,
				GameHelper.toString(game.cardsContainer.played));
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
			this.game = new Game(this.sets, this.level, this.trump);
			BitSet tmp = GameHelper.fromString(preferences.getString(
					MyIntents.CARDS, null));
			this.game.cardsContainer.copyPlayed(tmp);
		} else {
			this.game = new Game(this.sets, this.level, this.trump);
		}

		final String levelName = GameHelper.getLevelName(this.level);
		StringBuffer settingsText = new StringBuffer();
		final Resources resources = getResources();
		settingsText.append(Card.getSetsName(this.sets)).append(
				resources.getString(R.string.text_set_unit));
		this.textSettingSets.setText(settingsText.toString());

		settingsText = new StringBuffer();
		settingsText.append(resources.getString(R.string.text_level_unit))
				.append(levelName);
		this.textSettingLevel.setText(settingsText.toString());

		final String trumpName = GameHelper.getTrumpName(trump, this.level);
		final String color = GameHelper.getColorString(trump.color.color);
		settingsText = new StringBuffer();

		settingsText.append("<font color='").append(color).append("'>")
				.append(trump.symbol).append("</font>").append(trumpName);
		this.textSettingTrump.setText(Html.fromHtml(settingsText.toString()));

		updateBoard();

		updateInfo();
	}

	private void updateInfo() {
		final Resources resources = getResources();
		StringBuffer infoText = new StringBuffer();
		infoText.append(String.valueOf(this.game.getCardsLeft())).append(
				resources.getString(R.string.text_card_unit));
		this.textInfoCardsLeft.setText(infoText.toString());

		infoText = new StringBuffer();
		infoText.append(String.valueOf(this.game.getPointsLeft())).append(
				resources.getString(R.string.text_point_unit));
		this.textInfoPointsLeft.setText(infoText.toString());

		infoText = new StringBuffer();
		infoText.append(String.valueOf(this.game.getPointsGained())).append(
				resources.getString(R.string.text_point_unit));
		this.textInfoPointsGained.setText(infoText.toString());
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
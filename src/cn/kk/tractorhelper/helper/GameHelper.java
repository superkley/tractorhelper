package cn.kk.tractorhelper.helper;

import cn.kk.tractorhelper.game.Card;
import cn.kk.tractorhelper.game.Suit;

public final class GameHelper {

    public static final String getLevelName(final int level) {
        final String name;
        if (level != Card.LEVEL_JOKER) {
            name = Card.toString(level);
        } else {
            name = Card.toString(Card.LEVEL_JOKER);
        }
        return name;
    }

    public static final String getTrumpName(final Suit trump, final int level) {
        final String name;
        if (trump != Suit.JOKER) {
            name = GameHelper.getLevelName(level);
        } else {
            name = trump.name;
        }
        return name;
    }

    public static final String getColorString(final int color) {
        return String.format("#%06x", Integer.valueOf(color));
    }

}

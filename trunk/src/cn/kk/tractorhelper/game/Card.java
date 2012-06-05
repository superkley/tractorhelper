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
package cn.kk.tractorhelper.game;

public final class Card {
    public static final int SET_CARDS = 54;

    public static final int BLACK_JOKER_NAME = 99;

    public static final int COLOR_JOKER_NAME = 100;

    public static final int LEVEL_JOKER = -1;

    private final static Card[] ALL_CARDS = new Card[SET_CARDS];

    public final Suit suit;

    public final int name;

    public final int points;
    
    public final int idx;

    private Card(final Suit suit, final int name, final int idx) {
        this.suit = suit;
        this.name = name;
        switch (name) {
        case 5:
            this.points = 5;
            break;
        case 10:
        case 13:
            this.points = 10;
            break;
        default:
            this.points = 0;
        }
        this.idx = idx;
    }

    @Override
    public String toString() {
        return suit.symbol + toString(name);
    }

    public static final String toString(final int name) {
        switch (name) {
        case 1:
            return "A";
        case 11:
            return "J";
        case 12:
            return "Q";
        case 13:
            return "K";
        case BLACK_JOKER_NAME:
            return "小<br />鬼";
        case COLOR_JOKER_NAME:
            return "大<br />鬼";
        case LEVEL_JOKER:
            return "无主";
        default:
            return String.valueOf(name);
        }
    }

    public static final Card from(final Suit suit, final int cardName) {
        final int idx;
        if (cardName == BLACK_JOKER_NAME) {
            idx = SET_CARDS - 2;
        } else if (cardName == COLOR_JOKER_NAME) {
            idx = SET_CARDS - 1;
        } else {
            idx = suit.id * 13 + cardName - 1;
        }
        Card result = ALL_CARDS[idx];
        if (result == null) {
            result = new Card(suit, cardName, idx);
            ALL_CARDS[idx] = result;
        }
        return result;
    }

    public static final String getSetsName(final int sets) {
        switch (sets) {
        case 1:
            return "一";
        case 2:
            return "二";
        case 3:
            return "三";
        case 4:
            return "四";
        case 5:
            return "五";
        case 6:
            return "六";
        }
        return String.valueOf(sets);
    }

	public static Card from(final int id) {
		return ALL_CARDS[id];
	}
}

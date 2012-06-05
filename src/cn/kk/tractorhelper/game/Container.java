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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;

public final class Container {
    private static Container instance;

    public final Card[] cards;

    public final BitSet played;

    public final int totalCards;

    public static final Container get(final int sets) {
        final int totalCards = Card.SET_CARDS * sets;
        if (instance == null || instance.totalCards != totalCards) {
            instance = new Container(totalCards);
        } else {
            instance.clear();
        }
        return instance;
    }

    private Container(final int totalCards) {
        this.totalCards = totalCards;
        this.cards = new Card[totalCards];
        this.played = new BitSet(totalCards);
        initialize();
    }

    private final void initialize() {
        final int sets = this.cards.length / Card.SET_CARDS;
        int cardIdx = 0;
        for (int i = 0; i < sets; i++) {
            this.cards[cardIdx++] = Card.from(Suit.JOKER, Card.COLOR_JOKER_NAME);
        }
        for (int i = 0; i < sets; i++) {
            this.cards[cardIdx++] = Card.from(Suit.JOKER, Card.BLACK_JOKER_NAME);
        }
        for (int suitId = 0; suitId < 4; suitId++) {
            for (int i = 0; i < sets; i++) {
                this.cards[cardIdx++] = Card.from(Suit.fromId(suitId), 1);
            }
            for (int cardName = 13; cardName > 1; cardName--) {
                for (int i = 0; i < sets; i++) {
                    this.cards[cardIdx++] = Card.from(Suit.fromId(suitId), cardName);
                }
            }
        }
    }

    public final void clear() {
        this.played.clear();
    }

    public final Card[] get(final Suit suit, final int level, final Suit trump) {
        final ArrayList<Card> result = new ArrayList<Card>();
        if (suit == Suit.JOKER) {
            // jokers
            for (int i = 0; i < totalCards; i++) {
                final Card card = cards[i];
                if (!this.played.get(i) && card.suit == Suit.JOKER) {
                    result.add(card);
                }
            }
            if (level != Card.LEVEL_JOKER) {
                if (trump != Suit.JOKER) {
                    // color level trumps
                    for (int i = 0; i < totalCards; i++) {
                        final Card card = cards[i];
                        if (!this.played.get(i) && card.suit == trump && card.name == level) {
                            result.add(card);
                        }
                    }
                }
                // level trumps
                for (int i = 0; i < totalCards; i++) {
                    final Card card = cards[i];
                    if (!this.played.get(i) && card.suit != trump && card.name == level) {
                        result.add(card);
                    }
                }
                if (trump != Suit.JOKER) {
                    // normal trumps
                    for (int i = 0; i < totalCards; i++) {
                        final Card card = cards[i];
                        if (!this.played.get(i) && card.name != level && card.suit == trump) {
                            result.add(card);
                        }
                    }
                }
            }
            return result.toArray(new Card[result.size()]);
        } else {
            for (int i = 0; i < totalCards; i++) {
                final Card card = cards[i];
                if (!this.played.get(i) && (card.name != level && card.suit == suit)) {
                    result.add(card);
                }
            }
            return result.toArray(new Card[result.size()]);
        }
    }

    public final int select(final Card[] cards) {
        int points = 0;
        for (Card c : cards) {
            if (select(c)) {
                points += c.points;
            }
        }
        return points;
    }

    public final boolean select(final Card c) {
        for (int i = 0; i < totalCards; i++) {
            final Card card = cards[i];
            if (!this.played.get(i) && c == card) {
                this.played.set(i);
                return true;
            }
        }
        return false;
    }

    public final int countCardsPlayed() {
        return played.cardinality();
    }

    public final int countPointsPlayed() {
        int result = 0;
        for (int i = 0; i < totalCards; i++) {
            final Card card = cards[i];
            if (played.get(i)) {
                result += card.points;
            }
        }
        return result;
    }


    public final void copyPlayed(BitSet played) {
    	this.played.clear();
    	this.played.or(played);
    }
}

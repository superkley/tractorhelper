package cn.kk.tractorhelper.game;

import java.util.ArrayList;
import java.util.Arrays;

public final class Container {
    private static Container instance;

    public final Card[] cards;

    public final boolean[] played;

    public static final Container get(final int sets) {
        final int totalCards = Card.SET_CARDS * sets;
        if (instance == null || instance.cards.length != totalCards) {
            instance = new Container(totalCards);
        } else {
            instance.clear();
        }
        return instance;
    }

    private Container(final int totalCards) {
        this.cards = new Card[totalCards];
        this.played = new boolean[totalCards];
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
        Arrays.fill(this.played, false);
    }

    public final Card[] get(final Suit suit, final int level, final Suit trump) {
        final ArrayList<Card> result = new ArrayList<Card>();
        if (suit == Suit.JOKER) {
            // jokers
            for (int i = 0; i < cards.length; i++) {
                final Card card = cards[i];
                if (!played[i] && card.suit == Suit.JOKER) {
                    result.add(card);
                }
            }
            if (level != Card.LEVEL_JOKER) {
                if (trump != Suit.JOKER) {
                    // color level trumps
                    for (int i = 0; i < cards.length; i++) {
                        final Card card = cards[i];
                        if (!played[i] && card.suit == trump && card.name == level) {
                            result.add(card);
                        }
                    }
                }
                // level trumps
                for (int i = 0; i < cards.length; i++) {
                    final Card card = cards[i];
                    if (!played[i] && card.suit != trump && card.name == level) {
                        result.add(card);
                    }
                }
                if (trump != Suit.JOKER) {
                    // normal trumps
                    for (int i = 0; i < cards.length; i++) {
                        final Card card = cards[i];
                        if (!played[i] && card.name != level && card.suit == trump) {
                            result.add(card);
                        }
                    }
                }
            }
            return result.toArray(new Card[result.size()]);
        } else {
            for (int i = 0; i < cards.length; i++) {
                final Card card = cards[i];
                if (!played[i] && (card.name != level && card.suit == suit)) {
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
        for (int i = 0; i < cards.length; i++) {
            final Card card = cards[i];
            if (!played[i] && c == card) {
                played[i] = true;
                return true;
            }
        }
        return false;
    }

    public final int countCardsPlayed() {
        int result = 0;
        for (int i = 0; i < played.length; i++) {
            if (played[i]) {
                result++;
            }
        }
        return result;
    }

    public final int countPointsPlayed() {
        int result = 0;
        for (int i = 0; i < cards.length; i++) {
            final Card card = cards[i];
            if (played[i]) {
                result += card.points;
            }
        }
        return result;
    }
}

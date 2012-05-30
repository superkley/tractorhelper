package cn.kk.tractorhelper.game;

import java.util.BitSet;

public final class History {
    private static History instance;

    public static final int MAX_MEMORIES = 10;

    public final BitSet[] memories;

    public final int[] points;
    
    public final int totalCards;

    private int idx = -1;

    public static final History get(final int sets) {
        final int totalCards = Card.SET_CARDS * sets;
        if (instance == null || totalCards != instance.totalCards) {
            instance = new History(totalCards);
        } else {
            instance.clear();
        }
        return instance;
    }

    private History(final int totalCards) {
        this.totalCards = totalCards;
        this.memories = new BitSet[MAX_MEMORIES];
        for (int i = 0; i < MAX_MEMORIES; i++) {
            this.memories[i] = new BitSet(totalCards);
        }
        this.points = new int[MAX_MEMORIES];
    }

    public final void store(final BitSet played, final int points) {
        if (this.idx < MAX_MEMORIES - 1) {
            this.idx++;
        } else {
            final BitSet tmp = this.memories[0];
            for (int i = 0; i < MAX_MEMORIES - 1; i++) {
                this.memories[i] = this.memories[i + 1];
                this.points[i] = this.points[i + 1];
            }
            this.memories[this.idx] = tmp;
        }
        final BitSet selected = this.memories[this.idx];
        selected.clear();
        selected.or(played);
        this.points[this.idx] = points;
    }

    public final synchronized int restore(final BitSet played) {
        if (hasHistory()) {
            played.clear();
            played.or(this.memories[this.idx]);
            return this.points[this.idx--];
        }
        return -1;
    }

    public final boolean hasHistory() {
        return this.idx >= 0;
    }

    public final void clear() {
        this.idx = -1;
    }
}

package cn.kk.tractorhelper.game;

public final class History {
    private static History instance;

    public static final int MAX_MEMORIES = 10;

    public final boolean[][] memories;

    public final int[] points;

    private int idx = -1;

    public static final History get(final int sets) {
        final int totalCards = Card.SET_CARDS * sets;
        if (instance == null || totalCards != instance.memories[0].length) {
            instance = new History(totalCards);
        } else {
            instance.clear();
        }
        return instance;
    }

    private History(final int totalCards) {
        this.memories = new boolean[MAX_MEMORIES][totalCards];
        this.points = new int[MAX_MEMORIES];
    }

    public final void store(final boolean[] played, final int points) {
        if (this.idx < MAX_MEMORIES - 1) {
            this.idx++;
        } else {
            final boolean[] tmp = this.memories[0];
            for (int i = 0; i < MAX_MEMORIES - 1; i++) {
                this.memories[i] = this.memories[i + 1];
                this.points[i] = this.points[i + 1];
            }
            this.memories[this.idx] = tmp;
        }
        System.arraycopy(played, 0, this.memories[this.idx], 0, played.length);
        this.points[this.idx] = points;
    }

    public final synchronized int restore(final boolean[] played) {
        if (hasHistory()) {
            System.arraycopy(this.memories[this.idx], 0, played, 0, played.length);
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

    public final void store(final boolean[] played) {
        store(played, 0);
    }
}

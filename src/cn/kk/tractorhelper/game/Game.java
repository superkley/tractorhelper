package cn.kk.tractorhelper.game;

public final class Game {
    public final Container cardsContainer;

    private final History history;

    public final int level;

    public final Suit trump;

    public final int pointsTotal;

    public final int pointsNeeded;

    private int pointsGained;

    public synchronized int getCardsLeft() {
        return cardsContainer.cards.length - cardsContainer.countCardsPlayed();
    }

    public synchronized int getPointsLeft() {
        return pointsTotal - cardsContainer.countPointsPlayed();
    }

    public synchronized int getPointsGained() {
        return pointsGained;
    }

    public Game(final int sets, final int level, final Suit trump) {
        this.level = level;
        this.trump = trump;
        this.cardsContainer = Container.get(sets);
        this.history = History.get(sets);
        this.pointsTotal = sets * 100;
        this.pointsNeeded = sets * 40;
    }

    public synchronized Card[] getCards(final Suit suit) {
        return this.cardsContainer.get(suit, this.level, this.trump);
    }

    public synchronized void drop(final Card... cards) {
        this.history.store(this.cardsContainer.played, this.pointsGained);
        this.pointsGained += this.cardsContainer.select(cards);
    }

    public synchronized void select(final Card... cards) {
        this.history.store(this.cardsContainer.played, this.pointsGained);
        this.cardsContainer.select(cards);
    }

    public synchronized void restore() {
        final int points = this.history.restore(this.cardsContainer.played);
        this.pointsGained = points;
    }

    public synchronized void clear() {
        this.cardsContainer.clear();
        this.history.clear();
        this.pointsGained = 0;
    }

    public synchronized boolean hasHistory() {
        return this.history.hasHistory();
    }
}

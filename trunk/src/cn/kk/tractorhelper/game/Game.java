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

	public void clearHistory() {
		this.history.clear();
	}
}

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

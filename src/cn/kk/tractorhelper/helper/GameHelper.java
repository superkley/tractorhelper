package cn.kk.tractorhelper.helper;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.BitSet;

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

	public static final boolean[] fromBitSet(final BitSet bs, final int length) {
		final boolean[] b = new boolean[length];
		for (int idx = bs.nextSetBit(0); idx >= 0; idx = bs.nextSetBit(idx + 1)) {
			b[idx] = true;
		}
		return b;
	}

	public static final BitSet toBitSet(final boolean[] b) {
		final int l = b.length;
		final BitSet bs = new BitSet(l);
		for (int i = 0; i < l; i++) {
			if (b[i]) {
				bs.set(i);
			}
		}
		return bs;
	}

	public final static String toString(final BitSet bs) {
		try {
			return new String(toByteArray(bs), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public final static BitSet fromString(final String data) {
		try {
			return fromByteArray(data.getBytes("ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

	}

	public final static byte[] toByteArray(final BitSet bs) {
		final int bitsLength = bs.length();
		final int bytesLength = (bitsLength + 7) / 8;
		final byte[] bytes = new byte[bytesLength];
		int byteIdx;
		int bitIdx;
		for (int idx = bs.nextSetBit(0); idx >= 0; idx = bs.nextSetBit(idx + 1)) {
			byteIdx = bytesLength - idx / 8 - 1;
			bitIdx = idx % 8;
			bytes[byteIdx] |= 1 << bitIdx;
		}
		return bytes;
	}

	public final static BitSet fromByteArray(final byte[] bytes) {
		final int bitsLength = bytes.length * 8;
		final BitSet bs = new BitSet(bitsLength);
		for (int i = 0; i < bitsLength; i++) {
			if (((bytes[bytes.length - i / 8 - 1] >> (i % 8)) & 1) == 1) {
				bs.set(i);
			}
		}
		return bs;
	}
}

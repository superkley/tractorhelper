package cn.kk.tractorhelper.game;

public enum Suit {
    SPADE(Color.BLACK, 3, '♠', "黑桃"), HEART(Color.RED, 2, '♥', "红心"), CLUB(Color.BLACK, 1, '♣', "梅花"), DIAMOND(
            Color.RED, 0, '♦', "方块"), JOKER(Color.NONE, -1, '⊙', "无主");

    public final Color color;

    public final int id;

    public final char symbol;

    public final String name;

    Suit(final Color c, final int id, final char symbol, final String name) {
        this.color = c;
        this.id = id;
        this.symbol = symbol;
        this.name = name;
    }

    public static final Suit fromId(final int suitId) {
        switch (suitId) {
        case 3:
            return SPADE;
        case 2:
            return HEART;
        case 1:
            return CLUB;
        case 0:
            return DIAMOND;
        default:
            return JOKER;
        }
    }
}

package org.chessvn.android.app.chessvn;

import java.util.List;

public class ChessvnRule {

	/** number of columns and rows */
	public static byte boardNumber = 10;
	public static boolean isChessvn = true;
	public static byte totalField = (byte) (boardNumber * boardNumber);

	static final byte EM = 0; // EMpty.
	static final byte WK = 1; // White King
	static final byte WQ = 2; // White Queen
	static final byte WR = 3; // White Rook
	static final byte WB = 4; // White Bishop
	static final byte WN = 5; // White Knight
	static final byte WP = 6; // White Pawn
	static final byte WKD = 7; // White Khida
	static final byte BK = 8; // Black King
	static final byte BQ = 9; // Black Queen
	static final byte BR = 10; // Black Rook
	static final byte BB = 11; // Black Bishop
	static final byte BN = 12; // Black Knight
	static final byte BP = 13; // Black Pawn
	static final byte BKD = 14; // Black Khida
	static final byte[] startPosition = { WR, EM, WN, WB, WQ, WK, WB, WN, EM,
			WR, EM, WKD, EM, EM, EM, EM, EM, EM, WKD, EM, WP, WP, WP, WP, WP,
			WP, WP, WP, WP, WP, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM,
			EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM,
			EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, EM, BP, BP, BP, BP, BP, BP,
			BP, BP, BP, BP, EM, BKD, EM, EM, EM, EM, EM, EM, BKD, EM, BR, EM,
			BN, BB, BQ, BK, BB, BN, EM, BR };
	String[] name = { "  ", "WK", "WQ", "WR", "WB", "WN", "WP", "WKD", "BK",
			"BQ", "BR", "BB", "BN", "BP", "BKD" };
	byte[] field = new byte[totalField];
	boolean whiteKingHasMoved;
	boolean blackKingHasMoved;
	boolean leftBlackRookHasMoved;
	boolean rightBlackRookHasMoved;
	boolean leftWhiteRookHasMoved;
	boolean rightWhiteRookHasMoved;
	boolean isWhiteTurn;
	byte blackKing, whiteKing; // Positions of these.
	byte lastFrom = totalField; // 3 bits per rank/file
	byte lastTo = totalField; // (x>>3) gives rank, (x&7) gives file
	byte pawnPromotion = 2; // Queen. In black player is turn, 6 will be added.

	public List<Byte> allKhidaLeftPos = null;
	public List<Byte> allKhidaRightPos = null;

	public boolean isWhiteFigure(byte f) {
		return f > 0 && f < 8;
	}

	public boolean isBlackFigure(byte f) {
		return f > 7 && f < 15;
	}

	public byte boardCoords(String s) {
		// must check carefully
		return ((byte) (((int) s.charAt(0) - (int) 'a') + 
				(((int) (Character.getNumericValue(s.charAt(1)))) * 10)));// need to change to
														// getPos();
	}

	public String algebraic(byte co) {
		// must check carefully
		// Converts internal board coordinates to algebraic notation.
		return ("" + (char) ((int) 'a' + getFile(co)) + "" + (getRank(co)));
	}

	public String algebraic(int co) {
		// Converts internal board coordinates to algebraic notation.
		return algebraic((byte) co);
	}

	public String algebraic(int rank, int file) {
		return algebraic(getPos(rank, file));
	}

	private byte getRank(byte pos) {
		// maybe optimized later
		return (byte) (pos / 10);
	}

	private byte getFile(byte pos) {
		// maybe optimized later
		return (byte) (pos % 10);
	}

	private byte getPos(byte Rank, byte File) {
		// maybe optimized later
		return (byte) (Rank * 10 + File);
	}

	private byte getPos(int Rank, int File) {
		// maybe optimized later
		return (byte) (Rank * 10 + File);
	}

	private int abs(int x) {
		return (x < 0) ? -x : x;
	}

	public byte abs(byte a) {
		return (byte) ((a < 0) ? -a : a);
	}

	public void init() {
		// Initializes the board ("new game")
		for (int n = 0; n < totalField; n++)
			field[n] = startPosition[n];
		isWhiteTurn = true;
		whiteKingHasMoved = false;
		blackKingHasMoved = false;
		leftBlackRookHasMoved = false;
		rightBlackRookHasMoved = false;
		leftWhiteRookHasMoved = false;
		rightWhiteRookHasMoved = false;
		lastFrom = totalField;// 64; // not a valid Position!
		lastTo = totalField;// 64;
		whiteKing = getPos(0, 5);// (7<<3)+4;
		blackKing = getPos(9, 5);// (0<<3)+4;
	}

	public ChessvnRule clonedBoard() {
		ChessvnRule newBoard = new ChessvnRule();
		for (int n = 0; n < totalField; n++)
			newBoard.field[n] = field[n];
		newBoard.isWhiteTurn = isWhiteTurn;
		newBoard.whiteKingHasMoved = whiteKingHasMoved;
		newBoard.blackKingHasMoved = blackKingHasMoved;
		newBoard.leftBlackRookHasMoved = leftBlackRookHasMoved;
		newBoard.rightBlackRookHasMoved = rightBlackRookHasMoved;
		newBoard.leftWhiteRookHasMoved = leftWhiteRookHasMoved;
		newBoard.rightWhiteRookHasMoved = rightWhiteRookHasMoved;
		newBoard.lastFrom = lastFrom;
		newBoard.lastTo = lastTo;
		newBoard.blackKing = blackKing;
		newBoard.whiteKing = whiteKing;
		return newBoard;
	}
}

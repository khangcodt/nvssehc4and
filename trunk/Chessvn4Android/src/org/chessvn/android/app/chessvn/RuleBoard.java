package org.chessvn.android.app.chessvn;

import java.util.ArrayList;
import java.util.List;

public class RuleBoard {

	/** number of columns and rows */
	public static byte boardNumber = 10;
	public static boolean isChessvn = true;
	public static byte totalField = (byte) (boardNumber * boardNumber);

	public static final byte EM = 0; // EMpty.
	public static final byte WK = 1; // White King
	public static final byte WQ = 2; // White Queen
	public static final byte WR = 3; // White Rook
	public static final byte WB = 4; // White Bishop
	public static final byte WN = 5; // White Knight
	public static final byte WP = 6; // White Pawn
	public static final byte WKD = 7; // White Khida
	public static final byte BK = 8; // Black King
	public static final byte BQ = 9; // Black Queen
	public static final byte BR = 10; // Black Rook
	public static final byte BB = 11; // Black Bishop
	public static final byte BN = 12; // Black Knight
	public static final byte BP = 13; // Black Pawn
	public static final byte BKD = 14; // Black Khida
	public static final byte[] startPosition = {
	 	WR,EM,WN,WB,WQ,WK,WB,WN,EM,WR,
	 	EM,WKD,EM,EM,EM,EM,EM,EM,WKD,EM,
	 	WP,WP,WP,WP,WP,WP,WP,WP,WP,WP,
		EM,EM,EM,EM,EM,EM,EM,EM,EM,EM,			
		EM,EM,EM,EM,EM,EM,EM,EM,EM,EM,			
		EM,EM,EM,EM,EM,EM,EM,EM,EM,EM,			
		EM,EM,EM,EM,EM,EM,EM,EM,EM,EM,			
		BP,BP,BP,BP,BP,BP,BP,BP,BP,BP,
		EM,BKD,EM,EM,EM,EM,EM,EM,BKD,EM,
		BR,EM,BN,BB,BQ,BK,BB,BN,EM,BR 	};
	String[] name = 	{"  ", "WK","WQ","WR","WB","WN","WP","WKD",
 						       "BK","BQ","BR","BB","BN","BP","BKD"};
	public byte[] field = new byte[totalField];
	public List<Byte> allKhidaLeftPos = null;
	public List<Byte> allKhidaRightPos = null;
	public boolean userIsWhite = true;
	public boolean userIsBlack = true;
	public boolean isLocked = false;
	
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

	public byte getRank(byte pos) {
		// maybe optimized later
		return (byte) (pos / 10);
	}

	public byte getFile(byte pos) {
		// maybe optimized later
		return (byte) (pos % 10);
	}

	public byte getPos(byte Rank, byte File) {
		// maybe optimized later
		return (byte) (Rank * 10 + File);
	}

	public byte getPos(int Rank, int File) {
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

	public RuleBoard clonedBoard() {
		RuleBoard newBoard = new RuleBoard();
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
	
	/**
	 * Check if the piece in position can handle
	 * @param pos
	 * @return true if piece can handle
	 */
	public boolean isHandledPiece(byte pos) {
		return !(isLocked || 
				(isWhiteFigure(field[pos])!=isWhiteTurn) || 
				(!userIsWhite && isWhiteTurn) || 
				(!userIsBlack && !isWhiteTurn));
	}
	
	/**
	 * Check if no piece in position
	 * @param pos
	 * @return true if no piece in position
	 */
	public boolean isEmptyField(byte pos) {
		return field[pos] == EM;
	}
	
	public boolean isEqualTo(RuleBoard rb) {
		// Whether rooks or kings have moved is not considered here.
		for (int n = 0; n < totalField; n++)
			if (field[n] != rb.field[n])
				return false;
		return true;
	}

	public boolean makeMove(String move) {
		// This method is for external use only.
		byte from, to;
		if (move.length()!=4 && move.length()!=5)
			return false;
		if(move.length()==5) {
			if(move.substring(4,5).equals("q"))
				pawnPromotion=WQ;
			else if(move.substring(4,5).equals("r"))
				pawnPromotion=WR;
			else if(move.substring(4,5).equals("b"))
				pawnPromotion=WB;
			else if(move.substring(4,5).equals("n"))
				pawnPromotion=WN;
			else 
				return false;
		}
		from = boardCoords(move.substring(0,2));
		if(from<0 || from>(totalField-1)) return false;
		to = boardCoords(move.substring(2,4));
		if(to<0 || to>(totalField-1)) return false;
		return makeMove(from,to);
	}
	
	public boolean makeMove(int fromRank, int fromFile, int toRank, int toFile) {
		return makeMove(getPos(fromRank, fromFile),getPos(toRank, toFile));//how to change
	}

	public boolean makeMove(byte from, byte to) {
		// This method is for external use only.
//		String move;
//		if(jcb == null) return false; // Should not happen!
		if(field[from]!=EM && isPossibleMove(from,to))  {
			move(from,to);
			return true;
		}
		return false;
	}

	public boolean isAttackedByWhite(byte f) {
		// A field is attacked, even if the attacker would leave his king in check.
		for(byte x=0; x<totalField; x++)
			if(isWhiteFigure(field[x]) && mightBePossibleMove(x,f)) {
				return true;
			}
		return false;
	}

	public boolean isAttackedByWhite(int f) {
		return isAttackedByWhite((byte) f);
	}

	public boolean isAttackedByBlack(byte f) {
		for(byte x=0; x<totalField; x++)
			if(isBlackFigure(field[x]) && mightBePossibleMove(x,f)) {
//				if(f==whiteKing) log.info("White King is checked by piece in position " + x);
				return true;
			}
		return false;
	}

	public boolean isAttackedByBlack(int f) {
		return isAttackedByBlack((byte) f);
	}

	public boolean isPossibleMove(byte from,byte to) {
//		if(isAttackedByBlack(this.whiteKing)) log.info("White King is checked");
//		if(isAttackedByWhite(this.blackKing)) log.info("Black King is checked");
		return (tryMove(from,to)!=null);
	}

	public RuleBoard tryMove(byte from, byte to) {
		if (field[from] != EM && (isWhiteTurn == isWhiteFigure(field[from]))
				&& mightBePossibleMove(from, to)) {
			RuleBoard rb = RuleBoard.this.clonedBoard(); // nb= new board
			rb.move(from, to);
			if (isWhiteTurn) {
				if (rb.whiteKing < totalField && rb.whiteKing > 0
						&& !rb.isAttackedByBlack(rb.whiteKing))
					return rb;
			} else if (rb.blackKing < totalField && rb.blackKing > 0
					&& !rb.isAttackedByWhite(rb.blackKing))
				return rb;
		}
		return null;
	}

	public boolean mightBePossibleMove(byte from, byte to) {
//		log.info("from = " + from);
//		log.info("to = " + to);
		// Might return true even if the King is left in check.
		if(field[from]==EM || from==to) return false;
		if(to<0 || to>(totalField-1)) return false;
		// No capturing of own figures:
		if(isWhiteFigure(field[from]) && isWhiteFigure(field[to]))
			return false;
		if(isBlackFigure(field[from]) && isBlackFigure(field[to]))
			return false;
		if(field[from]==WK)
			return WKmightMove(from,to);
		else if(field[from]==WQ)
			return WQmightMove(from,to);
		else if(field[from]==WR)
			return WRmightMove(from,to);
		else if(field[from]==WB)
			return WBmightMove(from,to);
		else if(field[from]==WN)
			return WNmightMove(from,to);
		else if(field[from]==WP)
			return WPmightMove(from,to);
		else if(field[from]==WKD)
			return WKDmightMove(from,to);
		else if(field[from]==BK)
			return BKmightMove(from,to);
		else if(field[from]==BQ)
			return BQmightMove(from,to);
		else if(field[from]==BR)
			return BRmightMove(from,to);
		else if(field[from]==BB)
			return BBmightMove(from,to);
		else if(field[from]==BN)
			return BNmightMove(from,to);
		else if(field[from]==BP)
			return BPmightMove(from,to);
		else if(field[from]==BKD)
			return BKDmightMove(from,to);//change here
		return false; // Should never be reached.
	}
	
	public boolean onlyKingsLeft() {
		for(int n=0; n<totalField; n++)
			if(field[n]!=EM && field[n]!=WK && field[n]!=BK)
				return false;
		return true;
	}
	
	public boolean gameIsFinished() {
		// Looks for possible moves for current player. Returns immediatly
		// if a move was found, and thus the game cannot be finished.
		// To do: if there are only the kings left, the game is inevitable
		// drawn.
		if(onlyKingsLeft())
			return true;
		for(byte from=0; from<totalField; from++)	
			if(field[from]>0 && (isWhiteTurn == (field[from]<8)))//recheck field[from]<7
				for(byte to=0; to<totalField; to++)
					if(isPossibleMove(from,to))
						return false;
		return true;
	}

	public void checkFinish() {
		// Gives an appropriate message if the game is finished.

	}

	protected void move(byte from, byte to) {
		// Moves that arrive this point have been checked 
		// to be valid.
		int file=getFile(from);//from&7; //int rank=from>>3;//how to change
//		int newFile=to&7; int newRank=to>>3;//how to change	
		int newFile=getFile(to); int newRank=getRank(to);
		byte tmp;
		
//		No caslte first
		/*if (file == 4) {
			if ((field[from]==WK) && file==4 && newFile==2) {
				// White castles queen side:
				move((byte) 0,(byte) 3);
				isWhiteTurn = !isWhiteTurn;
			} else if ((field[from]==WK) && file==4 && newFile==6) {
				// White castles king side:
				move((byte) 7,(byte) 5);
				isWhiteTurn = !isWhiteTurn;
			} else if ((field[from]==BK) && file==4 && newFile==2) {
				// Black castles queen side:
				move((byte) ((7<<3)+0),(byte) ((7<<3)+3));
				isWhiteTurn = !isWhiteTurn;
			} else if ((field[from]==BK) && file==4 && newFile==6) {
				// Black castles king side:
				move((byte) ((7<<3)+7),(byte) ((7<<3)+5));
				isWhiteTurn = !isWhiteTurn;
			}
		}*/
		
//		consider and check
		if ((field[to]==EM && field[from]==WP) && file!=newFile) {
			// Capturing en passant (white):
			field[getPos(5, newFile)]=EM;
		} else if ((field[to]==EM && field[from]==BP) && file!=newFile) {
			// Capturing en passant (black):
			field[getPos(4, newFile)]=EM;
		}  
		if (field[to]==WK) //Oops!
			whiteKing=totalField; // Illegal field!
		else if (field[to]==BK)
			blackKing=totalField;	//
		// No, thats not a joke. Its required for move checking.
		tmp=field[from];
		field[from]=EM;	
		field[to]=tmp;
		lastFrom=from;
		lastTo=to;
		isWhiteTurn = !isWhiteTurn;
		if (field[to]==WP && newRank==9) {
			field[to]=pawnPromotion;
			pawnPromotion=2;//===========??????========
		} else if (field[to]==BP && newRank==0) {
			field[to]=(byte) (pawnPromotion+7);
			pawnPromotion=2;//===========??????========
		} else if (field[to]==WR && from==0)
			leftWhiteRookHasMoved=true;
		else if (field[to]==WR && from==9)
			rightWhiteRookHasMoved=true;
		else if (field[to]==BR && from==90)
			leftBlackRookHasMoved=true;
		else if (field[to]==BR && from==99)
			rightBlackRookHasMoved=true;
		else if (field[to]==WK) {
			whiteKingHasMoved=true;
			whiteKing=to;
		} else if (field[to]==BK) {
			blackKingHasMoved=true;
			blackKing=to;
		}
//		log.info(toString());
	}	

	// Black knight
	public boolean BNmightMove(byte from, byte to) {
		return !(to==from)
			&& getFile(from) != getFile(to) && getRank(from) != getRank(to)
			&& abs(getFile(from)-getFile(to))+abs(getRank(from)-getRank(to))==3 	
			&& !(isBlackFigure(field[to]));
	}

	// black queen
	public boolean BQmightMove(byte from, byte to) {
		return !(to==from)
			&& !(isBlackFigure(field[to]))
			&& checkLineOfSight(from,to);
	}

	// black bishop
	public boolean BBmightMove(byte from, byte to) {
		return !(to==from)
			&& (abs(getRank(to)-getRank(from)) == abs(getFile(to)-getFile(from)))
			&& !(isBlackFigure(field[to]))
			&& checkLineOfSight(from,to);
	}

	// black rook
	public boolean BRmightMove(byte from, byte to) {
		return !(to==from)
			&& (getRank(to)==getRank(from) || getFile(to)==getFile(from))
			&& !(isBlackFigure(field[to]))
			&& checkLineOfSight(from,to);
	}
	// black pawn
//	not check with new board of chessvn
	public boolean BPmightMove(byte from, byte to) {
		return !(to==from)
//				single move
			&& ( (getRank(to) == getRank(from)-1 && getFile(to) == (getFile(from)) 
					&& field[to]==EM)
//				capture
				|| (getRank(to) == getRank(from)-1 && abs(getFile(to)-(getFile(from)))==1 	
					&& (isWhiteFigure(field[to]))
//				first move
				|| (getRank(from)==7 && getRank(to)==5 && getFile(to)==(getFile(from)) 
					&& field[getPos((byte)6, getFile(to))]==EM
					&& field[getPos((byte)5, getFile(to))]==EM)	
//				en passant, maybe? not check carefully yet!!
				|| (getRank(to) == getRank(from)-1 && abs(getFile(to)-(getFile(from)))==1 	
					&& getRank(to)==2 && field[to]==EM 	
					&& field[to+10]==WP	
					&& (getRank(lastFrom)==1) && (getFile(lastFrom)==getFile(to))		
					&& (getRank(lastTo)==3) && (getFile(lastTo)==getFile(to)))));		
	}
	public boolean BPmightAttack(byte from, byte to) {
		return getRank(to) == getRank(from)-1 && abs(getFile(to)-(getFile(from)))==1	
			&& !isBlackFigure(field[to]);
	}
	
	//black khida
	public boolean BKDmightMove(byte from, byte to) {
//		log.info("Testing Black Khida movement...");
		return (to!=from)
				&&!(isBlackFigure(field[to]))
				&&((allKhidaLeftPos.contains(to)&&(allKhidaLeftPos.contains(from)||from==81))//left Khida
						||(allKhidaRightPos.contains(to)&&(allKhidaRightPos.contains(from)||from==88)))//right Khida
				&&(!isWhiteFigure(field[to])||canKhidaCapture(from, to, true));
	}

	public boolean BKDmightAttack(byte from, byte to) {
		return (to!=from)
				&&canKhidaCapture(from, to, true)
				&&((allKhidaLeftPos.contains(to)&&(allKhidaLeftPos.contains(from)||from==11))
						||(allKhidaRightPos.contains(to)&&(allKhidaRightPos.contains(from)||from==18)));
	}

	// black king
	public boolean BKmightAttack(byte from, byte to) {
		return !(to==from)
			&& ( abs(getRank(to)-getRank(from))<=1 && abs(getFile(to)-(getFile(from)))<=1)	
			&& !(isBlackFigure(field[to]));
	}
	public boolean BKmightMove(byte from, byte to) {
		return from!=to
			&& !(isBlackFigure(field[to]))
			&& ( (abs(getRank(to)-getRank(from))<=1 && abs(getFile(to)-getFile(from))<=1));	
//				|| (to==(7<<3)+2 && BKcouldCastleQueenSide())	
//				|| (to==(7<<3)+6 && BKcouldCastleKingSide()));		
	}
	public boolean BKcouldCastleQueenSide() {
		return !blackKingHasMoved && !leftBlackRookHasMoved
			&& checkLineOfSight((byte) (7<<3)+0,(byte) (7<<3)+4)	
			&& !isAttackedByWhite((byte) (7<<3)+4)		
			&& !isAttackedByWhite((byte) (7<<3)+3);		
	}
	public boolean BKcouldCastleKingSide() {
		return !blackKingHasMoved && !rightBlackRookHasMoved
			&& checkLineOfSight((byte) (7<<3)+7,(byte) (7<<3)+4) 		
			&& !isAttackedByWhite((byte) (7<<3)+4)	
			&& !isAttackedByWhite((byte) (7<<3)+5);		
	}

	// white knight
	public boolean WNmightMove(byte from, byte to) {
			return !(to==from)
			&& !(isWhiteFigure(field[to]))
			&& abs((getFile(from))-getFile(to))+abs(getRank(from)-getRank(to))==3			
			&& (getFile(from)) != getFile(to) && getRank(from) != getRank(to);	
	}

	// white queen
	public boolean WQmightMove(byte from, byte to) {
		return !(to==from)
			&& !(isWhiteFigure(field[to]))
			&& checkLineOfSight(from,to);
	}

	// white bishop
	public boolean WBmightMove(byte from, byte to) {
		return !(to==from)
			&& abs(getRank(to)-getRank(from)) == abs(getFile(to)-(getFile(from)))			
			&& !(isWhiteFigure(field[to]))
			&& checkLineOfSight(from,to);
	}

	// white rook	
	public boolean WRmightMove(byte from, byte to) {
		return !(to==from)
			&& ( (getRank(to)==getRank(from)) || (getFile(to)==getFile(from)))		
			&& !(isWhiteFigure(field[to]))
			&& checkLineOfSight(from,to);
	}

	//white khida
	public boolean WKDmightMove(byte from, byte to) {
//		log.info("Testing White Khida movement...");
		return (to!=from)
				&&!(isWhiteFigure(field[to]))
				&&((allKhidaLeftPos.contains(to)&&(allKhidaLeftPos.contains(from)||from==11))//left Khida
						||(allKhidaRightPos.contains(to)&&(allKhidaRightPos.contains(from)||from==18)))//right Khida
				&&(!isBlackFigure(field[to])||canKhidaCapture(from, to, false))
				;
	}

	public boolean WKDmightAttack(byte from, byte to) {
		return (to!=from)
				&&canKhidaCapture(from, to, false)
				&&((allKhidaLeftPos.contains(to)&&(allKhidaLeftPos.contains(from)||from==81))
						||(allKhidaRightPos.contains(to)&&(allKhidaRightPos.contains(from)||from==88)));
	}

	// white king
	public boolean WKmightAttack(byte from, byte to) {
		return !(to==from)
			&& ( abs(getRank(to)-getRank(from))<=1 && abs(getFile(to)-(getFile(from)))<=1)		
			&& !(isWhiteFigure(field[to]));
	}
	public boolean WKmightMove(byte from, byte to) {
		return from!=to
			&& !(isWhiteFigure(field[to]))
			&& ( (abs(getRank(to)-getRank(from))<=1 && abs(getFile(to)-getFile(from))<=1));	
//				|| (to==2 && WKcouldCastleQueenSide())
//				|| (to==6 && WKcouldCastleKingSide()));
	}
	public boolean WKcouldCastleQueenSide() {
		return !whiteKingHasMoved && !leftWhiteRookHasMoved
			&& checkLineOfSight((byte) 0,(byte) 4) 
			&& !isAttackedByBlack((byte) 4)
			&& !isAttackedByBlack((byte) 3);
	}
	public boolean WKcouldCastleKingSide() {
		return !whiteKingHasMoved && !rightWhiteRookHasMoved
			&& checkLineOfSight((byte) 7,(byte) 4) 
			&& !isAttackedByBlack((byte) 4)
			&& !isAttackedByBlack((byte) 5);
	}

	// white pawn
	public boolean WPmightMove(byte from, byte to) {
		return !(to==from)
//				single move
			&& ( (getRank(to) == getRank(from)+1 && (getFile(to) == getFile(from)) 		
					&& field[to]==EM)
//				capture					
				|| (getRank(to) == getRank(from)+1 && abs(getFile(to)-getFile(from))==1 	
					&& (isBlackFigure(field[to]))
//				first move					
				|| (getRank(from)==2 && getRank(to)==4 && getFile(to)==getFile(from) 		
					&& field[getPos((byte)3, getFile(to))]==EM		
					&& field[getPos((byte)4, getFile(to))]==EM)		
//				en passant, maybe? not check carefully yet!!					
				|| (getRank(to) == getRank(from)+1 && abs(getFile(to)-getFile(from))==1 		
					&& (getRank(to)==5) && field[to]==EM	
					&& field[to-10]==BP	
					&& (getRank(lastFrom)==6) && (getFile(lastFrom)==getFile(to))		
					&& (getRank(lastTo)==4) && (getFile(lastTo)==getFile(to)))));		
	}
	
	public boolean WPmightAttack(byte from, byte to) {
		return getRank(to) == getRank(from)+1 && abs(getFile(to)-(getFile(from)))==1		
			&& !isWhiteFigure(field[to]);
	}


	// 
	// private methods
	//

//	methods for Khida piece rule
	private List<Byte> getAllKhidaLeftPos() {
		List<Byte> resultList = new ArrayList<Byte>();
		
		for(byte i=3;i<6;i++) resultList.add(i);
		for(byte i=12;i<17;i++) resultList.add(i);
		for(byte i=21;i<27;i++) resultList.add(i);
		for(byte i=30;i<37;i++) resultList.add(i);
		for(byte i=40;i<46;i++) resultList.add(i);
		for(byte i=50;i<54;i++) resultList.add(i);
		for(byte i=60;i<63;i++) resultList.add(i);
		for(byte i=71;i<73;i++) resultList.add(i);
		resultList.add((byte)82); resultList.add((byte)93);
		
		return resultList;
	}
	
	private List<Byte> getAllKhidaRightPos() {
		List<Byte> resultList = new ArrayList<Byte>();
		
		resultList.add((byte)6); resultList.add((byte)17);
		for(byte i=27;i<29;i++) resultList.add(i);
		for(byte i=37;i<40;i++) resultList.add(i);
		for(byte i=46;i<50;i++) resultList.add(i);
		for(byte i=54;i<60;i++) resultList.add(i);
		for(byte i=63;i<70;i++) resultList.add(i);
		for(byte i=73;i<79;i++) resultList.add(i);
		for(byte i=83;i<88;i++) resultList.add(i);
		for(byte i=94;i<97;i++) resultList.add(i);
		
		return resultList;
	}
	
	private boolean isKhidaMatchWithKnightPos(byte from, byte to) {
		return abs(getRank(from)-getRank(to))+abs(getFile(from)-getFile(to))==3&& getFile(from) != getFile(to) && getRank(from) != getRank(to);
	}
	
	private boolean isKhidaMatchWithBishopPos(byte from, byte to) {
		return abs(getRank(from)-getRank(to)) == abs(getFile(from)-getFile(to))&& checkLineOfSight(from, to);
	}

	private boolean isKhidaMatchWithRookPos(byte from, byte to) {
		return (getRank(from)==getRank(to) || getFile(from)==getFile(to)) && checkLineOfSight(from, to);
	}
	
	private boolean isKhidaMatchedWithKnight(byte from, boolean isWKnight) {
		byte KnightPiece = isWKnight?WN:BN;
		byte rank = getRank(from);
		byte file = getFile(from);
		for(byte checkRank=(byte) (rank-2);checkRank<=rank+2;checkRank++) {
			if(checkRank<0||checkRank>9) continue;
			if(checkRank==rank) continue;
			for(byte checkFile=(byte) (file-2);checkFile<=file+2; checkFile++) {
				if(checkFile<0||checkFile>9) continue;
				if(checkFile==file) continue;
				if((field[getPos(checkRank,checkFile)] == KnightPiece)&&(abs(checkRank-rank)+abs(checkFile-file)==3)) return true;
			}
		}
		return false;
	}

	private boolean isKhidaMatchedWithBishop(byte from, boolean isWBishop) {
		byte BishopPiece = isWBishop?WB:BB;
		byte rank = getRank(from);
		byte file = getFile(from);
		byte checkRank = rank;
		byte checkFile = file;
		while(checkRank>0&&checkFile>0) {
			checkRank--;
			checkFile--;
			if(field[getPos(checkRank,checkFile)] == BishopPiece) {
				return true;
			}
			if(isWhiteFigure(field[getPos(checkRank,checkFile)])) break;
			if(isBlackFigure(field[getPos(checkRank,checkFile)])) break;
		}

		checkRank = rank;
		checkFile = file;
		while(checkRank<9&&checkFile<9) {
			checkRank++;
			checkFile++;
			if(field[getPos(checkRank,checkFile)] == BishopPiece) {
				return true;
			}
			if(isWhiteFigure(field[getPos(checkRank,checkFile)])) break;
			if(isBlackFigure(field[getPos(checkRank,checkFile)])) break;
		}
		
		checkRank = rank;
		checkFile = file;
		while(checkRank>0&&checkFile<9) {
			checkRank--;
			checkFile++;
			if(field[getPos(checkRank,checkFile)] == BishopPiece) {
				return true;
			}
			if(isWhiteFigure(field[getPos(checkRank,checkFile)])) break;
			if(isBlackFigure(field[getPos(checkRank,checkFile)])) break;
		}

		checkRank = rank;
		checkFile = file;
		while(checkRank<9&&checkFile>0) {
			checkRank++;
			checkFile--;
			if(field[getPos(checkRank,checkFile)] == BishopPiece) {
				return true;
			}
			if(isWhiteFigure(field[getPos(checkRank,checkFile)])) break;
			if(isBlackFigure(field[getPos(checkRank,checkFile)])) break;
		}
					
		return false;
	}
	
	private boolean isKhidaMatchedWithRook(byte from, boolean isWRook) {
		byte RookPiece = isWRook?WR:BR;
		byte rank = getRank(from);
		byte file = getFile(from);
		byte checkRank = rank;
		byte checkFile = file;
		while(checkRank>0) {
			checkRank--;
			if(field[getPos(checkRank,checkFile)] == RookPiece) {
				return true;
			}
			if(isWhiteFigure(field[getPos(checkRank,checkFile)])) break;
			if(isBlackFigure(field[getPos(checkRank,checkFile)])) break;
		}
		
		checkRank = rank;
		checkFile = file;
		while(checkRank<9) {
			checkRank++;
			if(field[getPos(checkRank,checkFile)] == RookPiece) {
				return true;
			}
			if(isWhiteFigure(field[getPos(checkRank,checkFile)])) break;
			if(isBlackFigure(field[getPos(checkRank,checkFile)])) break;
		}
		
		checkRank = rank;
		checkFile = file;
		while(checkFile>0) {
			checkFile--;
			if(field[getPos(checkRank,checkFile)] == RookPiece) {
				return true;
			}
			if(isWhiteFigure(field[getPos(checkRank,checkFile)])) break;
			if(isBlackFigure(field[getPos(checkRank,checkFile)])) break;
		}
		
		checkRank = rank;
		checkFile = file;
		while(checkFile<9) {
			checkFile++;
			if(field[getPos(checkRank,checkFile)] == RookPiece) {
				return true;
			}
			if(isWhiteFigure(field[getPos(checkRank,checkFile)])) break;
			if(isBlackFigure(field[getPos(checkRank,checkFile)])) break;
		}
		return false;
	}
	
	private boolean canKhidaCapture(byte from, byte to, boolean isWhiteCap) {
		boolean capPiece = isWhiteCap?isWhiteFigure(field[to]):isBlackFigure(field[to]);
		return (capPiece)&&
				   (isKhidaMatchWithKnightPos(from, to)&&isKhidaMatchedWithKnight(from, !isWhiteCap))
				 ||(isKhidaMatchWithBishopPos(from, to)&&isKhidaMatchedWithBishop(from, !isWhiteCap))
				 ||(isKhidaMatchWithRookPos(from, to)&&isKhidaMatchedWithRook(from, !isWhiteCap));
	}
	
	private boolean checkLineOfSight(int from, int to) {	
		// Using byte values in Java suxx!
		// You do: (byte) 1 + (byte) 2 + 3, and you get int.
		return checkLineOfSight((byte) from, (byte) to);
	}

	private boolean checkLineOfSight(byte from, byte to) {	
		int fromRank=getRank(from);		
		int fromFile=getFile(from);		
		int toRank=getRank(to);			
		int toFile=getFile(to);			
		// Checks whether there is any piece on the line between the two given fields.
		if(fromRank!=toRank && fromFile!=toFile
			&& abs(toRank-fromRank)!=abs(toFile-fromFile))
			return false;   // Only horizontal, vertical, or diagonal lines
							// are allowed.
		int rdiff=(toRank>=fromRank)?((toRank>fromRank)?1:0):-1; 
		int fdiff=(toFile>=fromFile)?((toFile>fromFile)?1:0):-1; 
		int rank=fromRank+rdiff;
		int file=fromFile+fdiff;
		while (rank!=toRank || file!=toFile) {
			if(field[getPos(rank,file)]!=EM) return false;		//how to change	
			rank+=rdiff; 
			file+=fdiff;
		}
		return true;
	}

	private void sendMessage(String s) {
		// The interface to show messages on the current JChessBoard,
		// just for stylistic reasons.
//		lastMessage=s;
//		if(jcb!=null)
//			jcb.showMessage(s);
	}
	
	private void initConstructor() {
		if(isChessvn) boardNumber = 10;//Chessvn Board
		else boardNumber = 8;//Chess Board
		
		allKhidaLeftPos = getAllKhidaLeftPos();
		allKhidaRightPos = getAllKhidaRightPos();
		init();
	}
	
	public RuleBoard() {
		initConstructor();
	}
	
	public RuleBoard(boolean isChessvn) {
		RuleBoard.isChessvn = isChessvn;
		initConstructor();
	}
}

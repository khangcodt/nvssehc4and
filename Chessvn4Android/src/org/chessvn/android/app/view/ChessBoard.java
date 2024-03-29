package org.chessvn.android.app.view;

import java.util.List;

import org.chessvn.android.app.R;
import org.chessvn.android.app.chessvn.RuleBoard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ChessBoard extends View {

	/** number of columns and rows */
	public static int boardNumber = 10;
	public static boolean isChessvn = true;
	public boolean reverseBoard = false;
	public boolean showMove = true;
	
	private int cellSize;
	private int circleRadius = 15;
	private List<Byte> allPossibleMoves = null;
	private boolean showMoveOn = false;//bit that turn on or off showMove depend on Touch Event
	private int touchedX, touchedY;//store coordinate of touched point
	private byte touchedPiece;
	private int MAX_SIZE = cellSize*(boardNumber);
//	private Drawable selPieceImage = null;//image to draw with touched point

	// color theme
	private Paint cellColor;
	private Paint cellWhiteColor;
	private Paint cirColor;
	private Paint cirWhiteColor;
	private Paint showMoveColor;
	
	RuleBoard rb;
	
	private void initConstructor() {
		isChessvn = RuleBoard.isChessvn;
		boardNumber = RuleBoard.boardNumber;
		rb = new RuleBoard(isChessvn);
		
		touchedX = touchedY = 0;
		touchedPiece = RuleBoard.EM;
		MAX_SIZE = cellSize*(boardNumber);
		
		cellSize = getCellSize();
		cellColor = new Paint();
		cellWhiteColor = new Paint();
		cirColor = new Paint();
		cirWhiteColor = new Paint();
		showMoveColor = new Paint();
		setColorTheme();
	}
	
	public ChessBoard(Context context, AttributeSet attrs) {
		super(context, attrs);
		initConstructor();
	}
	
	public ChessBoard(Context context, AttributeSet attrs, RuleBoard rb) {
		super(context, attrs);
		this.rb = rb;
		initConstructor();
	}
	
	public ChessBoard(Context context, AttributeSet attrs, boolean isChessvn) {
		super(context, attrs);
		ChessBoard.isChessvn = isChessvn;
		initConstructor();
	}
	
	public ChessBoard(Context context, AttributeSet attrs, boolean isChessvn, RuleBoard rb) {
		super(context, attrs);
		ChessBoard.isChessvn = isChessvn;
		this.rb = rb;
		initConstructor();
	}
	
	/**
	 * Set the board to given state
	 * @param rb The state of board (position of pieces on board) to set
	 */
	public void setRuleBoard(RuleBoard rb) {
		this.rb = rb;
		invalidate();
	}
	
	/**
	 * Set color theme for the board, maybe improved to similar to DroidFish theme
	 */
	public void setColorTheme() {
		cellColor.setColor(getResources().getColor(R.color.cellColor));
		cellWhiteColor.setColor(getResources().getColor(R.color.cellWhiteColor));
		cirColor.setColor(getResources().getColor(R.color.cirColor));
		cirWhiteColor.setColor(getResources().getColor(R.color.cirWhiteColor));
		showMoveColor.setColor(getResources().getColor(R.color.showMoveColor));
	}
	
	public Drawable loadPieceImage(byte piece) {
		Drawable returnPiece = null;
		switch (piece) {
		case RuleBoard.WK: returnPiece = getResources().getDrawable(R.drawable.wk); break;
		case RuleBoard.WQ: returnPiece = getResources().getDrawable(R.drawable.wq); break;
		case RuleBoard.WR: returnPiece = getResources().getDrawable(R.drawable.wr); break;
		case RuleBoard.WB: returnPiece = getResources().getDrawable(R.drawable.wb); break;
		case RuleBoard.WN: returnPiece = getResources().getDrawable(R.drawable.wn); break;
		case RuleBoard.WP: returnPiece = getResources().getDrawable(R.drawable.wp); break;
		case RuleBoard.WKD: returnPiece = getResources().getDrawable(R.drawable.wkd); break;
		case RuleBoard.BK: returnPiece = getResources().getDrawable(R.drawable.bk); break;
		case RuleBoard.BQ: returnPiece = getResources().getDrawable(R.drawable.bq); break;
		case RuleBoard.BR: returnPiece = getResources().getDrawable(R.drawable.br); break;
		case RuleBoard.BB: returnPiece = getResources().getDrawable(R.drawable.bb); break;
		case RuleBoard.BN: returnPiece = getResources().getDrawable(R.drawable.bn); break;
		case RuleBoard.BP: returnPiece = getResources().getDrawable(R.drawable.bp); break;
		case RuleBoard.BKD: returnPiece = getResources().getDrawable(R.drawable.bkd); break;
		}
		return returnPiece;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Log.i("ChessBoard", "Redawing view ...");
		cellSize = getCellSize();
//		reverseBoard = true;//test
//		drawCell(canvas, 4, 4, cellColor);//test
		drawAllCells(canvas);
		if(isChessvn) drawAllCircles(canvas);
//		if(rb == null) rb = new RuleBoard(isChessvn);
		drawAllPiecesOnBoard(rb, canvas);
//		drawFreePiece(canvas, cellSize+134, cellSize+176, RuleBoard.BR);//test
		if(showMoveOn) {
			showMoveOnBoard(canvas);
		}
		drawFreePiece(canvas, touchedX, touchedY, touchedPiece);//draw touched piece
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
    	byte selectedPos = getPosOnEvent(event);
		int action = event.getAction();
		switch(action) {
        case (MotionEvent.ACTION_DOWN) :
        	//if(cannot handle piece or board) return;
        	if(!rb.isHandledPiece(selectedPos)) {
        		Log.i("ChessBoard", "cannot handle piece");
        		return true;
        	}
        
        	//set touched point
        	touchedX = (int) event.getX();//maybe check involve with reversed and side of x,y
        	touchedY = (int) event.getY();
        
        	//else if no piece in this position: revalidate(); return;
        	if(rb.isEmptyField(selectedPos)) {
        		Log.i("ChessBoard", "Empty piece");
        		invalidate();
        		return true;
        	}
        	
        	//esle if show move true: showmove();
        	if(showMove) {
        		showMoveOn = true;
        		Log.i("ChessBoard", "Showing move now ...");
        		Log.i("ChessBoard", "selectedPos = " + selectedPos);
        		showMove(selectedPos);
        	}
        	return true;
        case (MotionEvent.ACTION_MOVE) :
        	Log.i("ChessBoard", "Move action");
        	//if(cannot handle piece or board) return;
        	if(!rb.isHandledPiece(selectedPos)) {
        		Log.i("ChessBoard", "cannot handle piece");
        		return true;
        	}
        
        	//append piece to touch point if support drag piece to move
        	drawTouchedPiece(event);
        	
        case (MotionEvent.ACTION_UP) :
        	showMoveOn = false;
        	invalidate();
        	Log.i("ChessBoard", "Showing move off now.");
        	//check if piece can move to new position
        	//make move
        	//show message
        	//check game is finished?
        	return true;
        default : return super.onTouchEvent(event);
		}
//		return false;
	}
	
	/**
	 * Get the position (0 - 63) on board corresponding to the coordinates of a mouse event.
	 * @param event mouse event.
	 * @return The position corresponding to the mouse event, or -1 if not in board.
	 */
	public byte getPosOnEvent(MotionEvent event) {//check carefully calculation of rank and file
		byte posSelected = -1;
		int xCrd = (int)(event.getX());
        int yCrd = (int)(event.getY());
        if ((xCrd >= 0) && (yCrd >= 0) && (cellSize > 0)) {
        	int file = xCrd/cellSize;
        	int rank = boardNumber - 1 - yCrd/cellSize;
        	if((rank >= 0)&&(rank < boardNumber)&&(file >= 0)&&(file < boardNumber)) {
        		if(reverseBoard) {
        			rank = boardNumber - 1 - rank;
        			file = boardNumber - 1 - file;
        		}
        		posSelected = RuleBoard.getPos(rank, file);
        	}
        	Log.i("ChessBoard", "xCrd = " + xCrd);
        	Log.i("ChessBoard", "yCrd = " + yCrd);
//        	Log.i("ChessBoard", "cellSize = " + cellSize);
//        	Log.i("ChessBoard", "rank = " + rank);
//        	Log.i("ChessBoard", "file = " + file);
//        	Log.i("ChessBoard", "posSelected = " + posSelected);
        }
		return posSelected;
	}
	
	/**
	 * Update all possible moves of board from piece of given position
	 * @param fromPos
	 */
	public void showMove(byte fromPos) {
		this.allPossibleMoves = rb.getAllPossibleMoves(fromPos);
		invalidate();//update view
	}
	
	/**
	 * Show real all  possible moves on the board 
	 * @param canvas
	 */
	private void showMoveOnBoard(Canvas canvas) {
		if(allPossibleMoves == null) return;
		int rank, file;
		for(byte curPos : allPossibleMoves) {
			rank = boardNumber - 1 - RuleBoard.getRank(curPos);
			file = RuleBoard.getFile(curPos);
			if(reverseBoard) {
				rank = boardNumber - 1 - rank;
    			file = boardNumber - 1 - file;
			}
			drawCell(canvas, rank, file, showMoveColor);
		}
	}
	
	/**
	 * Draw square cell of the board. The board is square so don't need to check reversed
	 * @param canvas
	 */
	private void drawAllCells(Canvas canvas) {
		for (byte rank = 0; rank < boardNumber; rank++)
			for (byte file = 0; file < boardNumber; file++) {
				if ((rank + file & 1) == 1) drawCell(canvas, rank, file, cellColor); 
				else drawCell(canvas, rank, file, cellWhiteColor);
			}
	}

	private void drawCell(Canvas canvas, int rank, int file, Paint color) {
		int xLeft = file * cellSize;
		int yLeft = rank * cellSize;
		canvas.drawRect(xLeft, yLeft, xLeft + cellSize, yLeft + cellSize, color);
	}
	
	/**
	 * Statically draw position of circles on the board of chessvn
	 * @param canvas
	 */
	private void drawAllCircles(Canvas canvas) {
		Paint cirBightColor = reverseBoard ? cirColor : cirWhiteColor;
		Paint cirDarkColor = reverseBoard ? cirWhiteColor : cirColor;
		
		for(int i=3;i<6;i++) 	drawCircle(canvas, 0, i, cirBightColor);
		for(int i=6;i<7;i++) 	drawCircle(canvas, 0, i, cirDarkColor);
		for(int i=2;i<7;i++) 	drawCircle(canvas, 1, i, cirBightColor);
		for(int i=7;i<8;i++) 	drawCircle(canvas, 1, i, cirDarkColor);
		for(int i=1;i<7;i++) 	drawCircle(canvas, 2, i, cirBightColor);
		for(int i=7;i<9;i++) 	drawCircle(canvas, 2, i, cirDarkColor);
		for(int i=0;i<7;i++) 	drawCircle(canvas, 3, i, cirBightColor);
		for(int i=7;i<10;i++) 	drawCircle(canvas, 3, i, cirDarkColor);
		for(int i=0;i<6;i++) 	drawCircle(canvas, 4, i, cirBightColor);
		for(int i=6;i<10;i++) 	drawCircle(canvas, 4, i, cirDarkColor);
		for(int i=0;i<4;i++) 	drawCircle(canvas, 5, i, cirBightColor);
		for(int i=4;i<10;i++) 	drawCircle(canvas, 5, i, cirDarkColor);
		for(int i=0;i<3;i++) 	drawCircle(canvas, 6, i, cirBightColor);
		for(int i=3;i<10;i++) 	drawCircle(canvas, 6, i, cirDarkColor);
		for(int i=1;i<3;i++) 	drawCircle(canvas, 7, i, cirBightColor);
		for(int i=3;i<9;i++) 	drawCircle(canvas, 7, i, cirDarkColor);
		for(int i=2;i<3;i++) 	drawCircle(canvas, 8, i, cirBightColor);
		for(int i=3;i<8;i++) 	drawCircle(canvas, 8, i, cirDarkColor);
		for(int i=3;i<4;i++) 	drawCircle(canvas, 9, i, cirBightColor);
		for(int i=4;i<7;i++) 	drawCircle(canvas, 9, i, cirDarkColor);
	}
	
	/**
	 * Draw on circle in given position
	 * @param canvas
	 * @param rank
	 * @param file
	 * @param color
	 */
	private void drawCircle(Canvas canvas, int rank, int file, Paint color) {
		int cx,cy;
//		if(reverseBoard) {
//			cx = (chessvnBoardNumber - 1 - file)*cellSize + cellSize/2;
//			cy = rank*cellSize + cellSize/2;
//		} else {
			cx = file*cellSize + cellSize/2;
			cy = (boardNumber - 1 - rank)*cellSize + cellSize/2;
		canvas.drawCircle(cx, cy, circleRadius, color);
	}
	
	/**
	 * Draw given piece in given position
	 * @param canvas
	 * @param rank
	 * @param file
	 * @param piece
	 */
	public void drawPiece(Canvas canvas, int rank, int file, byte piece) {
		int left = file*cellSize;
		int top = (boardNumber - 1 - rank)*cellSize;
		int right = left + cellSize;
		int bottom = top + cellSize;
		Drawable thispiece = loadPieceImage(piece);
		if(thispiece != null) {
			thispiece.setBounds(left, top, right, bottom);
			thispiece.draw(canvas);
		}
	}
	
	/**
	 * Draw given piece in given position
	 * @param canvas
	 * @param pos
	 * @param piece
	 */
	public void drawPiece(Canvas canvas, byte pos, byte piece) {
		drawPiece(canvas, RuleBoard.getRank(pos), RuleBoard.getFile(pos), piece);
	}
	
	/**
	 * Draw piece in free position, for moving action
	 * @param canvas
	 * @param xPos
	 * @param yPos
	 * @param piece
	 */
	public void drawFreePiece(Canvas canvas, int xPos, int yPos, byte piece) {
		Drawable thispiece = loadPieceImage(piece);
		if(thispiece != null) {
			thispiece.setBounds(xPos, yPos, xPos+cellSize, yPos+cellSize);
			thispiece.draw(canvas);
		}
	}
	
	/**
	 * Draw all pieces on given RuleBoard
	 * @param rb
	 * @param canvas
	 */
	public void drawAllPiecesOnBoard(RuleBoard rb, Canvas canvas) {
		for(byte rank = 0; rank < boardNumber; rank++) 
			for(byte file = 0; file < boardNumber; file++) {
				drawPiece(canvas, rank, file, rb.field[RuleBoard.getPos(rank, file)]);
			}
	}
	
	public void drawTouchedPiece(MotionEvent event) {
		byte selectedPos = getPosOnEvent(event);
		touchedPiece = rb.field[selectedPos];
		if(touchedPiece == RuleBoard.EM) {
			return;
		}
//		Drawable selPieceImage = loadPieceImage(selectedPiece);
		//remove selected piece on board
		rb.field[selectedPos] = RuleBoard.EM;
		//draw touched piece
		gettouchedPoint(event);
		invalidate();
	}
	
	/**
	 * update touched point
	 * @param event
	 */
	private void gettouchedPoint(MotionEvent event) {
		touchedX = (int) event.getX();//maybe check involve with reversed and side of x,y
    	touchedY = (int) event.getY();
    	Log.i("ChessBoard", "touchedX = " + touchedX);
    	Log.i("ChessBoard", "touchedY = " + touchedY);
	}
	
	/**
	 * Get size of cell on the board
	 * @return
	 */
	public int getCellSize() {
		int width = getWidth();
		int height = getHeight();
		return Math.min(width/boardNumber, height/boardNumber);
	}
}

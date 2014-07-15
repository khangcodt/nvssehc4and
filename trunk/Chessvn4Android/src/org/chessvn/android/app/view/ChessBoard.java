package org.chessvn.android.app.view;

import org.chessvn.android.app.R;
import org.chessvn.android.app.chessvn.RuleBoard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
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

	// color theme
	private Paint cellColor;
	private Paint cellWhiteColor;
	private Paint cirColor;
	private Paint cirWhiteColor;
	private Paint showMoveColor;
	
	RuleBoard rb;
	
	private void initConstructor() {
//		if(isChessvn) boardNumber = 10;//Chessvn Board
//		else boardNumber = 8;//Chess Board
		isChessvn = RuleBoard.isChessvn;
		boardNumber = RuleBoard.boardNumber;
		rb = new RuleBoard(isChessvn);
		
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
//		reverseBoard = true;//test
		drawAllCells(canvas);
		if(isChessvn) drawAllCircles(canvas);
//		if(rb == null) rb = new RuleBoard(isChessvn);
		drawAllPiecesOnBoard(rb, canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch(action) {
        case (MotionEvent.ACTION_DOWN) :
        	byte selectedPos = getPosOnEvent(event);
        	//if(cannot handle piece or board) return;
        	if(!rb.isHandledPiece(selectedPos)) return true;
        	
        	//else if no piece in this position: revalidate(); return;
        	if(rb.isEmptyField(selectedPos)) {
        		invalidate();
        		return true;
        	}
        	
        	//esle if show move true: showmove();
        	if(showMove) {
//        		showMove(canvas, selectedPos);
        	}
        	return true;
        case (MotionEvent.ACTION_UP) :
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
        	int rank = xCrd/cellSize;
        	if((rank >= 0)&&(rank < boardNumber)&&(file >= 0)&&(file < boardNumber)) {
        		if(reverseBoard) {
        			rank = boardNumber - 1 - rank;
        			file = boardNumber - 1 - file;
        		}
        		posSelected = rb.getPos(rank, file);
        	}
        }
        
		return posSelected;
	}
	
	public void showMove(Canvas canvas, byte fromPos) {
		for (byte rank = 0; rank < boardNumber; rank++)
			for (byte file = 0; file < boardNumber; file++) {
				byte toPos = rb.getPos(rank, file);
				if(rb.isPossibleMove(fromPos, toPos)) {
					if(reverseBoard) drawCell(canvas, boardNumber - 1 - rank, boardNumber - 1 - file, showMoveColor);
					else drawCell(canvas, rank, file, showMoveColor);
				}
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
		int right = (file+1)*cellSize;
		int bottom = (boardNumber - rank)*cellSize;
		Drawable thispiece = loadPieceImage(piece);
		if(thispiece != null) {
			thispiece.setBounds(left, top, right, bottom);
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
				drawPiece(canvas, rank, file, rb.field[rb.getPos(rank, file)]);
			}
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

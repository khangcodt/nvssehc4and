package org.chessvn.android.app.view;

import org.chessvn.android.app.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ChessBoard extends View {

	/** number of columns and rows */
	public static int boardNumber = 10;
	public static boolean isChessvn = true;
	public boolean reverseBoard =false;
	private int cellSize;
	private int circleRadius = 11;

	// color theme
	private Paint cellColor;
	private Paint cellWhiteColor;
	private Paint cirColor;
	private Paint cirWhiteColor;
	private Paint showMoveColor;

	public ChessBoard(Context context, AttributeSet attrs) {
		super(context, attrs);

		if(isChessvn) boardNumber = 10;//Chessvn Board
		else boardNumber = 8;//Chess Board
		
		cellColor = new Paint();
		cellWhiteColor = new Paint();
		cirColor = new Paint();
		cirWhiteColor = new Paint();
		showMoveColor = new Paint();
		setColorTheme();
	}

	public ChessBoard(Context context, AttributeSet attrs, boolean isChessvn) {
		super(context, attrs);
		
		ChessBoard.isChessvn = isChessvn;
		if(isChessvn) boardNumber = 10;//Chessvn Board
		else boardNumber = 8;//Chess Board
		
		cellColor = new Paint();
		cellWhiteColor = new Paint();
		cirColor = new Paint();
		cirWhiteColor = new Paint();
		showMoveColor = new Paint();
		setColorTheme();
	}
	
	public void setColorTheme() {
		cellColor.setColor(getResources().getColor(R.color.cellColor));
		cellWhiteColor.setColor(getResources().getColor(R.color.cellWhiteColor));
		cirColor.setColor(getResources().getColor(R.color.cirColor));
		cirWhiteColor.setColor(getResources().getColor(R.color.cirWhiteColor));
		showMoveColor.setColor(getResources().getColor(R.color.showMoveColor));
	}

	@Override
	protected void onDraw(Canvas canvas) {
//		reverseBoard = true;//test
		cellSize = getCellSize();
		drawCell(canvas);
		if(isChessvn) drawCircles(canvas);
	}

	
	/**
	 * Draw square cell of the board. The board is square so don't need to check reversed
	 * @param canvas
	 */
	private void drawCell(Canvas canvas) {
		int xLeft, yLeft;
		for (int rank = 0; rank < boardNumber; rank++)
			for (int file = 0; file < boardNumber; file++) {
				xLeft = file * cellSize;
				yLeft = rank * cellSize;
				if ((rank + file & 1) == 1) canvas.drawRect(xLeft, yLeft, xLeft + cellSize, yLeft + cellSize, cellColor);
				else canvas.drawRect(xLeft, yLeft, xLeft + cellSize, yLeft + cellSize, cellWhiteColor);
			}
	}

	/**
	 * Statically draw position of circles on the board 
	 * @param canvas
	 */
	private void drawCircles(Canvas canvas) {
		Paint cirBightColor = reverseBoard ? cirColor : cirWhiteColor;
		Paint cirDarkColor = reverseBoard ? cirWhiteColor : cirColor;
		
		for(int i=3;i<6;i++) 	drawOneCircle(canvas, 0, i, cirBightColor);
		for(int i=6;i<7;i++) 	drawOneCircle(canvas, 0, i, cirDarkColor);
		for(int i=2;i<7;i++) 	drawOneCircle(canvas, 1, i, cirBightColor);
		for(int i=7;i<8;i++) 	drawOneCircle(canvas, 1, i, cirDarkColor);
		for(int i=1;i<7;i++) 	drawOneCircle(canvas, 2, i, cirBightColor);
		for(int i=7;i<9;i++) 	drawOneCircle(canvas, 2, i, cirDarkColor);
		for(int i=0;i<7;i++) 	drawOneCircle(canvas, 3, i, cirBightColor);
		for(int i=7;i<10;i++) 	drawOneCircle(canvas, 3, i, cirDarkColor);
		for(int i=0;i<6;i++) 	drawOneCircle(canvas, 4, i, cirBightColor);
		for(int i=6;i<10;i++) 	drawOneCircle(canvas, 4, i, cirDarkColor);
		for(int i=0;i<4;i++) 	drawOneCircle(canvas, 5, i, cirBightColor);
		for(int i=4;i<10;i++) 	drawOneCircle(canvas, 5, i, cirDarkColor);
		for(int i=0;i<3;i++) 	drawOneCircle(canvas, 6, i, cirBightColor);
		for(int i=3;i<10;i++) 	drawOneCircle(canvas, 6, i, cirDarkColor);
		for(int i=1;i<3;i++) 	drawOneCircle(canvas, 7, i, cirBightColor);
		for(int i=3;i<9;i++) 	drawOneCircle(canvas, 7, i, cirDarkColor);
		for(int i=2;i<3;i++) 	drawOneCircle(canvas, 8, i, cirBightColor);
		for(int i=3;i<8;i++) 	drawOneCircle(canvas, 8, i, cirDarkColor);
		for(int i=3;i<4;i++) 	drawOneCircle(canvas, 9, i, cirBightColor);
		for(int i=4;i<7;i++) 	drawOneCircle(canvas, 9, i, cirDarkColor);
	}
	
	private void drawOneCircle(Canvas canvas, int rank, int file, Paint color) {
		int cx,cy;
//		if(reverseBoard) {
//			cx = (chessvnBoardNumber - 1 - file)*cellSize + cellSize/2;
//			cy = rank*cellSize + cellSize/2;
//		} else {
			cx = file*cellSize + cellSize/2;
			cy = (boardNumber - 1 - rank)*cellSize + cellSize/2;
		canvas.drawCircle(cx, cy, circleRadius, color);
	}
	
	public int getCellSize() {
		int width = getWidth();
		int height = getHeight();
		return Math.min(width/boardNumber, height/boardNumber);
	}
}

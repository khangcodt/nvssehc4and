package org.chessvn.android.app.view;

import org.chessvn.android.app.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ChessvnBoard extends View {

	/** number of columns and rows */
	public static int chessvnBoardNumber = 10;
	public boolean reverseBoard =false;
	private int cellSize;
	private int circleRadius = 12;

	// color theme
	private Paint cellColor;
	private Paint cellWhiteColor;
	private Paint cirColor;
	private Paint cirWhiteColor;
	private Paint showMoveColor;

	public ChessvnBoard(Context context, AttributeSet attrs) {
		super(context, attrs);

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
		cellSize = getCellSize();
		drawCell(canvas);
		drawCircles(canvas);
	}

	private void drawCell(Canvas canvas) {
		int xLeft, yLeft;
		for (int rank = 0; rank < chessvnBoardNumber; rank++)
			for (int file = 0; file < chessvnBoardNumber; file++) {
				xLeft = file * cellSize;
				yLeft = rank * cellSize;
				if ((rank + file & 1) == 1)
					canvas.drawRect(xLeft, yLeft, xLeft + cellSize, yLeft + cellSize, cellColor);
				else
					canvas.drawRect(xLeft, yLeft, xLeft + cellSize, yLeft + cellSize, cellWhiteColor);
			}
	}

	private void drawCircles(Canvas canvas) {
		if(reverseBoard) {
			
		} else {
			drawOneCircle(canvas, 0, 3, cirColor);
		}
	}
	
	private void drawOneCircle(Canvas canvas, int rank, int file, Paint color) {
		int cx,cy;
		if(reverseBoard) {
			cx = (chessvnBoardNumber - 1 - file)*cellSize + cellSize/2;
			cy = rank*cellSize + cellSize/2;
		} else {
			cx = file*cellSize + cellSize/2;
			cy = (chessvnBoardNumber - 1 - rank)*cellSize + cellSize/2;
		}
		canvas.drawCircle(cx, cy, circleRadius, color);
	}
	
	public int getCellSize() {
		int width = getWidth();
		int height = getHeight();
		return Math.min(width / chessvnBoardNumber, height / chessvnBoardNumber);
	}
}

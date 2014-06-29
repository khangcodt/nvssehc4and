package org.chessvn.android.app.view;

import org.chessvn.android.app.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ChessvnBoard extends View {

	/** number of columns and rows */
	public static int chessvnBoardNumber = 8;
	private int cellSize;

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
		for(int rank = 0; rank < chessvnBoardNumber; rank++) 
			for(int file = 0; file < chessvnBoardNumber; file++) {
				int xLeft = rank*cellSize;
				int yLeft = file*cellSize;
				if ((rank+file&1)==1) canvas.drawRect(xLeft, yLeft, xLeft + cellSize, yLeft + cellSize, cellColor);
				else canvas.drawRect(xLeft, yLeft, xLeft + cellSize, yLeft + cellSize, cellWhiteColor);
			}
	}

	public int getCellSize() {
		int width = getWidth();
		int height = getHeight();
		return Math.min(width/chessvnBoardNumber, height/chessvnBoardNumber);
	}
}

package org.chessvn.android.app.view;

import org.chessvn.android.app.R;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ChessvnBoard extends View {

	/**number of columns and rows */
	private int chessvnBoardNumber = 10;
	private int cellSize;
	
	//color theme
	private Paint cellColor;
	private Paint cellWhiteColor;
	private Paint cirColor;
	private Paint cirWhiteColor;
	private Paint showMoveColor;
	
	
	public ChessvnBoard(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	public void setColorTheme() {
		cellColor.setColor(getResources().getColor(R.color.cirColor));
		cellWhiteColor.setColor(getResources().getColor(R.color.cellWhiteColor));
		cirColor.setColor(getResources().getColor(R.color.cirColor));
		cirWhiteColor.setColor(getResources().getColor(R.color.cirWhiteColor));
		showMoveColor.setColor(getResources().getColor(R.color.showMoveColor));
	}
}

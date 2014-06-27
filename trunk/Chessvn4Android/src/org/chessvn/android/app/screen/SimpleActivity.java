package org.chessvn.android.app.screen;

import org.chessvn.android.app.R;
import org.chessvn.android.app.common.Constant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SimpleActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple);
	}

	public void sendMsg(View view) {
		Intent intent = new Intent(SimpleActivity.this, MainActivity.class);
		this.startActivity(intent);
		EditText editText = (EditText) findViewById(R.id.edit_msg);
		String msg = editText.getText().toString();
		intent.putExtra(Constant.EXTRA_MESSAGE, msg);
		startActivity(intent);
	}
	
}

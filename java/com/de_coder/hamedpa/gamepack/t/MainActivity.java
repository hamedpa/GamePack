package com.de_coder.hamedpa.gamepack.t;

//Developed by HamedPa


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.de_coder.hamedpa.gamepack.R;

public class MainActivity extends Activity implements View.OnClickListener, OnItemSelectedListener{
	

	private static final int buttonSize = 80;
	
	private static final int GAME_OVER = 1;
	

	protected int userWon = 0;
	protected int cpuWon = 0;
	

	protected TicTacToe game;
	

	protected Button[] buttons;
	
	protected int difficulty = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.mainn);
		this.buttons = new Button[9];
		
		Spinner difficultySpinner = (Spinner) findViewById(R.id.difficulty);
		difficultySpinner.setOnItemSelectedListener(this);
		
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.tictactoe);
		
		int buttonIterator = 0;
		for( int i = 0; i < layout.getChildCount(); i++ ){
			View v = layout.getChildAt(i);
			
			if(v instanceof Button) {
				Button btn = (Button) v;
				this.buttons[buttonIterator] = btn;
				this.setUpButton(btn);
				buttonIterator++;
			}
			
		}
		this.resetGame();
	}
	

	protected void setUpButton(Button btn) {
		
		btn.setOnClickListener(this);
		LayoutParams layoutParams = btn.getLayoutParams();
		layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, buttonSize, getResources().getDisplayMetrics());
		layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, buttonSize, getResources().getDisplayMetrics());
		btn.setTextSize(buttonSize / 2);
		btn.setEnabled(true);
	}
	

	public void resetAction(View view) {
		this.resetGame();
	}
	

	protected void resetGame() {
		TextView userSign = (TextView) findViewById(R.id.youSign);
		TextView computerSign = (TextView) findViewById(R.id.computerSign);
		
		for(int i = 0; i < this.buttons.length; i++) {
			this.buttons[i].setEnabled(true);
			this.buttons[i].setText("");
		}
		userSign.setText("");
		computerSign.setText("");
		
		this.game = new TicTacToe(this.difficulty);
		
		userSign.setText(this.game.getUserSign());
		computerSign.setText(this.game.getComputerSign());
		
		int index = this.game.randomStart();
		if(index != -1) {
			this.buttons[index].setEnabled(false);
			this.buttons[index].setText(this.game.getComputerSign());
		}
	}
	

	public void onClick(View view) {
		Button button = (Button) view;
		button.setText(this.game.getUserSign());
		button.setEnabled(false);
		for(int i = 0; i < this.buttons.length; i++){
			if(button.equals(this.buttons[i])){
				this.game.setUserField(i);
			}
		}
		
		if(this.game.checkGameComplete() == -1) {
			int index = this.game.setComputerField();
			if(index >= 0) {
				this.buttons[index].setEnabled(false);
				this.buttons[index].setText(this.game.getComputerSign());
			}
		}
		if(this.game.checkGameComplete() != -1) {
			this.finalizeGame();
		}
	}
	

	protected void finalizeGame() {
		Bundle b = new Bundle();
		
		for(int i = 0; i < this.buttons.length; i++){
			this.buttons[i].setEnabled(false);
		}
		Intent i = new Intent(this,GameOverActivity.class);
		
		b.putInt("result", this.game.checkGameComplete());
		b.putInt("interactions", this.game.getWinnerInteractions());
		
		if(this.game.checkGameComplete() == 0) {
			this.userWon++;
		}else if(this.game.checkGameComplete() == 1) {
			this.cpuWon++;
		}
		
		b.putInt("userWon", this.userWon);
		b.putInt("cpuWon", this.cpuWon);
		i.putExtras(b);
		
		startActivityForResult(i, GAME_OVER);
	}
	

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (Activity.RESULT_OK == resultCode && GAME_OVER == requestCode) {
			this.resetGame();
		}
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
		this.difficulty = pos;
		this.resetGame();
	}

	public void onNothingSelected(AdapterView<?> arg0) {
	}
	
}

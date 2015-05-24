package com.icodeyou.securechat.contactslist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class LetterBar extends LinearLayout{

	public interface OnLetterSelectedListener {
		void onLetterSelected(String letter);
	}
	
	private OnLetterSelectedListener onLetterSelectedListener;
	
	
	public void setOnLetterSelectedListener(
			OnLetterSelectedListener onLetterSelectedListener) {
		this.onLetterSelectedListener = onLetterSelectedListener;
	}

	public LetterBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public LetterBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public LetterBar(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		setBackgroundColor(Color.GRAY);
		setOrientation(VERTICAL);
		for(int i=0; i<26; i++) {
			TextView tv = new TextView(context);
			LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 1);
			tv.setLayoutParams(params);
			tv.setText((char)('A' + i) + "");
			tv.setTextColor(Color.WHITE);
			
			addView(tv);
		}
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			float y = event.getY();
			int defSize = getHeight() / getChildCount();
			int index = (int) (y / defSize);
			TextView tv = (TextView) getChildAt(index);
			
			if(tv != null && onLetterSelectedListener != null) {
				onLetterSelectedListener.onLetterSelected(tv.getText().toString());
			}
			
			break;
		case MotionEvent.ACTION_UP:
			
			if(onLetterSelectedListener != null) {
				onLetterSelectedListener.onLetterSelected("");
			}
			
			break;

		default:
			break;
		}
		
		return true;
	}
}

package com.mare.foldingeffect.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class BottomLayout extends RelativeLayout {

	public BottomLayout(Context context) {
		super(context);
	}

	public BottomLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BottomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public BottomLayout(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}
	
	@Override
	public boolean hasOverlappingRendering() {
		return false;
	}

}

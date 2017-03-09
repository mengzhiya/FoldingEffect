package com.mare.foldingeffect;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements OnClickListener {

	public static final boolean DEBUG = true;
	public static final String TAG = MainActivity.class.getSimpleName();

	public void logf(String fmt, Object... args) {
		Log.i(TAG, String.format(fmt, args));
	}

	View handler;
	FrameLayout container;
	View bottom, top;
	float minTheshold, maxTheshold;
	private ValueAnimator mCurtainChangeAnimator;
	private boolean isOccupied = false;
	int roots[];
	static int  MIN_INIT = 0;
	float mCurentY;
	float handlerMinY;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		handler = findViewById(R.id.handler);
		container = (FrameLayout) findViewById(R.id.container);
		bottom = container.findViewById(R.id.bottom);
		top = container.findViewById(R.id.top);
		handler.setOnClickListener(this);
		bottom.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

			@Override
			public boolean onPreDraw() {
				if (roots == null) {
					roots = new int[2];
					bottom.getLocationOnScreen(roots);
					minTheshold =  roots[1];
					MIN_INIT = bottom.getHeight();
					handlerMinY = bottom.getY() + bottom.getPaddingTop();
					maxTheshold = bottom.getY() +  bottom.getHeight(); 
					top.setY(maxTheshold );
					logf("maxTheshold : " +maxTheshold );
					mCurentY = maxTheshold;
				}
				return true;
			}
			
		});
	}
  
	@Override
	public void onClick(View v) {   
		expand(!isExpanded());
	}  

	private void expand(boolean expanded) {
		if (isOccupied || (minTheshold == 0 || maxTheshold <= minTheshold)) return ;
		startAnim( expanded ? maxTheshold : handlerMinY);
	}


	private void startAnim(final float newHeight) {
		if (mCurtainChangeAnimator != null) {
			mCurtainChangeAnimator.cancel();
		}
		mCurentY = top.getY();
		logf("startAnim >>> from :%f ---> to :%f  ",mCurentY,newHeight);
		mCurtainChangeAnimator = ValueAnimator.ofFloat(mCurentY, newHeight);
		mCurtainChangeAnimator.setDuration(1000);
		mCurtainChangeAnimator.setInterpolator(AnimationUtils.loadInterpolator(
				this, android.R.interpolator.fast_out_slow_in));
		mCurtainChangeAnimator
				.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						float height = (Float) mCurtainChangeAnimator.getAnimatedValue();
						updateTopLayout(height);
						logf("onAnimationUpdate >> height : %f ,expanded :%b",height,isExpanded());
						isOccupied = true;
					}
				});
		mCurtainChangeAnimator.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mCurtainChangeAnimator = null;
				isOccupied = false;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				mCurtainChangeAnimator = null;
				isOccupied = false;
			}
		});
		mCurtainChangeAnimator.start();
	}
	
	private boolean isExpanded(){
		return top.getY() >= maxTheshold ;
	}

	protected void updateTopLayout(float height) {
		hideOverTop(height);//隐藏bottom超出top的部分
		top.setY(height);
		bottom.setVisibility(height <= bottom.getTop() ? View.INVISIBLE : View.VISIBLE);
	}

	private void hideOverTop(float height) {
		int b = bottom.getBottom();
		if(height >= b )return;
		//bottom.layout(bottom.getLeft(), bottom.getTop(), bottom.getRight(), (int) (bottom.getBottom() - (height - b)));
		View space = new View(this);
		space.setY(height);
		space.setBottom(b);
		space.setLeft(bottom.getLeft());
		space.setRight(bottom.getRight());
		space.setBackground(container.getBackground());
		
		//bottom.layout(bottom.getLeft(), bottom.getTop(), bottom.getRight(), bottom.getTop() +bottom.getMeasuredHeight());
	}
	
}

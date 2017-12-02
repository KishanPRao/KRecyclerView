package com.kishanprao.krecycler;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;

import com.kishanprao.krecycler.helper.KRecyclerListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Kishan P Rao on 18/11/17.
 */

public abstract class KRecyclerAdapter extends RecyclerView.Adapter<KRecyclerAdapter.SwipeableVH> implements KRecyclerListener {
	private static final boolean VERBOSE = true;
	private KRecyclerListener mWrapperListener;
	private List mItems = new ArrayList();
	private Drawable mLeftBackground;
	private Drawable mRightBackground;
	private ItemTouchHelper.SimpleCallback itemTouchHelperCallback;
	private ItemTouchHelper mItemTouchHelper;
	private RecyclerView mRecyclerView;
	
	private static final long SWIPE_RETURN_DURATION = 400;
	private static final long SWIPE_RETURN_DELAY = 750;
	
	public KRecyclerAdapter(RecyclerView recyclerView, KRecyclerListener listener, Drawable leftBackground, Drawable rightBackground) {
		this.mWrapperListener = listener;
		this.mLeftBackground = leftBackground;
		this.mRightBackground = rightBackground;
		mRecyclerView = recyclerView;
		itemTouchHelperCallback = new KRecyclerHelper(KRecyclerHelper.BOTH_UD, KRecyclerHelper.BOTH_LR, this);
		mItemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
		mItemTouchHelper.attachToRecyclerView(recyclerView);
	}
	
	public void updateItems(List items) {
		mItems = items;
		notifyDataSetChanged();
	}
	
	@Override
	public int getItemCount() {
		return mItems.size();
	}
	
//	@Override
//	public boolean onItemMove(int fromPosition, int toPosition) {
//		Timber.v("onItemMove, %d, %d", fromPosition, toPosition);
//		return true;
//	}
//	
//	@Override
//	public void onItemDismiss(RecyclerView.ViewHolder viewHolder, int direction, int position) {
//		@KRecyclerHelper.SwipeRecyclerDirection int recyclerDirection = KRecyclerHelper.LEFT;
//		switch (direction) {
//			case ItemTouchHelper.RIGHT: {
//				recyclerDirection = KRecyclerHelper.RIGHT;
//				break;
//			}
//		}
//		mWrapperListener.onSwiped(viewHolder, recyclerDirection, position);
//		Timber.v("onItemDismiss, " + position);
//	}
	
	@Override
	public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
		Timber.v("onStartDrag, ");
		mItemTouchHelper.startDrag(viewHolder);
		if (mWrapperListener != null) {
			mWrapperListener.onStartDrag(viewHolder);
		}
	}
	
	@Override
	public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
		Timber.v("onSwiped, ");
		if (mWrapperListener != null) {
			mWrapperListener.onSwiped(viewHolder, direction, position);
		}
		KRecyclerAdapter.SwipeableVH holder = ((KRecyclerAdapter.SwipeableVH) viewHolder);
		holder.mForegroundView.setAlpha(0);
		holder.mForegroundView.animate()
				.alpha(1.0f)
				.translationX(0)
				.setStartDelay(SWIPE_RETURN_DELAY)
				.setDuration(SWIPE_RETURN_DURATION)
				.setListener(new Animator.AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animation) {
					}
					
					@Override
					public void onAnimationEnd(Animator animation) {
						try {
							mItemTouchHelper.attachToRecyclerView(null);
							mItemTouchHelper.attachToRecyclerView(mRecyclerView);
						} catch (NullPointerException e) {
							if (VERBOSE) e.printStackTrace();
						}
					}
					
					@Override
					public void onAnimationCancel(Animator animation) {
					}
					
					@Override
					public void onAnimationRepeat(Animator animation) {
					}
				}).start();
	}
	
	@Override
	public void onMoved(int fromPosition, int toPosition) {
		Timber.v("onMoved, %d -> %d", fromPosition, toPosition);
		Collections.swap(mItems, fromPosition, toPosition);
		notifyItemMoved(fromPosition, toPosition);
		if (mWrapperListener != null) {
			mWrapperListener.onMoved(fromPosition, toPosition);
		}
	}
	
	public class SwipeableVH extends RecyclerView.ViewHolder {
		View mBackgroundView;
		View mForegroundView;
		View mDragView;
		
		public SwipeableVH(View itemView, View mBackgroundView, View mForegroundView, View mDragView) {
			super(itemView);
			this.mBackgroundView = mBackgroundView;
			this.mForegroundView = mForegroundView;
			this.mDragView = mDragView;
			mDragView.setOnTouchListener(new View.OnTouchListener() {
				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						onStartDrag(SwipeableVH.this);
					}
					return false;
				}
			});
		}
		
		public View getBackgroundView() {
			return mBackgroundView;
		}
		
		public View getForegroundView() {
			return mForegroundView;
		}
		
		public View getDragView() {
			return mDragView;
		}
		
		void showRight() {
			mBackgroundView.setBackground(mRightBackground);
		}
		
		void showLeft() {
			mBackgroundView.setBackground(mLeftBackground);
		}
		
		void startDrag() {
			mBackgroundView.setBackgroundColor(Color.BLUE);
		}
		
		void stopDrag() {
			mBackgroundView.setBackgroundColor(Color.DKGRAY);
		}
	}
}

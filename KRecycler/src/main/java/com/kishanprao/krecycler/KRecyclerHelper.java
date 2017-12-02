package com.kishanprao.krecycler;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.kishanprao.krecycler.helper.KRecyclerListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import timber.log.Timber;


/**
 * Created by Kishan P Rao on 18/11/17.
 */

public class KRecyclerHelper extends ItemTouchHelper.SimpleCallback {
	private static final float ALPHA_FULL = 1.0f;
	
	public static final int LEFT = ItemTouchHelper.START;
	public static final int RIGHT = ItemTouchHelper.END;
	public static final int BOTH_LR = LEFT | RIGHT;
	public static final int UP = ItemTouchHelper.UP;
	public static final int DOWN = ItemTouchHelper.DOWN;
	public static final int BOTH_UD = UP | DOWN;
	public static final int ALL = BOTH_LR | BOTH_UD;
	
	@IntDef({
			LEFT, RIGHT, BOTH_LR,
			UP, DOWN, BOTH_UD,
			ALL,
	})
	@Retention(RetentionPolicy.SOURCE)
	public @interface SwipeRecyclerDirection {
	}
	
	private static final int MS_SINCE_START_SCROLL = 5000;
	
	@Override
	public int interpolateOutOfBoundsScroll(RecyclerView recyclerView, int viewSize, int viewSizeOutOfBounds, int totalSize, long msSinceStartScroll) {
		return super.interpolateOutOfBoundsScroll(recyclerView, viewSize, viewSizeOutOfBounds, totalSize, MS_SINCE_START_SCROLL);
	}
	
	private KRecyclerListener mListener;
	
	public KRecyclerHelper(@SwipeRecyclerDirection int dragDirs, @SwipeRecyclerDirection int swipeDirs, KRecyclerListener listener) {
		super(dragDirs, swipeDirs);
		this.mListener = listener;
	}
	
	@Override
	public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
		@SwipeRecyclerDirection int recyclerDirection = LEFT;
		switch (direction) {
			case ItemTouchHelper.RIGHT: {
				recyclerDirection = RIGHT;
				break;
			}
		}
		mListener.onSwiped(viewHolder, recyclerDirection, viewHolder.getAdapterPosition());
	}
	
	@Override
	public int getDragDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
		return super.getDragDirs(recyclerView, viewHolder);
	}
	
	@Override
	public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
		Timber.v("onMove, ");
		if (viewHolder.getItemViewType() != target.getItemViewType()) {
			return false;
		}
		mListener.onMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
		return true;
	}
	
	@Override
	public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {}
	
	@Override
	public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
	                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
	                            int actionState, boolean isCurrentlyActive) {
		super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
	}
	
	@Override
	public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
		KRecyclerAdapter.SwipeableVH holder = ((KRecyclerAdapter.SwipeableVH) viewHolder);
		holder.mForegroundView.setAlpha(ALPHA_FULL);
		holder.mForegroundView.setBackgroundColor(Color.GRAY);
		holder.mForegroundView.setTranslationX(0);
	}
	
	@Override
	public boolean isLongPressDragEnabled() {
		return false;
	}
	
	@Override
	public void onChildDraw(Canvas c, RecyclerView recyclerView,
	                        RecyclerView.ViewHolder viewHolder, float dX, float dY,
	                        int actionState, boolean isCurrentlyActive) {
		KRecyclerAdapter.SwipeableVH holder = ((KRecyclerAdapter.SwipeableVH) viewHolder);
		if (dX < 0) {
			holder.showRight();
		} else {
			holder.showLeft();
		}
		if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
			// Fade out the view as it is swiped out of the parent's bounds
			final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
			holder.mForegroundView.setAlpha(alpha);
			holder.mForegroundView.setTranslationX(dX);
		} else {
			super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
		}
	}
	
	@Override
	public int convertToAbsoluteDirection(int flags, int layoutDirection) {
		return super.convertToAbsoluteDirection(flags, layoutDirection);
	}
}

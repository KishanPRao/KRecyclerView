package com.kishanprao.krecycler.helper;

import android.support.v7.widget.RecyclerView;

import com.kishanprao.krecycler.KRecyclerHelper;

/**
 * Created by Kishan P Rao on 18/11/17.
 */

public interface KRecyclerListener {
	void onStartDrag(RecyclerView.ViewHolder viewHolder);
	
	void onSwiped(RecyclerView.ViewHolder viewHolder,
	              @KRecyclerHelper.SwipeRecyclerDirection int direction,
	              int position);
	
	void onMoved(int fromPosition, int toPosition);
}

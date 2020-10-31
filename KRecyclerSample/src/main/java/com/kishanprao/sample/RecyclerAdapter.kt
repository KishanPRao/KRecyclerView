package com.kishanprao.sample

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.kishanprao.krecycler.KRecyclerAdapter
import com.kishanprao.krecycler.helper.KRecyclerListener
import java.util.*

/**
 * Created by Kishan P Rao on 02/12/17.
 */
class RecyclerAdapter : KRecyclerAdapter {
	private val mItems = ArrayList<Int>()
	
	constructor(recyclerView: RecyclerView, listener: KRecyclerListener, leftBackground: Drawable, rightBackground: Drawable) :
			super(recyclerView, listener, leftBackground, rightBackground) {
		for (i in 0..99) {
			mItems.add(i)
		}
		updateItems(mItems)
	}
	
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeableVH {
		val fg = FrameLayout(parent.context)
		
		val fgText = TextView(parent.context)
		fgText.setBackgroundColor(Color.DKGRAY)
		fgText.setTextColor(Color.WHITE)
		fgText.gravity = Gravity.CENTER
		fg.addView(fgText, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300))
		
		val drag = View(parent.context)
		drag.setBackgroundResource(R.drawable.drag_handle)
		val params = FrameLayout.LayoutParams(200, 200)
		params.gravity = Gravity.CENTER_VERTICAL
		fg.addView(drag, params)
		
		val bg = View(parent.context)
		
		val layout = FrameLayout(parent.context)
		layout.addView(bg, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300))
		layout.addView(fg, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300))
		
		val holder = RecyclerViewHolder(layout, bg, fg, drag)
		holder.text = fgText
		return holder
	}
	
	
	override fun onBindViewHolder(holder: SwipeableVH, position: Int) {
		val number = mItems[position]
		(holder as RecyclerViewHolder).text.text = number.toString()
	}
	
	inner class RecyclerViewHolder(itemView: View, bg: View, fg: View, drag: View) : SwipeableVH(itemView, bg, fg, drag) {
		lateinit var text: TextView
	}
}
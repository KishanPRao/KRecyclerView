package com.kishanprao.sample

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.kishanprao.krecycler.KRecyclerAdapter
import com.kishanprao.krecycler.helper.KRecyclerListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), KRecyclerListener {
	override fun onMoved(fromPosition: Int, toPosition: Int) {}
	
	override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int) {}
	
	override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {}
	
	lateinit var adapter : KRecyclerAdapter
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		
		val left = ColorDrawable(Color.BLUE)
		val right = ColorDrawable(Color.RED)
		adapter = RecyclerAdapter(recyclerView, this, left, right)
		recyclerView.adapter = adapter
		recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
		recyclerView.itemAnimator = DefaultItemAnimator()
		recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
	}
}

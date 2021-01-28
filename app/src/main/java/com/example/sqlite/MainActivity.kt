package com.example.sqlite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sqlite.db.MyDbManager
import com.example.sqlite.db.MyIntentConstants
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val myDbManager = MyDbManager(this)
    val myAdapter = MyAdapter(ArrayList<ListItem>(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        initSearchView()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        fillAdapter()
    }
    override fun onDestroy() {
        myDbManager.closeDb()
        super.onDestroy()
    }

    fun onClickNew(view: View) {
        val i = Intent(this, EditActivity::class.java)
        startActivity(i)
    }

    fun init(){
        rcView.layoutManager = LinearLayoutManager(this)
        val swipeHelper = getSwapMng()
        swipeHelper.attachToRecyclerView(rcView)
        rcView.adapter = myAdapter
    }

    fun fillAdapter() {
        val list = myDbManager.readDbData("")
        myAdapter.updateAdapter(list)
        if(list.size > 0) {
            tvNoElements.visibility = View.GONE
        }
        else {
            tvNoElements.visibility = View.VISIBLE
        }
    }

    private fun initSearchView() {
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean {
            TODO("Not yet implemented")
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            val list = myDbManager.readDbData(newText!!)
            myAdapter.updateAdapter(list)
            return true
        }
    })
    }

    private fun getSwapMng():ItemTouchHelper {
        return ItemTouchHelper(object:ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myAdapter.removeItem(viewHolder.adapterPosition, myDbManager)
            }
        })
    }

}
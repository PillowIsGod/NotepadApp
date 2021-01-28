package com.example.sqlite.db

import android.content.ContentValues
import android.content.Context
import android.content.LocusId
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.example.sqlite.ListItem

class MyDbManager(context: Context) {
    private val myDbHelper = MyDbHelper(context)
    var db: SQLiteDatabase? = null


    fun openDb() {
        db = myDbHelper.writableDatabase

    }

    fun insertToDb(title:String, content:String, uri:String) {
        val values = ContentValues().apply {
            put(MyDBnameClass.COLUMN_TITLE, title)
            put(MyDBnameClass.COLUMN_NAME_CONTENT, content)
            put(MyDBnameClass.COLUMN_NAME_IMAGE_URI, uri)
        }
        db?.insert(MyDBnameClass.TABLE_NAME, null, values)
    }
    fun removeItemFromDb(id:String) {
        val selection = BaseColumns._ID +  "= $id"
        db?.delete(MyDBnameClass.TABLE_NAME, selection, null)
    }


    fun readDbData(searchText:String):ArrayList<ListItem> {
        val dataList = ArrayList<ListItem>()
        val selection = "${MyDBnameClass.COLUMN_TITLE} like ?"
        val cursor = db?.query(MyDBnameClass.TABLE_NAME, null, selection,
            arrayOf("%$searchText%"), null, null, null)
            while(cursor?.moveToNext()!!) {
                val dataText1 = cursor?.getString(cursor.getColumnIndex(MyDBnameClass.COLUMN_TITLE))
                val dataText2 = cursor?.getString(cursor.getColumnIndex(MyDBnameClass.COLUMN_NAME_CONTENT))
                val dataText3 = cursor?.getString(cursor.getColumnIndex(MyDBnameClass.COLUMN_NAME_IMAGE_URI))
                val dataText4 = cursor?.getInt(cursor.getColumnIndex(BaseColumns._ID))
                val item = ListItem()
                item.desc = dataText2
                item.title = dataText1
                item.uri = dataText3
                item.id = dataText4
                dataList.add(item)
            }
        cursor.close()
       return dataList
    }
    fun closeDb() {
        myDbHelper.close()
    }


}
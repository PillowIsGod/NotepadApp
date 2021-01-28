package com.example.sqlite

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.sqlite.db.MyDbManager
import com.example.sqlite.db.MyIntentConstants
import kotlinx.android.synthetic.main.activity_edit.*
//import kotlin.collections.UArraysKt.contentToString

class EditActivity : AppCompatActivity() {
    val imageRequestCode = 10
    var tempImgUri = "empty"
    val myDbManager = MyDbManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        getMyIntents()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == imageRequestCode) {
            imMainImg.setImageURI(data?.data)
            tempImgUri = data?.data.toString()
            contentResolver.takePersistableUriPermission(data?.data!!, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }
    fun onClickAddImage(view: View) {
        mainImgLayout.visibility = View.VISIBLE
        fbAddImage.visibility = View.GONE
    }

    fun onClickDeleteImg(view: View) {
        mainImgLayout.visibility = View.GONE
        fbAddImage.visibility = View.VISIBLE
    }
    fun onClickChooseImg(view: View) {
    val i = Intent(Intent.ACTION_OPEN_DOCUMENT)
        i.type = "image/*"

        startActivityForResult(i,imageRequestCode)

    }
    fun onClickSave(view: View) {
        val mytitle = edTitle.text.toString()
        val myDesc = edDesc.text.toString()
        if(mytitle != "" && myDesc != ""){

            myDbManager.insertToDb(mytitle, myDesc, tempImgUri)
            finish()
        }
    }
    fun getMyIntents(){
        val i = intent
        if (i != null){

            if(i.getStringExtra(MyIntentConstants.I_TITLE_KEY) != null) {
                fbAddImage.visibility = View.GONE
                edTitle.setText(i.getStringExtra(MyIntentConstants.I_TITLE_KEY))
                edDesc.setText(i.getStringExtra(MyIntentConstants.I_DESC_KEY))


                var stringExtra = i.getStringExtra(MyIntentConstants.I_URI_KEY)

                var stringIsEqual = stringExtra.toString() == "empty";

                if (stringExtra != null && !stringIsEqual) {
                    mainImgLayout.visibility = View.VISIBLE
                    val uri = Uri.parse(i.getStringExtra(MyIntentConstants.I_URI_KEY))
                    imMainImg.setImageURI(uri)

                    imBtnDelImg.visibility = View.GONE
                    imBtnEditImg.visibility = View.GONE
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        myDbManager.openDb()

    }
    override fun onDestroy() {
        myDbManager.closeDb()
        super.onDestroy()

    }
}
package com.halit.contacthalitkotlin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_record_detail.*
import java.util.*

class RecordDetailActivity : AppCompatActivity() {

    // actionBar
    private var actionBar: ActionBar?=null

    // dbHelper
    private var dbHelper:MyDbHelper?=null

    private  var recordId:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_detail)

        // setting up actionBar
        actionBar = supportActionBar
        actionBar!!.title = "Record Detail"
        actionBar!!.setDisplayShowHomeEnabled(true)
        actionBar!!.setDefaultDisplayHomeAsUpEnabled(true)

        // init dbHelper
        dbHelper = MyDbHelper(this)

        // get record id from intent
        val intent = intent
        recordId = intent.getStringExtra("RECORD_ID")

        showRecordDetail()

    }

    private fun showRecordDetail() {
        // get record details

        val selectQuery = "SELECT * FROM "+Constants.TABLE_NAME+ " WHERE "+ Constants.C_ID + " ="+recordId+""

//        val db = dbHelper!!.writableDatabase
        val db = dbHelper!!.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()){
            do {

                val id = ""+cursor.getInt(cursor.getColumnIndex(Constants.C_ID))
                val name = ""+cursor.getString(cursor.getColumnIndex(Constants.C_NAME))
                val image = ""+cursor.getString(cursor.getColumnIndex(Constants.C_IMAGE))
                val bio = ""+cursor.getString(cursor.getColumnIndex(Constants.C_BIO))
                val phone = ""+cursor.getString(cursor.getColumnIndex(Constants.C_PHONE))
                val email = ""+cursor.getString(cursor.getColumnIndex(Constants.C_EMAIL))
                val dob = ""+cursor.getString(cursor.getColumnIndex(Constants.C_DOB))
                val addedTimeStamp = ""+cursor.getString(cursor.getColumnIndex(Constants.C_ADDED_TIMESTAMP))
                val updatedTimeStamp = ""+cursor.getString(cursor.getColumnIndex(Constants.C_UPDATED_TIMESTAMP))

                // convert timeStamp to dd//mm/yyyy e.g. 14/01/2020 08:22 AM
                val calendar1 = Calendar.getInstance(Locale.getDefault())
                calendar1.timeInMillis = addedTimeStamp.toLong()
                val  timeAdded = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm:aa", calendar1)

                val calendar2 = Calendar.getInstance(Locale.getDefault())
                calendar1.timeInMillis = updatedTimeStamp.toLong()
                val  timeUpdated = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm:aa", calendar2)


                // set data
                nameTv.text = name
                bioTv.text = bio
                phoneTv.text = phone
                emailTv.text = email
                dobTv.text = dob
                addedDateTv.text = timeAdded
                updatedDateTv.text = timeUpdated

                main_item_contact.setOnClickListener {
                    val intent =
                        Intent(Intent.ACTION_CALL, Uri.parse("tel:$phone"))
                    startActivity(intent)
                }

                // if user dosn't attach image then imageUri will be null, so set default image in that case
                if (image == "null"){
                    // no image in record, set default
                    profileIv.setImageResource(R.drawable.ic_person_black)
                }else{
                    // have image in record
                    profileIv.setImageURI(Uri.parse(image))
                }

            }while (cursor.moveToNext())
        }
        // close db connection
        db.close()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}

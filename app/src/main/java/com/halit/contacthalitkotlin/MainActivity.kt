package com.halit.contacthalitkotlin

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // dbHelper
    lateinit var dbHelper: MyDbHelper

    // orderBy / sort queries
    private val NEWEST_FIRST = "${Constants.C_ADDED_TIMESTAMP} DESC"
    private val OLDEST_FIRST = "${Constants.C_ADDED_TIMESTAMP} ASC"
    private val TITLE_ASC = "${Constants.C_NAME} ASC"
    private val TITLE_DESC = "${Constants.C_NAME} DESC"

    private var recentSortOrder = NEWEST_FIRST

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // init dbHelper
        dbHelper = MyDbHelper(this)

        loadRecords(NEWEST_FIRST) // by default load newest first

        // click FloatingActionButton to start AddUpdateRecordActivity
        addRecordBtn.setOnClickListener {
            val intent = Intent(this, AddUpdateRecordActivity::class.java)
            intent.putExtra("isEditMode", false) // want to add new record, set it false
            startActivity(intent)
        }
    }

    private fun loadRecords(orderBy:String) {
        recentSortOrder = orderBy
        val adapterRecord = AdapterRecord(this, dbHelper.getAllRecords(orderBy))

        recordRv.adapter = adapterRecord
    }

    private fun searchRecords(query:String) {
        val adapterRecord = AdapterRecord(this, dbHelper.searchRecords(query))

        recordRv.adapter = adapterRecord
    }

    private fun sortDialog() {
        // OPTIONS to display in dialog
        val  options = arrayOf("Name Ascending", "Name Descending", "Newest", "Oldest")
        // Dialog
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setItems(options){_, which ->
            // Handle Items
            if (which==0){
                // Name Ascending
                loadRecords(TITLE_ASC)
            }else if(which==1){
                // Name Descending
                loadRecords(TITLE_DESC)
            }else if (which==2){
                // Newest
                loadRecords(NEWEST_FIRST)
            }else if (which==3){
                // Oldest first
                loadRecords(OLDEST_FIRST)
            }
        }
            .show()
    }

    public override fun onResume() {
        super.onResume()
        loadRecords(recentSortOrder)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // inflate menu
        menuInflater.inflate(R.menu.menu_main, menu)

        // searchView
        val item = menu.findItem(R.id.action_search)
        val searchView = item.actionView as SearchView

        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                // search as you type
                if (newText != null){
                    searchRecords(newText)
                }

                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                // search when search button on keyboard is clicked
                if (query != null){
                    searchRecords(query)
                }

                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle menu item clicks
        val id= item.itemId
        if (id==R.id.action_sort){
            sortDialog()
        }
        else if (id==R.id.action_deleteAll){
            // Delete all records
            dbHelper.deleteAllRecords()
            onResume()
        }
        return super.onOptionsItemSelected(item)
    }

}

package com.example.datetracker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.example.datetracker.databinding.ActivityMainBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        val db = DBHelper(this, null)

        binding.fab.setOnClickListener { view ->
            val builder = MaterialDatePicker.Builder.dateRangePicker()
            val picker = builder.build()
            picker.show(supportFragmentManager, picker.toString())
            picker.addOnCancelListener {
                Log.d("DatePickerActivity", "Dialog was cancelled")
            }
            picker.addOnNegativeButtonClickListener {
                Log.d("DatePickerActivity", "Dialog Negative Button was clicked")
            }
            picker.addOnPositiveButtonClickListener {
                Log.d(
                    "DatePickerActivity", "Date String = ${picker.headerText}::Date epoch values::${it.first}:: to ::${it.second}"
                )
                db.addDates(it.first.toString(), it.second.toString())
                prepareItems(db)
        }
    }
    }
    fun prepareItems(db: DBHelper){
        val cursor = db.getDates()
        cursor!!.moveToFirst()
        val first = findViewById<TextView>(R.id.First)
        val second = findViewById<TextView>(R.id.Second)
        val f = cursor.getColumnIndex(DBHelper.FIRST_DATE)
        val s = cursor.getColumnIndex(DBHelper.SECOND_DATE)
        first.setText("")
        second.setText("")
        if (f >= 0 && s >= 0) {
            first.append(getDateTime(cursor.getString(f)) + "\n")
            second.append(getDateTime(cursor.getString(s)) + "\n")
        }
        while(cursor.moveToNext()){
            val f = cursor.getColumnIndex(DBHelper.FIRST_DATE)
            val s = cursor.getColumnIndex(DBHelper.SECOND_DATE)
            if (f >= 0 && s >= 0) {
                first.append(getDateTime(cursor.getString(f)) + "\n")
                second.append(getDateTime(cursor.getString(s)) + "\n")
            }
        }
        cursor.close()
    }

    private fun getDateTime(s: String): String? {
        return try {
            val simpleDateFormat = SimpleDateFormat("MM/dd")
            val newDate = Date(s.toLong()).addDays(1)
            simpleDateFormat.format(newDate)
        } catch(e: Exception) {
            e.toString()
        }
    }

    fun Date.addDays(numberOfDaysToAdd: Int): Date{
        return Date(this.time + numberOfDaysToAdd * 86400000)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.clearAll -> onClearAllSelected()
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun onClearAllSelected(): Boolean {
        val db = DBHelper(this, null)
        db.removeAllDates()
        val first = findViewById<TextView>(R.id.First)
        val second = findViewById<TextView>(R.id.Second)
        first.setText("")
        second.setText("")
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return true
    }
}

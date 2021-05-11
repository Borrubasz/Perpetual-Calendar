package com.example.perpetualcalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.perpetualcalendar.databinding.ActivitySundaysBinding
import java.util.*
import kotlin.collections.ArrayList

class CalcSundays(rok: Int) : CalcMoveable(rok) {
    fun calcSundays(): ArrayList<GregorianCalendar> {
        val arr: ArrayList<GregorianCalendar> = ArrayList();
        var sunDate = GregorianCalendar(rok, 0, 31);
        arr.add(toClosestSunday(sunDate.clone() as GregorianCalendar));
        sunDate = calcEaster();
        sunDate.add(Calendar.DATE, -7);
        arr.add(sunDate.clone() as GregorianCalendar);
        sunDate = GregorianCalendar(rok, 3, 30);
        arr.add(toClosestSunday(sunDate.clone() as GregorianCalendar));
        sunDate = GregorianCalendar(rok, 5, 30);
        arr.add(toClosestSunday(sunDate.clone() as GregorianCalendar));
        sunDate = GregorianCalendar(rok, 7, 31);
        arr.add(toClosestSunday(sunDate.clone() as GregorianCalendar));
        sunDate = calcFirstAdventSun();
        sunDate.add(Calendar.DATE, 14);
        arr.add(toClosestSunday(sunDate.clone() as GregorianCalendar));
        sunDate.add(Calendar.DATE, 7);
        arr.add(toClosestSunday(sunDate.clone() as GregorianCalendar));
        return arr;
    }
    private fun toClosestSunday(dat: GregorianCalendar) : GregorianCalendar {
        return if(dat.get(Calendar.DAY_OF_WEEK) == 1) {
            dat;
        } else {
            dat.add(Calendar.DATE, -dat.get(Calendar.DAY_OF_WEEK)+1);
            dat;
        }
    }
}

class Sundays : AppCompatActivity() {

    private lateinit var binding: ActivitySundaysBinding;
    private lateinit var listview: ListView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySundaysBinding.inflate(layoutInflater);
        val view = binding.root;
        setContentView(view);
        listview = binding.SundaysList;
        val preElems = CalcSundays(intent.getIntExtra("rok", 2020)).calcSundays();
        val elems: ArrayList<String> = ArrayList();
        preElems.forEach {
            elems.add(
                getString(
                    R.string.data,
                    it.get(Calendar.DATE),
                    it.get(Calendar.MONTH) + 1,
                    it.get(Calendar.YEAR)
                )
            )
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, elems);
        listview.adapter = adapter;
    }
}
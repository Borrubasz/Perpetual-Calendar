package com.example.perpetualcalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.perpetualcalendar.databinding.ActivityWorkingDaysBinding
import java.util.*

class CalcWorkingDays(private var bDate: GregorianCalendar, private var eDate: GregorianCalendar) {
    fun calcWorkingDays() : IntArray {
        var days = IntArray(2);
        while(compDates(bDate, eDate) >= 0) {
            val easter = CalcMoveable(bDate.get(Calendar.YEAR)).calcEaster()
            val bozeCialo = easter.clone() as GregorianCalendar
            easter.add(Calendar.DATE, 1);
            bozeCialo.add(Calendar.DATE, 60);
            when {
                compDates(bDate, GregorianCalendar(bDate.get(Calendar.YEAR), 0, 1)) == 0 -> days[0]++;
                compDates(bDate, GregorianCalendar(bDate.get(Calendar.YEAR), 0, 6)) == 0 -> days[0]++;
                compDates(bDate, GregorianCalendar(bDate.get(Calendar.YEAR), 4, 1)) == 0 -> days[0]++;
                compDates(bDate, GregorianCalendar(bDate.get(Calendar.YEAR), 4, 3)) == 0 -> days[0]++;
                compDates(bDate, GregorianCalendar(bDate.get(Calendar.YEAR), 7, 15)) == 0 -> days[0]++;
                compDates(bDate, GregorianCalendar(bDate.get(Calendar.YEAR), 10, 1)) == 0 -> days[0]++;
                compDates(bDate, GregorianCalendar(bDate.get(Calendar.YEAR), 11, 25)) == 0 -> days[0]++;
                compDates(bDate, GregorianCalendar(bDate.get(Calendar.YEAR), 11, 26)) == 0 -> days[0]++;
                compDates(bDate, easter) == 0 -> days[0]++;
                compDates(bDate, bozeCialo) == 0 -> days[0]++;
                bDate.get(Calendar.DAY_OF_WEEK) == 1 -> days[0]++;
                bDate.get(Calendar.DAY_OF_WEEK) == 7 -> days[0]++;
                else -> { days[0]++; days[1]++;}
            }
            bDate.add(Calendar.DATE, 1);
        }
        return days;
    }

    private fun compDates(fDate: GregorianCalendar, sDate: GregorianCalendar) : Int {
        var tmp: Int = 0;
        when {
            fDate.get(Calendar.YEAR) < sDate.get(Calendar.YEAR) -> { tmp = 1; }
            fDate.get(Calendar.YEAR) > sDate.get(Calendar.YEAR) -> { tmp = -1; }
            fDate.get(Calendar.YEAR) == sDate.get(Calendar.YEAR) -> {
                when {
                    fDate.get(Calendar.MONTH) < sDate.get(Calendar.MONTH) -> {
                        tmp = 1;
                    }
                    fDate.get(Calendar.MONTH) > sDate.get(Calendar.MONTH) -> {
                        tmp = -1;
                    }
                    fDate.get(Calendar.DATE) < sDate.get(Calendar.DATE) -> {
                        tmp = 1;
                    }
                    fDate.get(Calendar.DATE) > sDate.get(Calendar.DATE) -> {
                        tmp = -1;
                    }
                }
            }
        }
        return tmp;
    }
}

class WorkingDays : AppCompatActivity() {

    private lateinit var binding: ActivityWorkingDaysBinding;
    private var days = IntArray(2);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkingDaysBinding.inflate(layoutInflater);
        val view = binding.root;
        setContentView(view);
        binding.dataPocz.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            days = CalcWorkingDays(GregorianCalendar(year, monthOfYear, dayOfMonth),
                    GregorianCalendar(binding.dataKon.year, binding.dataKon.month, binding.dataKon.dayOfMonth)).calcWorkingDays();
            binding.dniKalen.text = days[0].toString();
            binding.dniRobo.text = days[1].toString();
        }
        binding.dataKon.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            days = CalcWorkingDays(GregorianCalendar(binding.dataPocz.year, binding.dataPocz.month, binding.dataPocz.dayOfMonth),
                    GregorianCalendar(year, monthOfYear, dayOfMonth)).calcWorkingDays();
            binding.dniKalen.text = days[0].toString();
            binding.dniRobo.text = days[1].toString();
        }
    }
}
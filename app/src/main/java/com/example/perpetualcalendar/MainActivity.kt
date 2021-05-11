package com.example.perpetualcalendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import com.example.perpetualcalendar.databinding.ActivityMainBinding
import java.util.*

open class CalcMoveable(protected var rok: Int) {
    fun calcEaster(): GregorianCalendar {
        val a: Int = rok.rem(19);
        val b: Int = kotlin.math.floor((rok / 100).toDouble()).toInt();
        val c: Int = rok.rem(100);
        val d: Int = kotlin.math.floor((b/4).toDouble()).toInt();
        val e: Int = b.rem(4);
        val f: Int = kotlin.math.floor(((b+8)/25).toDouble()).toInt();
        val g: Int = kotlin.math.floor(((b - f + 1) / 3).toDouble()).toInt();
        val h: Int = (19 * a + b - d - g + 15).rem(30);
        val i: Int = kotlin.math.floor((c / 4).toDouble()).toInt();
        val k: Int = c.rem(4);
        val l: Int = (32 + 2 * e + 2 * i - h - k).rem(7);
        val m: Int = kotlin.math.floor(((a + 11 * h + 22 * l) / 451).toDouble()).toInt();
        val p: Int = (h + l - 7 * m + 114).rem(31);
        val dzien: Int = p + 1;
        val miesiac: Int = kotlin.math.floor(((h + l - 7 * m + 114)/31).toDouble()).toInt();
        return GregorianCalendar(rok, miesiac-1, dzien);
    }
    fun calcFirstAdventSun(): GregorianCalendar {
        val tmp: GregorianCalendar = GregorianCalendar(rok, 11, 25);
        return if(tmp.get(Calendar.DAY_OF_WEEK) == 1){
            tmp.add(Calendar.DATE, -28);
            tmp;
        } else {
            tmp.add(Calendar.DATE, -21-(tmp.get(Calendar.DAY_OF_WEEK)-1));
            tmp;
        }
    }
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        val view = binding.root;
        setContentView(view);
        binding.niedziele.isClickable = false;
        binding.rok.maxValue = 2200;
        binding.rok.minValue = 1900;
        binding.rok.wrapSelectorWheel = false;
        binding.rok.setOnValueChangedListener() { _: NumberPicker, _: Int, newVal: Int ->
            binding.niedziele.isClickable = newVal >= 2020;
            val cm = CalcMoveable(newVal);
            val easter: GregorianCalendar = cm.calcEaster();
            binding.wielkanoc.text = getString(R.string.data, easter.get(Calendar.DATE), easter.get(Calendar.MONTH)+1, easter.get(Calendar.YEAR));
            easter.set(Calendar.DATE, easter.get(Calendar.DATE) - 46);
            binding.popielec.text = getString(R.string.data, easter.get(Calendar.DATE), easter.get(Calendar.MONTH)+1, easter.get(Calendar.YEAR));
            easter.set(Calendar.DATE, easter.get(Calendar.DATE) + 106);
            binding.bozeCialo.text = getString(R.string.data, easter.get(Calendar.DATE), easter.get(Calendar.MONTH)+1, easter.get(Calendar.YEAR));
            val advent: GregorianCalendar = cm.calcFirstAdventSun();
            binding.adwent.text = getString(R.string.data, advent.get(Calendar.DATE), advent.get(Calendar.MONTH)+1, advent.get(Calendar.YEAR));
        }
    }

    fun goToSundays(v: View) {
        val intent: Intent = Intent(this, Sundays::class.java).apply {
            putExtra("rok", binding.rok.value);
        }
        startActivity(intent);
    }

    fun goToWorkingDays(v: View) {
        val intent = Intent(this, WorkingDays::class.java);
        startActivity(intent);
    }
}
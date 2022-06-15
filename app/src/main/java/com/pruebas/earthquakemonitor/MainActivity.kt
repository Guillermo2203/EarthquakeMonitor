package com.pruebas.earthquakemonitor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.pruebas.earthquakemonitor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.eqRecycler.layoutManager = LinearLayoutManager(this)

        val eqList = mutableListOf<Earthquake>()
        eqList.add(Earthquake("1", "Buenos Aires", 4.3, 273846152L, -102.4756, 28.47365))
        eqList.add(Earthquake("2", "Lima", 2.9, 273846152L, -102.4756, 28.47365))
        eqList.add(Earthquake("3", "Ciudad de México", 6.0, 273846152L, -102.4756, 28.47365))
        eqList.add(Earthquake("4", "Bogotá", 1.8, 273846152L, -102.4756, 28.47365))
        eqList.add(Earthquake("5", "Caracas", 3.5, 273846152L, -102.4756, 28.47365))
        eqList.add(Earthquake("6", "Madrid", 0.6, 273846152L, -102.4756, 28.47365))
        eqList.add(Earthquake("7", "Acra", 5.1, 273846152L, -102.4756, 28.47365))
        eqList.add(Earthquake("1", "Zaragoza", 4.3, 273846152L, -102.4756, 28.47365))
        eqList.add(Earthquake("2", "Chile", 2.9, 273846152L, -102.4756, 28.47365))
        eqList.add(Earthquake("3", "a", 6.0, 273846152L, -102.4756, 28.47365))
        eqList.add(Earthquake("4", "s", 1.8, 273846152L, -102.4756, 28.47365))
        eqList.add(Earthquake("5", "f", 3.5, 273846152L, -102.4756, 28.47365))
        eqList.add(Earthquake("6", "d", 0.6, 273846152L, -102.4756, 28.47365))
        eqList.add(Earthquake("7", "h", 5.1, 273846152L, -102.4756, 28.47365))

        val adapter = EqAdapter()
        binding.eqRecycler.adapter = adapter
        adapter.submitList(eqList)
        adapter.onItemClickListener = {
            Toast.makeText(this, it.place, Toast.LENGTH_SHORT).show()
        }

        if (eqList.isEmpty()) {
            binding.eqEmptyView.visibility = View.VISIBLE
        } else {
            binding.eqEmptyView.visibility = View.GONE
        }
    }
}
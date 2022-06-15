package com.pruebas.earthquakemonitor.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pruebas.earthquakemonitor.Earthquake
import com.pruebas.earthquakemonitor.R
import com.pruebas.earthquakemonitor.api.ApiResponseStatus
import com.pruebas.earthquakemonitor.api.WorkerUtil
import com.pruebas.earthquakemonitor.databinding.ActivityMainBinding

private const val SORT_TYPE_KEY = "sort_type"

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.eqRecycler.layoutManager = LinearLayoutManager(this)

        WorkerUtil.scheduleSync(this)
        val sortType = getSortType()

        viewModel = ViewModelProvider(this, MainViewModelFactory(application, sortType)).get(MainViewModel::class.java)

        val adapter = EqAdapter(this)
        binding.eqRecycler.adapter = adapter

        viewModel.eqList.observe(this, Observer{
            eqList ->
            adapter.submitList(eqList)

            handleEmptyView(eqList, binding)
        })

        viewModel.status.observe(this, Observer{
            apiResponseStatus ->
            when (apiResponseStatus) {
                ApiResponseStatus.LOADING -> {
                    binding.loadingWheel.visibility = View.VISIBLE
                }
                ApiResponseStatus.ERROR -> {
                    binding.loadingWheel.visibility = View.GONE
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                }
                else -> {
                    binding.loadingWheel.visibility = View.GONE
                }
            }
        })

        adapter.onItemClickListener = {
            Toast.makeText(this, it.place, Toast.LENGTH_SHORT).show()
        }

    }

    private fun getSortType(): Boolean {
        val prefs = getPreferences(MODE_PRIVATE)
        return prefs.getBoolean(SORT_TYPE_KEY, false)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.main_menu_sort_magnitude) {
            viewModel.realoadEqsFromDb(true)
            saveSortType(true)
        } else if (id == R.id.main_menu_sort_time) {
            viewModel.realoadEqsFromDb(false)
            saveSortType(false)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveSortType (sortByMagnitude: Boolean) {
        // getSharedPrefences es para usar las preferencias en cualquier parte de la app
        // pasandole la llave que habriq eu sacar a una varia local como sort_type_key
        // getPreferences es para usar las preferencias en una sola actividad
        val sharedPref = getSharedPreferences("eq_preferences", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(SORT_TYPE_KEY, sortByMagnitude)
    }

    private fun handleEmptyView(
        eqList: MutableList<Earthquake>,
        binding: ActivityMainBinding
    ) {
        if (eqList.isEmpty()) {
            binding.eqEmptyView.visibility = View.VISIBLE
        } else {
            binding.eqEmptyView.visibility = View.GONE
        }
    }
}
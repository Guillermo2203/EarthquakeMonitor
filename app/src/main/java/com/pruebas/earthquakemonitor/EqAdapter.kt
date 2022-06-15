package com.pruebas.earthquakemonitor

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pruebas.earthquakemonitor.databinding.EqListItemBinding

private val TAG = EqAdapter::class.java.simpleName

class EqAdapter(): ListAdapter<Earthquake, EqAdapter.EqViewHolder>(DiffCallback) {

    companion object DiffCallback: DiffUtil.ItemCallback<Earthquake>() {
        override fun areItemsTheSame(oldItem: Earthquake, newItem: Earthquake): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Earthquake, newItem: Earthquake): Boolean {
            return oldItem.id == newItem.id
        }
    }

    lateinit var onItemClickListener: (Earthquake) -> Unit

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): EqViewHolder {
        val binding = EqListItemBinding.inflate(LayoutInflater.from(viewGroup.context))
        return EqViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: EqViewHolder, position: Int) {
        val earthquake = getItem(position)
        viewHolder.bind(earthquake)
    }

    inner class EqViewHolder(private val binding: EqListItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        init {
        }

        fun bind(earthquake: Earthquake) {
            binding.eqMagnitudeText.text = earthquake.magnitude.toString()
            binding.eqPlaceText.text = earthquake.place
            binding.executePendingBindings()
            binding.root.setOnClickListener{
                if (::onItemClickListener.isInitialized) {
                    onItemClickListener(earthquake)
                } else {
                    Log.e(TAG, "onItemClickListener not initialized")
                }
            }
        }
    }
}
package com.example.weatherforcasting.ui.list.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforcasting.R
import com.example.weatherforcasting.data.model.NotificationData
import com.example.weatherforcasting.databinding.OneItemNotificationBinding

class localeNotificationAdaptor(
    // one selection
    var context: Context, var arrayList: ArrayList<NotificationData>,
    val functionCallBack: (NotificationData) -> Unit,
)
// var selectedArrayList: ArrayList<ModelTrip>
    :
    RecyclerView.Adapter<localeNotificationAdaptor.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OneItemNotificationBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(
            binding
        )

    }

    override fun getItemCount(): Int {

        return arrayList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindItems(arrayList[position])

    }

    inner class ViewHolder(val itemViews: OneItemNotificationBinding) :
        RecyclerView.ViewHolder(itemViews.root) {
        fun bindItems(
            currentItem: NotificationData?
        ) {
            itemViews.cardContainer.setOnClickListener{
                functionCallBack(currentItem!!)
            }
            itemViews.hint.text = currentItem?.title?:""
            itemViews.estimatedTime.text =
                context.getString(R.string.estimated_time, currentItem?.time?.toString())

        }


    }

    fun updateList(results: List<NotificationData>) {
        arrayList.addAll(results)
        notifyDataSetChanged()
    }

}
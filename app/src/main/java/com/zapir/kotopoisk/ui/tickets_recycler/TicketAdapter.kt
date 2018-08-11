package com.zapir.kotopoisk.ui.tickets_recycler

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zapir.kotopoisk.R
import com.zapir.kotopoisk.data.model.Ticket
import kotlin.properties.Delegates

class TicketAdapter(var listener: OnItemClickListener) : RecyclerView.Adapter<TicketViewHolder>() {

    var items: ArrayList<Ticket> by Delegates.observable(arrayListOf()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ticket_element, parent,
                false)
        return TicketViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun removeAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }


}


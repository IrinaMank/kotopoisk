package com.example.zapir.kotopoisk.ui.tickets_recycler

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.zapir.kotopoisk.model.Ticket
import kotlin.properties.Delegates
//
//class TicketAdapter : RecyclerView.Adapter<TicketViewHolder>() {
//
//    var items: ArrayList<Ticket> by Delegates.observable(arrayListOf()) { _, _, _ ->
//        notifyDataSetChanged()
//    }
//
//    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
//        holder.bind(items[position])
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_element_info, parent, false)
//        return BookViewHolder(view)
//    }
//
//    override fun getItemCount(): Int {
//        return items.size
//    }
//
//    fun removeAt(position: Int) {
//        items.removeAt(position)
//        notifyItemRemoved(position)
//    }
//
//
//
//}
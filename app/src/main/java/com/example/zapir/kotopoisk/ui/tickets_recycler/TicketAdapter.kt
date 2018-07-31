package com.example.zapir.kotopoisk.ui.tickets_recycler

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.model.Ticket
import kotlinx.android.synthetic.main.ticket_element.view.*
import kotlin.properties.Delegates

class TicketAdapter(private var onClick: (position: Int) -> Unit = {},
                    private var onFavorClick: (view: CheckBox, position: Int) -> Unit) :
        RecyclerView
.Adapter<TicketViewHolder>() {

    var items: ArrayList<Ticket> by Delegates.observable(arrayListOf()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        holder.bind(items[position])
//        holder.itemView.setOnClickListener {
//            onClick(position)
//        }
        holder.itemView.favor_checkbox.setOnClickListener {
            onFavorClick(holder.itemView.favor_checkbox, position)
        }
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


}
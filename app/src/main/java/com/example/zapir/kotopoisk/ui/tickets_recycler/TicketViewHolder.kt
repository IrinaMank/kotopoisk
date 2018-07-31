package com.example.zapir.kotopoisk.ui.tickets_recycler

import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.common.TypesConverter
import com.example.zapir.kotopoisk.model.Ticket
import kotlinx.android.synthetic.main.ticket_element.view.*


class TicketViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    //val favorCheckbox = view.favor_checkbox

    fun bind(ticket: Ticket) {

        itemView.apply {

            Glide.with(this)
                    .load(ticket.photo.url)
                    .into(this.iv_element)

            tv_element_type.text = TypesConverter.getTypeString(ticket.type, itemView.context)
            tv_element_date.text = ticket.date
            tv_element_user.text = ticket.user.nickname
            tv_element_user.paintFlags = tv_element_user.paintFlags or Paint
                    .UNDERLINE_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
            tv_element_is_found.text = if (ticket.isFound) {
                context.getString(R.string
                        .pet_is_found)
            } else {
                context.getString(R.string.pet_not_found)
            }
            if (!ticket.isPublished) {
                tv_element_is_published.visibility = View.VISIBLE
            }
        }

    }
}
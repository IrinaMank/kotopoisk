package com.zapir.kotopoisk.ui.tickets_recycler

import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zapir.kotopoisk.R
import com.zapir.kotopoisk.data.model.Ticket
import com.zapir.kotopoisk.domain.common.TypesConverter
import kotlinx.android.synthetic.main.ticket_element.view.*


class TicketViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(ticket: Ticket, listener: OnItemClickListener) {


        itemView.apply {

            Glide.with(this)
                    .load(ticket.photo.url)
                    .apply(RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.profile_icon)
                            .error(R.drawable.profile_icon).dontAnimate())
                    .into(this.iv_element)

            tv_element_type.text = TypesConverter.getStringFromType(ticket.type, itemView.context)
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
            if (ticket.isFavorite) {
                favor_checkbox.isChecked = true
            }
        }

        itemView.setOnClickListener { listener.onItemClick(ticket) }
        itemView.favor_checkbox.setOnClickListener { listener.onFavorClick(ticket) }

    }
}


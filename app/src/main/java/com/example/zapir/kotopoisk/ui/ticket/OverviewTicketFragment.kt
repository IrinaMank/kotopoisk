package com.example.zapir.kotopoisk.ui.ticket

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.zapir.kotopoisk.KotopoiskApplication
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.data.model.Ticket
import com.example.zapir.kotopoisk.data.model.User
import com.example.zapir.kotopoisk.domain.bottomBarApi.TransactionUtils
import com.example.zapir.kotopoisk.domain.common.SelectedPage
import com.example.zapir.kotopoisk.domain.common.TypesConverter
import com.example.zapir.kotopoisk.ui.base.BaseActivity
import com.example.zapir.kotopoisk.ui.base.BaseFragment
import com.example.zapir.kotopoisk.ui.login.LoginActivity
import com.example.zapir.kotopoisk.ui.main.MainActivity
import com.example.zapir.kotopoisk.ui.profile.MyTicketListFragment
import com.example.zapir.kotopoisk.ui.profile.ProfileFragment
import com.fernandocejas.arrow.optional.Optional
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_ticket_overview.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.concurrent.TimeUnit

class OverviewTicketFragment : BaseFragment() {

    companion object {
        private const val INSTANCE_MESSAGE_KEY = "arguments for OverviewTicketFragment"

        fun newInstance(ticket: Ticket): BaseFragment {
            return OverviewTicketFragment().apply {
                val arguments = Bundle()
                arguments.putParcelable(INSTANCE_MESSAGE_KEY, ticket)
                setArguments(arguments)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_ticket_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back_button.visibility = View.VISIBLE
        back_button.setOnClickListener {
            activity?.onBackPressed()
        }
        toolbar_title.text = getString(R.string.ticket_overview)

        val ticket = arguments?.getParcelable<Ticket>(INSTANCE_MESSAGE_KEY)
                ?: throw RuntimeException("OverviewTicketFragment has no ticket in arguments")

        Glide.with(view)
                .load(ticket.photo.url)
                .apply(RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.profile_icon)
                        .error(R.drawable.profile_icon).dontAnimate())
                .into(overview_ticket_photo)

        animal_type.text = TypesConverter.getStringFromType(ticket.type, getBaseActivity())
        breed.text = TypesConverter.getStringFromBreed(ticket.breed
                ?: throw RuntimeException("ticket ${ticket.id} has no breed"),
                ticket.type, getBaseActivity())
        color.text = TypesConverter.getStringFromColor(ticket.color, getBaseActivity())
        size.text = TypesConverter.getStringFromSize(ticket.size, ticket.type, getBaseActivity())
        furLength.text = TypesConverter.getStringFromFurLength(ticket.furLength, getBaseActivity())
        collar_switch_compat.apply {
            isClickable = false
            isChecked = ticket.hasCollar
        }
        description.text = ticket.overview

        if (!ticket.isPublished) {
            overview_publish_button.visibility = View.VISIBLE
            overview_go_button.visibility = View.GONE
            overview_publish_button.setOnClickListener { publishTicket(ticket) }
        }

        overview_go_button.setOnClickListener { navigateToMap(ticket) }
    }

    private fun publishTicket(ticket: Ticket) {

        initTicket(ticket).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            ticket.isPublished = true
                            ticket.user = it.get()
                            ticketController.publishTicket(ticket)
                                    .timeout(R.integer.timeout.toLong(), TimeUnit.SECONDS)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnSubscribe {
                                        showLoading(true)
                                    }
                                    .doFinally {
                                        showLoading(false)
                                    }
                                    .subscribe(
                                            {
                                                navigateToMap(ticket)
                                                //showToast(getBaseActivity(), "ticket published")
                                            },
                                            {
                                                errorHandler.handleException(it, getBaseActivity())
                                            }
                                    )
                        },
                        {
                            errorHandler.handleException(it, getBaseActivity())
                        }
                )

    }

    private fun initTicket(ticket: Ticket): Single<Optional<User>> {
        val userId = KotopoiskApplication.preferencesManager.getString(LoginActivity.PREFS_ID)
        ticket.user.id = userId
        ticket.photo.userId = userId
        ticket.photo.ticketId = ticket.id
        ticket.type =
                when (animal_type.text) {
                    getString(R.string.cat) -> TypesConverter.getTypeFromString(getString(R.string.cat), getBaseActivity())
                    else -> TypesConverter.getTypeFromString(getString(R.string.dog), getBaseActivity())
                }
        ticket.breed = TypesConverter.getBreedFromString(breed.text.toString(),
                ticket.type, getBaseActivity())
        ticket.color = TypesConverter.getColorFromString(color.text.toString(), getBaseActivity())
        ticket.furLength = TypesConverter.getFurLengthFromString(color.text.toString(), getBaseActivity())
        ticket.hasCollar = collar_switch_compat.isChecked
        ticket.overview = description.text.toString()
        return userController.getUser(userId)
    }

    private fun navigateToMap(ticket: Ticket) {
        KotopoiskApplication.rxBus().postNewTicket(ticket)
        startActivity(MainActivity.newIntent(getBaseActivity(), SelectedPage.MAP))
        getBaseActivity().finish()
    }

    private fun showLoading(show: Boolean) {
        if (show) {
            overview_publish_button.visibility = View.INVISIBLE
            progress_bar.visibility = View.VISIBLE
        } else {
            overview_publish_button.visibility = View.VISIBLE
            progress_bar.visibility = View.GONE
        }
    }

}
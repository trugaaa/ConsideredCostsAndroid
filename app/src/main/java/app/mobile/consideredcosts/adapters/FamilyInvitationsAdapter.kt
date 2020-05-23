package app.mobile.consideredcosts.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.basic.DateFormatter
import app.mobile.consideredcosts.http.models.FamilyInvitation
import kotlinx.android.synthetic.main.item_invitation.view.*

class FamilyInvitationsAdapter(
    private var invitationList: MutableList<FamilyInvitation>,
    val acceptInvitation: (Int, MutableList<FamilyInvitation>) -> Unit,
    val cancelInvitation: (Int, MutableList<FamilyInvitation>) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var cont: Context


    fun updateInvitations(list: MutableList<FamilyInvitation>) {
        invitationList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = invitationList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        cont = parent.context
        val invitations =
            LayoutInflater.from(parent.context).inflate(R.layout.item_invitation, parent, false)
        return when (viewType) {
            TYPE_INVITATION -> InvitationViewHolder(invitations)
            else -> throw Exception()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is InvitationViewHolder) {
            holder.itemView.invitation_accept.setOnClickListener {
                acceptInvitation(position,invitationList)
            }

            holder.itemView.invitation_cancel.setOnClickListener {
                cancelInvitation(position,invitationList)
            }

            try {
                with(invitationList[position]) {
                    holder.itemView.invitation_nickname_value.text = Username
                    holder.itemView.invitation_date_value.text = DateFormatter(cont).dateFromString(Date).toString()
                }
            }
            catch (ex:KotlinNullPointerException)
            {
                holder.itemView.visibility = View.GONE
                Log.e("Crash", ex.stackTrace.toString())
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position < invitationList.size -> TYPE_INVITATION
            else -> throw Exception()
        }
    }

    companion object {
        private const val TYPE_INVITATION = 1
    }
}

class InvitationViewHolder(view: View) : RecyclerView.ViewHolder(view)

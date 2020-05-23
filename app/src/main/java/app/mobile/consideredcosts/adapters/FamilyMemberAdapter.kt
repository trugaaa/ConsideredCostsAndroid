package app.mobile.consideredcosts.adapters

import app.mobile.consideredcosts.http.models.FamilyMember
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.data.DataHolder
import app.mobile.consideredcosts.data.DataHolder.userInfo
import kotlinx.android.synthetic.main.item_member.view.*
import java.lang.Exception

class FamilyMemberAdapter(
    private var membersList: MutableList<FamilyMember>,
    val click: (Int, MutableList<FamilyMember>) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var cont: Context


    fun updateFamilyMembers(list: MutableList<FamilyMember>) {
        membersList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = membersList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        cont = parent.context
        val members =
            LayoutInflater.from(parent.context).inflate(R.layout.item_member, parent, false)
        return when (viewType) {
            TYPE_MEMBER -> MemberViewHolder(members)
            else -> throw Exception()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MemberViewHolder) {
            holder.itemView.member_kick.setOnClickListener {
                click(position, membersList)
            }

            try {
                with(membersList[position]) {
                    holder.itemView.invitation_nickname_value.text = Nickname
                    holder.itemView.member_money_value.text = Money.toString()
                    userInfo.let {
                        holder.itemView.member_currency_value.text =
                            DataHolder.currencyList[userInfo!!.CurrencyId.toInt()].Name
                    }
                    holder.itemView.member_email_value.text = Email
                    holder.itemView.invitation_date_value.text =
                        cont.getString(R.string.userNamePattern, FirstName, SecondName)
                }
            } catch (e: KotlinNullPointerException) {
                e.message.let {
                    holder.itemView.visibility = View.GONE
                    Log.e("Crash caught:", e.message!!)
                }
            } catch (e: NullPointerException) {
                e.message.let {
                    holder.itemView.visibility = View.GONE
                    Log.e("Crash caught:", e.message!!)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position < membersList.size -> TYPE_MEMBER
            else -> throw Exception()
        }
    }

    companion object {
        private const val TYPE_MEMBER = 1
    }
}

class MemberViewHolder(view: View) : RecyclerView.ViewHolder(view)

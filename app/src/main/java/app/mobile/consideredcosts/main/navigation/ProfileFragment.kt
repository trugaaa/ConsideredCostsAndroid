package app.mobile.consideredcosts.main.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.adapters.FamilyInvitationsAdapter
import app.mobile.consideredcosts.adapters.FamilyMemberAdapter
import app.mobile.consideredcosts.data.DataHolder
import app.mobile.consideredcosts.data.SharedPreferencesManager
import app.mobile.consideredcosts.http.RetrofitClient
import app.mobile.consideredcosts.http.models.FamilyCreate
import app.mobile.consideredcosts.http.models.FamilyInvitation
import app.mobile.consideredcosts.http.models.FamilyMember
import app.mobile.consideredcosts.main.navigation.profile.UserActivity
import app.mobile.consideredcosts.sign.PinActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class ProfileFragment : Fragment() {
    private val sharedPreferences by lazy {
        SharedPreferencesManager(context!!)
    }

    private val familyAdapter by lazy {
        FamilyMemberAdapter(mutableListOf()) { position, list ->
            kickUser(position.toLong(), list)
            getFamily()
        }
    }

    private val invitationsAdapter by lazy {
        FamilyInvitationsAdapter(mutableListOf(),
            { position, list ->
                acceptInvitation(position.toLong(), list)
                getUserInvitations()
            },
            { position, list ->
                cancelInvitation(position.toLong(), list)
                getUserInvitations()
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        familyMembersRecyclerView.layoutManager = LinearLayoutManager(context!!)
        familyMembersRecyclerView.adapter = familyAdapter
        familyInvitationsRecyclerView.layoutManager = LinearLayoutManager(context!!)
        familyInvitationsRecyclerView.adapter = invitationsAdapter

        updateUserInfo()
        getFamily()
        getUserInvitations()
    }

    override fun onResume() {
        super.onResume()
        edit_info_profile.setOnClickListener {
            userActivityInvoke()
        }

        logout_profile.setOnClickListener {
            logout()
        }

        create_family_button.setOnClickListener {
            createFamily()
        }

        invite_member_button.setOnClickListener {
            inviteToFamily()
        }

        leave_family_button.setOnClickListener {
            leaveFamily()
        }

        tryAgainProfileButton.setOnClickListener {
            Log.e("Button_Click","Try again button is tapped")
            when {
                DataHolder.currencyList.isNullOrEmpty() -> getCurrencyList()
                DataHolder.userInfo == null -> getUserInfo()
            }
            getFamily()
        }

        deleteFamilyButton.setOnClickListener{
            deleteFamily()
        }
    }

    private fun updateUserInfo() {
        try {
            user_firstName_profile.text = sharedPreferences.getUsername()
            user_secondName_profile.text = sharedPreferences.getUsername()
        } catch (ex: Exception) {
            ex.message.let {
                Log.e("Crash", ex.message!!)
            }
        }
    }

    private fun userActivityInvoke() {
        try {
            startActivity(Intent(context, UserActivity::class.java))
        } catch (ex: Exception) {
            ex.message.let {
                Log.e("Crash", ex.message!!)
            }
        }
    }

    private fun logout() {
        try {
            startActivity(Intent(context, PinActivity::class.java))
            activity!!.finish()
        } catch (ex: Exception) {
            ex.message.let {
                Log.e("Crash", ex.message!!)
            }
        }
    }

    private fun invokeGeneralErrorActivity(errorText: String) {
        val snackBar = Snackbar.make(
            fragment_profile_layout,
            errorText,
            Snackbar.LENGTH_LONG
        )

        snackBar.view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorError))
        snackBar.setActionTextColor(ContextCompat.getColor(context!!, R.color.colorPrimaryText))
        snackBar.show()
    }

    private fun closeKeyboard(context: Context, view: View) {
        try {
            val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (ex: Exception) {
            ex.message.let {
                Log.e(
                    "Crash",
                    "Crash on family from http response to family object parsing\n" + ex.message
                )
            }
        }
    }

    private suspend fun updateLayout() {
        withContext(Dispatchers.Main) {
            when (DataHolder.currencyList.isNullOrEmpty() || DataHolder.userInfo == null) {
                true -> {
                    profile_scroll_layout.visibility = View.GONE
                    error_profile_fragment.visibility = View.VISIBLE
                    success_profile_fragment.visibility = View.GONE
                }
                false -> {
                    profile_scroll_layout.visibility = View.VISIBLE
                    error_profile_fragment.visibility = View.GONE
                    success_profile_fragment.visibility = View.VISIBLE

                    when (DataHolder.hasFamily) {
                        true -> {
                            familyAdapter.updateFamilyMembers(DataHolder.family!!.members)

                            user_has_family_layout.visibility = View.VISIBLE
                            invite_member_profile_layout.visibility = View.VISIBLE
                            family_name.text =
                                context!!.getString(
                                    R.string.familyNamePattern,
                                    DataHolder.family!!.Name
                                )
                            family_founder_value.text = DataHolder.family!!.Creator
                            family_money_value.text = DataHolder.family!!.Money.toString()
                            family_money_currency.text =
                                DataHolder.currencyList.find { currencyElement ->
                                    currencyElement.Id == DataHolder.userInfo!!.CurrencyId.toInt()
                                }!!.Name

                            family_invitations_layout.visibility = View.GONE
                            family_create_layout.visibility = View.GONE
                        }
                        false -> {
                            family_create_layout.visibility = View.VISIBLE
                            invitationsAdapter.updateInvitations(DataHolder.invitationList)
                            if (!DataHolder.invitationList.isNullOrEmpty()) {
                                family_invitations_layout.visibility = View.GONE
                            }
                            user_has_family_layout.visibility = View.GONE
                            invite_member_profile_layout.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun getFamily() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                launch {
                    val response =
                        RetrofitClient.getFamily(sharedPreferences.getToken()!!)
                    when (response.code()) {
                        200 -> {
                            try {
                                DataHolder.family = response.body()!!.data
                                DataHolder.hasFamily = true
                                withContext(Dispatchers.Main) {
                                    updateLayout()
                                }
                            } catch (ex: Exception) {
                                ex.message.let {
                                    Log.e(
                                        "Crash",
                                        "Crash on family from http response to family object parsing\n" + ex.message
                                    )
                                }
                            }
                        }
                        404, 400 -> {
                            DataHolder.family = null
                            DataHolder.hasFamily = false
                            withContext(Dispatchers.Main) {
                                updateLayout()
                            }
                        }
                        401 -> {
                            logout()
                        }
                        504, 503, 502, 501, 500 -> {
                            invokeGeneralErrorActivity(resources.getString(R.string.serverNotAvailable))
                        }
                        else -> {

                            invokeGeneralErrorActivity(
                                response.body()?.firstMessage()
                                    ?: resources.getString(R.string.unknownError)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun createFamily() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                launch {
                    val response =
                        RetrofitClient.createFamily(
                            sharedPreferences.getToken()!!,
                            FamilyCreate(
                                null,
                                create_family_name_field_profile.text.toString(),
                                null
                            )
                        )
                    when (response.code()) {
                        200 -> {
                            try {
                                create_family_name_field_profile.text.clear()
                                closeKeyboard(context!!, create_family_button)
                                getFamily()
                            } catch (ex: Exception) {
                                ex.message.let {
                                    Log.e(
                                        "Crash",
                                        "Crash on family from http response to family object parsing\n" + ex.message
                                    )
                                }
                            }
                        }
                        401 -> {
                            logout()
                        }
                        504, 503, 502, 501, 500 -> {
                            invokeGeneralErrorActivity(resources.getString(R.string.serverNotAvailable))
                        }
                        else -> {
                            invokeGeneralErrorActivity(
                                response.body()?.firstMessage()
                                    ?: resources.getString(R.string.unknownError)
                            )
                        }
                    }

                }
            }
        }
    }

    private fun inviteToFamily() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                launch {
                    val response =
                        RetrofitClient.inviteUser(
                            sharedPreferences.getToken()!!,
                            invite_member_nickname_field.text.toString()
                        )
                    when (response.code()) {
                        200 -> {
                            try {
                                closeKeyboard(context!!, invite_member_button)
                                getFamily()
                            } catch (ex: Exception) {
                                ex.message.let {
                                    Log.e(
                                        "Crash",
                                        "Crash on family from http response to family object parsing\n" + ex.message
                                    )
                                }
                            }
                        }
                        401 -> {
                            logout()
                        }
                        404 -> {
                            invokeGeneralErrorActivity(
                                response.body()?.firstMessage()
                                    ?: resources.getString(R.string.noUserFound)
                            )
                        }
                        504, 503, 502, 501, 500 -> {
                            invokeGeneralErrorActivity(resources.getString(R.string.serverNotAvailable))
                        }
                        else -> {
                            invokeGeneralErrorActivity(
                                response.body()?.firstMessage()
                                    ?: resources.getString(R.string.unknownError)
                            )
                        }
                    }

                }
            }
        }
    }

    private fun getCurrencyList() {
        try {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    launch {
                        val response = RetrofitClient.getCurrencyList()
                        when (response.code()) {
                            200 -> {
                                DataHolder.currencyList = response.body()!!.data!!.list
                                updateLayout()
                            }
                            504, 503, 502, 501, 500 -> {
                                invokeGeneralErrorActivity(resources.getString(R.string.serverNotAvailable))
                            }
                            401 -> {
                                logout()
                            }
                            else -> {
                                invokeGeneralErrorActivity(
                                    response.body()?.firstMessage()
                                        ?: resources.getString(R.string.unknownError)
                                )
                            }
                        }
                    }
                }
            }
        } catch (e: KotlinNullPointerException) {
            e.message.let {
                Log.e("Crash caught:", e.message!!)
            }
        }
    }

    private fun leaveFamily() {
        try {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    launch {
                        val response = RetrofitClient.leaveFamily(sharedPreferences.getToken()!!)
                        when (response.code()) {
                            200 -> {
                                withContext(Dispatchers.Main) {
                                    getFamily()
                                }
                            }
                            504, 503, 502, 501, 500 -> {
                                invokeGeneralErrorActivity(resources.getString(R.string.serverNotAvailable))
                            }
                            401 -> {
                                logout()
                            }
                            else -> {
                                invokeGeneralErrorActivity(
                                    response.body()?.firstMessage()
                                        ?: resources.getString(R.string.unknownError)
                                )
                            }
                        }
                    }
                }
            }
        } catch (e: KotlinNullPointerException) {
            e.message.let {
                Log.e("Crash caught:", e.message!!)
            }
        }
    }

    private fun kickUser(id: Long, list: MutableList<FamilyMember>) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                launch {
                    val response =
                        RetrofitClient.kickUser(sharedPreferences.getToken()!!, list[id.toInt()].Id)
                    when (response.code()) {
                        200 -> {
                            try {
                                getFamily()
                            } catch (ex: Exception) {
                                ex.message.let {
                                    Log.e(
                                        "Crash",
                                        "Crash on family from http response to family object parsing\n" + ex.message
                                    )
                                }
                            }
                        }
                        404 -> {
                            DataHolder.hasFamily = false
                            updateLayout()
                        }
                        401 -> {
                            logout()
                        }
                        504, 503, 502, 501, 500 -> {
                            invokeGeneralErrorActivity(resources.getString(R.string.serverNotAvailable))
                        }
                        else -> {
                            invokeGeneralErrorActivity(
                                response.body()?.firstMessage()
                                    ?: resources.getString(R.string.unknownError)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun cancelInvitation(id: Long, list: MutableList<FamilyInvitation>) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                launch {
                    val response =
                        RetrofitClient.cancelInvitation(
                            sharedPreferences.getToken()!!,
                            list[id.toInt()].Id
                        )
                    when (response.code()) {
                        200 -> {
                            try {
                                getFamily()
                            } catch (ex: Exception) {
                                ex.message.let {
                                    Log.e(
                                        "Crash",
                                        "Crash on family from http response to family object parsing\n" + ex.message
                                    )
                                }
                            }
                        }
                        404 -> {
                            DataHolder.hasFamily = false
                            updateLayout()
                        }
                        401 -> {
                            logout()
                        }
                        504, 503, 502, 501, 500 -> {
                            invokeGeneralErrorActivity(resources.getString(R.string.serverNotAvailable))
                        }
                        else -> {
                            invokeGeneralErrorActivity(
                                response.body()?.firstMessage()
                                    ?: resources.getString(R.string.unknownError)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun acceptInvitation(id: Long, list: MutableList<FamilyInvitation>) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                launch {
                    val response =
                        RetrofitClient.acceptInvitation(
                            sharedPreferences.getToken()!!,
                            list[id.toInt()].Id
                        )
                    when (response.code()) {
                        200 -> {
                            try {
                                getFamily()
                            } catch (ex: Exception) {
                                ex.message.let {
                                    Log.e(
                                        "Crash",
                                        "Crash on family from http response to family object parsing\n" + ex.message
                                    )
                                }
                            }
                        }
                        404 -> {
                            DataHolder.hasFamily = false
                            updateLayout()
                        }
                        401 -> {
                            logout()
                        }
                        504, 503, 502, 501, 500 -> {
                            invokeGeneralErrorActivity(resources.getString(R.string.serverNotAvailable))
                        }
                        else -> {
                            invokeGeneralErrorActivity(
                                response.body()?.firstMessage()
                                    ?: resources.getString(R.string.unknownError)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getUserInfo() {
        try {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    launch {
                        val response = RetrofitClient.getUserInfo(sharedPreferences.getToken()!!)
                        when (response.code()) {
                            200 -> {
                                DataHolder.userInfo = response.body()!!.data
                            }
                            504, 503, 502, 501, 500 -> {
                                invokeGeneralErrorActivity(resources.getString(R.string.serverNotAvailable))
                            }
                            401 -> {
                                logout()
                            }
                            else -> {
                                invokeGeneralErrorActivity(
                                    response.body()?.firstMessage()
                                        ?: resources.getString(R.string.unknownError)
                                )
                            }
                        }
                    }
                }
            }
        } catch (e: KotlinNullPointerException) {
            e.message.let {
                Log.e("Crash caught:", e.message!!)
            }
        }
    }

    private fun getUserInvitations() {
        try {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    launch {
                        val response = RetrofitClient.getInvitations(sharedPreferences.getToken()!!)
                        when (response.code()) {
                            200 -> {
                                DataHolder.invitationList = response.body()!!.data!!.list!!
                                updateLayout()
                            }
                            504, 503, 502, 501, 500 -> {
                                invokeGeneralErrorActivity(resources.getString(R.string.serverNotAvailable))
                            }
                            401 -> {
                                logout()
                            }
                            else -> {
                                invokeGeneralErrorActivity(
                                    response.body()?.firstMessage()
                                        ?: resources.getString(R.string.unknownError)
                                )
                            }
                        }
                    }
                }
            }
        } catch (e: KotlinNullPointerException) {
            e.message.let {
                Log.e("Crash caught:", e.message!!)
            }
        }
    }

    private fun deleteFamily() {
        try {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    launch {
                        val response = RetrofitClient.deleteFamily(sharedPreferences.getToken()!!)
                        when (response.code()) {
                            200 -> {
                                getFamily()
                            }
                            504, 503, 502, 501, 500 -> {
                                invokeGeneralErrorActivity(resources.getString(R.string.serverNotAvailable))
                            }
                            401 -> {
                                logout()
                            }
                            else -> {
                                invokeGeneralErrorActivity(
                                    response.body()?.firstMessage()
                                        ?: resources.getString(R.string.unknownError)
                                )
                            }
                        }
                    }
                }
            }
        } catch (e: KotlinNullPointerException) {
            e.message.let {
                Log.e("Crash caught:", e.message!!)
            }
        }
    }
}

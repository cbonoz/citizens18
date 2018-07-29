package com.refundpal.www.refundpal.fragments


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.refundpal.www.refundpal.R
import com.refundpal.www.refundpal.RefundApplication
import com.refundpal.www.refundpal.models.User
import com.refundpal.www.refundpal.models.transaction.Transaction
import com.refundpal.www.refundpal.models.transaction.TransactionResponse
import com.refundpal.www.refundpal.util.RefundService
import com.refundpal.www.refundpal.util.UserSessionManager
import kotlinx.android.synthetic.main.fragment_profile.*
import net.idik.lib.slimadapter.SlimAdapter
import timber.log.Timber
import java.lang.Float.parseFloat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : BaseMainFragment() {

    protected lateinit var adapter: SlimAdapter
    protected lateinit var layoutManager: LinearLayoutManager
    protected val data: ArrayList<Transaction> = ArrayList()

    protected lateinit var recyclerView: RecyclerView

    @Inject
    lateinit var userSessionManager: UserSessionManager
    @Inject
    lateinit var refundService: RefundService

    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RefundApplication.injectionComponent.inject(this)

        currentUser = userSessionManager.getUser()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_profile, container, false)

        return v
    }

    fun setupTransactionList(transactionResponse: TransactionResponse?) {
        if (transactionResponse == null) {
            Toast.makeText(activity, "Could not get account history, please login again", Toast.LENGTH_SHORT).show()
            userSessionManager.logout()
            return
        }

        var avgDeposit: Float = 0f
        var deposits: Int = 0
        transactionResponse.transactions.map {
            val value = parseFloat(it.details.value.amount)
            if (value > 0) {
                avgDeposit += value
                deposits += 1
            }
        }

        avgDeposit /= deposits


        val targetSdf = SimpleDateFormat("M/dd/yyyy hh:mm:ss aa", Locale.getDefault())
        val utcFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        utcFormat.timeZone = TimeZone.getTimeZone("UTC")
        adapter = SlimAdapter.create()
                .register<Transaction>(R.layout.item_transaction) { data, injector ->
                    val lastUpdatedDate = targetSdf.format(utcFormat.parse(data.details.completed))
                    val color: Int
                    val value = parseFloat(data.details.value.amount)
                    if (value > avgDeposit * 1.5) {
                        color = R.color.md_light_green_200
                    } else {
                        color = R.color.md_white_1000
                    }
                    injector.background(R.id.transactionLayout, color)
                            .text(R.id.title, data.details.description)
                            .text(R.id.timestamp, "Last Updated: $lastUpdatedDate")
                            .text(R.id.amount, "${data.details.value}")
                            .clicked(R.id.transactionLayout) {
                                val detailString = "Clicked transaction: ${data.details.description}: ${data.details.value.amount} ${data.details.value.currency}"
                                Timber.d(detailString)
                                Toast.makeText(activity, detailString, Toast.LENGTH_SHORT).show()
                            }
                }
                .attachTo(this.recyclerView)

        adapter.updateData(transactionResponse.transactions)
        adapter.notifyDataSetChanged()
    }

    private val handler: Handler = Handler()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = recycler_view.apply {
            layoutManager = LinearLayoutManager(activity as Context, LinearLayoutManager.VERTICAL, false)
        }

        setupTransactionList(getTransactionResponse())

        userInfoText.text = "Username: ${currentUser.name}"

        transactionButton.setOnClickListener {
            val phone = getString(R.string.test_phone)
            val amount: Float = 5000f
            val s = currentUser.attributes.get("question2") ?: "25"
            val multString = s.replace("[^a-zA-Z0-9]", "", true);
            val percentage = parseFloat(multString.replace("%", "")) / 100
            val goalName = currentUser.attributes["question1"] ?: "College Saving"
            confirmDepositViaText(phone, amount, percentage, goalName)
        }

    }

    private fun confirmDepositViaText(phone: String, amount: Float, percentage: Float, goalName: String) {
        val amountString = "$%.2f".format(amount)
        val percentageString = "$%f".format(percentage * 100)
        val depositString = "$%.2f".format(amount * percentage)
        val message0 = "Cha-ching! We noticed a large deposit of $amountString. You committed to saving $percentageString% toward your $goalName goal."
        val message1 = "That's $depositString. Reply 'YES' to apply this savings rule."
        val message2 = "Accepted, Congrats! You're $depositString closer to your $goalName goal"
        val message3 = "In the meantime, text 'SAVE' anytime to make an additional deposit toward your $goalName goal"

        refundService.sendSMS(phone, message0)
        refundService.sendSMS(phone, message1)
        handler.postDelayed({
            refundService.sendSMS(phone, message2)
            handler.postDelayed({
                refundService.sendSMS(phone, message3)
            }, 1000)
        }, 10000)
    }
}

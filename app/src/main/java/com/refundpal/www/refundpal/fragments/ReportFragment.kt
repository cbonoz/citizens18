package com.refundpal.www.refundpal.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.refundpal.www.refundpal.R
import android.graphics.Color.parseColor
import com.refundpal.www.refundpal.models.transaction.TransactionResponse
import devlight.io.library.ArcProgressStackView
import kotlinx.android.synthetic.main.fragment_report.*
import java.lang.Float.parseFloat
import java.lang.Math.abs


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ReportFragment : BaseMainFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.fragment_report, container, false)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setBackgroundColor(resources.getColor(R.color.md_light_green_100));

        val response = getTransactionResponse()
        if (response != null) {
            setupArcView(response)
        }
    }

    private fun setupArcView(transactionResponse: TransactionResponse) {
        val models = ArrayList<ArcProgressStackView.Model>()
        // Get colors
        val startColors = resources.getStringArray(R.array.devlight)
        val endColors = resources.getStringArray(R.array.default_preview)
        val bgColors = resources.getStringArray(R.array.medical_express)

        val mStartColors = startColors.map {
            parseColor(it)
        }

        val mBgColors = bgColors.map {
            parseColor(it)
        }

        val mEndColors = endColors.map {
            parseColor(it)
        }

        var netDeposit: Float = 0f
        var netWithdraw: Float = 0f

        val transactions = transactionResponse.transactions
        val volume: Int = transactions.size
        transactions.map {
            val amount = parseFloat(it.details.value.amount)
            if (amount > 0) {
                netDeposit += 1
            } else {
               netWithdraw += 1
            }
        }

        models.add(ArcProgressStackView.Model("Action Rate", 75f, mBgColors[3], mStartColors[3]))
        models.add(ArcProgressStackView.Model("Goal %", 67f, mBgColors[0], mStartColors[0]))
        models.add(ArcProgressStackView.Model("Deposit %", (netDeposit/volume)*100f, mBgColors[1], mStartColors[1]))
        models.add(ArcProgressStackView.Model("Withdraw %", (netWithdraw/volume)*100f, mBgColors[2], mStartColors[2]))

        arcView.setModels(models)
    }


}

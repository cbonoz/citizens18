package com.refundpal.www.refundpal.util

import android.content.Context
import android.telephony.SmsManager
import com.refundpal.www.refundpal.R
import com.refundpal.www.refundpal.models.transaction.Question
import java.util.*
import kotlin.collections.HashMap

class RefundService(private val prefManager: PrefManager, private val context: Context) {

    val questionMap: HashMap<String, Question> = HashMap()

    init {
        setupQuestionBank(context)
    }

    private fun setupQuestionBank(context: Context) {
        questionMap.put("1", context.resources.getStringArray(R.array.question1).toList().createQuestion())
        questionMap.put("2", context.resources.getStringArray(R.array.question2).toList().createQuestion())
        questionMap.put("3", context.resources.getStringArray(R.array.question3).toList().createQuestion())
        questionMap.put("4", context.resources.getStringArray(R.array.question4).toList().createQuestion())
    }

    fun getQuestion(index: Int): Question? {
       return questionMap[index.toString()]
    }

    fun postAuthUrl(): String {
        return "$BASE_URL/my/logins/direct"
    }

    //Sends an SMS message to another device
    fun sendSMS(phoneNumber: String, message: String) {
        val sms = SmsManager.getDefault()
        sms.sendTextMessage(phoneNumber, null, message, null, null)
    }

    fun getTransactionsUrl(bankId: String, accountId: String): String {
        return "$BASE_URL/obp/v3.1.0/my/banks/$bankId/accounts/$accountId/transactions"
    }

    private fun generateUrl(): String {
        return "https://ctzn.ly/login.jsp?ref=${UUID.randomUUID()}"
    }

    companion object {
        val BASE_URL  = "https://citizensbank.openbankproject.com"
    }

}

private fun List<String>.createQuestion(): Question {
    val question = this[0]
    val answers = this.takeLast(this.size - 1)
    return Question(question, answers)
}


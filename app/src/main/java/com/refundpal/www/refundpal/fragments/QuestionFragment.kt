package com.refundpal.www.refundpal.fragments


import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.text.Spanned
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.refundpal.www.refundpal.R
import com.refundpal.www.refundpal.RefundApplication
import com.refundpal.www.refundpal.models.transaction.Question
import com.refundpal.www.refundpal.models.User
import com.refundpal.www.refundpal.util.RefundService
import com.refundpal.www.refundpal.util.UserSessionManager
import kotlinx.android.synthetic.main.fragment_question.*
import javax.inject.Inject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class QuestionFragment : BaseMainFragment() {

    @Inject
    lateinit var refundService: RefundService
    @Inject
    lateinit var userSessionManager: UserSessionManager

    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RefundApplication.injectionComponent.inject(this)

        user = userSessionManager.getUser()
    }

    private var currentQuestion: Question? = null
    private var questionIndex: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_question, container, false)
        val args = arguments
        questionIndex = args!!.getInt(RefundApplication.QUESTION_INDEX)
        currentQuestion = refundService.getQuestion(questionIndex)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupQuestionView()
    }

    private fun setupQuestionView() {
        if (currentQuestion == null) {
            Toast.makeText(activity, "Questionnaire Completed!", Toast.LENGTH_SHORT).show()
            user.attributes.put("done", "true")
            userSessionManager.saveUser(user)
            loadFragmentIntoMain(HomeFragment(), getString(R.string.title_home))
            return
        }

        val q = currentQuestion!!.question
                .replace("%q1", user.attributes["question1"] ?: "College Saving", true)
                .replace("%q3", user.attributes["question3"] ?: "10%", true)
        questionText.text = q

        currentQuestion!!.answers.map {
            addAnswerToView(it)
        }
    }

    private fun addAnswerToView(answer: String) {
        val btn = Button(ContextThemeWrapper(activity, R.style.Widget_AppCompat_Button_Colored), null, 0)

        btn.text = getSpannedText(answer)
        btn.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        btn.setPadding(20, 20, 20, 20)
//        btn.textSize = resources.getDimension(R.dimen.small_text)
        btn.setOnClickListener {
            user.attributes["question$questionIndex"] = answer
            userSessionManager.saveUser(user)
            loadQuestionFragment(questionIndex + 1, getString(R.string.prepare_for_my_refund))
        }
        answerLayout.addView(btn)
    }

//    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation {
//        return if (enter) {
//            MoveAnimation.create(MoveAnimation.UP, enter, 1000)
//        } else {
//            CubeAnimation.create(CubeAnimation.UP, enter, 1000)
//        }
//    }

    private fun getSpannedText(text: String): Spanned {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(text);
        }
    }


}

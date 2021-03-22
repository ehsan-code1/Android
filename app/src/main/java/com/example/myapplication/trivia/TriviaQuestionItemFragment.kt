package com.example.myapplication.trivia

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

/**
 * A simple [Fragment] subclass.
 * Use the [TriviaQuestionItemFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TriviaQuestionItemFragment : Fragment(), View.OnClickListener {
    private val buttonToPositionMap = mapOf(
        // True false mappings
        R.id.t_quiz_answer_true to 0,
        R.id.t_quiz_answer_false to 1,
        // Multiple choice mappings
        R.id.t_quiz_answer_one to 0,
        R.id.t_quiz_answer_two to 1,
        R.id.t_quiz_answer_three to 2,
        R.id.t_quiz_answer_four to 3
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val layoutToUse =
            if (requireArguments().getBoolean(IS_BOOL))   R.layout.fragment_question_bool
            else                                          R.layout.fragment_question_mc

        val self = this
        // Inflate the layout for this fragment
        return inflater
                    .inflate(layoutToUse, container, false)
                    .apply {
                        findViewById<TextView>(R.id.t_quiz_question).text =
                                    requireArguments().getString(QUESTION)

                        val answers =  requireArguments().getSerializable(ANSWERS) as ArrayList<*>
                        buttonToPositionMap
                            .forEach {
                                findViewById<TextView>(it.key)?.apply {
                                        setOnClickListener(self)
                                        text = answers[it.value].toString()
                                    }
                            }
                    }
    }


    /**
     * Returns the index of the answer clicked
     */
    override fun onClick(v: View) {
        val parentActivity = requireActivity()
        // If mobile
        if (parentActivity is TriviaEmptyQuestionActivity) {
            buttonToPositionMap[v.id]?.let {
                parentActivity.returnDataFromFragment(requireArguments().getInt(QUESTION_ID), it)
            }
        }
    }

    companion object {
        const val QUESTION_ID = "questionId"
        const val QUESTION = "question"
        const val ANSWERS = "answers"
        const val IS_BOOL = "isBool"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param questionId The index of the question, as it comes from the adapter
         * @param question The question to display on the fragment
         * @param answers  An ArrayList<String> of all possible answers to display as buttons
         * @param isBooleanQuestion The type (Boolean/Multiple) of question
         * @return A new instance of fragment TriviaQuestionItemFragment.
         */
        @JvmStatic
        fun newInstance(questionId: Int, question: String, answers: ArrayList<String>, isBooleanQuestion: Boolean) =
                TriviaQuestionItemFragment().apply {
                    arguments = Bundle().apply {
                        putInt(QUESTION_ID, questionId)
                        putString(QUESTION, question)
                        putSerializable(ANSWERS, answers)
                        putBoolean(IS_BOOL, isBooleanQuestion)
                    }
                }
    }
}
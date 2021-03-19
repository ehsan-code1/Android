package com.example.myapplication.trivia

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

private const val QUESTION = "question"

/**
 * A simple [Fragment] subclass.
 * Use the [QuestionFragmentBool.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuestionFragmentBool : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var parentActivity: AppCompatActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater
                    .inflate(R.layout.fragment_question_bool, container, false)
                    .apply {
                        findViewById<TextView>(R.id.t_quiz_question).text =
                                    requireArguments().getString(QUESTION)
                    }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentActivity = context as AppCompatActivity
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param question The question to display on the fragment
         * @return A new instance of fragment QuestionFragmentBool.
         */
        @JvmStatic
        fun newInstance(question: String) =
                QuestionFragmentBool().apply {
                    arguments = Bundle().apply {
                        putString(QUESTION, question)
                    }
                }
    }
}
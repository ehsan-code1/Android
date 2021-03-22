package com.example.myapplication.trivia

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.fragment.app.FragmentManager
import com.example.myapplication.R

class TriviaEmptyQuestionActivity : AppCompatActivity(), ReturnDataFromFragment {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trivia_empty_question)

        val extras = intent.extras!!
        val dFragment = TriviaQuestionItemFragment.newInstance(
                            extras.getInt(TriviaQuestionItemFragment.QUESTION_ID),
                            extras.getString(TriviaQuestionItemFragment.QUESTION)!!,
                            extras.getSerializable(TriviaQuestionItemFragment.ANSWERS) as ArrayList<String>,
                            extras.getBoolean(TriviaQuestionItemFragment.IS_BOOL)
                        )

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.t_quiz_frame, dFragment)
            .commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(
            RESULT_OK,
            null
        )
        finish()
    }

    override fun returnDataFromFragment(questionId: Int, pos: Int) {
        setResult(
            RESULT_OK,
            Intent().apply{
                putExtra("questionId", intent.extras!!.getInt("questionId"))
                putExtra("answerIndex", pos)
            }
        )
        finish()
    }
}
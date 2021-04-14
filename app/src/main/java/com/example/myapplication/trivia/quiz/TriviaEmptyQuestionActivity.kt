package com.example.myapplication.trivia.quiz

import android.content.Intent
import android.os.Bundle
import com.example.myapplication.BaseActivityWithDrawer
import com.example.myapplication.R

/**
 * Empty activity for mobile version of QuestionItemFragment. Implements the [ReturnDataFromQuizFragment]
 * interface in order keep a smooth api between mobile and tablet implementations.
 */
class TriviaEmptyQuestionActivity : BaseActivityWithDrawer(), ReturnDataFromQuizFragment {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.t_activity_trivia_empty_question)

        val extras = intent.extras!!
        val dFragment = TriviaQuestionItemFragment.newInstance(
                extras.getInt(TriviaQuestionItemFragment.QUESTION_ID),
                extras.getString(TriviaQuestionItemFragment.QUESTION)!!,
                extras.getStringArrayList(TriviaQuestionItemFragment.ANSWERS)!!,
                extras.getBoolean(TriviaQuestionItemFragment.IS_BOOL)
        )

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.t_quiz_frame, dFragment)
            .commit()
    }

    /**
     * If back is pressed, just exit this activity and return null result bundle
     */
    override fun onBackPressed() {
        super.onBackPressed()
        setResult(
            RESULT_OK,
            null
        )
        finish()
    }

    /**
     * Sets the result of the activity, and finishes.
     * Uses the questionId that was received when this activity was created, and the index of the button selection chosen
     * @param questionId the index of the question, to keep track of which question this activity belongs to
     * @param pos the index of the answer button chosen
     */
    override fun returnDataFromQuizFragment(questionId: Int, pos: Int) {
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
package com.example.myapplication.trivia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.trivia.common.TriviaCommonUtils.Companion.SCORE
import com.example.myapplication.trivia.common.TriviaCommonUtils.Companion.AMOUNT
import com.example.myapplication.trivia.common.TriviaCommonUtils.Companion.DIFFICULTY
import com.example.myapplication.trivia.common.TriviaCommonUtils.Companion.TYPE
import com.google.android.material.snackbar.Snackbar
import com.example.myapplication.trivia.common.TriviaCommonUtils.QuestionDifficulty
import com.example.myapplication.trivia.common.TriviaCommonUtils.QuestionType
import com.example.myapplication.trivia.leaderboard.TriviaActivityLeaderboard
import com.example.myapplication.trivia.quiz.TriviaQuizActivity

/**
 * The first activity launched for the trivia section. This activity accepts the parameters to be used
 * for the trivia quiz. This includes the number of questions, the type of questions (multiple choice and/or boolean),
 * and the difficulty of the questions. These three parameters are to be used to create the api url.
 */
class TriviaActivityLanding : AppCompatActivity() {
    private lateinit var currentToast: Toast // The current toast object. This is replaced if it exists already
    private lateinit var qDifficulty : QuestionDifficulty // The current choice of Question Difficulty
    private lateinit var qType : QuestionType // The current choice of Question Type

    /**
     * Simply set all the listeners for UI components
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.t_activity_trivia_landing)

        /* Set Question Type Listeners */
        findViewById<ToggleButton>(R.id.t_q_type_mc).setOnClickListener(LandingClickListener())
        findViewById<ToggleButton>(R.id.t_q_type_tf).setOnClickListener(LandingClickListener())
        /* Set Question Difficulty Listeners */
        arrayOf(R.id.t_diff_easy_btn, R.id.t_diff_med_btn, R.id.t_diff_hard_btn)
                .forEach {
                    findViewById<ImageButton>(it).setOnClickListener(LandingClickListener())
                }
        /* Set Begin Button Click Listener */
        findViewById<Button>(R.id.t_begin_game_btn).setOnClickListener(LandingClickListener())
    }

    /**
     * A class to handle all click listeners on theTriviaActivityLanding activity
     */
    inner class LandingClickListener : View.OnClickListener {
        /**
         * Click Listener setter. Gets the id of the view whose listener is being set, and
         * sets the appropriate listener.
         */
        override fun onClick(v: View) {
            when (v.id) {
                /* Question Type Listeners */
                R.id.t_q_type_tf, R.id.t_q_type_mc ->
                    qTypeListener()
                /* Difficulty Mode Listeners */
                R.id.t_diff_easy_btn, R.id.t_diff_med_btn, R.id.t_diff_hard_btn ->
                    qDifficultyListener(v.id)
                /* Begin Button Listener */
                R.id.t_begin_game_btn ->
                    beginGameListener()
            }
        }

        /**
         * Sets the click listeners for the Question Type (Multiple Choice and/or True/False)
         */
        private fun qTypeListener() {
            // Set the qType variable with the selected choice, and then pass the appropriate toast message to checkSetShowToast
            val toastMessage =
                    if (findViewById<ToggleButton>(R.id.t_q_type_mc).isChecked
                        && findViewById<ToggleButton>(R.id.t_q_type_tf).isChecked) {
                        qType = QuestionType.BOTH
                        "Multiple Choice & True/False"
                    }
                    else if (findViewById<ToggleButton>(R.id.t_q_type_mc).isChecked) {
                        qType = QuestionType.MC
                        "Multiple Choice"
                    }
                    else if (findViewById<ToggleButton>(R.id.t_q_type_tf).isChecked) {
                        qType = QuestionType.TF
                        "True/False"
                    }
                    else {
                        qType = QuestionType.NONE
                        ""
                    }
            checkSetShowToast(toastMessage)
        }

        /**
         * Checks if a toast already exists, if it does, cancel it, and set the new message, if not empty
         * @param toastMessage the message to display. If blank, it just cancels the current message
         */
        private fun checkSetShowToast(toastMessage: String) {
            // @ symbol is a "qualified this", used to denote specific scopes being used
            if (this@TriviaActivityLanding::currentToast.isInitialized) {
                currentToast.cancel()
                // If we unselect the qType, we don't need to handle the event
                if (toastMessage == "") { return }
            }
            currentToast = Toast.makeText(
                this@TriviaActivityLanding,
                getString(R.string.t_qtype_toast, toastMessage),
                Toast.LENGTH_SHORT
            )
            currentToast.show()
        }

        /**
         * Handles the Question Difficulty Button selections, setting the qDifficulty variable according to selection,
         * and highlighting the choice made by user
         * @param id the layout id of the button clicked
         */
        private fun qDifficultyListener(id: Int) {
            // Set all Difficulty mode buttons to half-alpha
            arrayOf(R.id.t_diff_easy_btn, R.id.t_diff_med_btn, R.id.t_diff_hard_btn)
                    .forEach {
                        findViewById<ImageButton>(it).alpha = 0.5F
                    }
            // Set clicked button alpha to original level, so as to highlight the choice
            findViewById<ImageButton>(id).alpha = 1.0F

            val toastMessage =
                    when (id) {
                        R.id.t_diff_easy_btn -> {
                            qDifficulty = QuestionDifficulty.EASY
                            "Easy Mode"
                        }
                        R.id.t_diff_med_btn -> {
                            qDifficulty = QuestionDifficulty.MEDIUM
                            "Medium Mode"
                        }
                        R.id.t_diff_hard_btn -> {
                            qDifficulty = QuestionDifficulty.HARD
                            "Hard Mode"
                        }
                        else -> ""
                    }

            checkSetShowToast(toastMessage)
        }

        /**
         * Handles the begin game click event. Retrieves values from the number of questions TextView,
         * and the values of the qDifficulty and qType variables. If any of these are not set correctly,
         * a snackbar is created to prompt user to input the values
         */
        private fun beginGameListener() {
            // Ensure number of questions has been entered
            val numQs = findViewById<EditText>(R.id.t_num_qs).text.toString()
            if (numQs == "") {
                Snackbar.make(
                        findViewById(R.id.t_begin_game_btn),
                        "Please enter the number of questions to ask",
                        Snackbar.LENGTH_SHORT
                ).show()
                return
            }
            // Ensure question type(s) has been selected
            if (!this@TriviaActivityLanding::qType.isInitialized || qType == QuestionType.NONE) {
                Snackbar.make(
                        findViewById(R.id.t_begin_game_btn),
                        "Please select the Question Type(s)",
                        Snackbar.LENGTH_SHORT
                ).show()
                return
            }
            // Ensure question difficulty has been selected
            if (!this@TriviaActivityLanding::qDifficulty.isInitialized) {
                Snackbar.make(
                        findViewById(R.id.t_begin_game_btn),
                        "Please select the Question Difficulty",
                        Snackbar.LENGTH_SHORT
                ).show()
                return
            }

            val goToTriviaQuiz = Intent(this@TriviaActivityLanding, TriviaQuizActivity::class.java)
            val dataToPass = Bundle()
                    .apply {
                        putString(AMOUNT, numQs)
                        putInt(TYPE, qType.value)
                        putInt(DIFFICULTY, qDifficulty.value)
                    }
            goToTriviaQuiz.putExtras(dataToPass)
            startActivityForResult(goToTriviaQuiz, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // If data is null, we assume back button pressed
        if (data == null) { return }

        when (requestCode) {
            // Returning from Quiz
            1 -> {
                val quizData = data.extras!!
                Log.i(this.localClassName, "SCORE: ${quizData.getInt(SCORE)}/${quizData.getInt(AMOUNT)}")

                val goToLeaderboards = Intent(this@TriviaActivityLanding, TriviaActivityLeaderboard::class.java)
                val dataToPass = Bundle()
                    .apply {
                        putInt(SCORE, quizData.getInt(SCORE))
                        putInt(AMOUNT, quizData.getInt(AMOUNT))
                        putInt(DIFFICULTY, quizData.getInt(DIFFICULTY))
                        putInt(TYPE, quizData.getInt(TYPE))
                    }
                goToLeaderboards.putExtras(dataToPass)
                startActivityForResult(goToLeaderboards, 2)
            }
        }
    }
}
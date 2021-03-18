package com.example.myapplication.trivia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.android.material.snackbar.Snackbar
import com.example.myapplication.trivia.TriviaCommonUtils.QuestionDifficulty
import com.example.myapplication.trivia.TriviaCommonUtils.QuestionType
import com.example.myapplication.trivia.TriviaCommonUtils.URLCOMPONENTS

class TriviaActivityLanding : AppCompatActivity() {
    private lateinit var currentToast: Toast
    private lateinit var qDifficulty : QuestionDifficulty
    private lateinit var qType : QuestionType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trivia_landing)

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
     * A class to handle all click listeners on the TriviaActivityLanding activity
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
            /* Sets the toast message for the Question Type buttons */
            val toastMessage = setQTypeMessage()

            // Check if a toast already exists, if it does, cancel it
            // @ symbol is a "qualified this", used to denote specific scopes being used
            if (this@TriviaActivityLanding::currentToast.isInitialized) {
                currentToast.cancel()
                if (toastMessage == "") {
                    return
                }
            }
            currentToast = Toast.makeText(
                    this@TriviaActivityLanding,
                    getString(R.string.t_qtype_toast, toastMessage),
                    Toast.LENGTH_SHORT
            )
            currentToast.show()
        }

        /**
         * Checks if the MC and/or TF question type buttons are selected. Sets the qType enum
         * of specified choice
         * @return a string stating which buttons are selected, or an empty string if none are
         */
        private fun setQTypeMessage(): String {
            if (findViewById<ToggleButton>(R.id.t_q_type_mc).isChecked
                && findViewById<ToggleButton>(R.id.t_q_type_tf).isChecked) {
                    qType = QuestionType.BOTH
                    return "Multiple Choice & True/False"
            }
            else if (findViewById<ToggleButton>(R.id.t_q_type_mc).isChecked) {
                qType = QuestionType.MC
                return "Multiple Choice"
            }
            else if (findViewById<ToggleButton>(R.id.t_q_type_tf).isChecked) {
                qType = QuestionType.TF
                return "True/False"
            }
            else {
                qType = QuestionType.NONE
                return ""
            }
        }

        /**
         * Sets the click listeners for the Question Difficulty Buttons
         */
        private fun qDifficultyListener(id: Int) {
            // Set all Difficulty mode buttons to half-alpha
            arrayOf(R.id.t_diff_easy_btn, R.id.t_diff_med_btn, R.id.t_diff_hard_btn)
                    .forEach {
                        findViewById<ImageButton>(it).alpha = 0.5F
                    }
            // Set clicked button alpha to original level, so as to highlight the choice
            findViewById<ImageButton>(id).alpha = 1.0F

            val toastMessage : String
            when (id) {
                R.id.t_diff_easy_btn -> {
                    toastMessage = "Easy Mode"
                    qDifficulty = QuestionDifficulty.EASY
                }
                R.id.t_diff_med_btn -> {
                    toastMessage = "Medium Mode"
                    qDifficulty = QuestionDifficulty.MEDIUM
                }
                R.id.t_diff_hard_btn -> {
                    toastMessage = "Hard Mode"
                    qDifficulty = QuestionDifficulty.HARD
                }
                else -> toastMessage = ""
            }

            // Check if a toast already exists, if it does, cancel it
            if (this@TriviaActivityLanding::currentToast.isInitialized) {
                currentToast.cancel()
                if (toastMessage == "") {
                    return
                }
            }

            currentToast = Toast.makeText(
                    this@TriviaActivityLanding,
                    toastMessage,
                    Toast.LENGTH_SHORT
            )
            currentToast.show()
        }

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
            val questionTypeText =
                    if (!this@TriviaActivityLanding::qType.isInitialized || qType == QuestionType.NONE) {
                        Snackbar.make(
                                findViewById(R.id.t_begin_game_btn),
                                "Please select the Question Type(s)",
                                Snackbar.LENGTH_SHORT
                        ).show()
                        return
                    } else qType
            // Ensure question difficulty has been selected
            val questionDifficultyText =
                    if (!this@TriviaActivityLanding::qDifficulty.isInitialized) {
                        Snackbar.make(
                                findViewById(R.id.t_begin_game_btn),
                                "Please select the Question Difficulty",
                                Snackbar.LENGTH_SHORT
                        ).show()
                        return
                    } else {
                        when (qDifficulty) {
                            QuestionDifficulty.EASY -> "Easy Mode"
                            QuestionDifficulty.MEDIUM -> "Medium Mode"
                            QuestionDifficulty.HARD -> "Hard Mode"
                        }
                    }

            val snackText = "$numQs questions; $questionTypeText; $questionDifficultyText"
            Snackbar.make(
                    findViewById(R.id.t_begin_game_btn),
                    snackText,
                    Snackbar.LENGTH_SHORT
            ).show()

            val goToTriviaQuiz = Intent(this@TriviaActivityLanding, TriviaQuizActivity::class.java)
            val dataToPass = Bundle()
                    .apply {
                        putString(URLCOMPONENTS.AMOUNT, numQs)
                        putInt(URLCOMPONENTS.TYPE, qType.value)
                        putInt(URLCOMPONENTS.DIFFICULTY, qDifficulty.value)
                    }
            goToTriviaQuiz.putExtras(dataToPass)
            startActivity(goToTriviaQuiz)
        }
    }
}
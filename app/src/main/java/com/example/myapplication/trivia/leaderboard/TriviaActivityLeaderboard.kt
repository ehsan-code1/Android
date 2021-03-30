package com.example.myapplication.trivia.leaderboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.trivia.common.TriviaCommonUtils

/**
 * Main activity for the post-quiz scorekeeping. Allows user to input a name and see a summary of their quiz results.
 * Also displays the current leaderboards for the quiz, according to the database on the phone.
 * Uses a FrameLayout/fragment combination to show both the namecard (for user input) and the current leaderboards.
 */
class TriviaActivityLeaderboard : AppCompatActivity(), ReturnDataFromLBFragment {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.t_activity_trivia_leaderboard)

        val data = intent!!.extras!!

        val prescore = data.getInt(TriviaCommonUtils.SCORE)
        val difficulty = data.getInt(TriviaCommonUtils.DIFFICULTY)
        val amount = data.getInt(TriviaCommonUtils.AMOUNT)
        val type = data.getInt(TriviaCommonUtils.TYPE)

        val dFragment = TriviaLeaderboardEntryFragment.newInstance(
                prescore,
                calculateTotalScore(prescore, difficulty),
                difficulty,
                amount,
                type
        )

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.t_leaderboard_frame, dFragment)
            .commit()
    }

    /**
     * Calculates the final score of the user. May use a more complicated formula in the future
     * @param score the prescore, ie., the number of questions answered correctly
     * @param difficulty the difficulty, which is currently used as a multiplier. Based on the [TriviaCommonUtils.QuestionDifficulty] enum
     */
    private fun calculateTotalScore(score: Int, difficulty: Int): Int {
        return score * difficulty
    }

    /**
     * Called on the submission of the [TriviaLeaderboardEntryFragment] fragment
     * @param name the name of the user, to be used in inputting the score into the database
     */
    override fun returnDataFromQuizFragment(name: String) {
        // Make new fragment containing current leaderboards, from database and replace current fragment
    }
}
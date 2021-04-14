package com.example.myapplication.trivia.leaderboard

import android.content.ContentValues
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.BaseActivityWithDrawer
import com.example.myapplication.R
import com.example.myapplication.trivia.common.TriviaCommonUtils

/**
 * Main activity for the post-quiz scorekeeping. Allows user to input a name and see a summary of their quiz results.
 * Also displays the current leaderboards for the quiz, according to the database on the phone.
 * Uses a FrameLayout/fragment combination to show both the namecard (for user input) and the current leaderboards.
 */
class TriviaActivityLeaderboard : BaseActivityWithDrawer(), ReturnDataFromLBFragment {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.t_activity_trivia_leaderboard)

        val data = intent!!.extras!!

        // Check to see if we are simply checking the highscores, or if we are coming from a quiz submission
        if (data.getBoolean("fromLanding")) {
            val dFragment = TriviaLeaderboardListFragment.newInstance()
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.t_leaderboard_frame, dFragment)
                    .commit()
        } else {
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.t_help_menu, menu)
        return true
    }

    /**
     * Displays an alert dialog for the help item and for the about item
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.t_menu_help_item -> {
                AlertDialog.Builder(this)
                        .setPositiveButton("Okay") { _, _ -> }
                        .setTitle("Trivia Instructions")
                        .setMessage(R.string.t_lb_help)
                        .create()
                        .show()
            }
            R.id.t_menu_about_item -> {
                AlertDialog.Builder(this)
                        .setPositiveButton("Okay") { _, _ -> }
                        .setTitle("CST2335 Project: Trivia")
                        .setMessage("Matthew Ellero. V1.5.2")
                        .create()
                        .show()
            }
        }
        return true
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
     * Called on the submission of the [TriviaLeaderboardEntryFragment] fragment.
     * Enters the user data into the database, then swaps the entry fragment for the highscore display fragment.
     * @param dataToReturn a bundle of data including the name, difficulty, and final score for the user
     */
    override fun returnDataFromLBFragment(dataToReturn: Bundle) {
        // Create and place new user info into ContentValue
        val cVals = ContentValues().apply {
            put(TriviaOpener.COL_USERNAME, dataToReturn.getString("name")!!)
            put(TriviaOpener.COL_DIFFICULTY, dataToReturn.getString(TriviaCommonUtils.DIFFICULTY)!!)
            put(TriviaOpener.COL_SCORE, dataToReturn.getInt(TriviaCommonUtils.SCORE))
        }

        // Add ContentValue with user info to db
        TriviaOpener(this).writableDatabase.insert(TriviaOpener.TABLE_NAME, null, cVals)

        // Load in highscores fragment
        val dFragment = TriviaLeaderboardListFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.t_leaderboard_frame, dFragment)
            .commit()
    }
}
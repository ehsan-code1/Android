package com.example.myapplication.trivia.leaderboard

import android.os.Bundle

/**
 * A simple interface to allow for data to be returned from fragments and activities.
 */
interface ReturnDataFromLBFragment {
    /**
     * @param username the name of the user, to be returned from the fragment
     */
    fun returnDataFromLBFragment(dataToReturn: Bundle)
}
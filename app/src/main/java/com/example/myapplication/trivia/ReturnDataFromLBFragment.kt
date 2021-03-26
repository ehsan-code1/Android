package com.example.myapplication.trivia

/**
 * A simple interface to allow for data to be returned from fragments and activities.
 */
interface ReturnDataFromLBFragment {
    /**
     * @param name the name of the user, to be returned from the fragment
     */
    fun returnDataFromQuizFragment(name: String)
}
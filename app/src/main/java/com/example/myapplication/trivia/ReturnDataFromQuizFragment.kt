package com.example.myapplication.trivia

/**
 * A simple interface to allow for data to be returned from fragments and activities.
 */
interface ReturnDataFromQuizFragment {
    /**
     * @param questionId the index to be used in the adapter list of the questions. For tracking which question has been answered
     * @param pos the index of the answer clicked, based on the order of answers shown
     */
    fun returnDataFromQuizFragment(questionId: Int, pos: Int)
}
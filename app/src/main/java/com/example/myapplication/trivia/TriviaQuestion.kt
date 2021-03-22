package com.example.myapplication.trivia

import org.json.JSONArray

/**
 * A wrapper class representing a single Question object. Provides an API for getting various question state,
 * for setting the selected answer, and for checking if the selected answer is correct
 */
class TriviaQuestion(
        category: String,
        type: String,
        question: String,
        correctAnswer: String,
        incorrectAnswers: JSONArray
) {
    private val questionObj = Question(category, type, question, correctAnswer) // An instance of the data class to contain state
    private var isAnswered = false
    private val allAnswers = ArrayList<String>()
    private var correctAnswerIndex = -1
    private var selectedAnswerIndex: Int = -1

    /**
     * Initializes the isAnswered variable, randomly selecting an index to place the correct answer for multiple choice
     */
    init {
        // If the question is a simple boolean, just set the correctAnswerIndex so the mapping of the buttons matches
        if (questionObj.type == "boolean") {
            correctAnswerIndex = if (correctAnswer == "True") 0 else 1
        } else {
            allAnswers.apply {
                val rand = Math.random()
                // Add 1 to incorrect answer length to get total number of answers
                val correctIndex = (rand * (incorrectAnswers.length() + 1)).toInt()

                for (i in (0 until incorrectAnswers.length() + 1)) {
                    if (i == correctIndex) {
                        add(correctAnswer)
                        correctAnswerIndex = i
                    }

                    if (i < incorrectAnswers.length()) {
                        val answer = incorrectAnswers.getString(i)
                        add(answer)
                    }
                }
            }
        }
    }


    // Simple getters
    fun getCategory(): String           { return questionObj.category }
    fun getType(): String               { return questionObj.type }
    fun getQuestion(): String           { return questionObj.question }
    fun getAnswers(): ArrayList<String> { return allAnswers }
    fun numberOfAnswers(): Int          { return allAnswers.size }
    fun getIsAnswered(): Boolean        { return isAnswered }
    fun checkCorrectAnswer(): Boolean   { return selectedAnswerIndex == correctAnswerIndex }

    /**
     * Setter for the selected answer. Allows correct calls to checkCorrectAnswer
     */
    fun setSelectedAnswer(position: Int) {
        selectedAnswerIndex = position
        isAnswered = true
    }
}

/**
 * The actual data class representing the question and its state, as it comes from the async call
 */
private data class Question (
        val category: String,
        val type: String,
        val question: String,
        val correctAnswer: String
)

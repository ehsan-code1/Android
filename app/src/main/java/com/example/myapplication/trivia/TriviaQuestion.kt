package com.example.myapplication.trivia

import org.json.JSONArray
import kotlin.properties.Delegates

class TriviaQuestion(
        category: String,
        type: String,
        question: String,
        correctAnswer: String,
        incorrectAnswers: JSONArray
) {
    private val questionObj = Question(category, type, question, correctAnswer)
    private var isAnswered = false
    private val allAnswers = ArrayList<String>()
    private var correctAnswerIndex = -1
    private var selectedAnswerIndex: Int = -1

    init {
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

    fun getCategory(): String           { return questionObj.category }
    fun getType(): String               { return questionObj.type }
    fun getQuestion(): String           { return questionObj.question }
    fun getAnswers(): ArrayList<String> { return allAnswers }
    fun numberOfAnswers(): Int          { return allAnswers.size }
    fun getIsAnswered(): Boolean        { return isAnswered }
    fun checkCorrectAnswer(): Boolean   { return selectedAnswerIndex == correctAnswerIndex }

    fun setSelectedAnswer(position: Int) {
        selectedAnswerIndex = position
        isAnswered = true
    }


}

private data class Question (
        val category: String,
        val type: String,
        val question: String,
        val correctAnswer: String
)

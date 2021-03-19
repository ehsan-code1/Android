package com.example.myapplication.trivia

import org.json.JSONArray

class TriviaQuestion(
        category: String,
        type: String,
        question: String,
        correctAnswer: String,
        incorrectAnswers: JSONArray
) {

    // TODO: Does difficulty need to be set, per question? Or per quiz?
    private val questionObj = Question(category, type, question, correctAnswer)
    private var isAnswered = false
    private val allAnswers = ArrayList<String>()
    private var selectedAnswerIndex: Int = -1

    init {
        allAnswers.apply {
            add(correctAnswer) // The correct answer index will always be 0
            for (i in (0 until incorrectAnswers.length())) {
                val answer = incorrectAnswers.getString(i)
                add(answer)
            }
        }
    }

    fun getCategory(): String           { return questionObj.category }
    fun getType(): String               { return questionObj.type }
    fun getQuestion(): String           { return questionObj.question }
    fun numberOfAnswers(): Int          { return allAnswers.size }
    fun getIsAnswered(): Boolean        { return isAnswered }
    fun checkCorrectAnswer(): Boolean   { return selectedAnswerIndex == 0 }

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

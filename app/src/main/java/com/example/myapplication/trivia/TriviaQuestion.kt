package com.example.myapplication.trivia

import org.json.JSONArray

data class TriviaQuestion (
        val category: String,
        val type: String,
        val difficulty: String,
        val question: String,
        val correctAnswer: String,
        val incorrectAnswers: JSONArray
)
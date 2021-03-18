package com.example.myapplication.trivia

class TriviaCommonUtils {
    companion object URLCOMPONENTS {
        const val triviaURL = "https://opentdb.com"
        const val AMOUNT = "amount"
        const val TYPE = "type"
        const val DIFFICULTY = "difficulty"
    }

    enum class QuestionDifficulty(val value: Int) {
        EASY (1),
        MEDIUM (2),
        HARD (3);

        companion object {
            fun getValue(value: Int): QuestionDifficulty? {
                return values().find{ it.value == value }
            }
        }
    }

    enum class QuestionType(val value: Int) {
        NONE (0),
        MC (1),
        TF (2),
        BOTH (3);

        companion object {
            fun getValue(value: Int): QuestionType? {
                return values().find{ it.value == value }
            }
        }
    }
}
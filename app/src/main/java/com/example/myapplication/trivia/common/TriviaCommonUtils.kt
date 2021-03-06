package com.example.myapplication.trivia.common

/**
 * A class to contain common utilities used by multiple pieces of the trivia application.
 * Includes:
 * [Companion]: a companion object for constants used in url building, and in object keys
 * [QuestionDifficulty]: an Enum for easy, medium, and hard difficulties
 * [QuestionType]: an Enum for the question type, includes none, multiple choice, boolean (true/false), and both
 */
class TriviaCommonUtils {
    companion object {
        const val triviaURL = "https://opentdb.com"
        const val AMOUNT = "amount"
        const val TYPE = "type"
        const val DIFFICULTY = "difficulty"
        const val SCORE = "score"
    }

    enum class QuestionDifficulty(val value: Int) {
        EASY (1),
        MEDIUM (2),
        HARD (3);

        companion object {
            fun getValue(value: Int): QuestionDifficulty? {
                return values().find { it.value == value }
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
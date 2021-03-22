package com.example.myapplication.trivia

/**
 * A class to contain common utilities used by multiple pieces of the trivia application.
 * Includes:
 * [URLCOMPONENTS]: Companion object of string components to build the url
 * [QuestionDifficulty]: an Enum for easy, medium, and hard difficulties
 * [QuestionType]: an Enum for the question type, includes none, multiple choice, boolean (true/false), and both
 */
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
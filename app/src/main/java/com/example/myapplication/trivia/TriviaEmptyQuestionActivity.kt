package com.example.myapplication.trivia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.myapplication.R

class TriviaEmptyQuestionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trivia_empty_question)

        val dFragment = QuestionFragmentBool.newInstance(intent.extras!!.getString("question")!!)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.t_quiz_frame, dFragment)
            .commit()

        findViewById<Button>(R.id.t_quiz_answer_true)
        findViewById<Button>(R.id.t_quiz_answer_false)
    }
}
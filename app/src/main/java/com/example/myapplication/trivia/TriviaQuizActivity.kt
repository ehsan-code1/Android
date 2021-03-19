package com.example.myapplication.trivia

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.R
import com.example.myapplication.trivia.TriviaCommonUtils.URLCOMPONENTS.AMOUNT
import com.example.myapplication.trivia.TriviaCommonUtils.URLCOMPONENTS.DIFFICULTY
import com.example.myapplication.trivia.TriviaCommonUtils.URLCOMPONENTS.TYPE
import com.example.myapplication.trivia.TriviaCommonUtils.QuestionType
import com.example.myapplication.trivia.TriviaCommonUtils.QuestionDifficulty
import com.example.myapplication.trivia.TriviaCommonUtils.URLCOMPONENTS.triviaURL
import com.google.android.material.snackbar.Snackbar
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.URL

class TriviaQuizActivity : AppCompatActivity() {
    private val testJSON = "{\"response_code\":0,\"results\":[{\"category\":\"Entertainment: Film\",\"type\":\"multiple\",\"difficulty\":\"easy\",\"question\":\"When was the movie &#039;Con Air&#039; released?\",\"correct_answer\":\"1997\",\"incorrect_answers\":[\"1985\",\"1999\",\"1990\"]},{\"category\":\"Science & Nature\",\"type\":\"multiple\",\"difficulty\":\"easy\",\"question\":\"What is the first element on the periodic table?\",\"correct_answer\":\"Hydrogen\",\"incorrect_answers\":[\"Helium\",\"Oxygen\",\"Lithium\"]},{\"category\":\"Entertainment: Video Games\",\"type\":\"boolean\",\"difficulty\":\"easy\",\"question\":\"In &quot;Undertale&quot;, the main character of the game is Sans.\",\"correct_answer\":\"False\",\"incorrect_answers\":[\"True\"]},{\"category\":\"Vehicles\",\"type\":\"multiple\",\"difficulty\":\"easy\",\"question\":\"The LS2 engine is how many cubic inches?\",\"correct_answer\":\"364\",\"incorrect_answers\":[\"346\",\"376\",\"402\"]},{\"category\":\"Vehicles\",\"type\":\"multiple\",\"difficulty\":\"easy\",\"question\":\"What country was the Trabant 601 manufactured in?\",\"correct_answer\":\"East Germany\",\"incorrect_answers\":[\"Soviet Union\",\"Hungary\",\"France\"]}]}"
    private val adapter = MyAdapter()
    private val parsedQuestions = ArrayList<TriviaQuestion>()
    private var totalQuestions = 0
    private lateinit var difficulty: QuestionDifficulty

    // TODO: Add fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trivia_quiz)

        val isTablet = findViewById<FrameLayout>(R.id.t_quiz_frame) != null

        val progressBar = findViewById<ProgressBar>(R.id.t_quiz_progress_bar)
        progressBar.visibility = View.VISIBLE

        /* Get passed data and initialize class members */
        val data = intent.extras!!
        totalQuestions = data.getString(AMOUNT)!!.toInt()
        difficulty = QuestionDifficulty.getValue(data.getInt(DIFFICULTY))!!

        val fetchedJSON = FetchTriviaQuestions().execute(
                buildURL(data.getString(AMOUNT)!!,
                        QuestionType.getValue(data.getInt(TYPE))!!,
                        difficulty)
        )

        val listView = findViewById<ListView>(R.id.t_q_list_view)
        listView.adapter = adapter

        /* Set Click Listener for List View. Currently holds Dialog logic, will eventually
         * hold Fragment logic
         */
        listView.setOnItemClickListener{ _, _, i: Int, _ ->
            val item = adapter.getItem(i)
            val frameToUse =
                if (isTablet)   { R.id.t_quiz_frame }
                else            { R.id.t_quiz_mobile_frame }

            val fragment =
                if (item.getType() == "boolean") QuestionFragmentBool
                else                             QuestionFragmentBool
            val dFragment = fragment.newInstance(item.getQuestion())
            supportFragmentManager
                .beginTransaction()
                .replace(frameToUse, dFragment)
                .commit()
        }

        /* Set Dialog for submit button if not all questions have been answered */
        findViewById<Button>(R.id.t_quiz_submit).setOnClickListener {
            val allQsAnswered = checkAllQuestionsAnswered()
            if (!allQsAnswered) {
                AlertDialog.Builder(this)
                        .setPositiveButton("Yes") { _, _ -> }
                        .setNegativeButton("No") { _, _ -> }
                        .setTitle("Are you sure you want to submit the quiz?")
                        .setMessage("You have not answered all of the questions.")
                        .create()
                        .show()
            }
        }

        /* Test logging of parsed questions */
        for (q in parsedQuestions) {
            Log.i(this.localClassName, q.toString())
        }
    }

    /**
     * Builds the url using the given parameters: Amount of questions, Type of questions,
     * Difficulty of questions
     * @param a a string representing the number of questions to retrieve
     * @param t an enum of QuestionType representing if the url should ask for multiple choice,
     * true/false, or both
     * @param d an enum of QuestionDifficulty stating the difficulty of questions to retrieve
     */
    private fun buildURL(a: String, t: QuestionType, d: QuestionDifficulty): String {
        val type =
                when (t) {
                    QuestionType.MC -> "multiple"
                    QuestionType.TF -> "boolean"
                    else -> ""
                }
        val difficulty =
                when (d) {
                    QuestionDifficulty.EASY -> "easy"
                    QuestionDifficulty.MEDIUM -> "medium"
                    QuestionDifficulty.HARD -> "hard"
                }
        return "$triviaURL/api.php?amount=$a&type=$type&difficulty=$difficulty"
    }

    /**
     * Checks if any questions have not been answered and returns corresponding boolean
     */
    private fun checkAllQuestionsAnswered(): Boolean {
        parsedQuestions.forEach{ q -> if (!q.getIsAnswered()) return false }
        return true
    }

    /**
     * Hardcoded (ie., Synchronous) "fetch" of test URI
     */
    inner class FetchTriviaQuestions: AsyncTask<String, Int, String>() {
        override fun doInBackground(vararg params: String?): String {
            // Test call. Hardcoded json object + size
            testParseJSON(testJSON, 5)
            return "Done"
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            findViewById<ProgressBar>(R.id.t_quiz_progress_bar).visibility = View.VISIBLE
            // Update the progress bar as JSON is parsed
            findViewById<ProgressBar>(R.id.t_quiz_progress_bar).progress = values[0]!!
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            findViewById<ProgressBar>(R.id.t_quiz_progress_bar).visibility = View.INVISIBLE
        }

        /**
         * Hardcoded parsing of test JSON
         */
        private fun testParseJSON(jsonString: String, size: Int) {
            val progressChunk = 100/size

            try {
                val results = (JSONObject(jsonString)).get("results") as JSONArray
                for (i in (0 until size)) {
                    parsedQuestions.add(TriviaQuestion(
                            results.getJSONObject(i)["category"].toString(),
                            results.getJSONObject(i)["type"].toString(),
                            results.getJSONObject(i)["question"].toString(),
                            results.getJSONObject(i)["correct_answer"].toString(),
                            results.getJSONObject(i)["incorrect_answers"] as JSONArray
                    ))

                    publishProgress(progressChunk * i)
                }
            } catch (e: IOException) {
                print("ERROR: $e")
            }
        }
    }

    inner class MyAdapter : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val item = getItem(position)
            return layoutInflater.inflate(
                R.layout.trivia_layout_question, parent, false
            ).apply {
                findViewById<TextView>(R.id.t_quiz_question_number).text =
                            getString(R.string.t_quiz_question_number,  (position + 1).toString())
                findViewById<ImageView>(R.id.t_quiz_question_state).setImageResource(
                        if (!item.getIsAnswered())          R.drawable.unanswered
                        else if (item.checkCorrectAnswer()) R.drawable.correct
                        else                                R.drawable.incorrect
                )
            }
        }

        override fun getItem(position: Int): TriviaQuestion {
            return parsedQuestions[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong() // Hack. Should add ID to Question?
        }

        override fun getCount(): Int {
            return parsedQuestions.size
        }
    }
}
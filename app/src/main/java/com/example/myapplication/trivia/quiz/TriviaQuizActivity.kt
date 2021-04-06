package com.example.myapplication.trivia.quiz

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import com.example.myapplication.R
import com.example.myapplication.trivia.common.TriviaCommonUtils.Companion.AMOUNT
import com.example.myapplication.trivia.common.TriviaCommonUtils.Companion.SCORE
import com.example.myapplication.trivia.common.TriviaCommonUtils.Companion.DIFFICULTY
import com.example.myapplication.trivia.common.TriviaCommonUtils.Companion.TYPE
import com.example.myapplication.trivia.common.TriviaCommonUtils.QuestionType
import com.example.myapplication.trivia.common.TriviaCommonUtils.QuestionDifficulty
import com.example.myapplication.trivia.common.TriviaCommonUtils.Companion.triviaURL
import com.example.myapplication.trivia.common.TriviaQuestion
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.URL

/**
 * Main Quiz activity. Implements a ListView and an Adapter to keep track of its items.
 * Currently uses dummy JSON data, with dummy async calls to populate the list.
 */
class TriviaQuizActivity : AppCompatActivity(), ReturnDataFromQuizFragment {
    private val adapter = QuizAdapter()
    private val parsedQuestions = ArrayList<TriviaQuestion>()
    private var totalQuestions = 0
    private var isTablet: Boolean = false
    private lateinit var dFragment: TriviaQuestionItemFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.t_activity_trivia_quiz)

        isTablet = findViewById<FrameLayout>(R.id.t_quiz_frame) != null

        val progressBar = findViewById<ProgressBar>(R.id.t_quiz_progress_bar)
        progressBar.visibility = View.VISIBLE

        /* Get passed data and initialize class members */
        val data = intent.extras!!
        totalQuestions = data.getString(AMOUNT)!!.toInt()
        val difficulty = QuestionDifficulty.getValue(data.getInt(DIFFICULTY))!!
        val type = QuestionType.getValue(data.getInt(TYPE))!!

        FetchTriviaQuestions().execute(
                buildURL(data.getString(AMOUNT)!!,
                        type,
                        difficulty)
        )

        val listView = findViewById<ListView>(R.id.t_q_list_view)
        listView.adapter = adapter

        /* Set Click Listener for List View. Currently holds Dialog logic, will eventually
         * hold Fragment logic
         */
        listView.setOnItemClickListener { _, _, i: Int, _ ->
            val item = adapter.getItem(i)

            // If already answered, disallow viewing of question
            if (item.getIsAnswered()) return@setOnItemClickListener

            val isBoolean = item.getType() == "boolean"

            if (isTablet) {
                dFragment =
                        TriviaQuestionItemFragment.newInstance(i, item.getQuestion(), item.getAnswers(), isBoolean)

                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.t_quiz_frame, dFragment)
                    .commit()
            } else {

                val goToEmptyTriviaQuestion = Intent(this, TriviaEmptyQuestionActivity::class.java)
                // Put bundle info
                goToEmptyTriviaQuestion.putExtra(TriviaQuestionItemFragment.QUESTION_ID, i)
                goToEmptyTriviaQuestion.putExtra(TriviaQuestionItemFragment.IS_BOOL, isBoolean)
                goToEmptyTriviaQuestion.putExtra(TriviaQuestionItemFragment.QUESTION, item.getQuestion())
                goToEmptyTriviaQuestion.putExtra(TriviaQuestionItemFragment.ANSWERS, item.getAnswers())

                startActivityForResult(goToEmptyTriviaQuestion, 0)
            }
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
            } else {
                // If all questions answered, return the result with the final score
                setResult(
                    1,
                    Intent().apply{
                        putExtra(SCORE, calculatePreScore())
                        putExtra(AMOUNT, totalQuestions)
                        putExtra(DIFFICULTY, difficulty.value)
                        putExtra(TYPE, type.value)
                    }
                )
                finish()
            }
        }

        /* Test logging of parsed questions */
        for (q in parsedQuestions) {
            Log.i(this.localClassName, q.toString())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.t_help_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.t_menu_help_item -> {
                AlertDialog.Builder(this)
                        .setPositiveButton("Okay") { _, _ -> }
                        .setTitle("Trivia Instructions")
                        .setMessage(R.string.t_quiz_help)
                        .create()
                        .show()
            }
            R.id.t_menu_about_item -> {
                AlertDialog.Builder(this)
                        .setPositiveButton("Okay") { _, _ -> }
                        .setTitle("CST2335 Project: Trivia")
                        .setMessage("Matthew Ellero. V1.5.2")
                        .create()
                        .show()
            }
        }
        return true
    }

    /**
     * Calculates the total number of questions answered correctly, known as the "prescore" (ie., before any multipliers)
     */
    private fun calculatePreScore(): Int {
        // Create a 1 || 0 mapping of each answer, then sum across the map
        return parsedQuestions
                    .map { if (it.checkCorrectAnswer()) 1 else 0 }
                    .sum()
    }


    /**
     * Sets the selected answer for the question item, given the question and answer selected
     * @param questionId the index of the question, to keep track of which question this activity belongs to
     * @param pos the index of the answer button chosen
     */
    override fun returnDataFromQuizFragment(questionId: Int, pos: Int) {
        val item = adapter.getItem(questionId)
        item.setSelectedAnswer(pos)
        adapter.notifyDataSetChanged()

        if (isTablet) {
            supportFragmentManager
                .beginTransaction()
                .remove(dFragment)
                .commit()
        }
    }

    /**
     * Chains to the returnDataFromFragment implementation. If the data is null, back was pressed and so just ignore it
     * @param requestCode the request code for the mobile intent
     * @param resultCode the result code returning from the mobile intent for the question item
     * @param data an Intent object that will hold the index of the answer chosen, and the questionId
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data == null) return

        val answerIndex = data.extras?.getInt("answerIndex")!!
        val questionId = data.extras?.getInt("questionId")!!
        Log.i(this.localClassName, "Question: ${adapter.getItem(questionId).getQuestion()}, a: $answerIndex")
        returnDataFromQuizFragment(questionId, answerIndex)
    }

    /**
     * Builds the url using the given parameters: Amount of questions, Type of questions,
     * Difficulty of questions
     * @param amount a string representing the number of questions to retrieve
     * @param type an enum of QuestionType representing if the url should ask for multiple choice,
     * true/false, or both
     * @param difficulty an enum of QuestionDifficulty stating the difficulty of questions to retrieve
     */
    private fun buildURL(amount: String, type: QuestionType, difficulty: QuestionDifficulty): String {
        val typeP =
                when (type) {
                    QuestionType.MC -> "multiple"
                    QuestionType.TF -> "boolean"
                    else -> ""
                }
        val difficultyP =
                when (difficulty) {
                    QuestionDifficulty.EASY -> "easy"
                    QuestionDifficulty.MEDIUM -> "medium"
                    QuestionDifficulty.HARD -> "hard"
                }
        return "$triviaURL/api.php?amount=$amount&type=$typeP&difficulty=$difficultyP"
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
        private val progressBar: ProgressBar = findViewById(R.id.t_quiz_progress_bar)

        override fun doInBackground(vararg params: String?): String {
            Log.i(this.javaClass.simpleName, "Executing async fetch on ${params[0]}")

            try {
                val reader = BufferedReader(
                        InputStreamReader(
                                URL(params[0]).openConnection().getInputStream(),
                                "UTF-8"
                        ), 8)
                val sb = StringBuilder()
                reader.useLines {
                    it.forEach { s -> sb.append(s) }
                }

                val quoteReplacedJson =  sb.toString().replace("&quot;", "&lsquo;")
                val deencodedJson = HtmlCompat.fromHtml(quoteReplacedJson,
                                                        HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                val results = JSONObject(deencodedJson).get("results") as JSONArray

                val size = results.length()
                val progressChunk = size / 100;
                Log.i(this.javaClass.simpleName, "Fetched $size results.")

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
                println("ERROR fetching: ${e.message}")
            }
            return "Done"
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            progressBar.apply {
                visibility = View.VISIBLE
                progress = values[0]!!
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            adapter.notifyDataSetChanged()
            findViewById<ProgressBar>(R.id.t_quiz_progress_bar).visibility = View.INVISIBLE
        }
    }

    /**
     * A private adapter class to handle the ListView of questions
     */
    inner class QuizAdapter : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val item = getItem(position)
            return layoutInflater.inflate(
                R.layout.t_layout_question, parent, false
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
            return position.toLong() // Hack. Should add ID to Question? Not sure if needed
        }

        override fun getCount(): Int {
            return parsedQuestions.size
        }
    }
}
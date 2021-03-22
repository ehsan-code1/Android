package com.example.myapplication.trivia

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

/**
 * Main Quiz activity. Implements a ListView and an Adapter to keep track of its items.
 * Currently uses dummy JSON data, with dummy async calls to populate the list.
 */
class TriviaQuizActivity : AppCompatActivity(), ReturnDataFromFragment {
    private val testJSON = "{\"response_code\":0,\"results\":[{\"category\":\"Entertainment: Film\",\"type\":\"multiple\",\"difficulty\":\"easy\",\"question\":\"When was the movie &#039;Con Air&#039; released?\",\"correct_answer\":\"1997\",\"incorrect_answers\":[\"1985\",\"1999\",\"1990\"]},{\"category\":\"Science & Nature\",\"type\":\"multiple\",\"difficulty\":\"easy\",\"question\":\"What is the first element on the periodic table?\",\"correct_answer\":\"Hydrogen\",\"incorrect_answers\":[\"Helium\",\"Oxygen\",\"Lithium\"]},{\"category\":\"Entertainment: Video Games\",\"type\":\"boolean\",\"difficulty\":\"easy\",\"question\":\"In &quot;Undertale&quot;, the main character of the game is Sans.\",\"correct_answer\":\"False\",\"incorrect_answers\":[\"True\"]},{\"category\":\"Vehicles\",\"type\":\"multiple\",\"difficulty\":\"easy\",\"question\":\"The LS2 engine is how many cubic inches?\",\"correct_answer\":\"364\",\"incorrect_answers\":[\"346\",\"376\",\"402\"]},{\"category\":\"Vehicles\",\"type\":\"multiple\",\"difficulty\":\"easy\",\"question\":\"What country was the Trabant 601 manufactured in?\",\"correct_answer\":\"East Germany\",\"incorrect_answers\":[\"Soviet Union\",\"Hungary\",\"France\"]}]}"
    private val adapter = MyAdapter()
    private val parsedQuestions = ArrayList<TriviaQuestion>()
    private var totalQuestions = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trivia_quiz)

        val isTablet = findViewById<FrameLayout>(R.id.t_quiz_frame) != null

        val progressBar = findViewById<ProgressBar>(R.id.t_quiz_progress_bar)
        progressBar.visibility = View.VISIBLE

        /* Get passed data and initialize class members */
        val data = intent.extras!!
        totalQuestions = data.getString(AMOUNT)!!.toInt()
        val difficulty = QuestionDifficulty.getValue(data.getInt(DIFFICULTY))!!

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
        listView.setOnItemClickListener { _, _, i: Int, _ ->
            val item = adapter.getItem(i)

            // If already answered, disallow viewing of question
            if (item.getIsAnswered()) return@setOnItemClickListener

            val isBoolean = item.getType() == "boolean"

            if (isTablet) {
                val dFragment =
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
            }
        }

        /* Test logging of parsed questions */
        for (q in parsedQuestions) {
            Log.i(this.localClassName, q.toString())
        }
    }

    /**
     * Sets the selected answer for the question item, given the question and answer selected
     * @param questionId the index of the question, to keep track of which question this activity belongs to
     * @param pos the index of the answer button chosen
     */
    override fun returnDataFromFragment(questionId: Int, pos: Int) {
        val item = adapter.getItem(questionId)
        item.setSelectedAnswer(pos)
        adapter.notifyDataSetChanged()
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
        returnDataFromFragment(questionId, answerIndex)
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

    /**
     * A private adapter class to handle the ListView of questions
     */
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
            return position.toLong() // Hack. Should add ID to Question? Not sure if needed
        }

        override fun getCount(): Int {
            return parsedQuestions.size
        }
    }
}
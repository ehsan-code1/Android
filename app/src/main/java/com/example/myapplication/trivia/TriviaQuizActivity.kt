package com.example.myapplication.trivia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import android.widget.TextView
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
    private lateinit var parsedQuestions : ArrayList<TriviaQuestion>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trivia_quiz)

        val data = intent.extras!!
        val url = buildURL(data.getString(AMOUNT)!!,
                            QuestionType.getValue(data.getInt(TYPE))!!,
                            QuestionDifficulty.getValue(data.getInt(DIFFICULTY))!!)
        findViewById<TextView>(R.id.test).text = url

        // Test call. Hardcoded json object + size
        testParseJSON(testJSON, 5)

        for (q in parsedQuestions) {
            Log.i(this.localClassName, q.toString())
        }
    }

    private fun testParseJSON(jsonString: String, size: Int) {

        try {
//            val reader = jsonString.byteInputStream().bufferedReader()
//            val sb = StringBuilder()
//            reader.useLines {
//                it.forEach { s -> sb.append(s) }
//            }
//            Log.i(this.localClassName, sb.toString())

            parsedQuestions = ArrayList<TriviaQuestion>()
            val results = (JSONObject(jsonString)).get("results") as JSONArray
            for (i in (0 until size)) {
                parsedQuestions.add(TriviaQuestion(
                        results.getJSONObject(i)["category"].toString(),
                        results.getJSONObject(i)["type"].toString(),
                        results.getJSONObject(i)["difficulty"].toString(),
                        results.getJSONObject(i)["question"].toString(),
                        results.getJSONObject(i)["correct_answer"].toString(),
                        results.getJSONObject(i)["incorrect_answers"] as JSONArray
                ))
            }
        } catch (e: IOException) {
            print("ERROR: $e")
        }
    }

    /**
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
}
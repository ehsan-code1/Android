package com.example.myapplication.trivia.leaderboard

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.R
import com.example.myapplication.trivia.common.TriviaCommonUtils

private const val PRESCORE = "score"
private const val TOTALSCORE = "totalscore"
private const val DIFFICULTY = "difficulty"
private const val AMOUNT = "amount"
private const val TYPE = "type"

/**
 * A simple [Fragment] subclass.
 * Use the [TriviaLeaderboardEntryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TriviaLeaderboardEntryFragment : Fragment() {
    private var prescore: Int = 0
    private var totalscore: Int = 0
    private var difficulty: Int = 0
    private var amount: Int = 0
    private var type: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Retrieve name from SharedPreferences

        arguments?.let {
            prescore = it.getInt(PRESCORE)
            totalscore = it.getInt(TOTALSCORE)
            difficulty = it.getInt(DIFFICULTY)
            amount = it.getInt(AMOUNT)
            type = it.getInt(TYPE)
        }
    }

    /**
     * Sets the name field using the SharedPreferences interface
     */
    override fun onPause() {
        super.onPause()
        context!!.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .edit()
                .putString("username", view!!.findViewById<EditText>(R.id.t_lb_name).text.toString())
                .apply()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater
                    .inflate(R.layout.t_fragment_trivia_leaderboard_entry, container, false)
                    .apply {
                        val prefs = context!!.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

                        findViewById<EditText>(R.id.t_lb_name).setText(prefs.getString("username", ""))
                        findViewById<TextView>(R.id.t_lb_score).text        = prescore.toString()
                        findViewById<TextView>(R.id.t_lb_totalscore).text   = totalscore.toString()
                        findViewById<TextView>(R.id.t_lb_difficulty).text   = TriviaCommonUtils.QuestionDifficulty.getValue(difficulty).toString()
                        findViewById<TextView>(R.id.t_lb_amount).text       = amount.toString()
                        findViewById<TextView>(R.id.t_lb_type).text         = TriviaCommonUtils.QuestionType.getValue(type).toString()

                        /* Submit button click listener. If no name has been entered, show a simple alert dialog asking for input */
                        findViewById<Button>(R.id.t_lb_submit).setOnClickListener {
                            val name = findViewById<EditText>(R.id.t_lb_name).text.toString()
                            if (name == "") {
                                AlertDialog.Builder(this@TriviaLeaderboardEntryFragment.requireContext())
                                    .setPositiveButton("Okay") { _, _ -> }
                                    .setTitle("Cannot submit score")
                                    .setMessage("Please enter a name.")
                                    .create()
                                    .show()
                                return@setOnClickListener
                            }

                            val dataToReturn = Bundle().apply {
                                putString("name", name)
                                putInt(PRESCORE, totalscore)
                                putString(DIFFICULTY, TriviaCommonUtils.QuestionDifficulty.getValue(difficulty).toString())
                            }


                            (requireActivity() as TriviaActivityLeaderboard).returnDataFromLBFragment(dataToReturn)
                        }
                    }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment TriviaLeaderboardEntryFragment.
         */
        @JvmStatic
        fun newInstance(prescore: Int, totalScore: Int, difficulty: Int, amount: Int, types: Int) =
            TriviaLeaderboardEntryFragment().apply {
                arguments = Bundle().apply {
                    putInt(PRESCORE, prescore)
                    putInt(TOTALSCORE, totalScore)
                    putInt(DIFFICULTY, difficulty)
                    putInt(AMOUNT, amount)
                    putInt(TYPE, types)
                }
            }
    }
}
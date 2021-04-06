package com.example.myapplication.trivia.leaderboard

import android.app.AlertDialog
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.trivia.common.TriviaCommonUtils

/**
 * A simple [Fragment] subclass.
 * Use the [TriviaLeaderboardListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TriviaLeaderboardListFragment : Fragment() {
    private lateinit var db : SQLiteDatabase
    private val adapter = LeaderboardAdapter()
    private val users = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater
            .inflate(R.layout.t_fragment_leaderboard_list, container, false)
            .apply {
                val listView = findViewById<ListView>(R.id.t_lb_highscore_list)
                listView.adapter = adapter

                loadDataFromDatabase()

                listView.setOnItemClickListener { _, _, position, id ->
                    AlertDialog.Builder(context)
                            .setTitle("Do you want to delete this?")
                            .setPositiveButton("Yes") { _, _ ->
                                db.delete(TriviaOpener.TABLE_NAME, "_id=?", arrayOf(id.toString()))
                                users.remove(adapter.getItem(position))
                                adapter.notifyDataSetChanged()
                            }
                            .setNegativeButton("No") {_, _ -> }
                            .setMessage("The selected row is: $position" +
                                    "\nThe database id is: $id")
                            .create()
                            .show()
                }
            }
    }

    private fun loadDataFromDatabase() {
        // Initialize and connect to DB
        db = TriviaOpener(context!!).writableDatabase
        // Retrieve columns
        val cols = TriviaOpener.COLUMNS
        // Query results from db
        val results = db.query(TriviaOpener.TABLE_NAME, cols, null, null,
            null, null, "${TriviaOpener.COL_SCORE} DESC")

        // Get indexes for columns
        val idIdx = results.getColumnIndex(TriviaOpener.COL_ID)
        val usernameIdx = results.getColumnIndex(TriviaOpener.COL_USERNAME)
        val scoreIdx = results.getColumnIndex(TriviaOpener.COL_SCORE)
        val diffIdx = results.getColumnIndex(TriviaOpener.COL_DIFFICULTY)
        // Iterate through results and add messages from DB to list
        while (results.moveToNext()) {
            // TODO: Limit number of items to display?

            val id = results.getLong(idIdx)
            val username = results.getString(usernameIdx)
            val score = results.getInt(scoreIdx)
            val difficulty = results.getString(diffIdx)

            users.add(User(username, score, difficulty, id))
        }
        results.close()
    }

    inner class User(
        val username: String,
        val totalScore: Int,
        val difficulty: String,
        val id: Long
    )

    /**
     * A private adapter class to handle the ListView of questions
     */
    inner class LeaderboardAdapter : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val item: User = getItem(position)

            // TODO: Create layout for leaderboard user
            return layoutInflater.inflate(
                R.layout.t_lb_layout_user, parent, false
            ).apply {
                findViewById<TextView>(R.id.t_lb_highscore_username).text =
                        getString(R.string.t_lb_hs_username,  (position + 1).toString(), item.username)
                findViewById<TextView>(R.id.t_lb_highscore_score).text = item.totalScore.toString()
                findViewById<ImageView>(R.id.t_lb_highscore_difficulty).setImageResource(
                    when (item.difficulty) {
                        "EASY" -> R.drawable.easy
                        "MEDIUM" -> R.drawable.med
                        "HARD" -> R.drawable.hard
                        else -> 0
                    }
                )
            }
        }

        override fun getItem(position: Int): User {
            return users[position]
        }


        override fun getItemId(position: Int): Long {
            return users[position].id
        }

        override fun getCount(): Int {
            return users.size
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment TriviaLeaderboardListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = TriviaLeaderboardListFragment()
    }
}
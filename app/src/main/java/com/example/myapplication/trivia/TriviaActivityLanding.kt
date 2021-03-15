package com.example.myapplication.trivia

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class TriviaActivityLanding : AppCompatActivity() {
    private companion object {
        const val triviaURL = "https://opentdb.com/api.php?"
    }

    private lateinit var currentToast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trivia_landing)

        setQTypeListeners()

    }

    /**
     * Sets the click listeners for the Question Type (Multiple Choice and/or True/False)
     */
    private fun setQTypeListeners() {
        findViewById<ToggleButton>(R.id.q_type_mc).setOnClickListener(LandingClickListener())
        findViewById<ToggleButton>(R.id.q_type_tf).setOnClickListener(LandingClickListener())
    }

    /**
     * A class to handle all click listeners on the TriviaActivityLanding activity
     */
    inner class LandingClickListener : View.OnClickListener {
        /**
         * Click Listener setter. Gets the id of the view whose listener is being set, and
         * sets the appropriate listener.
         */
        override fun onClick(v: View) {
            when (v.id) {
                /* Question Type Listeners */
                R.id.q_type_tf, R.id.q_type_mc -> {
                    /* Sets the toast message for the Question Type buttons */
                    val toastMessage = setQTypeMessage()

                    // Check if a toast already exists, if it does, cancel it
                    // @ symbol is a "qualified this", used to denote specific scopes being used
                    if (this@TriviaActivityLanding::currentToast.isInitialized) {
                        currentToast.cancel()
                        if (toastMessage == "") {
                            return
                        }
                    }
                    currentToast = Toast.makeText(
                            this@TriviaActivityLanding,
                            getString(R.string.t_qtype_toast, toastMessage),
                            Toast.LENGTH_SHORT
                    )
                    currentToast.show()
                }
            }
        }

        /**
         * Checks if the MC and/or TF question type buttons are selected
         * @return a string stating which buttons are selected, or an empty string if none are
         */
        private fun setQTypeMessage(): String {
            return if (findViewById<ToggleButton>(R.id.q_type_mc).isChecked
                    && findViewById<ToggleButton>(R.id.q_type_tf).isChecked)
                "Multiple Choice & True/False"
            else if (findViewById<ToggleButton>(R.id.q_type_mc).isChecked)
                "Multiple Choice"
            else if (findViewById<ToggleButton>(R.id.q_type_tf).isChecked)
                "True/False"
            else
                ""
        }
    }
}
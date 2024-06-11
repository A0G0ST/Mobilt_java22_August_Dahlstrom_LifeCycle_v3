package com.example.lifecycleapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.content.SharedPreferences

class ProfileActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        setContentView(R.layout.activity_profile)

        val ageInput = findViewById<EditText>(R.id.age)
        val hasDriverLicense = findViewById<CheckBox>(R.id.has_driver_license)
        val genderGroup = findViewById<RadioGroup>(R.id.gender_group)
        val emailInput = findViewById<EditText>(R.id.email)
        val submitButton = findViewById<Button>(R.id.submit_button)

        loadSavedData()

        submitButton.setOnClickListener {
            val age = ageInput.text.toString().toIntOrNull() ?: 0
            val hasLicense = hasDriverLicense.isChecked
            val genderId = genderGroup.checkedRadioButtonId
            val email = emailInput.text.toString()


            val editor = sharedPreferences.edit()
            editor.putInt("age", age)
            editor.putBoolean("has_license", hasLicense)
            editor.putInt("gender", genderId)
            editor.putString("email", email)
            editor.apply()


            showSavedData()
        }
    }

    private fun loadSavedData() {
        val age = sharedPreferences.getInt("age", 0)
        val hasLicense = sharedPreferences.getBoolean("has_license", false)
        val gender = sharedPreferences.getInt("gender", -1)
        val email = sharedPreferences.getString("email", "")

        findViewById<EditText>(R.id.age).setText(age.toString())
        findViewById<CheckBox>(R.id.has_driver_license).isChecked = hasLicense
        if (gender != -1) {
            findViewById<RadioGroup>(R.id.gender_group).check(gender)
        }
        findViewById<EditText>(R.id.email).setText(email)

        showSavedData()
    }

    private fun showSavedData() {
        val age = sharedPreferences.getInt("age", 0)
        val hasLicense = sharedPreferences.getBoolean("has_license", false)
        val gender = sharedPreferences.getInt("gender", -1)
        val email = sharedPreferences.getString("email", "")

        val genderText = when (gender) {
            R.id.male -> "Male"
            R.id.female -> "Female"
            else -> "Unknown"
        }

        val message = """
            Age: $age
            Has Driver License: $hasLicense
            Gender: $genderText
            Email: $email
        """.trimIndent()

        findViewById<TextView>(R.id.saved_data).text = message
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (sharedPreferences.getBoolean("is_logged_in", false)) {
            menuInflater.inflate(R.menu.menu_main, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_login -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}





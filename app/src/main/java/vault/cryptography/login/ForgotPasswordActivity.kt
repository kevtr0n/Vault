/**
 * file: ForgotPasswordActivity.kt
 * author: Kevin Hayden
 * course: MSCS 630
 * assignment: Project
 * due date: May 8th, 2018
 * version: 1.0
 *
 * The file that contains the class that
 * houses the methods for authorizing a
 * user to change a password.
 */

package vault.cryptography.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_forgot_password.*
import vault.cryptography.R
import vault.cryptography.banking.Core

class ForgotPasswordActivity : AppCompatActivity() {

  private var firstJob = ""
  private var maiden = ""

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_forgot_password)

    submitButton.setOnClickListener { attemptVerification() }
    cancelText.setOnClickListener { cancel() }
  }

  /**
   * TODO
   */
  private fun attemptVerification() {

    try {

      verifyEmail()

      val jobText: String? = firstJobEditText.text.toString().toUpperCase()
      val maidenText: String? = maidenEditText.text.toString().toUpperCase()


      if (maiden == maidenText && firstJob == jobText) {
        val intent = Intent(ForgotPasswordActivity@ this, ChangePasswordActivity::class.java)
        Log.d("Success: ", "true")
        startActivity(intent)
        finish()
      } else {
        Log.d("Success: ", "false")
      }
    } catch (ex: Exception) {
      Log.d("Exception: ", ex.toString())
    }
  }

  /**
   * TODO
   */
  private fun verifyEmail() {

    val email = emailEditTxt.text.toString().replace("[@.]".toRegex(), "").toUpperCase()
    Core.getInstance().email = email

    val database = FirebaseDatabase.getInstance()
    val reference = database.getReference("users")

    reference.addValueEventListener(object : ValueEventListener {

      override fun onDataChange(snapshot: DataSnapshot?) {
        if (snapshot!!.hasChild(email)) {
          firstJob = snapshot.child(email).child("firstJob").value.toString().toUpperCase()
          maiden = snapshot.child(email).child("maidenName").value.toString().toUpperCase()
        }
      }

      override fun onCancelled(error: DatabaseError?) {
        Log.d("Error: ", error.toString())
      }
    })
  }

  /**
   * TODO
   */
  private fun cancel() {
    val intent = Intent(ForgotPasswordActivity@ this, LoginActivity::class.java)
    startActivity(intent)
    finish()
  }
}

/**
 * file: ChangePasswordActivity.kt
 * author: Kevin Hayden
 * course: MSCS 630
 * assignment: Project
 * due date: May 8th, 2018
 * version: 1.0
 *
 * The file that contains the class and
 * methods for changing a user password.
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
import kotlinx.android.synthetic.main.activity_change_password.*
import vault.cryptography.R
import vault.cryptography.banking.Core
import vault.cryptography.banking.models.User
import vault.cryptography.encryption.AESCipher

class ChangePasswordActivity : AppCompatActivity() {

  private var key = ""
  private var canChange = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_change_password)

    changePasswordButton.setOnClickListener { verifyMatch() }
    cancelText.setOnClickListener { cancel() }
  }

  /**
   * Verifies that the two input passwords
   * are the same string.
   */
  private fun verifyMatch() {

    val password = passwordPromptEditText.text.toString()
    val confirm = passwordConfirmEditText.text.toString()

    when (password == confirm) {
      true -> {
        try {
          changePassword(password.toUpperCase())
        }

        catch (ex: Exception) {
          Log.d("Ex: ", ex.toString())
        }
      }
      false -> passwordConfirmEditText.error = "PASSWORDS DO NOT MATCH"
    }
  }

  /**
   * Changes the user's password in the database.
   * @param password the new password
   */
  private fun changePassword(password: String) {

    val email = Core.getInstance().email
    val database = FirebaseDatabase.getInstance()
    val reference = database.getReference("users/$email")
    val key = User.generateRandomKey()
    val aesCipher = AESCipher()
    val authenticationKey = aesCipher.encrypt("$email:$password", key)

    try {
      reference.child("authenticationKey").setValue(authenticationKey)
      reference.child("key").setValue(key)
      cancel()
    } catch (ex: Exception) {
      Log.d("Exception: ", ex.toString())
    }
  }

  /**
   * Cancels the current operation and
   * returns to the login screen.
   */
  private fun cancel() {
    val intent = Intent(ChangePasswordActivity@this, LoginActivity::class.java)
    startActivity(intent)
    finish()
  }
}

/**
 * file: LoginActivity.kt
 * author: Kevin Hayden
 * course: MSCS 630
 * assignment: Project
 * due date: May 8th, 2018
 * version: 1.0
 *
 * The class that houses the functions
 * for logging into the banking application.
 */
package vault.cryptography.login

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.content.Intent
import android.util.Log
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import vault.cryptography.R
import vault.cryptography.registration.RegisterActivity
import vault.cryptography.banking.Core
import vault.cryptography.banking.MainActivity
import vault.cryptography.banking.models.User
import vault.cryptography.encryption.AESCipher

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {

  private var core: Core = Core.getInstance()

  /**
   *
   */
  override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
//    TEST.visibility = View.GONE

    email_sign_in_button.setOnClickListener { attemptLogin() }
    register_button.setOnClickListener { register() }
    forgotPassword.setOnClickListener { forgotPassword() }
  }


  /**
   * Attempts to sign in or register the account specified by the login form.
   * If there are form errors (invalid email, missing fields, etc.), the
   * errors are presented and no actual login attempt is made.
   */
  private fun attemptLogin() {

    // Reset errors.
    email.error = null
    password.error = null

    // Store values at the time of the login attempt.
    val emailStr = email.text.toString()
    val passwordStr = password.text.toString()

    var cancel = false
    var focusView: View? = null

    // Check for a valid password, if the user entered one.
    if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
      password.error = getString(R.string.error_invalid_password)
      focusView = password
      cancel = true
    }

    // Check for a valid email address.
    if (TextUtils.isEmpty(emailStr)) {
      email.error = getString(R.string.error_field_required)
      focusView = email
      cancel = true
    } else if (!isEmailValid(emailStr)) {
      email.error = getString(R.string.error_invalid_email)
      focusView = email
      cancel = true
    }

    if (cancel) {
      // There was an error; don't attempt login and focus the first
      // form field with an error.
      focusView?.requestFocus()
    } else {
      // Show a progress spinner, and kick off a background task to
      // perform the user login attempt.

      authenticate(emailStr, passwordStr)

      try {
        if (core.user != null) {
          val intent = Intent(this@LoginActivity, MainActivity::class.java)
          startActivity(intent)
          finish()
        }
      }

      catch (ex: Exception) {
        Log.d("Exception: ", ex.toString())
      }
    }
  }

  private fun isEmailValid(email: String): Boolean {
    return email.contains("@")
  }

  private fun isPasswordValid(password: String): Boolean {
    return password.length > 4
  }

  /**
   * Takes the email/password input and attempts to authenticate
   * the user.
   */
  private fun authenticate(email: String, password: String): Boolean {

    // Formats the email and password:
    val mEmail = email.replace("[@.]".toRegex(), "").toUpperCase()
    val mPassword = password.toUpperCase()
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val reference: DatabaseReference = database.getReference("users/$mEmail")
    val aesCipher = AESCipher()
    var passwordMatch = false

    reference.addValueEventListener(object : ValueEventListener {

      override fun onDataChange(snapshot: DataSnapshot?) {
        val key = snapshot!!.child("key").value.toString()
        val aesAuthenticationKey = snapshot.child("authenticationKey").value.toString()
        val aesEncryptionKey = aesCipher.encrypt("$mEmail:$mPassword", key)
        if (aesAuthenticationKey == aesEncryptionKey) {
          passwordMatch = true
          core.user = User(
                  snapshot.child("firstName").value.toString(),
                  snapshot.child("lastName").value.toString(),
                  snapshot.child("birthDate").value.toString(),
                  snapshot.child("email").value.toString(),
                  snapshot.child("key").value.toString(),
                  snapshot.child("userID").value.toString(),
                  snapshot.child("authenticationKey").value.toString(),
                  snapshot.child("maidenName").value.toString(),
                  snapshot.child("firstJob").value.toString()
          )
        } else {
          passwordMatch = false
          Log.d("Error: ", snapshot.toString())
        }
      }

      override fun onCancelled(error: DatabaseError?) {
        Log.d("Error: ", error.toString())
      }
    })

    return passwordMatch
  }

  /**
   *
   */
  private fun register() {
    val mIntent = Intent(this@LoginActivity, RegisterActivity::class.java)
    startActivity(mIntent)
    finish()
  }

  /**
   *
   */
  private fun forgotPassword() {
    val mIntent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
    startActivity((mIntent))
    finish()
  }
}
/**
 * file: RegisterActivity.kt
 * author: Kevin Hayden
 * course: MSCS 630
 * assignment: Project
 * due date: May 8th, 2018
 * version: 1.0
 *
 * The class that houses the code for
 * user registration.
 */

package vault.cryptography.registration

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register.*
import vault.cryptography.R
import vault.cryptography.banking.Core
import vault.cryptography.banking.models.User
import vault.cryptography.encryption.AESCipher
import vault.cryptography.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

  private var validCredentials = false
  private var user: User? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_register)

    registerButton.setOnClickListener { attemptRegister() }
    cancelText.setOnClickListener { cancel() }
  }

  /**
   * Attempts to register user and add it to
   * the database.
   */
  private fun attemptRegister() {

    generateUser()
    if (validCredentials)
      register(user!!)
  }

  /**
   * Creates user and checks to see if credentials
   * are valid.
   */
  private fun generateUser() {
    val fn = firstNameText.text.toString()
    val ln = lastNameText.text.toString()
    val bd = birthDate.text.toString()
    val em = emailText.text.toString()
    val pw = passwordText.text.toString()
    val cf = confirmPassword.text.toString()
    val id = userIDText.text.toString()
    val key = User.generateRandomKey()
    val aes = AESCipher()
    val emE = em.replace("[@.]".toRegex(), "").toUpperCase()
    val pwE = pw.toUpperCase()
    user = User(fn, ln, bd, em, key, id, aes.encrypt("$emE:$pwE", key), null, null)
    validCredentials = isValidPersonalInfo(fn, ln, bd) &&
            isValidEmail(em) && isValidPassword(pw, cf)
  }

  private fun isValidPersonalInfo(fn: String, ln: String, bd: String): Boolean {
    return fn.isNotEmpty() && ln.isNotEmpty() && bd.isNotEmpty()
  }

  /**
   * Search database for duplicate email and make sure
   * email is in correct format.
   *
   * @param email the email to be checked.
   * @return whether or not email is valid.
   */
  private fun isValidEmail(email: String): Boolean {
    var exists = false
    val pKey = email.replace("[@.]".toRegex(), "").toUpperCase()
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val reference: DatabaseReference = database.getReference("users")

    reference.addListenerForSingleValueEvent(object : ValueEventListener {

      /**
       * Sets flag to true if email exists.
       * @param snapshot the current snapshot of the database.
       */
      override fun onDataChange(snapshot: DataSnapshot?) {
        if (!snapshot!!.hasChild(pKey))
          exists = true
      }

      /**
       * Logs an error on cancel.
       * @param error the error to be logged.
       */
      override fun onCancelled(error: DatabaseError?) {
        Log.d("Error: ", error.toString())
      }
    })
    return (email.contains("@") && email.length > 8 && !exists)
  }

  /**
   * Returns boolean based on if password is valid.
   *
   * @param password the password to be checked.
   * @return whether or not password is valid.
   */
  private fun isValidPassword(password: String, confirm: String): Boolean {
    return (password.length > 8 && (password == confirm))
  }

  /**
   * Registers user in database and proceeds to setting
   * up to password recovery.
   * @param user the User to be added.
   */
  private fun register(user: User) {

    Core.getInstance().user = user
    val intent = Intent(RegisterActivity@this, PasswordRecoveryActivity::class.java)
    startActivity(intent)
    finish()
  }

  /**
   * Cancels registration and returns to login.
   */
  private fun cancel() {
    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
    startActivity(intent)
    finish()
  }
}

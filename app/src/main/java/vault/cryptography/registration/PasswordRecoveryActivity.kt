/**
 * file: PasswordRecoveryActivity.kt
 * author: Kevin Hayden
 * course: MSCS 630
 * assignment: Project
 * due date: May 8th, 2018
 * version: 1.0
 *
 * The class that houses the functions for setting
 * up a users password recovery protocol.
 */
package vault.cryptography.registration

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_password_recovery.*
import vault.cryptography.R
import vault.cryptography.banking.Core
import vault.cryptography.banking.MainActivity

class PasswordRecoveryActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_password_recovery)

    submitButton.setOnClickListener { attemptSubmit() }
    backText.setOnClickListener { back() }
  }

  /**
   * Tries to submit the User to the
   * database.
   */
  private fun attemptSubmit() {

    if (validInput()) {
      try {
        submit()
      }

      catch (ex: Exception) {
        Log.d("Exception: ", ex.toString())
      }
    }
  }

  /**
   * Checks to see if the text fields are null.
   * @return a boolean value.
   */
  private fun validInput(): Boolean {
    return (firstJobEdit.text != null && maidenEdit.text != null)
  }

  /**
   * Pushes the User to the database.
   */
  private fun submit() {

    Core.getInstance().user.firstJob = firstJobEdit.text.toString()
    Core.getInstance().user.maidenName = maidenEdit.text.toString()
    Core.getInstance().user.push()

    val intent = Intent(PasswordRecoveryActivity@this, MainActivity::class.java)
    startActivity(intent)
    finish()
  }

  /**
   * Returns to the previous page.
   */
  private fun back() {
    val intent = Intent(PasswordRecoveryActivity@this, RegisterActivity::class.java)
    startActivity(intent)
    finish()
  }
}
